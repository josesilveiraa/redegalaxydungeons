package com.gabrielblink.galaxydungeons.commands;

import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.objects.Party;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

import net.fancyful.FancyMessage;


public class CommandParty implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("dungeonparty")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(args.length < 1) {
					p.sendMessage("");
					p.sendMessage("§aComandos disponíveis:");
					new FancyMessage("§e/dungeonparty transferir ")
					.color(YELLOW)
					.then("§7<nick>")
					.tooltip("§eNick do jogador a promover")
				    .send(p);
					new FancyMessage("§e/dungeonparty aceitar ")
					.color(YELLOW)
					.then("§7<nick>")
					.tooltip("§eNick do jogador que será procurado")
				    .send(p);
					new FancyMessage("§e/dungeonparty expulsar ")
					.color(YELLOW)
					.then("§7<nick>")
					.tooltip("§eNick do jogador a ser expulso")
				    .send(p);
					p.sendMessage("§e/dungeonparty info");
					p.sendMessage("§e/dungeonparty eliminar");
					new FancyMessage("§e/dungeonparty convidar ")
					.color(YELLOW)
					.then("§7<nick>")
					.tooltip("§eNick do jogador que será convidado")
				    .send(p);
					new FancyMessage("§e/dungeonparty rejeitar ")
					.color(YELLOW)
					.then("§7<nick>")
					.tooltip("§eNick do jogador que será procurado")
				    .send(p);
					p.sendMessage("§e/dungeonparty sair");
					p.sendMessage("");
					return true;
				}
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("sair")) {
						if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
							if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty()!=null) {
								if(!CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider().equalsIgnoreCase(p.getName())) {
								for(String userinparty : CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers()) {
									CoreStorage.getStoraged_users().get(userinparty).getCurrentParty().getPartyMembers().remove(p.getName());
								
									if(Bukkit.getPlayer(userinparty)!=null) {
										Bukkit.getPlayer(userinparty).sendMessage("§c"+p.getName()+"§c saiu da party.");
									}
								}
								CoreStorage.getStoraged_users().get(p.getName()).setCurrentParty(null);
								}else {
									p.sendMessage("§cVocê é o líder da party, você não pode sair dela.");
									return true;
								}
							}else {
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								p.sendMessage("§cVocê não está em nenhuma party!");
								return true;
							}
						}
					}else if(args[0].equalsIgnoreCase("info")) {
						if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty() != null) {
							p.sendMessage("§eID: §f"+CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyID());
							p.sendMessage("§eLíder: §f"+CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider());
							p.sendMessage("§eMembros: §f"+CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers().toString().replace("[", "").replace("]", ""));
						}else {
							p.sendMessage("§cVocê não está em nenhuma party!");
							return true;
						}
					}else if(args[0].equalsIgnoreCase("eliminar")) {
						if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
						if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty() != null) {
							if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider().equalsIgnoreCase(p.getName())) {
								Party currentParty = CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty();
								for(String userinParty : currentParty.getPartyMembers()) {
									if(Bukkit.getPlayer(userinParty)!=null) {
										Bukkit.getPlayer(userinParty).sendMessage("§c"+currentParty.getPartyLider()+" §celiminou a party.");
									}
									CoreStorage.getStoraged_users().get(userinParty).setCurrentParty(null);
								}
								CoreStorage.getPartys().remove(currentParty.getPartyID());
								
							}else {
								p.sendMessage("§cVocê precisa ser o líder da party para elimina-la.");
							}
						}else {
							p.sendMessage("§cVocê não está em nenhuma party!");
							return true;
						}
						}else {
							GalaxyDungeonUser du = new GalaxyDungeonUser(p.getName());
							CoreStorage.getStoraged_users().put(p.getName(), du);
							p.sendMessage("§cOcorreu um erro, tente novamente mais tarde.");
						}
					}
				}
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("convidar")) {
						String nick = args[1];
						if(nick.equalsIgnoreCase(p.getName())) {
							p.sendMessage("§CVocê não pode convidar si mesmo para sua party!");
							return true;
						}
						if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
							
						if(CoreStorage.getStoraged_users().containsKey(nick)) {
							if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty()==null) {
								if(Bukkit.getPlayer(nick)!=null) {
									
									Player target = Bukkit.getPlayer(nick);
									String mynickname = p.getName();
								CoreStorage.createParty(p.getName());
								CoreStorage.getStoraged_users().get(nick).getConvites_de_party().put(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyID(), CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty());
								p.sendMessage("§aVocê convidou §6"+nick+"§a para sua party!");
								p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
								target.sendMessage("");
								target.sendMessage("§6"+p.getName()+" §elhe convidou para uma Party!");
								new FancyMessage("§eClique ")
								.color(YELLOW)
								.then("§a§lAQUI")
								.command("/dungeonparty aceitar "+p.getName())
								.tooltip("§aClique e aceite o pedido\n§ade party de "+p.getName())
								.then("§e para aceitar ou ")
								.then("§c§lAQUI")
								.command("/dungeonparty rejeitar "+p.getName())
								.tooltip("§cClique e negue o pedido\n§cde party de "+p.getName())
								.then("§e para negar.")
							    .send(target);
								target.sendMessage("");
								new BukkitRunnable() {
									
									@Override
									public void run() {
										if(CoreStorage.getStoraged_users().get(nick).getConvites_de_party().containsKey(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyID())) {
											if(Bukkit.getPlayer(nick)!=null) {
												Bukkit.getPlayer(nick).sendMessage("§cVocê ignorou o pedido de party de "+mynickname+".");
											}
										}
									}
								}.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 20L*60*2);
								}else {
									p.sendMessage("§cEste jogador não está online.");
									return true;
								}
							}else {
								if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider().equalsIgnoreCase(p.getName())) {
									if(Bukkit.getPlayer(nick)!=null) {
										
										if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers().size() < 5) {
									Player target = Bukkit.getPlayer(nick);
									if(!CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers().contains(target.getName())) {
									String mynickname = p.getName();
								CoreStorage.getStoraged_users().get(nick).getConvites_de_party().put(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyID(), CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty());
								p.sendMessage("§aVocê convidou §6"+nick+"§a para sua party!");
								p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
								target.sendMessage("");
								target.sendMessage("§6"+p.getName()+" §elhe convidou para uma Party!");
								new FancyMessage("§eClique ")
								.color(YELLOW)
								.then("§a§lAQUI")
								.command("/dungeonparty aceitar "+p.getName())
								.tooltip("§aClique e aceite o pedido\n§ade party de "+p.getName())
								.then("§e para aceitar ou ")
								.then("§c§lAQUI")
								.command("/dungeonparty rejeitar "+p.getName())
								.tooltip("§cClique e negue o pedido\n§cde party de "+p.getName())
								.then("§e para negar.")
							    .send(target);
								target.sendMessage("");
								new BukkitRunnable() {
									
									@Override
									public void run() {
										if(CoreStorage.getStoraged_users().get(nick).getConvites_de_party().containsKey(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyID())) {
											if(Bukkit.getPlayer(nick)!=null) {
												Bukkit.getPlayer(nick).sendMessage("§cVocê ignorou o pedido de party de "+mynickname+".");
											}
										}
									}
								}.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 20L*60*2);
									}else {
										p.sendMessage("§cO jogador '"+nick+"' já pertence a sua party.");
									}
										}else {
											p.sendMessage("§cA party atingiu o numero máximo de jogadores.");
											return true;
										}
								}else {
									p.sendMessage("§cEste jogador não está online.");
									return true;
								}
								}else {
									p.sendMessage("§cVocê precisa ser o líder da party para convidar outros jogadores.");
									return true;
								}
							}
						}else {
							p.sendMessage("§cO usuário '"+nick+"' não existe.");
							return true;
						}
					}else {
						GalaxyDungeonUser du = new GalaxyDungeonUser(p.getName());
						CoreStorage.getStoraged_users().put(p.getName(), du);
						p.sendMessage("§cOcorreu um erro, tente novamente mais tarde.");
					}
					}else if(args[0].equalsIgnoreCase("aceitar")) {
						String nick = args[1];
						if(!CoreStorage.getStoraged_users().containsKey(p.getName())) {
							GalaxyDungeonUser du = new GalaxyDungeonUser(p.getName());
							CoreStorage.getStoraged_users().put(p.getName(), du);
							p.sendMessage("§cOcorreu um erro, tente novamente mais tarde.");
						}
						if(CoreStorage.getStoraged_users().containsKey(nick)) {
							if(CoreStorage.getStoraged_users().get(nick).getCurrentParty()!=null) {
								int partyID = CoreStorage.getStoraged_users().get(nick).getCurrentParty().getPartyID();
								if(CoreStorage.getStoraged_users().get(p.getName()).getConvites_de_party().containsKey(partyID)) {
									if(Bukkit.getPlayer(nick)!=null) {
										for(String user : CoreStorage.getStoraged_users().get(nick).getCurrentParty().getPartyMembers()) {
											if(Bukkit.getPlayer(user)!=null) {
												Bukkit.getPlayer(user).sendMessage("§6"+p.getName()+"§a entrou na sua party!");
											}
										}
										p.sendMessage("§aVocê entrou na party de §6"+Bukkit.getPlayer(nick).getName()+"§a!");
										CoreStorage.getStoraged_users().get(nick).getCurrentParty().getPartyMembers().add(p.getName());
										CoreStorage.getStoraged_users().get(p.getName()).getConvites_de_party().remove(CoreStorage.getStoraged_users().get(nick).getCurrentParty().getPartyID());
										CoreStorage.getStoraged_users().get(p.getName()).setCurrentParty(CoreStorage.getStoraged_users().get(nick).getCurrentParty());
										
									}else {
										p.sendMessage("§cVocê não pode responder a esse convite ainda!");
										return true;
									}
								}else {
									p.sendMessage("§cVocê não tem um convite desse jogador!");
									return true;
								}
							}else {
								p.sendMessage("§cVocê não tem um convite desse jogador!");
								return true;
							}
						}else {
							p.sendMessage("§cO usuário '"+nick+"' não existe.");
							return true;
						}
					}else if(args[0].equalsIgnoreCase("rejeitar")) {
						String nick = args[1];
						if(CoreStorage.getStoraged_users().containsKey(nick)) {
							if(CoreStorage.getStoraged_users().get(nick).getCurrentParty()!=null) {
								int partyID = CoreStorage.getStoraged_users().get(nick).getCurrentParty().getPartyID();
								if(CoreStorage.getStoraged_users().get(p.getName()).getConvites_de_party().containsKey(partyID)) {
									if(Bukkit.getPlayer(nick)!=null) {
										p.sendMessage("§aVocê rejeitou o pedido de party do jogador §6"+Bukkit.getPlayer(nick).getName()+"§a.");
										CoreStorage.getStoraged_users().get(p.getName()).getConvites_de_party().remove(CoreStorage.getStoraged_users().get(nick).getCurrentParty().getPartyID());
										Bukkit.getPlayer(nick).sendMessage("§cO jogador "+p.getName()+" §crejeitou seu convite para a party.");
									}else {
										p.sendMessage("§cVocê não pode responder a esse convite ainda!");
										return true;
									}
								}else {
									p.sendMessage("§cVocê não tem um convite desse jogador!");
									return true;
								}
							}else {
								p.sendMessage("§cVocê não tem um convite desse jogador!");
								return true;
							}
						}else {
							p.sendMessage("§cO usuário '"+nick+"' não existe.");
							return true;
						}
					}else if(args[0].equalsIgnoreCase("transferir")) {
						String nick = args[1];
						if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
						if(CoreStorage.getStoraged_users().containsKey(nick) ) {
							if(CoreStorage.getStoraged_users().get(p.getName())!=null) {
							if(CoreStorage.getStoraged_users().get(nick).getCurrentParty()!=null) {
								if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider().equalsIgnoreCase(p.getName())) {
									if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyID() == CoreStorage.getStoraged_users().get(nick).getCurrentParty().getPartyID())
									{
										for(String user : CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers()) {
											CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().setPartyLider(nick);
											if(Bukkit.getPlayer(user)!=null) {
												Bukkit.getPlayer(user).sendMessage("§6"+nick+"§a é o novo líder da party.");
											}
										}
										
									}else {
										p.sendMessage("§cEste jogador não está em sua party!");
									}
								}else {
									p.sendMessage("§cVocê precisa ser o líder da party para transferir a liderança para outro jogador.");
									return true;
								}
									
								}else {
									p.sendMessage("§cEste jogador não está em sua party!");
								}
							}else {
								p.sendMessage("§cVocê não está em nenhuma party!");
								return true;
							}
							}else {
								p.sendMessage("§cO usuário '"+nick+"' não existe.");
								return true;
							}
						}else {
							GalaxyDungeonUser du = new GalaxyDungeonUser(p.getName());
							CoreStorage.getStoraged_users().put(p.getName(), du);
							p.sendMessage("§cOcorreu um erro, tente relogar.");
						}
					}else if(args[0].equalsIgnoreCase("expulsar")) {
						String nick = args[1];
						if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
						if(CoreStorage.getStoraged_users().containsKey(nick) ) {
							if(CoreStorage.getStoraged_users().get(p.getName())!=null) {
								if(CoreStorage.getStoraged_users().get(nick).getCurrentParty()!=null) {
									if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyLider().equalsIgnoreCase(p.getName())) {
										if(CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyID() == CoreStorage.getStoraged_users().get(nick).getCurrentParty().getPartyID())
										{
											for(String user : CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers()) {
												CoreStorage.getStoraged_users().get(p.getName()).getCurrentParty().getPartyMembers().remove(nick);
												if(!user.equalsIgnoreCase(nick)) {
												if(Bukkit.getPlayer(user)!=null) {
													Bukkit.getPlayer(user).sendMessage("§c"+nick+"§c foi expulso da party.");
												}
												}else {
													p.sendMessage("§cVocê foi expulso da party.");
												}
											}
										}else {
											p.sendMessage("§cEste jogador não está em sua party!");
										}
									}else {
										p.sendMessage("§cVocê precisa ser o líder da party para expulsar um jogador da party.");
										return true;
									}
								}else {
									p.sendMessage("§cEste jogador não está em sua party!");
								}
							}else {
								p.sendMessage("§cEste jogador não está em sua party!");
							}
						}else {
							p.sendMessage("§cO usuário '"+nick+"' não existe.");
							return true;
						}
						}else {
							GalaxyDungeonUser du = new GalaxyDungeonUser(p.getName());
							CoreStorage.getStoraged_users().put(p.getName(), du);
							p.sendMessage("§cOcorreu um erro, tente relogar.");
						}
					}
				}
			}else {
				Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeons] Erro - Este comando apenas pode ser executado in-game.");
				return true;
			}
		}
		return false;
	}

}
