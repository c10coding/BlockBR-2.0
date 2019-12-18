package me.caleb.BlockBR.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.Rewards;
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
		Tier t = new Tier(plugin);
		Rewards r = new Rewards(plugin);
		
		try {
			
			if(!(sender instanceof Player)) {
				Chat.sendConsoleMessage("You must be a player!");
			}else {
				//Args[1] should be the tierName
				if(args[0].equalsIgnoreCase("tierlist")) {
					if(player.hasPermission("blockbr.use")) {
						t.showTierList(player);
					}
				}			
				
				//0 is tier, 1 is add or remove, 2 is tiername
				if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("add") && !args[2].isEmpty()) {
					if(player.hasPermission("blockbr.admin")) {
						t.tierAdd(args[2], player);
						t.configWork(args[2], args[1]);
					}
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("remove") && !args[2].isEmpty()) {
					if(player.hasPermission("blockbr.admin")) {
						t.tierRemove(args[2], player);
						t.configWork(args[2], args[1]);	
					}		
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("edit") && !args[2].isEmpty() && !args[3].isEmpty() && !args[4].isEmpty()) {
					if(player.hasPermission("blockbr.admin")) {
						t.tierEdit(args[2], args[3], args[4], player);
						t.configWork(args[2], args[1]);
					}
				//Crates
				//bbr rewardadd (tier) ("Crate") (CrateName)
				}else if(args[0].equalsIgnoreCase("rewardadd") && !args[1].isEmpty() && args[2].equalsIgnoreCase("crate") && !args[3].isEmpty()) {
					if(player.hasPermission("blockbr.admin")) {
						r.rewardAddCrate(player, args[1], args[2], args[3]);
					}
				//Items
				//bbr rewardadd (tier) ("Item")
				}else if(args[0].equalsIgnoreCase("rewardadd") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Item")) {
					if(player.hasPermission("blockbr.admin")) {
						r.rewardAddItem(player, args[1]);
					}
				}else if(args[0].equalsIgnoreCase("rewardadd") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Money") && !args[3].isEmpty()) {
					if(player.hasPermission("blockbr.admin")) {
						r.rewardAddMoney(player, args[1], args[2],Integer.parseInt(args[3]));
					}
				//bbr tier rename (tier) (newName)
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("rename") && !args[2].isEmpty() && !args[3].isEmpty()) {
					if(player.hasPermission("blockbr.admin")) {
						t.tierRename(args[2], args[3],player);
					}
				}else if(args[0].equalsIgnoreCase("rewardadd") && !args[1].isEmpty() && args[2].equalsIgnoreCase("command")) {
					if(player.hasPermission("blockbr.admin")) {
						r.rewardAddCommand(player, args[1]);
					}
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("info") && !args[2].isEmpty()) {
					if(player.hasPermission("blockbr.use")) {
						t.getTierInfo(player, args[2]);
					}
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
