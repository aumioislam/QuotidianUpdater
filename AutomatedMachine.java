import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

public class AutomatedMachine implements Runnable {
	private RssFeedReaderWeather rW;
	private RssFeedReaderSports rS;
	private EmailMachine eM;
	private BufferedReader reader;
	private final String FILEPATH  = "Users//userData.txt";
	
	public void run() {
		
		try {
			reader = new BufferedReader(new FileReader(FILEPATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line;
		
		try {
			while ((line = reader.readLine()) != null) {
				String[] userData = line.split("%");
				User u = new User(userData[0], userData[1], userData[2]); //create user
				String[] choices = userData[3].split("-"); //setting user preferences for sports
				for (int i = 0; i < 4; i++) {
					boolean temp = false;
					if (choices[i].equals("1")) temp = true;
					u.setSportPref(i, temp);
				}
				
				if (userData.length == 5) {
					String[] cities = userData[4].split("-"); //adding cities for users weather
					for(String c : cities) {
						u.addCity(new City(c.split(",")[2], c.split(",")[1], c.split(",")[0]));
					}
				}
			}
		} catch (MalformedURLException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		} try {
			reader.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		rW = new RssFeedReaderWeather();
		rS = new RssFeedReaderSports();
		
		rW.start();
		rS.start();
		
		while (rW.isAlive() || rS.isAlive());
		
		eM = new EmailMachine(User.getUserList(), rS.getResults(), rW.getData());
		eM.run();
	}
	
	public static void main(String[] args) {new AutomatedMachine().run();}
}
