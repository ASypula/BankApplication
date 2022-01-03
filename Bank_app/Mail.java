import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
 
 
public class Mail
{
    final static String username = "casa.de.papel.pap@gmail.com";
    final static String password = "casadepapel8";

    public static void send(String recipient, HashMap<String, String> mail_info, boolean advert, Dictionary dict) throws MessagingException {
        // if advert is true then the mail is sent with automatic advert message

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

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("casa.de.papel.pap@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipient));
        message.setSubject(mail_info.get("title"));

        if (advert) {
            String content = advertMsg(dict);
            message.setContent(content, "text/html");
        } else {
            message.setContent("<h2>" + mail_info.get("msg") + "</h2>", "text/html");
        }

        Transport.send(message);
    }
    
    public static String advertMsg(Dictionary dict) {
        String msg = "<body><h1>" + dict.getText("mail_msg_1") + "</h1>" +
        "<h2>" + dict.getText("mail_msg_2") + "</h2>" +
        "<h2>" + dict.getText("mail_msg_3") + dict.getText("mail_msg_4") +
        dict.getText("mail_msg_5") + "</h2>" +
        "<h2 style=\"color:#2E86C1;\">" + dict.getText("mail_msg_6") +
        dict.getText("mail_msg_7") + "</h2>" +
        "<h2>" + dict.getText("mail_msg_8") + "</h2>" +
        "<h2 style=\"color:#2B79DC;\">" + dict.getText("mail_msg_9");
        return msg;
    }

}