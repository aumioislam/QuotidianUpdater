 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;

import org.apache.commons.validator.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;





public class UserInterface extends Application {
		private final String FILEPATH = "Users//userData.txt";
		private BufferedWriter writer;
		private BufferedReader reader;
		
		private Scene login, userSettings;
		private GridPane loginGrid;
		private Text loginText;
		private Label username, password;
		private TextField userField;
		private PasswordField passwordField;
		private Button loginBtn, registerBtn;
		
		@Override
		public void init() throws Exception {
			//creating all saved user objects
			reader = new BufferedReader(new FileReader(FILEPATH));
			String line;
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
			} reader.close();
			
			
			//initializing elements for the UI
			loginGrid = new GridPane(); //Grid layout for the login screen
				loginGrid.setAlignment(Pos.TOP_CENTER); //set grid in the top center of the ui
				loginGrid.setHgap(10); //horizontal gap between elements 
				loginGrid.setVgap(10); //vertical gap between elements
				loginGrid.setPadding(new Insets(50,25,25,25)); //padding between start of grid and where I want the 
															   //login elements to be
			loginText = new Text("Welcome"); //creating text
			loginText.setFont(Font.font("Georgia", 20)); //setting font
			final Text error = new Text();
			error.setFill(Color.FIREBRICK);
			
			username = new Label("Username: ");
			password = new Label("Password: ");
			userField = new TextField();
			passwordField = new PasswordField();
			
			loginBtn = new Button("Login");
			loginBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e){
					error.setText(null);
					User u = User.searchUsers(userField.getText());
					if (u == null) {
						error.setText("That username does not exist.");
					} else if (!u.getPassword().equals(passwordField.getText())) {
						error.setText("Username or password is incorrect.");
					} else {
						showUserScreen(u);
					}
					
				}
			});
			
			registerBtn = new Button("Register");
			registerBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					showRegistration();
				}
			});
			
			HBox hb = new HBox(10);
			hb.setAlignment(Pos.BOTTOM_RIGHT);
			hb.getChildren().add(registerBtn);
			hb.getChildren().add(loginBtn);
			
			loginGrid.add(loginText, 0, 0, 2, 1);
			loginGrid.add(username, 0, 1);
			loginGrid.add(userField, 1, 1);
			loginGrid.add(password, 0, 2);
			loginGrid.add(passwordField, 1, 2);
			loginGrid.add(error, 0, 4);
			loginGrid.add(hb, 1, 4);
			loginGrid.setId("login");
			
		}
		
		@Override
		public void start(Stage stage) throws Exception {
			//creating login screen
			login = new Scene(loginGrid, 400, 250);
			
			login.getStylesheets().addAll(this.getClass().getClassLoader().getResource("style.css").toExternalForm());
			
			stage.setTitle("Quotidian Updater");
			//stage.setResizable(false);
			
			stage.setScene(login);
			stage.show();
			
		}
		
		@Override
		public void stop() throws Exception{
			//writes all user data back to the file;
			writer = new BufferedWriter(new FileWriter(FILEPATH));
			for (User u : User.getUserList()) {
				writer.write(u.toString() + "\n");
			}
			writer.close();
		}
		
		public void showRegistration() {
			Stage register = new Stage();
			GridPane grid = new GridPane();
				grid.setAlignment(Pos.TOP_CENTER);
				grid.setHgap(10);
				grid.setVgap(15);
				grid.setPadding(new Insets(10, 5, 5, 0));
			
			Text text = new Text("Registration");
			text.setFont(Font.font("Georgia", 15));
			Label user = new Label("Username:");
			Label pw = new Label("Password:");
			Label pwTwo = new Label("Confirm:");
			Label email = new Label("Email Address:");
			
			TextField uF = new TextField();
			PasswordField pF = new PasswordField();
			PasswordField pFTwo = new PasswordField();
			TextField eF = new TextField();
			
			final Text error = new Text();
			error.setFill(Color.FIREBRICK);
			
			Button registerBtn = new Button("Register");
			registerBtn.setOnAction(new EventHandler<ActionEvent>() {
				
				@SuppressWarnings("deprecation")
				@Override public void handle(ActionEvent e) {
					error.setText("");
					if ((User.searchUsers(uF.getText())) != null) { //if username exists
						error.setText("That username is taken.");
					} else if (uF.getText().matches("")) {
						error.setText("Pleaes enter a username.");
					} else if(!pF.getText().matches("^[a-zA-Z0-9]{8,}$")) { //if password does not meet requirements
						error.setText("Password must be 8 digit alphanumeric.");
					} else if (!pF.getText().equals(pFTwo.getText())) {
						error.setText("Passwords do not match.");
					} else {
						EmailValidator validator = EmailValidator.getInstance();
						if (validator.isValid(eF.getText())) {
							new User(uF.getText(), pF.getText(), eF.getText());
							register.close();
						} else {
							error.setText("Please enter a valid email address.");
						}
					
					}
					
				}
			});
			
			grid.add(text, 0, 0, 2, 1);
			grid.add(user, 0, 1);
			grid.add(uF, 1, 1);
			grid.add(pw, 0, 2);
			grid.add(pF, 1, 2);
			grid.add(pwTwo, 0, 3);
			grid.add(pFTwo, 1, 3);
			grid.add(email, 0, 4);
			grid.add(eF, 1, 4);
			grid.add(error, 0, 5);
			grid.add(registerBtn, 1, 5);
			
			Scene scene = new Scene(grid, 400, 250);
			register.setScene(scene);
			register.setResizable(false);
			register.initModality(Modality.APPLICATION_MODAL);
			register.show();
		}
		
		public void showUserScreen(User u) {
			boolean[] choices = u.getChoices();
			Stage userScreen = new Stage(); 
			GridPane grid = new GridPane();
				grid.setAlignment(Pos.CENTER_LEFT);
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(10, 5, 10, 5));
			
			Text sports = new Text("Sport Preferences:");
				sports.setFont(Font.font("Georgia", 15));
				
			Text weather = new Text("Weather Preferences:");
				weather.setFont(Font.font("Georgia", 15));
			
			ToggleButton hockey = new ToggleButton("Hockey");
				hockey.setUserData(0);
				hockey.setSelected(choices[0]);
				hockey.selectedProperty().addListener((p, o, n) -> {
					u.setSportPref(0, p.getValue());
				});
				
			ToggleButton basketball = new ToggleButton("Basketball");
				basketball.setUserData(1);
				basketball.setSelected(choices[1]);
				basketball.selectedProperty().addListener((p, o, n) -> {
					u.setSportPref(1, p.getValue());
				});
				
			ToggleButton baseball = new ToggleButton("Baseball");
				baseball.setUserData(2);
				baseball.setSelected(choices[2]);
				baseball.selectedProperty().addListener((p, o, n) -> {
					u.setSportPref(2, p.getValue());
				});
				
			ToggleButton football = new ToggleButton("Football");
				football.setUserData(3);
				football.setSelected(choices[3]);
				football.selectedProperty().addListener((p, o, n) -> {
					u.setSportPref(3, p.getValue());
				});
				
			VBox vb = new VBox();
				vb.setAlignment(Pos.CENTER_LEFT);
				vb.getChildren().add(hockey);
				vb.getChildren().add(basketball);
				vb.getChildren().add(baseball);
				vb.getChildren().add(football);
				
			ComboBox<City> combo = new ComboBox<City>();
				for (City c : u.getUserCities()) {
					combo.getItems().add(c);
					
				}
			
			Button add = new Button("Add City");
				add.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						Stage newCity = new Stage();
						Scene scene;
						GridPane grid = new GridPane();
							grid.setAlignment(Pos.TOP_CENTER);
							grid.setHgap(10);
							grid.setVgap(10);
							grid.setPadding(new Insets(10, 10, 10, 10));
							
						Label city = new Label("City:");
						Label region = new Label("Region:");
						Label country = new Label("Country:");
						
						TextField cityF = new TextField();
						TextField regionF = new TextField();
						TextField countryF = new TextField();
						
						final Text error = new Text("Case Sensitive.");
							error.setFill(Color.BLACK);
						
						Button addCity = new Button("Add");
							addCity.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent e) {
									error.setText("Case Sensitive");
									error.setFill(Color.BLACK);
									try {
										City c = new City(countryF.getText(), regionF.getText(), cityF.getText());
										RssFeedReaderWeather r = new RssFeedReaderWeather();
										r.readRssFeed(c);
										u.addCity(c);
										combo.getItems().add(c);
										newCity.close();
									} catch (Exception x) { //can catch improper url, or catch IO exception when object r tries to read the feed
										error.setFill(Color.FIREBRICK);
										error.setText("Invalid syntax.\nRefer to yr.no for proper syntax.");
									}
								}
							});
							
						grid.add(city, 0, 0, 2, 1);
						grid.add(region, 0, 1);
						grid.add(country, 0, 2);
						grid.add(cityF, 1, 0);
						grid.add(regionF, 1, 1);
						grid.add(countryF, 1, 2);
						grid.add(addCity, 1, 3);
						grid.add(error, 0, 4);
						
						scene = new Scene(grid, 250, 180);
						newCity.setScene(scene);
						newCity.setResizable(false);
						newCity.initModality(Modality.APPLICATION_MODAL);
						newCity.show();
					}
				});
				
			Button remove = new Button("Remove City");
				remove.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						City temp = combo.getValue();
						u.removeCity(temp);
						combo.getItems().remove(temp);
					}
				});
				
			
			HBox hb = new HBox();
				hb.setAlignment(Pos.BOTTOM_RIGHT);
				hb.getChildren().add(remove);
				hb.getChildren().add(add);
				
			VBox vbT = new VBox();
				vbT.setAlignment(Pos.CENTER_RIGHT);
				vbT.getChildren().add(combo);
				vbT.getChildren().add(hb);
				
			grid.add(sports, 0, 0, 2, 1);
			grid.add(vb, 0, 1);
			grid.add(weather, 4, 0);
			grid.add(vbT, 4, 1);
			
			userSettings = new Scene(grid, 300, 250);
			userScreen.setScene(userSettings);
			userScreen.setResizable(false);
			userScreen.initModality(Modality.APPLICATION_MODAL);
			userScreen.show();
			
			
		}
				
		public static void main(String[] args) {launch(args);}
		
}
