package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO
{

	private static final String DERBY_DRIVER = "org.apache.derby.jdbc.ClientDriver";
	private static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	private static final String QUERY = "SELECT * FROM SIS WHERE SURNAME LIKE ? AND GPA >= ?";

	public List<StudentBean> retrieve(String name, String gpa) throws Exception
	{
		System.out.println("1");
		Class.forName(DERBY_DRIVER).newInstance();
		System.out.println("2");
		Connection con = DriverManager.getConnection(DB_URL);
		System.out.println("3");
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");
		System.out.println("4");
		PreparedStatement preS = con.prepareStatement(QUERY);
		
		System.out.println("name");
		if (!name.isEmpty())
			preS.setString(1, name);
		else
			preS.setString(1, "%");
		
		System.out.println("gpa");
		if (!gpa.isEmpty())
			preS.setString(2, gpa);
		else
			preS.setString(2, "0");
		System.out.println("sortby");
		
		System.out.println("query     " + QUERY);
		
		System.out.println("pres     " + preS.toString());
		
		System.out.println("execution");
		
		ResultSet r = preS.executeQuery();
		
		List<StudentBean> studentList = new ArrayList<>();
		System.out.println("list");
		
		while (r.next())
		{
			StudentBean student = new StudentBean();
			student.setCourses(r.getInt("COURSES"));
			student.setGpa(r.getDouble("GPA"));
			student.setMajor(r.getString("MAJOR"));
			student.setName(r.getString("SURNAME") + ", " + r.getString("GIVENNAME"));

			System.out.println(student.toString());

			studentList.add(student);
		}

		return studentList;

	}
}
