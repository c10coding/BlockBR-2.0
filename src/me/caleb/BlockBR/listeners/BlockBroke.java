package me.caleb.BlockBR.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.sql.GetData;
import me.caleb.BlockBR.utils.Chat;

public class BlockBroke implements Listener{

	private Main plugin;
	
	public BlockBroke(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		
		Block block = e.getBlock();
		Player player = e.getPlayer();
		String playerName = player.getName();
		GetData gd = new GetData(plugin);
		
		//Chat.sendPlayerMessage(player, playerName);
		
		//The region plugins cancel the block breaking. If it's cancelled, e.isCancelled is true
		if(e.isCancelled()) {
			//If block broken is canceled
			return;
		}else {
			//If block broken is not canceled aka broken
			boolean inDB = gd.ifInDB(playerName);
			
			if(inDB == true) {
				
			}else {
				
			}
		}
		
	}
	
}
