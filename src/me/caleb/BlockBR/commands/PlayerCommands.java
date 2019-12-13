package me.caleb.BlockBR.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.caleb.BlockBR.Main;

public class PlayerCommands implements CommandExecutor{

	private Main plugin;
	
	public PlayerCommands(Main plugin) {
		this.plugin = plugin;
		//plugin.getCommand("blockbr").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		
		return false;
	}

}
