package com.gabrielblink.galaxydungeons.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gabrielblink.galaxydungeons.maxidratemysql.MaxidrateMethods;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class DungeonUserSetup implements Listener{

	@EventHandler
	public void onSetupDungeonUser(PlayerJoinEvent event) {
		String username = event.getPlayer().getName();
		if(!CoreStorage.getStoraged_users().containsKey(username)) {
			if(MaxidrateMethods.contains(username)) {
				MaxidrateMethods.downloadGalaxyUser(username);
			}else {
				GalaxyDungeonUser du = new GalaxyDungeonUser(username);
				MaxidrateMethods.addGalaxyUser(username, MaxidrateMethods.encodeGalaxyDungeonUser(du));
				CoreStorage.getStoraged_users().put(username, du);
			}
		}
	}
	
	@EventHandler
	public void setupDungeonUserWithAsynchronously(AsyncPlayerPreLoginEvent event) {
		String username = event.getName();
		if(!CoreStorage.getStoraged_users().containsKey(username)) {
			if(MaxidrateMethods.contains(username)) {
				MaxidrateMethods.downloadGalaxyUser(username);
			}else {
				GalaxyDungeonUser du = new GalaxyDungeonUser(username);
				MaxidrateMethods.addGalaxyUser(username, MaxidrateMethods.encodeGalaxyDungeonUser(du));
				CoreStorage.getStoraged_users().put(username, du);
			}
		}
	}
}
