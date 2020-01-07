package me.caleb.BlockBR.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Chat;
import net.md_5.bungee.api.ChatColor;

public class ScoreboardManager implements Listener{

	private static Main plugin;
	
	public ScoreboardManager(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		boolean wantScoreboard = plugin.getConfig().getBoolean("Side-Menu");
		String mineType = plugin.getConfig().getString("MineType");
		
		if(wantScoreboard && mineType.equalsIgnoreCase("onebyone")) {
			setScoreboard(event.getPlayer());	
		}else {
			removeScoreboard();
			return;
		}
		
	}
	//Updating for one player purposes
	public static void setScoreboard(Player p) {
		DataManager dm = new DataManager(plugin,p);
		
		Scoreboard b = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective o = b.registerNewObjective("Tier", "");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(Chat.chat("&8&l[&6&lBlockBR&r&8&l]&r"));
		
		Score tier = o.getScore(ChatColor.WHITE + "Tier: " + ChatColor.GOLD + dm.getTier().toUpperCase());
		Score amount = o.getScore(ChatColor.WHITE + "Amount: " + ChatColor.GOLD + dm.getTierAmount(dm.getTier()));
		tier.setScore(2);
		amount.setScore(1);
		p.setScoreboard(b);
	}
	
	//MineType has changed
	public static void setScoreboard() {
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			DataManager dm = new DataManager(plugin,p);
			
			Scoreboard b = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
			Objective o = b.registerNewObjective("Tier", "");
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
			o.setDisplayName(Chat.chat("&8&l[&6&lBlockBR&r&8&l]&r"));
			
			Score tier = o.getScore(ChatColor.WHITE + "Tier: " + ChatColor.GOLD + dm.getTier().toUpperCase());
			Score amount = o.getScore(ChatColor.WHITE + "Amount: " + ChatColor.GOLD + dm.getTierAmount(dm.getTier()));
			tier.setScore(2);
			amount.setScore(1);
			
			p.setScoreboard(b);
		}
		
	}
	public static void removeScoreboard() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
}
