package cz.plesioEngine.entities.weapons;

import cz.plesioEngine.audio.Source;
import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import cz.plesioEngine.renderEngine.particles.SpreadParticleSystem;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.toolbox.MousePicker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class WeaponEntity extends Entity implements Weapon {

	private Projectile projectileType;
	private List<Projectile> projectiles = new ArrayList<>();

	private SpreadParticleSystem weaponFireParticles;
	
	private float originalZ = 0;
	
	private int currentAmmo;
	private int ammoCapacity;

	private float fireCooldown = 0.1f;
	private float currentCooldown = 0;

	private float baseWeaponDamage = 500;
	private float baseDistanceMultiplier = 0.02f;

	private float fireParticleSpread;

	private Source fireAudioSource = new Source();
	private Source hitEffectAudioSource = new Source();

	private int fireSoundEffectBuffer;
	private int weaponHitSoundEffectBuffer;

	public WeaponEntity(TexturedMesh texturedMesh, Vector3f position, float rotX,
		float rotY, float rotZ, float scale, Texture weaponParticle,
		float particleAmount, float particleSpeed, float particleGravity,
		float particleLifetime, float fireParticleSpread) {
		super(texturedMesh, position, rotX, rotY, rotZ, scale);
		weaponFireParticles = new SpreadParticleSystem(particleAmount, particleSpeed,
			particleGravity, particleLifetime, weaponParticle);
		this.fireParticleSpread = fireParticleSpread;
		hitEffectAudioSource.setVolume(2f);
	}

	public void setProjectileType(Projectile projectile) {
		this.projectileType = projectile;
	}
	
	public void moveForward(){
		if(getPosition().z() > originalZ){
			getPosition().z -= 0.01f;
		}
	}

	public void shoot() {
		Vector3f pickerRay = MousePicker.getCurrentRay();

		weaponFireParticles.generateParticles(projectileType.getProjectileOffset(),
			pickerRay, fireParticleSpread);

		currentCooldown = fireCooldown;

		//Camera.accelerate(pickerRay.negate(),
		//		1 + Camera.getDirection().lengthSquared(), 60);
		fireAudioSource.play(fireSoundEffectBuffer);
		
		currentAmmo -= 1;
		
		getPosition().z += 0.8f;
		Camera.setPitch(Camera.getPitch() - 5);
		
	}
	
	public void addAmmo(int amount){
		if(currentAmmo + amount <= ammoCapacity){
			currentAmmo += amount;
		}
	}
	
	public void addAmmoRand(int limit){
		Random r = new Random();
		int amount = r.nextInt(limit + 1);
		if(currentAmmo + amount <= ammoCapacity){
			currentAmmo += amount;
		}
	}

	@Override
	public Projectile getProjectileType() {
		return projectileType;
	}

	@Override
	public List<Projectile> getCurrentProjectiles() {
		return Collections.unmodifiableList(projectiles);
	}

	public float getFireRate() {
		return fireCooldown;
	}

	public void setFireCooldown(float fireRate) {
		this.fireCooldown = fireRate;
	}

	public float getCurrentCooldown() {
		return currentCooldown;
	}

	public void setCurrentCooldown(float currentCooldown) {
		this.currentCooldown = currentCooldown;
	}

	public void removeProjectile(Projectile p) {
		this.projectiles.remove(p);
	}

	public void setProjectileOffset(float offset) {
		this.getProjectileType().setProjectileOffset(offset);
	}

	public void setProjectileSize(float size) {
		this.getProjectileType().setScale(size);
	}

	public SpreadParticleSystem getWeaponParticleSystem() {
		return weaponFireParticles;
	}

	public void setFireSoundEffect(int buffer) {
		this.fireSoundEffectBuffer = buffer;
	}

	public void setHitSoundEffect(int buffer) {
		this.weaponHitSoundEffectBuffer = buffer;
	}

	public void playWeaponHitEffect() {
		this.hitEffectAudioSource.play(weaponHitSoundEffectBuffer);
	}

	public Source getAudioSource() {
		return fireAudioSource;
	}

	public float getWeaponDamage(float distance) {
		float alteredDamage = (float) (baseWeaponDamage * Math.pow(Math.E,
			-distance * baseDistanceMultiplier));
		return alteredDamage;
	}

	public int getCurrentAmmo() {
		return currentAmmo;
	}

	public void setCurrentAmmo(int currentAmmo) {
		this.currentAmmo = currentAmmo;
	}

	public int getAmmoCapacity() {
		return ammoCapacity;
	}

	public void setAmmoCapacity(int ammoCapacity) {
		this.ammoCapacity = ammoCapacity;
	}
	
}
