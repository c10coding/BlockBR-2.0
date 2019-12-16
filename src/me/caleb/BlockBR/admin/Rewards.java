package me.caleb.BlockBR.admin;

import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.utils.Chat;

public class Rewards {
		
		private Main plugin;
		
		public Rewards(Main plugin) {
			this.plugin = plugin;
		}
	
		//For CrateName
		//bbr rewardadd (tier) (rewardType) (Crate)
		public void rewardAdd(Player p,String tier, String rewardType, String crateName) {	
			/*
			 * CrateReloaded is not required for this plugin to fully work,
			 * but it does limit your reward types.
			 * (Can't use the crate types)
			 */
			if(checkCrateReloaded() == false) {
				Chat.sendPlayerMessage(p, "This plugin is not enabled");
				return;
			}else {
				
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
