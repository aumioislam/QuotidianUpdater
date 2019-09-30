import java.util.ArrayList;
import java.util.Map;

public class EmailMachine {
	private ArrayList<User> users;
	private String[] sportsData;
	private Map<City, String> weatherData;
	private Emailer e = new Emailer();
	
	EmailMachine(ArrayList<User> u, String[] sportsData, Map<City, String> weatherData) {
		this.users = u;
		this.sportsData = sportsData;
		this.weatherData = weatherData;
	}
	
	public void run() {
		for (User u : users) {
			e.sendEmail(u.getEmail(), this.composeMessage(u));	
		}
	}
	
	public String composeMessage(User u) {
		String message = "<i><font face =Georgia size=4>Good Morning,</font></i><br>";
		
		if (u.getUserCities() != null) {
			message += "<br><font face=Lucida size=3><u><b>Today's Weather:</b></u></font><p>";
			for (City c : u.getUserCities()) {
				message += weatherData.get(c);
			}
		}
		
		boolean[] choices = u.getChoices();
		
		for (int i = 0; i < 4; i++) {
			if (choices[i]) {
				if(!message.contains("Yesterday's Scores:")) message += "<p><br><font face =Lucida size=3><u><b>Yesterday's Scores</u></b>:</font><p>";
				message += sportsData[i];
			}
		}
		return message;
	}

}

