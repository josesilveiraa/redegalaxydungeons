package com.gabrielblink.galaxydungeons.exceptions;

import org.bukkit.Bukkit;

import com.gabrielblink.galaxydungeons.Main;

public class DependenciesNotFoundException extends Exception{

	
	private static final long serialVersionUID = 1L;
	
	
	public DependenciesNotFoundException(String faltando) {
		super(faltando);
		Bukkit.getConsoleSender().sendMessage("§4[GalaxyDungeons] Erro crítico: Dependencia: "+faltando+" não encontrada.");
		Bukkit.getPluginManager().disablePlugin(Main.getPlugin(Main.class));
	}


}
