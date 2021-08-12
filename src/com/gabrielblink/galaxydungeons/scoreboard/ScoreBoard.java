package com.gabrielblink.galaxydungeons.scoreboard;

import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import com.gabrielblink.galaxydungeons.apis.Config;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.Fase;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class ScoreBoard implements Listener{
	
	public static WeakHashMap<Player, ScoreBoardAPI> boards = new WeakHashMap<>();
	public boolean mcMMO = true;
	
	
	public static void createLobbyScoreBoard(Player p,DungeonServer DS) {
		p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		String UUIDRandom = new RandomUUID().getUUID();

		ScoreBoardAPI sb = new ScoreBoardAPI("§c"+DS.getDungeon().getFancyDungeonName(), UUIDRandom);
		sb.blankLine(6);
		sb.add("  §fJogadores: ",5);
		sb.add("  §fPepitas: ", 4);
		sb.add("  §fCash: ", 3);
		sb.blankLine(2);
		sb.add("  "+Config.get("Geral.Server_URL").toString().replace("&", "§"), 1);
		
		sb.build();
		ScoreBoard.boards.remove(p);
		sb.send(p);
		ScoreBoard.boards.put(p, sb);
	}
	public static void createDungeonScoreBoardWithoutBoss(Player p,DungeonServer DS) {
		p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		String UUIDRandom = new RandomUUID().getUUID();
		ScoreBoardAPI sb = new ScoreBoardAPI("§c"+DS.getDungeon().getFancyDungeonName(), UUIDRandom);
		sb.blankLine(8);
		sb.add(" §fMobs vivos: ",7);
		sb.add(" §fFase atual: ",6);
		sb.add(" §fJogadores: ",5);
		sb.add(" §fPepitas: ", 4);
		sb.add(" §fCash: ", 3);
		sb.blankLine(2);
		sb.add("  "+Config.get("Geral.Server_URL").toString().replace("&", "§"), 1);
		
		sb.build();
		ScoreBoard.boards.remove(p);
		sb.send(p);
		ScoreBoard.boards.put(p, sb);
	}
	public static void createDungeonScoreBoardWithBoss(Player p,DungeonServer DS) {
		p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		String UUIDRandom = new RandomUUID().getUUID();
		ScoreBoardAPI sb = new ScoreBoardAPI("§c"+DS.getDungeon().getFancyDungeonName(), UUIDRandom);
		sb.blankLine(9);
		sb.add(" §fBoss: ",8);
		sb.add(" §fMobs vivos: ",7);
		sb.add(" §fFase atual: ",6);
		sb.add(" §fJogadores: ",5);
		sb.add(" §fPepitas: ", 4);
		sb.add(" §fCash: ", 3);
		sb.blankLine(2);
		sb.add("  "+Config.get("Geral.Server_URL").toString().replace("&", "§"), 1);
		
		sb.build();
		ScoreBoard.boards.remove(p);
		sb.send(p);
		ScoreBoard.boards.put(p, sb);
	}
	
	@EventHandler
	public void onExit(PlayerQuitEvent e) {
		if(boards.containsKey(e.getPlayer())) {
		boards.remove(e.getPlayer());
		}
	}
	
}
