package me.caleb.BlockBR.menus;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.sql.DataManager;

public abstract class AbstractMenu implements Listener, InventoryHolder{

	public final Inventory inv;
	private Main plugin;
	
	public AbstractMenu(Main plugin, String menuTitle, int numSlots, DataManager dm) {
		inv = Bukkit.createInventory(this, numSlots, menuTitle);
		this.plugin = plugin;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public void openInventory(Player p) {
		p.openInventory(inv);
	}
	
	/*
	 * If the item has enchantments
	 */
	public static ItemStack createGuiItem(Material mat, String name, Map<Enchantment,Integer> enchants, int amount, String...lore) {
		
		ItemStack item = new ItemStack(mat,1);
		ItemMeta meta = item.getItemMeta();
		ArrayList<Enchantment> itemEnchants = new ArrayList<Enchantment>();
		ArrayList<Integer> itemLevels = new ArrayList<Integer>();
		
		//Gets all the enchants from the map
		for(Entry<Enchantment, Integer> en : enchants.entrySet()){
			itemEnchants.add(en.getKey());
		}
		
		//Gets all the levels from the map
		for(Entry<Enchantment, Integer> en : enchants.entrySet()){
			itemLevels.add(en.getValue());
		}
		
		for(int x = 0; x < enchants.size(); x++) {
			
			if(!item.getType().equals(Material.NETHER_STAR)) {
				meta.addEnchant(itemEnchants.get(x), itemLevels.get(x), true);
			}else {
				EnchantmentStorageMeta metaE = (EnchantmentStorageMeta) item.getItemMeta();
				metaE.addStoredEnchant(itemEnchants.get(x), itemLevels.get(x), true);
				item.setItemMeta(metaE);
			}
			
		}
		
		meta.setDisplayName(name);
		item.setAmount(amount);
		
		ArrayList<String> metaLore = new ArrayList<String>();
		
		for(String lorecomments : lore) {
			metaLore.add(lorecomments);
		}
		
		if(!item.getType().equals(Material.ENCHANTED_BOOK)) {
			meta.setLore(metaLore);
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
	/*
	 * No enchants. Regular items
	 */
	public static ItemStack createGuiItem(Material material, String name,int amount, String...lore) {
		
		ItemStack item = new ItemStack(material,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setAmount(amount);
		ArrayList<String> metaLore = new ArrayList<String>();
		
		for(String lorecomments : lore) {
			
			metaLore.add(lorecomments);
			
		}
		
		meta.setLore(metaLore);
		item.setItemMeta(meta);
		return item;
		
	}
	
	//If you only want one item
	public static ItemStack createGuiItem(Material material, String name, String...lore) {
		
		ItemStack item = new ItemStack(material,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		
		ArrayList<String> metaLore = new ArrayList<String>();
		
		for(String lorecomments : lore) {
			
			metaLore.add(lorecomments);
			
		}
		
		meta.setLore(metaLore);
		item.setItemMeta(meta);
		return item;
	}
	
	//Filler item
	public static ItemStack createGuiItem() {
		
		ItemStack item = new ItemStack(Material.CYAN_STAINED_GLASS_PANE,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("");
		meta.setLore(null);
		
		return item;
	}
	
	public abstract void initializeItems(String tier, int amount, int level, double threshold);
	
	@EventHandler
    public abstract void onInventoryClick(InventoryClickEvent e);
	
}
