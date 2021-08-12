package com.gabrielblink.galaxydungeons.holograms;

public interface ArmorHologram {
  
  public void setText(String text);
  
  public void killEntity();
  
  public HologramLine getLine();
  
  public Hologram getHologram();
}
