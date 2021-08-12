package com.gabrielblink.galaxydungeons.objects;

import java.util.HashMap;

public class GalaxyDungeonUser {
	
	private String Player;
	private HashMap<String,Correio> correio = new HashMap<String,Correio>();
	private HashMap<String,Integer> acessos_dungeon = new HashMap<String,Integer>();
	private Dungeon currentDungeon;
	private Fase currentFase;
	private Party currentParty;
	private HashMap<Integer, Party> convites_de_party = new HashMap<Integer,Party>();
	
	public GalaxyDungeonUser(String jogador) {
		this.Player = jogador;
	}
	public boolean hasParty() {
		if(currentParty == null) {
			return false;
		}else {
			return true;
		}
	}
	public Fase getCurrentFase() {
		return currentFase;
	}

	public void setCurrentFase(Fase currentFase) {
		this.currentFase = currentFase;
	}

	public boolean isInDungeon() {
		if(currentDungeon == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public Dungeon getCurrentDungeon() {
		return currentDungeon;
	}



	public void setCurrentDungeon(Dungeon currentDungeon) {
		this.currentDungeon = currentDungeon;
	}



	public Party getCurrentParty() {
		return currentParty;
	}


	public void setCurrentParty(Party currentParty) {
		this.currentParty = currentParty;
	}


	public HashMap<Integer, Party> getConvites_de_party() {
		return convites_de_party;
	}


	public void setConvites_de_party(HashMap<Integer, Party> convites_de_party) {
		this.convites_de_party = convites_de_party;
	}


	public HashMap<String, Correio> getCorreio() {
		return correio;
	}

	
	public void setCorreio(HashMap<String, Correio> correio) {
		this.correio = correio;
	}

	public HashMap<String, Integer> getAcessos_dungeon() {
		return acessos_dungeon;
	}
	public void setAcessos_dungeon(HashMap<String, Integer> acessos_dungeon) {
		this.acessos_dungeon = acessos_dungeon;
	}
	public String getPlayer() {
		return Player;
	}

	public void setPlayer(String player) {
		Player = player;
	}

	
	
	
}
