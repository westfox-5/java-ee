package it.westfox5.helloworld.servlets;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "HomepageServlet")
@MultipartConfig()
public class HomepageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Counter counter;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext ctx = config.getServletContext();
		counter = Counter.of(ctx);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter writer = res.getWriter();

		writer.write("<html>");
		writer.write("<head>");
		writer.write("<title>Hello World</title>");
		writer.write("</head>");
		writer.write("<body>");

		writer.write("Hi! you are currently being served by:<br />");
		writer.write("Server Name: " + req.getServerName() + "<br />");
		writer.write("Server Port: " + req.getServerPort() + "<br />");
		writer.write("Context Path: " + req.getContextPath() + "<br />");
		writer.write("Servlet Path: " + req.getServletPath() + "<br />");
		writer.write("Locale: " + req.getLocale() + "<br />");
		writer.write("Session ID: " + req.getSession().getId() + "<br /><br />");
		writer.write("Counter: " + counter.getAndIncrement(req.getSession().getId()) + "<br /><br />");

		writer.write("<form align='left' action='Homepage' method='POST' enctype='multipart/form-data'>");
		writer.write("<input value='Choose' name='myFile' type='file' accept='text/plain'><br>");
		writer.write("<input value='Upload' type='submit\'><br>");
		writer.write("</form>");
		writer.write("</body>");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {
			uploadFile(req);
			resp.sendRedirect("Homepage");
		} else {
			resp.sendError(500);
		}
	}



	private void uploadFile(HttpServletRequest request) throws IOException, ServletException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String filename = null;
		for (Part p : request.getParts()) {
			this.copyBytes(p.getInputStream(), baos);
			filename = p.getSubmittedFileName();
		}
		if (!"".equals(filename)) {
			filename = filename.substring(0, filename.lastIndexOf(".")) + "-UPLOADED" + filename.substring(filename.lastIndexOf("."), filename.length());
			try(OutputStream outputStream = new FileOutputStream(filename)) {
				baos.writeTo(outputStream);
			}
		}
	}

	private void copyBytes(InputStream is, OutputStream os) throws IOException {
		int i;
		while ((i = is.read()) != -1) {
			os.write(i);
		}
		is.close();
		os.close();
	}

}
