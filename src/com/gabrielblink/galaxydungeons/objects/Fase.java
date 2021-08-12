package com.gabrielblink.galaxydungeons.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gabrielblink.galaxydungeons.apis.ScrollerInventory;
import com.gabrielblink.galaxydungeons.customentitys.VendedorHorse;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class Fase {

	private String FaseName;
	private Gate gate;
	private String Boss;
	private String chestLocation;
	private int pepitasonWin;
	private Location vendedor;
	private Location BossLocation;
	private ArrayList<Location> locations_mob_spawn = new ArrayList<Location>();
	private Location location_players_spawn;
	private ArrayList<String> mobs = new ArrayList<String>();
	private ArrayList<String> drops = new ArrayList<String>();
	private HashMap<UUID,ArrayList<ItemStack>> itemsCollecteds = new HashMap<UUID,ArrayList<ItemStack>>();
	private World world;
	
	public Fase(String faseName, String boss, ArrayList<Location> locations_mob_spawn, Location location_players_spawn,
			ArrayList<String> mobs, ArrayList<String> drops,Gate gate,String chest,Location vendedor,int pepitasonWin,Location BossLocation) {
		super();
		FaseName = faseName;
		Boss = boss;
		this.locations_mob_spawn = locations_mob_spawn;
		this.location_players_spawn = location_players_spawn;
		this.mobs = mobs;
		this.drops = drops;
		this.gate = gate;
		this.chestLocation = chest;
		this.vendedor = vendedor;
		this.pepitasonWin = pepitasonWin;
		this.BossLocation = BossLocation;
	}
	
	public Location getBossLocation() {
		return BossLocation;
	}

	public void setBossLocation(Location bossLocation) {
		BossLocation = bossLocation;
	}

	public int getFaseNumber() {
		if(FaseName.contains("_")) {
			String[] args = FaseName.split("_");
			int number = Integer.parseInt(args[1]);
			return number;
		}else {
			Bukkit.getConsoleSender().sendMessage("_ não encontrado no nome da fase "+FaseName);
			return 0;
		}
	}
	public int getPepitasonWin() {
		return pepitasonWin;
	}

	public void setPepitasonWin(int pepitasonWin) {
		this.pepitasonWin = pepitasonWin;
	}

	public Location getVendedor() {
		return vendedor;
	}

	public void setVendedor(Location vendedor) {
		this.vendedor = vendedor;
	}

	public boolean hasVendedor() {
		if(vendedor == null) {
			return false;
		}else {
			return true;
		}
	}
	public void despawnVendedor() {
		boolean encontrado = false;
		if(hasVendedor()) {
		for(Entity e : vendedor.getWorld().getEntities()) {
			if(e.hasMetadata("Vendedor")) {
				vendedor.getWorld().getEntities().remove(e);
				encontrado = true;
			}
		}
		}
	}
	public void spawnVendedor() {
		if(hasVendedor()) {
			new VendedorHorse(vendedor);
		}else {
			Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeons] Erro: Não há uma localização para o vendedor.");
		}
	}
	public void showRecompensas(Player p) {
		try {
	  new ScrollerInventory(itemsCollecteds.get(p.getUniqueId()), "Recompensas", p, false, "");
		}catch (Exception e) {
		}
		}
	public void addPlayer(UUID u) {
		ArrayList<ItemStack> a = new ArrayList<ItemStack>();
		itemsCollecteds.put(u, a);
	}
	public HashMap<UUID, ArrayList<ItemStack>> getItemsCollecteds() {
		return itemsCollecteds;
	}

	public void setItemsCollecteds(HashMap<UUID, ArrayList<ItemStack>> itemsCollecteds) {
		this.itemsCollecteds = itemsCollecteds;
	}

	public boolean hasGate() {
		if(gate == null) {
			return false;
		}else {
			return true;
		}
	}
	public boolean hasChestDefinido() {
		if(chestLocation.equalsIgnoreCase("~")) {
			return false;
		}else {
			return true;
		}
	}
	public Location getChestLocation() {
		String[] args = chestLocation.split("~");
		Location loc = CoreStorage.getDeserializedLocation(args[0]);
		return loc;
	}
	public BlockFace getChestFace() {
		String[] args = chestLocation.split("~");
		return BlockFace.valueOf(args[1]);
	}
	public void setChestLocation(String chestLocation) {
		this.chestLocation = chestLocation;
	}
	public Gate getGate() {
		return gate;
	}

	public void setGate(Gate gate) {
		this.gate = gate;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public boolean hasBoss() {
		if(Boss == null) {
			return false;
		}
		if(Boss.equalsIgnoreCase("") || Boss.equalsIgnoreCase(" ") || Boss.equalsIgnoreCase("null")) {
			return false;
		}else {
			return true;
		}
	}
	/**
	 * @return the faseName
	 */
	public String getFaseName() {
		return FaseName;
	}
	/**
	 * @param faseName the faseName to set
	 */
	public void setFaseName(String faseName) {
		FaseName = faseName;
	}
	/**
	 * @return the boss
	 */
	public String getBoss() {
		return Boss;
	}
	/**
	 * @param boss the boss to set
	 */
	public void setBoss(String boss) {
		Boss = boss;
	}
	/**
	 * @return the locations_mob_spawn
	 */
	public ArrayList<Location> getLocations_mob_spawn() {
		return locations_mob_spawn;
	}
	/**
	 * @param locations_mob_spawn the locations_mob_spawn to set
	 */
	public void setLocations_mob_spawn(ArrayList<Location> locations_mob_spawn) {
		this.locations_mob_spawn = locations_mob_spawn;
	}
	/**
	 * @return the location_players_spawn
	 */
	public Location getLocation_players_spawn() {
		return location_players_spawn;
	}
	/**
	 * @param location_players_spawn the location_players_spawn to set
	 */
	public void setLocation_players_spawn(Location location_players_spawn) {
		this.location_players_spawn = location_players_spawn;
	}
	/**
	 * @return the mobs
	 */
	public ArrayList<String> getMobs() {
		return mobs;
	}
	/**
	 * @param mobs the mobs to set
	 */
	public void setMobs(ArrayList<String> mobs) {
		this.mobs = mobs;
	}
	/**
	 * @return the drops
	 */
	public ArrayList<String> getDrops() {
		return drops;
	}
	/**
	 * @param drops the drops to set
	 */
	public void setDrops(ArrayList<String> drops) {
		this.drops = drops;
	}
	
	
}
