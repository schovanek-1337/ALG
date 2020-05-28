package cz.plesioEngine.entities.enemies;

import cz.plesioEngine.audio.AudioMaster;
import cz.plesioEngine.audio.Source;
import cz.plesioEngine.entities.enemies.AI.AIMaster;
import cz.plesioEngine.entities.enemies.AI.EnemyAI;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.renderEngine.DisplayManager;
import cz.plesioEngine.renderEngine.particles.GibletEmmiter;
import cz.plesioEngine.renderEngine.particles.HealthPickupEmmiter;
import cz.plesioEngine.renderEngine.particles.SpreadParticleSystem;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import org.joml.Vector3f;

/**
 * Bad boy.
 *
 * @author plesio
 */
public class Enemy extends Entity {

	private Vector3f velocity = new Vector3f(0, 0, 0);
	private Vector3f forwardDirection = new Vector3f();
	private Vector3f forwardFace = new Vector3f();

	private EnemyAI currentAI;

	private float speedCap = 0.7f;
	private float decelerationFactor = 0.1f;
	private float acceleration = 80;

	private float AABBRegenerationCD = 2;
	private float AABBCurrentCD = 0;

	private float currentHealth = 100;
	private SpreadParticleSystem blood;
	private Source gruntAudioSource = new Source();
	private int gruntAudioBuffer = AudioMaster.loadSound("res/sound/monster/monster-grunts.ogg");

	private GibletEmmiter gibEmmiter
		= new GibletEmmiter(
			"res/sound/gore-01.ogg",
			"res/sound/gore-02.ogg",
			"res/sound/gore-03.ogg",
			"res/sound/gore-04.ogg",
			"res/sound/gore-05.ogg",
			"res/sound/gore-06.ogg",
			"res/sound/gore-07.ogg",
			"res/sound/gore-08.ogg",
			"res/sound/gore-09.ogg",
			"res/sound/gore-10.ogg",
			"res/sound/gore-11.ogg",
			"res/sound/gore-12.ogg",
			"res/sound/gore-13.ogg",
			"res/sound/gore-14.ogg",
			"res/sound/gore-15.ogg",
			"res/sound/gore-16.ogg");

	private HealthPickupEmmiter hpe
		= new HealthPickupEmmiter();

	private Source deathSoundSource = new Source();

	public Enemy(TexturedMesh texturedMesh, Vector3f position, float rotX,
		float rotY, float rotZ, float scale, Vector3f forwardDirection, float health) {
		super(texturedMesh, position, rotX, rotY, rotZ, scale);
		this.forwardDirection = forwardDirection;
		this.forwardFace = new Vector3f(forwardDirection);
		this.currentAI = new EnemyAI(this);
		this.currentHealth = health;
		this.blood = new SpreadParticleSystem(300, 60, 60, 4,
			TextureMaster.requestTexture("blood"));
	}

	public Enemy(Enemy e) {
		super(e.getTexturedModel(), e.getPosition(), e.getRotX(), e.getRotY(),
			e.getRotZ(), e.getScale());
		this.forwardDirection = new Vector3f(e.getForwardDirection());
		this.forwardFace = new Vector3f(this.forwardDirection);
		this.currentAI = new EnemyAI(this);
		this.currentHealth = e.getHealth();
		this.blood = e.getBlood();
	}

	public void register() {
		AIMaster.processEnemy(this);
		EntityRenderer.processEntity(this);
		PhysicsEngine.processEntity(this);
	}

	public void unregister() {
		AIMaster.removeEnemy(this);
		EntityRenderer.removeEntity(this);
		PhysicsEngine.removeEntity(this);
	}

	public void update() {
		move();
		updateAudio();
	}

	private void move() {
		accelerate(new Vector3f(0, -1, 0), speedCap, acceleration * 0.2f);
		PhysicsEngine.checkEnemyMovement(this, velocity);
		Vector3f position = new Vector3f(getPosition());
		setPosition(position.add(velocity));
	}

	private void updateAudio() {
		deathSoundSource.setPosition(getPosition());
		gruntAudioSource.setPosition(getPosition());
		gruntAudioSource.setVolume(10);

		if (!gruntAudioSource.isPlaying()) {
			gruntAudioSource.play(gruntAudioBuffer);
		}

		if (!isAlive() && gruntAudioSource.isPlaying()) {
			gruntAudioSource.stop();
		}
	}

	private void createBoundingBox() {
		if ((AABBCurrentCD += DisplayManager.getDelta()) >= AABBRegenerationCD) {
			getBoundingBox().generateBoundingBox(this);
			AABBCurrentCD = 0;
		}
	}

	public void lookAtNode(Vector3f nodePosition) {

		Vector3f enemyPositionCopy = new Vector3f(getPosition());
		Vector3f nodePositionCopy = new Vector3f(nodePosition);

		forwardDirection = nodePositionCopy.sub(enemyPositionCopy).normalize();

		float angle = (float) Math.toDegrees(Math.acos(forwardDirection.dot(forwardFace)));

		Vector3f rotationAxis = new Vector3f();
		forwardDirection.cross(forwardFace, rotationAxis).normalize();

		if (rotationAxis.y > 0) {
			setRotY(180 - angle);
		}

		if (rotationAxis.y < 0) {
			setRotY(180 + angle);
		}

	}

	public void accelerate(Vector3f wishDirection,
		float wishSpeed, float acceleration) {

		PhysicsEngine.checkEnemyMovement(this, wishDirection);

		float currentSpeed = velocity.dot(wishDirection);
		float addspeed = wishSpeed - currentSpeed;

		if (addspeed <= 0) {
			return;
		}

		float accelspeed = acceleration * DisplayManager.getDelta() * wishSpeed;

		if (accelspeed > addspeed) {
			accelspeed = addspeed;
		}

		velocity.x += accelspeed * wishDirection.x;
		velocity.y += accelspeed * wishDirection.y;
		velocity.z += accelspeed * wishDirection.z;
	}

	public Vector3f getForwardDirection() {
		return forwardDirection;
	}

	public float getHealth() {
		return currentHealth;
	}

	@Override
	public void setRotX(float rotX) {
		this.rotX = rotX;
		createBoundingBox();
	}

	@Override
	public void setRotY(float rotY) {
		this.rotY = rotY;
		createBoundingBox();
	}

	@Override
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
		createBoundingBox();
	}

	@Override
	public void setScale(float scale) {
		this.scale = scale;
		createBoundingBox();
	}

	public SpreadParticleSystem getBlood() {
		return blood;
	}

	public GibletEmmiter getGibEmmiter() {
		return gibEmmiter;
	}

	public void smoothRotY(float amount) {
		//TODO
	}

	public void decelerate() {
		velocity.mul(decelerationFactor * DisplayManager.getDelta());
	}

	public void decelerate(float factor) {
		velocity.mul(factor * DisplayManager.getDelta());
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public float getSpeedCap() {
		return speedCap;
	}

	public void setSpeedCap(float speedCap) {
		this.speedCap = speedCap;
	}

	public float getDecelerationFactor() {
		return decelerationFactor;
	}

	public void setDecelerationFactor(float decelerationFactor) {
		this.decelerationFactor = decelerationFactor;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public boolean isAlive() {
		return currentHealth > 0;
	}

	public void modifyHealth(float amount) {
		this.currentHealth += amount;
	}

	public EnemyAI getAI() {
		return currentAI;
	}

	public HealthPickupEmmiter getHpe() {
		return hpe;
	}

}
