package me.caleb.BlockBR.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.caleb.BlockBR.Main;

public class DataManager {

	private Main plugin;
	Player p;
	
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
				return rs.getString("tier");
			}

		}catch(SQLException e) {
			
		}
		
		return null;
		
	}
	
	public String getGroup() {
		
		String group = null;
		
		return group;
	}
}
