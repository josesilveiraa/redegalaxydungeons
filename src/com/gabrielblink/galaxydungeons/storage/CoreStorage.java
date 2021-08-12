package com.gabrielblink.galaxydungeons.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.apis.ItemBuilder;
import com.gabrielblink.galaxydungeons.enums.ArmaduraType;
import com.gabrielblink.galaxydungeons.enums.GateStatus;
import com.gabrielblink.galaxydungeons.events.PartyCreateEvent;
import com.gabrielblink.galaxydungeons.holograms.Hologram;
import com.gabrielblink.galaxydungeons.listeners.EntityFixer;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonNPC;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.Fase;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.objects.Gate;
import com.gabrielblink.galaxydungeons.objects.Party;

public class CoreStorage {

	public static HashMap<String,GalaxyDungeonUser> storaged_users = new HashMap<String,GalaxyDungeonUser>();
	public static HashMap<Integer,Party> partys = new HashMap<Integer,Party>();
	public static HashMap<String,Dungeon> dungeons = new HashMap<String,Dungeon>();
	public static HashMap<String,ItemStack> customItems = new HashMap<String,ItemStack>();
	public static ArrayList<EntityType> usingEntitys = new ArrayList<EntityType>();
	public static HashMap<String,DungeonServer> dungeonServers = new HashMap<String,DungeonServer>();
	public static ArrayList<String> worlds_registreds_names = new ArrayList<String>();
	public static DungeonNPC npc;
	public static Location spawn;
	public static ArrayList<Hologram> spawned_holograms = new ArrayList<Hologram>();
	
	public static boolean existsEntityType(String name) {
		try {
			EntityType.valueOf(name);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static HashMap<String, DungeonServer> getDungeonServers() {
		return dungeonServers;
	}

	public static void setDungeonServers(HashMap<String, DungeonServer> dungeonServers) {
		CoreStorage.dungeonServers = dungeonServers;
	}
	public static void fix() {
		spawned_holograms.forEach(H->{
			H.despawn();
		});
	}
	public static void createParty(String lider) {
		int i = partys.size()+1;
		Party p = new Party(i, lider);
		p.getPartyMembers().add(lider);
		partys.put(i, p);
		PartyCreateEvent pevent = new PartyCreateEvent(p, lider);
		storaged_users.get(lider).setCurrentParty(p);
		Bukkit.getServer().getPluginManager().callEvent(pevent);
	}
	public static void setupDungeonNPC() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.data.contains("DungeonNPC")) {
				if(!Main.data.getString("DungeonNPC").equalsIgnoreCase("")) {
					Location loc = getDeserializedLocation(Main.data.getString("DungeonNPC"));
					DungeonNPC dnpc = new DungeonNPC(loc);
					dnpc.spawn();
					npc = dnpc;
					Bukkit.getConsoleSender().sendMessage("§a[>] NPC da Dungeon setado com sucesso!");
				}else {
					Bukkit.getConsoleSender().sendMessage("§c[!] Localização do NPC de Dungeon não encontrada, continuando...");
				}
				}
			}
		}.runTaskLater(Main.getPlugin(Main.class), 20L);
	}
	public static HashMap<Integer,Party> getPartys(){
		return partys;
	}
	public static ItemStack getItemMenu(String dungeon_name) {
		int id = Main.getInstance().getConfig().getInt("Dungeons."+dungeon_name+".MenuItem.ID");
		int data = Main.getInstance().getConfig().getInt("Dungeons."+dungeon_name+".MenuItem.Data");
		boolean glow = Main.getInstance().getConfig().getBoolean("Dungeons."+dungeon_name+".MenuItem.Glow");
		String nome = Main.getInstance().getConfig().getString("Dungeons."+dungeon_name+".MenuItem.Nome").replace("&", "§");
		ArrayList<String> lore = new ArrayList<String>();
		for(String a : Main.getInstance().getConfig().getStringList("Dungeons."+dungeon_name+".MenuItem.Lore")) {
			lore.add(a.replace("&", "§"));
		}
		if(glow) {
			return new ItemBuilder(Material.getMaterial(id)).setDurability((short)data).setName(nome).setLore(lore).addEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).toItemStack();
		}else {
			return new ItemBuilder(Material.getMaterial(id)).setDurability((short)data).setName(nome).setLore(lore).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).toItemStack();
		}
	}
	public static String getSerializedLocation(Location loc) { 
        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch()
                + ";" + loc.getWorld().getUID();
     }
  
    public static Location getDeserializedLocation(String s) {
    	if(s.equalsIgnoreCase("") || s.equalsIgnoreCase(" ")) {
    		return null;
    	}
            String [] parts = s.split(";"); 
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            double z = Double.parseDouble(parts[2]);
            float yaw = Float.parseFloat(parts[3]);
            float pitch = Float.parseFloat(parts[4]);
            UUID u = UUID.fromString(parts[5]);
            World w = Bukkit.getServer().getWorld(u);
            return new Location(w, x, y, z, yaw, pitch); 
    }
    public static void setupSpawn() {
    	if(!Main.getInstance().getConfig().getString("Geral.SpawnLocation").equalsIgnoreCase("")) {
    		Location l = getDeserializedLocation(Main.getInstance().getConfig().getString("Geral.SpawnLocation"));
    		spawn = l;
    		Bukkit.getConsoleSender().sendMessage("§a[!] Spawn carregado com sucesso.");
    	}else {
    		spawn = null;
    		Bukkit.getConsoleSender().sendMessage("§e[!] Localização do spawn geral do servidor ainda não foi definida.");
    		Bukkit.getConsoleSender().sendMessage("§e[!] Utilize: /dungeon setspawn para definir a localização do spawn geral.");
    	}
    }
    public static World getWorldFromString(String s) {
    	 String [] parts = s.split(";"); 
    	 UUID u = UUID.fromString(parts[5]);
         World w = Bukkit.getServer().getWorld(u);
         return w;
    }
	public static void setupDungeons() {
		for(String key : Main.getInstance().getConfig().getConfigurationSection("Dungeons").getKeys(false)) {
			int preco = Main.getInstance().getConfig().getInt("Dungeons."+key+".PrecoPasse");
			String nomeFancy = Main.getInstance().getConfig().getString("Dungeons."+key+".NomeFancy").replace("&", "§");
			ItemStack item_menu = getItemMenu(key);
			HashMap<String,Fase> fases = new HashMap<String,Fase>();
	
			for(String fase : Main.getInstance().getConfig().getConfigurationSection("Dungeons."+key+".Fases").getKeys(false)) {
				String boss = Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".Boss");
				ArrayList<Location> locations_mob_spawn = new ArrayList<Location>();
				for(String s : Main.getInstance().getConfig().getStringList("Dungeons."+key+".Fases."+fase+".Locations")) {
					if(!s.equalsIgnoreCase("")) {
					locations_mob_spawn.add(getDeserializedLocation(s));
					}
					}
				Location location_players_spawn = null;
				if(!Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".Location").equalsIgnoreCase("")) {
					location_players_spawn = getDeserializedLocation(Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".Location"));
				}
				ArrayList<String> mobs = new ArrayList<String>();
				for(String s : Main.getInstance().getConfig().getStringList("Dungeons."+key+".Fases."+fase+".Mobs")) {
					mobs.add(s);
				}
				ArrayList<String> drops = new ArrayList<String>();
				for(String s : Main.getInstance().getConfig().getStringList("Dungeons."+key+".Fases."+fase+".Drops")) {
					drops.add(s);
				}
				
				
				ArrayList<Location> gate_locations = new ArrayList<Location>();
				for(String s : Main.getInstance().getConfig().getStringList("Dungeons."+key+".Fases."+fase+".Gate")) {
					if(!s.equalsIgnoreCase("")) {
					   gate_locations.add(getDeserializedLocation(s));
					}
					}
				Gate gate = null;
				if(gate_locations.size() > 0) {
			      gate = new Gate(GateStatus.CLOSED, gate_locations.get(0), gate_locations.get(1));
				}
				
				String ChestLocation = "";
				String bf = "";
				if(!Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".ChestLocation").equalsIgnoreCase("")) {
					String[] args = Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".ChestLocation").split("~");
					ChestLocation = args[0];
					bf = args[1];
				}
				String chestLoc = ChestLocation+"~"+bf;
				
				Location HorseLocation = null;
				if(!Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".HorseLocation").equalsIgnoreCase("")) {
					HorseLocation = getDeserializedLocation(Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".HorseLocation"));
				}
				Location BossLocation = null;
				if(!Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".BossLocation").equalsIgnoreCase("")) {
					BossLocation = getDeserializedLocation(Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".BossLocation"));
				}
				int pepitasonWin = Main.getInstance().getConfig().getInt("Dungeons."+key+".Fases."+fase+".PepitasOnWin");
				
				Fase f = new Fase(fase, boss, locations_mob_spawn, location_players_spawn, mobs, drops,gate,chestLoc,HorseLocation,pepitasonWin,BossLocation);
				if(!Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".Location").equalsIgnoreCase("")) {
					if(getWorldFromString(Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".Location")) != null) {
					worlds_registreds_names.add(getWorldFromString(Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".Location")).getName());
					f.setWorld(getWorldFromString(Main.getInstance().getConfig().getString("Dungeons."+key+".Fases."+fase+".Location")));
					}
				}
				fases.put(fase, f);
			}
			ArrayList<ItemStack> itensIniciais = new ArrayList<ItemStack>();
			for(String s : Main.getInstance().getConfig().getStringList("Dungeons."+key+".ItensIniciais")) {
				itensIniciais.add(EntityFixer.getItemStackIniciaisFromString(s));
			}
			HashMap<ArmaduraType,ItemStack> armor_inicial = new HashMap<ArmaduraType,ItemStack>();
			armor_inicial.put(ArmaduraType.Helmet, EntityFixer.getItemStackIniciaisFromString(Main.getInstance().getConfig().getString("Dungeons."+key+".ArmaduraInicial.Helmet")));
			armor_inicial.put(ArmaduraType.Chestplate, EntityFixer.getItemStackIniciaisFromString(Main.getInstance().getConfig().getString("Dungeons."+key+".ArmaduraInicial.Chestplate")));
			armor_inicial.put(ArmaduraType.Boots, EntityFixer.getItemStackIniciaisFromString(Main.getInstance().getConfig().getString("Dungeons."+key+".ArmaduraInicial.Boots")));
			armor_inicial.put(ArmaduraType.Leggings, EntityFixer.getItemStackIniciaisFromString(Main.getInstance().getConfig().getString("Dungeons."+key+".ArmaduraInicial.Leggings")));
			Dungeon d = new Dungeon(key, nomeFancy, preco, item_menu, fases,itensIniciais,armor_inicial,getDeserializedLocation(Main.getInstance().getConfig().getString("Dungeons."+key+".LobbyLocation")));
			dungeons.put(key, d);
			Bukkit.getConsoleSender().sendMessage("§3[+] Dungeon "+key+" carregada com sucesso.");
		}
	}
	public static void setupUsingEntitys() {
		for(String key : Main.getInstance().getConfig().getConfigurationSection("Dungeons").getKeys(false)) {
			for(String fase : Main.getInstance().getConfig().getConfigurationSection("Dungeons."+key+".Fases").getKeys(false)) {
				for(String s : Main.getInstance().getConfig().getStringList("Dungeons."+key+".Fases."+fase+".Mobs")) {
					String[] args = s.split(",");
					String mob_type = args[0];
					if(existsEntityType(mob_type)) {
						usingEntitys.add(EntityType.valueOf(mob_type));
					}else {
						Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeons] Erro - Tipo de Mob não encontrado na dungeon: "+key+" , fase: "+fase+" mob: "+mob_type);
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static void setupCustomItems() {
		for(String key : Main.getInstance().getConfig().getConfigurationSection("CustomItems").getKeys(false)) {
			int id = Main.getInstance().getConfig().getInt("CustomItems."+key+".ID");
			int data = Main.getInstance().getConfig().getInt("CustomItems."+key+".Data");
			boolean glow = Main.getInstance().getConfig().getBoolean("CustomItems."+key+".Glow");
			String Nome = Main.getInstance().getConfig().getString("CustomItems."+key+".Nome").replace("&", "§");
			ArrayList<String> lore = new ArrayList<String>();
			for(String a : Main.getInstance().getConfig().getStringList("CustomItems."+key+".Lore")) {
				lore.add(a.replace("&", "§"));
			}
			ItemStack item = null;
			if(glow) {
				item = new ItemBuilder(Material.getMaterial(id)).setDurability((short)data).addEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).setName(Nome).setLore(lore).toItemStack();
			}else {
				item = new ItemBuilder(Material.getMaterial(id)).setDurability((short)data).setName(Nome).setLore(lore).toItemStack();
			}
			customItems.put(key, item);
		}
	}
	
	
	/**
	 * @return the customItems
	 */
	public static HashMap<String, ItemStack> getCustomItems() {
		return customItems;
	}
	/**
	 * @param customItems the customItems to set
	 */
	public static void setCustomItems(HashMap<String, ItemStack> customItems) {
		CoreStorage.customItems = customItems;
	}
	/**
	 * @return the usingEntitys
	 */
	public static ArrayList<EntityType> getUsingEntitys() {
		return usingEntitys;
	}
	/**
	 * @param usingEntitys the usingEntitys to set
	 */
	public static void setUsingEntitys(ArrayList<EntityType> usingEntitys) {
		CoreStorage.usingEntitys = usingEntitys;
	}
	/**
	 * @return the storaged_users
	 */
	public static HashMap<String, GalaxyDungeonUser> getStoraged_users() {
		return storaged_users;
	}
	/**
	 * @param storaged_users the storaged_users to set
	 */
	public static void setStoraged_users(HashMap<String, GalaxyDungeonUser> storaged_users) {
		CoreStorage.storaged_users = storaged_users;
	}
	/**
	 * @return the dungeons
	 */
	public static HashMap<String, Dungeon> getDungeons() {
		return dungeons;
	}
	/**
	 * @param dungeons the dungeons to set
	 */
	public static void setDungeons(HashMap<String, Dungeon> dungeons) {
		CoreStorage.dungeons = dungeons;
	}
	
	
	
}
