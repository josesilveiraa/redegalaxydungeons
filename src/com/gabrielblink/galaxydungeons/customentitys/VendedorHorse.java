package com.gabrielblink.galaxydungeons.customentitys;


import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.apis.Config;
import com.gabrielblink.galaxydungeons.apis.EntityAI;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class VendedorHorse {

	
	public VendedorHorse(Location location) {
		if(CitizensAPI.getNPCRegistry().getById(40004)!= null) {
			if(CitizensAPI.getNPCRegistry().getById(40004).isSpawned()) {
				CitizensAPI.getNPCRegistry().getById(40004).despawn();
				CitizensAPI.getNPCRegistry().deregister(CitizensAPI.getNPCRegistry().getById(40004));
			}
		}
		 ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
		 as.setMetadata("Armor", new FixedMetadataValue(Main.getPlugin(Main.class), "Armor"));
	        as.setBasePlate(false);
	        as.setArms(true);
	        as.setVisible(false);
	        as.setCanPickupItems(false);
	        as.setGravity(false);
	        as.setSmall(true);
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, UUID.randomUUID(), 40004, "§8[NPC] Vendedor");
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, Config.get("DungeonNPCS.Vendedor").toString());
		npc.spawn(location);
		npc.getEntity().setCustomNameVisible(false);
		Horse h = (Horse) location.getWorld().spawnEntity(location, EntityType.HORSE);
		h.setMetadata("Vendedor", new FixedMetadataValue(Main.getPlugin(Main.class), "Vendedor"));
		h.setBreed(true);
		h.setTamed(true);
		h.setOwner((AnimalTamer) npc.getEntity());
		h.setDomestication(h.getMaxDomestication());
		h.setCanPickupItems(false);
		h.setVariant(Variant.HORSE);
		h.setFireTicks(0);
		h.setColor(Color.BROWN);
		h.setStyle(Style.BLACK_DOTS);
		h.setCarryingChest(true);
		if(h.getPassenger()== null) {
			h.setPassenger(npc.getEntity());
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				npc.getEntity().setPassenger(as);
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						if(h!=null) {
						if(h.getPassenger()== null) {
							h.setPassenger(npc.getEntity());
						}else {
							this.cancel();
						}
						}else {
							this.cancel();
						}
					}
				}.runTaskTimer(Main.getPlugin(Main.class), 0, 20L*2);
			}
		}.runTaskLater(Main.getPlugin(Main.class), 20L*5);
		EntityAI.setAiEnabled(h, false);
	}
}
