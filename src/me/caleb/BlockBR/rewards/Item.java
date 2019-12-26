package me.caleb.BlockBR.rewards;

import java.util.Map;

public class Item {

	private String name;
	public Map<String,Integer> enchants;
	private int amount;
	
	public Item(String name, Map<String, Integer> enchants, int amount) {
		this.name = name;
		this.enchants = enchants;
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "Name: " + this.name + ", Amount: " + this.amount + ", Enchants: " + this.enchants.toString();
	}
	
}
