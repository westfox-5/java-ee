package it.westfox5.helloworld.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Homepage")
public class Homepage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ServletContext ctx = req.getServletContext();
		Counter counter = Counter.of(ctx);

		PrintWriter writer = res.getWriter();
		writer.println("Hi! you are currently being served by:");
		writer.println("Server Name: " + req.getServerName());
		writer.println("Server Port: " + req.getServerPort());
		writer.println("Context Path: " + req.getContextPath());
		writer.println("Servlet Path: " + req.getServletPath());
		writer.println("Locale: " + req.getLocale());
		writer.println("Session ID: " + req.getSession().getId());
		writer.println("---------------------------------");
		writer.println("Counter: " + counter.getAndIncrement(req.getSession().getId()));
	}

}
