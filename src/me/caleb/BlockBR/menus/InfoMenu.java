package me.caleb.BlockBR.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Checker;

public class InfoMenu extends AbstractMenu implements Listener, InventoryHolder{

	public InfoMenu(Main plugin, String menuTitle, int numSlots) {
		super(plugin, menuTitle, numSlots);
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}
	
	@Override
	public void initializeItems(Player p) {
		
		DataManager dm = new DataManager(plugin, p);
		int level = dm.getLevel();
		
		if(mineType.equalsIgnoreCase("group")) {
			
			Group g = new Group(plugin);

			String group = dm.getGroup();
			int groupNum = g.getNumGroup(group);
			
			inv.addItem(createGuiItem(Material.SLIME_BALL, chat("&6Current Group: &5&l" + group.toUpperCase()),groupNum, chat("&rThis is the group and group number"), chat("&r that you are on!")));
			inv.addItem(createGuiItem(Material.IRON_PICKAXE, chat("&6Amount mined"), chat("&rClick me to see the amount you have"), chat("&rmined in each tier!")));
			inv.addItem(createGuiItem(Material.EXPERIENCE_BOTTLE, chat("&6Current level: &5&l" + level), level, chat("&rThis is the level that you are currently on")));
			inv.addItem(createGuiItem(Material.CHEST, chat("&6Potential rewards"),chat("&rClick me to see potential rewards for each tier!")));
			inv.addItem(createGuiItem(Material.BEACON, chat("&6Tiers in each group"), chat("&rClick me to see the tiers in each group!")));
			
			for(int x = 5; x < inv.getSize(); x++) {
				inv.setItem(x, createGuiItem());
			}
			
		}else if(mineType.equalsIgnoreCase("onebyone")) {
			
			String tier = dm.getTier();
			String matString = config.getString("Tiers." + tier + "Properties.Material");
			Material mat = Material.getMaterial(matString);
			
			Checker c = new Checker(plugin);
			int thresh = c.getThreshold(tier);
			int amount = dm.getTierAmount(tier);
			int diff = thresh - amount;
			
			inv.addItem(createGuiItem(mat, chat("&6Current tier: &5&l" + tier.toUpperCase()), chat("&rYou need mine " + diff + " more blocks to get to complete this tier!")));
			inv.addItem(createGuiItem(Material.EXPERIENCE_BOTTLE, chat("&6Current level: &5&l" + level), level, chat("&rThis is the level that you are currently on")));
			inv.addItem(createGuiItem(Material.IRON_PICKAXE, chat("&6Amount mined"), chat("&rThis is the amount you have mined on"),chat("&ron this tier")));
			inv.addItem(createGuiItem(Material.CHEST, chat("&6Potential rewards"),chat("&rClick me to see potential rewards for each tier!")));
			inv.addItem(createGuiItem(Material.BEACON, chat("&6Tiers in each group"), chat("&rClick me to see the tiers in each group!")));
			
		}else if(mineType.equalsIgnoreCase("all")) {
			inv.addItem(createGuiItem(Material.EXPERIENCE_BOTTLE, chat("&6Current level: &5&l" + level), level, chat("&rThis is the level that you are currently on")));
			inv.addItem(createGuiItem(Material.IRON_PICKAXE, chat("&6Amount mined"), chat("&rClick me to see the amount you have"), chat("&rmined in each tier!")));
			inv.addItem(createGuiItem(Material.CHEST, chat("&6Potential rewards"),chat("&rClick me to see potential rewards for each tier!")));
			inv.addItem(createGuiItem(Material.BEACON, chat("&6Tiers in each group"), chat("&rClick me to see the tiers in each group!")));
		}
		
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if (e.getInventory().getHolder() != this) {
            return;
        }
        
        if (e.getClick().equals(ClickType.NUMBER_KEY)){
            e.setCancelled(true);
        }
        
        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        
        if(clickedItem == null && clickedItem.getType().equals(Material.AIR)) return;
        
        if(mineType.equalsIgnoreCase("group")) {
        	
        	if(e.getRawSlot() == 1) {
        		AmountMinedMenu a = new AmountMinedMenu(plugin, "Amount Mined", findSlotAmount());
        	}else if(e.getRawSlot() == 3) {
        		
        	}else if(e.getRawSlot() == 4) {
        		
        	}
        	
        }
        
        e.setCancelled(true);
		
	}
	
	private int findSlotAmount() {
		int slotSize = 0;
		List<String> tierList = config.getStringList("TierList");
		
		do {
			slotSize+=9;
		}while(slotSize < tierList.size());
		
		return slotSize;
		
	}

}
