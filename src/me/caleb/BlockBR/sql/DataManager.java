package me.caleb.BlockBR.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.mineType.Group;

public class DataManager {

	private Main plugin;
	private Player p;
	
	public DataManager(Main plugin,Player p) {
		this.plugin = plugin;
		this.p = p;
	}
	
	//Gets all the data from the player
	public ResultSet getResultSet(String playerName) {
		
		try {
			
			PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr` WHERE playerName=?");
			stmt.setString(1, playerName);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs;
			}else {
				return null;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Checks to see if player is in database
	public boolean ifInDB(String playerName) {
		
		try {
			
			PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2` WHERE playerName=?");
			stmt.setString(1, playerName);
			
			ResultSet rs = stmt.executeQuery();
			//Bukkit.broadcastMessage(playerName);
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
			
		}catch(SQLException e) {
			return false;
		}catch(NullPointerException e) {
			return false;
		}
		
	}
	
	//Adds player to the database
	
	public void addPlayer(Player p) {
		
		try {
			
			PreparedStatement stmt = plugin.getConnection().prepareStatement("INSERT INTO `blockbr2` (playerName,tier) VALUES(?,?)");
			stmt.setString(1, p.getName());
			stmt.setString(2, "grass");
			stmt.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getTier(Player p) {
		
		String playerName = p.getName();
		
		try {
			
			PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT tier FROM `blockbr2` WHERE playerName=?");
			stmt.setString(1, playerName);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.isBeforeFirst()) {
				rs.next();
				return rs.getString("tier");
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/*
	 * Gets the group that the player is in based on 
	 * the values in the database
	 * 
	 * Looks for the group that has tiers that amount to a number instead of the word "done"
	 * 
	 * The keyword "done" signifies that that tier has been finished. If all tiers in that group are "done" then
	 * the player is not in that group
	 */
	public String getGroup() {
		
		List<String> groupList = plugin.config.getStringList("GroupList");
		String group = null;
		for(int x = 0; x < groupList.size(); x++) {
			Group g = new Group(plugin);
			List<String> groupTiers = g.getGroupTiers(x);
			
			for(String tier : groupTiers) {
				
				try {
					PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2` WHERE playerName=?");
					stmt.setString(1, p.getName());
					
					ResultSet rs = stmt.executeQuery();
					rs.next();
					String amount = rs.getString(tier);
					
					if(!amount.equalsIgnoreCase("done")) {
						
						group = Group.tierGroup(tier);
						return group;
					}
					
				}catch(SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return group;
		
	}
	
	/*
	 * This is a mess of a method
	 */
	public ArrayList<String> getUndoneTiers(){
		
		List<String> groupList = plugin.config.getStringList("GroupList");
		ArrayList<String> undoneTiers = new ArrayList<String>();
		FileConfiguration config = plugin.getConfig();
		String group;
		List<String> tiersInDesiredGroup;
		search:{
			for(int x = 0; x < groupList.size(); x++) {
				Group g = new Group(plugin);
				List<String> groupTiers = g.getGroupTiers(x);
				
				for(String tier : groupTiers) {
					
					try {
						PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2` WHERE playerName=?");
						stmt.setString(1, p.getName());
						
						ResultSet rs = stmt.executeQuery();
						rs.next();
						String amount = rs.getString(tier);
						
						if(!amount.equalsIgnoreCase("done")) {
							//Bukkit.broadcastMessage(tier);
							
							group = Group.tierGroup(tier);
							
							tiersInDesiredGroup = config.getStringList("Groups." + group);
							
							for(String t : tiersInDesiredGroup){
								stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2` WHERE playerName=?");
								stmt.setString(1, p.getName());
								rs = stmt.executeQuery();
								rs.next();
								amount = rs.getString(t);
								if(!amount.equalsIgnoreCase("done")) {
									undoneTiers.add(t);
								}
								
							}
							
							break search;
							
						}
						
					}catch(SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
		return undoneTiers;
		
	}
	
}
