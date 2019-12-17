package me.caleb.BlockBR.admin;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
		public void isTier(String tierName,Player p) {
			
			if(config.getString("Tiers." + tierName.toLowerCase()) == null) {
				Chat.sendPlayerMessage(p, "&bThis is not a tier! Do &6&l/bbr tierlist &bto see all the different tiers!");
				return;
			}
			
		}
		
		//For CrateName
		//bbr rewardadd (tier) (rewardType) (Crate)
		public void rewardAdd(Player p,String tier, String rewardType, String crateName) {	
			
			final String PATH = "Tiers." + tier + ".Properties.Rewards.Crate";

			isTier(tier,p);
			
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
				config.set("Tiers." + tier + ".Properties.Rewards.Crate", crateName);
				plugin.saveConfig();
			}
			
		}
		//For Money
		//bbr rewardadd (tier) (rewardType) (Amount of money)
		public void rewardAdd(Player p,String tier, String rewardType, int amount) {	
			
			final String PATH = "Tiers." + tier + ".Properties.Rewards.Money";

			isTier(tier,p);
			
			config.set("Tiers" + tier + ".Properties.Rewards.Money", amount);
			
		}
		//For Items
		//It will add the item in your hand
		//bbr rewardadd (tier) (rewardType)
		public void rewardAdd(Player p,String tier, String rewardType) {	
			
			final String PATH = "Tiers." + tier + ".Properties.Rewards.Money";
			ItemStack item = p.getItemInHand();
			isTier(tier,p);
			
			int amount = item.getAmount();
			String itemName = item.getType().name();
			Map<Enchantment, Integer> enchants = item.getEnchantments();
			
			
			
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
