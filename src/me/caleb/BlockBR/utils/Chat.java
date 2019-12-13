package me.caleb.BlockBR.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static String blockBrChat(String s) {
		return chat("&8&l[&6&lBlockBR&r&8&l]&r " + s);
	}
	
	public static void sendPlayerMessage(Player p, String s) {
		p.sendMessage(blockBrChat(s));
	}
	
	public static void sendConsoleMessage(String s) {
		Bukkit.getConsoleSender().sendMessage(blockBrChat(s));
	}
	
}

