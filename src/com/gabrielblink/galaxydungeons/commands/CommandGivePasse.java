package com.gabrielblink.galaxydungeons.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.gabrielblink.galaxydungeons.GalaxyDungeonAPI;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class CommandGivePasse implements CommandExecutor{

	public boolean isInteger(String a) {
		try {
			Integer.parseInt(a);
			return true;	
		} catch (Exception e) {
			return false;
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("givepasse")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(p.hasPermission("galaxydungeons.givepasse")) {
				if(args.length < 3 || args.length > 3) {
					p.sendMessage("§cUtilize: /givepasse <jogador> <dungeon> <quantidade>.");
					return true;
				}
				if(args.length == 3) {
					String jogador = args[0];
					if(GalaxyDungeonAPI.hasGalaxyUser(jogador)) {
						String dungeonName = args[1];
						if(CoreStorage.getDungeons().containsKey(dungeonName)) {
							if(isInteger(args[2])) {
								int quantity = Integer.parseInt(args[2]);
								if(quantity > 10) {
									p.sendMessage("§cDesculpe, o máximo de passes para uma dungeon é 10.");
								}else {
									if(CoreStorage.getStoraged_users().containsKey(jogador)) {
							    	   if(CoreStorage.getStoraged_users().get(jogador).getAcessos_dungeon().containsKey(dungeonName)) {
									    	  int old_value = CoreStorage.getStoraged_users().get(jogador).getAcessos_dungeon().get(dungeonName);
									    	   CoreStorage.getStoraged_users().get(jogador).getAcessos_dungeon().put(dungeonName, old_value+quantity);
									    	   }else {
									    		   CoreStorage.getStoraged_users().get(jogador).getAcessos_dungeon().put(dungeonName, quantity);
									    	   }
							    	   p.sendMessage("§aForam adicionados "+quantity+" passes para a dungeon "+CoreStorage.dungeons.get(dungeonName).getFancyDungeonName()+" para o jogador "+jogador);
								}
								}
							}else {
								p.sendMessage("§cPor favor digite apenas números de 1 à 10.");
							}
						}else {
							p.sendMessage("§cEsta dungeon não existe.");
							return true;
						}
					}else {
						p.sendMessage("§cEste jogador não existe.");
						return true;
					}
				}
				}else {
					p.sendMessage("§cDesculpe, mas você não tem permissão para executar este comando.");
					return true;
				}
			}else if(sender instanceof ConsoleCommandSender) {
				ConsoleCommandSender p = Bukkit.getConsoleSender();
				if(args.length < 3 || args.length > 3) {
					p.sendMessage("§cUtilize: /givepasse <jogador> <dungeon> <quantidade>.");
					return true;
				}
				if(args.length == 3) {
					String jogador = args[0];
					if(GalaxyDungeonAPI.hasGalaxyUser(jogador)) {
						String dungeonName = args[1];
						if(CoreStorage.getDungeons().containsKey(dungeonName)) {
							if(isInteger(args[2])) {
								int quantity = Integer.parseInt(args[2]);
								if(quantity > 10) {
									p.sendMessage("§cDesculpe, o máximo de passes para uma dungeon é 10.");
								}else {
									if(CoreStorage.getStoraged_users().containsKey(jogador)) {
								    	   if(CoreStorage.getStoraged_users().get(jogador).getAcessos_dungeon().containsKey(dungeonName)) {
										    	  int old_value = CoreStorage.getStoraged_users().get(jogador).getAcessos_dungeon().get(dungeonName);
										    	   CoreStorage.getStoraged_users().get(jogador).getAcessos_dungeon().put(dungeonName, old_value+quantity);
										    	   }else {
										    		   CoreStorage.getStoraged_users().get(jogador).getAcessos_dungeon().put(dungeonName, quantity);
										    	   }
								    	   p.sendMessage("§aForam adicionados "+quantity+" passes para a dungeon "+CoreStorage.dungeons.get(dungeonName).getFancyDungeonName()+" para o jogador "+jogador);
									}
									}
							}else {
								p.sendMessage("§cPor favor digite apenas números de 1 à 10.");
							}
						}else {
							p.sendMessage("§cEsta dungeon não existe.");
							return true;
						}
					}else {
						p.sendMessage("§cEste jogador não existe.");
						return true;
					}
				}
			}else {
				Bukkit.getConsoleSender().sendMessage("§c[Error] Sender not found on execute: /givepasse");
				return true;
			}
		}
		return false;
	}

}
