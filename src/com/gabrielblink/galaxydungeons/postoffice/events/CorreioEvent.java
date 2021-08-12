package com.gabrielblink.galaxydungeons.postoffice.events;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.apis.HiddenStringUtils;
import com.gabrielblink.galaxydungeons.apis.ScrollerInventory;
import com.gabrielblink.galaxydungeons.listeners.DungeonGeneralEvents;
import com.gabrielblink.galaxydungeons.listeners.EntityFixer;
import com.gabrielblink.galaxydungeons.objects.Correio;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.postoffice.CorreioCommand;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class CorreioEvent implements Listener{

	   public static String toBase64(ItemStack item) throws IOException {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        DataOutputStream dataOutput = new DataOutputStream(outputStream);

	        NBTTagList nbtTagListItems = new NBTTagList();
	        NBTTagCompound nbtTagCompoundItem = new NBTTagCompound();

	        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

	        nmsItem.save(nbtTagCompoundItem);

	        nbtTagListItems.add(nbtTagCompoundItem);

				NBTCompressedStreamTools.a(nbtTagCompoundItem, (DataOutput) dataOutput);

	        return new BigInteger(1, outputStream.toByteArray()).toString(32);
	    }

	    /**
	     * Item from Base64
	     * @param data
	     * @return
	     * @throws IOException 
	     */
	    public static ItemStack fromBase64(String data) throws IOException {
	        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());

	        NBTTagCompound nbtTagCompoundRoot = NBTCompressedStreamTools.a(new DataInputStream(inputStream));

	        net.minecraft.server.v1_8_R3.ItemStack nmsItem = net.minecraft.server.v1_8_R3.ItemStack.createStack(nbtTagCompoundRoot);
	        ItemStack item = (ItemStack) CraftItemStack.asBukkitCopy(nmsItem);

	        return item;
	    }
		@EventHandler
		public void onCollectItem(PlayerPickupItemEvent event) {
			if (DungeonGeneralEvents.isSpectating(event.getPlayer())) {
				return;
			}
			if(event.getItem().isCustomNameVisible()) {
				Player p = (Player)event.getPlayer();
				if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
					if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
						Dungeon d = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
						DungeonServer DS = CoreStorage.getDungeonServers().get(d.getDungeonName());
						if(DS.getDungeon_drops_correio().containsKey(p.getName())) {
									DS.getDungeon_drops_correio().get(p.getName()).add(event.getItem().getItemStack());
								DS.getDungeon().getFases().get(CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getFaseName()).getItemsCollecteds().get(p.getUniqueId()).add(event.getItem().getItemStack());
							event.getItem().remove();
							event.setCancelled(true);
						}
					}
				}
			}
		}
	    @EventHandler
	    public void onClick3(InventoryClickEvent event) {
	    	if(event.getWhoClicked() instanceof Player) {
	    		Player p = (Player)event.getWhoClicked();
	    		if(event.getInventory().getName().contains("Correio")) {
	    			if(ScrollerInventory.users2.containsKey(p.getUniqueId())) {
	    				 ScrollerInventory inv = ScrollerInventory.users2.get(p.getUniqueId());
	    				 if(event.getSlot() == 26) {
	    					 if(inv.currpage >= inv.pages2.size()-1){
	 	    	                return;
	 	    	            }else{
	 	    	                //Next page exists, flip the page
	 	    	                inv.currpage += 1;
	 	    	                p.openInventory(inv.pages2.get(inv.currpage));
	 	    	            }
	    				 }
	    			  	if(event.getSlot() == 18) {
	    	        			 event.setCancelled(true);
	    	    	             //If the page number is more than 0 (So a previous page exists)
	    	    	             if(inv.currpage > 0){
	    	    	             //Flip to previous page
	    	    	                 inv.currpage -= 1;
	    	    	                 p.openInventory(inv.pages2.get(inv.currpage));
	    	    	             }
	    	        	}
	    			}
	    		}
	    	}
	    }
	    @EventHandler
	    public void rewards2(InventoryClickEvent event) {
	    	if(event.getInventory().getName().contains("Recompensas")) {
	    		event.setCancelled(true);
	    	}
	    }
	    @EventHandler
	    public void rewards(InventoryClickEvent event) {
	    	if(event.getWhoClicked() instanceof Player) {
	    		Player p = (Player)event.getWhoClicked();
	    		if(event.getInventory().getName().contains("Recompensas")) {
	    			if(ScrollerInventory.users2.containsKey(p.getUniqueId())) {
	    				 ScrollerInventory inv = ScrollerInventory.users2.get(p.getUniqueId());
	    				 if(event.getSlot() == 26) {
	    					 if(inv.currpage >= inv.pages2.size()-1){
	 	    	                return;
	 	    	            }else{
	 	    	                //Next page exists, flip the page
	 	    	                inv.currpage += 1;
	 	    	                p.openInventory(inv.pages2.get(inv.currpage));
	 	    	            }
	    				 }
	    			  	if(event.getSlot() == 18) {
	    	        			 event.setCancelled(true);
	    	    	             //If the page number is more than 0 (So a previous page exists)
	    	    	             if(inv.currpage > 0){
	    	    	             //Flip to previous page
	    	    	                 inv.currpage -= 1;
	    	    	                 p.openInventory(inv.pages2.get(inv.currpage));
	    	    	             }
	    	        	}
	    			}
	    		}
	    	}
	    }
	    @EventHandler
	    public void onClick2(InventoryClickEvent event) {
	    	if(event.getWhoClicked() instanceof Player) {
	    		Player p = (Player)event.getWhoClicked();
	    		if(event.getInventory().getName().contains("Correio")) {
	    			if(ScrollerInventory.users.containsKey(p.getUniqueId())) {
	    				 ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
	    				 if(event.getSlot() == 26) {
	    					 if(inv.currpage >= inv.pages.size()-1){
	 	    	                return;
	 	    	            }else{
	 	    	                //Next page exists, flip the page
	 	    	                inv.currpage += 1;
	 	    	                p.openInventory(inv.pages.get(inv.currpage));
	 	    	            }
	    				 }
	    			  	if(event.getSlot() == 18) {
	    	        			 event.setCancelled(true);
	    	    	             //If the page number is more than 0 (So a previous page exists)
	    	    	             if(inv.currpage > 0){
	    	    	             //Flip to previous page
	    	    	                 inv.currpage -= 1;
	    	    	                 p.openInventory(inv.pages.get(inv.currpage));
	    	    	             }
	    	        	}
	    			}
	    		}
	    	}
	    }
	 /*   @EventHandler(ignoreCancelled = true)
	    public void onClick(InventoryClickEvent event){
	       if(!(event.getWhoClicked() instanceof Player)) return;
	        Player p = (Player) event.getWhoClicked();
	        //Get the current scroller inventory the player is looking at, if the player is looking at one.
	        if(!ScrollerInventory.users2.containsKey(p.getUniqueId())) return;
	        ScrollerInventory inv = ScrollerInventory.users2.get(p.getUniqueId());
	        if(event.getCurrentItem() == null) return;
	        if(event.getCurrentItem().getItemMeta() == null) return;
	        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
	        //If the pressed item was a nextpage button
	        if(event.getInventory().getName().contains("Correio")) {
	        	if(event.getInventory().getItem(26)!=null) {
	        		if(event.getInventory().getItem(26).getItemMeta().getDisplayName().contains("Pagina")) {
	        	        event.setCancelled(true);
	    	            //If there is no next page, don't do anything
	    	            if(inv.currpage >= inv.pages2.size()-1){
	    	                return;
	    	            }else{
	    	                //Next page exists, flip the page
	    	                inv.currpage += 1;
	    	                p.openInventory(inv.pages2.get(inv.currpage));
	    	            }
	        		}
	        	}
	      
	        }
	        
	    }*/
	   /* @EventHandler(ignoreCancelled = true)
	    public void onClick2(InventoryClickEvent event){
	       if(!(event.getWhoClicked() instanceof Player)) return;
	        Player p = (Player) event.getWhoClicked();
	        //Get the current scroller inventory the player is looking at, if the player is looking at one.
	        if(!ScrollerInventory.users.containsKey(p.getUniqueId())) return;
	        ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
	        if(event.getCurrentItem() == null) return;
	        if(event.getCurrentItem().getItemMeta() == null) return;
	        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
	        //If the pressed item was a nextpage button
	        if(event.getInventory().getName().contains("Correio")) {
	        	if(event.getInventory().getItem(26)!=null) {
	        		if(event.getInventory().getItem(26).getItemMeta().getDisplayName().contains("Pagina")) {
	        	        event.setCancelled(true);
	    	            //If there is no next page, don't do anything
	    	            if(inv.currpage >= inv.pages.size()-1){
	    	                return;
	    	            }else{
	    	                //Next page exists, flip the page
	    	                inv.currpage += 1;
	    	                p.openInventory(inv.pages.get(inv.currpage));
	    	            }
	        		}
	        	}
	        	if(event.getSlot() == 18) {
	        			 event.setCancelled(true);
	    	             //If the page number is more than 0 (So a previous page exists)
	    	             if(inv.currpage > 0){
	    	             //Flip to previous page
	    	                 inv.currpage -= 1;
	    	                 p.openInventory(inv.pages.get(inv.currpage));
	    	             }
	        	}
	        }
	        
	    }*/
	    @EventHandler
	    public void collectItem(InventoryClickEvent event) {
	    	if(event.getSlotType().equals(SlotType.OUTSIDE)) {
	    		return;
	    	}
	    	if(event.getInventory().getName().contains("Correio")) {
	    		if(HiddenStringUtils.extractHiddenString(event.getInventory().getName())!=null) {
	    	     if(HiddenStringUtils.extractHiddenString(event.getInventory().getName()).equalsIgnoreCase("ec")) {
	    			if(event.getCurrentItem()!=null) {
	    				Player p = (Player)event.getWhoClicked();
	    				if(event.getClickedInventory().equals(p.getInventory())) {
	    					event.setCancelled(true);
	    					return;
	    				}
	    				if(event.getCurrentItem().getType().equals(Material.ARROW)) {
	    					if(event.getCurrentItem().hasItemMeta()) {
		    					if(event.getCurrentItem().getItemMeta().hasDisplayName()) {
	    					if(event.getCurrentItem().getItemMeta().getDisplayName().contains("§aVoltar")) {
	    						event.setCancelled(true);
    							new BukkitRunnable() {
									
									@Override
									public void run() {
										CorreioCommand.open(p);
									}
								}.runTaskLater(Main.getPlugin(Main.class), 3L);
	    						return;
	    					}else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Pagina")) {
	    						event.setCancelled(true);
	    						return;
	    					}
		    					}
	    					}
	    				}
	    				String correio_id = HiddenStringUtils.extractHiddenString(event.getInventory().getItem(49).getItemMeta().getDisplayName());
	    				if(event.getCurrentItem()!=null) { 
	    				try {
	    					 event.setCancelled(true);
							String itemstack_base64 = toBase64(event.getCurrentItem());
							CoreStorage.getStoraged_users().get(p.getName()).getCorreio().get(correio_id).getItens().remove(itemstack_base64);
							p.sendMessage("§aItem coletado com sucesso!");
							p.getInventory().addItem(event.getCurrentItem());
							if(CoreStorage.getStoraged_users().get(p.getName()).getCorreio().get(correio_id).getItens().size() == 0) {
								CoreStorage.getStoraged_users().get(p.getName()).getCorreio().remove(correio_id);
								p.closeInventory();
							}else {
							CorreioCommand.abrirEncomenda(CoreStorage.getStoraged_users().get(p.getName()).getCorreio().get(correio_id), p);
							}
							} catch (IOException e) {
								p.closeInventory();
								p.sendMessage("§cAguarde alguns segundos e tente novamente.");
						}
	    			}
	    		        }
	    	     }
	    		}
	    	}
	    }
	    @EventHandler
	    public void comeback(InventoryClickEvent event) {
	    	if(event.getInventory().getName().contains("Correio")) {
	    		if(event.getCurrentItem()!=null) {
	    			if(event.getCurrentItem().getType().equals(Material.ARROW)) {
	    				if(event.getCurrentItem().hasItemMeta()) {
	    					if(event.getCurrentItem().getItemMeta().hasDisplayName()) {
	    						if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aVoltar")) {
	    							Player p = (Player)event.getWhoClicked();
	    							event.setCancelled(true);
	    							new BukkitRunnable() {
										
										@Override
										public void run() {
											CorreioCommand.open(p);
										}
									}.runTaskLater(Main.getPlugin(Main.class), 3L);
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	
	@EventHandler
	public void abrirEncomenda(InventoryClickEvent event) {
		if(event.getInventory().getName().equalsIgnoreCase("Correio")) {
			if(event.getCurrentItem()!=null) {
				if(event.getCurrentItem().hasItemMeta()) {
					if(event.getCurrentItem().getItemMeta().hasDisplayName()) {
						if(event.getCurrentItem().getItemMeta().hasLore()) {
						if(HiddenStringUtils.extractHiddenString(event.getCurrentItem().getItemMeta().getLore().get(0))!=null) {
							Player p = (Player)event.getWhoClicked();
							String id = HiddenStringUtils.extractHiddenString(event.getCurrentItem().getItemMeta().getLore().get(0));
							GalaxyDungeonUser gu = CoreStorage.getStoraged_users().get(p.getName());
							CorreioCommand.abrirEncomenda(gu.getCorreio().get(id), p);
						}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void correio(InventoryClickEvent event) {
		if(event.getInventory().getName().equalsIgnoreCase("Correio")) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void a(InventoryCloseEvent event) {
		if(event.getInventory().getName().contains("Encomenda")) {
			Player p = (Player)event.getPlayer();
			ArrayList<String> contents = new ArrayList<String>();
			for(ItemStack i : event.getInventory().getContents()) {
				if(i!=null) {
				try {
					contents.add(toBase64(i));
				} catch (IOException e) {
				}
			}
			}
			String generated_uuid = UUID.randomUUID().toString();
			Correio c = new Correio(generated_uuid, contents, p.getName());
			String[] args = event.getInventory().getName().split("Encomenda");
			String tosend = args[1].replace(" ", "");
			if(contents.size() > 0) {
				CoreStorage.getStoraged_users().get(tosend).getCorreio().put(generated_uuid, c);
				p.sendMessage("§aEncomenda enviada!");
			}else {
				p.sendMessage("§cEncomenda cancelada.");
			}
			}
	}
}
