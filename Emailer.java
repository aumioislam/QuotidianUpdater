import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.PasswordAuthentication;

public class Emailer {
	private static final String HOST = "smtp.gmail.com";
	private static final String USER = "quotidianupdater@gmail.com";
	private static final String PASSWORD = "=b^eqdDwAU&;5+BqUFH>6vS!9{;/u]q?";
	
	public void sendEmail(String toAddr, String body) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DATE, 0);
		SimpleDateFormat header = new SimpleDateFormat("E, MMMMM d, yyyy");
		String date = header.format(rightNow.getTime());
		
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(USER, PASSWORD); 
			}
		}
		);
		
		try {
			Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(USER, "QuotidianUpdater"));
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
				message.setSubject("Your Quotidian Update, " + date);
				message.setText(body);
				message.setContent(body, "text/html");
				
			Transport.send(message);
	
		} catch (MessagingException | UnsupportedEncodingException e) {
			System.out.println("failed");
		}
	}
	

	
}
