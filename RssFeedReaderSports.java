import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Base64;


public class RssFeedReaderSports extends Thread {
	private URL[] scoreSites = new URL[5];
	private URL hockey;
	private URL basketball;
	private URL baseball;
	private URL football;
	private String[] currentWebResults = new String[4];
	
	public RssFeedReaderSports() {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DATE, -1);
		SimpleDateFormat urlForm = new SimpleDateFormat("yyyyMMdd");
		String date = urlForm.format(rightNow.getTime());
		
		try {
			hockey = new URL("https://www.mysportsfeeds.com/api/feed/pull/nhl/latest/scoreboard.xml?fordate=" + date);
			basketball = new URL("https://www.mysportsfeeds.com/api/feed/pull/nba/latest/scoreboard.xml?fordate=" + date);
			baseball = new URL("https://www.mysportsfeeds.com/api/feed/pull/mlb/latest/scoreboard.xml?fordate=" + date);
			football = new URL("https://www.mysportsfeeds.com/api/feed/pull/nfl/latest/scoreboard.xml?fordate=" + date);
			
		} catch (MalformedURLException e) {
		}
		
		scoreSites[0] = hockey;
		scoreSites[1] = basketball;
		scoreSites[2] = baseball;
		scoreSites[3] = football;
	}
	
	public void run() {
		String[] leagues = {"NHL", "NBA", "MLB", "NFL"};
		
		for (int i = 0; i < 4; i++) {
			String data = this.pullData(scoreSites[i]);
			currentWebResults[i] = this.formatData(data, leagues[i]);
		}
	}
	
	public String[] getResults() {
		return currentWebResults;
	}
	
	public String pullData(URL u){
		URL url = u;
		String data = ""; 
		try {
			byte[] encodedBytes = Base64.getEncoder().encode("aumio.islam:orchid2015".getBytes());
			String encoding = new String(encodedBytes);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty  ("Authorization", "Basic " + encoding);
			InputStream content = (InputStream)connection.getInputStream();
			BufferedReader reader = new BufferedReader (new InputStreamReader (content));
			String line;
			while ((line = reader.readLine()) != null) {
				data += line;
			}
		} catch(Exception e) {
		}
		return data;
	}
	
	public String formatData(String data, String league) {
		if (!data.contains("gameScore")) return "<br><u><font face=Lucida size=3>" + league + "</u>: no games</font><p>";
		data = data.substring(data.indexOf("Score>"));
		String[] dataArr = data.split("<scor:gameScore>");
		String formattedData = "<br><u><font face=Lucida size=3>" + league + " Scores</u>:</font><br><br>";
		String formatTeam = "%1$-25s";
		
		for (String d : dataArr) {
			
			String awayData = d.substring(d.indexOf("awayTeam"), d.indexOf("</scor:awayTeam>"));
			String awayTeam = awayData.substring(awayData.indexOf("City>") + 5, awayData.indexOf("<", awayData.indexOf("City>") + 5)) 
					+ " " + awayData.substring(awayData.indexOf("Name>") + 5, awayData.indexOf("<", awayData.indexOf("Name>") + 5));
			String awayScore = d.substring(d.indexOf("awayScore>") + 10, d.indexOf("<", d.indexOf("awayScore>") + 10));
			
			String homeData = d.substring(d.indexOf("homeTeam"), d.indexOf("</scor:homeTeam>"));
			String homeTeam = homeData.substring(homeData.indexOf("City>") + 5, homeData.indexOf("<", homeData.indexOf("City>") + 5)) 
					+ " " + homeData.substring(homeData.indexOf("Name>") + 5, homeData.indexOf("<", homeData.indexOf("Name>") + 5));
			String homeScore = d.substring(d.indexOf("homeScore>") + 10, d.indexOf("<", d.indexOf("homeScore>") + 10));
			
			
			String awayLine = String.format(formatTeam, awayTeam) + awayScore;
			String homeLine = String.format(formatTeam, homeTeam) + homeScore;
			
			//formattedData += "<font face=Lucida>" + awayLine +  homeLine + "<br></font>";
			
			
			
			formattedData += formatScore(awayLine, homeLine) + "<br>";
		}
		return formattedData;
	}
	
	/*
	public String verticalAlign(String partOne, String partTwo) {
		return "<td><table cellpadding=10 cellspacing=0 width=100%><tr><td align=left>" + partOne + "</td><td align=right>" + partTwo + "</td></tr></table></td>";
	}
	*/
	
	/*
	public String formatScore(String awayTeam, String awayScore, String homeTeam, String homeScore) {
		return "<table style=width:75% max-width:250><tr><td align=left>" + awayTeam + "</td><td align=right>" + awayScore + "</td></tr><tr><td align=left>" + homeTeam + "</td><td align=right>" + homeScore + "</td></tr></table>";
	}
	*/
	
	
	public String formatScore(String awayLine, String homeLine) {
		return "<pre>" + awayLine + "<br>" + homeLine + "</pre><p>";
	}

}
