import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
 
 
public class Mail
{
    public void send(String recipient, HashMap<String, String> mail_info) {

        final String username = "casa.de.papel.pap@gmail.com";
        final String password = "casadepapel8";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("casa.de.papel.pap@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );
            message.setSubject(mail_info.get("title"));
            message.setText(mail_info.get("msg"));

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}