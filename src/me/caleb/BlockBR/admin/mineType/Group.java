package me.caleb.BlockBR.admin.mineType;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.utils.Chat;

public class Group {

	private static Main plugin;
	private static FileConfiguration config;
	private static Player p;
	
	public Group(Main plugin, Player p) {
		this.plugin = plugin;
		config = plugin.getConfig();
		this.p = p;
	}
	
	public Group(Main plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
	}
	
	/*
	 * Method that returns a 2d array
	 * of the groups
	 */
	public static ArrayList<ArrayList<String>> formGroups() {
		
		List<String> tierList = (List<String>) config.getList("TierList");
		ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
		
		//3 groups
		groups.add(new ArrayList<String>());
		groups.add(new ArrayList<String>());
		groups.add(new ArrayList<String>());
		
		int listSize = tierList.size();
		
		int groupIndexNum = 0;
		for(int x = 0; x < listSize; x++) {
			groups.get(groupIndexNum).add(tierList.get(x));
			if(groupIndexNum == 2) {
				groupIndexNum = 0;
			}else {
				groupIndexNum++;
			}
		}
		
		return groups;
		
	}
	
	/*
	 * Shows the user the list of groups
	 */
	
	public static void showGroupList() {
		
		if (isTypeGroup() == false) return;
		
		List<String> groupList = config.getStringList("GroupList");
		
		Chat.sendPlayerMessage(p, "List of Groups:");
		
		for(String group : groupList) {
			p.sendMessage(Chat.chat("&b&l- " + group));
		}
		
	}
	
	public static void groupTiers(String groupName) {
		
		if (isTypeGroup() == false) return;
		
		if (isGroup(groupName) == false) return;
		
		List<String> groupTiers = config.getStringList("Groups." + groupName);
		
		if(groupTiers.isEmpty()) {
			Chat.sendPlayerMessage(p, "You do not have any tiers in this group!");
		}else {
			
			Chat.sendPlayerMessage(p, "Group tiers:");
			
			for(String tier : groupTiers) {
				p.sendMessage(Chat.chat("&b&l- " + tier));
			}
			
		}
		
	}
	
	/*
	 * Formulates the config appropriately when switched to MineType Group
	 * Deletes everything the formulation did when the group type is switched to anything but group
	 */
	public static void formulateConfig(String action) {

		if(action.equalsIgnoreCase("form")) {
			
			ArrayList<ArrayList<String>> groups = formGroups();
			
			List<String> groupList = new ArrayList();
			groupList.add("Default1");
			groupList.add("Default2");
			groupList.add("Default3");
			
			config.set("GroupList", groupList);
			
			for(int x = 0;x < groups.size();x++) {
				config.set("Groups." + groupList.get(x), groups.get(x));
			}
			
		}else {
			
			List<String> groupList = config.getStringList("GroupList");
			
			config.set("GroupList", null);
			
			for(int x = 0; x < groupList.size(); x++) {
				config.set("Groups." + groupList.get(x), null);
				config.set("Groups", null);
			}
			
		}

		plugin.saveConfig();
		
	}
	
	public static void editName(String groupName, String newName) {
		
		if(isTypeGroup() == false) return;
		
		if(isGroup(groupName) == false) return;
		
		List<String> groupList = config.getStringList("GroupList");
		groupList.remove(groupName);	
		groupList.add(newName);
		config.set("GroupList", groupList);
		
		//Gets the old group contents and pastes them to the new group
		List<String> prevGroup = config.getStringList("Groups." + groupName);
		config.set("Groups." + groupName, null);
		
		config.set("Groups." + newName, prevGroup);
		
		Chat.sendPlayerMessage(p, "The group &5&l" + groupName + " has been renamed to &5&l" + newName + "!");
		
		plugin.saveConfig();
		
	}
	
	/*
	 * Checks to see if the MineType is type "Group"
	 */
	public static boolean isTypeGroup() {
		
		String mineType = config.getString("MineType");
		
		if(!mineType.equalsIgnoreCase("group")) {
			Chat.sendPlayerMessage(p, "&4You can only use this command when MineType is type &5&lGROUP!&4 You can change this by doing &6&l/bbr minetype (type)");
			return false;
		}else {
			return true;
		}
		
	}
	
	/*
	 * Returns the group that the tier is in
	 */
	public static String tierGroup(String tierName) {
		
		List<String> allGroups = config.getStringList("GroupList");
		String group = null;
		
		search:{
			for(int x = 0; x < allGroups.size(); x++) {
				List<String> groupList = config.getStringList("Groups." + allGroups.get(x));
				for(String tier : groupList) {
					if(tier.equalsIgnoreCase(tierName)) {
						group = allGroups.get(x);
						break search;
					}
				}
			}
		}
		
		return group;
	}
	
	/*
	 * Checks to see if the Group is a valid group
	 */
	public static boolean isGroup(String groupName) {
		
		List<String> groups = config.getStringList("GroupList");
		
		for(String g : groups) {
			if(g.equalsIgnoreCase(groupName)) {
				return true;
			}
		}
		
		Chat.sendPlayerMessage(p, "Group &5&l" + groupName.toUpperCase() + " is not a valid group!");
		return false;
	}
	
	/*
	 * Allows the user to create a group
	 * with a name of their choice.
	 */
	public static void createGroup(String groupName, Player p) {
		
		if(isTypeGroup() == false) return;
		
		List<String> groupList = config.getStringList("GroupList");
		
		for(String group : groupList) {
			if(group.equalsIgnoreCase(groupName)) {
				Chat.sendPlayerMessage(p, "There is already a group with this name!");
				return;
			}
		}
		
		groupList.add(groupName);
		config.set("GroupList", groupList);
		
		List<String> newList = new ArrayList();
		
		config.set("Groups." + groupName, newList);
		
		Chat.sendPlayerMessage(p, "&bGroup &5&l" + groupName.toUpperCase() + "&b has been created!");
		plugin.saveConfig();
		
	}
	
	/*
	 * Allows the user to remove a group
	 */
	public void removeGroup(String groupName, Player p) {
		
		if(isTypeGroup() == false) return;
		
		List<String> groupList = config.getStringList("GroupList");
		
		if(!groupList.contains(groupName)) {
			Chat.sendPlayerMessage(p, "There is no group named &5&l" + groupName.toUpperCase());
			return;
		}
		
		groupList.remove(groupName);
		config.set("GroupList", groupList);
		config.set("Groups." + groupName, null);
		
		Chat.sendPlayerMessage(p, "&bGroup &5&l" + groupName.toUpperCase() + "&b has been removed!");
		plugin.saveConfig();
		
	}
	
}
