package me.caleb.BlockBR.admin.mineType;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import me.caleb.BlockBR.Main;

public class Group {

	private Main plugin;
	private FileConfiguration config;
	
	public Group(Main plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
	}
	
	/*
	 * Method that returns a 2d array
	 * of the groups
	 */
	public ArrayList<ArrayList<String>> formGroups() {
		
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
	
	public void formulateConfig(String action) {

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
}
