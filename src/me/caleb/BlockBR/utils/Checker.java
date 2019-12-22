package me.caleb.BlockBR.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;

public class Checker {

	private Main plugin;
	private FileConfiguration config;
	String mineType;
	
	public Checker(Main plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
		mineType = config.getString("MineType");
	}
	
}
