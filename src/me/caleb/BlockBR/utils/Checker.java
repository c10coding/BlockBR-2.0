package me.caleb.BlockBR.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.listeners.ScoreboardManager;
import me.caleb.BlockBR.rewards.RewardHandler;
import me.caleb.BlockBR.sql.DataManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Checker {

	private Main plugin;
	private FileConfiguration config;
	String mineType;
	Player p;
	ArrayList<Material> matList = new ArrayList<Material>();
	String currentTier, groupTierAffected;
	ArrayList<String> undoneTiers;
	Block b;
	
	public Checker(Main plugin, Block b, Player p) {
		this.plugin = plugin;
		this.b = b;
		this.p = p;
		config = plugin.getConfig();
		mineType = config.getString("MineType");
		DataManager dm = new DataManager(plugin,p);
		currentTier = dm.getTier();
	}
	
	public Checker(Main plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
		DataManager dm = new DataManager(plugin,p);
		currentTier = dm.getTier();
	}
	
	public Checker(Main plugin, Player p) {
		this.plugin = plugin;
		this.p = p;
		config = plugin.getConfig();
		DataManager dm = new DataManager(plugin,p);
		currentTier = dm.getTier();
	}

	public String tierAffected() {
		
		Material blockMat = b.getType();
		
		if(mineType.equalsIgnoreCase("all")) {
			
			List<String> tierList = config.getStringList("TierList");
			
			for(String tier : tierList) {
				
				String matString = config.getString("Tiers." + tier + ".Properties.Material");
				Material mat = Material.getMaterial(matString);
				
				if(mat.equals(Material.GRASS_BLOCK) || mat.equals(Material.DIRT)) {
					if(blockMat.equals(Material.GRASS_BLOCK) || blockMat.equals(Material.DIRT)) {
						return tier;
					}
				}else {
					if(blockMat.equals(mat)) {
						return tier;
					}
				}
				
			}
			
			return "none";
			
		}else if(mineType.equalsIgnoreCase("group")){
			
			DataManager dm = new DataManager(plugin, p);
			String group = dm.getGroup();
			ArrayList<Material> groupMats = new ArrayList<Material>();
			ArrayList<String> undoneTiers = dm.getUndoneTiersInGroup();
			
			for(String tier : undoneTiers) {
				
				String matString  = config.getString("Tiers." + tier + ".Properties.Material");
				Material mat = Material.getMaterial(matString);
				
				if(mat.equals(Material.GRASS_BLOCK) || mat.equals(Material.DIRT)) {
					if(blockMat.equals(Material.GRASS_BLOCK) || blockMat.equals(Material.DIRT)) {
						return tier;
					}
				
				}else{
					if(mat.equals(blockMat)) {
						return tier;
					}
				}
			}
			
			return "none";
		
		}else if(mineType.equalsIgnoreCase("onebyone")) {
			
			DataManager dm = new DataManager(plugin,p);
			String tier = dm.getTier();
			String matString = config.getString("Tiers." + tier + ".Properties.Material");
			Material mat = Material.getMaterial(matString);
			
			if(mat.equals(Material.GRASS_BLOCK) || mat.equals(Material.DIRT)) {
				if(blockMat.equals(Material.GRASS_BLOCK) || blockMat.equals(Material.DIRT)) {
					return tier;
				}
			}else {
				if(blockMat.equals(mat)) {
					return tier;
				}
			}
			
		}else {
			return "none";
		}
		
			return "none";
		
	}
	
	public boolean atThreshold(String tier) {
		
		DataManager dm = new DataManager(plugin,p);
		int amount = dm.getTierAmount(tier);
		int threshold = getThreshold(tier);
		amount++;
		
		if(threshold == amount) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public int getThreshold(String tier) {
		
		DataManager dm = new DataManager(plugin, p);
		int level = dm.getLevel();
		double mult = config.getDouble("Tiers." + tier + ".Properties.Multiplier");
		int initThreshold = config.getInt("Tiers." + tier + ".Properties.Threshold");
		int threshold = 0;
		//If they are on a level greater than 1, then get the appropriate threshold based on their level
		if(level > 1) {
			threshold = (int) ((int) initThreshold * (mult * (level-1)));
		}else {
			return initThreshold;
		}
		
		return threshold;
		
	}
	
	/*
	 * Increasing the tier amount or it increases your group or tier
	 */
	public void increaseAmount(boolean atThreshold,String tier) {
		
		DataManager dm = new DataManager(plugin, p);
		
		if(atThreshold) {
			
			List<String> tierList = config.getStringList("TierList");
			RewardHandler r = new RewardHandler(plugin,tier,p);
			r.giveRewards();
			Fireworks f = new Fireworks(plugin);
			f.spawnFireWorks(p);
			
			if(mineType.equalsIgnoreCase("group")) {
				List<String> undoneTiers = dm.getUndoneTiersInGroup();
				List<String> groups = config.getStringList("GroupList");
				String group = dm.getGroup();
				
				//If this is the last group
				//Level up
				if(groups.get(groups.size()-1).equalsIgnoreCase(group)) {
					if(undoneTiers.size() == 1 && undoneTiers.contains(tier)) {
						
						dm.levelUp(tier);
						dm.resetTiers();
						
						int level = dm.getLevel();
						
						Chat.sendPlayerMessage(p, "&5&lCongratulations! &bYou have gone up to level " + level);
						Chat.sendPlayerMessage(p, "Enjoy your new set of rewards for the next tiers! You are now back on tier &5&l" + tierList.get(0).toUpperCase());
						Chat.sendPlayerMessage(p, "&rWant to see the rewards for the other tiers? Do &6/bbr menu");
						
						return;
					}
				}
				
				//You aren't on the last group, but you are on the last tier of whatever group you are on
				if(undoneTiers.size() == 1 && undoneTiers.contains(tier)) {
					Chat.sendPlayerMessage(p, "&5&lCongratulations! &bYou have gone up to the next group! You have also completed tier &5&l" + tier.toUpperCase());
					dm.setToDone(tier);
					return;
				}else {
					Chat.sendPlayerMessage(p, "&5&lCongratulations! &bYou have completed the tier &5&l" + tier.toUpperCase() + "&b. You are one step closer to going to the next group!");
					dm.setToDone(tier);
					return;
				}
				
				
				
			}else if(mineType.equalsIgnoreCase("all")) {
				
				ArrayList<String> undoneTiers = dm.getAllUndoneTiers();
				
				//If this is the last tier
				if(undoneTiers.size() == 1 && undoneTiers.contains(tier)) {
					
					dm.levelUp(tier);
					dm.resetTiers();
					int level = dm.getLevel();
					
					Chat.sendPlayerMessage(p, "&5&lCongratulations! &bYou have gone up to level " + level);
					Chat.sendPlayerMessage(p, "Enjoy your new set of rewards for the next tiers! You are now back on tier &5&l" + tierList.get(0).toUpperCase());
					return;
					
				//If this is not the last tier
				}else {
					
					Chat.sendPlayerMessage(p, "&5&lCongratulations! &bYou have completed the tier &5&l" + tier.toUpperCase() + "&b. You are one step closer to leveling up!");
					dm.setToDone(tier);
					return;
					
				}
				
			}else if(mineType.equalsIgnoreCase("onebyone")) {
				
				ArrayList<String> undoneTiers = dm.getAllUndoneTiers();
				
				//This is the last tier
				if(undoneTiers.size() == 1 && undoneTiers.contains(tier)) {
					
					dm.levelUp(tier);
					dm.resetTiers();
					int level = dm.getLevel();
					
					Chat.sendPlayerMessage(p, "&5&lCongratulations! &bYou have gone up to level " + level);
					Chat.sendPlayerMessage(p, "Enjoy your new set of rewards for the next tiers! You are now back on tier &5&l" + tierList.get(0).toUpperCase());
					return;
				//This is not the last tier
				}else {
					
					String nextTier = tierList.get(tierList.indexOf(tier)+1);
					
					Chat.sendPlayerMessage(p, "&5&lCongratulations! &bYou have completed the tier &5&l" + tier.toUpperCase() + "&b. You are now on tier &5&l" + nextTier.toUpperCase());
					
					dm.setToDone(tier);
					dm.nextTier(tier);
					return;
					
				}
				
			}			
			
		}else {
			
			int amount = dm.getTierAmount(tier);
			/*
			 * If the amount is -1, then stop all execution of the plugin
			 * You can't increment a value of "done!"
			 */
			if(amount == -1) {
				return;
			}
			int level = dm.getLevel();
			int threshold = getThreshold(tier);
			amount++;
			dm.increaseAmount(tier, amount);
			
			if(mineType.equalsIgnoreCase("group")) {
				String group = dm.getGroup();
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&lLevel: &l" + level + " &r&l| &0&lTier: &8&l[&5&l" + tier.toUpperCase() + "&8&l]" + " &r&l| &0&lGroup: &8&l[&5&l" + group.toUpperCase() + "&8&l] " + "&b&l" + amount + "&b&l/" + threshold)));
			}else{
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&lLevel: &l" + level + " &r&l| &0&lTier: &8&l[&5&l" + tier.toUpperCase() + "&8&l] " + "&b&l" + amount + "&b&l/" + threshold)));
			}
			
			if(mineType.equalsIgnoreCase("onebyone")) {
				ScoreboardManager.setScoreboard(p);
			}
			
			return;
		}
		
	}
	

	
}
