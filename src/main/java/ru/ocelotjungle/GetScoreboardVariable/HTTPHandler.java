package ru.ocelotjungle.GetScoreboardVariable;

import java.io.IOException;
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

		byte operationStatus = 0;
		
		String vname = req.getParameter("vname"),
				pname = req.getParameter("pname");
		
		if (vname == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			Objective var = Main.scboard.getObjective(vname);
			if (var == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				operationStatus = 1;
			} else {
				Integer score = var.getScore(pname).getScore();
				resp.getWriter().write(score.toString());
				operationStatus = 2;
			}
		}
		resp.getWriter().flush();
		
		logInfo(operationStatus, vname, pname, req);
	}
	
	private void logInfo(byte opStatus, String vname, String pname, HttpServletRequest req) {
		if (Main.loggingMode > 0 && opStatus > 0) {
			if (opStatus == 1) {
				log("] (NO VAR '" + vname + "')");
			} else if (opStatus == 2) {
				log("] (" + vname + "; " + pname + ")");
			}
			if (Main.loggingMode == 2) {
				log("-ADV] " + req.getRemoteAddr() + ":" + req.getRemotePort());
			}
		}
	}
	
	private void log(Object Log) {
		Main.bServer.getLogger().info(Log == null ? "null" : "[GSV" + Log.toString());
	}

}