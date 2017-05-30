package ru.ocelotjungle.GetScoreboardVariable;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.eclipse.jetty.server.Server;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static org.bukkit.Server bServer;
	public static Scoreboard scboard;
	public static byte loggingMode;
	private Server server;
	private int port;
	
	public Main() {
		getLogger().setLevel(Level.OFF);
	}
	
	public void onEnable() {
		plugin = this;
		bServer = getServer();
		scboard = getServer().getScoreboardManager().getMainScoreboard();
		
		saveDefaultConfig();
		
		loggingMode = (byte) (getConfig().getBoolean("logging.enable") == true ? 
							(getConfig().getBoolean("logging.advanced") == true? 2: 1) : 0);
		port = getConfig().getInt("port");
		
		server = new Server(port);
		server.setHandler(new HTTPHandler());
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onDisable() {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}