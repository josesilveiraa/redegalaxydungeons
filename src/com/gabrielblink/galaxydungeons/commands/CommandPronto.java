package com.gabrielblink.galaxydungeons.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gabrielblink.galaxydungeons.apis.Title;
import com.gabrielblink.galaxydungeons.enums.Status;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.Party;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class CommandPronto implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("pronto")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
					if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
						Dungeon d = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
						Party party = CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty();
						if(CoreStorage.getDungeonServers().get(d.getDungeonName()).getPlayerStatus().containsKey(p.getName())) {
							///check if is pronto
							if(!CoreStorage.getDungeonServers().get(d.getDungeonName()).getPlayerStatus().get(p.getName()).equals(Status.READY)){
						Title t = new Title("§a§lVocê está pronto!", "§7Aguarde pelo resto da equipe", 1, 100000, 10000);
						t.send(p);
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
						p.sendMessage("§aVocê está pronto para o combate");
						CoreStorage.getDungeonServers().get(d.getDungeonName()).getPlayerStatus().put(p.getName(), Status.READY);
						CoreStorage.getDungeonServers().get(d.getDungeonName()).updatePlayersProntos();
							}else {
								p.sendMessage("§cVocê já está pronto para o combate!");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
							}
						}
					}else {
						p.sendMessage("§cEste comando apenas pode ser utilizado em uma dungeon.");
					}
				}
			}
		}
		return false;
	}

}
