package me.caleb.BlockBR.admin;

import org.bukkit.Bukkit;

import me.caleb.BlockBR.Main;

public class Tier {

	private Main plugin;
	private String rewardType = null;
	
	public Tier(Main plugin) {
		this.plugin = plugin;
	}
	
	public void tierAdd(String tierName) {
		
	}
	
	public void tierRemove() {
		
	}
	
	public void tierEdit() {
		
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
