package com.gabrielblink.galaxydungeons.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gabrielblink.galaxydungeons.objects.Party;

public class PartyCreateEvent extends Event{

	private Party p;
	private String lider;
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	return handlers;
	}
	public PartyCreateEvent(Party p,String lider) {
		this.lider = lider;
		this.p = p;
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
