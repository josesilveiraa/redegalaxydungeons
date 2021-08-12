package com.gabrielblink.galaxydungeons;

import com.gabrielblink.galaxydungeons.exceptions.DungeonNotFoundException;
import com.gabrielblink.galaxydungeons.exceptions.DungeonServerNotFoundException;
import com.gabrielblink.galaxydungeons.exceptions.GalaxyUserNotFoundException;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class GalaxyDungeonAPI {

	public static boolean hasGalaxyUser(String user) {
		return CoreStorage.getStoraged_users().containsKey(user);
	}
	
	public static GalaxyDungeonUser getGalaxyUser(String username) throws GalaxyUserNotFoundException {
		if(CoreStorage.getStoraged_users().containsKey(username)) {
			return CoreStorage.getStoraged_users().get(username);
		}else {
			 throw new GalaxyUserNotFoundException(username);
		}
	}
	public static DungeonServer getDungeonServer(String dungeonName) throws DungeonServerNotFoundException {
		if(CoreStorage.getDungeonServers().containsKey(dungeonName)) {
			return CoreStorage.getDungeonServers().get(dungeonName);
		}else {
			throw new DungeonServerNotFoundException(dungeonName);
		}
	}
	public static Dungeon getDungeon(String dungeonName) throws DungeonServerNotFoundException {
		if(CoreStorage.getDungeons().containsKey(dungeonName)) {
			return CoreStorage.getDungeons().get(dungeonName);
		}else {
			throw new DungeonServerNotFoundException(dungeonName);
		}
	}
	public static Dungeon getCurrentDungeon(String user) throws DungeonNotFoundException, GalaxyUserNotFoundException {
		if(hasGalaxyUser(user)) {
		GalaxyDungeonUser gu = CoreStorage.getStoraged_users().get(user);
		if(gu.isInDungeon()) {
			return gu.getCurrentDungeon();
		}else {
			throw new DungeonNotFoundException(user);
		}
		}else {
			throw new GalaxyUserNotFoundException(user);
		}
	}
}
