package com.gabrielblink.galaxydungeons.scoreboard;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class ScoreBoardAPI {

	private Scoreboard scoreboard;

	private String title, objName = null;
	private Map<String, Integer> scores;
	private HashMap<Integer, Team> teams;
	private Objective obj = null;

	public ScoreBoardAPI(String title, String objName) {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.title = title;
		this.objName = objName;
		this.scores = Maps.newLinkedHashMap();
		this.teams = Maps.newLinkedHashMap();
	}

	public void blankLine() {
		add("§c ");
	}

	public void blankLine(int pos) {
		add("§c ", pos);
	}

	public HashMap<Integer, Team> getTeams() {
		return this.teams;
	}

	public void add(String text) {
		add(text, null);
	}

	
	
	public void add(String text, Integer score) {
		Preconditions.checkArgument(text.length() < 48, "text cannot be over 48 characters in length");
		text = fixDuplicates(text);
		text = ChatColor.translateAlternateColorCodes('&', text);
		scores.put(text, score);
	}

	private String fixDuplicates(String text) {
		while (scores.containsKey(text)) {
			text += "§r";
		}
		return text;
	}

	int nteams = 50;

	private Map.Entry<Team, String> createTeam(String text) {
		String result = "";
		Team team = scoreboard.registerNewTeam("" + nteams--);
		if (text.length() <= 16) {
			return new AbstractMap.SimpleEntry<>(team, text);
		}
		Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
		team.setPrefix(iterator.next());
		result = iterator.next();
		return new AbstractMap.SimpleEntry<>(team, result);
	}

	@SuppressWarnings({ "deprecation" })
	public void build() {
		obj = scoreboard.registerNewObjective(objName, "dummy");
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		int next = scores.size() - 2;
		int index = next + 1;
		int par = 0;
		for (Map.Entry<String, Integer> text : scores.entrySet()) {
			par = par + 2;
			Map.Entry<Team, String> team = createTeam(text.getKey());
			int score = text.getValue() != null ? text.getValue() : index;
			OfflinePlayerv2 player = new OfflinePlayerv2(team.getValue());
			team.getKey().addPlayer(player);
			obj.getScore(player).setScore(score);
			teams.put(score, team.getKey());
			index -= 1;
		}
	}

	public void setDisplayName(String displayName) {
		obj.setDisplayName(displayName);
	}

	public void reset() {
		title = null;
		scores.clear();
		for (Team t : teams.values()) {
			t.unregister();
		}
		teams.clear();
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void send(Player... players) {
		try {
			for (Player p : players) {
				p.setScoreboard(scoreboard);
			}
		} catch (NullPointerException e) {
		}
	}

	@SuppressWarnings("deprecation")
	public void update(String text, int score) {
		if (teams.containsKey(score)) {
			text = ChatColor.translateAlternateColorCodes('&', text);
			Team team = teams.get(score);
			if (text.length() <= 16) {
				team.setPrefix("");
				team.setSuffix(text);
				return;
			}

			Map.Entry<Team, String> team2 = makeTeam(text);

			if (score == 3)
				team2 = makeTeam("  §fOnline: §a"+Bukkit.getServer().getOnlinePlayers().size());

			OfflinePlayerv2 player = new OfflinePlayerv2(team2.getValue());
			team2.getKey().addPlayer(player);
			resetScore(score);
			obj.getScore(player).setScore(score);
		} else {
			text = ChatColor.translateAlternateColorCodes('&', text);
			add(text, score);
		}
	}

	public void resetScore(int score) {
		for (String s : obj.getScoreboard().getEntries()) {
			if (obj.getScore(s).getScore() == score) {
				getScoreboard().resetScores(s);
				return;
			}
		}
	}

	int nnn = 100;

	private Map.Entry<Team, String> makeTeam(String text) {
		String result = "";
		Team team = scoreboard.registerNewTeam("" + nnn++);
		if (text.length() <= 16) {
			return new AbstractMap.SimpleEntry<>(team, text);
		}
		Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
		team.setPrefix(iterator.next());
		result = iterator.next();
		return new AbstractMap.SimpleEntry<>(team, result);
	}

	public class OfflinePlayerv2 implements OfflinePlayer {

		private String playerName;

		public OfflinePlayerv2(String playerName) {
			this.playerName = playerName;
		}

		public boolean isOnline() {
			return false;
		}

		public String getName() {
			return this.playerName;
		}

		public UUID getUniqueId() {
			return UUID.nameUUIDFromBytes(this.playerName.getBytes(Charsets.UTF_8));
		}

		public void setName(String s) {
			this.playerName = s;
		}

		public boolean isBanned() {
			return false;
		}

		public void setBanned(boolean banned) {
			throw new UnsupportedOperationException();
		}

		public boolean isWhitelisted() {
			return false;
		}

		public void setWhitelisted(boolean value) {
			throw new UnsupportedOperationException();
		}

		public Player getPlayer() {
			throw new UnsupportedOperationException();
		}

		public long getFirstPlayed() {
			return System.currentTimeMillis();
		}

		public long getLastPlayed() {
			return System.currentTimeMillis();
		}

		public boolean hasPlayedBefore() {
			return false;
		}

		public org.bukkit.Location getBedSpawnLocation() {
			throw new UnsupportedOperationException();
		}

		public boolean isOp() {
			return false;
		}

		public void setOp(boolean value) {
			throw new UnsupportedOperationException();
		}

		public Map<String, Object> serialize() {
			throw new UnsupportedOperationException();
		}

	}
}
