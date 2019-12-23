package me.caleb.BlockBR.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import me.caleb.BlockBR.Main;

public class DataManager {

	private Main plugin;
	
	public DataManager(Main plugin) {
		this.plugin = plugin;
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
			
			PreparedStatement stmt = plugin.getConnection().prepareStatement("SELECT * FROM `blockbr` WHERE playerName=?");
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
	
	public void addPlayer() {
		
		try {
			
			PreparedStatement stmt = plugin.getConnection().prepareStatement("INSERT INTO `blockbr`");
			
		}catch(SQLException e) {
			
		}
		
	}
	
}
