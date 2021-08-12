package com.gabrielblink.galaxydungeons.objects;

import java.util.ArrayList;

public class Party {

	private int partyID;
	private String partyLider;
	private ArrayList<String> partyMembers = new ArrayList<String>();
	
	public Party(int partyID,String partyLider) {
		this.partyID = partyID;
		this.partyLider = partyLider;
	}

	/**
	 * @return the partyID
	 */
	public int getPartyID() {
		return partyID;
	}

	/**
	 * @param partyID the partyID to set
	 */
	public void setPartyID(int partyID) {
		this.partyID = partyID;
	}

	/**
	 * @return the partyLider
	 */
	public String getPartyLider() {
		return partyLider;
	}

	/**
	 * @param partyLider the partyLider to set
	 */
	public void setPartyLider(String partyLider) {
		this.partyLider = partyLider;
	}

	/**
	 * @return the partyMembers
	 */
	public ArrayList<String> getPartyMembers() {
		return partyMembers;
	}

	/**
	 * @param partyMembers the partyMembers to set
	 */
	public void setPartyMembers(ArrayList<String> partyMembers) {
		this.partyMembers = partyMembers;
	}
	
}
