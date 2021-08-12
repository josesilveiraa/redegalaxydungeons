package com.gabrielblink.galaxydungeons.exceptions;

import org.bukkit.Bukkit;

public class DungeonServerNotFoundException extends Exception{

	
	private static final long serialVersionUID = 1L;
	
	
	public DungeonServerNotFoundException(String dungeon) {
		super(dungeon);
		Bukkit.getConsoleSender().sendMessage("§4[GalaxyDungeons] Erro crítico: A dungeon "+dungeon+" não foi encontrada.");
	}


}
