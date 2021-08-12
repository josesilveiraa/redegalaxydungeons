package com.gabrielblink.galaxydungeons.apis;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
 
public class FrozenEntity {
    private Plugin plugin;
    private LivingEntity frozen;
    private Location spot;
    private int task = -1;
 
    public FrozenEntity(Plugin Plugin, LivingEntity e) {
        this.plugin = Plugin;
        this.frozen = e;
        spot = e.getLocation();
        freeze();
    }
 
    private void freeze() {
        task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (frozen.isDead()) {
                    if (task != -1) {
                        plugin.getServer().getScheduler().cancelTask(task);
                    }
                    return;
                } else {
                    if (!frozen.getLocation().equals(spot)) {
                        frozen.teleport(spot);
                    }
                }
            }
        }, 0L, 1L);
    }
}
