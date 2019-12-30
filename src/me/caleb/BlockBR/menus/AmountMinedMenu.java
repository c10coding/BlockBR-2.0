package me.caleb.BlockBR.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

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
		
		dm = new DataManager(plugin,p);
		List<String> tierList = config.getStringList("TierList");
		Checker c = new Checker(plugin);
		 
		if(mineType.equalsIgnoreCase("group")){
		
			String group = dm.getGroup();
			
			for(String tier : tierList) {
				
				Material mat = Material.getMaterial(config.getString("Tiers." + tier + ".Properties.Material"));
				int amount = dm.getTierAmount(tier);
				int thresh = c.getThreshold(tier);
				int diff = thresh - amount;
				
				inv.addItem(createGuiItem(mat, chat("&6 " + tier), chat("&rYou need to mine " + diff), chat("&rto complete this group!")));
				
			}
			
			/*
			 * Inv size should be larger than tier size
			 */
			int invSlot = inv.getSize();
			int tierSize = tierList.size();
			
			
		}
		
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e) {
		
	}
	
	
	
}
