package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import com.gabrielblink.galaxydungeons.Main;

public class MaxidrateCore {

	public static Connection con = Main.generalConnection.getConnection();
	public static ConsoleCommandSender sc = Bukkit.getConsoleSender();
	
	public static void createTable(){
		if (con != null){
			PreparedStatement stm = null;
			try {
				stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `galaxydungeons` (`id` INT NOT NULL AUTO_INCREMENT,`jogador` VARCHAR(35) NULL,`json` TEXT NOT NULL,PRIMARY KEY (`id`));");
				stm.executeUpdate();
				sc.sendMessage("§a[GalaxyDungeons] Tabela carregada");
			} catch (SQLException e) {
				sc.sendMessage("§c[GalaxyDungeons] Não foi possivel carregar a tabela");
			}
		}
	}

}
