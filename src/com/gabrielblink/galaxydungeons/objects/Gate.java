package com.gabrielblink.galaxydungeons.objects;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

import com.gabrielblink.galaxydungeons.Main;
import com.gabrielblink.galaxydungeons.enums.GateStatus;

public class Gate {

	private GateStatus gateStatus;
	private Location loc1;
	private Location loc2;
	private HashMap<Location,Material> gate_comeback = new HashMap<Location,Material>();
	
	public Gate(GateStatus gateStatus,Location loc1, Location loc2) {
		super();
		this.gateStatus = gateStatus;
		this.loc1 = loc1;
		this.loc2 = loc2;
	}
	public void closeGate() {
			World w = loc1.getWorld();
			for(Location l : gate_comeback.keySet()) {
				w.getBlockAt(l).setType(gate_comeback.get(l));
			}
			gateStatus = GateStatus.CLOSED;
	}
	public void openGate() {
		if(gateStatus.equals(GateStatus.CLOSED)) {
			int MinX, MaxX, MinY, MaxY, MinZ, MaxZ;
			
			if(loc1.getX() < loc2.getX()) {
				MinX = loc1.getBlockX();
				MaxX = loc2.getBlockX();
			}else {
				MinX = loc2.getBlockX();
				MaxX = loc1.getBlockX();
			}
			
			if(loc1.getY() < loc2.getY()) {
				MinY = loc1.getBlockY();
				MaxY = loc2.getBlockY();
			}else {
				MinY = loc2.getBlockY();
				MaxY = loc1.getBlockY();
			}
			
			if(loc1.getZ() < loc2.getZ()) {
				MinZ = loc1.getBlockZ();
				MaxZ = loc2.getBlockZ();
			}else {
				MinZ = loc2.getBlockZ();
				MaxZ = loc1.getBlockZ();
			}
			World world = loc1.getWorld();
			for(int x = MinX; x <= MaxX; x++) {
				for(int y = MinY; y <= MaxY; y++) {
					for(int z = MinZ; z <= MaxZ; z++) {
						Block b = world.getBlockAt(x, y, z);
						if(!b.getType().equals(Material.BARRIER)) {
						b.getLocation().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId());
						}
						gate_comeback.put(b.getLocation(), b.getType());
						b.setType(Material.AIR);
					}
				}
			}
			gateStatus = GateStatus.OPENED;
		}else {
			Bukkit.getConsoleSender().sendMessage("§c[GalaxyDungeons] Este Gate já foi aberto.");
		}
	}
	public GateStatus getGateStatus() {
		return gateStatus;
	}

	public void setGateStatus(GateStatus gateStatus) {
		this.gateStatus = gateStatus;
	}

	public Location getLoc1() {
		return loc1;
	}
	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}
	public Location getLoc2() {
		return loc2;
	}
	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}
	
	
}
