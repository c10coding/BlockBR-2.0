package me.caleb.BlockBR.rewards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.CratePlugin;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Chat;
import me.caleb.BlockBR.utils.ItemParser;

public class RewardHandler {

	private Main plugin;
	private FileConfiguration config;
	private String baseRewardPath;
	private String basePropPath;
	private String tier;
	private Player p;
	private static CratePlugin api = CrateAPI.getInstance();
	
	public RewardHandler(Main plugin, String tier, Player p) {
		this.plugin = plugin;
		config = plugin.getConfig();
		this.tier = tier;
		this.p = p;
		baseRewardPath = "Tiers." + tier + ".Properties.Rewards.";
		basePropPath = "Tiers." + tier + ".Properties.";
	}
	
	public void giveRewards() {
		
		int money = getMoney();
		String crate = getCrate();
		List<String> commands = getCommands();
		ArrayList<ItemStack> items = getItems();
		
		// If money is not 0, i.e. if it's not false or 0 in config
		if(money > 0) {
			Main.getEconomy().depositPlayer(p,money);
			Chat.sendPlayerMessage(p, "&bYou have been given &5&l " + money);
		}else {
			Bukkit.getConsoleSender().sendMessage(Chat.blockBrChat("Money is equal to false. Ignoring the money reward..."));
		}
		
		if(!crate.equalsIgnoreCase("false")) {
			if(!plugin.checkCrateReloaded() == false) {
				try {
					api.getCrateRegistrar().getCrate(crate).giveTo(p, 1);
					Chat.sendPlayerMessage(p, "You have been given a &5&l" + crate + " crate key!");
				}catch(NullPointerException e) {
					Chat.sendPlayerMessage(p, "&4There has been an error when attempting to give you the crate key. Please contact an admin!");
					Chat.sendConsoleMessage("The crate " + crate + " has not been found! Please check your CrateReloaded config to make sure that this is an actual crate");
				}
				
			}else {
				Chat.sendConsoleMessage("CrateReloaded is not enabled! Ignoring the crate reward... If you wish to install the plugin, go to https://www.spigotmc.org/resources/free-crate-reloaded-mystery-crate-1-8-1-14-x.861/");
			}	
		}else {
			Chat.sendConsoleMessage("The crate is set to false. Ignoring the crate reward...");
		}
		
		if(!commands.isEmpty()) {
			for(String command : commands) {
				if(command.contains("%player%")) {
					command = command.replace("%player%", p.getName());
				}
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
			}
		}else {
			Chat.sendConsoleMessage("There are no commands for this tier! Ignoring the commands reward...");
		}
		
		if(!items.isEmpty()) {
			
			for(ItemStack i : items) {
				p.getInventory().addItem(i);
			}
			
			Chat.sendPlayerMessage(p, "You have been given the following items for completing this tier:");
			for(ItemStack i : items) {
				Chat.sendPlayerMessage(p, i.getType().name() + " x " + i.getAmount());
			}
			
		}else {
			Chat.sendConsoleMessage("There are no items for this tier! Ignoring the items reward...");
		}
		
	}

	public int getMoney() {
		
		DataManager dm = new DataManager(plugin, p);
		int level = dm.getLevel();
		int money;
		
		try {
			money = config.getInt(baseRewardPath + "Money");
		}catch(NullPointerException e) {
			money = -1;
		}
		
		double moneyMult = config.getDouble(basePropPath + "MoneyMultiplier");
		
		if(level > 1) {
			money = (int) (money * (moneyMult * (level - 1)));
		}
		
		return money;
		
	}
	
	public String getCrate() {
		return config.getString(baseRewardPath + "Crate");
	}
	
	public List<String> getCommands(){
		List<String> commands = config.getStringList(baseRewardPath + "Commands");
		return commands;
	}

	public ArrayList<ItemStack> getItems() {
		
		ArrayList<ItemStack> items = new ArrayList();
		List<String> itemList = config.getStringList(baseRewardPath + "Items");

		for(int x = 0; x < itemList.size(); x++) {
			String line = itemList.get(x);
			ItemParser ip = new ItemParser(plugin, line,tier);
			ItemStack is = new ItemStack(Material.valueOf(ip.getItemName().toUpperCase()),ip.getAmount());
			
			is.addEnchantments(ip.getItemEnchants());
			items.add(is);
		}
				
		return items;
		
	}
}
