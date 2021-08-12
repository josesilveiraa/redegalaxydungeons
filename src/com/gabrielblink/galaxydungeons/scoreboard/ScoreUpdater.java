package com.gabrielblink.galaxydungeons.scoreboard;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabrielblink.galaxydungeons.CashAPI;
import com.gabrielblink.galaxydungeons.enums.DungeonStatus;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;
import com.google.common.base.Strings;

public class ScoreUpdater
  extends BukkitRunnable
{
	
  public void run()
  {
    Iterator<Player> iter = ScoreBoard.boards.keySet().iterator();
    try
    {
      while (iter.hasNext())
      {
        Player p = (Player)iter.next();
      }
    }
    catch (ConcurrentModificationException localConcurrentModificationException) {}
  }
  public static int getFaseAtual(Player p) {
	  String[] args = CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getFaseName().split("_");
	  int faseAtual = Integer.parseInt(args[1]);
	  return faseAtual;
  }
  public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
          ChatColor notCompletedColor) {
      float percent = (float) current / max;
      int progressBars = (int) (totalBars * percent);

      return Strings.repeat("" + completedColor + symbol, progressBars)
              + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
  }
  public static void updateScoreBoard(Player p,DungeonServer DS)
  {
	  DecimalFormatSymbols DFS = new DecimalFormatSymbols(new Locale("pt", "BR"));
	  DecimalFormat FORMATTER = new DecimalFormat("###,###,###", DFS);
	  ScoreBoardAPI sb = (ScoreBoardAPI)ScoreBoard.boards.get(p);
	  if(DS.getDungeonStatus().equals(DungeonStatus.WAITING_FOR_OK)) {
		  sb.update("§a"+DS.getPlayers_pepitas().size()+"/§a"+DS.getParty().getPartyMembers().size(), 5);
		  sb.update("§e"+DS.getPlayers_pepitas().get(p.getName()), 4);
		  sb.update("§6"+CashAPI.getCash(p.getName()), 3);
	  }else if(DS.getDungeonStatus().equals(DungeonStatus.RUNNING)) {
		  if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
			  if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase()!=null) {
		  if(!CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().hasBoss()) {//Checking if the current fase has a boss
			  
			  sb.update("§a"+DS.getEntitysALive(), 7);
			  sb.update("§a"+DS.getPlayers_pepitas().size()+"/§a"+DS.getParty().getPartyMembers().size(), 5);
			  sb.update("§e"+DS.getPlayers_pepitas().get(p.getName()), 4);
			  sb.update("§a"+getFaseAtual(p)+"/"+DS.getDungeon().getFases().size(), 6);
			  sb.update("§6"+CashAPI.getCash(p.getName()), 3);
		  }else {
			  if(DS.getBOSS() != null) {
				  LivingEntity lv = (LivingEntity)DS.getBOSS();
			  sb.update(" §fBoss: "+getProgressBar((int)lv.getHealth(), (int)lv.getMaxHealth(), 6, '♥', ChatColor.RED, ChatColor.GRAY), 8);
			  }else {
				  sb.update("§c♥♥♥♥♥♥", 8);
			  }
			  sb.update("§a"+DS.getEntitysALive(), 7);
			  sb.update("§a"+DS.getPlayers_pepitas().size()+"/§a"+DS.getParty().getPartyMembers().size(), 5);
			  sb.update("§e"+DS.getPlayers_pepitas().get(p.getName()), 4);
			  sb.update("§a"+getFaseAtual(p)+"/"+DS.getDungeon().getFases().size(), 6);
			  sb.update("§6"+CashAPI.getCash(p.getName()), 3);
		  }
	  }
		  }
	  }
  }
	    

}
