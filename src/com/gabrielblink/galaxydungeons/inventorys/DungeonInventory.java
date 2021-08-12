package com.gabrielblink.galaxydungeons.inventorys;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class DungeonInventory {

	public static DecimalFormatSymbols DFS = new DecimalFormatSymbols(new Locale("pt", "BR"));
	public static DecimalFormat FORMATTER = new DecimalFormat("###,###,###", DFS);
	
	public static void openDungeons(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54,"Dungeons");
		int slot = 10;
		for(String dungeon : CoreStorage.getDungeons().keySet()) {
			Dungeon d = CoreStorage.getDungeons().get(dungeon);
			ItemStack clone = CoreStorage.getDungeons().get(dungeon).getItem_stack().clone();
			ItemMeta cloneItemMeta = clone.getItemMeta();
			
			ArrayList<String> lore = new ArrayList<String>();
			for(String s : clone.getItemMeta().getLore()) {
				lore.add(s);
			}
		
			if(CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().containsKey(d.getDungeonName())) {
				cloneItemMeta.setDisplayName("§a"+clone.getItemMeta().getDisplayName());
				lore.add("");
				lore.add("§fAcessos: §e"+CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().get(d.getDungeonName()));
				lore.add("§aClique para jogar!");
			}else {
				lore.add("");
				lore.add("§7Custo: §e"+FORMATTER.format(d.getPrice())+" coins");
				lore.add("");
				if(Main.getEconomy().getBalance(p.getName()) >= d.getPrice()) {
					cloneItemMeta.setDisplayName("§a"+clone.getItemMeta().getDisplayName());
					lore.add("§aClique para comprar! §7(2 acessos)");
				}else {
					cloneItemMeta.setDisplayName("§c"+clone.getItemMeta().getDisplayName());
					lore.add("§cVocê não tem coins suficientes.");
				}
			}
			cloneItemMeta.setLore(lore);
			clone.setItemMeta(cloneItemMeta);
			inv.setItem(slot, clone);
			if(slot == 16 || slot == 25) {
				slot++;
				slot++;
				slot++;
			}else {
				slot++;
			}
		}
		p.openInventory(inv);
	}
	
	
}
