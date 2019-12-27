package me.caleb.BlockBR.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.Rewards;
import me.caleb.BlockBR.admin.Tier;
import me.caleb.BlockBR.admin.mineType.Group;
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
		Tier t = new Tier(plugin,player);
		Rewards r = new Rewards(plugin);
		Group g = new Group(plugin,player);
		
		/*
		 * In the future, use 2 different command executors
		 * for admin and for player commands
		 */
		
		try {
			
			if(!(sender instanceof Player)) {
				Chat.sendConsoleMessage("You must be a player!");
			}else {
				
				//Args[1] should be the tierName
				if(args[0].equalsIgnoreCase("tierlist")) {
					if(isPlayer(player) == true) {
						t.showTierList(player);
					}
				}	
				
				//0 is tier, 1 is add or remove, 2 is tiername
				if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("add") && !args[2].isEmpty()) {
					if(isAdmin(player) == true) {
						t.tierAdd(args[2], player);
					}
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("remove") && !args[2].isEmpty()) {
					if(isAdmin(player) == true) {
						if(args[2].equalsIgnoreCase("all")) {
							t.tierRemove(player);
						}else {
							t.tierRemove(args[2], player);
						}
						
					}		
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("edit") && !args[2].isEmpty() && !args[3].isEmpty() && !args[4].isEmpty()) {
					if(isAdmin(player) == true) {
						t.tierEdit(args[2], args[3], args[4], player);
						t.configWork(args[2], args[1]);
					}
				//Crates
				//bbr rewardadd (tier) ("Crate") (CrateName)
				}else if(args[0].equalsIgnoreCase("rewardedit") && !args[1].isEmpty() && args[2].equalsIgnoreCase("crate") && !args[3].isEmpty()) {
					if(isAdmin(player) == true) {
						r.rewardAddCrate(player, args[1], args[2], args[3]);
					}
				//Items
				//bbr rewardadd (tier) ("Item")
				}else if(args[0].equalsIgnoreCase("rewardadd") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Item")) {
					if(isAdmin(player) == true) {
						r.rewardAddItem(player, args[1]);
					}
				}else if(args[0].equalsIgnoreCase("rewardedit") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Money") && !args[3].isEmpty()) {
					if(isAdmin(player) == true) {
						try {
							r.rewardAddMoney(player, args[1], args[2],Integer.parseInt(args[3]));
						}catch(NumberFormatException e) {
							r.rewardAddMoney(player, args[1], args[2], args[3]);
						}
					}
				//bbr tier rename (tier) (newName)
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("rename") && !args[2].isEmpty() && !args[3].isEmpty()) {
					if(isAdmin(player) == true) {
						t.tierRename(args[2], args[3],player);
					}
				}else if(args[0].equalsIgnoreCase("rewardadd") && !args[1].isEmpty() && args[2].equalsIgnoreCase("command")) {
					if(isAdmin(player) == true) {
						r.rewardAddCommand(player, args[1]);
					}
				//bbr tier info
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("info") && !args[2].isEmpty()) {
					if(isPlayer(player) == true) {
						t.getTierInfo(player, args[2]);
					}
				//bbr rewardremove (Tier) "Item" (##)
				}else if(args[0].equalsIgnoreCase("rewardremove") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Item") && !args[3].isEmpty()) {
					if(isAdmin(player) == true) {
						r.rewardRemoveItem(args[1],args[3],player);
					}
				//bbr rewardremove (Tier) "Command" (##)
				}else if(args[0].equalsIgnoreCase("rewardremove") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Command") && !args[3].isEmpty()) {
					if(isAdmin(player) == true) {
						r.rewardRemoveCommand(args[1], args[3], player);
					}
				//bbr changeType (Type)
				}else if(args[0].equalsIgnoreCase("changeType") && !args[1].isEmpty()) {
					if(isAdmin(player) == true) {
						r.changeMineType(args[1], player);
					}
				//bbr group create (Group Name)
				}else if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("create") && args.length == 3) {
					if(isAdmin(player) == true) {
						g.createGroup(args[2], player);
					}
				//bbr group remove (Group Name)
				}else if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("remove") && args.length == 3) {
					if(isAdmin(player) == true) {
						g.removeGroup(args[2], player);
					}
				//bbr tier move (Group Name)
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("move") && args.length == 4) {
					if(isAdmin(player) == true) {
						player.sendMessage("here");
						t.tierMove(args[2], args[3]);
					}
				}else if(args[0].equalsIgnoreCase("groupList") && args.length == 1) {
					if(isAdmin(player) == true) {
						g.showGroupList();
					}
				}else if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("tiers") && args.length == 3) {
					if(isAdmin(player) == true) {
						g.groupTiers(args[2]);
					}
				}else if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("rename") && args.length == 4) {
					if(isAdmin(player) == true) {
						g.editName(args[2], args[3]);
					}
				}	

			}
			
		}catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean isAdmin(Player p) {
		
		if(p.hasPermission("blockbr.admin")) {
			return true;
		}else {
			Chat.sendPlayerMessage(p, "You do not have permission to use this command!");
			return false;
		}
		
	}
	
	public boolean isPlayer(Player p) {
		
		if(p.hasPermission("blockbr.use")) {
			return true;
		}else {
			Chat.sendPlayerMessage(p, "You do not have permission to use this command!");
			return false;
		}
		
	}
	
	public void sendHelp() {
		
	}

}
