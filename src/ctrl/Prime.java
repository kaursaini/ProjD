package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

@WebServlet("/Prime.do")
public class Prime extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Prime() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if ((request.getParameter("calc") == null) && (request.getParameter("recalc") == null)) {

		} else if ((request.getParameter("recalc") == null) && (request.getParameter("calc") != null)) {
			Engine engine = Engine.getInstance();
			String min = request.getParameter("min");
			String max = request.getParameter("max");

			try {
				request.setAttribute("min", min);
				request.setAttribute("max", max);
				request.setAttribute("last", engine.doPrime(min, max));
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}
		} else if ((request.getParameter("recalc") != null) && (request.getParameter("calc") == null)) {
			Engine engine = Engine.getInstance();
			String newMin = request.getParameter("last");
			String max = request.getParameter("max");
			request.setAttribute("min", newMin);
			request.setAttribute("max", max);
			try {
				request.setAttribute("last", engine.doPrime(newMin, max));

			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}
		}
		this.getServletContext().getRequestDispatcher("/Prime.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
