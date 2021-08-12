package com.gabrielblink.galaxydungeons.postoffice;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gabrielblink.galaxydungeons.apis.HiddenStringUtils;
import com.gabrielblink.galaxydungeons.apis.ItemBuilder;
import com.gabrielblink.galaxydungeons.apis.ScrollerInventory;
import com.gabrielblink.galaxydungeons.objects.Correio;
import com.gabrielblink.galaxydungeons.postoffice.events.CorreioEvent;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;

public class CorreioCommand implements CommandExecutor{

	public static void enviarEncomenda(Player p,String user) {
		Inventory inv = Bukkit.createInventory(null, 54,"Encomenda "+user);
		
		p.openInventory(inv);
	}
	public static void abrirEncomenda(Correio c,Player p) {
		
		ArrayList<ItemStack> i = new ArrayList<ItemStack>();
		
		for(String item_encrypted : c.getItens()) {
			try {
				i.add(CorreioEvent.fromBase64(item_encrypted));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ScrollerInventory si = new ScrollerInventory(i, "Correio"+HiddenStringUtils.encodeString("ec"), p,true,c.getData());
		
	}
	public static void open(Player p) {
		if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
		if(CoreStorage.getStoraged_users().get(p.getName()).getCorreio().size() >0) {
		ArrayList<ItemStack> itens = new ArrayList<ItemStack>();
		for(String s : CoreStorage.getStoraged_users().get(p.getName()).getCorreio().keySet()) {
			Correio c = CoreStorage.getStoraged_users().get(p.getName()).getCorreio().get(s);
			itens.add(new ItemBuilder(Material.STORAGE_MINECART).setName("§aEnviado por: §f"+c.getCause()+" §8("+c.getItens().size()+" "+(c.getItens().size() > 1 ? "itens" : "item")+")").setLore("§eClique para ver"+HiddenStringUtils.encodeString(s)).toItemStack());
		}
		ScrollerInventory si = new ScrollerInventory(itens, "Correio", p,false,"");
		}else {
			Inventory inv = Bukkit.createInventory(null, 54,"Correio");
			inv.setItem(22, new ItemBuilder(Material.WEB).setName("§cVazio").toItemStack());
			p.openInventory(inv);
		}
		}else {
			
			p.sendMessage("§cOcorreu um erro ao abrir o correio, tente relogar.");
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("correio")) {
			if(sender instanceof Player)
			{
				Player p = (Player)sender;
				if(p.hasPermission("galaxy.dungeons.correio")) {
				if(args.length == 0) {
					p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
					p.sendMessage("§aAbrindo correio...");
					open(p);
				}
				if(args.length == 1) {
					p.sendMessage("§cUtilize: /correio encomenda <jogador>");
					return true;
				}
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("encomenda")) {
						String user = args[1];
						if(CoreStorage.getStoraged_users().containsKey(user)) {
							enviarEncomenda(p, user);
						}else {
							p.sendMessage("§cEste usuário não existe.");
							return true;
						}
					}
				}
				}else {
				p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
				p.sendMessage("§aAbrindo correio...");
				open(p);
				}
			}else {
				Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeons] Este comando apenas pode ser executado in-game.");
				return true;
			}
		}
		return false;
	}

}
