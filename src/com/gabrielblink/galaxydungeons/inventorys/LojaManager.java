package com.gabrielblink.galaxydungeons.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.gabrielblink.galaxydungeons.CashAPI;
import com.gabrielblink.galaxydungeons.apis.Config;
import com.gabrielblink.galaxydungeons.apis.ItemBuilder;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.scoreboard.ScoreUpdater;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class LojaManager implements Listener{
	
	@EventHandler
	public void onBuyCustomItem(InventoryClickEvent event) {
		if(event.getInventory().getName().equalsIgnoreCase("Loja")) {
			if(event.getInventory().getItem(31)!=null) {
				if(event.getInventory().getItem(31).hasItemMeta()) {
				  if(event.getInventory().getItem(31).getItemMeta().hasDisplayName()) {
					if(event.getInventory().getItem(31).getItemMeta().getDisplayName().contains("Pepitas")) {
					  event.setCancelled(true);
					  Player p = (Player)event.getWhoClicked();
					  Dungeon currentDungeon = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
					  DungeonServer DS = CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName());
					  if(event.getSlot() == 15) {
						  if(DS.getPlayers_pepitas().get(p.getName()) >= (int)Config.get("Loja.GranadaExplosiva")) {
							 if(p.getInventory().firstEmpty() != -1) {
							  DS.getPlayers_pepitas().put(p.getName(), DS.getPlayers_pepitas().get(p.getName())-(int)Config.get("Loja.GranadaExplosiva"));
							  p.getInventory().addItem(getCustomItem(CustomItemType.GranadaExplosiva));
							  ScoreUpdater.updateScoreBoard(p, DS);
							  openLoja(p);
							 }else {
								  p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								 p.sendMessage("§cSeu inventário está lotado para receber este item.");
								 p.closeInventory();
							 }
							 }else {
							  p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						  }
					  }
					  if(event.getSlot() == 11) {
						  if(DS.getPlayers_pepitas().get(p.getName()) >= (int)Config.get("Loja.GranadaDistracao")) {
							 if(p.getInventory().firstEmpty() != -1) {
							  DS.getPlayers_pepitas().put(p.getName(), DS.getPlayers_pepitas().get(p.getName())-(int)Config.get("Loja.GranadaDistracao"));
							  p.getInventory().addItem(getCustomItem(CustomItemType.GranadaDistracao));
							  ScoreUpdater.updateScoreBoard(p, DS);
							  openLoja(p);
							 }else {
								  p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								 p.sendMessage("§cSeu inventário está lotado para receber este item.");
								 p.closeInventory();
							 }
							 }else {
							  p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						  }
					  }
					}
				  }
				}
			}
		}
	}
	
	
	public static ItemStack charge(Color cor) {
		ItemStack playerDrop = new ItemStack( Material.FIREWORK_CHARGE, 1);
        ItemMeta meta = playerDrop.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(cor).build();
        metaFw.setEffect(aa);
        playerDrop.setItemMeta(metaFw);
        return playerDrop;
	}
	
	public static ItemStack getCustomItem(CustomItemType citype) {
		switch(citype) {
		case GranadaDistracao:
			return new ItemBuilder(charge(Color.PURPLE).clone()).setName("§aGranada de Distração").setLore("§7Distraia todos os monstros em um raio de 15","§7blocos para onde você jogar a granada").toItemStack();
		case GranadaExplosiva:
			return new ItemBuilder(charge(Color.RED).clone()).setName("§aGranada Explosiva").setLore("§7A granada de explosão causa um alto dano","§7aos mobs que estão em volta dela, além de","§7deixar o chão em chamas por alguns segundos.").toItemStack();
		}
		return null;
	}
	
	public static void openLoja(Player p) {
		/*
		if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
			if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
				Dungeon currentDungeon = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
				if(CoreStorage.getDungeonServers().containsKey(currentDungeon.getDungeonName())) {
					DungeonServer DS = CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName());
		Inventory inv = Bukkit.createInventory(null, 36,"Loja");
		inv.setItem(31, new ItemBuilder(Material.PAPER).setName("§fPepitas: §7"+DS.getPlayers_pepitas().get(p.getName())).toItemStack());
		if(DS.getPlayers_pepitas().get(p.getName()) >= (int)Config.get("Loja.GranadaDistracao")) {
			inv.setItem(11, new ItemBuilder(charge(Color.PURPLE).clone()).setName("§aGranada de Distração").setLore("§7Distraia todos os monstros em um raio de 15","§7blocos para onde você jogar a granada","§7","§7Custo: §e"+Config.get("Loja.GranadaDistracao").toString()+" pepitas").toItemStack());
		}else {
			inv.setItem(11, new ItemBuilder(charge(Color.PURPLE).clone()).setName("§cGranada de Distração").setLore("§7Distraia todos os monstros em um raio de 15","§7blocos para onde você jogar a granada","§7","§7Custo: §e"+Config.get("Loja.GranadaDistracao").toString()+" pepitas","§c","§cVocê não tem pepitas suficientes.").toItemStack());
		}
		if(DS.getPlayers_pepitas().get(p.getName()) >= (int)Config.get("Loja.GranadaExplosiva")) {
			inv.setItem(15, new ItemBuilder(charge(Color.RED).clone()).setName("§aGranada Explosiva").setLore("§7A granada de explosão causa um alto dano","§7aos mobs que estão em volta dela, além de","§7deixar o chão em chamas por alguns segundos.","§7","§7Custo: §e"+Config.get("Loja.GranadaExplosiva").toString()+" pepitas").toItemStack());
		}else {
			inv.setItem(15, new ItemBuilder(charge(Color.RED).clone()).setName("§cGranada Explosiva").setLore("§7A granada de explosão causa um alto dano","§7aos mobs que estão em volta dela, além de","§7deixar o chão em chamas por alguns segundos.","§7","§7Custo: §e"+Config.get("Loja.GranadaExplosiva").toString()+" pepitas","§c","§cVocê não tem pepitas suficientes.").toItemStack());
		}
		if(!DS.getPlayersCanRevive().contains(p.getName())) {
	    if(CashAPI.getCash(p.getName())>= (int)Config.get("Loja.PocaoRessureicao")) {
	    	inv.setItem(13, new ItemBuilder(Material.POTION).setName("§aPoção de Ressureição").setLore("§7Seja reanimado na mesma fase","§7em que você foi eliminado.","§7","§7Custo: §6"+Config.get("Loja.PocaoRessureicao").toString()+"§6 cash").toItemStack());
	    }else {
	    	inv.setItem(13, new ItemBuilder(Material.POTION).setName("§cPoção de Ressureição").setLore("§7Seja reanimado na mesma fase","§7em que você foi eliminado.","§7","§7Custo: §6"+Config.get("Loja.PocaoRessureicao").toString()+"§6 cash","§7","§cVocê não tem saldo suficiente.").toItemStack());
	    }
		}else {
			inv.setItem(13, new ItemBuilder(Material.POTION).setName("§cPoção de Ressureição").setLore("§7Seja reanimado na mesma fase","§7em que você foi eliminado.","§7","§7Custo: §6"+Config.get("Loja.PocaoRessureicao").toString()+"§6 cash","§7","§cVocê já possui este item!").toItemStack());
		}
		p.openInventory(inv);
				}
			}
		}*/
		p.sendMessage("§cEm breve.");
	}
	public enum CustomItemType{
		GranadaExplosiva,GranadaDistracao
	}
}
