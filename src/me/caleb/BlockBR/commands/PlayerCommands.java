package me.caleb.BlockBR.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import cratereloaded.p;
import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.Rewards;
import me.caleb.BlockBR.admin.Tier;
import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.menus.InfoMenu;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Chat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerCommands implements CommandExecutor{

	private Main plugin;
	Player player;
	
	public PlayerCommands(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("blockbr").setExecutor(this);
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
				
				if(args[0].equalsIgnoreCase("tierlist") && args.length == 1) {
					if(isPlayer() == true) {
						t.showTierList(player);
					}	
				//0 is tier, 1 is add or remove, 2 is tiername
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("add") && !args[2].isEmpty()) {
					if(isAdmin() == true) {
						t.tierAdd(args[2], player);
					}
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("remove") && !args[2].isEmpty()) {
					if(isAdmin() == true) {
						if(args[2].equalsIgnoreCase("all")) {
							t.tierRemove(player);
						}else {
							t.tierRemove(args[2], player);
						}	
					}		
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("edit") && !args[2].isEmpty() && !args[3].isEmpty() && !args[4].isEmpty()) {
					if(isAdmin() == true) {
						t.tierEdit(args[2], args[3], args[4], player);
						t.configWork(args[2], args[1]);
					}
				//Crates
				//bbr rewardadd (tier) ("Crate") (CrateName)
				}else if(args[0].equalsIgnoreCase("rewardedit") && !args[1].isEmpty() && args[2].equalsIgnoreCase("crate") && !args[3].isEmpty()) {
					if(isAdmin() == true) {
						r.rewardAddCrate(player, args[1], args[2], args[3]);
					}
				//Items
				//bbr rewardadd (tier) ("Item")
				}else if(args[0].equalsIgnoreCase("rewardadd") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Item")) {
					if(isAdmin() == true) {
						r.rewardAddItem(player, args[1]);
					}
				}else if(args[0].equalsIgnoreCase("rewardedit") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Money") && !args[3].isEmpty()) {
					if(isAdmin() == true) {
						try {
							r.rewardAddMoney(player, args[1], args[2],Integer.parseInt(args[3]));
						}catch(NumberFormatException e) {
							r.rewardAddMoney(player, args[1], args[2], args[3]);
						}
					}
				//bbr tier rename (tier) (newName)
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("rename") && !args[2].isEmpty() && !args[3].isEmpty()) {
					if(isAdmin() == true) {
						t.tierRename(args[2], args[3],player);
					}
				}else if(args[0].equalsIgnoreCase("rewardadd") && !args[1].isEmpty() && args[2].equalsIgnoreCase("command")) {
					if(isAdmin() == true) {
						r.rewardAddCommand(player, args[1]);
					}
				//bbr tier info
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("info") && !args[2].isEmpty()) {
					if(isAdmin() == true) {
						t.getTierInfo(player, args[2]);
					}
				//bbr rewardremove (Tier) "Item" (##)
				}else if(args[0].equalsIgnoreCase("rewardremove") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Item") && !args[3].isEmpty()) {
					if(isAdmin() == true) {
						r.rewardRemoveItem(args[1],args[3],player);
					}
				//bbr rewardremove (Tier) "Command" (##)
				}else if(args[0].equalsIgnoreCase("rewardremove") && !args[1].isEmpty() && args[2].equalsIgnoreCase("Command") && !args[3].isEmpty()) {
					if(isAdmin() == true) {
						r.rewardRemoveCommand(args[1], args[3], player);
					}
				//bbr changeType (Type)
				}else if(args[0].equalsIgnoreCase("changeType") && !args[1].isEmpty()) {
					if(isAdmin() == true) {
						Tier tier = new Tier(plugin, player);
						if(tier.isTierListEmpty()) {
							Chat.sendPlayerMessage(player, "You cannot do this right now! The tier list is empty! Try doing &6/bbr tier add (Tier Name) before using this command!");
							return false;
						}else {
							r.changeMineType(args[1], player);
						}
						
					}else {
						return false;
					}
				//bbr group create (Group Name)
				}else if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("create") && args.length == 3) {
					if(isAdmin() == true) {
						g.createGroup(args[2], player);
					}
				//bbr group remove (Group Name)
				}else if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("remove") && args.length == 3) {
					if(isAdmin() == true) {
						g.removeGroup(args[2], player);
					}
				//bbr tier move (Group Name)
				}else if(args[0].equalsIgnoreCase("tier") && args[1].equalsIgnoreCase("move") && args.length == 4) {
					if(isAdmin() == true) {
						player.sendMessage("here");
						t.tierMove(args[2], args[3]);
					}
				}else if(args[0].equalsIgnoreCase("groupList") && args.length == 1) {
					if(isAdmin() == true) {
						g.showGroupList();
					}
				}else if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("tierList") && args.length == 3) {
					if(isAdmin() == true) {
						g.groupTiers(args[2]);
					}
				}else if(args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("rename") && args.length == 4) {
					if(isAdmin() == true) {
						g.editName(args[2], args[3]);
					}
				}else if(args[0].equalsIgnoreCase("help") && args.length == 1) {
					if(isPlayer() == true) {
						sendHelp();
					}
				}else if(args[0].equalsIgnoreCase("help2") && args.length == 1) {
					sendHelp2();
				}else if(args[0].equalsIgnoreCase("help3") && args.length == 1) {
					sendHelp3();
				}else if(args[0].equalsIgnoreCase("menu") && args.length == 1) {
					if(isPlayer() == true){
						DataManager dm = new DataManager(plugin,player);
						InfoMenu im = new InfoMenu(plugin); 
						if(!dm.ifInDB(player.getName())) {
							dm.addPlayer(player);
						}
						im.initializeItems(player);
						im.openInventory(player);
					}
					
				}else {
					sendHelp();
				}
			}
			
		}catch(ArrayIndexOutOfBoundsException e) {
			sendHelp();
		}
		
		return false;
	}
	
	private boolean isAdmin() {
		
		if(player.hasPermission("blockbr.admin")) {
			return true;
		}else {
			Chat.sendPlayerMessage(player, "You do not have permission to use this command!");
			return false;
		}
		
	}
	
	private boolean isPlayer() {
		
		if(player.hasPermission("blockbr.use")) {
			return true;
		}else {
			Chat.sendPlayerMessage(player, "You do not have permission to use this command!");
			return false;
		}
		
	}
	
	private void sendHelp() {
		FileConfiguration config = plugin.getConfig();
		Chat.sendPlayerMessage(player, "&5&lList of commands:");
		
		List<String> helpList = config.getStringList("Help");
		
		for(String helpLine : helpList) {
			player.sendMessage(Chat.chat("&b> " + helpLine));
		}
		
		TextComponent message = new TextComponent("Page 1");
		TextComponent arrows = new TextComponent(" >>>");
		arrows.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		arrows.setBold(true);
		message.setColor(net.md_5.bungee.api.ChatColor.AQUA);
		arrows.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bbr help2"));
		message.addExtra(arrows);
		arrows.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Go to the next page of commands" ).create() ) );

		player.spigot().sendMessage(message);
		
	}
	
	private void sendHelp2() {
		
		FileConfiguration config = plugin.getConfig();
		Chat.sendPlayerMessage(player, "&5&lPage 2:");
		List<String> helpList = config.getStringList("Help2");
		
		for(String helpLine : helpList) {
			player.sendMessage(Chat.chat("&b> " + helpLine));
		}
		
		TextComponent arrows = new TextComponent("<<< ");
		TextComponent message = new TextComponent("Page 2");
		TextComponent arrows2 = new TextComponent(" >>>");
		arrows2.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		arrows2.setBold(true);
		arrows.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		arrows.setBold(true);
		message.setColor(net.md_5.bungee.api.ChatColor.AQUA);
		arrows.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bbr help"));
		arrows2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bbr help3"));
		arrows.addExtra(message);
		arrows.addExtra(arrows2);
		arrows.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Go to the first page of commands" ).create() ) );
		arrows2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Go to the next page of commands" ).create() ) );
		
		player.spigot().sendMessage(arrows);
		
	}
	
	private void sendHelp3() {
		
		FileConfiguration config = plugin.getConfig();
		Chat.sendPlayerMessage(player, "&5&lPage 3:");
		List<String> helpList = config.getStringList("Help3");
		
		for(String helpLine : helpList) {
			player.sendMessage(Chat.chat("&b> " + helpLine));
		}
		
		TextComponent arrows = new TextComponent("<<< ");
		TextComponent message = new TextComponent("Page 3");
		arrows.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		arrows.setBold(true);
		message.setColor(net.md_5.bungee.api.ChatColor.AQUA);
		arrows.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bbr help2"));
		arrows.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Go to the second page of commands" ).create() ) );
		arrows.addExtra(message);
		player.spigot().sendMessage(arrows);
		
		
		
	}

}
