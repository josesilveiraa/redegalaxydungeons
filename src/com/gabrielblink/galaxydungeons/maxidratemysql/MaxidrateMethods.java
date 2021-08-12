package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;
import com.google.gson.Gson;

public class MaxidrateMethods extends MaxidrateCore {

	public static String getJSON(String player) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("SELECT * FROM `galaxydungeons` WHERE `jogador` = ?");
			stm.setString(1, player);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				return rs.getString("json");
			}
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeonsCore] Erro interno no servidor MySQL.");
			e.printStackTrace();
		}
		return null;
	}

	public static boolean contains(String user) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("SELECT * FROM `galaxydungeons` WHERE `jogador` = ?");
			stm.setString(1, user);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeonsCore] Erro interno no servidor MySQL.");
			e.printStackTrace();
			return false;
		}
	}
	public static String encodeGalaxyDungeonUser(GalaxyDungeonUser gdu) {
		gdu.setCurrentDungeon(null);
		gdu.getConvites_de_party().clear();
		gdu.setCurrentFase(null);
		gdu.setCurrentParty(null);
		Gson gson = new Gson();
    	String objectencoded = gson.toJson(gdu);
    	return objectencoded;
	}
	public static GalaxyDungeonUser decodeGalaxyDungeonUser(String encoded) {
		Gson gson = new Gson();
		GalaxyDungeonUser f = gson.fromJson(encoded, GalaxyDungeonUser.class);
    	return f;
	}
	public static GalaxyDungeonUser downloadGalaxyUser(String jogador) {
		GalaxyDungeonUser du = decodeGalaxyDungeonUser(getJSON(jogador));
		CoreStorage.getStoraged_users().put(jogador, du);
		return du;
	}

	public static void updateState(String jogador, String json) {
		Main.generalConnection.runUpdate(new SQL("UPDATE `galaxydungeons` SET `json` = ? WHERE `jogador` = ?;", json, jogador), true);
	}


	public static void addGalaxyUser(String player, String json) {
		if(!contains(player)) {
			Main.generalConnection.runUpdate(
					new SQL("INSERT INTO `galaxydungeons`(`jogador`, `json`) VALUES (?,?)", player, json), true);
		}else {
			updateState(player, json);
		}
	}

	public static void deleteUser(String name) {
		Main.generalConnection.runUpdate(new SQL("DELETE FROM `galaxydungeons` WHERE `jogador` = ?;", name), true);
	}

	public static ArrayList<String> getAllUsers() {
		ArrayList<String> list = new ArrayList<String>();
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("SELECT * FROM `galaxydungeons`");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("jogador"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
