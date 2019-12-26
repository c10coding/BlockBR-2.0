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
		}
		
		if(!crate.equalsIgnoreCase("false")) {
			api.getCrateRegistrar().getCrate(crate).giveTo(p, 1);
		}
		
		if(!commands.isEmpty()) {
			for(String command : commands) {
				command = command.replace("%player%", p.getName());
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
			}
		}
		
		if(!items.isEmpty()) {
			for(ItemStack i : items) {
				p.getInventory().addItem(i);
			}
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
			ItemStack is = new ItemStack(Material.getMaterial(ip.getItemName()),ip.getAmount());
			is.addEnchantments(ip.getItemEnchants());
			items.add(is);
		}
				
		return items;
		
	}
}
