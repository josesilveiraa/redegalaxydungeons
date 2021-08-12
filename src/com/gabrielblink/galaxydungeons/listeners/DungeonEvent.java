package com.gabrielblink.galaxydungeons.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.apis.Config;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.Fase;
import com.gabrielblink.galaxydungeons.objects.Party;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class DungeonEvent implements Listener
{

	public static String getDungeonKeyByFancyName(String fancy_name) {
		for(String s : CoreStorage.getDungeons().keySet()) {
			Dungeon d = CoreStorage.getDungeons().get(s);
			if(ChatColor.stripColor(d.getItem_stack().getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(fancy_name))) {
				return s;
			}
		}
		return null;
	}
	@EventHandler
	public void a(EntityChangeBlockEvent eat) {
		if(CoreStorage.worlds_registreds_names.contains(eat.getBlock().getWorld().getName())) {
		 if (eat.getEntity().getType() == EntityType.WITHER_SKULL){
             eat.setCancelled(true);
         }
         if (eat.getEntity().getType() == EntityType.WITHER){
             eat.setCancelled(true);
         }
         if(eat.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
        	 eat.setCancelled(true);
         }
		}
	}
	public static boolean canJoin(Player p) {
		if(p.getInventory().getHelmet() == null) {
			if(p.getInventory().getChestplate() == null) {
				if(p.getInventory().getBoots() == null) {
					if(p.getInventory().getLeggings() == null) {
						ArrayList<ItemStack> itens = new ArrayList<ItemStack>();
						for(ItemStack i : p.getInventory().getContents()) {
							if(i!=null) {
							itens.add(i);
							}
						}
						if(itens.size() == 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static Fase getFirstFase(Dungeon d) {
		Fase f = null;
		int i = 0;
		for(String c : d.getFases().keySet()) {
			if(i == 0) {
				f = d.getFases().get(c);
			}
			i++;
		}
		return f;
	}
	public static boolean dungeonIsStable(Dungeon d) {
		if(d.getArmor_inicial().size() < 4) {
			Bukkit.getConsoleSender().sendMessage("§C[GalaxyDungeons] Armaduras iniciais erro.");
			return false;
		}
		if(d.getFases().size() < 1) {
			Bukkit.getConsoleSender().sendMessage("§C[GalaxyDungeons] Exception ao iniciar a dungeon: Nenhuma fase cadastrada.");
			return false;
		}
		if(d.getLobby() == null) {
			Bukkit.getConsoleSender().sendMessage("§C[GalaxyDungeons] Localização do Lobby da dungenon "+d.getDungeonName()+" não cadastrada.");
			return false;
		}
	    for(Fase f : d.getFases().values()) {
	    	if(f.getGate() == null) {
	    		Bukkit.getConsoleSender().sendMessage("§C[GalaxyDungeons] A Dungeon "+d.getDungeonName()+" não tem um portão(Gate) setado.");
	    		return false;
	    	}
	    		if(f.getLocations_mob_spawn().size() < 1) {
	    			Bukkit.getConsoleSender().sendMessage("§C[GalaxyDungeons] A Dungeon "+d.getDungeonName()+" não tem nenhuma localização para os Mobs serem spawnados.");
	    			return false;
	    		}else {
	    			if(f.getMobs().size() < 1) {
	    				Bukkit.getConsoleSender().sendMessage("§C[GalaxyDungeons] A Dungeon "+d.getDungeonName()+" não tem nenhum mob cadastrado.");
	    				return false;
	    			}else {
	    				return true;
	    			}
	    			}
	    }
	    return false;
	}
	public boolean allPartyMembersOn(Party p) {
		for(String s : p.getPartyMembers()) {
			if(Bukkit.getPlayer(s)==null) {
				return false;
			}
		}
		return true;
	}
	public boolean AllMembersContainsIngress(Party p,String dungeonName) {
		for(String member : p.getPartyMembers()) {
			if(!CoreStorage.getStoraged_users().get(member).getAcessos_dungeon().containsKey(dungeonName)) {
				return false;
			}
		}
		return true;
	}
	public String getFilhoDaPutaquenaotemoingresso(Party p,String dungeonName) {
		for(String member : p.getPartyMembers()) {
			if(!CoreStorage.getStoraged_users().get(member).getAcessos_dungeon().containsKey(dungeonName)) {
				return member;
			}
		}
		return null;
	}
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(event.getInventory().getName().equalsIgnoreCase("Dungeons")) {
			event.setCancelled(true);
			if(event.getCurrentItem()!=null) {
				Player p =(Player)event.getWhoClicked();
				if(event.getCurrentItem().getType().isTransparent()) {
					return;
				}
				if(!event.getCurrentItem().hasItemMeta()) {
					return;
				}
				if(!event.getCurrentItem().getItemMeta().hasDisplayName()) {
					return;
				}
				if(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName())!=null) {
					Dungeon d = CoreStorage.getDungeons().get(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()));
					if(!event.getCurrentItem().getItemMeta().getLore().contains("§aClique para jogar!")) {
						if(event.getCurrentItem().getItemMeta().getLore().contains("§cVocê não tem coins suficientes.")) {
							p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						}else if(event.getCurrentItem().getItemMeta().getLore().contains("§aClique para comprar! §7(2 acessos)")) {
						     if(Main.getEconomy().getBalance(p.getName()) >= d.getPrice()) {
						    	   Main.getEconomy().withdrawPlayer(p.getName(), d.getPrice()); //removing money from player
						    	   p.sendMessage("§aParabéns , você comprou 2 acessos para a dungeon "+d.getFancyDungeonName());
						    	   if(CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().containsKey(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()))) {
						    	  int old_value = CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().get(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()));
						    	   CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().put(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()), old_value+2);
						    	   }else {
						    		   CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().put(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()), 2);
						    	   }
						    	   p.closeInventory();
						    	   p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
						     }else {
						    	    p.closeInventory();
						    	    p.sendMessage("§cOcorreu um erro , tente novamente mais tarde.");
						    		p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						     }
						}
					}else {
						
						if(canJoin(p)) {
							if(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName())!=null) {
								if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
									p.sendMessage("§cVocê já está em uma dungeon!.");
									return;
								}
								Dungeon d1 = CoreStorage.getDungeons().get(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()));
					   	   if(dungeonIsStable(d1)) {
					   		   if(CoreStorage.getStoraged_users().get(p.getName()).hasParty()) {
					   			   
					   			for(String s : CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers()) {
					   				if(Bukkit.getPlayer(s)==null) {
					   					p.sendMessage("§cDesculpe mas o jogador "+s+" da sua party está offline.");
					   					return;
					   				}else
					   				{
					   					if(CoreStorage.getStoraged_users().get(s).isInDungeon()) {
					   						p.sendMessage("§cDesculpe mas o jogador "+s+" está ainda na dungeon.");
					   						return;
					   					}else if(!canJoin(Bukkit.getPlayer(s))) {
					   						p.sendMessage("§cDesculpe mas o jogador "+s+" contém itens em seu inventário.");
					   						return;
					   					}
					   				}
					   		      }
					   			   if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers().size() >= (int)Config.get("Geral.MinJogadores")) {
								    if(CoreStorage.getDungeonServers().containsKey(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()))) {
								    	if(CoreStorage.getDungeonServers().get(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName())).getParty().getPartyID() != CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyID()) {
								    		p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
									    	p.sendMessage("§cDesculpe mas o servidor desta dungeon já está sendo ocupado por outra party de jogadores, Tente novamente mais tarde.");
									    	p.closeInventory();
									    	return;
								    	}else {
								    		
								    	}
								    }
								    	 if(allPartyMembersOn(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty())) {
								    		 if(AllMembersContainsIngress(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty(), getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()))) {
												    if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider().equalsIgnoreCase(p.getName())) {
								    			 p.closeInventory();
				    	              p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
				    	              DungeonServer du = new DungeonServer(d1,CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty(),getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()));
				    	              du.joinServer(p.getName());
				    	              CoreStorage.getStoraged_users().get(p.getName()).setCurrentDungeon(d1);
				    	              CoreStorage.getStoraged_users().get(p.getName()).setCurrentFase(getFirstFase(d1));
				    	              CoreStorage.getDungeonServers().put(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName()), du);
				    	              for(String s : CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers()) {
				    	            	  if(!s.equalsIgnoreCase(p.getName())) {
				    	            		  CoreStorage.getStoraged_users().get(s).setCurrentDungeon(d1);
						    	              CoreStorage.getStoraged_users().get(s).setCurrentFase(getFirstFase(d1));
						    	              if(CoreStorage.getStoraged_users().get(s).getAcessos_dungeon().get(d1.getDungeonName()) > 1) {
						    	              CoreStorage.getStoraged_users().get(s).getAcessos_dungeon().put(d1.getDungeonName(), CoreStorage.getStoraged_users().get(s).getAcessos_dungeon().get(d1.getDungeonName())-1);
						    	              }else {
						    	            	  CoreStorage.getStoraged_users().get(s).getAcessos_dungeon().remove(d1.getDungeonName());
						    	              }
						    	              CoreStorage.getDungeonServers().get(getDungeonKeyByFancyName(event.getCurrentItem().getItemMeta().getDisplayName())).joinServer(s);
				    	            	  }
				    	              }
				    	              if(CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().get(d1.getDungeonName()) > 1) {
					    	              CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().put(d1.getDungeonName(), CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().get(d1.getDungeonName())-1);
					    	              }else {
					    	            	  CoreStorage.getStoraged_users().get(p.getName()).getAcessos_dungeon().remove(d1.getDungeonName());
					    	              }
				    	              p.sendMessage("§aSucesso! Enviando sua party para a dungeon...");
								    		 
												    }else {
												    	p.sendMessage("§cApenas o líder da party pode entrar na dungeon.");
												    	p.closeInventory();
												    	p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
												    }
												    
								    		 }else {
								     			 p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
									   			   p.sendMessage("§cTodos os jogadores da party precisam conter o ingresso desta dungeon.");
									   			   p.closeInventory();
								     		 }
								    	 }else {
								    		 p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								   			   p.sendMessage("§cTodos os jogadores da party precisam estar online.");
								   			   p.closeInventory();
								    	 }
								    
					   			   }else {
					   				   p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					   				   p.sendMessage("§cSua party precisa no mínimo de "+Config.get("Geral.MinJogadores")+" jogadores para entrar em uma dungeon.");
					   				   p.closeInventory();
					   			   }
					   		   }else {
					   			   p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					   			   p.sendMessage("§cVocê precisa de uma party para entrar nas dungeons.");
					   			   p.closeInventory();
					   		   }
				    	   
					   	   }else {
					   		   p.sendMessage("§cErro: Dungeon não configurada corretamente.");
					   		   p.closeInventory();
					   	   }
							}
						}else {
							p.closeInventory();
							p.sendMessage("§cVocê não pode entrar na dungeon com armaduras ou itens em seu inventário.");
							return;
						}
						//join in the dungeon.
					}
				}
			}
		}
	}
}
