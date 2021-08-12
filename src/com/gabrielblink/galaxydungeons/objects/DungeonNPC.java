package com.gabrielblink.galaxydungeons.objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.apis.Config;
import com.gabrielblink.galaxydungeons.holograms.Hologram;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;


public class DungeonNPC {

	private static final int NPC_ID = 1093;
	private Location loc;
	private Hologram DUNGEONS;
	private Hologram CLIQUE;
	private NPC npc;
	
	
	public DungeonNPC(Location loc) {
		super();
		this.loc = loc;
	}
	public void despawn() {
		for(Entity e : loc.getWorld().getEntities()) {
			if(e.getType().equals(EntityType.ARMOR_STAND)) {
				if(e.hasMetadata("ArmorDungeon")) {
					e.remove();
				}
			}
		}
		CoreStorage.spawned_holograms.clear();
		this.DUNGEONS.despawn();
		this.CLIQUE.despawn();
	}
	
	public void spawn() {
		if(CitizensAPI.getNPCRegistry().getById(NPC_ID)!= null) {
			if(CitizensAPI.getNPCRegistry().getById(NPC_ID).isSpawned()) {
				CitizensAPI.getNPCRegistry().getById(NPC_ID).despawn();
				CitizensAPI.getNPCRegistry().deregister(CitizensAPI.getNPCRegistry().getById(NPC_ID));
			}
		}
		 ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);
		 as.setMetadata("ArmorDungeon", new FixedMetadataValue(Main.getPlugin(Main.class), "ArmorDungeon"));
	        as.setBasePlate(false);
	        as.setArms(true);
	        as.setVisible(false);
	        as.setCanPickupItems(false);
	        as.setGravity(false);
	        as.setSmall(true);
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, UUID.randomUUID(), NPC_ID, "§8[NPC] Dungeon NPC");
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, Config.get("DungeonNPCS.Dungeon").toString());
		npc.spawn(loc);
		Hologram h = new Hologram(loc.clone().add(0,0.9,0), "§a§lDUNGEONS");
		Hologram h2 = new Hologram(loc.clone().add(0,0.5,0), "§aClique para abrir!");
		h2.spawn();
		h.spawn();
		this.CLIQUE = h2;
		this.DUNGEONS = h;
		CoreStorage.spawned_holograms.add(h);
		CoreStorage.spawned_holograms.add(h2);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				try {
					npc.getEntity().setPassenger(as);
				} catch (NullPointerException e) {
					// TODO: handle exception
				}
				}
		}.runTaskLater(Main.getPlugin(Main.class), 20L*4);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(DUNGEONS.isSpawned()) {
				if(DUNGEONS.getLines().get(0).getText().equalsIgnoreCase("§a§lDUNGEONS")){
					DUNGEONS.updateLine(1, "§2§lDUNGEONS");
				}else {
					DUNGEONS.updateLine(1, "§a§lDUNGEONS");
				}
				}else {
					this.cancel();
				}
			}
		}.runTaskTimer(Main.getPlugin(Main.class), 0, 20L*1);
	    this.npc = npc;

	}
	
	public NPC getNpc() {
		return npc;
	}

	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	public static int getNpcId() {
		return NPC_ID;
	}

	public Location getLoc() {
		return loc;
	}


	public void setLoc(Location loc) {
		this.loc = loc;
	}


	public Hologram getDUNGEONS() {
		return DUNGEONS;
	}


	public void setDUNGEONS(Hologram dUNGEONS) {
		DUNGEONS = dUNGEONS;
	}
	
	
}
