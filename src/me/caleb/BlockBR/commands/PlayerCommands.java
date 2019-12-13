package me.caleb.BlockBR.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.Tier;
import me.caleb.BlockBR.utils.Chat;

public class PlayerCommands implements CommandExecutor{

	private Main plugin;
	Player player;
	
	public PlayerCommands(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("blockbr").setExecutor(this);
		plugin.getCommand("blockbra").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		player = (Player) sender;
		try {
			
			if(!(sender instanceof Player)) {
				Chat.sendConsoleMessage("You must be a player!");
			}else {
				//Args[1] should be the tierName
				if(args[0].equalsIgnoreCase("add") && !args[1].isEmpty()) {
					Tier t = new Tier(plugin);
					t.tierAdd(args[1]);
				}
			}
			
		}catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void sendHelp() {
		
	}

}
