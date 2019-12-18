package me.caleb.BlockBR.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;

public class RewardCommandPrompt extends StringPrompt{
	
	private Main plugin;
	String tier;
	Player player;
	
	public RewardCommandPrompt(Main plugin,String tier,Player p) {
		this.plugin = plugin;
		this.tier = tier;
		this.player = p;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {
		
		List<String> cmds = (List<String>) plugin.getConfig().getList("Tiers." + tier + ".Properties.Rewards.Commands");
		cmds.add(answer);
		
		con.getForWhom().sendRawMessage(Chat.blockBrChat("&bYour command has been added as a reward!"));
		plugin.saveConfig();
		
		return null;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return Chat.blockBrChat("&bEnter the desired command you wish to run as a reward! Make sure you use the &5&l%player% &bplaceholder if you want a player\'s name to be used there!");
	}

}
