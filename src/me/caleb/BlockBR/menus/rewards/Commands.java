package me.caleb.BlockBR.menus.rewards;

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
import me.caleb.BlockBR.menus.AbstractMenu;
import me.caleb.BlockBR.menus.TierRewardsMenu;

public class Commands extends AbstractMenu implements Listener, InventoryHolder{

	private List<String> commands;
	private String tier;
	private Material mat;
	
	public Commands(Main plugin, int numSlots, List<String> commands, String tier, Material mat) {
		super(plugin, "Commands", numSlots);
		this.commands = commands;
		this.tier = tier;
		this.mat = mat;
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}

	@Override
	public void initializeItems(Player p) {
		for(int x = 0; x < commands.size(); x++) {
			inv.addItem(createGuiItem(Material.COMMAND_BLOCK,chat("&6Command " + (x+1)), 1 , chat("&r&o" + commands.get(x))));
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
		int itemSize = commands.size();
		for(int x = itemSize;x < (inv.getSize()-1);x++) {
			inv.setItem(x, createGuiItem());
		}
		inv.addItem(createGuiItem(Material.RED_WOOL, chat("&6Go back"), chat("&rClick me to go back to the"), chat("&rlast menu!")));
	}
	
}
