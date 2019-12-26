package me.caleb.BlockBR.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Chat;
import me.caleb.BlockBR.utils.Checker;

public class BlockEvent implements Listener{

	private Main plugin;
	
	public BlockEvent(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}
	
	@EventHandler
	private void onBlockBreak(BlockBreakEvent e) {
		
		Block block = e.getBlock();
		Player player = e.getPlayer();
		String playerName = player.getName();
		DataManager dm = new DataManager(plugin, player);
		/*
		 * If the breaking of the block is actually canceled i.e. a region plugin is preventing you from breaking the block
		 * If the block has meta data of being placed
		 * If the block has meta data of it being formed (The forming of cobblestone or obsidian)
		 */
		if(e.isCancelled() || block.hasMetadata("PLACED") || block.hasMetadata("FORMED")) {
			return;
		}else {
			//If block broken is not canceled aka broken
			boolean inDB = dm.ifInDB(playerName);

			if(inDB == true) {
				Checker c = new Checker(plugin, block, player);
				String tier = c.tierAffected();
				if(!tier.equalsIgnoreCase("none")) {
					
					boolean atThreshold = c.atThreshold(tier);
					c.increaseAmount(atThreshold,tier);
					
				}else {
					return;
				}
			}else {
				dm.addPlayer(player);
				return;
			}
		}
		
	}
	
	@EventHandler
	private void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		// Makes a new metadata value called placed 
		block.setMetadata("PLACED", new FixedMetadataValue(plugin,true));
	}
	
	@EventHandler
	private void onBlockForm(BlockFormEvent event) {
		Block block = event.getBlock();
		block.setMetadata("FORMED", new FixedMetadataValue(plugin,true));
	}
	
}
