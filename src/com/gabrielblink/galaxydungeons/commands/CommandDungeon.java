package com.gabrielblink.galaxydungeons.commands;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.customentitys.EntityManager;
import com.gabrielblink.galaxydungeons.enums.GateStatus;
import com.gabrielblink.galaxydungeons.inventorys.DungeonInventory;
import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.Fase;
import com.gabrielblink.galaxydungeons.objects.Gate;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class CommandDungeon implements CommandExecutor{

	public static void sendHelp(Player p) {
		p.sendMessage("§a/dungeon debug §8- §7Comando para criação de dungeons.");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> gate §8- Definir portão de saida/entrada para nova fase.");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> players §8- Definir respawn de jogadores.");
		p.sendMessage("§a/dungeon setspawn §8- Definir spawn central do seu servidor.");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> horse - §8Definir cavalo de troca");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> chest <facing> §8- Definir Baú de itens");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> boss §8- Definir boss caso exista configurado na fase da config.");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> §8- §7Definir localizações de spawn de mobs em uma fase determinada.");
		p.sendMessage("§a/dungeon setup <dungeon name> remove all §8- §7Remover todas as localizações de spawn definidas anteriormente.");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> players §8- §7Definir localização de spawn dos jogadores na horda.");
		p.sendMessage("§a/dungeon setup <dungeon name> lobby §8- §7Definir localização de spawn dos jogadores no lobby.");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> boss §8- §7Definir localização de spawn do Boss(Se tiver).");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> gate §8- §7Definir portão para a próxima fase.");
		p.sendMessage("§a/dungeon setup <dungeon name> <fase> chest <Facing> §8- §7Definir baú que irá spawnar os itens coletados naquela fase.");
		p.sendMessage("§a/dungeon menu §8- §7Abrir menu de dungeons.");
	}
	public static void sendHelpDebug(Player p) {
		p.sendMessage("§a/dungeon debug start horda §8- §7Iniciar uma horda de monstros em seu local.");
		p.sendMessage("§a/dungeon debug info <dungeon name> §8- §7Obter informações da dungeon.");
		p.sendMessage("§a/dungeon open gate §8- Abrir portão da fase atual(Pode causar erros)");
		p.sendMessage("§a/dungeon close gate §8- Abrir portão da fase atual(Pode causar erros)");
	}
	
	public static ArrayList<String> getInArrayString(ArrayList<Location> arrayloc){
		ArrayList<String> lista = new ArrayList<String>();
		for(Location l : arrayloc) {
			lista.add(CoreStorage.getSerializedLocation(l));
		}
		return lista;
	}
	
	public static DecimalFormatSymbols DFS = new DecimalFormatSymbols(new Locale("pt", "BR"));
	public static DecimalFormat FORMATTER = new DecimalFormat("###,###,###", DFS);
	public static HashMap<UUID,String> settingChests = new HashMap<UUID,String>();
	public boolean existisFacing(String facing) {
		if(facing.equalsIgnoreCase("WEST")) {
			return true;
		}else if(facing.equalsIgnoreCase("NORTH")) {
			return true;
		}else if(facing.equalsIgnoreCase("EAST")) {
			return true;
		}else if(facing.equalsIgnoreCase("SOUTH")) {
			return true;
		}else {
			return false;
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("dungeon")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(!p.hasPermission("galaxy.galaxydungeons.admin")) {
					DungeonInventory.openDungeons(p);
				}else {
					if(args.length < 1) {
						DungeonInventory.openDungeons(p);
					}else {
						if(args.length == 1) {
							if(args[0].equalsIgnoreCase("stop")) {
								CoreStorage.getDungeonServers().get(CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon().getDungeonName()).stopServer();
							}else
						   if(args[0].equalsIgnoreCase("menu")) {
							   DungeonInventory.openDungeons(p);
						   }else if(args[0].equalsIgnoreCase("debug")){
						      sendHelpDebug(p);
						   }else if(args[0].equalsIgnoreCase("setup")){
							   sendHelp(p);
						   }else if(args[0].equalsIgnoreCase("setspawn")){
							   CoreStorage.spawn = p.getLocation();
							   Main.getInstance().getConfig().set("Geral.SpawnLocation", CoreStorage.getSerializedLocation(p.getLocation()));
							   Main.getInstance().saveConfig();
							   p.sendMessage("§aSpawn geral do servidor definido com sucesso.");
						   }else {
							   sendHelp(p);
						   }
						}
						if(args.length == 5) {
							if(args[0].equalsIgnoreCase("setup")) {
								String dungeon_name = args[1];
								String fase = args[2];
								if(CoreStorage.getDungeons().containsKey(dungeon_name)) {
									if(CoreStorage.getDungeons().get(dungeon_name).getFases().containsKey(fase)) {
									
								if(args[3].equalsIgnoreCase("chest")) {
									if(existisFacing(args[4])) {
									p.sendMessage("§aQuebre um bloco para definir a localização do baú que será spawnado ao terminar a fase.");
									settingChests.put(p.getUniqueId(),dungeon_name+"-"+fase+"-"+args[4].toUpperCase());
									return true;
									}else {
										p.sendMessage("§cPor favor utilize apenas: SOUTH,EAST,NORTH,WEST.");
										return true;
									}
								}
									}
								}
							}
						}
						if(args.length == 4) {
							
							if(args[0].equalsIgnoreCase("setup")) { // /dungeon setup <dungeon name> <fase> chest <facing>
								String dungeon_name = args[1];
								String fase = args[2];
								if(args[3].equalsIgnoreCase("chest")) {
									p.sendMessage("§cUtilize: /dungeon setup <dungeon name> <fase> <facing>");
									p.sendMessage("§eTIP: Aperte F3 e observe o facing para onde você quer que o bloco seja direcionado.");
									return true;
								}
								if(CoreStorage.getDungeons().containsKey(dungeon_name)) {
									if(CoreStorage.getDungeons().get(dungeon_name).getFases().containsKey(fase)) {
										if(args[3].equalsIgnoreCase("boss")) { //dungeon setup <dungeon name> <fase> boss
											CoreStorage.getDungeons().get(dungeon_name).getFases().get(fase).setBossLocation(p.getLocation());
											  Main.getInstance().getConfig().set("Dungeons."+dungeon_name+".Fases."+fase+".BossLocation", CoreStorage.getSerializedLocation(p.getLocation()));
												Main.getInstance().saveConfig();
												p.sendMessage("§aLocalização do boss definida com sucesso.");
										}
									if(args[3].equalsIgnoreCase("horse")) {//dungeon setup <dungeon name> <fase> horse
										CoreStorage.getDungeons().get(dungeon_name).getFases().get(fase).setVendedor(p.getLocation());
										  Main.getInstance().getConfig().set("Dungeons."+dungeon_name+".Fases."+fase+".HorseLocation", CoreStorage.getSerializedLocation(p.getLocation()));
											Main.getInstance().saveConfig();
											p.sendMessage("§aLocalização de spawn do vendedor definida com sucesso.");
									}
								if(args[3].equalsIgnoreCase("players")) {//dungeon setup <dungeon name> <fase> players
											CoreStorage.getDungeons().get(dungeon_name).getFases().get(fase).setLocation_players_spawn(p.getLocation());
											  Main.getInstance().getConfig().set("Dungeons."+dungeon_name+".Fases."+fase+".Location", CoreStorage.getSerializedLocation(p.getLocation()));
												Main.getInstance().saveConfig();
												p.sendMessage("§aLocalização de spawn de jogadores na fase §e"+fase+" §adefinida com sucesso.");
								}
								if(args[3].equalsIgnoreCase("gate")) {//dungeon setup <dungeon name> <fase> gate
											 Selection s = Main.worldEditPlugin.getSelection(p);
											 Location a = s.getMaximumPoint();
											 Location b = s.getMinimumPoint();
											 if(a == null) {
												 p.sendMessage("§cPor favor selecione uma área com o machado do world edit para fazer o setup do gate.");
												 return true;
											 }else if(b == null) {
												 p.sendMessage("§cPor favor selecione uma área com o machado do world edit para fazer o setup do gate.");
												 return true;
											 }
											 ArrayList<Location> gate_locs = new ArrayList<Location>();
											 gate_locs.add(a);
											 gate_locs.add(b);
												 Gate gate = new Gate(GateStatus.CLOSED, a, b);
												 CoreStorage.getDungeons().get(dungeon_name).getFases().get(fase).setGate(gate);
											  Main.getInstance().getConfig().set("Dungeons."+dungeon_name+".Fases."+fase+".Gate", getInArrayString(gate_locs));
												Main.getInstance().saveConfig();
												p.sendMessage("§aLocalização do portão da fase §e"+fase+" §adefinida com sucesso.");
								}
								
									}else {
										p.sendMessage("§cA fase da dungeon "+dungeon_name+" não existe.");
									}
								}else {
									p.sendMessage("§cDungeon não cadastrada.");
								}
							}
						}
						if(args.length == 2) {
							if(args[0].equalsIgnoreCase("open")) {
								if(args[1].equalsIgnoreCase("gate")) {
									CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getGate().openGate();
								}
							}else if(args[0].equalsIgnoreCase("close")) {
								if(args[1].equalsIgnoreCase("gate")) {
									CoreStorage.getStoraged_users().get(p.getName()).getCurrentFase().getGate().closeGate();
								}
							}
						}
						if(args.length == 3) {
							if(args[0].equalsIgnoreCase("debug")) {
								 if(args[1].equalsIgnoreCase("info")){
									String dungeon_name = args[2];
									if(CoreStorage.getDungeons().containsKey(dungeon_name)) {
										Dungeon d = CoreStorage.getDungeons().get(dungeon_name);
										p.sendMessage("");
										p.sendMessage("§eInformações da Dungeon:");
										p.sendMessage("§eNome da dungeon: §7"+d.getFancyDungeonName());
										p.sendMessage("§ePreço atual do passe: §7"+FORMATTER.format(d.getPrice()));
										p.sendMessage("§eFases: §7"+d.getFases().keySet().toString());
										for(String s : d.getFases().keySet()) {
											Fase f = d.getFases().get(s);
											p.sendMessage("§e Informações da "+s);
											p.sendMessage("  §eDrops: §7"+f.getDrops().toString());
											p.sendMessage("  §eMobs cadastrados: §7"+f.getMobs().toString());
											p.sendMessage("  §eLocations Mobs spawns: §7"+f.getLocations_mob_spawn().toString());
										}
									}else {
										p.sendMessage("§cEsta dungeon não existe.");
										p.sendMessage(""+CoreStorage.getDungeons().keySet().toString());
										return true;
									}
								}else {
									sendHelpDebug(p);
								}
							}else if(args[0].equalsIgnoreCase("setup")) {
									
								String dungeon_name = args[1];
								if(args[2].equalsIgnoreCase("lobby")) {
									if(CoreStorage.getDungeons().containsKey(dungeon_name)) {
										CoreStorage.getDungeons().get(dungeon_name).setLobby(p.getLocation());
										 Main.getInstance().getConfig().set("Dungeons."+dungeon_name+".LobbyLocation", CoreStorage.getSerializedLocation(p.getLocation()));
											Main.getInstance().saveConfig();
										p.sendMessage("§aLocalização do Lobby da Dungeon "+dungeon_name+" definida com sucesso.");
									}else {
										p.sendMessage("§cEsta dungeon não existe.");
										return true;
									}
								}else {
								if(CoreStorage.getDungeons().containsKey(dungeon_name)) {
									String fase = args[2];
									if(CoreStorage.getDungeons().get(dungeon_name).getFases().containsKey(fase)) {
										CoreStorage.getDungeons().get(dungeon_name).getFases().get(fase).getLocations_mob_spawn().add(p.getLocation());
									    Main.getInstance().getConfig().set("Dungeons."+dungeon_name+".Fases."+fase+".Locations", getInArrayString(CoreStorage.getDungeons().get(dungeon_name).getFases().get(fase).getLocations_mob_spawn()));
										Main.getInstance().saveConfig();
										p.sendMessage("§aLocalização §e#"+CoreStorage.getDungeons().get(dungeon_name).getFases().get(fase).getLocations_mob_spawn().size()+" §acadastrada.");
										return true;
									}else {
										p.sendMessage("§cEsta fase não foi cadastrada.");
										return true;
									}
								}else {
									p.sendMessage("§cEsta dungeon não existe.");
									return true;
								}
							}
							}
						}
					
					    
					}
				}
			}
		}
		return false;
	}

}
