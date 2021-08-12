package com.gabrielblink.galaxydungeons.listeners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gabrielblink.galaxydungeons.apis.ItemName;
import com.gabrielblink.galaxydungeons.customentitys.EntityManager;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.Fase;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class EntityFixer implements Listener
{

	@EventHandler
	public void onBreakcausedbyPlugin(EntityExplodeEvent event) {
		if(CoreStorage.worlds_registreds_names.contains(event.getLocation().getWorld().getName())) {
		if(event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
		  event.setCancelled(true);	
		}else if(event.getEntityType().equals(EntityType.CREEPER)) {
			event.setCancelled(true);
		}else if(event.getEntityType().equals(EntityType.WITHER)) {
			event.setCancelled(true);
		}
		}
	}
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event){
	if(event.getEntity() instanceof Zombie || event.getEntity() instanceof Skeleton){
		if(event.getEntity().hasMetadata("GalaxyCustomMob")) {
	           event.setCancelled(true);
		}
	}
	}
	 public static boolean percentChance(double percent){
	        if(percent > 100 || percent < 0){
	            throw new IllegalArgumentException("Percentage cannot be greater than 100 or less than 0!");
	        }
	        double result = new Random().nextDouble() * 100;
	        return result <= percent;
	    }
	 public static int getPercentageOfString(String string) {
		 String[] args = string.split("-");
		 return Integer.parseInt(args[1]);
	 }
	 public static boolean isCustomItem(String str) {
	    String[] array = str.split(",");
	    if(array.length == 2) {
	    	return true;
	    }else {
	    	return false;
	    }
	    }
		public static ItemStack getItemStackIniciaisFromString(String string) {
			if(!isCustomItem(string)) {
			String[] variaveis = string.split(",");
			int data = Integer.valueOf(variaveis[1]);
			ItemStack item = new com.gabrielblink.galaxydungeons.apis.ItemBuilder(Material.getMaterial(Integer.valueOf(variaveis[0]))).setDurability((short)data).toItemStack();
			String nomedoitem = variaveis[2].replace("&", "§");
			int quantidade = Integer.valueOf(variaveis[3]);
			

			if(!nomedoitem.equalsIgnoreCase("null")){
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName(nomedoitem);
				item.setItemMeta(itemMeta);
			}
			item.setAmount(quantidade);
			if(!variaveis[4].equalsIgnoreCase("null")){
				int encantamento1 = Integer.valueOf(variaveis[4]);
				int levelencantamento1 = Integer.valueOf(variaveis[5]);
				item.addUnsafeEnchantment(Enchantment.getById(encantamento1), levelencantamento1);
			}
			if(!variaveis[6].equalsIgnoreCase("null")){
				int encantamento2 = Integer.valueOf(variaveis[6]);
				int levelencantamento2 = Integer.valueOf(variaveis[7]);
				item.addUnsafeEnchantment(Enchantment.getById(encantamento2), levelencantamento2);
			}
			if(!variaveis[8].equalsIgnoreCase("null")){
				int encantamento3 = Integer.valueOf(variaveis[8]);
				int levelencantamento3 = Integer.valueOf(variaveis[9]);
				item.addUnsafeEnchantment(Enchantment.getById(encantamento3), levelencantamento3);
			}
			if(!variaveis[10].equalsIgnoreCase("null")){
				int encantamento4 = Integer.valueOf(variaveis[10]);
				int levelencantamento4 = Integer.valueOf(variaveis[11]);
				item.addUnsafeEnchantment(Enchantment.getById(encantamento4), levelencantamento4);
			}
			return item;
			}else {
				String[] variaveis = string.split(",");
				String key = variaveis[0];
				int quantity = Integer.parseInt(variaveis[1]);
			    ItemStack i = CoreStorage.getCustomItems().get(key).clone();
			    i.setAmount(quantity);
			    return i;
				
			}
		}
	public static ItemStack getItemStackFromString(String string) {
		if(!isCustomItem(string)) {
		String[] args = string.split("-");
		String[] variaveis = args[0].split(",");
		int data = Integer.valueOf(variaveis[1]);
		ItemStack item = new com.gabrielblink.galaxydungeons.apis.ItemBuilder(Material.getMaterial(Integer.valueOf(variaveis[0]))).setDurability((short)data).toItemStack();
		String nomedoitem = variaveis[2].replace("&", "§");
		int quantidade = Integer.valueOf(variaveis[3]);
		

		if(!nomedoitem.equalsIgnoreCase("null")){
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(nomedoitem);
			item.setItemMeta(itemMeta);
		}
		item.setAmount(quantidade);
		if(!variaveis[4].equalsIgnoreCase("null")){
			int encantamento1 = Integer.valueOf(variaveis[4]);
			int levelencantamento1 = Integer.valueOf(variaveis[5]);
			item.addUnsafeEnchantment(Enchantment.getById(encantamento1), levelencantamento1);
		}
		if(!variaveis[6].equalsIgnoreCase("null")){
			int encantamento2 = Integer.valueOf(variaveis[6]);
			int levelencantamento2 = Integer.valueOf(variaveis[7]);
			item.addUnsafeEnchantment(Enchantment.getById(encantamento2), levelencantamento2);
		}
		if(!variaveis[8].equalsIgnoreCase("null")){
			int encantamento3 = Integer.valueOf(variaveis[8]);
			int levelencantamento3 = Integer.valueOf(variaveis[9]);
			item.addUnsafeEnchantment(Enchantment.getById(encantamento3), levelencantamento3);
		}
		if(!variaveis[10].equalsIgnoreCase("null")){
			int encantamento4 = Integer.valueOf(variaveis[10]);
			int levelencantamento4 = Integer.valueOf(variaveis[11]);
			item.addUnsafeEnchantment(Enchantment.getById(encantamento4), levelencantamento4);
		}
		return item;
		}else {
			String[] variaveis = string.split(",");
			String key = variaveis[0];
			String[] others = variaveis[1].split("-");
			int quantity = Integer.parseInt(others[0]);
		    ItemStack i = CoreStorage.getCustomItems().get(key).clone();
		    i.setAmount(quantity);
		    return i;
			
		}
	}
	public static boolean isArmadura(ItemStack item){
		if(item.getType().equals(Material.DIAMOND_CHESTPLATE)){
			return true;
		}else if(item.getType().equals(Material.DIAMOND_BOOTS)){
			return true;
		}else if(item.getType().equals(Material.DIAMOND_LEGGINGS)){
			return true;
		}else if(item.getType().equals(Material.DIAMOND_HELMET)){
			return true;
		}else 		if(item.getType().equals(Material.GOLD_CHESTPLATE)){
			return true;
		}else if(item.getType().equals(Material.GOLD_BOOTS)){
			return true;
		}else if(item.getType().equals(Material.GOLD_LEGGINGS)){
			return true;
		}else if(item.getType().equals(Material.GOLD_HELMET)){
			return true;
		}else 		if(item.getType().equals(Material.LEATHER_CHESTPLATE)){
			return true;
		}else if(item.getType().equals(Material.LEATHER_BOOTS)){
			return true;
		}else if(item.getType().equals(Material.LEATHER_LEGGINGS)){
			return true;
		}else if(item.getType().equals(Material.LEATHER_HELMET)){
			return true;
		}else 		if(item.getType().equals(Material.IRON_CHESTPLATE)){
			return true;
		}else if(item.getType().equals(Material.IRON_BOOTS)){
			return true;
		}else if(item.getType().equals(Material.IRON_LEGGINGS)){
			return true;
		}else if(item.getType().equals(Material.IRON_HELMET)){
			return true;
		}else 		if(item.getType().equals(Material.CHAINMAIL_CHESTPLATE)){
			return true;
		}else if(item.getType().equals(Material.CHAINMAIL_BOOTS)){
			return true;
		}else if(item.getType().equals(Material.CHAINMAIL_LEGGINGS)){
			return true;
		}else if(item.getType().equals(Material.CHAINMAIL_HELMET)){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isFerramenta(ItemStack item){
		if(item.getType().equals(Material.IRON_SPADE)){
			return true;
		}else if(item.getType().equals(Material.IRON_PICKAXE)){
			return true;
		}else if(item.getType().equals(Material.IRON_AXE)){
			return true;
		}else if(item.getType().equals(Material.WOOD_SPADE)){
			return true;
		}else if(item.getType().equals(Material.WOOD_AXE)){
			return true;
		}else if(item.getType().equals(Material.WOOD_PICKAXE)){
			return true;
		}else if(item.getType().equals(Material.STONE_SPADE)){
			return true;
		}else if(item.getType().equals(Material.STONE_PICKAXE)){
			return true;
		}else if(item.getType().equals(Material.STONE_AXE)){
			return true;
		}else if(item.getType().equals(Material.DIAMOND_SPADE)){
			return true;
		}else if(item.getType().equals(Material.DIAMOND_AXE)){
			return true;
		}else if(item.getType().equals(Material.DIAMOND_PICKAXE)){
			return true;
		}else if(item.getType().equals(Material.GOLD_AXE)){
			return true;
		}else if(item.getType().equals(Material.GOLD_PICKAXE)){
			return true;
		}else if(item.getType().equals(Material.GOLD_SPADE)){
			return true;
		}else if(item.getType().equals(Material.WOOD_HOE)){
			return true;
		}else if(item.getType().equals(Material.STONE_HOE)){
			return true;
		}else if(item.getType().equals(Material.IRON_HOE)){
			return true;
		}else if(item.getType().equals(Material.GOLD_HOE)){
			return true;
		}else if(item.getType().equals(Material.DIAMOND_HOE)){
			return true;
		}else{
			return false;
		}
	}
	public static String getColorNameItem(ItemStack a) {
		if(isArmadura(a) || isFerramenta(a)) {
			return "§b";
		}else {
			return "§6";
		}
	}
	public static boolean containsItem(ItemStack i,Player p) {
		Dungeon d = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
		DungeonServer DS = CoreStorage.getDungeonServers().get(d.getDungeonName());
		for(ItemStack inDB : DS.getDungeon_drops_correio().get(p.getName())) {
			if(inDB.getType() == i.getType()) {
				if(inDB.getDurability() == i.getDurability()) {
					if(inDB.hasItemMeta() && i.hasItemMeta()) {
						if(inDB.getItemMeta().hasDisplayName() && i.getItemMeta().hasDisplayName()) {
							if(inDB.getItemMeta().getDisplayName().equalsIgnoreCase(i.getItemMeta().getDisplayName())) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	public static boolean containsItemRewards(ItemStack i,Player p) {
		Dungeon d = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
		DungeonServer DS = CoreStorage.getDungeonServers().get(d.getDungeonName());
		for(ItemStack inDB : DS.getDungeon().getFases().get(CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getFaseName()).getItemsCollecteds().get(p.getUniqueId())) {
			if(inDB.getType() == i.getType()) {
				if(inDB.getDurability() == i.getDurability()) {
					if(inDB.hasItemMeta() && i.hasItemMeta()) {
						if(inDB.getItemMeta().hasDisplayName() && i.getItemMeta().hasDisplayName()) {
							if(inDB.getItemMeta().getDisplayName().equalsIgnoreCase(i.getItemMeta().getDisplayName())) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	public static int getIndex(ItemStack i,Player p) {
		Dungeon d = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
		DungeonServer DS = CoreStorage.getDungeonServers().get(d.getDungeonName());
		int ia = 0;
		for(ItemStack inDB : DS.getDungeon_drops_correio().get(p.getName())) {
			if(inDB.getType() == i.getType()) {
				if(inDB.getDurability() == i.getDurability()) {
					if(inDB.hasItemMeta() && i.hasItemMeta()) {
						if(inDB.getItemMeta().hasDisplayName() && i.getItemMeta().hasDisplayName()) {
							if(inDB.getItemMeta().getDisplayName().equalsIgnoreCase(i.getItemMeta().getDisplayName())) {
								return ia;
							}
						}
					}
				}
			}
			ia++;
		}
		return ia;
	}
	public static int getIndexRewards(ItemStack i,Player p) {
		Dungeon d = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
		DungeonServer DS = CoreStorage.getDungeonServers().get(d.getDungeonName());
		int ia = 0;
		for(ItemStack inDB : DS.getDungeon().getFases().get(CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getFaseName()).getItemsCollecteds().get(p.getUniqueId())) {
			if(inDB.getType() == i.getType()) {
				if(inDB.getDurability() == i.getDurability()) {
					if(inDB.hasItemMeta() && i.hasItemMeta()) {
						if(inDB.getItemMeta().hasDisplayName() && i.getItemMeta().hasDisplayName()) {
							if(inDB.getItemMeta().getDisplayName().equalsIgnoreCase(i.getItemMeta().getDisplayName())) {
								return ia;
							}
						}
					}
				}
			}
			ia++;
		}
		return ia;
	}

	@EventHandler
	public void onCustomMobDeath(EntityDeathEvent event) {
		if(event.getEntity() != null) {
			if(event.getEntity().hasMetadata("GalaxyCustomMob")) {
				if(event.getEntity().getKiller() instanceof Player) {
				Player p = (Player)event.getEntity().getKiller();
					if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
						Dungeon d = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
						Fase currentFase = CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase();
							Random r = new Random();
							ArrayList<String> l = currentFase.getDrops();
							String sortuda = l.get(r.nextInt(l.size()));
							ItemStack item_sortudo = getItemStackFromString(sortuda);
							event.getDrops().clear();
							event.getDrops().forEach(i -> {
								i.setType(Material.AIR);
							});
							if(percentChance(getPercentageOfString(sortuda))) {
								Location locationofdeath = event.getEntity().getLocation();
								Item i = locationofdeath.getWorld().dropItemNaturally(locationofdeath, item_sortudo);
								i.setCustomNameVisible(true);
								if(isCustomItem(sortuda)) {
									i.setCustomName(item_sortudo.getItemMeta().getDisplayName());	
								}else {
									i.setCustomName(getColorNameItem(item_sortudo)+ItemName.valueOf(item_sortudo).getName());
								}
							}
									
					}
				}
			}
		}
	}
}
