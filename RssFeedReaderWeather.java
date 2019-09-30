import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RssFeedReaderWeather extends Thread {
	private ArrayList<City> cityList = new ArrayList<City>();
	private Map<City, String> dataStorage;
	
	public RssFeedReaderWeather() {
		 this.cityList = City.getCities();
		 this.dataStorage = new HashMap<City, String>(cityList.size());
	}
	
	public void run() {
		for (City c : cityList) {
			dataStorage.put(c, this.readRssFeed(c));
		}
	}
	
	public Map<City, String> getData() {
		return dataStorage;
	}
	
	public String readRssFeed(City c) {
		String degCelsius = "\u00b0" + "C";
		String formattedData = "<br><u><font face=Lucida size=3>" + c.getCityName() + ", " + c.getCountry() + ":</font></u><br><br>";
		String data = "";
		
		
		try {
			URL rssUrl = c.toURL();
			BufferedReader reader = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
			String line;
			boolean currentWeather = false;
			
			while((line = reader.readLine()) != null) {
				
				if (line.contains("sun")) data += line + "\n";
				
				if (line.contains("<time from")) currentWeather = true;
			
				if (currentWeather) data += line + "\n";
				
				if (line.contains("</time>")) break;
				
			}
			
		} catch (IOException e) {
		}
		
		String[] linesOfData = data.split("\n");
		for (int i = 0; i < linesOfData.length; i++) linesOfData[i] = linesOfData[i].replaceAll(".*<", "");
		
		String[] rawData = new String[linesOfData.length];
		
		for (int i = 0; i < linesOfData.length; i++) {
			rawData[i] = this.formatData(linesOfData[i]);
		}
		
		formattedData += "<font face=Lucida>Conditions: " + rawData[3] + "</font><br>";
		formattedData += "<font face=Lucida>Precipitation: " + rawData[4] + " expected</font><br>";
		formattedData += "<font face=Lucida>Wind: A " + rawData[7].split(" ")[1].toLowerCase().replace("-", " ") + " to the " + rawData[6].toLowerCase() + " (" + rawData[7].split(" ")[0].replace("-", " ") + ")</font><br>";
		formattedData += "<font face=Lucida>Temperature: " + rawData[8] + degCelsius + "<br>";
		formattedData += "<font face=Lucida>Pressure: " + rawData[9].split(" ")[1] + " " + rawData[9].split(" ")[0] + "</font><br>";
		formattedData += "<font face=Lucida>Sunrise: " + rawData[0].split(" ")[0].replace("-", " ") + " | Sunset: " + rawData[0].split(" ")[1].replace("-", " ") + "</font><br><p>";
		
		return formattedData;
	}
	
	public String formatData(String d) {
		
		if (d.contains("sun")) {
			String[] dArr = d.split(" ");
			
			String sunRise = dArr[1].split("T")[1];
			sunRise = twentyFourToTwelve(sunRise);
			
			String sunSet = dArr[2].split("T")[1];
			sunSet = twentyFourToTwelve(sunSet);
			
			return sunRise + " " + sunSet;
			
			
			
			
		} else if (d.contains("symbol number")) {
			String weather = d.substring(d.indexOf("name=\"") + 6, d.indexOf("\"", d.indexOf("name=\"") + 6));
			return weather;
			
		} else if (d.contains("precipitation")) return d.split(" ")[1].replaceAll("[^\\d|^.]", "") + " mm"; 
		
		else if (d.contains("windDirection")) {
			d = d.replaceAll("\\w+=\"", "");
			d = d.replaceAll("\"", "");
			String[] dArr = d.split(" ");
			return dArr[3].replace(" ", "-");
			
		} else if (d.contains("windSpeed")) {
			d = d.replaceAll("\\w+=\"", "");
			d = d.replaceAll("\"", "");
			String[] dArr = d.split(" ");
			String d1 = d.replaceFirst("[\\d{1}][ ]", "*");
			d1 = d1.split("\\*")[1].substring(0, d1.split("\\*")[1].lastIndexOf(" "));
			d1 = d1.replaceAll(" ", "-");
			return mpsToKph(dArr[1]) + " " + d1;
			
		} else if (d.contains("temp")) {
			String[] dArr = d.split(" ");
			String weather = dArr[2].replaceAll("\\w+=\"", "");
			return weather.replaceAll("\"", "");
			
		} else if (d.contains("pressure")) {
			String[] dArr = d.split(" ");
			String weather = dArr[1].replaceAll("\\w+=\"", "") + " " + dArr[2].replaceAll("\\w+=\"", "");
			return weather.replaceAll("\"", "");
			
		} return null;
	}
	
	public static String mpsToKph(String s) {
		return String.format("%.2f", Double.parseDouble(s) * 3.6) + "-kph";
	}
	
	public static String twentyFourToTwelve(String time) {
		String[] tArr = time.split(":");
		String dayTime = null;
		if (Integer.parseInt(tArr[0]) > 12) {
			tArr[0] = Integer.toString(Integer.parseInt(tArr[0]) - 12);
			dayTime = "PM";
		} else {
			dayTime = "AM";
		}
		
		return tArr[0] + ":" + tArr[1] + "-" + dayTime;
	}
	
}
