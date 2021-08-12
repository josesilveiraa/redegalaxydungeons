package com.gabrielblink.galaxydungeons.objects;

import java.util.ArrayList;

public class Correio {

	private String data;
	private String Cause;
	private ArrayList<String> itens = new ArrayList<String>();
	
	
	public Correio(String data, ArrayList<String> itens,String Cause) {
		super();
		this.data = data;
		this.itens = itens;
		this.Cause = Cause;
	}
	
	
	public String getCause() {
		return Cause;
	}

	public void setCause(String cause) {
		Cause = cause;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the itens
	 */
	public ArrayList<String> getItens() {
		return itens;
	}
	/**
	 * @param itens the itens to set
	 */
	public void setItens(ArrayList<String> itens) {
		this.itens = itens;
	}
	
	
}
