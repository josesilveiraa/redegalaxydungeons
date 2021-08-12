package com.gabrielblink.galaxydungeons.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gabrielblink.galaxydungeons.enums.DungeonStatus;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;
import com.sk89q.worldedit.entity.Player;

public class DungeonPlayerQuit implements Listener{

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = (Player) event.getPlayer();
		if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
			if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
				Dungeon currentDungeon = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
				DungeonServer DS = CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName());
				if(DS.getDungeonStatus().equals(DungeonStatus.RUNNING) || DS.getDungeonStatus().equals(DungeonStatus.WAITING_FOR_OK) ||
						DS.getDungeonStatus().equals(DungeonStatus.WAITING_FOR_PLAYERS_GO_THROUGHT_GATE)) {
				if(DS.getPlayers_pepitas().containsKey(p.getName())) {
					DS.getPlayers_pepitas().remove(p.getName());
				}
				if(DS.getOld_itens().containsKey(p.getName())) {
					DS.getOld_itens().remove(p.getName());
				}
				}
			}
		}
	}
}
