package me.caleb.BlockBR.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.rewards.Item;
import net.minecraft.server.v1_14_R1.Enchantments;

public class ItemParser {

	private Main plugin;
	List<String> itemList = null;
	String line;
	String tier;
	
	public ItemParser(Main plugin,String line,String tier) {
		this.plugin = plugin;
		this.line = line;
		this.tier = tier;
	}
	
	public String getItemName() {
		
		String itemName = "";
		
		String[] arrLine = line.split(" ");
		itemName = arrLine[1];
		
		if(itemName.contains("amount")) {
			spacingError();
			itemName = "";
		}
		
		return itemName;
	}
	
	public void spacingError() {
		Bukkit.getConsoleSender().sendMessage(Chat.chat("&4vvvvvvv BLOCKBR ERROR vvvvvvv"));
		Bukkit.getConsoleSender().sendMessage(Chat.chat("The spacing in this line " + line + " is out of wack in tier &5&l" + tier));
		Bukkit.getConsoleSender().sendMessage("Make sure that there is a space after name:, amount:, and Enchants: ");
		Bukkit.getConsoleSender().sendMessage(Chat.chat("&4^^^^^^^ BLOCKBR ERROR ^^^^^^^"));
	}
	
	public Map<Enchantment, Integer> getItemEnchants() {
		
		Map<Enchantment,Integer> enchants = new HashMap();
		String[] arrLine = line.split(" ");
		ArrayList<String> arrEnchants = new ArrayList<String>();
		
		//If the item has no enchants
		if(!line.contains("Enchants")) {
			return enchants;
		}
		
		//Gets all the Enchants into an array
		for(int i = 4; i < arrLine.length;i++) {
			arrEnchants.add(arrLine[i]);	
		}
		
		//If the first index is not Enchants:, then there is something wrong with the yml file
		if(!arrEnchants.get(0).equalsIgnoreCase("Enchants:")) {
			spacingError();
		}else {
			//Compiles the map with the enchant, and then the level
			//Increments it by 2 to go on to the next enchants
			for(int x = 1; x < arrEnchants.size();x+=2) {
				//The substring separates the colon from the number
				//For example: 5; -> 5
				Enchantment e = Enchantment.getByName(arrEnchants.get(x));
				enchants.put(e, Integer.parseInt(arrEnchants.get(x+1).substring(0,1)));
			}
		}
		return enchants;
	}
	
	public int getAmount() {
		
		int amount = 0;
		
		String[] arrLine = line.split(" ");
		
		String[] amountArr = {arrLine[2],arrLine[3]};
		
		if(!amountArr[0].equalsIgnoreCase("amount:")) {
			spacingError();
			return amount;
		}else {
			return Integer.parseInt(arrLine[3]);
		}
		
	}
	
	
	
}
