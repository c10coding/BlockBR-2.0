package me.caleb.BlockBR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.commands.PlayerCommands;
import me.caleb.BlockBR.listeners.BlockEvent;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Chat;
import me.caleb.BlockBR.utils.Fireworks;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{

	private Connection connection;
	private String host, password, database, username;
	private int port;
	public static Economy economy = null;
	public FileConfiguration config;
	
	@Override
	public void onEnable() {
		
		config = this.getConfig();
		
		if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
		
		/*
		 * This is their first time setting up 
		 */
		if(config.getString("Host").isEmpty() || config.getString("Password").isEmpty() || config.getString("Username").isEmpty()) {
			Chat.sendConsoleMessage("Please enter your MYSQL credentials into the config so that we can get started with the fun!");
		}else {
			mysqlSetup();
			loadConfig();
			getConfig().options().copyDefaults(true);
			validateConfig();
			
			new BlockEvent(this);
			new PlayerCommands(this);
			new Fireworks(this);
		}
		
		
		
	}
	
	@Override
	public void onDisable() {
		
		try {
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		saveDefaultConfig();
	}
	
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
    public static Economy getEconomy() {
        return economy;
    }
	
	public Connection getConnection() {
		return connection;
	}
	
	public void openConnection() throws SQLException, ClassNotFoundException {
		
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        } 
	        Class.forName("com.mysql.jdbc.Driver");
	        
	        String dbName = config.getString("Database");
	        if(dbName.equalsIgnoreCase("none")) {
	        	connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port, this.username, this.password);
	        }else {
	        	 connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
	        }
	        
	        Bukkit.getConsoleSender().sendMessage(Chat.blockBrChat(("MYSQL CONNECTED!")));
	    }
	    
	}
	
	public void mysqlSetup() {
		
		host = this.getConfig().getString("Host");
		port = this.getConfig().getInt("Port");
		password = this.getConfig().getString("Password");
		database = this.getConfig().getString("Database");
		username = this.getConfig().getString("Username");

		try {  
			
            openConnection();
            
            /*
             * Creates the database and the table
             */
        	try {
        		PreparedStatement stmt = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS blockbr2");
        		stmt.executeUpdate();
        		stmt = connection.prepareStatement("USE `blockbr2`");
        		stmt.executeQuery();
        		config.set("Database", "blockbr2");
        		this.saveConfig();
        		stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `blockbr2` (`id` int(11) NOT NULL AUTO_INCREMENT, `playerName` varchar(255) NOT NULL, `tier` varchar(255) NOT NULL, `level` varchar(255) NOT NULL DEFAULT 1, PRIMARY KEY (`id`));");
        		stmt.executeUpdate();
        	}catch(SQLException e1) {
        		e1.printStackTrace();
        	}
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            Chat.sendConsoleMessage("Your credentials to your mysql database is incorrect!");
        }
			
	}
	
	
	/*
	 * Validates the MineType and tierlist relative to what's in the database
	 */
	public void validateConfig() {
		
		String mineType = config.getString("MineType");
		
		if(!(mineType.equalsIgnoreCase("all")) && !(mineType.equalsIgnoreCase("group")) && !(mineType.equalsIgnoreCase("onebyone"))) {
			config.set("MineType", "all");
			Chat.sendConsoleMessage("Error reading the the config value \"MineType\". Setting MineType to \"all\"");
		}
		
		this.saveConfig();
		
		List<String> tierList = config.getStringList("TierList");
		DataManager dm = new DataManager(this);
		ArrayList<String> columns = dm.getColumns();
		
		/*
		 * If the config does not have this column, then remove it
		 * The database should have the same stuff the config does
		 */
		
		if(tierList.size() == 0 && columns.size() > 1) {
			for(String column : columns) {
				dm.removeColumn(column);
				Chat.sendConsoleMessage("Config does not match up with database. Removing tier " + column);
			}
		}else if(tierList.size() > 1 && columns.size() == 0){
			for(String tier : tierList) {
				dm.addColumn(tier);
				Chat.sendConsoleMessage("Config does not match up with database. Adding tier " + tier);
			}
		}else{
			
			/*
			 * There are more tiers in db than tierlist
			 * Remove ---
			 */
			if(columns.size() > tierList.size()) {
				for(int x = 0; x < columns.size(); x++) {
					if(!tierList.contains(columns.get(x))) {
						dm.removeColumn(columns.get(x));
						Chat.sendConsoleMessage("Config does not match up with database. Removing tier " + columns.get(x));
					}
				}
			/*
			 * There are less tiers in db than tierlist
			 * Add ---
			 */
			}else if(columns.size() < tierList.size()){
				for(int x = 0; x < tierList.size();x++) {
					if(!columns.contains(tierList.get(x))) {
						dm.addColumn(tierList.get(x));
						Chat.sendConsoleMessage("Config does not match up with database. Adding tier " + tierList.get(x));
					}
				}
			}else {
				Chat.sendConsoleMessage("The config matches up with the database!");
				return;
			}
		}
		
		
		
	}
	
	public boolean checkCrateReloaded() {
		return this.getServer().getPluginManager().isPluginEnabled("CrateReloaded");
	}
	
	
	

}
