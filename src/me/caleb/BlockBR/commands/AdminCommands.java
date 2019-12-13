package me.caleb.BlockBR.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor{

	Player player;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		player = (Player) sender;
		
		if(!(sender instanceof Player)) {
			
		}
		
		return false;
	}

}
