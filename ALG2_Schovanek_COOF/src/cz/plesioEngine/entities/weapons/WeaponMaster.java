package cz.plesioEngine.entities.weapons;

import cz.plesioEngine.entities.enemies.Enemy;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.gameLogic.Overkill;
import cz.plesioEngine.gameLogic.PlayerStats;
import cz.plesioEngine.gameLogic.SettingMaster;
import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import cz.plesioEngine.toolbox.MousePicker;
import org.javatuples.Pair;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public final class WeaponMaster {

	private static WeaponEntity heldWeapon;

	private WeaponMaster() {

	}

	public static WeaponEntity requestWeapon(String weaponObjectName,
		String weaponTextureName, String projectileObjectName,
		String projectileTextureName, String particleName, int particleAtlas) {

		Mesh projectileMesh = MeshMaster.requestMesh(projectileObjectName);
		Texture projectileTexture = TextureMaster.requestTexture(projectileTextureName);
		TexturedMesh projectileTexturedMesh = new TexturedMesh(projectileMesh,
			projectileTexture);

		Mesh entityMesh = MeshMaster.requestMesh(weaponObjectName);
		Texture entityTexture = TextureMaster.requestTexture(weaponTextureName);
		TexturedMesh weaponTexturedMesh = new TexturedMesh(entityMesh, entityTexture);

		WeaponEntity newWeapon = new WeaponEntity(weaponTexturedMesh,
			new Vector3f(1f, -1.5f, -2f), 0, 0, 0, 1,
			TextureMaster.requestTexture(particleName, particleAtlas),
			250, 100, 0, 0.25f, 0.5f);

		Projectile newProjectile
			= new Projectile(projectileTexturedMesh, new Vector3f(0, 0, 0), 0, 0, 0, 1);

		newWeapon.setProjectileType(newProjectile);

		return newWeapon;
	}

	public static void selectWeapon(WeaponEntity weapon) {
		heldWeapon = weapon;
	}

	/**
	 * Shoot method called on mouse callback, tells the physics engine to
	 * check for direct hits
	 */
	public static void shoot() {
		if (heldWeapon != null && heldWeapon.getCurrentCooldown() <= 0
			&& heldWeapon.getCurrentAmmo() > 0) {
			heldWeapon.shoot();
			Pair<Entity, Float> hit
				= PhysicsEngine.checkProjectileBodyCollision();
			if (hit != null) {
				Entity hitEntity = hit.getValue0();
				if (hitEntity instanceof Enemy) {
					Enemy hitEnemy = (Enemy) hitEntity;
					hitEnemy.modifyHealth(-heldWeapon.getWeaponDamage(hit.getValue1()));
					if (hitEnemy.getHealth() < 0) {
						if (Math.abs(hitEnemy.getHealth()) >= 100) {
							PlayerStats.score += 2 * Math.abs(hitEnemy.getHealth());
							hitEnemy.getGibEmmiter().emitGiblets(hitEnemy.getPosition());
							hitEnemy.getHpe().emitHealthPickups(hitEnemy.getPosition());
							Overkill.overkill();
						}
						PlayerStats.score += Math.abs(hitEnemy.getHealth());
					}
					heldWeapon.playWeaponHitEffect();
					if (SettingMaster.enableGore) {
						hitEnemy.getBlood().generateParticles(hitEnemy.getPosition(),
							MousePicker.getCurrentRay(), 0.9f);
					}
				}
			}
		}
	}

	public static WeaponEntity getHeldWeapon() {
		return heldWeapon;
	}

}
