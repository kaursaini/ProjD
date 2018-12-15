package ctrl;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Engine;

@WebServlet("/Drone.do")
public class Drone extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Drone() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("calc") == null) {

		} else {
			Engine engine = Engine.getInstance();
			String from = request.getParameter("from");
			String dest = request.getParameter("dest");

			HttpSession sn = request.getSession();
			sn.setAttribute("usedDrone", true);
			
			try {
				request.setAttribute("from", from);
				request.setAttribute("dest", dest);
				double result = engine.doDrone(from, dest);
				String output = NumberFormat.getIntegerInstance().format(Math.round(result)) + " min";
				request.setAttribute("result", output);
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}
		}
		this.getServletContext().getRequestDispatcher("/Drone.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
