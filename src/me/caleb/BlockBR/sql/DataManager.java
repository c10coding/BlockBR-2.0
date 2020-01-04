package me.caleb.BlockBR.sql;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.utils.Chat;

public class DataManager {

	private Main plugin;
	private Player p;
	private FileConfiguration config;
	
	public DataManager(Main plugin,Player p) {
		this.plugin = plugin;
		this.p = p;
		config = plugin.getConfig();
	}
	
	public DataManager(Main plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
	}
	
	/*
	 * Gets all the data from a player and returns a ResultSet
	 * for easy usage
	 */
	public ResultSet getResultSet(String playerName) {
		
		try {
			
			PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2` WHERE playerName=?");
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
	
	public int getLevel() {
		
		try {
			ResultSet rs = getResultSet(p.getName());
			return rs.getInt("level");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 5;
	}
	
	/*
	 * Gets the tier amount for a specific tier
	 */
	public int getTierAmount(String tier) {
		
		PreparedStatement stmt;
		int amount = 0;
		try {
			stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2` WHERE playerName=?");
			stmt.setString(1, p.getName());
			ResultSet rs = stmt.executeQuery();
			rs.next();
			
			/*
			 * If the amount comes back as "done" then it makes the value -1
			 * This is unnecessary, but i'm going to leave it there because everything is working as intended
			 */
			try {
				amount = Integer.parseInt(rs.getString(tier));
			}catch(NumberFormatException e) {
				return -1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return amount;
	}
	
    /*
     * Checks to see if the player is the database
     */
	public boolean ifInDB(String playerName) {
		
		try {
			
			PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2` WHERE playerName=?");
			stmt.setString(1, playerName);
			
			ResultSet rs = stmt.executeQuery();
			
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
	
	
	/*
	 * Adds the player into the `blockbr2` database
	 */
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
	
	/*
	 * Gets the tier that the player is currently on
	 */
	public String getTier() {
		
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
	 * When all the checks are done and the player isn't at the threshold, increase the amount that they have at a specific tier
	 */
	public void increaseAmount(String tier,int amount) {
		
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("UPDATE `blockbr2` SET " + tier + "=? WHERE playerName=?");
			stmt.setInt(1, amount);
			stmt.setString(2, p.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
	public ArrayList<String> getUndoneTiersInGroup(){
		
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
	
	/*
	 * Sets the current column to "done" whenever they meet the threshold
	 */
	public void setToDone(String tier) {
		
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("UPDATE `blockbr2` SET " + tier + "=? WHERE playerName=?");
			stmt.setString(1, "done");
			stmt.setString(2, p.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * Returns the undone tiers of all the tiers
	 * If tier =! "done", then it adds it to the array of undone tiers
	 */
	public ArrayList<String> getAllUndoneTiers() {
		
		List<String> tierList = config.getStringList("TierList");
		ArrayList<String> undoneTiers = new ArrayList<String>();
		
		for(String tier : tierList) {
			
			try {
				
				PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2` WHERE playerName=?");
				stmt.setString(1, p.getName());
				ResultSet rs = stmt.executeQuery();
				rs.next();
				String tierAmount = rs.getString(tier);
				
				if(!tierAmount.equalsIgnoreCase("done")) {
					undoneTiers.add(tier);
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		
		return undoneTiers;
	}

	/*
	 * Levels up the player whenever they complete all the tiers
	 */
	public void levelUp(String tier) {
		
		try {
			int level = getLevel();
			level++;
			PreparedStatement stmt = plugin.getConnection().prepareStatement("UPDATE `blockbr2` SET level=? WHERE playerName=?");
			stmt.setString(1, String.valueOf(level));
			stmt.setString(2, p.getName());
			stmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Used when there's nothing in the tierlist and the player wants to add their first tier.
	 */
	public void setTier(String tier) {
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("UPDATE `blockbr2` SET tier=? WHERE playerName=?");
			stmt.setString(1, tier);
			stmt.setString(2, p.getName());
			stmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * If the first tier is removed from the database, then 
	 * the "tier" column is set to the next tier in line
	 * for all people that were on the first tier
	 */
	public void alterFirstTiers(String tier, String nextTier) {
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("UPDATE `blockbr2` SET tier=? WHERE tier=?");
			stmt.setString(1, nextTier);
			stmt.setString(2, tier);
			stmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This is called whenever someone levels up. It sets all the tiers from "done" --> 0
	 */
	public void resetTiers() {
		
		List<String> tierList = config.getStringList("TierList");
		
		for(String tier : tierList) {
			try {
				PreparedStatement stmt = plugin.getConnection().prepareStatement("UPDATE `blockbr2` SET " + tier + "=? WHERE playerName=?");
				stmt.setInt(1, 0);
				stmt.setString(2, p.getName());
				stmt.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("UPDATE `blockbr2` SET tier=? WHERE playerName=?");
			stmt.setString(1, tierList.get(0));
			stmt.setString(2, p.getName());
			stmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void nextTier(String completedTier) {
		
		List<String> tierList = config.getStringList("TierList");
		
		int index = tierList.indexOf(completedTier);
		int nextIndex = index+=1;
		
		String nextTier = tierList.get(nextIndex);
		
		PreparedStatement stmt;
		try {
			stmt = plugin.getConnection().prepareStatement("UPDATE `blockbr2` SET tier=? WHERE playerName=?");
			stmt.setString(1, nextTier);
			stmt.setString(2, p.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<String> getColumns() {
		
		ArrayList<String> columns = new ArrayList<String>();
		
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr2`");
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			
			for(int x = 5;x <= rsmd.getColumnCount(); x++) {
				columns.add(rsmd.getColumnName(x));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return columns;
		
	}

	public void removeColumn(String tier) {
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("ALTER TABLE `blockbr2` DROP " + tier);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addColumn(String tier) {
		try {
			PreparedStatement stmt = plugin.getConnection().prepareStatement("ALTER TABLE `blockbr2` ADD " + tier + " varchar(255) NOT NULL DEFAULT 0");
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
