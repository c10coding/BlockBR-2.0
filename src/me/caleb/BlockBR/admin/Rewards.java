package me.caleb.BlockBR.admin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.utils.Chat;

public class Rewards {
		
		private Main plugin;
		private FileConfiguration config = null;
		
		public Rewards(Main plugin) {
			this.plugin = plugin;
			config = plugin.getConfig();
		}
		
		//Checks to see if the path in the config even exists before it does anything else
		public boolean isTier(String tierName) {
			
			if(!(config.getString("Tiers." + tierName.toLowerCase()) == null)) return true;
			
			return false;
	
		}
		
		//For CrateName
		//bbr rewardadd (tier) (rewardType) (Crate)
		public void rewardAdd(Player p,String tier, String rewardType, String crateName) {	
			
			final String PATH = "Tiers." + tier.toLowerCase() + ".Properties.Rewards.Crate";
			Chat.sendPlayerMessage(p, PATH);
			if(isTier(tier) == false) {
				Chat.sendPlayerMessage(p, "&bThis is not a tier! Do &6&l/bbr tierlist &bto see all the different tiers!");
				return;
			}
			
			/*
			 * CrateReloaded is not required for this plugin to fully work,
			 * but it does limit your reward types.
			 * (Can't use the crate types)
			 */
			if(checkCrateReloaded() == false) {
				Chat.sendPlayerMessage(p, "&bYou must have &5&lCrateReloaded &binstalled to use this reward type!");
				return;
			}else {
				Chat.sendPlayerMessage(p, "&bThe crate for tier &5&l" + tier.toUpperCase() + "&b has been updated to &5&l" + crateName.toUpperCase());
				config.set(PATH, crateName);
			}
			
		}
		//For Money
		//bbr rewardadd (tier) (rewardType) (Amount of money)
		public void rewardAdd(Player p,String tier, String rewardType, int amount) {	
			
			
		}
		//For Items
		//It will add the item in your hand
		//bbr rewardadd (tier) (rewardType)
		public void rewardAdd(Player p,String tier, String rewardType) {	
			
		}
		
		//For Commands
		//bbr rewardadd (tier) (rewardType)
		public void rewardAddCommand(Player p,String tier, String rewardType) {	
			
		}
		
		public void rewardRemove() {
			
		}

		public boolean checkCrateReloaded() {
			return plugin.getServer().getPluginManager().isPluginEnabled("CrateReloaded");
		}
}
