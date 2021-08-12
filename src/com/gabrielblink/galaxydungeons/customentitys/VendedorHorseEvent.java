package com.gabrielblink.galaxydungeons.customentitys;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.event.entity.EntityMountEvent;

import com.gabrielblink.galaxydungeons.CashAPI;
import com.gabrielblink.galaxydungeons.apis.Config;
import com.gabrielblink.galaxydungeons.apis.ItemBuilder;
import com.gabrielblink.galaxydungeons.inventorys.LojaManager;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

import net.citizensnpcs.api.event.NPCRightClickEvent;

public class VendedorHorseEvent implements Listener{
	
	@EventHandler
	public void en(EntityMountEvent event) {
		 if(event.getEntity().hasMetadata("Vendedor")) {
			 event.setCancelled(true);
		 }
	}
	@EventHandler
	public void onInteractWithVendor(NPCRightClickEvent event) {
		if(event.getNPC()!=null) {
			if(event.getNPC().getName().equalsIgnoreCase("§8[NPC] Vendedor")) {
				if(event.getNPC().getId() == 40004) {
					Player p = event.getClicker();
					LojaManager.openLoja(p);
				}
			}
		}
	}
	@EventHandler
	public void onInteractWithVendorSecond(PlayerInteractAtEntityEvent event) {
		if(event.getRightClicked()!=null) {
			if(event.getRightClicked().getType().equals(EntityType.HORSE)) {
				if(event.getRightClicked().hasMetadata("Vendedor")) {
					Player p = event.getPlayer();
					LojaManager.openLoja(p);
				}
			}
		}
	}
	@EventHandler
	public void onPreventDismontBug(VehicleExitEvent event) {
		if(event.getExited().getType().equals(EntityType.HORSE)) {
			if(CoreStorage.worlds_registreds_names.contains(event.getVehicle().getLocation().getWorld().getName())) {
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPreventMontBug(VehicleEnterEvent event) {
		if(event.getEntered().getType().equals(EntityType.HORSE)) {
			if(CoreStorage.worlds_registreds_names.contains(event.getVehicle().getLocation().getWorld().getName())) {
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onDamageHorse(EntityDamageEvent event) {
		if(event.getEntity().getType().equals(EntityType.HORSE)) {
			if(CoreStorage.worlds_registreds_names.contains(event.getEntity().getLocation().getWorld().getName())) {
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void preventArmorStandItem(PlayerInteractAtEntityEvent event) {
		if(event.getRightClicked()!=null) {
			if(event.getRightClicked().hasMetadata("Armor")) {
				event.setCancelled(true);
			}
		}
	}
}
