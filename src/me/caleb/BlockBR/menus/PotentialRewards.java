package me.caleb.BlockBR.menus;

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

public class PotentialRewards extends AbstractMenu implements Listener, InventoryHolder{

	public PotentialRewards(Main plugin, int slotSize) {
		super(plugin, "Potential Rewards", slotSize);
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}

	@Override
	public void initializeItems(Player p) {
		
		for(String tier : tierList) {
			Material mat = Material.getMaterial(config.getString("Tiers." + tier + ".Properties.Material"));
			inv.addItem(createGuiItem(mat,chat("&6" + tier.toUpperCase()), chat("&rClick me to see the rewards"), chat("&rfor this tier!")));
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
        	if(e.getRawSlot() != (inv.getSize()-1) && !(clickedItem.getType().equals(FILLER_MATERIAL))) {
             	Material mat = clickedItem.getType();
             	TierRewardsMenu trm = new TierRewardsMenu(plugin,mat);
             	p.closeInventory();
             	trm.initializeItems(p);
             	trm.openInventory(p);
             }else if(e.getRawSlot() == (inv.getSize()-1)) {
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
