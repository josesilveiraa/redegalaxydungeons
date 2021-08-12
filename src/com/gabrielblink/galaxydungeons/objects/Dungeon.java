package com.gabrielblink.galaxydungeons.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.gabrielblink.galaxydungeons.enums.ArmaduraType;

public class Dungeon {
	
	private String dungeonName;
	private String fancyDungeonName;
	private int price;
	private ItemStack item_stack;
	private Location lobby;
	private HashMap<String,Fase> fases = new HashMap<String,Fase>();
	private ArrayList<ItemStack> itens_iniciais = new ArrayList<ItemStack>();
	public HashMap<ArmaduraType,ItemStack> armor_inicial = new HashMap<ArmaduraType,ItemStack>();
	
	public Dungeon(String dungeonName, String fancyDungeonName, int price, ItemStack item_stack,
			HashMap<String, Fase> fases,ArrayList<ItemStack> itens_iniciais,HashMap<ArmaduraType,ItemStack> a,Location lobby) {
		super();
		this.dungeonName = dungeonName;
		this.fancyDungeonName = fancyDungeonName;
		this.price = price;
		this.item_stack = item_stack;
		this.fases = fases;
		this.itens_iniciais = itens_iniciais;
		this.armor_inicial = a;
		this.lobby = lobby;
	}
	
	

	public Location getLobby() {
		return lobby;
	}



	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}



	public HashMap<ArmaduraType, ItemStack> getArmor_inicial() {
		return armor_inicial;
	}



	public void setArmor_inicial(HashMap<ArmaduraType, ItemStack> armor_inicial) {
		this.armor_inicial = armor_inicial;
	}



	public ArrayList<ItemStack> getItens_iniciais() {
		return itens_iniciais;
	}



	public void setItens_iniciais(ArrayList<ItemStack> itens_iniciais) {
		this.itens_iniciais = itens_iniciais;
	}



	/**
	 * @return the dungeonName
	 */
	public String getDungeonName() {
		return dungeonName;
	}
	/**
	 * @param dungeonName the dungeonName to set
	 */
	public void setDungeonName(String dungeonName) {
		this.dungeonName = dungeonName;
	}
	/**
	 * @return the fancyDungeonName
	 */
	public String getFancyDungeonName() {
		return fancyDungeonName;
	}
	/**
	 * @param fancyDungeonName the fancyDungeonName to set
	 */
	public void setFancyDungeonName(String fancyDungeonName) {
		this.fancyDungeonName = fancyDungeonName;
	}
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * @return the item_stack
	 */

	/**
	 * @return the fases
	 */
	public HashMap<String, Fase> getFases() {
		return fases;
	}
	/**
	 * @return the item_stack
	 */
	public ItemStack getItem_stack() {
		return item_stack;
	}
	/**
	 * @param item_stack the item_stack to set
	 */
	public void setItem_stack(ItemStack item_stack) {
		this.item_stack = item_stack;
	}
	/**
	 * @param fases the fases to set
	 */
	public void setFases(HashMap<String, Fase> fases) {
		this.fases = fases;
	}
	
	
}
