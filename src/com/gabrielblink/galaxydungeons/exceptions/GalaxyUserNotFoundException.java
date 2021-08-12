package com.gabrielblink.galaxydungeons.exceptions;

import org.bukkit.Bukkit;

public class GalaxyUserNotFoundException extends Exception{

	
	private static final long serialVersionUID = 1L;
	
	
	public GalaxyUserNotFoundException(String user) {
		super(user);
		Bukkit.getConsoleSender().sendMessage("§4[GalaxyDungeons] Erro crítico: O usuário "+user+" solicitado pela API não foi encontrado.");
	}


}
