package me.caleb.BlockBR.admin;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.BooleanControl.Type;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.utils.Chat;

public class Tier {

	private Main plugin;
	private String rewardType = null;
	private FileConfiguration config = null;
	
	public Tier(Main plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
	}
	/*
	 * Method crates a column within the database with the tier name
	 */
	public void tierAdd(String tierName,Player p) {
		
		tierName = tierName.toLowerCase();
		//SQL WORK
		try {
			String query = "ALTER TABLE `blockbr2` ADD " + tierName + " varchar(255) NOT NULL";
			PreparedStatement stmt = plugin.getConnection().prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			Chat.sendPlayerMessage(p, "There has been an error while adding tier to database!");
			Chat.sendPlayerMessage(p, "Is there already a tier named &6&l" + tierName + " &rin the database?");
			return;
		}
		
		Chat.sendPlayerMessage(p, "The new tier has been created!");
		Chat.sendConsoleMessage("The new tier " + tierName.toUpperCase() + " has been added to the database!");
	}
	
	/*
	 * Removes the tier from the database
	 */
	public void tierRemove(String tierName, Player p) {
		
		tierName = tierName.toLowerCase();
		//SQL WORK
		try {
			String query = "ALTER TABLE `blockbr2` DROP " + tierName;
			PreparedStatement stmt = plugin.getConnection().prepareStatement(query);
			
			stmt.executeUpdate();
		}catch(SQLException e) {
			Chat.sendPlayerMessage(p, "There has been an error while removing the tier");
			Chat.sendPlayerMessage(p, "Are you sure there is a tier named &6&l" + tierName.toUpperCase() + " &rin the database?");
			Chat.sendConsoleMessage("Are you sure there is a tier named &6&l" + tierName.toUpperCase() + " &rin the database?");
			return;
		}
		
		Chat.sendPlayerMessage(p, "The tier has been removed");	
		Chat.sendConsoleMessage("The tier " + tierName.toUpperCase() + " has been removed!");
	}
	
	public void tierEditName(String tier, String newTier,Player p) {
		
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("ALTER TABLE `blockbr2` CHANGE " + tier + " " + newTier + " varchar(255)");
			stmt.executeUpdate();
		} catch (SQLException e) {
			Chat.sendPlayerMessage(p, "There has been an error in the database! Please contact an admin!");
		}
		
	}
	
	/*
	 * Does all the config work for the adding, removing, and editing
	 * of anything within the config
	 */
	public void configWork(String tierName, String action) {
		
		action = action.toLowerCase();	
		
		//Removes the tier from the "Tiers" section
		switch(action) {
		
			case "add":
				config.set("Tiers." + tierName + ".Properties.Material","Material.GRASS_BLOCK");
				config.set("Tiers." + tierName + ".Properties.Multiplier", 2.0);
				config.set("Tiers." + tierName + ".Properties.Threshold", 500);
				config.set("Tiers." + tierName + ".Properties.Rewards.Money", 0);
				config.set("Tiers." + tierName + ".Properties.Rewards.Crate", "CrateNameGoesHere");
				
				List<String> itemList = new ArrayList<String>();
				itemList.add("name:Apple amount:64 Enchants: Damage_all 1;Protection 1");
				config.set("Tiers." + tierName + ".Properties.Rewards.Items", itemList);
				
				List<String> commandList = new ArrayList<String>();
				commandList.add("give %player% apple 64");
				config.set("Tiers." + tierName + ".Properties.Rewards.Commands", commandList);
				
				List<String> tierList = (List<String>) config.getList("TierList");
				tierList.add(tierName);
				
				break;
			//Removes all the values by setting them to null, which removes them
			case "remove":
				config.set("Tiers." + tierName + ".Properties.Material",null);
				config.set("Tiers." + tierName + ".Properties.Multiplier", null);
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
				
				try {
					//Chat.sendPlayerMessage(p, mat.toString());
					for(Material materialType : Material.values()) {
						if(mat.equals(materialType)) {
							config.set("Tiers." + name + ".Properties.Material", mat.name());
							Chat.sendPlayerMessage(p, "&bThe Material for tier &5&l" + name.toUpperCase() + " &bhas been set to &5&l" + mat.toString());
						}	
					}
					
				}catch(NullPointerException e) {
					Chat.sendPlayerMessage(p, "&cThis is not an item. To see all the different materials, refer to this link: &5https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html");
				}
				
				break;
				
			case "threshold":
				
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
	
	public void getTierInfo() {
		
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
		
		Chat.sendPlayerMessage(p, "&4List of tiers:");
		for(String tier : tierList) {
			p.sendMessage(Chat.chat("&b&l- " + tier));
		}
		
		
	}

}
