package com.gabrielblink.galaxydungeons.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.inventorys.DungeonInventory;
import com.gabrielblink.galaxydungeons.objects.DungeonNPC;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

import net.citizensnpcs.api.event.NPCRightClickEvent;

public class CommandDungeonNPC implements CommandExecutor, Listener{
	@EventHandler
	public void preventArmorStandItem(PlayerInteractAtEntityEvent event) {
		if(event.getRightClicked()!=null) {
			if(event.getRightClicked().hasMetadata("ArmorDungeon")) {
				event.setCancelled(true);
				DungeonInventory.openDungeons(event.getPlayer());
			}else if(event.getRightClicked()!=null) {
				if(event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
					if(event.getRightClicked().hasMetadata("CustomArmorDungeon")) {
						DungeonInventory.openDungeons(event.getPlayer());
					}
				}
			}
		}
	}
	@EventHandler
	public void onClick(NPCRightClickEvent event) {
		if(event.getNPC().getName().equalsIgnoreCase("§8[NPC] Dungeon NPC")) {
			DungeonInventory.openDungeons(event.getClicker());
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("setdungeonnpc")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(p.hasPermission("galaxy.galaxydungeons.admin")) {
					if(CoreStorage.npc == null) {
					DungeonNPC dnpc = new DungeonNPC(p.getLocation());	
					dnpc.spawn();
					CoreStorage.npc = dnpc;
					Main.data.set("DungeonNPC", CoreStorage.getSerializedLocation(p.getLocation()));
					Main.data.save();
					p.sendMessage("§aNPC §2§LDUNGEONS §aspawnado com sucesso.");
				}else {
					CoreStorage.npc.despawn();
					CoreStorage.npc.getNpc().despawn();
					DungeonNPC dnpc = new DungeonNPC(p.getLocation());	
					dnpc.spawn();
					Main.data.set("DungeonNPC", CoreStorage.getSerializedLocation(p.getLocation()));
					Main.data.save();
					CoreStorage.npc = dnpc;
					p.sendMessage("§aNPC §2§LDUNGEONS §aspawnado com sucesso.");
				}
				}else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}
		}
		return false;
	}

}
