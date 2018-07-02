package ru.ocelotjungle.GetScoreboardVariable;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.eclipse.jetty.server.Server;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static org.bukkit.Server bServer;
	public static Scoreboard scboard;
	public static boolean loggingMode;
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
		
		loggingMode = getConfig().getBoolean("logging.enable");
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
		plugin.getCommand("gsv").setExecutor(new CommandManager());
		
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