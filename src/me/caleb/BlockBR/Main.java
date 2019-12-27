package me.caleb.BlockBR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.caleb.BlockBR.admin.mineType.Group;
import me.caleb.BlockBR.commands.PlayerCommands;
import me.caleb.BlockBR.listeners.BlockEvent;
import me.caleb.BlockBR.utils.Chat;
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
		
		mysqlSetup();
		
		loadConfig();
		getConfig().options().copyDefaults(true);
		
		/*
		 * When the plugin gets reloaded or ran for the first time, it checks to see if any 
		 * edits have been made to the MineType to make sure that the config is formulated correctly
		 * according to the MineType
		 */
		/*
		String mineType = config.getString("MineType");
		List<String> groupList = config.getStringList("GroupList");
		Group g = new Group(this);
		
		if(mineType.equalsIgnoreCase("group") && groupList.isEmpty()) {
			g.formulateConfig("form");
		}else {
			g.formulateConfig("remove");
		}*/
		
		new BlockEvent(this);
		new PlayerCommands(this);
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
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
			
	}
	
	/*
	 * Checks on reload to see if the player has removed something
	 * from the config that is still in the database
	 */
	public void validateConfig() {
		
		
		
	}
	
	public boolean checkCrateReloaded() {
		return this.getServer().getPluginManager().isPluginEnabled("CrateReloaded");
	}
	
	
	

}
