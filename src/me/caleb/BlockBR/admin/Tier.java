package me.caleb.BlockBR.admin;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.BooleanControl.Type;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Chat;

public class Tier {

	private Main plugin;
	private String rewardType = null;
	private FileConfiguration config = null;
	Player p;
	
	public Tier(Main plugin, Player p) {
		this.plugin = plugin;
		config = plugin.getConfig();
		this.p = p;
	}
	/*
	 * Method creates a column within the database with the tier name
	 */
	public void tierAdd(String tierName,Player p) {
		
		tierName = tierName.toLowerCase();
		//SQL WORK
		try {
			String query = "ALTER TABLE `blockbr2` ADD " + tierName + " varchar(255) NOT NULL DEFAULT 0";
			PreparedStatement stmt = plugin.getConnection().prepareStatement(query);
			int rowsAffected = stmt.executeUpdate();
			
			if(rowsAffected == 0) {
				configWork(tierName, "add");
				Chat.sendPlayerMessage(p, "The new tier has been created!");
			}
			
		} catch (SQLException e) {
			Chat.sendPlayerMessage(p, "There has been an error while adding tier to database!");
			Chat.sendPlayerMessage(p, "Is there already a tier named &6&l" + tierName + " &rin the database?");
			return;
		}
	}
	
	/*
	 * Removes the tier from the database
	 */
	public void tierRemove(String tierName, Player p) {
		
		Tier t = new Tier(plugin, p);
		tierName = tierName.toLowerCase();
		
		if(!t.isTier(p, tierName)) {
			Chat.sendPlayerMessage(p, "This is not a tier!");
		}
	
		//SQL WORK
		try {
			String query = "ALTER TABLE `blockbr2` DROP " + tierName;
			PreparedStatement stmt = plugin.getConnection().prepareStatement(query);
			
			int rowsAffected = stmt.executeUpdate();
			
			if(rowsAffected == 0) {
				configWork(tierName, "remove");
				Chat.sendPlayerMessage(p, "The tier has been removed!");
			}
			
		}catch(SQLException e) {
			Chat.sendPlayerMessage(p, "There has been an error while removing the tier");
			Chat.sendPlayerMessage(p, "Are you sure there is a tier named &6&l" + tierName.toUpperCase() + " &rin the database?");
			return;
		}
	}
	
	/*
	 * Removes all tiers
	 */
	public void tierRemove(Player p) {
		
		List<String> tierList = config.getStringList("TierList");
		
		if(tierList.size() == 0) {
			Chat.sendPlayerMessage(p, "There are no tiers to delete!");
		}else {
			try {
				
				for(int x = 0; x < tierList.size(); x++) {
					String tierName = tierList.get(x).toLowerCase();
					String query = "ALTER TABLE `blockbr2` DROP " + tierName;
					PreparedStatement stmt = plugin.getConnection().prepareStatement(query);
					int rowsAffected = stmt.executeUpdate();
					
					if(rowsAffected == 0) {
						configRemoveAll();
					}
					
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
				Chat.sendPlayerMessage(p, "There has been an error while removing a tier. Please contact an admin or the plugin owner");
				return;
			}
			
			Chat.sendPlayerMessage(p, "All tiers have been removed!");
		}
		
	}
	
	public void tierEditName(String tier, String newTier,Player p) {
		
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("ALTER TABLE `blockbr2` CHANGE " + tier + " " + newTier + " varchar(255)");
			stmt.executeUpdate();
		} catch (SQLException e) {
			Chat.sendPlayerMessage(p, "There has been an error in the database! Please contact an admin!");
		}
		
	}
	
	public String getMaterialName() {
		
		String matList[] = {Material.GRASS_BLOCK.name(),Material.OAK_LOG.name(),Material.STONE.name(),Material.COAL_ORE.name(), Material.REDSTONE_ORE.name(), Material.LAPIS_ORE.name(), Material.IRON_ORE.name(),Material.GOLD_ORE.name(),Material.OBSIDIAN.name(),Material.DIAMOND_ORE.name(),Material.EMERALD_ORE.name(),Material.DIRT.name(),Material.SAND.name(),Material.CLAY.name(),Material.DIORITE.name(),Material.GRANITE.name(),Material.NETHER_QUARTZ_ORE.name(),Material.SANDSTONE.name(),Material.STONE_BRICKS.name(),Material.GRAY_WOOL.name(),Material.LIGHT_GRAY_WOOL.name(),Material.LIGHT_BLUE_WOOL.name(),Material.PURPLE_WOOL.name(),Material.BLUE_WOOL.name(),Material.BROWN_WOOL.name()};
		ArrayList<String> tierMatList = new ArrayList<String>();
		List<String> tierList = (List<String>) config.getList("TierList");
		
		/*
		 * Populates the array with the material that you currently have in the configuration file
		 */
		for(String tier : tierList) {
			tierMatList.add(config.getString("Tiers." + tier + ".Properties.Material"));
		}
		
		for(int x = 0; x < tierList.size(); x++) {
			if(!tierMatList.contains(matList[x])) {
				return matList[x];
			}
		}
		
		return Material.GRASS_BLOCK.name();
	}
	
	/*
	 * Does all the config work for the adding, removing, and editing
	 * of anything within the config
	 */
	public void configWork(String tierName, String action) {
		
		action = action.toLowerCase();	
		List<String> tierList = (List<String>) config.getList("TierList");
		//Removes the tier from the "Tiers" section
		switch(action) {
		
			case "add":

				tierName = tierName.toLowerCase();
				
				if(tierList.size() == 0) {
					DataManager dm = new DataManager(plugin,p);
					dm.setTier(tierName);
				}
				
				tierList.add(tierName);
				
				config.set("TierList", tierList);
				config.set("Tiers." + tierName + ".Properties.Material",getMaterialName());
				config.set("Tiers." + tierName + ".Properties.Multiplier", 2.0);
				config.set("Tiers." + tierName + ".Properties.MoneyMultiplier", 2.0);
				config.set("Tiers." + tierName + ".Properties.Threshold", 500);
				config.set("Tiers." + tierName + ".Properties.Rewards.Money", 0);
				config.set("Tiers." + tierName + ".Properties.Rewards.Crate", "CrateNameGoesHere");
				
				List<String> itemList = new ArrayList<String>();
				config.set("Tiers." + tierName + ".Properties.Rewards.Items", itemList);
				
				List<String> commandList = new ArrayList<String>();
				config.set("Tiers." + tierName + ".Properties.Rewards.Commands", commandList);
				
				break;
			//Removes all the values by setting them to null, which removes them
			case "remove":
				
				if(removalOfFirstTier(tierName)) {
					/*
					 * Exception to this. If remove all is done, then none of this should occur
					 */
					try {
						String nextTier = tierList.get(1);
						DataManager dm = new DataManager(plugin,p);
						dm.alterFirstTiers(tierName, nextTier);	
					}catch(IndexOutOfBoundsException e) {
						Chat.sendConsoleMessage("There is no second tier column to set the tier to. Ignoring...");
					}

				}
				config.set("Tiers." + tierName + ".Properties.Material",null);
				config.set("Tiers." + tierName + ".Properties.Multiplier", null);
				config.set("Tiers." + tierName + ".Properties.MoneyMultiplier", null);
				config.set("Tiers." + tierName + ".Properties.Threshold", null);
				config.set("Tiers." + tierName + ".Properties.Reward.RewardType", null);
				config.set("Tiers." + tierName + ".Properties.Reward", null);
				config.set("Tiers." + tierName + ".Properties", null);
				config.set("Tiers." + tierName, null);
				config.getList("TierList").remove(tierName);
				break;
			default:
				break;
		}
		
		plugin.saveConfig();
		
	}
	
	public boolean removalOfFirstTier(String tier) {
		List<String> tierList = (List<String>) config.getList("TierList");
		//If this is the first tier
		if(tierList.get(0).equalsIgnoreCase(tier) ) {
			return true;
		}else {
			return false;
		}
	}
	
	public void configRemoveAll() {
		
		List<String> tierList = config.getStringList("TierList");
		
		for(int x = 0; x < tierList.size(); x++) {
			configWork(tierList.get(x).toLowerCase(), "remove");
		}

	}
	
	public boolean ifMatInConfig(Material mat) {
		ArrayList<String> tierMatList = new ArrayList<String>();
		List<String> tierList = (List<String>) config.getList("TierList");
		
		/*
		 * Populates the array with the material that you currently have in the configuration file
		 */
		for(String tier : tierList) {
			tierMatList.add(config.getString("Tiers." + tier + ".Properties.Material"));
		}
		
		if(tierMatList.contains(mat.name())) {
			Chat.sendPlayerMessage(p, "&4You already have a tier of this material. Please choose another material! &rTo see all the different materials, refer to this link: &ohttps://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html ");
			return true;
		}else {
			return false;
		}
	}
	
	public void tierEdit(String name, String property, String v,Player p) {
		
		FileConfiguration config = plugin.getConfig();
		
		//Checks to see if the path in the config even exists before it does anything else
		if(config.getString("Tiers." + name) == null) {
			Chat.sendPlayerMessage(p, "&cThis tier does not exist! Do &6&l/bbr tierlist &bto see the different tier names!");
			return;
		}
		
		switch(property.toLowerCase()) {
		
			case "mult":
				
				try {
						
					double value = Double.parseDouble(v);
					
					//Checks to see if the new multiplier given is 0, or less than 0
						//I don't understand why this if statement works
					if(Math.signum(value) > 0) {
						config.set("Tiers." + name + ".Properties.Multiplier", value);
						Chat.sendPlayerMessage(p, "&bThe multiplier has been edited for the tier &5&l" + name.toUpperCase());
					}else if(value == 0.0){
						config.set("Tiers." + name + ".Properties.Multiplier", value);
						Chat.sendPlayerMessage(p, "&bThe multiplier has been edited for the tier &5&l" + name.toUpperCase() +  " &bBe wary that the value is &5&l0!");
					}else {
						Chat.sendPlayerMessage(p, "&cThe multiplier must be &c&lgreater &cthan 0!");
						return;
					}
					
				}catch(NumberFormatException e) {
					Chat.sendPlayerMessage(p, "This is not a number! Please try again...");
					return;
				}
				
				break;
				
			case "mat":
				
				Material mat = Material.matchMaterial(v);
				
				if(ifMatInConfig(mat) == true) return;
				
				try {
					for(Material materialType : Material.values()) {
						if(mat.equals(materialType)) {
							config.set("Tiers." + name + ".Properties.Material", mat.name());
							Chat.sendPlayerMessage(p, "&bThe material for tier &5&l" + name.toUpperCase() + " &bhas been set to &5&l" + mat.toString());
						}	
					}
					
				}catch(NullPointerException e) {
					Chat.sendPlayerMessage(p, "&cThis is not an item. To see all the different materials, refer to this link: &ohttps://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
				}
				
				break;
				
			case "thresh":
				
				try {
					//If it's a decimal, you can't cast it straight to a integer. Input String double -> Double -> Integer cast
					double d = Double.parseDouble(v);
					int thresh = (int) d;
					
					if(thresh < 0) {
						Chat.sendPlayerMessage(p, "&cThis must be a positive number!");
					}else {
						config.set("Tiers." + name + ".Properties.Threshold", thresh);
						Chat.sendPlayerMessage(p, "&bThe threshold for tier &5&l" + name.toUpperCase() + " &bhas been set to &5&l" + thresh);
					}
					
				}catch(NumberFormatException e) {
					Chat.sendPlayerMessage(p, "This is not a number! Please try again...");
					return;
				}
				
				break;
			default:
				Chat.sendPlayerMessage(p, "An error has occurred");
				break;
		}
		
		plugin.saveConfig();	
	}
	
	public void tierMove(String tierName, String newGroupName) {
		
		//Checks to see if the tier given is an actual tier
		if(isTier(p,tierName) == false) return;
		
		//Checks to see if the MineType is type "Group"
		if(Group.isTypeGroup() == false) return;
		
		//Checks to see if the group given is an actual group
		if(Group.isGroup(newGroupName) == false) return;
		
		//Tests to see if the tier is in a group at all
		//If not, just put it it the last group
		String group = Group.tierGroup(tierName);
		List<String> allGroups = config.getStringList("GroupList");
		
		try {
			
			if(Group.tierGroup(tierName).equalsIgnoreCase(newGroupName)) {
				Chat.sendPlayerMessage(p, "&bThis tier is already in group &5&l" + newGroupName + "!");
				return;
			}
		//Will return null when the tier is not in a group
		}catch(NullPointerException e) {
			
			Chat.sendPlayerMessage(p, "The tier was not in a group previously. Tier &5&l" + tierName + " is now in group &5&l" + allGroups.get(allGroups.size()-1));
			List<String> lastGroup = config.getStringList("Groups." + allGroups.get(allGroups.size()-1));
			lastGroup.add(tierName);
			config.set("Groups." + allGroups.get(allGroups.size()-1), lastGroup);
			plugin.saveConfig();
			return;
			
		}

		//Removes the tier from that group
		List<String> tierGroup = config.getStringList("Groups." + group);
		tierGroup.remove(tierName);
		config.set("Groups." + group, tierGroup);
		
		//Adds the tier to the new group
		List<String> newGroup = config.getStringList("Groups." + newGroupName);
		newGroup.add(tierName);
		config.set("Groups." + newGroupName, newGroup);
		Chat.sendPlayerMessage(p, "&bTier &5&l" + tierName.toUpperCase() + "&b has been moved from &5&l" + group.toUpperCase() + "&b to group &5&l" + newGroupName.toUpperCase());
		
		plugin.saveConfig();
	}
	
	//Checks to see if the path in the config even exists before it does anything else
	public boolean isTier(Player p, String tierName) {
		
		if(config.getString("Tiers." + tierName.toLowerCase()) == null) {
			Chat.sendPlayerMessage(p, "&bThis is not a tier! Do &6&l/bbr tierlist &bto see all the different tiers!");
			return false;
		}
		return true;
		
	}
	
	public void getTierInfo(Player p, String tier) {
		
		if(isTier(p,tier) == false) {
			return;
		}
		
		String material = config.getString("Tiers." + tier + ".Properties.Material");
		double mult = config.getDouble("Tiers." + tier + ".Properties.Multiplier");
		int threshold = config.getInt("Tiers." + tier + ".Properties.Threshold");
		int money = config.getInt("Tiers." + tier + ".Properties.Rewards.Money");
		String crate = config.getString("Tiers." + tier + ".Properties.Rewards.Crate");
		List<String> itemList = (List<String>) config.getList("Tiers." + tier + ".Properties.Rewards.Items");
		List<String> commandList = (List<String>) config.getStringList("Tiers." + tier + ".Properties.Rewards.Commands");
		
		Chat.sendPlayerMessage(p, "&bTier info for tier &5&l" + tier.toUpperCase() );
		
		p.sendMessage(Chat.chat("&b&lMaterial: &5" + material));
		p.sendMessage(Chat.chat("&b&lMultiplier: &5" + mult));
		p.sendMessage(Chat.chat("&b&lThreshold: &5" + threshold));
		p.sendMessage(Chat.chat("&b&lMoney: &5" + money));
		p.sendMessage(Chat.chat("&b&lCrate: &5" + crate));
		
		p.sendMessage(Chat.chat("&b&lItems:"));
		
		int count = 1;
		for(String item : itemList) {
			p.sendMessage(Chat.chat("&b&l" + count + ".) &5" + item));
			count++;
		}
		
		count = 1;
		
		p.sendMessage(Chat.chat("&b&lCommands:"));
		for(String command : commandList) {
			p.sendMessage(Chat.chat("&b&l" + count + ".) &5" + command));
			count++;
		}
		
	}
	
	public void tierRename(String tier, String newTier,Player p) {
		
		Rewards r = new Rewards(plugin);
		//If it's not a tier, tierRename won't even be executed
		if(r.isTier(tier,p) == false) {
			return;
		}
		
		String material = config.getString("Tiers." + tier + ".Properties.Material");
		double mult = config.getDouble("Tiers." + tier + ".Properties.Multiplier");
		int threshold = config.getInt("Tiers." + tier + ".Properties.Threshold");
		int money = config.getInt("Tiers." + tier + ".Properties.Rewards.Money");
		String crate = config.getString("Tiers." + tier + ".Properties.Rewards.Crate");
		List<String> itemList = (List<String>) config.getList("Tiers." + tier + ".Properties.Rewards.Items");
		p.sendMessage(itemList.toString());
		List<String> commandList = (List<String>) config.getStringList("Tiers." + tier + ".Properties.Rewards.Commands");
		List<String> tierList = (List<String>) config.getList("TierList");
		tierList.add(newTier);
		
		config.set("Tiers." + newTier + ".Properties.Material", material);
		config.set("Tiers." + newTier + ".Properties.Multiplier", mult);
		config.set("Tiers." + newTier + ".Properties.Threshold", threshold);
		config.set("Tiers." + newTier + ".Properties.Rewards.Money", money);
		config.set("Tiers." + newTier + ".Properties.Rewards.Crate", crate);
		config.set("Tiers." + newTier + ".Properties.Rewards.Items", itemList);
		config.set("Tiers." + newTier + ".Properties.Rewards.Commands", commandList);
		
		Chat.sendPlayerMessage(p, "The tier &5&l" + tier.toUpperCase() + " has been renamed to &5&l" + newTier.toUpperCase());
		tierEditName(tier,newTier,p);
		configWork(tier,"remove");
		
		plugin.saveConfig();
	}
	
	public void showTierList(Player p) {
		
		List<String> tierList = (List<String>) plugin.getConfig().getList("TierList");
		
		try {
			if(tierList.isEmpty()) {
				Chat.sendPlayerMessage(p, "There are no tiers yet! Do create one, do &6&l/bbr tier add (tiername)");
			}else {
				Chat.sendPlayerMessage(p, "List of tiers:");
				int counter = 1;
				for(String tier : tierList) {
					p.sendMessage(Chat.chat("&b&l " + counter + ".) " + tier));
					counter++;
				}	
			}	
		}catch(NullPointerException e) {
			Chat.sendPlayerMessage(p, "There are no tiers yet! To create one, do &6&l/bbr tier add (tiername)");
			return;
		}
	
	}
	
	/*
	 * This method is very poorly written. Fix in future
	 */
	public boolean isTierListEmpty() {
		List<String> tierList = (List<String>) plugin.getConfig().getList("TierList");
		
		try {
			if(tierList.isEmpty())
				return true;
			else
				return false;
		}catch(NullPointerException e) {
			return true;
		}
		
	}

}
