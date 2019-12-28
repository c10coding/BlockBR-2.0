package me.caleb.BlockBR.menus;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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
	
	/*
	 * If the item has enchantments
	 */
	public static ItemStack createGuiItem(Material mat, String name, Map<Enchantment,Integer> enchants, int amount, String...lore) {
		
		ItemStack item = new ItemStack(mat,1);
		
		for(int x = 0; x < enchants.size(); x++) {
			
			if(!item.getType().equals(Material.NETHER_STAR)) {
				ItemMeta meta = item.getItemMeta();
			}else {
				EnchantmentStorageMeta metaE = (EnchantmentStorageMeta) item.getItemMeta();
				metaE.addStoredEnchant(, arg1, arg2)
			}
			
		}
		
		return null;
	}
}
