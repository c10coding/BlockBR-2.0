package me.caleb.BlockBR.utils;

import java.util.List;

import org.bukkit.enchantments.Enchantment;

import me.caleb.BlockBR.Main;

public class ItemParser {

	private Main plugin;
	String path = null;
	List<String> itemList = null;
	String tier;
	
	public ItemParser(Main plugin, String tierName) {
		this.plugin = plugin;
		this.tier = tierName;
		path = "Tiers." + tierName.toLowerCase() + ".Properties.Rewards.Items";
		itemList = (List<String>) plugin.getConfig().getList(path);
	}
	
	public String getItemName(int index) {
		return null;
	}
	
	public Enchantment getItemEnchant() {
		return null;
	}
	
	public int getAmount() {
		return 0;
	}
	
}
