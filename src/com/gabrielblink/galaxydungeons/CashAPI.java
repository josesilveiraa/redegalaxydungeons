package com.gabrielblink.galaxydungeons;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;


public class CashAPI {
	
	public static CashPluginType HookedPluginCash;
	public static PlayerPoints playerPoints;
	
	public static boolean hookPlayerPoints() {
		Bukkit.getConsoleSender().sendMessage("§e[GalaxyHook] Tentando fazer hook com o PlayerPoints...");
	    final Plugin plugin = Bukkit.getPluginManager().getPlugin("PlayerPoints");
	    if(plugin != null) {
	    playerPoints = PlayerPoints.class.cast(plugin);
	    return true;
	    }else {
	    	Bukkit.getConsoleSender().sendMessage("§c[GalaxyHook] Erro - PlayerPoints não encontrado.");
	    	return false;
	    }
	}

   /* public static MineSHOP getMineShopCore() {
	  	return shop;
    }*/
	
	public static PlayerPoints getPlayerPoints() {
	    return playerPoints;
	}
	
	
	public static void hook() {
		if(hookPlayerPoints()) {
			Bukkit.getConsoleSender().sendMessage("§a[GalaxyHook] * Hook com PlayerPoints Finalizado com sucesso.");
			HookedPluginCash = CashPluginType.PlayerPoints;
			return;
		}else {
			Bukkit.getConsoleSender().sendMessage("§4[GalaxyHook] * Nenhum Hook de Cash foi encontrado!");
			HookedPluginCash = CashPluginType.NOTFOUND;
			return;
		}
	}
	
	public static int getCash(String playername) {
		if(HookedPluginCash.equals(HookedPluginCash.MineShop)) {
			return 0;
		}else if(HookedPluginCash.equals(HookedPluginCash.PlayerPoints)) {
			return getPlayerPoints().getAPI().look(playername);
		}else {
			return 0;
		}
	}
	
	
	
	public enum CashPluginType{
		PlayerPoints,MineShop,NOTFOUND
	}
}
