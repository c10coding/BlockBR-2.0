package me.caleb.BlockBR.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.sql.DataManager;

public class Checker {

	private Main plugin;
	private FileConfiguration config;
	String mineType;
	Player p;
	ArrayList<Material> matList = new ArrayList<Material>();
	String currentTier;
	Block b;
	
	public Checker(Main plugin, Block b, Player p) {
		this.plugin = plugin;
		this.b = b;
		this.p = p;
		config = plugin.getConfig();
		mineType = config.getString("MineType");
		DataManager dm = new DataManager(plugin,p);
		currentTier = dm.getTier(p);
	}
	
	public void formMaterialList() {
		
		List<String> tierList = config.getStringList("TierList");
		
		for(String tierName : tierList) {
			String materialString = config.getString("Tiers." + tierName + ".Properties.Material");
			Material material = Material.getMaterial(materialString);
			matList.add(material);
		}
		
	}
	
	public boolean aValidBlock() {
		
		Material blockMat = b.getType();
		
		if(mineType.equalsIgnoreCase("all")) {
			
			for(Material mat : matList) {
				
				if(mat.equals(blockMat)) {
					return true;
				}
				
			}
			
		}else if(mineType.equalsIgnoreCase("group")){
			
			Group g = new Group(plugin, p);
			String group = null;
			
			/*
			 * This shouldn't happen unless the user deletes the default groups and does not make new ones
			 */
			try {
				group = g.tierGroup(currentTier);
			}catch(NullPointerException e) {
				Bukkit.getConsoleSender().sendMessage("A player has mined a block, but there are no groups! Please create more groups for the plugin to properly function by doing /bbr group create (Group name)");
				return false;
			}
			
			List<String> tiersInGroup = config.getStringList("Groups." + group);
			
		
		}
		
		
		return false;
	}
	
}
