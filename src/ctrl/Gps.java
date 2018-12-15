package ctrl;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

@WebServlet("/Gps.do")
public class Gps extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public Gps() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("calc") == null) {

		} else {
			Engine engine = Engine.getInstance();
			String lat1 = request.getParameter("lat1");
			String lon1 = request.getParameter("lon1");
			String lat2 = request.getParameter("lat2");
			String lon2 = request.getParameter("lon2");

			try {
				request.setAttribute("lat1", lat1);
				request.setAttribute("lon1", lon1);
				request.setAttribute("lat2", lat2);
				request.setAttribute("lon2", lon2);
				double result = engine.doGps(lat1, lon1, lat2, lon2);
				String output = NumberFormat.getIntegerInstance().format(Math.round(result)) + " km";
				request.setAttribute("result", output);
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}
		}
		this.getServletContext().getRequestDispatcher("/Gps.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
