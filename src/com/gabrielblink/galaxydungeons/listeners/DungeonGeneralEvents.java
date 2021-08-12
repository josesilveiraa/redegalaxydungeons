
package com.gabrielblink.galaxydungeons.listeners;


import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.apis.ItemBuilder;
import com.gabrielblink.galaxydungeons.apis.Title;
import com.gabrielblink.galaxydungeons.commands.CommandDungeon;
import com.gabrielblink.galaxydungeons.enums.DungeonMobStatus;
import com.gabrielblink.galaxydungeons.enums.DungeonStatus;
import com.gabrielblink.galaxydungeons.events.AsyncDungeonWinEvent;
import com.gabrielblink.galaxydungeons.events.DungeonLostEvent;
import com.gabrielblink.galaxydungeons.events.DungeonWinEvent;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.Fase;
import com.gabrielblink.galaxydungeons.scoreboard.ScoreUpdater;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class DungeonGeneralEvents implements Listener{

	public static boolean isSpectating(Player p) {
		  Player targetSetting = p;
		   if(CoreStorage.getStoraged_users().containsKey(targetSetting.getName())) {
			   if(CoreStorage.getStoraged_users().get(targetSetting.getName()).isInDungeon()) {
				   DungeonServer DS = CoreStorage.getDungeonServers().get(CoreStorage.getStoraged_users().get(targetSetting.getName()).getCurrentDungeon().getDungeonName());
				   if(DS.getDeathPlayers().contains(targetSetting.getName())) {
					   return true;
				   }
			   }
		   }
		return false;
	}
	@EventHandler
	public void onInteractWithExit(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = event.getPlayer();
			if(isSpectating(p)) {
				if(p.getItemInHand().getType().equals(Material.REDSTONE_TORCH_ON)) {
					if(p.getItemInHand().hasItemMeta()) {
						if(p.getItemInHand().getItemMeta().hasDisplayName()) {
							if(p.getItemInHand().getItemMeta().hasLore()) {
								if(!CoreStorage.getStoraged_users().containsKey(p.getName())) {
									return;
								}
								if(!CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
									return;
								}
								Dungeon currentDungeon = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
								DungeonServer DS = CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName());
								if(DS.getDungeonStatus().equals(DungeonStatus.RUNNING) || DS.getDungeonStatus().equals(DungeonStatus.WAITING_FOR_PLAYERS_GO_THROUGHT_GATE)) {
									CoreStorage.getStoraged_users().get(p.getName()).setCurrentDungeon(null);
									CoreStorage.getStoraged_users().get(p.getName()).setCurrentFase(null);
									p.getInventory().clear();
									p.getInventory().setArmorContents(null);
									p.teleport(CoreStorage.spawn);
									DS.getDeathPlayers().remove(p.getName());
									DS.getOld_itens().remove(p.getName());
									DS.getPlayers_pepitas().remove(p.getName());
									DS.getPlayerStatus().remove(p.getName());
									for(String memberofParty : DS.getParty().getPartyMembers()) {
										if(Bukkit.getPlayer(memberofParty)!=null) {
											Player t = Bukkit.getPlayer(memberofParty);
											if(!t.getName().equalsIgnoreCase(p.getName())) {
											t.sendMessage("§cO jogador "+p.getName()+" abandonou a dungeon.");
											}
											}
									}
									p.sendMessage("§cVocê abandonou a Dungeon.");
								}else {
									p.sendMessage("§cVocê não pode abandonar a dungeon.");
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onInventory(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			if(isSpectating((Player)event.getWhoClicked())){
				event.setCancelled(true);
				Player p = (Player)event.getWhoClicked();
				Dungeon currentDungeon = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
				DungeonServer DS = CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName());
				if(event.getSlot() == 5) {
					if(DS.getDungeonStatus().equals(DungeonStatus.RUNNING) || DS.getDungeonStatus().equals(DungeonStatus.WAITING_FOR_PLAYERS_GO_THROUGHT_GATE)) {
					CoreStorage.getStoraged_users().get(p.getName()).setCurrentDungeon(null);
					CoreStorage.getStoraged_users().get(p.getName()).setCurrentFase(null);
					p.getInventory().clear();
					p.getInventory().setArmorContents(null);
					p.teleport(CoreStorage.spawn);
					DS.getDeathPlayers().remove(p.getName());
					DS.getOld_itens().remove(p.getName());
					DS.getPlayers_pepitas().remove(p.getName());
					DS.getPlayerStatus().remove(p.getName());
					for(String memberofParty : DS.getParty().getPartyMembers()) {
						if(Bukkit.getPlayer(memberofParty)!=null) {
							Player t = Bukkit.getPlayer(memberofParty);
							if(!t.getName().equalsIgnoreCase(p.getName())) {
							t.sendMessage("§cO jogador "+p.getName()+" abandonou a dungeon.");
							}
							}
					}
					p.sendMessage("§cVocê abandonou a Dungeon.");
				}else {
					p.sendMessage("§cVocê não pode abandonar a dungeon.");
				}
				}
			}
		}
	}
    @EventHandler
    protected void onPlayerDropItem(final PlayerDropItemEvent e) {
        if (isSpectating(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    protected void onPlayerPickupItem(final PlayerPickupItemEvent e) {
        if (isSpectating(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    protected void onEntityDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && !e.getEntity().hasMetadata("NPC") && isSpectating((Player) e.getEntity())) {
            e.setCancelled(true);
            e.getEntity().setFireTicks(0);
        }
    }
    
    @EventHandler
    protected void onFoodLevelChange(final FoodLevelChangeEvent e) {
    	if(e.getEntity() == null) {
    		return;
    	}
        if (e.getEntity() instanceof Player && !e.getEntity().hasMetadata("NPC") && isSpectating((Player) e.getEntity())) {
            e.setCancelled(true);
            ((Player)e.getEntity()).setFoodLevel(20);
            ((Player)e.getEntity()).setSaturation(20.0f);
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
    	Player p = event.getPlayer();
    	if(isSpectating(p)) {
    		Dungeon currentDungeon = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
			DungeonServer DS = CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName());
			for(String dungeonFriends : DS.getPlayers_pepitas().keySet()) {
				if(!dungeonFriends.equalsIgnoreCase(p.getName())) {
					if(Bukkit.getPlayer(dungeonFriends)!=null) {
						Player target = Bukkit.getPlayer(dungeonFriends);
						target.showPlayer(p);
					}
				}
			}
    	}
    }
	@EventHandler
	public void removeTargetOnSpect(EntityTargetEvent e) {
		   if (e.getTarget() != null && e.getTarget() instanceof Player && !e.getTarget().hasMetadata("NPC")) {
			   Player targetSetting = (Player) e.getTarget();
			   if(CoreStorage.getStoraged_users().containsKey(targetSetting.getName())) {
				   if(CoreStorage.getStoraged_users().get(targetSetting.getName()).isInDungeon()) {
					   DungeonServer DS = CoreStorage.getDungeonServers().get(CoreStorage.getStoraged_users().get(targetSetting.getName()).getCurrentDungeon().getDungeonName());
					   if(DS.getDeathPlayers().contains(targetSetting.getName())) {
						   e.setCancelled(true);
					   }
				   }
			   }
		   }
	}
	@EventHandler
	public void onFire(EntityDamageEvent event) {
		if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)){
			if(event.getEntity() instanceof Player) {
				Player p = (Player)event.getEntity();
				if(CoreStorage.worlds_registreds_names.contains(p.getLocation().getWorld().getName())) {
							if(isSpectating(p)) {
								event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler
	public void onRejoin(PlayerJoinEvent event) {
		event.getPlayer().resetTitle();
	}
	@EventHandler
	public void onQuitDungeon(PlayerQuitEvent event) {
		Player p = (Player)event.getPlayer();
		if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
			if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
				Dungeon currentDungeon = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
				DungeonServer DS = CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName());
				
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				if(DS.getDungeonStatus().equals(DungeonStatus.WAITING_FOR_OK)) {
					Title t = new Title("", "", 1, 1, 1);
					t.send(p); //S
				}
				p.teleport(CoreStorage.spawn);
				DS.getDeathPlayers().remove(p.getName());
				DS.getOld_itens().remove(p.getName());
				DS.getPlayers_pepitas().remove(p.getName());
				DS.getPlayerStatus().remove(p.getName());
				for(String memberofParty : DS.getParty().getPartyMembers()) {
					if(Bukkit.getPlayer(memberofParty)!=null) {
						Player t = Bukkit.getPlayer(memberofParty);
						if(!t.getName().equalsIgnoreCase(p.getName())) {
						t.sendMessage("§cO jogador "+p.getName()+" abandonou a dungeon.");
						}
						}
				}
				if(DS.getPlayers_pepitas().size() <= 0) {
					CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName()).stopServer();
					CoreStorage.getDungeonServers().remove(currentDungeon.getDungeonName());
				}
				CoreStorage.getStoraged_users().get(p.getName()).setCurrentDungeon(null);
				CoreStorage.getStoraged_users().get(p.getName()).setCurrentFase(null);
				p.sendMessage("§cVocê abandonou a Dungeon.");
			}
		}
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			Player p = event.getEntity();
			if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
				if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
					if(CoreStorage.worlds_registreds_names.contains(p.getLocation().getWorld().getName())) {
						Dungeon currentDungeon = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon();
						DungeonServer DS = CoreStorage.getDungeonServers().get(currentDungeon.getDungeonName());
						if(DS.getDeathPlayers().size()+1 == DS.getParty().getPartyMembers().size()) {
							event.getDrops().clear();
							String nameServer = currentDungeon.getDungeonName();
							DS.stopServer();
							p.setFireTicks(0);
							p.spigot().respawn();
							p.teleport(CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getLocation_players_spawn());
							
							DungeonLostEvent onLost = new DungeonLostEvent(DS.getParty(), DS.getParty().getPartyLider(), currentDungeon, DS);
							Bukkit.getPluginManager().callEvent(onLost);
							for(String dungeonFriends : DS.getPlayers_pepitas().keySet()) {
								if(Bukkit.getPlayer(dungeonFriends)!=null) {
									Bukkit.getPlayer(dungeonFriends).setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
									Player ta = Bukkit.getPlayer(dungeonFriends);
									Title t = new Title("§c§lDerrota!", "§7Você perdeu na "+CoreStorage.getStoraged_users().get(dungeonFriends).getCurrentFase().getFaseNumber()+" fase", 3, 110, 3);
									t.send(ta);
									ta.setGameMode(GameMode.ADVENTURE);
									ta.setAllowFlight(true);
									ta.setFlying(true);
									new BukkitRunnable() {
										
										@Override
										public void run() {
												ta.getInventory().setItem(3, new ItemBuilder(Material.GLASS_BOTTLE).setName("§cPoção de Ressureição").setLore("§7Seja reanimado na mesma fase","§7em que você foi eliminado.").toItemStack());
											ta.getInventory().setItem(5, new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("§cAbandonar Dungeon").setLore("§7Clique com o botão direito neste item","§7para sair/desistir da dungeon.").toItemStack());
										}
									}.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 20L*1);
								}
								CoreStorage.getStoraged_users().get(dungeonFriends).setCurrentDungeon(null);
								CoreStorage.getStoraged_users().get(dungeonFriends).setCurrentFase(null);
								
								//Teleport all players to spawn.
							}
							new BukkitRunnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									for(String dungeonFriends : DS.getPlayers_pepitas().keySet()) {
										if(Bukkit.getPlayer(dungeonFriends)!=null) {
									if(CoreStorage.spawn != null) {
										Bukkit.getPlayer(dungeonFriends).getInventory().clear();
										Bukkit.getPlayer(dungeonFriends).teleport(CoreStorage.spawn);
									}else {
										Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeons] [!] ERRO: Spawn geral não encontrado.");
									}
										}
									}
								}
							}.runTaskLater(Main.getPlugin(Main.class), 20L*8);
							
						  CoreStorage.getDungeonServers().remove(nameServer);
						  
						}else {
							ItemStack[] [] store = new ItemStack[2][1];
					        store[0] = p.getInventory().getContents();
					        store[1] = p.getInventory().getArmorContents();
						DS.getOld_itens().put(p.getName(), store);
						event.getDrops().clear();
						p.setFireTicks(0);
						p.spigot().respawn();
						DS.getDeathPlayers().add(p.getName());
						p.teleport(CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getLocation_players_spawn());
						Title t = new Title("§c§lVocê morreu!", "", 3, 100, 3);
						t.send(p);
						for(String dungeonFriends : DS.getPlayers_pepitas().keySet()) {
							if(!dungeonFriends.equalsIgnoreCase(p.getName())) {
								if(Bukkit.getPlayer(dungeonFriends)!=null) {
									Player target = Bukkit.getPlayer(dungeonFriends);
									target.hidePlayer(p);
								}
							}
						}
						p.setGameMode(GameMode.ADVENTURE);
						p.setAllowFlight(true);
						p.setFlying(true);
						new BukkitRunnable() {
							
							@Override
							public void run() {
								if(!DS.getPlayersCanRevive().contains(p.getName())) {
									p.getInventory().setItem(3, new ItemBuilder(Material.GLASS_BOTTLE).setName("§cPoção de Ressureição").setLore("§7Seja reanimado na mesma fase","§7em que você foi eliminado.").toItemStack());
								}else {
									p.getInventory().setItem(3, new ItemBuilder(Material.POTION).setName("§aPoção de Ressureição").setLore("§7Seja reanimado na mesma fase","§7em que você foi eliminado.").toItemStack());
								}
								p.getInventory().setItem(5, new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("§cAbandonar Dungeon").setLore("§7Clique com o botão direito neste item","§7para sair/desistir da dungeon.").toItemStack());
							}
						}.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 20L*1);
						}
					}
				}
			}
		}
	}
	
	
	public static Fase getNovaFase(Player p) {
		String currentFaseName = CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getFaseName();
		String[] args = currentFaseName.split("_");
		int number = Integer.parseInt(args[1]);
		int next = number+1;
		String argument = "_"+next;
		String nextFaseName = "";
		for(String fase : CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon().getFases().keySet()) {
			if(fase.contains(argument)) {
				nextFaseName = fase;
				break;
			}
		}
		return CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon().getFases().get(nextFaseName);
	}
	@EventHandler
	public void onInteractWithRewards(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(event.getClickedBlock()!=null) {
				if(event.getClickedBlock().hasMetadata("Rewards")) {
					Player p = event.getPlayer();
					if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
						if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
							event.setCancelled(true);
							CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().showRecompensas(p);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onTryPlace(BlockPlaceEvent event) {
		if(CoreStorage.worlds_registreds_names.contains(event.getBlock().getLocation().getWorld().getName())) {
			if(event.getPlayer().isOp()) {
				return;
			}
			if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				return;
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onTryBreak(BlockBreakEvent event) {
		if(CoreStorage.worlds_registreds_names.contains(event.getBlock().getLocation().getWorld().getName())) {
			if(event.getPlayer().isOp()) {
				return;
			}
			if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				return;
			}
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void definindo(BlockBreakEvent event) {
		Player p = event.getPlayer();
		if(CommandDungeon.settingChests.containsKey(p.getUniqueId())) {
			Block breaked = event.getBlock();
			String[] args = CommandDungeon.settingChests.get(p.getUniqueId()).split("-");
			String dungeon_name = args[0];
			String dungeon_fase = args[1];
			CoreStorage.getDungeons().get(dungeon_name).getFases().get(dungeon_fase).setChestLocation(CoreStorage.getSerializedLocation(breaked.getLocation())+"~"+args[2]);
			Bukkit.getConsoleSender().sendMessage("§f"+CoreStorage.getDungeons().get(dungeon_name).getFases().get(dungeon_fase).getChestLocation());
			Main.getInstance().getConfig().set("Dungeons."+dungeon_name+".Fases."+dungeon_fase+".ChestLocation", CoreStorage.getSerializedLocation(breaked.getLocation())+"~"+args[2]);
			Main.getInstance().saveConfig();
			p.sendMessage("§aBaú de recompensas definido com sucesso.");
			CommandDungeon.settingChests.remove(p.getUniqueId());
			event.setCancelled(true);
		}
	}
	private Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}
	public void launchFirework(Player p, int speed) {
		Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
       
        //Our random generator
        Random r = new Random();   

        //Get the type
        int rt = r.nextInt(4) + 1;
        Type type = Type.BALL;       
        if (rt == 1) type = Type.BALL;
        if (rt == 2) type = Type.BALL_LARGE;
        if (rt == 3) type = Type.BURST;
        if (rt == 4) type = Type.CREEPER;
        if (rt == 5) type = Type.STAR;
       
        //Get our random colours   
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);
       
        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
       
        //Then apply the effect to the meta
        fwm.addEffect(effect);
       
        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
       
        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);           
	}
	@EventHandler
	public void death(EntityDeathEvent event) {
		if(event.getEntity().getKiller() instanceof Player) {
          Player p = (Player)event.getEntity().getKiller();
          if(CoreStorage.worlds_registreds_names.contains(event.getEntity().getLocation().getWorld().getName())) {
          if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
        	  if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
        		  String dungeon_name = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon().getDungeonName();
        		  DungeonServer DS = CoreStorage.getDungeonServers().get(dungeon_name);
        		  if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase()!=null) {
        			  if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().hasBoss()) {
        				  if(event.getEntity().hasMetadata("GalaxyCustomMob")) {
        					  if(event.getEntity().isCustomNameVisible()) {
        					  if(event.getEntity().getCustomName().equalsIgnoreCase("§c§lBOSS")) {
        						  LivingEntity lv = (LivingEntity)DS.getBOSS();
        						  lv.setHealth(0);
        						  DS.setBOSS(null);
        					  }
        					  }
        				  }
        			  }
        		  }
        		  if(DS.getDungeonStatus().equals(DungeonStatus.RUNNING)) {
        			  if(DS.getEntitysALive() != 0) {
        			  ScoreUpdater.updateScoreBoard(p, DS);
        			  }else {
        				  if(DS.getBOSS() != null) {
        					  return;
        				  }
        				  if(!DS.isFinalFase(p)) {
        				  if(DS.getDungeonMobStatus().equals(DungeonMobStatus.NOSPAWNING)) {
        					  if(!DS.getDungeonStatus().equals(DungeonStatus.WAITING_FOR_PLAYERS_GO_THROUGHT_GATE)) {
        				  DS.reiniciarNovaFase(getNovaFase(p)); 
        					  }
        				  }
        				  }else {
        					  AsyncDungeonWinEvent asyncdwin = new AsyncDungeonWinEvent(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty(), CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider(), CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon(), CoreStorage.getDungeonServers().get(CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon().getDungeonName()));
        					  Bukkit.getPluginManager().callEvent(asyncdwin);
        					  if(DS.getDeathPlayers().size() > 0) {
        					  DS.reviveAllDeaths();
        					  }
        					  for(String user : DS.getParty().getPartyMembers()) {
        						  if(Bukkit.getPlayer(user)!=null) {
        							  Player target = Bukkit.getPlayer(user);
        							  Title t = new Title("§A§LVitória!", "§7Todas as fases foram concluidas", 3, 130, 3);
        							  t.send(target);
        						  }
        					  }
        					  new BukkitRunnable() {
								int fireworks = 0;
								@Override
								public void run() {
									if(fireworks < 12) {
									  for(String user : DS.getParty().getPartyMembers()) {
		        						  if(Bukkit.getPlayer(user)!=null) {
		        							  Player target = Bukkit.getPlayer(user);
		        							  launchFirework(target, 1);
		        						  }
									  }
									  fireworks++;
									}else {
	        							  DungeonWinEvent dWin = new DungeonWinEvent(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty(), CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider(), CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon(), CoreStorage.getDungeonServers().get(CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon().getDungeonName()));
	                					  Bukkit.getPluginManager().callEvent(dWin);
										DS.stopServer();
										 for(String user : DS.getParty().getPartyMembers()) {
			        						  if(Bukkit.getPlayer(user)!=null) {
			        							  Player target = Bukkit.getPlayer(user);
			        							  target.getInventory().setHelmet(new ItemBuilder(Material.AIR).toItemStack());
			        							  target.getInventory().setChestplate(new ItemBuilder(Material.AIR).toItemStack());
			        							  target.getInventory().setLeggings(new ItemBuilder(Material.AIR).toItemStack());
			        							  target.getInventory().setBoots(new ItemBuilder(Material.AIR).toItemStack());
			        							  target.getInventory().clear();
			        							  target.teleport(CoreStorage.spawn);
			        							  target.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			                					  CoreStorage.getStoraged_users().get(user).setCurrentDungeon(null);
			                					  CoreStorage.getStoraged_users().get(user).setCurrentFase(null);
			        						  }
										 }
										 CoreStorage.getDungeonServers().remove(DS.getDungeon().getDungeonName());
										this.cancel();
										
									}
								}
							}.runTaskTimer(Main.getPlugin(Main.class), 0, 20L*3);
        				  }
        				  }
        			  p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
        			  
        		  }
        	  }
          }
          }
		}
	}
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		if(CoreStorage.worlds_registreds_names.contains(event.getLocation().getWorld().getName())) {
			 event.setCancelled(true);	
		}
	}
	@EventHandler
	public void ww(WeatherChangeEvent e){
		if(CoreStorage.worlds_registreds_names.contains(e.getWorld().getName())) {
		e.setCancelled(true);
		}
	}
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		if(CoreStorage.worlds_registreds_names.contains(event.getLocation().getWorld().getName())) {
			if(event.getSpawnReason().equals(SpawnReason.NATURAL) || event.getSpawnReason().equals(SpawnReason.DEFAULT)) {
		if(!event.getEntity().hasMetadata("GalaxyCustomMob")) {
		    event.setCancelled(true);	
		}
			}
		}
	}
	@EventHandler
	public void onDamageonSpect(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			if(isSpectating((Player)event.getDamager())) {
				event.setCancelled(true);
			}
		}
	}
	   @EventHandler
	    public void onDamage(final EntityDamageByEntityEvent e) {
	        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
	            final Player morto = (Player)e.getEntity();
	            final Player matou = (Player)e.getDamager();
	            if (CoreStorage.getStoraged_users().containsKey(morto.getName()) && CoreStorage.getStoraged_users().containsKey(matou.getName())) {
	                   if(CoreStorage.getStoraged_users().get(morto.getName()).hasParty() && CoreStorage.getStoraged_users().get(matou.getName()).hasParty()) {
	                	if (CoreStorage.getStoraged_users().get(matou.getName()).getCurrentParty().getPartyID() == CoreStorage.getStoraged_users().get(morto.getName()).getCurrentParty().getPartyID()) {
	                        if(CoreStorage.getStoraged_users().get(matou.getName()).isInDungeon() && CoreStorage.getStoraged_users().get(morto.getName()).isInDungeon()) {
	                		e.setCancelled(true);
	                        }
	                    }
	                   }
	                }
	        }
	    }
    @EventHandler
    public void onArrow(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {
            final Projectile a = (Projectile)e.getDamager();
            if (a.getShooter() instanceof Player && e.getEntity() instanceof Player) {
                final Player matou = (Player)a.getShooter();
                final Player morto = (Player)e.getEntity();
                if (CoreStorage.getStoraged_users().containsKey(morto.getName()) && CoreStorage.getStoraged_users().containsKey(matou.getName())) {
                   if(CoreStorage.getStoraged_users().get(morto.getName()).hasParty() && CoreStorage.getStoraged_users().get(matou.getName()).hasParty()) {
                	if (CoreStorage.getStoraged_users().get(matou.getName()).getCurrentParty().getPartyID() == CoreStorage.getStoraged_users().get(morto.getName()).getCurrentParty().getPartyID()) {
                		if(CoreStorage.getStoraged_users().get(matou.getName()).isInDungeon() && CoreStorage.getStoraged_users().get(morto.getName()).isInDungeon()) {
	                		e.setCancelled(true);
	                        }
                    }
                   }
                }
            }
        }
    }
	@EventHandler
	public void onExecuteCommandsOnDungeon(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
			if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
				if(!event.getMessage().equalsIgnoreCase("/pronto")) {
					if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
						return;
					}
					if(event.getPlayer().isOp()) {
						return;
					}
				p.sendMessage("§cVocê não pode executar comandos enquanto você estiver na dungeon");
				event.setCancelled(true);
				}
			}
		}
	}
}
