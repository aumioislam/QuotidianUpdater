import java.util.ArrayList;


public class User {
	private boolean updateHockey, updateBasketball, updateFootball, updateBaseball;
	private boolean[] choices = {updateHockey, updateBasketball, updateBaseball, updateFootball};
	private ArrayList<City> userCityList = new ArrayList<City>();
	public static ArrayList<User> users = new ArrayList<User>();
	private String username, password, email;
	
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		users.add(this);
	}
		
	public String getPassword() {
		return this.password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public static User searchUsers(String username) {
		for (User u : users) {
			if (u.username.equals(username)) return u;
		}
		return null;
	}
	
	public boolean equals(User u) {
		return this.username.equals(u.username);
	}
	
	public void addCity(City c) {
		if (!this.userCityList.contains(c)) this.userCityList.add(c);
	}
	
	public void removeCity(City c) {
		for (int i = 0; i < this.userCityList.size(); i++) {
			if (this.userCityList.get(i).equals(c)) this.userCityList.remove(i);
		}
	}
	
	public void setSportPref(int i, boolean b) {
		this.choices[i] = b; 
	}
	
	public String toString() {
		String user = username + "%" + password + "%" + email + "%"; 
		
		for (boolean b : choices) {
			if (b) user += "1"; else user += "0";
			user += "-";
		} user += "%";
		
		for (City c : userCityList) {
			user += c.toStringIO() + "-";
		}
		
		return user;
	}
	
	public static ArrayList<User> getUserList() {
		return users;
	}
	
	public boolean[] getChoices() {
		return this.choices;
	}
	
	public ArrayList<City> getUserCities() {
		return this.userCityList;
	}
}
