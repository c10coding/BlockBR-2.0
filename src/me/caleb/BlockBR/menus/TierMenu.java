package me.caleb.BlockBR.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

public class TierMenu extends AbstractMenu implements Listener, InventoryHolder{

	public TierMenu(Main plugin,String title, int numSlots) {
		super(plugin, title, numSlots);
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}

	@Override
	public void initializeItems(Player p) {
		
		if(mineType.equalsIgnoreCase("group")) {
			
			List<String> groupList = config.getStringList("GroupList");
			DataManager dm = new DataManager(plugin,p);
			Group g = new Group(plugin);
			
			/*
			 * Gets every group, gets it's tiers, and then displays what tiers are in each group
			 */
			for(int x = 0; x < groupList.size(); x++) {
				List<String> tiers = g.getGroupTiers(groupList.get(x));
				if(x % 2 == 0) {
					inv.addItem(createGuiItem(Material.SLIME_BALL, chat("&6" + groupList.get(x)), tiersArray(tiers)));
				}else {
					inv.addItem(createGuiItem(Material.MAGMA_CREAM, chat("&6" + groupList.get(x)), tiersArray(tiers)));
				}
			}
			
			fillMenu(groupList);
			
		}else if(mineType.equalsIgnoreCase("all") || mineType.equalsIgnoreCase("onebyone")) {
			
			for(int x = 0; x < tierList.size(); x++) {
				Material mat = Material.getMaterial(config.getString("Tiers." + tierList.get(x) + ".Properties.Material"));
				inv.addItem(createGuiItem(mat,chat("&6" + tierList.get(x))));
			}
			
			fillMenu(tierList);
			
		}
		
	}
	
	public String[] tiersArray(List<String> tiers) {
		return tiers.toArray(new String[0]);
	}
	
	@EventHandler
	protected void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
		
        if (e.getInventory().getHolder() != this) return;
        
        if (e.getClick().equals(ClickType.NUMBER_KEY)) e.setCancelled(true);
        
        try {
        	if(e.getRawSlot() == (inv.getSize()-1)) {
        		InfoMenu im = new InfoMenu(plugin);	
        		p.closeInventory();
        		im.initializeItems(p);
        		im.openInventory(p);
        	}
        }catch(NullPointerException n) {
        	return;
        }
        
        e.setCancelled(true);
		
	}

}
