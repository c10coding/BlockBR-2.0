package me.caleb.BlockBR.menus.rewards;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.menus.AbstractMenu;
import me.caleb.BlockBR.menus.TierRewardsMenu;
import me.caleb.BlockBR.utils.ItemParser;

public class Items extends AbstractMenu implements Listener, InventoryHolder{

	private List<String> items;
	private String tier;
	private Material mat;
	
	public Items(Main plugin, int numSlots, List<String> items, String tier, Material mat) {
		super(plugin, "Items", numSlots);
		this.items = items;
		this.tier = tier;
		this.mat = mat;
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}

	@Override
	public void initializeItems(Player p) {
		
		ItemParser ip;
		String line, name;
		Material itemMat;
		int amount;
		Map<Enchantment, Integer> enchants;
		
		for(int x = 0; x < items.size(); x++) {
			
			line = items.get(x);
			ip = new ItemParser(plugin, line, tier);
			itemMat = Material.getMaterial(ip.getItemName());
			amount = ip.getAmount();
			enchants = ip.getItemEnchants();
			name = ip.getItemName();
			
			if(name.contains("_")) {
				name = name.replace("_", " ");
			}
			
			/*
			 * Makes sure that each word has the first letter capitalized, but the rest lowercase
			 */
			String nameArr[] = name.split(" ");
			
			for(int i = 0; i < nameArr.length; i++) {
				nameArr[i] = nameArr[i].substring(0,1).toUpperCase() + nameArr[i].substring(1).toLowerCase();
			}
			
			name = String.join(" ", nameArr);
			
			
			if(!enchants.isEmpty()) {
				inv.addItem(createGuiItem(itemMat, chat("&6" + name), enchants, amount));
			}else {
				inv.addItem(createGuiItem(itemMat, chat("&6" + name), amount));
			}
			
		}
		fillMenu();
	}
	
	@EventHandler
	protected void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
		
        if (e.getInventory().getHolder() != this) return;
        
        if (e.getClick().equals(ClickType.NUMBER_KEY)) e.setCancelled(true);
        
        try {
        	if(e.getRawSlot() == (inv.getSize()-1)) {
        		TierRewardsMenu trm = new TierRewardsMenu(plugin, mat);
        		p.closeInventory();
        		trm.initializeItems(p);
        		trm.openInventory(p);	
        	}
        }catch(NullPointerException n) {
        	return;
        }
        
        e.setCancelled(true);
		
	}
	
	@Override
	protected void fillMenu() {
		int itemSize = items.size();
		for(int x = itemSize;x < (inv.getSize()-1);x++) {
			inv.setItem(x, createGuiItem());
		}
		inv.addItem(createGuiItem(Material.RED_WOOL, chat("&6Go back"), chat("&rClick me to go back to the"), chat("&rlast menu!")));
	}

}
