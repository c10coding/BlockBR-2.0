package me.caleb.BlockBR.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Checker;

public class AmountMinedMenu extends AbstractMenu implements Listener, InventoryHolder{

	public AmountMinedMenu(Main plugin, String menuTitle, int numSlots) {
		super(plugin, menuTitle, numSlots);
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}

	@Override
	public void initializeItems(Player p) {
		
		DataManager dm = new DataManager(plugin,p);
		Checker c = new Checker(plugin, p);
		Group g = new Group(plugin);
		
		if(mineType.equalsIgnoreCase("group")){
		
			String group = dm.getGroup();
			List<String> groupTiers = g.getGroupTiers(group);
			
			for(String tier : groupTiers) {
				Material mat = Material.getMaterial(config.getString("Tiers." + tier + ".Properties.Material"));
				int amount = dm.getTierAmount(tier);
				int threshold = c.getThreshold(tier);
				
				if(amount == -1) {
					inv.addItem(createGuiItem(mat, chat("&6" + tier.toUpperCase()), chat("&rYou are done with this tier!")));
				}else {
					int diff = threshold - amount;
					if(diff < 100) {
						inv.addItem(createGuiItem(mat, chat("&6" + tier.toUpperCase()), chat("&rAmount: &5&o" + amount), chat("&rYou are almost there! You have to mine &5&o" + diff + "&r more blocks"), chat("&rto complete this tier!")));
					}else {
						inv.addItem(createGuiItem(mat, chat("&6" + tier.toUpperCase()), chat("&rAmount: &5&o" + amount), chat("&rYou have to mine &5&o" + diff + "&r more blocks"), chat("&rto complete this tier!")));
					}	
				}	
			}
			
			int invSlots = inv.getSize();
			int groupAmount = groupTiers.size();
			
			for(int x = groupAmount; x < invSlots; x++) {
				inv.setItem(x, createGuiItem());
				if(x == (invSlots - 1)) {
					inv.setItem(x, createGuiItem(Material.RED_WOOL, chat("&6Go back"), chat("&rClick me to go back to the"), chat("&rlast info menu!")));
				}
			}
			
		}else if(mineType.equalsIgnoreCase("all")) {
			
			List<String> tierList = config.getStringList("TierList");
			
			for(String tier : tierList) {
				Material mat = Material.getMaterial(config.getString("Tiers." + tier + ".Properties.Material"));
				int amount = dm.getTierAmount(tier);
				int threshold = c.getThreshold(tier);
				
				if(amount == -1) {
					inv.addItem(createGuiItem(mat,chat("&6" + tier.toUpperCase()), chat("You are done with this tier!")));
				}else {
					int diff = threshold - amount;
					if(diff < 100) {
						inv.addItem(createGuiItem(mat, chat("&6" + tier.toUpperCase()), chat("&rAmount: &5&o" + amount), chat("&rYou are almost there! You have to mine &5&o" + diff + "&r more blocks"), chat("&rto complete this tier!")));
					}else {
						inv.addItem(createGuiItem(mat, chat("&6" + tier.toUpperCase()), chat("&rAmount: &5&o" + amount), chat("&rYou have to mine &5&o" + diff + "&r more blocks"), chat("&rto complete this tier!")));
					}	
				}
				
			}
			
			fillMenu();
			
		}
		
		
		
		
	}

	@EventHandler
	protected void onInventoryClick(InventoryClickEvent e) {
		
		Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
		
		if (e.getInventory().getHolder() != this) return;
        
        if (e.getClick().equals(ClickType.NUMBER_KEY)) e.setCancelled(true);
        
        if(clickedItem == null && clickedItem.getType().equals(Material.AIR)) return;
		
        if(e.getRawSlot() == (inv.getSize()-1)) {
        	p.closeInventory();
        	InfoMenu i = new InfoMenu(plugin);
        	i.initializeItems(p);
        	i.openInventory(p);
        }
        
        
        e.setCancelled(true);
        
	}	
}
