package com.gabrielblink.galaxydungeons.apis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;


public class ItemBuilder {
   private ItemStack is;

   public ItemBuilder(Material m){
     this(m, 1);
   }

   public ItemBuilder(ItemStack is){
     this.is=is;
   }

   public ItemBuilder(Material m, int quantia){
     is= new ItemStack(m, quantia);
   }

   public ItemBuilder(Material m, int quantia, byte durabilidade){
     is = new ItemStack(m, quantia, durabilidade);
   }

   public ItemBuilder clone(){
     return new ItemBuilder(is);
   }

   public ItemBuilder setDurability(short durabilidade){
     is.setDurability(durabilidade);
     return this;
   }
   
    public ItemBuilder setDurability(int durabilidade){
     is.setDurability(Short.valueOf(""+durabilidade));
     return this;
   }

   public ItemBuilder setName(String nome){
     ItemMeta im = is.getItemMeta();
     im.setDisplayName(nome);
     is.setItemMeta(im);
     return this;
   }

   public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
     is.addUnsafeEnchantment(ench, level);
     return this;
   }

   public ItemBuilder removeEnchantment(Enchantment ench){
     is.removeEnchantment(ench);
     return this;
   }

   public ItemBuilder setSkullOwner(String dono){
     try{
       SkullMeta im = (SkullMeta)is.getItemMeta();
       im.setOwner(dono);
       is.setItemMeta(im);
     }catch(ClassCastException expected){}
     return this;
   }

   public ItemBuilder addEnchant(Enchantment ench, int level){
     ItemMeta im = is.getItemMeta();
     im.addEnchant(ench, level, true);
     is.setItemMeta(im);
     return this;
   }

   public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments){
     is.addEnchantments(enchantments);
     return this;
   }

   public ItemBuilder setInfinityDurability(){
     is.setDurability(Short.MAX_VALUE);
     return this;
   }
   
    public ItemBuilder addItemFlag(ItemFlag flag){
    	ItemMeta im = is.getItemMeta();
    	im.addItemFlags(flag);
    	is.setItemMeta(im);
        return this;
   }
   
   public ItemBuilder setLore(String... lore){
     ItemMeta im = is.getItemMeta();
     im.setLore(Arrays.asList(lore));
     is.setItemMeta(im);
     return this;
   }

   public ItemBuilder setLore(List<String> lore) {
     ItemMeta im = is.getItemMeta();
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }

   public ItemBuilder removeLoreLine(String linha){
     ItemMeta im = is.getItemMeta();
     List<String> lore = new ArrayList<>(im.getLore());
     if(!lore.contains(linha))return this;
     lore.remove(linha);
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }

   public ItemBuilder removeLoreLine(int index){
     ItemMeta im = is.getItemMeta();
     List<String> lore = new ArrayList<>(im.getLore());
     if(index<0||index>lore.size())return this;
     lore.remove(index);
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }

   public ItemBuilder addLoreLine(String linha){
     ItemMeta im = is.getItemMeta();
     List<String> lore = new ArrayList<>();
     if(im.hasLore())lore = new ArrayList<>(im.getLore());
     lore.add(linha);
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }
 
   public ItemBuilder addLoreLine(String linha, int pos){
     ItemMeta im = is.getItemMeta();
     List<String> lore = new ArrayList<>(im.getLore());
     lore.set(pos, linha);
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }

   @SuppressWarnings("deprecation")
   public ItemBuilder setDyeColor(DyeColor cor){
     this.is.setDurability(cor.getData());
     return this;
   }

   @Deprecated
   public ItemBuilder setWoolColor(DyeColor cor){
    if(!is.getType().equals(Material.WOOL))return this;
    this.is.setDurability(cor.getData());
    return this;
   }

   public ItemBuilder setLeatherArmorColor(Color cor){
     try{
       LeatherArmorMeta im = (LeatherArmorMeta)is.getItemMeta();
       im.setColor(cor);
       is.setItemMeta(im);
     }catch(ClassCastException expected){}
     return this;
   }

   public ItemStack toItemStack(){
     return is;
   }
}
