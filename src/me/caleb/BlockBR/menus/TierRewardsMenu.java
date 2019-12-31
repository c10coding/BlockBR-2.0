package me.caleb.BlockBR.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import me.caleb.BlockBR.Main;


/*
 * This menu shows the rewards for THIS TIER
 * Each one of these menus has a unique value - It's tier
 */
public class TierRewardsMenu extends AbstractMenu implements Listener, InventoryHolder{

	private String tier;
	
	public TierRewardsMenu(Main plugin, Material mat) {
		super(plugin,"Tier Rewards", 9);
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}

	@Override
	public void initializeItems(Player p) {
		inv.addItem(createGuiItem(Material.ITEM_FRAME, chat("&6Items"), chat("&rThese are the items you get"), chat("&rwhen you complete this tier")));
		//inv.addItem(createGuiItem(Material.GOLD_NUGGET, chat("&6Money"),chat("&rYou get &5&l" + )))
		
		
		fillMenu();
	}

	@EventHandler
	protected void onInventoryClick(InventoryClickEvent e) {
		
	}
	
}
