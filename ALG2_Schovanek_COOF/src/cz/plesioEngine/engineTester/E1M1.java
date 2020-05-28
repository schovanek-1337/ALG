package cz.plesioEngine.engineTester;

import cz.plesioEngine.audio.AudioMaster;
import cz.plesioEngine.entities.Camera;
import cz.plesioEngine.entities.enemies.Enemy;
import cz.plesioEngine.entities.staticEntities.Entity;
import cz.plesioEngine.entities.staticEntities.EntityCreator;
import cz.plesioEngine.entities.EntityRenderer;
import cz.plesioEngine.entities.lights.LightMaster;
import cz.plesioEngine.entities.staticEntities.MultimeshEntity;
import cz.plesioEngine.entities.staticEntities.ToiletPaper;
import cz.plesioEngine.entities.weapons.WeaponEntity;
import cz.plesioEngine.entities.weapons.WeaponMaster;
import cz.plesioEngine.gameLogic.PlayerStats;
import cz.plesioEngine.gameLogic.levels.Door;
import cz.plesioEngine.gameLogic.levels.EnemySpawn;
import cz.plesioEngine.gameLogic.levels.LevelFinish;
import cz.plesioEngine.gameLogic.levels.Room;
import cz.plesioEngine.guis.GuiRenderer;
import cz.plesioEngine.guis.GuiTexture;
import cz.plesioEngine.gameLogic.physicsEngine.PhysicsEngine;
import cz.plesioEngine.renderEngine.models.Mesh;
import cz.plesioEngine.renderEngine.models.MeshMaster;
import cz.plesioEngine.renderEngine.models.TexturedMesh;
import cz.plesioEngine.renderEngine.textures.Texture;
import cz.plesioEngine.renderEngine.textures.TextureMaster;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author plesio
 */
public class E1M1 {
	
	public static void load() {
		Camera.setPosition(new Vector3f(0, 13, -60));
		//Camera.setPosition(new Vector3f(-293, 13, -608));

		WeaponEntity shotgun = WeaponMaster.requestWeapon("weapons/meshes/doubleBarrelShotgun",
			"weapons/textures/doubleBarrelShotgun", "shotgunProjectile", "rustedMetal03",
			"particleAtlas", 4);
		shotgun.setPosition(new Vector3f(0, -1.2f, 0));
		shotgun.setScale(0.6f);
		shotgun.setFireSoundEffect(AudioMaster.loadSound("res/weapons/audio/shotgun.ogg"));
		shotgun.setHitSoundEffect(AudioMaster.loadSound("res/weapons/audio/weaponhit.ogg"));
		shotgun.setRotY(90);
		shotgun.setFireCooldown(0.7f);
		shotgun.getProjectileType().setProjectileSpeed(1.8f);
		shotgun.getProjectileType().setProjectileLifespan(5);
		shotgun.setProjectileOffset(6);
		shotgun.setProjectileSize(0.5f);
		shotgun.getWeaponParticleSystem().setParticleAmount(100);
		shotgun.getWeaponParticleSystem().setSpeed(100);
		shotgun.getWeaponParticleSystem().setLifeLength(0.2f);
		shotgun.setAmmoCapacity(20);
		shotgun.setCurrentAmmo(10);
		EntityRenderer.processEntity(shotgun);
		PhysicsEngine.processWeapon(shotgun);
		WeaponMaster.selectWeapon(shotgun);

		Texture E1M1SpawnRoomTexture
			= TextureMaster.requestTexture("levelAssets/textures/e1m1spawnroom");
		MultimeshEntity E1M1SpawnRoom = new MultimeshEntity(
			"levelAssets/meshes/e1m1SpawnRoom", E1M1SpawnRoomTexture,
			new Vector3f(-50, 0, -50), 0, 0, 0, 4.5f);
		EntityRenderer.processMultimeshEntity(E1M1SpawnRoom);
		PhysicsEngine.processMultimeshEntity(E1M1SpawnRoom);

		Texture E1M1SpawnRoomCorridor
			= TextureMaster.requestTexture("levelAssets/textures/e1m1SpawnRoomCorridor");
		MultimeshEntity E1M1spawncorridor = new MultimeshEntity(
			"levelAssets/meshes/e1m1SpawnCorridor", E1M1SpawnRoomCorridor,
			new Vector3f(-50, 0, -50), 0, 0, 0, 4.5f);
		EntityRenderer.processMultimeshEntity(E1M1spawncorridor);
		PhysicsEngine.processMultimeshEntity(E1M1spawncorridor);

		Texture[] E1M1Room1Textures
			= {TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 22),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 23),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 23),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 22),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 14),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 12),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 22),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 4),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 22),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 22),
				TextureMaster.requestTextureTiled("levelAssets/textures/cement01", 10),};
		MultimeshEntity E1M1Room1 = new MultimeshEntity("levelAssets/meshes/e1m1Room1",
			E1M1Room1Textures, E1M1SpawnRoom.getPosition(), 0, 0, 0,
			E1M1SpawnRoom.getScale());
		EntityRenderer.processMultimeshEntity(E1M1Room1);
		PhysicsEngine.processMultimeshEntity(E1M1Room1);

		Texture[] E1M1Room1Corridor2Textures
			= {TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 10),
				TextureMaster.requestTextureTiled("levelAssets/textures/cement01", 10),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 10)
			};
		MultimeshEntity E1M1Corridor2 = new MultimeshEntity("levelAssets/meshes/e1m1Corridor2",
			E1M1Room1Corridor2Textures, E1M1SpawnRoom.getPosition(), 0, 0, 0,
			E1M1SpawnRoom.getScale());
		EntityRenderer.processMultimeshEntity(E1M1Corridor2);
		PhysicsEngine.processMultimeshEntity(E1M1Corridor2);

		Texture[] E1M1Room2Textures
			= {TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 10),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 18),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 20),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 6),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 18),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 22),
				TextureMaster.requestTextureTiled("levelAssets/textures/cement01", 20),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 4),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 20)};
		MultimeshEntity E1M1Room2 = new MultimeshEntity("levelAssets/meshes/e1m1Room2",
			E1M1Room2Textures, E1M1SpawnRoom.getPosition(), 0, 0, 0,
			E1M1SpawnRoom.getScale());
		EntityRenderer.processMultimeshEntity(E1M1Room2);
		PhysicsEngine.processMultimeshEntity(E1M1Room2);

		Texture[] E1M1FinishTextures
			= {TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 12),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 12),
				TextureMaster.requestTextureTiled("levelAssets/textures/cement01", 12),
				TextureMaster.requestTextureTiled("levelAssets/textures/wallTile", 4)};
		MultimeshEntity E1M1Finish = new MultimeshEntity("levelAssets/meshes/e1m1Finish",
			E1M1FinishTextures, E1M1SpawnRoom.getPosition(), 0, 0, 0,
			E1M1SpawnRoom.getScale());
		EntityRenderer.processMultimeshEntity(E1M1Finish);
		PhysicsEngine.processMultimeshEntity(E1M1Finish);

		Entity e1m1Floor = EntityCreator.createEntity("levelAssets/meshes/e1m1Floor",
			"levelAssets/textures/floor");
		e1m1Floor.getTexturedModel()
			.getModelTexture().setTileSize(20f);
		e1m1Floor.setScale(E1M1SpawnRoom.getScale());
		e1m1Floor.setPosition(E1M1SpawnRoom.getPosition());
		EntityRenderer.processEntity(e1m1Floor);
		PhysicsEngine.processEntity(e1m1Floor);

		GuiTexture crosshair
			= new GuiTexture(TextureMaster.requestTexture("gui/crosshair"),
				new Vector2f(0, 0), new Vector2f(0.05f, 0.05f));
		GuiRenderer.processGUI(crosshair);

		Entity shelf1 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf1.setPosition(new Vector3f(102, 2.75f, -374));
		shelf1.setRotY(180);
		shelf1.setScale(10);

		EntityRenderer.processEntity(shelf1);
		PhysicsEngine.processEntity(shelf1);

		Entity shelf2 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf2.setPosition(new Vector3f(102, 2.75f, -414));
		shelf2.setRotY(180);
		shelf2.setScale(10);

		EntityRenderer.processEntity(shelf2);
		PhysicsEngine.processEntity(shelf2);

		Entity shelf3 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf3.setPosition(new Vector3f(57, 2.75f, -419));
		shelf3.setRotY(-90);
		shelf3.setScale(10);

		EntityRenderer.processEntity(shelf3);
		PhysicsEngine.processEntity(shelf3);

		Entity shelf4 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf4.setPosition(new Vector3f(17, 2.75f, -419));
		shelf4.setRotY(-90);
		shelf4.setScale(10);

		EntityRenderer.processEntity(shelf4);
		PhysicsEngine.processEntity(shelf4);

		Entity shelf5 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf5.setPosition(new Vector3f(-60, 2.75f, -419));
		shelf5.setRotY(-90);
		shelf5.setScale(10);

		EntityRenderer.processEntity(shelf5);
		PhysicsEngine.processEntity(shelf5);

		Entity shelf6 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf6.setPosition(new Vector3f(-100, 2.75f, -419));
		shelf6.setRotY(-90);
		shelf6.setScale(10);

		EntityRenderer.processEntity(shelf6);
		PhysicsEngine.processEntity(shelf6);

		Entity shelf7 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf7.setPosition(new Vector3f(-105, 2.75f, -374));
		shelf7.setRotY(0);
		shelf7.setScale(10);

		EntityRenderer.processEntity(shelf7);
		PhysicsEngine.processEntity(shelf7);

		Entity shelf8 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf8.setPosition(new Vector3f(-105, 2.75f, -334));
		shelf8.setRotY(0);
		shelf8.setScale(10);

		EntityRenderer.processEntity(shelf8);
		PhysicsEngine.processEntity(shelf8);

		Entity shelf9 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf9.setPosition(new Vector3f(-105, 2.75f, -254));
		shelf9.setRotY(0);
		shelf9.setScale(10);

		EntityRenderer.processEntity(shelf9);
		PhysicsEngine.processEntity(shelf9);

		Entity shelf10 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf10.setPosition(new Vector3f(-60, 2.75f, -249));
		shelf10.setRotY(90);
		shelf10.setScale(10);

		EntityRenderer.processEntity(shelf10);
		PhysicsEngine.processEntity(shelf10);

		Entity shelf11 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf11.setPosition(new Vector3f(57, 2.75f, -249));
		shelf11.setRotY(90);
		shelf11.setScale(10);

		EntityRenderer.processEntity(shelf11);
		PhysicsEngine.processEntity(shelf11);

		Entity shelf12 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf12.setPosition(new Vector3f(97, 2.75f, -249));
		shelf12.setRotY(90);
		shelf12.setScale(10);

		EntityRenderer.processEntity(shelf12);
		PhysicsEngine.processEntity(shelf12);

		Entity shelf13 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf13.setPosition(new Vector3f(102, 2.75f, -294));
		shelf13.setRotY(180);
		shelf13.setScale(10);

		EntityRenderer.processEntity(shelf13);
		PhysicsEngine.processEntity(shelf13);

		Entity shelf14 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf14.setPosition(new Vector3f(102, 2.75f, -334));
		shelf14.setRotY(180);
		shelf14.setScale(10);

		EntityRenderer.processEntity(shelf14);
		PhysicsEngine.processEntity(shelf14);

		Entity standBase = EntityCreator.createEntity("levelAssets/meshes/standBase",
			"levelAssets/textures/stand");
		standBase.setPosition(new Vector3f(-20, 2.75f, -320));
		standBase.setScale(7);

		EntityRenderer.processEntity(standBase);
		PhysicsEngine.processEntity(standBase);

		Entity standCage = EntityCreator.createEntity("levelAssets/meshes/standCage",
			"rustedMetal03");
		standCage.setPosition(new Vector3f(-20, 2.75f, -320));
		standCage.setScale(7);

		EntityRenderer.processEntity(standCage);
		PhysicsEngine.processEntity(standCage);

		ToiletPaper tp1 = new ToiletPaper(
			new TexturedMesh(
				MeshMaster.requestMesh("levelAssets/meshes/toiletPaper"),
				TextureMaster.requestTexture("levelAssets/textures/toiletPaper")),
			new Vector3f(0, 0, 0), 0, 0, 0, 1);

		tp1.setPosition(-13.8f, 10, -326);

		EntityRenderer.processEntity(tp1);
		PhysicsEngine.processEntity(tp1);

		LightMaster.createLight("toiletPaper1", tp1.getPosition(), new Vector3f(0.863f, 0.078f, 0.235f), new Vector3f(1, 0.001f, 0.0001f), true);
		
		Entity crate1 = EntityCreator.createEntity("crate", "crate");
		crate1.setScale(0.07f);
		crate1.setPosition(new Vector3f(-65, 10, -280));

		EntityRenderer.processEntity(crate1);
		PhysicsEngine.processEntity(crate1);

		Entity crate2 = EntityCreator.createEntity("crate", "crate");
		crate2.setScale(0.07f);
		crate2.setPosition(new Vector3f(15, 10, -280));

		EntityRenderer.processEntity(crate2);
		PhysicsEngine.processEntity(crate2);

		Entity crate3 = EntityCreator.createEntity("crate", "crate");
		crate3.setScale(0.07f);
		crate3.setPosition(new Vector3f(60, 10, -320));

		EntityRenderer.processEntity(crate3);
		PhysicsEngine.processEntity(crate3);

		Entity crate4 = EntityCreator.createEntity("crate", "crate");
		crate4.setScale(0.07f);
		crate4.setPosition(new Vector3f(40, 10, -380));

		EntityRenderer.processEntity(crate4);
		PhysicsEngine.processEntity(crate4);

		Entity crate5 = EntityCreator.createEntity("crate", "crate");
		crate5.setScale(0.07f);
		crate5.setPosition(new Vector3f(-20, 10, -375));

		EntityRenderer.processEntity(crate5);
		PhysicsEngine.processEntity(crate5);

		Entity crate6 = EntityCreator.createEntity("crate", "crate");
		crate6.setScale(0.07f);
		crate6.setPosition(new Vector3f(-70, 10, -345));

		EntityRenderer.processEntity(crate6);
		PhysicsEngine.processEntity(crate6);

		Entity shelf15 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf15.setPosition(new Vector3f(-280, 2.75f, -385));
		shelf15.setRotY(90);
		shelf15.setScale(10);

		EntityRenderer.processEntity(shelf15);
		PhysicsEngine.processEntity(shelf15);

		Entity shelf16 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf16.setPosition(new Vector3f(-240, 2.75f, -385));
		shelf16.setRotY(90);
		shelf16.setScale(10);

		EntityRenderer.processEntity(shelf16);
		PhysicsEngine.processEntity(shelf16);

		Entity shelf17 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf17.setPosition(new Vector3f(-320, 2.75f, -385));
		shelf17.setRotY(90);
		shelf17.setScale(10);

		EntityRenderer.processEntity(shelf17);
		PhysicsEngine.processEntity(shelf17);

		Entity shelf18 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf18.setPosition(new Vector3f(-365, 2.75f, -390));
		shelf18.setRotY(0);
		shelf18.setScale(10);

		EntityRenderer.processEntity(shelf18);
		PhysicsEngine.processEntity(shelf18);

		Entity shelf19 = EntityCreator.createEntity("levelAssets/meshes/shelf",
			"levelAssets/textures/shelf");
		shelf19.setPosition(new Vector3f(-365, 2.75f, -430));
		shelf19.setRotY(0);
		shelf19.setScale(10);

		EntityRenderer.processEntity(shelf19);
		PhysicsEngine.processEntity(shelf19);

		Entity crate7 = EntityCreator.createEntity("crate", "crate");
		crate7.setScale(0.07f);
		crate7.setPosition(new Vector3f(-356, 10, -500));

		EntityRenderer.processEntity(crate7);
		PhysicsEngine.processEntity(crate7);

		Entity crate8 = EntityCreator.createEntity("crate", "crate");
		crate8.setScale(0.05f);
		crate8.setPosition(new Vector3f(-370, 8, -500));

		EntityRenderer.processEntity(crate8);
		PhysicsEngine.processEntity(crate8);

		Entity crate9 = EntityCreator.createEntity("crate", "crate");
		crate9.setScale(0.07f);
		crate9.setPosition(new Vector3f(-280, 10, -490));

		EntityRenderer.processEntity(crate9);
		PhysicsEngine.processEntity(crate9);

		Entity crate10 = EntityCreator.createEntity("crate", "crate");
		crate10.setScale(0.07f);
		crate10.setPosition(new Vector3f(-360, 10, -550));

		EntityRenderer.processEntity(crate10);
		PhysicsEngine.processEntity(crate10);

		Entity crate11 = EntityCreator.createEntity("crate", "crate");
		crate11.setScale(0.05f);
		crate11.setPosition(new Vector3f(-345, 8, -550));

		EntityRenderer.processEntity(crate11);
		PhysicsEngine.processEntity(crate11);

		Entity crate12 = EntityCreator.createEntity("crate", "crate");
		crate12.setScale(0.05f);
		crate12.setPosition(new Vector3f(-350, 8, -536));

		EntityRenderer.processEntity(crate12);
		PhysicsEngine.processEntity(crate12);

		Entity standBase2 = EntityCreator.createEntity("levelAssets/meshes/standBase",
			"levelAssets/textures/stand");
		standBase2.setPosition(new Vector3f(-323, 2.75f, -466));
		standBase2.setScale(7);

		EntityRenderer.processEntity(standBase2);
		PhysicsEngine.processEntity(standBase2);

		Entity standCage2 = EntityCreator.createEntity("levelAssets/meshes/standCage",
			"rustedMetal03");
		standCage2.setPosition(new Vector3f(-323, 2.75f, -466));
		standCage2.setScale(7);

		EntityRenderer.processEntity(standCage2);
		PhysicsEngine.processEntity(standCage2);

		ToiletPaper tp2 = new ToiletPaper(
			new TexturedMesh(
				MeshMaster.requestMesh("levelAssets/meshes/toiletPaper"),
				TextureMaster.requestTexture("levelAssets/textures/toiletPaper")),
			new Vector3f(0, 0, 0), 0, 0, 0, 1);

		tp2.setPosition(-317, 10, -472);
		
		LightMaster.createLight("toiletPaper2", tp2.getPosition(), new Vector3f(0.863f, 0.078f, 0.235f), new Vector3f(1, 0.001f, 0.0001f), true);

		EntityRenderer.processEntity(tp2);
		PhysicsEngine.processEntity(tp2);

		Room r1 = new Room(new Vector3f(-25.3f, 13, -267));

		Mesh enemyMesh = MeshMaster.requestMesh("demon");
		Texture enemyTexture = TextureMaster.requestTexture("demon");

		Enemy enemyType1 = new Enemy(new TexturedMesh(enemyMesh, enemyTexture),
			new Vector3f(106, 20, -234), 0, 0, 0, 6, new Vector3f(0, 0, -1), 200);

		Enemy enemyType2 = new Enemy(new TexturedMesh(enemyMesh, enemyTexture),
			new Vector3f(-115, 20, -430), 0, 0, 0, 6, new Vector3f(0, 0, -1), 200);
		
		Enemy enemyType3 = new Enemy(new TexturedMesh(enemyMesh, enemyTexture),
			new Vector3f(117, 20, -430), 0, 0, 0, 6, new Vector3f(0, 0, -1), 200);
		
		Enemy enemyType4 = new Enemy(new TexturedMesh(enemyMesh, enemyTexture),
			new Vector3f(114, 20, -316), 0, 0, 0, 6, new Vector3f(0, 0, -1), 200);
		
		EnemySpawn spawn1 = new EnemySpawn();
		
		spawn1.addEnemyToSpawn(enemyType1,
			enemyType4,
			enemyType3,
			enemyType2);

		r1.addEnemySpawn(spawn1);

		EnemySpawn spawn2 = new EnemySpawn();

		spawn2.addEnemyToSpawn(enemyType2,
			enemyType3,
			enemyType4,
			enemyType2);

		r1.addEnemySpawn(spawn2);

		EnemySpawn spawn3 = new EnemySpawn();

		spawn3.addEnemyToSpawn(enemyType3,
			enemyType2,
			enemyType1,
			enemyType2,
			enemyType3);

		r1.addEnemySpawn(spawn3);

		EnemySpawn spawn4 = new EnemySpawn();

		spawn4.addEnemyToSpawn(enemyType4,
			enemyType1,
			enemyType2,
			enemyType4);

		r1.addEnemySpawn(spawn4);

		Mesh doorMesh = MeshMaster.requestMesh("levelAssets/meshes/doorBar");
		Texture doorTexture = TextureMaster.requestTexture("rustedMetal03");

		Door door1 = new Door(new TexturedMesh(doorMesh, doorTexture),
			new Vector3f(-13, 30, -215), 0, -90, 0, 6,
			new Vector3f(-13, 30, -215), new Vector3f(-13, 0, -215));

		EntityRenderer.processEntity(door1);
		PhysicsEngine.processEntity(door1);

		r1.addDoor(door1);

		Door door2 = new Door(new TexturedMesh(doorMesh, doorTexture),
			new Vector3f(-134, 30, -420), 0, 0, 0, 10,
			new Vector3f(-134, 30, -420), new Vector3f(-134, 0, -420));

		EntityRenderer.processEntity(door2);
		PhysicsEngine.processEntity(door2);

		r1.addDoor(door2);
		r1.setToiletPaper(tp1);
		r1.setGoal(standCage);

		Room r2 = new Room(new Vector3f(-285, 13, -400));

		EnemySpawn spawn5 = new EnemySpawn();

		Enemy enemyType5 = new Enemy(new TexturedMesh(enemyMesh, enemyTexture),
			new Vector3f(-384, 20, -527), 0, 0, 0, 6, new Vector3f(0, 0, -1), 200);

		spawn5.addEnemyToSpawn(enemyType5,
			enemyType5,
			enemyType5);

		r2.addEnemySpawn(spawn5);

		

		Enemy enemyType6 = new Enemy(new TexturedMesh(enemyMesh, enemyTexture),
			new Vector3f(-389, 20, -425), 0, 0, 0, 6, new Vector3f(0, 0, -1), 200);
		
		Enemy enemyType7 = new Enemy(new TexturedMesh(enemyMesh, enemyTexture),
			new Vector3f(-283, 20, -514), 0, 0, 0, 6, new Vector3f(0, 0, -1), 200);
		
		Enemy enemyType8 = new Enemy(new TexturedMesh(enemyMesh, enemyTexture),
			new Vector3f(-360, 20, -569), 0, 0, 0, 6, new Vector3f(0, 0, -1), 200);

		EnemySpawn spawn6 = new EnemySpawn();
		
		spawn6.addEnemyToSpawn(enemyType6,
			enemyType7,
			enemyType8);

		r2.addEnemySpawn(spawn6);

		EnemySpawn spawn7 = new EnemySpawn();

		spawn7.addEnemyToSpawn(enemyType7,
			enemyType8,
			enemyType7);

		r2.addEnemySpawn(spawn7);

		EnemySpawn spawn8 = new EnemySpawn();

		spawn8.addEnemyToSpawn(enemyType6,
			enemyType8,
			enemyType6);

		r2.addEnemySpawn(spawn8);

		r2.setGoal(standCage2);
		r2.setToiletPaper(tp2);

		Door door3 = new Door(new TexturedMesh(doorMesh, doorTexture),
			new Vector3f(-273.5f, 30, -580), 0, -90, 0, 10,
			new Vector3f(-273.5f, 30, -580), new Vector3f(-273.5f, 0, -580));

		EntityRenderer.processEntity(door3);
		PhysicsEngine.processEntity(door3);

		r2.addDoor(door3);

		Door door4 = new Door(new TexturedMesh(doorMesh, doorTexture),
			new Vector3f(-228, 30, -420), 0, 0, 0, 10,
			new Vector3f(-228, 30, -420), new Vector3f(-228, 0, -420));

		EntityRenderer.processEntity(door4);
		PhysicsEngine.processEntity(door4);

		r2.addDoor(door4);
		
		LevelFinish finish = new LevelFinish(
			new Vector3f(-293, 13, -648), 20);
		
		PlayerStats.time = 0;
		
	}

}
