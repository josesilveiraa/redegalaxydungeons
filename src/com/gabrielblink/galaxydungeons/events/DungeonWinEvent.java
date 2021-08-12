package com.gabrielblink.galaxydungeons.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gabrielblink.galaxydungeons.objects.Dungeon;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.Party;

public class DungeonWinEvent extends Event{

	private Party p;
	private String lider;
	private Dungeon d;
	private DungeonServer DS;
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	return handlers;
	}
	
	public DungeonWinEvent(Party p, String lider, Dungeon d, DungeonServer dS) {
		super();
		this.p = p;
		this.lider = lider;
		this.d = d;
		DS = dS;
	}

	public Dungeon getD() {
		return d;
	}

	public void setD(Dungeon d) {
		this.d = d;
	}

	public DungeonServer getDS() {
		return DS;
	}

	public void setDS(DungeonServer dS) {
		DS = dS;
	}

	public Party getP() {
		return p;
	}

	public void setP(Party p) {
		this.p = p;
	}

	public String getLider() {
		return lider;
	}

	public void setLider(String lider) {
		this.lider = lider;
	}
	
	

	

}
