import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class City {
	private String country, provState, cityName;
	private URL url;
	private static ArrayList<URL> cityLinks = new ArrayList<URL>();
	private static ArrayList<City> weatherCities = new ArrayList<City>();
	
	public City(String c, String p, String n) throws MalformedURLException {
		this.setCountry(c.substring(0, 1).toUpperCase() + c.substring(1));
		this.setProvState(p.substring(0, 1).toUpperCase() + p.substring(1));
		this.setCityName(n.substring(0, 1).toUpperCase() + n.substring(1));
		
		this.url = this.toURL();
		if (!cityLinks.contains(this.url)) cityLinks.add(this.url);
		
		if (!weatherCities.contains(this)) weatherCities.add(this);
	}
	
	public URL toURL() throws MalformedURLException {
		String address = "https://www.yr.no/place/";
		String temp = getCountry().replaceAll(" ", "_");
		address += temp;
		temp = provState.replaceAll(" ", "_");
		address += "/" + temp;
		temp = getCityName().replaceAll(" ", "_");
		if (getCountry().equals("Norway")) {
			address += "/" + temp + "/" + temp + "/forecast.xml";
		} else {
			address += "/" + temp + "/forecast.xml";
		}
		
		return new URL(address);
	}
	
	public boolean equals(City c) {
		return this.getCountry().equals(c.getCountry()) && this.provState.equals(c.provState) && this.getCityName().equals(c.getCityName());
	}
	
	public String toString() {
		return getCityName() + ", " + getCountry();
	}
	
	public String toStringIO() {
		return getCityName() + "," + getProvState() + "," + getCountry();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getProvState() {
		return provState;
	}
	
	public void setProvState(String p) {
		this.provState = p;
	}
	
	public static ArrayList<City> getCities() {
		return weatherCities;
	}
}
