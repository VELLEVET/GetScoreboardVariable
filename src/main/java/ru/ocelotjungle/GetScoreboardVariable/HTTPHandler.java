package ru.ocelotjungle.GetScoreboardVariable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bukkit.scoreboard.Objective;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class HTTPHandler extends AbstractHandler {
	
	@Override
	public void handle(String arg0, Request baseRequest, HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String act = req.getParameter("act");
		if(act != null) {
			switch(act) {
			case "value":
				returnValue(req.getParameter("vname"), req.getParameter("pname"), req, resp);
				break;
			case "top":
				returnTop(req.getParameter("vname"), req.getParameter("size"), req, resp);
				break;
			}
			resp.getWriter().flush();
		}
	}
	
	private void returnValue(String vname, String pname, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log(System.currentTimeMillis());
		if(vname != null && pname != null) {
			Objective var = Main.scboard.getObjective(vname);
			if(var != null) {
				try {
					Integer score = var.getScore(pname).getScore();
					resp.getWriter().write(score.toString());
					if(Main.loggingMode) {
						log("(" + vname + "; " + pname + ") [" + req.getRemoteAddr() + ":" + req.getRemotePort() + "]");
					}
					log(System.currentTimeMillis());
					return;
				} catch(IllegalArgumentException iae) { }
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
	
	private void returnTop(String vname, String ssize, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log(System.currentTimeMillis());
		if(vname != null && ssize != null) {
			Objective var = Main.scboard.getObjective(vname);
			
			if(var != null) {
				int size = Integer.parseInt(ssize);
				Set<String> playersSet = Main.scboard.getEntries();
				HashSet<String> players = new HashSet<>(playersSet.size());
				for(String player : playersSet) {
					players.add(player);
				}
				final String NEWLINE = "\r\n";
				StringBuffer result = new StringBuffer("{" + NEWLINE);
				for(int i = 0; i < size; i++) {
					String maxPlayer = "";
					int maxScore = Integer.MIN_VALUE;
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
					result.append(String.format("\"%s\": %d,%s", maxPlayer, maxScore, NEWLINE));
					players.remove(maxPlayer);
				}
				result.append("}");
				resp.getWriter().write(result.toString());
				log(System.currentTimeMillis());
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
	
	private void log(Object Log) {
		Main.bServer.getLogger().info(Log == null ? "null" : "[GSV] " + Log.toString());
	}

}