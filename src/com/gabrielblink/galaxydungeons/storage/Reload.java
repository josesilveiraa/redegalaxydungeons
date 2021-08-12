package com.gabrielblink.galaxydungeons.storage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;

public class Reload {

	public static void loadOnReload() {
		if(Bukkit.getOnlinePlayers().size() > 0) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!CoreStorage.getStoraged_users().containsKey(p.getName())) {
					GalaxyDungeonUser du = new GalaxyDungeonUser(p.getName());
					CoreStorage.getStoraged_users().put(p.getName(), du);
					Bukkit.getConsoleSender().sendMessage("§a§l[RELOAD GALAXY] Setup for player "+p.getName()+" made.");
				}
			}
		}
	}
}
