package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		https://api.openweathermap.org/data/2.5/weather?q=mumbai&appid=c61162a51846a2ec0982ee4165776817
		String apiKey = "c61162a51846a2ec0982ee4165776817";
		
		String city = request.getParameter("city");
		
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+ city + "&appid=" + apiKey;
		System.out.println(city);
		@SuppressWarnings("deprecation")
		URL url = new URL(apiUrl);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");
		
		InputStream inputStream = connection.getInputStream();
		System.out.println(inputStream + "-----------INP STREAM---------");
		
		InputStreamReader reader = new InputStreamReader(inputStream);
		System.out.println(inputStream + "-----------INP STREAM Reader---------");
		
		Scanner scanner = new Scanner(reader);
		StringBuilder responseContent = new StringBuilder();
		
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		scanner.close();
		
//		System.out.println(responseContent);
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
		
//		System.out.println(jsonObject);
		
		long dateTimeStamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTimeStamp).toString();
		
		double temInKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int tempCel = (int) (temInKelvin - 273.15);
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature",tempCel);
		request.setAttribute("condition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
