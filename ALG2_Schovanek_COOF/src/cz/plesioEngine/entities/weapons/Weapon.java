package cz.plesioEngine.entities.weapons;

import java.util.List;

/**
 *
 * @author plesio
 */
public interface Weapon {

	Projectile getProjectileType();

	List<Projectile> getCurrentProjectiles();
}
