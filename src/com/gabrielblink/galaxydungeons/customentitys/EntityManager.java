package com.gabrielblink.galaxydungeons.customentitys;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.apis.ItemBuilder;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;

import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityEndermite;
import net.minecraft.server.v1_8_R3.EntityFlying;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalTargetNearestPlayer;
import net.minecraft.server.v1_8_R3.WorldServer;


public class EntityManager {
	private static String versionString;
	private static Map<String, Class<?>> loadedOBCClasses = new HashMap<String, Class<?>>();
	private static Map<String, Class<?>> loadedNMSClasses = new HashMap<String, Class<?>>();
	   public static String getVersion() {
	        if (versionString == null) {
	            String name = Bukkit.getServer().getClass().getPackage().getName();
	            versionString = name.substring(name.lastIndexOf('.') + 1) + ".";
	        }

	        return versionString;
	    }
	    public static void followPlayer(Player player, LivingEntity entity, double d) {
	    }
	public static Class<?> getNMSClass(String nmsClassName) {
        if (loadedNMSClasses.containsKey(nmsClassName)) {
            return loadedNMSClasses.get(nmsClassName);
        }

        String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            return loadedNMSClasses.put(nmsClassName, null);
        }

        loadedNMSClasses.put(nmsClassName, clazz);
        return clazz;
    }
	
	public static void spawnCustomEntity
	(EntityType entityType,int quantityofmobs,Location loc,boolean isFinal,boolean useArmor,boolean isBoss,DungeonServer DS) {
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		switch(entityType) {
		case BAT:
			for (int i = 0; i < quantityofmobs; i++) {
				EntityBat giant = null;
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
					giant = new EntityBat(world);
				  giant.targetSelector.a(2, new PathfinderGoalTargetNearestPlayer(giant));
	            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
	        }
			break;
		case BLAZE:
			for (int i = 0; i < quantityofmobs; i++) {
				EntityBlaze giant = null;
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
					giant = new EntityBlaze(world);
	            giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget( giant,
	                    EntityHuman.class, true));
	            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
	        }
			break;
		case CAVE_SPIDER:
			for (int i = 0; i < quantityofmobs; i++) {
				EntityCaveSpider giant = null;
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
					giant = new EntityCaveSpider(world);
	            giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget( giant,
	                    EntityHuman.class, true));
	            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
	        }
			break;
		case CHICKEN:
			for (int i = 0; i < quantityofmobs; i++) {
				EntityChicken giant = null;
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				try {
					giant = new EntityChicken(world);
				}catch (Exception e) {
					// TODO: handle exception
				}
	            giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget( giant,
	                    EntityHuman.class, true));
	            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
	        }
			break;
		case CREEPER:
			for (int i = 0; i < quantityofmobs; i++) {
				EntityCreeper giant = null;
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				try {
					giant = new EntityCreeper(world);
				} catch (Exception e) {
					// TODO: handle exception
				}
	            giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget( giant,
	                    EntityHuman.class, true));
	            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
	        }
			break;
		case ENDERMAN:
			for (int i = 0; i < quantityofmobs; i++) {
				EntityEnderman giant = null;
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				try {
					giant = new EntityEnderman(world);
				}catch (Exception e) {
					// TODO: handle exception
				}
	            giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget( giant,
	                    EntityHuman.class, true));
	            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
	        }
			break;
		case ENDERMITE:
			  for (int i = 0; i < quantityofmobs; i++) {
		            EntityEndermite giant = null;
		            if(isBoss) {
						giant.setCustomNameVisible(true);
						giant.setCustomName("§c§lBOSS");
					}
						giant = new EntityEndermite(world);
					  giant.targetSelector.a(2, new PathfinderGoalTargetNearestPlayer(giant));
		            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
		              if(isBoss) {
		            	  DS.setBOSS(giant.getBukkitEntity());
		              }
		              world.addEntity(giant);
		        }
			break;
		case ENDER_DRAGON:
			  for (int i = 0; i < quantityofmobs; i++) {
		            EntityEnderDragon giant = null;
		            if(isBoss) {
						giant.setCustomNameVisible(true);
						giant.setCustomName("§c§lBOSS");
					}
						giant = new EntityEnderDragon(world);
					  giant.targetSelector.a(2, new PathfinderGoalTargetNearestPlayer(giant));
		            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
		              if(isBoss) {
		            	  DS.setBOSS(giant.getBukkitEntity());
		              }
		              world.addEntity(giant);
		        }
			break;
		case GHAST:

	        for (int i = 0; i < quantityofmobs; i++) {
	        	EntityFlying giant = null;
	        	if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant = new EntityGhast(world);
	            giant.targetSelector.a(2, new PathfinderGoalTargetNearestPlayer(giant));
	            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
	        }
			break;
		case GIANT:
	        for (int i = 0; i < quantityofmobs; i++) {
	            EntityCreature giant = null;
	            if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
					giant = new EntityGiantZombie(world);
	            try {
	            	try {
		            	giant.goalSelector.a(4, new PathfinderGoalMeleeAttack(giant, 1.0D, true));
					} catch (Exception e) {
						// TODO: handle exception
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
	            giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget( giant,
	                    EntityHuman.class, true));
	            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
	        }
			break;
		case GUARDIAN:
			  for (int i = 0; i < quantityofmobs; i++) {
		            EntityCreature giant = null;
		            if(isBoss) {
						giant.setCustomNameVisible(true);
						giant.setCustomName("§c§lBOSS");
					}
						giant = new EntityGuardian(world);
					try {
		            	giant.goalSelector.a(4, new PathfinderGoalMeleeAttack(giant, 1.0D, true));
					} catch (Exception e) {
						// TODO: handle exception
					}
		            giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget( giant,
		                    EntityHuman.class, true));
		            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
		              if(isBoss) {
		            	  DS.setBOSS(giant.getBukkitEntity());
		              }
		              world.addEntity(giant);
		        }
			break;
		case HORSE:
			  for (int i = 0; i < quantityofmobs; i++) {
		            EntityCreature giant = null;
		            if(isBoss) {
						giant.setCustomNameVisible(true);
						giant.setCustomName("§c§lBOSS");
					}
						giant = new EntityHorse(world);
					try {
		            	giant.goalSelector.a(4, new PathfinderGoalMeleeAttack(giant, 1.0D, true));
					} catch (Exception e) {
						// TODO: handle exception
					}
		            giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget( giant,
		                    EntityHuman.class, true));
		            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		            if(useArmor) {
						if(percentChance(7)) {
							giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
						}else if(percentChance(10)){
							giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
						}else if(percentChance(20)) {
							giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
						}else if(percentChance(30)) {
							giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
						}else if(percentChance(32)) {
							giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
						}else {
							
						}
						if(percentChance(15)) {
							if(isFinal) {
								if(percentChance(30)) {
									giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
								}else {
									giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
								}
							}else {
								giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
							}
						}else if(percentChance(25)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
						}else if(percentChance(25)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
						}else if(percentChance(25)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
						}else {
							
						}
						if(percentChance(10)) {
							giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
						}else if(percentChance(15)) {
							giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
						}else if(percentChance(15)) {
							giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
						}else if(percentChance(15)) {
							giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
						}else {
							
						}
						if(percentChance(7)) {
							giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
						}else if(percentChance(10)) {
							giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
						}else if(percentChance(10)) {
							giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
						}else if(percentChance(10)) {
							giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
						}else {
							
						}
						if(isFinal) {
							if(percentChance(30)) {
								if(percentChance(50)) {
									giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
								}else {
									giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
								}
							}
						}
			            }
		            giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
		            if(isBoss) {
		            	  DS.setBOSS(giant.getBukkitEntity());
		              } 
		            world.addEntity(giant);
		        }
			break;
		case IRON_GOLEM:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityIronGolem giant = new EntityIronGolem(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }  
				world.addEntity(giant);
			}
			break;
		case OCELOT:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityOcelot giant = new EntityOcelot(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
			}
			break;
		case PAINTING:
			break;
		case PIG:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityPig giant = new EntityPig(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
			}
			break;
		case PIG_ZOMBIE:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityPigZombie giant = new EntityPigZombie(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				
	            if(useArmor) {
	            	
				if(percentChance(7)) {
					giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).addEnchant(Enchantment.KNOCKBACK, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).addEnchant(Enchantment.DURABILITY, 1).toItemStack()));
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack()));
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }  
				world.addEntity(giant);
			}
			break;
		case RABBIT:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityRabbit giant = new EntityRabbit(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	              
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              } 
				world.addEntity(giant);
			}
			break;
		case SHEEP:
			for(int i = 0; i< quantityofmobs; i++) {
				EntitySheep giant = new EntitySheep(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }  
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              } 
				world.addEntity(giant);
			}
			break;
		case SILVERFISH:
			for(int i = 0; i< quantityofmobs; i++) {
				EntitySilverfish giant = new EntitySilverfish(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            } 
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }  
				world.addEntity(giant);
			}
			break;
		case SKELETON:
			for(int i = 0; i< quantityofmobs; i++) {
				EntitySkeleton giant = new EntitySkeleton(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
			    		if(percentChance(50)) {
				giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_FIRE, 3).addEnchant(Enchantment.ARROW_DAMAGE, 3).toItemStack()));
			    		}else {
			    			giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.BOW).toItemStack()));
			    		}
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
				world.addEntity(giant);
			}
			break;
		case SLIME:
			   for (int i = 0; i < quantityofmobs; i++) {
		        	EntitySlime giant = null;
		        	if(isBoss) {
						giant.setCustomNameVisible(true);
						giant.setCustomName("§c§lBOSS");
					}
					giant = new EntitySlime(world);
					giant.setSize(5);
					giant.targetSelector.a(2, new PathfinderGoalTargetNearestPlayer(giant));
		            giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		            
		            giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
		            if(isBoss) {
		            	  DS.setBOSS(giant.getBukkitEntity());
		              }
		            world.addEntity(giant);
		        }
			break;
		case SNOWMAN:
			for(int i = 0; i< quantityofmobs; i++) {
				EntitySnowman giant = new EntitySnowman(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              } 
				world.addEntity(giant);
			}
			break;
		case SPIDER:
			for(int i = 0; i< quantityofmobs; i++) {
				EntitySpider giant = new EntitySpider(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }  
				world.addEntity(giant);
			}
			break;
		case VILLAGER:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityVillager giant = new EntityVillager(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              } 
				world.addEntity(giant);
			}
			break;
		case WITCH:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityWitch giant = new EntityWitch(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              } 
				world.addEntity(giant);
			}
			break;
		case WITHER:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityWither giant = new EntityWither(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }  
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
				world.addEntity(giant);
			}
			break;
		case WOLF:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityWolf giant = new EntityWolf(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            } 
				giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
				if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              } 
				world.addEntity(giant);
			}
			break;
		case ZOMBIE:
			for(int i = 0; i< quantityofmobs; i++) {
				EntityZombie giant = new EntityZombie(world);
				if(isBoss) {
					giant.setCustomNameVisible(true);
					giant.setCustomName("§c§lBOSS");
				}
				giant.goalSelector.a(4, new PathfinderGoalMeleeAttack((EntityCreature)giant, 1.0D, true));
				giant.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature)giant, EntityHuman.class, true));
				giant.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	            if(useArmor) {
				if(percentChance(7)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_HELMET).toItemStack()));
				}else if(percentChance(10)){
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_HELMET).toItemStack()));
				}else if(percentChance(20)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.GOLD_HELMET).toItemStack()));
				}else if(percentChance(30)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_HELMET).toItemStack()));
				}else if(percentChance(32)) {
					giant.setEquipment(4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_HELMET).toItemStack()));
				}else {
					
				}
				if(percentChance(15)) {
					if(isFinal) {
						if(percentChance(30)) {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 2).toItemStack()));
						}else {
							giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
						}
					}else {
						giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_CHESTPLATE).toItemStack()));
					}
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack()));
				}else if(percentChance(25)) {
					giant.setEquipment(3, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
				}else {
					
				}
				if(percentChance(10)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack()));
				}else if(percentChance(15)) {
					giant.setEquipment(2, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).toItemStack()));
				}else {
					
				}
				if(percentChance(7)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.IRON_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack()));
				}else if(percentChance(10)) {
					giant.setEquipment(1, CraftItemStack.asNMSCopy(new ItemBuilder(Material.CHAINMAIL_BOOTS).toItemStack()));
				}else {
					
				}
				if(isFinal) {
					if(percentChance(30)) {
						if(percentChance(50)) {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack()));
						}else {
							giant.setEquipment(0, CraftItemStack.asNMSCopy(new ItemBuilder(Material.DIAMOND_SWORD).toItemStack()));
						}
					}
				}
	            }
	              giant.getBukkitEntity().setMetadata("GalaxyCustomMob", new FixedMetadataValue(Main.getPlugin(Main.class), "GalaxyCustomMob"));
	              if(isBoss) {
	            	  DS.setBOSS(giant.getBukkitEntity());
	              }
	              world.addEntity(giant);
			}
			break;
		default:
			break;
		
		}
	}
    
 public static boolean percentChance(double percent){
	        if(percent > 100 || percent < 0){
	            throw new IllegalArgumentException("Percentage cannot be greater than 100 or less than 0!");
	        }
	        double result = new Random().nextDouble() * 100;
	        return result <= percent;
	    }
	  public static Object getPrivateField(String fieldName, Class clazz,
	            Object object) {
	        Field field;
	        Object o = null;
	        try {
	            field = clazz.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            o = field.get(object);
	        } catch (NoSuchFieldException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	        return o;
	    }
}
