package ru.ocelotjungle.GetScoreboardVariable;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bukkit.scoreboard.Objective;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HTTPHandler extends AbstractHandler {
	
	@Override
	public void handle(String arg0, Request baseRequest, HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String act = req.getParameter("act");
		if(act != null) {
			String vname = req.getParameter("vname");
			String pass = req.getParameter("pass");
			switch(act) {
			case "value":
				returnValue(vname, req.getParameter("pname"), pass, req, resp);
				break;
			case "top":
				returnTop(vname, req.getParameter("size"), req.getParameter("min"), pass, req, resp);
				break;
			}
			resp.getWriter().flush();
		}
	}
	
	private void returnValue(String vname, String pname, String pass, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if(vname != null && pname != null) {
			if(!Main.valueBlacklist.contains(vname) || Main.valueBlacklist.contains(vname) && pass != null && pass.equals(Main.valuePass)) {
				Objective var = Main.scboard.getObjective(vname);
				if(var != null) {
					try {
						Integer score = var.getScore(pname).getScore();
						Value value = new Value(vname, pname, score);
						Gson gson = new GsonBuilder().setPrettyPrinting().create();
						resp.getWriter().write(gson.toJson(value));
						if(Main.loggingMode) {
							log("(VALUE; " + vname + "; " + pname + ") [" + req.getRemoteAddr() + ":" + req.getRemotePort() + "]");
						}
						return;
					} catch(IllegalArgumentException iae) { }
				}
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
	
	class Value {
		public String act, var, player;
		public int value;
		
		public Value(String var, String player, int value) {
			this.act = "value";
			this.var = var;
			this.player = player;
			this.value = value;
		}
	}
	
	private void returnTop(String vname, String ssize, String smin, String pass, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if(vname != null && ssize != null) {
			if(!Main.topBlacklist.contains(vname) || Main.topBlacklist.contains(vname) && pass != null && pass.equals(Main.topPass)) {
				Objective var = Main.scboard.getObjective(vname);
				
				if(var != null) {
					int size = Integer.parseInt(ssize);
					Set<String> playersSet = Main.scboard.getEntries();
					HashSet<String> players = new HashSet<>(playersSet.size());
					for(String player : playersSet) {
						players.add(player);
					}
					LinkedList<TopValue> values = new LinkedList<>();
					int min = smin == null ? Integer.MIN_VALUE : Integer.parseInt(smin);
					for(int i = 0; i < size; i++) {
						String maxPlayer = "";
						int maxScore = min;
						for(String player : players) {
							int score = var.getScore(player).getScore();
							if(score > maxScore) {
								maxPlayer = player;
								maxScore = score;
							}
						}
						if(maxPlayer.length() == 0) {
							break;
						}
						values.add(new TopValue(maxPlayer, maxScore));
						players.remove(maxPlayer);
					}
					Top top = new Top(vname, size, min, values);
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					resp.getWriter().write(gson.toJson(top));
					if(Main.loggingMode) {
						log("(TOP; " + vname + "; " + size + ") [" + req.getRemoteAddr() + ":" + req.getRemotePort() + "]");
					}
					return;
				}
			}
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	class Top {
		public String act, var;
		public int size, min;
		public LinkedList<TopValue> values;
		
		public Top(String var, int size, int min, LinkedList<TopValue> values) {
			this.act = "top";
			this.var = var;
			this.size = size;
			this.min = min;
			this.values = values;
		}
	}
	
	class TopValue {
		public String player;
		public int value;
		
		public TopValue(String player, int value) {
			this.player = player;
			this.value = value;
		}
	}
	
	private void log(Object Log) {
		Main.bServer.getLogger().info(Log == null ? "null" : "[GSV] " + Log.toString());
	}

}