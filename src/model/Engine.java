package model;

//import java.io.BufferedReader;
//import java.io.InputStreamReader;
import java.math.BigInteger;
//import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Engine
{

	private static Engine instance = null;
	private static String DRONE_API_KEY = "AIzaSyDZVFQFxO0GIglj1s0AXV-GVAGgqYb29FA";
	private static String RIDE_API_KEY = "AIzaSyAmz7Yqe3lBXS7Su2eUOr9MfYNEmH65_UQ";

	public static final String GEO_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
	public static final String DIST_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";

	public static final int EARTH_DIAMETER = 12742;
	public static final double CONVERT_TO_RADIANS = Math.PI / 180.0;
	public static final double DRONE_SPEED_KMH = 150.0;
	public static final double SECONDS_IN_MIN = 60.0;
	public static final double COST_PER_MINUTE = 0.5;
	private static DecimalFormat df2 = new DecimalFormat(".##");
	private StudentDAO dao;

	private Engine()
	{
	}

	public synchronized static Engine getInstance()
	{
		if (instance == null)
			instance = new Engine();
		return instance;
	}

	public String doPrime(String min, String max) throws Exception
	{

		long lowerLong = 0l;
		long upperLong = 0l;

		try
		{
			lowerLong = Long.parseLong(min);
			upperLong = Long.parseLong(max);
		} catch (NumberFormatException e)
		{
			throw new Exception("Invalid entires.");
		}

		if (upperLong < 0)
			throw new Exception("end < 0: " + upperLong);
		else if (lowerLong < 0)
			throw new Exception("start < 0: " + lowerLong);
		else if (lowerLong > upperLong)
			throw new Exception("No more primes in range");

		BigInteger primeInt;

		primeInt = new BigInteger(min);
		primeInt = primeInt.nextProbablePrime();

		if (primeInt.longValue() > Long.parseLong(max))
			throw new Exception("No more primes in range.");

		return primeInt.toString();
		/*
		 * try { int minInteger = Integer.parseInt(min); int maxInteger =
		 * Integer.parseInt(max); List<Integer> primes = new ArrayList<>(); // loop
		 * through the numbers one by one for (int i = minInteger + 1; i <= maxInteger;
		 * i++) { boolean isPrimeNumber = true; // check to see if the number is prime
		 * for (int j = 2; j < i; j++) { if (i % j == 0) { isPrimeNumber = false; break;
		 * // exit the inner for loop } } // add it to list the number if prime if
		 * (isPrimeNumber) { primes.add(i); } } return primes.remove(0) + "";
		 * 
		 * 
		 * } catch (NumberFormatException e) { throw new Exception("Invalid Entries!");
		 * } catch (IndexOutOfBoundsException e) { throw new
		 * Exception("No more primes in range."); }
		 */
	}

	/*
	 * public String doGps(String lat1, String lon1, String lat2, String lon2)
	 * throws Exception { try {
	 * 
	 * int latitude1 = Integer.parseInt(lat1); int longitude1 =
	 * Integer.parseInt(lon1); int latitude2 = Integer.parseInt(lat2); int
	 * longitude2 = Integer.parseInt(lon2); double theta = longitude1 - longitude2;
	 * double dist = Math.sin(deg2rad(latitude1)) * Math.sin(deg2rad(latitude2)) +
	 * Math.cos(deg2rad(latitude1)) * Math.cos(deg2rad(latitude2)) *
	 * Math.cos(deg2rad(theta)); dist = Math.acos(dist); dist = rad2deg(dist); dist
	 * = dist * 60 * 1.1515 * 1.609344; return
	 * NumberFormat.getIntegerInstance().format(Math.round(dist)) + " km"; } catch
	 * (Exception e) { throw new Exception("Empty String"); } }
	 * 
	 * 
	 * private static double deg2rad(double deg) { return (deg * Math.PI / 180.0); }
	 * private static double rad2deg(double rad) { return (rad * 180 / Math.PI); }
	 */

	public double doGps(String startLat, String startLong, String destLat, String destLong) throws Exception
	{
		try
		{
			double lat1 = Double.parseDouble(startLat) * CONVERT_TO_RADIANS;
			double long1 = Double.parseDouble(startLong) * CONVERT_TO_RADIANS;
			double lat2 = Double.parseDouble(destLat) * CONVERT_TO_RADIANS;
			double long2 = Double.parseDouble(destLong) * CONVERT_TO_RADIANS;

			double Y = Math.cos(lat1) * Math.cos(lat2);
			double X = Math.pow(Math.sin((lat2 - lat1) / 2), 2.0) + Y * Math.pow(Math.sin((long2 - long1) / 2), 2.0);

			double distance = EARTH_DIAMETER * Math.atan2(Math.sqrt(X), Math.sqrt(1 - X));

			return distance;
		} catch (Exception e)
		{
			throw new Exception("Empty String");
		}
	}

	public double doDrone(String startAddr, String destAddr) throws Exception
	{

		String[] startLatLng, destLatLng;

		if (startAddr.isEmpty() || destAddr.isEmpty())
			throw new IllegalArgumentException("One or more addresses are empty");
		if (startAddr.contains("-") || destAddr.contains("-"))
			throw new Exception("String index out of range");
		try
		{
			String formatStart = startAddr.replaceAll(" ", "+").trim();
			String formatDest = destAddr.replaceAll(" ", "+").trim();

			URL startURL = new URL(GEO_URL + "address=" + formatStart + "&key=" + DRONE_API_KEY);
			URL destURL = new URL(GEO_URL + "address=" + formatDest + "&key=" + DRONE_API_KEY);

			String startJson = accessJson(startURL);
			String destJson = accessJson(destURL);

			startLatLng = parseLatLng(startJson);
			destLatLng = parseLatLng(destJson);

			double distance = doGps(startLatLng[0], startLatLng[1], destLatLng[0], destLatLng[1]);

			double droneTime = (distance / DRONE_SPEED_KMH) * SECONDS_IN_MIN;

			return droneTime;
		} catch (Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}

	private String accessJson(URL httpAddr) throws Exception
	{

		URLConnection httpConnection = httpAddr.openConnection();

		Scanner httpInput = new Scanner(httpConnection.getInputStream());
		String result = "";

		while (httpInput.hasNextLine())
		{
			result += httpInput.nextLine();
		}

		httpInput.close();

		return result.toString();
	}

	private String[] parseLatLng(String json)
	{

		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(json).getAsJsonObject();

		JsonObject resultObj = rootObj.getAsJsonArray("results").get(0).getAsJsonObject();
		JsonObject locObj = resultObj.getAsJsonObject("geometry").getAsJsonObject("location");

		String lat = locObj.get("lat").getAsString();
		String lng = locObj.get("lng").getAsString();

		String[] latLng =
		{ lat, lng };

		return latLng;

	}

	public String doRide(String startAddr, String destAddr) throws Exception
	{
		if (startAddr.isEmpty() || destAddr.isEmpty())
			throw new IllegalArgumentException("One or more addresses are empty");
		if (startAddr.contains("-") || destAddr.contains("-"))
			throw new Exception("String index out of range");

		try
		{

			String formatStart = startAddr.replaceAll(" ", "+").trim();
			String formatDest = destAddr.replaceAll(" ", "+").trim();

			URL rideURL = new URL(DIST_URL + "origins=" + formatStart + "&destinations=" + formatDest
					+ "&departure_time=now&key=" + RIDE_API_KEY);

			String rideJson = accessJson(rideURL);
			int time = parseTime(rideJson);

			double cost = ((double) time / SECONDS_IN_MIN) * COST_PER_MINUTE;

			return "$ " + df2.format(cost);
		} catch (Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}

	private int parseTime(String rideJson)
	{
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(rideJson).getAsJsonObject();

		JsonObject rowObj = rootObj.getAsJsonArray("rows").get(0).getAsJsonObject();

		JsonObject durationObj = rowObj.getAsJsonArray("elements").get(0).getAsJsonObject()
				.getAsJsonObject("duration_in_traffic");

		int trafficSeconds = durationObj.get("value").getAsInt();

		return trafficSeconds;
	}

	public List<StudentBean> doSis(String prefix, String minGpa) throws Exception
	{
		try
		{
			System.out.println("engine.java : dao.retrive start");
			System.out.println(prefix + minGpa);
			List<StudentBean> students = dao.retrieve(prefix, minGpa);
			System.out.println("engine.java : dao.retrive end");
			return students;
		} catch (Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}
}
