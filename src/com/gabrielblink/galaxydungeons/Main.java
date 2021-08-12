package com.gabrielblink.galaxydungeons;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gabrielblink.galaxydungeons.apis.ItemBuilder;
import com.gabrielblink.galaxydungeons.apis.YamlConfig;
import com.gabrielblink.galaxydungeons.commands.CommandDungeon;
import com.gabrielblink.galaxydungeons.commands.CommandDungeonNPC;
import com.gabrielblink.galaxydungeons.commands.CommandGivePasse;
import com.gabrielblink.galaxydungeons.commands.CommandParty;
import com.gabrielblink.galaxydungeons.commands.CommandPronto;
import com.gabrielblink.galaxydungeons.customentitys.VendedorHorseEvent;
import com.gabrielblink.galaxydungeons.exceptions.DependenciesNotFoundException;
import com.gabrielblink.galaxydungeons.inventorys.LojaManager;
import com.gabrielblink.galaxydungeons.listeners.DungeonEvent;
import com.gabrielblink.galaxydungeons.listeners.DungeonGeneralEvents;
import com.gabrielblink.galaxydungeons.listeners.DungeonUserSetup;
import com.gabrielblink.galaxydungeons.listeners.EntityFixer;
import com.gabrielblink.galaxydungeons.maxidratemysql.DefaultSQLConnection;
import com.gabrielblink.galaxydungeons.maxidratemysql.MaxidrateMethods;
import com.gabrielblink.galaxydungeons.objects.DungeonServer;
import com.gabrielblink.galaxydungeons.objects.GalaxyDungeonUser;
import com.gabrielblink.galaxydungeons.postoffice.CorreioCommand;
import com.gabrielblink.galaxydungeons.postoffice.events.CorreioEvent;
import com.gabrielblink.galaxydungeons.scoreboard.ScoreUpdater;
import com.gabrielblink.galaxydungeons.storage.CoreStorage;
import com.gabrielblink.galaxydungeons.storage.Reload;
import com.google.common.io.Resources;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{

    public static Main instance;
    public static DefaultSQLConnection generalConnection;
    private static Economy econ = null;
    public static YamlConfig data;
    public static WorldEditPlugin worldEditPlugin = null;
    public static CashAPI cashAPI;
   
    public static Main getInstance() {
    	return instance;
    }
    private void loadScoreTask() {
		Bukkit.getOnlinePlayers().forEach(p -> {
			if(CoreStorage.getStoraged_users().containsKey(p.getName())) {
				if(CoreStorage.getStoraged_users().get(p.getName()).isInDungeon()) {
					 String dungeon_name = CoreStorage.getStoraged_users().get(p.getName()).getCurrentDungeon().getDungeonName();
	        		  DungeonServer DS = CoreStorage.getDungeonServers().get(dungeon_name);
	        		  ScoreUpdater.updateScoreBoard(p, DS);
				}
			}
		});
		new ScoreUpdater().runTaskTimerAsynchronously(this, 40L, 20L);
	}
    public static void antiBug() {
    	if(CoreStorage.getDungeonServers().size() > 0) { //Has any player playing on dungeon
    		for(String dungeon : CoreStorage.getDungeonServers().keySet()) {
    			DungeonServer DS = CoreStorage.getDungeonServers().get(dungeon);
    			DS.finishServer();
    			for(String onlinePlayer : DS.getPlayers_pepitas().keySet()) {
    				if(Bukkit.getPlayer(onlinePlayer)!=null) {
    					 Bukkit.getPlayer(onlinePlayer).getInventory().setHelmet(new ItemBuilder(Material.AIR).toItemStack());
						 Bukkit.getPlayer(onlinePlayer).getInventory().setChestplate(new ItemBuilder(Material.AIR).toItemStack());
						 Bukkit.getPlayer(onlinePlayer).getInventory().setLeggings(new ItemBuilder(Material.AIR).toItemStack());
						 Bukkit.getPlayer(onlinePlayer).getInventory().setBoots(new ItemBuilder(Material.AIR).toItemStack());
    					Bukkit.getPlayer(onlinePlayer).getInventory().clear();
    					Bukkit.getPlayer(onlinePlayer).teleport(CoreStorage.spawn);
    				}
    				if(CoreStorage.getStoraged_users().containsKey(onlinePlayer)) {
    					if(!CoreStorage.getStoraged_users().get(onlinePlayer).getAcessos_dungeon().containsKey(DS.getDungeon().getDungeonName())) {
    						CoreStorage.getStoraged_users().get(onlinePlayer).getAcessos_dungeon().put(DS.getDungeon().getDungeonName(), 1);
    					}else {
    						CoreStorage.getStoraged_users().get(onlinePlayer).getAcessos_dungeon().put(DS.getDungeon().getDungeonName(), CoreStorage.getStoraged_users().get(onlinePlayer).getAcessos_dungeon().get(DS.getDungeon().getDungeonName())+1);
    					}
    					CoreStorage.getStoraged_users().get(onlinePlayer).setCurrentDungeon(null);
    					CoreStorage.getStoraged_users().get(onlinePlayer).setCurrentFase(null);
    					CoreStorage.getStoraged_users().get(onlinePlayer).setCurrentParty(null);
    				}
    			}
    			Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeons] Desabilitando a dungeon "+dungeon+" pois o servidor está reiniciando.");
    		}
    	}
    }
	@Override
	public void onEnable() {
		saveDefaultConfig();
		data = new YamlConfig("data", this);
		saveDefaultConfig();
		try {
			  File file = new File(getDataFolder() + File.separator, "config.yml");
			  String allText = Resources.toString(file.toURI().toURL(), Charset.forName("UTF-8"));
			  getConfig().loadFromString(allText);
			} catch (Exception e) {
			  e.printStackTrace();
			}
		(Main.instance = this).saveDefaultConfig();
			Bukkit.getConsoleSender().sendMessage("§a[GalaxyDungeons] Inicializando o setup...");
			try {
				hookDependencies();
				Bukkit.getConsoleSender().sendMessage("§b[GalaxyDungeons] Dependencias lincadas com sucesso.");
			} catch (DependenciesNotFoundException e) {
			}
			getCommand("dungeon").setExecutor(new CommandDungeon());
			getCommand("pronto").setExecutor(new CommandPronto());
			getCommand("dungeonparty").setExecutor(new CommandParty());
			getCommand("givepasse").setExecutor(new CommandGivePasse());
			getCommand("setdungeonnpc").setExecutor(new CommandDungeonNPC());
			getCommand("correio").setExecutor(new CorreioCommand());
			CashAPI.hook(); //Tentando fazer hook com algum plugin de cash
			Bukkit.getPluginManager().registerEvents(new LojaManager(), this);
			Bukkit.getPluginManager().registerEvents(new EntityFixer(), this);
			Bukkit.getPluginManager().registerEvents(new DungeonEvent(), this);
			Bukkit.getPluginManager().registerEvents(new DungeonUserSetup(), this);
			Bukkit.getPluginManager().registerEvents(new DungeonGeneralEvents(), this);
			Bukkit.getPluginManager().registerEvents(new CommandDungeonNPC(), this);
			Bukkit.getPluginManager().registerEvents(new CorreioEvent(), this);
			Bukkit.getPluginManager().registerEvents(new VendedorHorseEvent(), this);
			CoreStorage.setupCustomItems();
			CoreStorage.setupUsingEntitys();
			CoreStorage.setupDungeons();
			CoreStorage.setupSpawn();
			CoreStorage.setupDungeonNPC();
			Reload.loadOnReload();
			loadScoreTask();
			setupMySQLConnection();
			load();
	}
	private void setupMySQLConnection() {
		generalConnection = new DefaultSQLConnection(
				"jdbc:mysql://" + Main.getInstance().getConfig().getString("Mysql.Host") + ":3306/"
						+ Main.getInstance().getConfig().getString("Mysql.Database") + "?autoReconnect=true",
				Main.getInstance().getConfig().getString("Mysql.Usuario"),
				Main.getInstance().getConfig().getString("Mysql.Senha"));
		generalConnection.openConnection();
		MaxidrateMethods.createTable();
	}
	
	@Override
	public void onDisable() {
		antiBug();
		CoreStorage.fix();
		for(String dungeonServer : CoreStorage.getDungeonServers().keySet()) {
			DungeonServer DS = CoreStorage.getDungeonServers().get(dungeonServer);
			DS.stopServer();
		}
		save();
		generalConnection.closeConnection();
	}
	public static boolean hasWorlds() {
		String jarpath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String mainfolderpath = jarpath.substring(0, jarpath.lastIndexOf("/plugins"));
		File userfiles;
		 try {
	            userfiles = new File(mainfolderpath + File.separator + "DungeonWorlds");
	            return userfiles.exists();
	        } catch(SecurityException e) {
	            // do something...
	            return false;
	        }
	}
	public static boolean hasWorldsInFolder() {
		String jarpath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String mainfolderpath = jarpath.substring(0, jarpath.lastIndexOf("/plugins"));
		File folder = new File(mainfolderpath + File.separator + "DungeonWorlds");
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles.length > 0) {
			return true;
		}else {
			return false;
		}
	}
	/*public static void setupWorlds() {
		
		String jarpath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String mainfolderpath = jarpath.substring(0, jarpath.lastIndexOf("/plugins"));
		if(!hasWorlds()) {
			Bukkit.getConsoleSender().sendMessage("§f[Path] "+mainfolderpath);
		  File userfiles;
	        try {
	            userfiles = new File(mainfolderpath + File.separator + "DungeonWorlds");
	            if(!userfiles.exists()){
	                userfiles.mkdirs();
	            }
	        } catch(SecurityException e) {
	        	Bukkit.getConsoleSender().sendMessage("§c[Galaxy World Loader] Error - SecurityException.");
	            return;
	        }
		}else {
			File folder = new File(mainfolderpath + File.separator + "DungeonWorlds");
			File[] listOfFiles = folder.listFiles();
			for(File f : listOfFiles) {
				 File destDir = new File(mainfolderpath+File.pathSeparator+f.getName());
			        try {
			            FileUtils.copyDirectory(folder.getAbsoluteFile(), destDir);
			        } catch (IOException ex) {
			            ex.printStackTrace();
			        }
				Bukkit.getConsoleSender().sendMessage("§6[Galaxy World Loader] Loading world "+f.getName());
				WorldCreator wcDragonBoss = new WorldCreator(f.getName()).environment(Environment.NORMAL).generateStructures(false);
				World world = wcDragonBoss.createWorld();
				Bukkit.getWorlds().add(world);
				Bukkit.getConsoleSender().sendMessage("§6[Galaxy World Loader] "+f.getName()+" loaded.");
			}
		}
	}*/
	public void moveFilesFromDirToDir(String srcDirPath, String destDirPath)
	{
		try
		{
			/* Apache commons io StringUtils class provide isNoneBlank() method. */
			if(StringUtils.isNoneBlank(srcDirPath) && StringUtils.isNoneBlank(destDirPath))
			{
				/* Create source directory instance. */
				File srcDirFile = new File(srcDirPath);
				/* Create target directory instance. */
				File destDirFile = new File(destDirPath);
				
				/* Move directory to directory. */
				/* The third parameter is true means create target directory if not exist. */
				FileUtils.moveDirectoryToDirectory(srcDirFile, destDirFile, true);
				
				System.out.println("Use Apache commons io to move all files from " + srcDirPath + " to " + srcDirPath + " successful. ");
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static void load() {
		for(String user : MaxidrateMethods.getAllUsers()) {
			String encoded_json = MaxidrateMethods.getJSON(user);
			GalaxyDungeonUser decoded_json = MaxidrateMethods.decodeGalaxyDungeonUser(encoded_json);
			CoreStorage.storaged_users.put(user, decoded_json);
		}
	}
	
	public static void save() {
		for(String user : CoreStorage.storaged_users.keySet()) {
			GalaxyDungeonUser gdu = CoreStorage.storaged_users.get(user);
			String encoded = MaxidrateMethods.encodeGalaxyDungeonUser(gdu);
			MaxidrateMethods.addGalaxyUser(user, encoded);
		}
	}
	
	public static YamlConfig getData() {
		return data;
	}
	
	public static Economy getEconomy() {
		return econ;
	}


	public boolean hookDependencies() throws DependenciesNotFoundException {
		//Settuping Vault Economy
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            throw new DependenciesNotFoundException("Vault");
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new DependenciesNotFoundException("iConomy/Plugin de economia");
        }
        econ = rsp.getProvider();
        //Setupping Citizens
        if (getServer().getPluginManager().getPlugin("Citizens") == null) {
            throw new DependenciesNotFoundException("Citizens");
        }
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
        	throw new DependenciesNotFoundException("WorldEdit");
        }else {
        	if(worldEditPlugin == null){
            	worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            }
        }
        
		return true;
	}
	

	
}
