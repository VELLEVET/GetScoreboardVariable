package ru.ocelotjungle.GetScoreboardVariable;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;

public class CommandManager implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int size = Integer.parseInt(args[0]);
		Objective obj = Main.scboard.getObjective("EventCheck");
		Random random = new Random();
		for(int i = 0; i < size; i++) {
			String pname = "TP_" + i;
			obj.getScore(pname).setScore(random.nextInt());
		}
		Main.bServer.broadcastMessage("Filling scoreboard is done (" + size + " values)!");
		Main.plugin.getLogger().info("Filling scoreboard is done (" + size + " values)!");
		return true;
	}

}
