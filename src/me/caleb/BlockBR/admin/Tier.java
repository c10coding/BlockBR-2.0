package me.caleb.BlockBR.admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.utils.Chat;

public class Tier {

	private Main plugin;
	private String rewardType = null;
	
	
	public Tier(Main plugin) {
		this.plugin = plugin;
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
		Chat.sendConsoleMessage("The tier " + tierName.toUpperCase() + " has been removed?");
	}
	
	/*
	 * Does all the config work for the adding, removing, and editing
	 * of anything within the config
	 */
	public void configWork(String tierName, String action) {
		
		FileConfiguration config = plugin.getConfig();
		action = action.toLowerCase();
		
		//Removes the tier from the "TierList" list
		
		
		//Removes the tier from the "Tiers" section
		switch(action) {
		
			case "add":
				config.set("Tiers." + tierName + ".Properties.Material","Material.GRASS_BLOCK");
				config.set("Tiers." + tierName + ".Properties.Multiplier", 2.0);
				config.set("Tiers." + tierName + ".Properties.Threshold", 500);
				config.set("Tiers." + tierName + ".Properties.Reward.RewardType", "Crates");
				
				List<String> tierList = (List<String>) config.getList("TierList");
				tierList.add(tierName);
				
				break;
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
	
	public void tierEdit(String name, String property, String v) {
		
		switch(property) {
		case "mult":
			double value = Double.parseDouble(v);
			if(value < 0) {
				
			}
		}
		
	}
	
	public void rewardAdd() {
		
	}
	
	public void rewardRemove() {
		
	}

	public void checkCrateReloaded() {
		
	}
	
	public String getRewardType(String tier) {
		rewardType = plugin.getConfig().getString("Tiers." + tier+".Properties.Reward.RewardType");
		return rewardType;
	}
	
	
}
