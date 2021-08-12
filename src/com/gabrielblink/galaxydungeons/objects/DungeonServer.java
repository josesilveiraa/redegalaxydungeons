package com.gabrielblink.galaxydungeons.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Chest;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.apis.ActionBar;
import com.gabrielblink.galaxydungeons.apis.Config;
import com.gabrielblink.galaxydungeons.apis.Title;
import com.gabrielblink.galaxydungeons.customentitys.EntityManager;
import com.gabrielblink.galaxydungeons.enums.ArmaduraType;
import com.gabrielblink.galaxydungeons.enums.DungeonMobStatus;
import com.gabrielblink.galaxydungeons.enums.DungeonStatus;
import com.gabrielblink.galaxydungeons.enums.GateStatus;
import com.gabrielblink.galaxydungeons.enums.Status;
import com.gabrielblink.galaxydungeons.listeners.DungeonEvent;
import com.gabrielblink.galaxydungeons.listeners.DungeonGeneralEvents;
import com.gabrielblink.galaxydungeons.postoffice.events.CorreioEvent;
import com.gabrielblink.galaxydungeons.scoreboard.ScoreBoard;
import com.gabrielblink.galaxydungeons.scoreboard.ScoreUpdater;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.citizensnpcs.api.CitizensAPI;
import net.fancyful.FancyMessage;

public class DungeonServer {

	private String key;
	private Party p;
	private Fase currentFase;
	public int max_mobs = 0;
	private Entity BOSS;
	private DungeonStatus dungeonStatus;
	private DungeonMobStatus dungeonMobStatus;
	private ArrayList<String> deathPlayers = new ArrayList<String>();
	private ArrayList<Hologram> holograms = new ArrayList<Hologram>();
	private ArrayList<String> playersCanRevive = new ArrayList<String>();
	private ArrayList<EntityType> mobs_spawneds = new ArrayList<EntityType>();
	private HashMap<String,ItemStack[] []> old_itens = new HashMap<String,ItemStack[] []>();
	private HashMap<String,Status> playerStatus = new HashMap<String,Status>();
	private HashMap<String,Integer> players_pepitas = new HashMap<String,Integer>();
	private HashMap<String,ArrayList<ItemStack>> dungeon_drops_correio = new HashMap<String,ArrayList<ItemStack>>();
	private Dungeon dungeon;
	
	
	public DungeonServer(Dungeon dungeon,Party p,String key) {
		super();
		this.dungeon = dungeon;
		this.p = p;
		this.dungeonMobStatus = DungeonMobStatus.NOSPAWNING;
		this.key = key;
		this.dungeonStatus = DungeonStatus.WAITING_FOR_OK;
		for(String s : CoreStorage.worlds_registreds_names) {
			//Clearing all mobs spawneds in the worlds.
			World world = Bukkit.getWorld(s);
            List<Entity> entList = world.getEntities();
            for(Entity current : entList){
                if (current instanceof Item){
                    current.remove();
                }else if(current.getType()!=EntityType.PLAYER) {
                	current.remove();
                }
            }
		}
		for(String party_user : p.getPartyMembers()) {
			if(Bukkit.getPlayer(party_user)!=null) {
				Bukkit.getPlayer(party_user).getActivePotionEffects().clear();
			}
		}
	}
	
	public ArrayList<String> getDeathPlayers() {
		return deathPlayers;
	}

	public void setDeathPlayers(ArrayList<String> deathPlayers) {
		this.deathPlayers = deathPlayers;
	}


	public Entity getBOSS() {
		return BOSS;
	}

	public void setBOSS(Entity bOSS) {
		BOSS = bOSS;
	}



	public ArrayList<String> getPlayersCanRevive() {
		return playersCanRevive;
	}

	public void setPlayersCanRevive(ArrayList<String> playersCanRevive) {
		this.playersCanRevive = playersCanRevive;
	}

	public ArrayList<EntityType> getMobs_spawneds() {
		return mobs_spawneds;
	}

	public void setMobs_spawneds(ArrayList<EntityType> mobs_spawneds) {
		this.mobs_spawneds = mobs_spawneds;
	}

	public void clearWorlds() {
		for(Entry<String, Fase> f : dungeon.getFases().entrySet()) {
			Fase a = f.getValue();
			for(Location l : a.getLocations_mob_spawn()) {
				if(l!=null && l.getWorld()!=null) {
				if(l.getWorld().getEntities().size() > 0) {
				for(Entity e : l.getWorld().getEntities()) {
					if(!e.getType().equals(EntityType.PLAYER)) {
					l.getWorld().getEntities().remove(e);
					}
				}
				}
				}else {
					Bukkit.getConsoleSender().sendMessage("§c[ERROR] Mundo Inexistente");
				}
			}
		}
	}
	public boolean isFinalFase(Player p) {
		if(DungeonGeneralEvents.getNovaFase(p) == null) { 
			return true;
		}else {
			return false;
		}
	}
	public DungeonMobStatus getDungeonMobStatus() {
		return dungeonMobStatus;
	}

	public void setDungeonMobStatus(DungeonMobStatus dungeonMobStatus) {
		this.dungeonMobStatus = dungeonMobStatus;
	}

	public void joinServer(String s) {
			if(Bukkit.getPlayer(s)!=null) {
				clearWorlds();
				Player target = Bukkit.getPlayer(s);
				players_pepitas.put(target.getName(), 0);
				ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
				dungeon_drops_correio.put(target.getName(), drops);
				this.playerStatus.put(target.getName(), Status.NOTREADY);
				target.teleport(dungeon.getLobby());
				target.getInventory().setHelmet(dungeon.getArmor_inicial().get(ArmaduraType.Helmet));
				target.getInventory().setChestplate(dungeon.getArmor_inicial().get(ArmaduraType.Chestplate));
				target.getInventory().setBoots(dungeon.getArmor_inicial().get(ArmaduraType.Boots));
				target.getInventory().setLeggings(dungeon.getArmor_inicial().get(ArmaduraType.Leggings));
				for(ItemStack i : dungeon.getItens_iniciais()) {
					target.getInventory().addItem(i);
				}
				Title t = new Title("§c§lDigite /pronto!", "§7para iniciar a partida", 1, 100000, 10000);
				t.send(Bukkit.getPlayer(s));
				target.playSound(Bukkit.getPlayer(s).getLocation(), Sound.LEVEL_UP, 1, 1);
				target.sendMessage("§aBem vindo à Dungeon! Quando estiver pronto escreva /pronto");
				ScoreBoard.createLobbyScoreBoard(target, this);
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if(DungeonServer.this != null) {
							if(target!=null) {
						ScoreUpdater.updateScoreBoard(target, DungeonServer.this);
							}
							}else {
							target.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
							this.cancel();
						}
						}
				}.runTaskTimer(Main.getPlugin(Main.class), 20L, 20L);
				new FancyMessage("§a ou clique ").then("§a§LAQUI").command("/pronto").tooltip("§AClique para ficar pronto.").send(target);
			}
	}
	

	public boolean checkifallpassoudogate() {
		ArrayList<String> playersAlive = new ArrayList<String>();
		for(String b : this.players_pepitas.keySet()) {
			if(!this.getDeathPlayers().contains(b)) {
				playersAlive.add(b);
			}
		}
		for(String s : playersAlive) {
			if(Bukkit.getPlayer(s)!=null) {
				int x = Math.abs(currentFase.getGate().getLoc1().getBlockX())+3;
				if(Math.abs(Bukkit.getPlayer(s).getLocation().getBlockX()) < x) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	public HashMap<String, ItemStack[][]> getOld_itens() {
		return old_itens;
	}

	public void setOld_itens(HashMap<String, ItemStack[][]> old_itens) {
		this.old_itens = old_itens;
	}

	public void reiniciarNovaFase(Fase novafase) {
		if(dungeonStatus.equals(DungeonStatus.RUNNING)) {
		if(currentFase.hasGate()) {
		currentFase.getGate().openGate();
		}
		if(currentFase.hasVendedor()) {
			currentFase.spawnVendedor();
		}
		Title t = new Title(Config.get("Titulos.FaseFim").toString().replace("&", "§"), Config.get("Titulos.FaseFim_SubTitle").toString().replace("&", "§"), 5, 80, 5);
		for(String s : this.players_pepitas.keySet()) {
			if(Bukkit.getPlayer(s)!=null) {
				t.send(Bukkit.getPlayer(s))	;
				if(currentFase.getPepitasonWin() > 0) {
				ActionBar.sendActionBar("§6+ "+currentFase.getPepitasonWin()+"§6 pepitas", Bukkit.getPlayer(s));
				this.players_pepitas.put(s, this.players_pepitas.get(s)+currentFase.getPepitasonWin()); //Adding Pepitas on Players account
				ScoreUpdater.updateScoreBoard(Bukkit.getPlayer(s), this);
				}
			}
		}
		if(currentFase.hasChestDefinido()) {
			Location chest = currentFase.getChestLocation().clone().add(0.5, 0, 0.5).add(0, 1.5, 0);
			Hologram holo = HologramsAPI.createHologram(Main.getPlugin(Main.class), chest);
			holo.appendTextLine("§aRecompensas Coletadas");
			holograms.add(holo);
			Block b = currentFase.getLocations_mob_spawn().get(0).getWorld().getBlockAt(currentFase.getChestLocation());
			b.setMetadata("Rewards", new FixedMetadataValue(Main.getPlugin(Main.class), "Rewards"));
			b.setType(Material.CHEST);
			BlockState state = b.getState();
			Chest chest1 = new Chest(currentFase.getChestFace());
			state.setData(chest1);
			state.update();
			
		}
		this.dungeonStatus = DungeonStatus.WAITING_FOR_PLAYERS_GO_THROUGHT_GATE;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(checkifallpassoudogate()) {
					currentFase.getGate().closeGate();
					currentFase.despawnVendedor();
					if(currentFase.hasChestDefinido()) {
						for(Hologram h : holograms) {
							h.delete();
						}
						currentFase.getLocations_mob_spawn().get(0).getWorld().getBlockAt(currentFase.getChestLocation()).setType(Material.AIR);
					}
					for(Entity e : currentFase.getLocations_mob_spawn().get(0).getWorld().getEntities()) {
						if(e.hasMetadata("Vendedor"))
						{
							e.remove();
						}
					}
					if(CitizensAPI.getNPCRegistry().getById(40004)!= null) {
						if(CitizensAPI.getNPCRegistry().getById(40004).isSpawned()) {
							CitizensAPI.getNPCRegistry().getById(40004).despawn();
							CitizensAPI.getNPCRegistry().deregister(CitizensAPI.getNPCRegistry().getById(40004));
						}
					}
					currentFase = novafase;
					
					for(String s : DungeonServer.this.players_pepitas.keySet()) {
						CoreStorage.getStoraged_users().get(s).setCurrentFase(novafase);
						if(Bukkit.getPlayer(s)!=null) {
							
							if(currentFase.hasBoss()) {
								ScoreBoard.createDungeonScoreBoardWithBoss(Bukkit.getPlayer(s), DungeonServer.this);
							}else {
								ScoreBoard.createDungeonScoreBoardWithoutBoss(Bukkit.getPlayer(s), DungeonServer.this);
							}
						}
					}
					iniciarFase(currentFase);
					DungeonServer.this.dungeonStatus = DungeonStatus.RUNNING;
					this.cancel();
				}
			}
		}.runTaskTimer(Main.getPlugin(Main.class), 0, 20L*10);
		}
	}
	public  int getEntitysALive() {
		Location z = currentFase.getLocations_mob_spawn().get(0);
		HashMap<EntityType,String> a = new HashMap<EntityType,String>();
		int entitys = 0;
		for(Entity entityALive : z.getWorld().getEntities()) {
			if(!entityALive.isDead()) {
			for(String s : currentFase.getMobs()) {
				String[] args = s.split(",");
				EntityType et = EntityType.valueOf(args[0]);
				a.put(et, s);
			}
			if(entityALive.hasMetadata("GalaxyCustomMob")) {
				if(a.containsKey(entityALive.getType())) {
					entitys++;
				}
			}
			}
		}
		return entitys;
	}
	public void reviveAllDeaths() {
		for(String deathPlayer : this.getDeathPlayers()) {
		   	if(Bukkit.getPlayer(deathPlayer)!=null) {
		   		Player target = Bukkit.getPlayer(deathPlayer);
		   		target.setGameMode(GameMode.SURVIVAL);
		   		target.teleport(this.currentFase.getLocation_players_spawn());
		   		if(getOld_itens().containsKey(target.getName())) {
		   		target.getInventory().setContents(getOld_itens().get(target.getName())[0]);
		   		target.getInventory().setArmorContents(getOld_itens().get(target.getName())[1]);
		   		}
		   		}
		}
		deathPlayers.clear();
	}
	public void iniciarFase(Fase f) {
		Bukkit.getConsoleSender().sendMessage("§a[GalaxyDungeons] Inicializando nova fase...");
		clearWorlds();
		currentFase = f;
		for(String s : this.players_pepitas.keySet()) {
			if(Bukkit.getPlayer(s)!=null) {
				currentFase.addPlayer(Bukkit.getPlayer(s).getUniqueId());
			}
			if(!currentFase.hasBoss()) {
				ScoreBoard.createDungeonScoreBoardWithoutBoss(Bukkit.getPlayer(s), DungeonServer.this);
				ScoreUpdater.updateScoreBoard(Bukkit.getPlayer(s), this);
				}else {
					ScoreBoard.createDungeonScoreBoardWithBoss(Bukkit.getPlayer(s), this);
					ScoreUpdater.updateScoreBoard(Bukkit.getPlayer(s), this);
				}
		}
		reviveAllDeaths();
		new BukkitRunnable() {
			int step = 5;
			@Override
			public void run() {
				if(step > 0) {
					for(String s : DungeonServer.this.players_pepitas.keySet()) {
						if(Bukkit.getPlayer(s)!=null) {
							Title t = new Title("§6"+step, "".toString().replace("&", "§"), 5, 200, 5);
							t.send(Bukkit.getPlayer(s));
							}
						Bukkit.getPlayer(s).playSound(Bukkit.getPlayer(s).getLocation(), Sound.CLICK, 1, 1);
					}
					step--;
				}else {
					for(String s : DungeonServer.this.players_pepitas.keySet()) {
						if(Bukkit.getPlayer(s)!=null) {
							Title t = new Title(Config.get("Titulos.FaseIniciada").toString().replace("&", "§"), Config.get("Titulos.FaseIniciada_SubTitle").toString().replace("&", "§"), 5, 100, 5);
							t.send(Bukkit.getPlayer(s));
							Bukkit.getPlayer(s).playSound(Bukkit.getPlayer(s).getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
							}
					}
					mobs_spawneds.clear();
					max_mobs = 0;
					for(String s : currentFase.getMobs()) {
						String[] args = s.split(",");
						EntityType et = EntityType.valueOf(args[0]);
						int quantityofmobs = Integer.parseInt(args[1]);
						max_mobs = max_mobs+quantityofmobs;
					}
					if(currentFase.hasBoss()) {
						EntityType et = EntityType.valueOf(currentFase.getBoss());
						try {
							EntityManager.spawnCustomEntity(et, 1, currentFase.getBossLocation(), true, false,true,DungeonServer.this);
							mobs_spawneds.add(et);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					new BukkitRunnable() {
						
						@Override
						public void run() {
							if(dungeonStatus.equals(DungeonStatus.RUNNING)) {
								if(getEntitysALive() < max_mobs) {
								Random r = new Random();
								int sorted = r.nextInt(currentFase.getLocations_mob_spawn().size());
								Location sorted_Location = currentFase.getLocations_mob_spawn().get(sorted);
								HashMap<EntityType,String> a = new HashMap<EntityType,String>();
								for(String s : currentFase.getMobs()) {
									String[] args = s.split(",");
									EntityType et = EntityType.valueOf(args[0]);
									a.put(et, s);
								}
								for(EntityType jaspawnada : mobs_spawneds) {
									a.remove(jaspawnada);
								}
								ArrayList<EntityType> sortear = new ArrayList<EntityType>();
								for(EntityType e : a.keySet()) {
									sortear.add(e);
								}
								if(sortear.size() > 0) {
									DungeonServer.this.dungeonMobStatus = DungeonMobStatus.SPAWNING;
								int sorted_mob = r.nextInt(sortear.size());
								String[] args = a.get(sortear.get(sorted_mob)).split(",");
								EntityType et = EntityType.valueOf(args[0]);
								int quantityofmobs = Integer.parseInt(args[1]);
								boolean isFinal = false;
								if(currentFase.hasBoss()) {
									isFinal = true;
								}
								boolean useArmor = Boolean.parseBoolean(args[2]);
								if(!mobs_spawneds.contains(et)) {
								
									try {
										EntityManager.spawnCustomEntity(et, quantityofmobs, sorted_Location, isFinal, useArmor,false,DungeonServer.this);
										mobs_spawneds.add(et);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								
								}else {
									DungeonServer.this.dungeonMobStatus = DungeonMobStatus.NOSPAWNING;
									this.cancel();
								}
								}else {
									DungeonServer.this.dungeonMobStatus = DungeonMobStatus.NOSPAWNING;
									this.cancel();
								}
							}else {	
								DungeonServer.this.dungeonMobStatus = DungeonMobStatus.NOSPAWNING;
								this.cancel();
							}
							
							
						}
					}.runTaskTimer(Main.getPlugin(Main.class), 0, 20L*2);
					this.cancel();
				}
			}
		}.runTaskTimer(Main.getPlugin(Main.class), 0,20L);
		
	
		
	}
	
	public int getMax_mobs() {
		return max_mobs;
	}
	public void setMax_mobs(int max_mobs) {
		this.max_mobs = max_mobs;
	}
	public void finishServer() {
		this.dungeonStatus = DungeonStatus.STOPPED;
		this.dungeonMobStatus = DungeonMobStatus.NOSPAWNING;
		for(String user_drop : this.dungeon_drops_correio.keySet()) {
			String generated_uuid = UUID.randomUUID().toString();
			ArrayList<String> contents = new ArrayList<String>();
			for(Hologram h : this.holograms) {
				h.delete();
			}
			for(Fase f : this.dungeon.getFases().values()) {
				if(f.hasChestDefinido()) {
				Block b = f.getLocations_mob_spawn().get(0).getWorld().getBlockAt(f.getChestLocation());
				if(b.getType().equals(Material.CHEST)) {
					b.setType(Material.AIR);
				}
				}
				if(f.hasVendedor()) {
					f.despawnVendedor();
					if(CitizensAPI.getNPCRegistry().getById(40004)!=null) {
						if(CitizensAPI.getNPCRegistry().getById(40004).isSpawned()) {
							CitizensAPI.getNPCRegistry().getById(40004).despawn();
						}
					}
				}
				if(f.hasGate()) {
					if(f.getGate().getGateStatus().equals(GateStatus.OPENED)) {
						f.getGate().closeGate();
					}
				}
			}

		}
		for(Fase f : dungeon.getFases().values()) {
			for(Entity e : f.getLocations_mob_spawn().get(0).getWorld().getEntities()) {
				if(e.getType() != EntityType.PLAYER) {
				e.remove();
				}
			}
		}
		if(this.dungeonStatus.equals(DungeonStatus.WAITING_FOR_OK)) {
		for(String s : p.getPartyMembers()) {
			if(Bukkit.getPlayer(s)!=null) {
				Bukkit.getPlayer(s).resetTitle();
			}
		}
		}
		if(CoreStorage.getDungeonServers().containsKey(key)) {
			CoreStorage.getDungeonServers().remove(key);
		}
	}
	public void stopServer() {
		this.dungeonStatus = DungeonStatus.STOPPED;
		this.dungeonMobStatus = DungeonMobStatus.NOSPAWNING;
		for(String user_drop : this.dungeon_drops_correio.keySet()) {
			String generated_uuid = UUID.randomUUID().toString();
			ArrayList<String> contents = new ArrayList<String>();
			for(Hologram h : this.holograms) {
				h.delete();
			}
			for(Fase f : this.dungeon.getFases().values()) {
				if(f.hasChestDefinido()) {
				Block b = f.getLocations_mob_spawn().get(0).getWorld().getBlockAt(f.getChestLocation());
				if(b.getType().equals(Material.CHEST)) {
					b.setType(Material.AIR);
				}
				}
				if(f.hasVendedor()) {
					f.despawnVendedor();
					if(CitizensAPI.getNPCRegistry().getById(40004)!=null) {
						if(CitizensAPI.getNPCRegistry().getById(40004).isSpawned()) {
							CitizensAPI.getNPCRegistry().getById(40004).despawn();
						}
					}
				}
				if(f.hasGate()) {
					if(f.getGate().getGateStatus().equals(GateStatus.OPENED)) {
						f.getGate().closeGate();
					}
				}
			}
			for(ItemStack i : this.dungeon_drops_correio.get(user_drop)) {
				if(i!=null) {
					ArrayList<String> itens = new ArrayList<String>();
				try {
					Bukkit.getConsoleSender().sendMessage("§b[Galaxy Saving] -> "+i.getType().toString()+" - "+i.getAmount());
					contents.add(CorreioEvent.toBase64(i));
					} catch (IOException e) {
					e.printStackTrace();
				}
			}
			}
			Correio c = new Correio(generated_uuid, contents, "Dungeon");
			if(CoreStorage.getStoraged_users().containsKey(user_drop)) {
				if(contents.size() > 0) {
				CoreStorage.getStoraged_users().get(user_drop).getCorreio().put(generated_uuid, c);
				}
				}
		}
		for(Fase f : dungeon.getFases().values()) {
			for(Entity e : f.getLocations_mob_spawn().get(0).getWorld().getEntities()) {
				if(e.getType() != EntityType.PLAYER) {
				e.remove();
				}
			}
		}
		if(this.dungeonStatus.equals(DungeonStatus.WAITING_FOR_OK)) {
		for(String s : p.getPartyMembers()) {
			if(Bukkit.getPlayer(s)!=null) {
				Bukkit.getPlayer(s).resetTitle();
			}
		}
		}
		if(CoreStorage.getDungeonServers().containsKey(key)) {
			CoreStorage.getDungeonServers().remove(key);
		}
	}
	public boolean isAllReadys() {
		for(String s : playerStatus.keySet()) {
			if(playerStatus.get(s).equals(Status.NOTREADY)) {
				return false;
			}
		}
		return true;
	}
	public void updatePlayersProntos() {
		if(this.playerStatus.size() == p.getPartyMembers().size()) {
			if(isAllReadys()) {
			//Start the countdown
			for(String userpronto : this.playerStatus.keySet()) {
				if(Bukkit.getPlayer(userpronto)!=null) {
					Player target = Bukkit.getPlayer(userpronto);
					target.teleport(dungeon.getLobby());
					Title t = new Title("§a§lVocê está pronto!", "§7Aguarde pelo resto da equipe", 1, 1, 1);
					t.send(target);
					target.sendMessage("§aSejam bem-vindos á Dungeon!");
				}
			}
			new BukkitRunnable() {
				int step = 0;
				@Override
				public void run() {
					if(step == 0) {
						for(String userpronto : DungeonServer.this.players_pepitas.keySet()) {
							if(Bukkit.getPlayer(userpronto)!=null) {
								Player target = Bukkit.getPlayer(userpronto);
								target.sendMessage("§aAqui você terá que eliminar vários mobs, que podem dropar recompensas.");
							}
						}
						step++;
					}else
					if(step == 1) {
						for(String userpronto : DungeonServer.this.players_pepitas.keySet()) {
							if(Bukkit.getPlayer(userpronto)!=null) {
								Player target = Bukkit.getPlayer(userpronto);
								target.sendMessage("§aAs recompensas serão entregues no seu /correio ao finalizar a Dungeon.");
							}
						}
						step++;
					}else
					if(step == 2) {
						for(String userpronto : DungeonServer.this.players_pepitas.keySet()) {
							if(Bukkit.getPlayer(userpronto)!=null) {
								Player target = Bukkit.getPlayer(userpronto);
								target.sendMessage("§aCaso você morra, renascerá na próxima fase.");
							}
						}
						step++;
					}else
					if(step == 3) {
						for(String userpronto : DungeonServer.this.players_pepitas.keySet()) {
							if(Bukkit.getPlayer(userpronto)!=null) {
								Player target = Bukkit.getPlayer(userpronto);
								target.sendMessage("§aCaso todo o seu time morra, a Dungeon é perdida.");
							}
						}
						step++;
					}else
					if(step == 4) {
						for(String userpronto : DungeonServer.this.players_pepitas.keySet()) {
							if(Bukkit.getPlayer(userpronto)!=null) {
								Player target = Bukkit.getPlayer(userpronto);
								target.sendMessage("§aAs recompensas que você coletou são entregues mesmo ao perder.");
							}
						}
						step++;
					}else
					if(step == 5) {
						String selected_player = "";
						for(String userpronto : DungeonServer.this.players_pepitas.keySet()) {
							if(Bukkit.getPlayer(userpronto)!=null) {
								Player target = Bukkit.getPlayer(userpronto);
								target.sendMessage("§aBoa sorte a todos! :)");
								if(selected_player.equalsIgnoreCase("")) {
									selected_player = target.getName();
								}
							}
						}
						DungeonServer.this.dungeonStatus = DungeonStatus.RUNNING;
						iniciarFase(CoreStorage.getStoraged_users().get(selected_player).getCurrentFase());
						this.cancel();
					}
				}
			}.runTaskTimer(Main.getPlugin(Main.class), 20L*2, 20L*3);
			}
		}
			
		
	}
	
	public DungeonStatus getDungeonStatus() {
		return dungeonStatus;
	}

	public void setDungeonStatus(DungeonStatus dungeonStatus) {
		this.dungeonStatus = dungeonStatus;
	}

	public HashMap<String, Integer> getPlayers_pepitas() {
		return players_pepitas;
	}

	public void setPlayers_pepitas(HashMap<String, Integer> players_pepitas) {
		this.players_pepitas = players_pepitas;
	}

	public Party getParty() {
		return p;
	}

	public void setParty(Party p) {
		this.p = p;
	}

	public HashMap<String, Status> getPlayerStatus() {
		return playerStatus;
	}

	public void setPlayerStatus(HashMap<String, Status> playerStatus) {
		this.playerStatus = playerStatus;
	}

	public HashMap<String, ArrayList<ItemStack>> getDungeon_drops_correio() {
		return dungeon_drops_correio;
	}

	public void setDungeon_drops_correio(HashMap<String, ArrayList<ItemStack>> dungeon_drops_correio) {
		this.dungeon_drops_correio = dungeon_drops_correio;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}
	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	
}
