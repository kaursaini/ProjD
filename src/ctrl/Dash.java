package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;
import model.StudentBean;

@WebServlet("/Sis.do")
public class Sis extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public Sis()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getParameter("calc") == null)
		{

		} else
		{
			Engine model = Engine.getInstance();

			String prefix = request.getParameter("prefix");
			String minGpa = request.getParameter("minGpa");
			String sortBy = request.getParameter("sortBy");

			try
			{
				System.out.println("doSis");
				List<StudentBean> result = model.doSis(prefix, minGpa);
				System.out.println(" end doSis");
				request.setAttribute("result", result);
			} catch (Exception e)
			{
				request.setAttribute("error", e.getMessage());
			}

			request.setAttribute("prefix", prefix);
			request.setAttribute("minGpa", minGpa);
			request.setAttribute("sortBy", sortBy);
		}

		this.getServletContext().getRequestDispatcher("/Sis.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
