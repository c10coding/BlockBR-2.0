package me.caleb.BlockBR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.caleb.BlockBR.commands.PlayerCommands;
import me.caleb.BlockBR.listeners.BlockBroke;
import me.caleb.BlockBR.utils.Chat;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{

	private Connection connection;
	private String host, password, database, username;
	private int port;
	public static Economy economy = null;
	
	private File customConfigFile;
    private FileConfiguration customConfig;
	
	@Override
	public void onEnable() {
		
		if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
		
		mysqlSetup();
		loadConfig();
		createRewardsConfig();
		
		new BlockBroke(this);
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
	
	public void createRewardsConfig() {
		customConfigFile = new File(getDataFolder(), "rewards.yml");
		
		if(!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdir();
			saveResource("rewards.yml",false);
		}
		
		customConfig= new YamlConfiguration();
		
		try {
			try {
				customConfig.load(customConfigFile);
			} catch (InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }
	
	

}
