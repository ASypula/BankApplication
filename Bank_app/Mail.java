import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import javax.swing.*;
 
 
public class Mail
{
    final static String username = "casa.de.papel.pap@gmail.com";
    final static String password = "casadepapel8";
    private final AppFrame owner;
    private Dictionary dict;

    public Mail(AppFrame mowner) {
        owner = mowner;
        dict = owner.dict;
    }

    public void send(String recipient, HashMap<String, String> mail_info, boolean advert) {
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

        try {

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

        } catch (MessagingException e) {
            wrongMailDialog();
        }
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

    private void wrongMailDialog() {
        AppDialog wrongLoginDialog = new AppDialog(owner, dict.getText("invalid_data"), 160, 150);
        JLabel wrongLoginText = new JLabel(
                "<html><div style='text-align: center;'>" +
                        dict.getText("wrong_login_1")+ "<br />ID "+
                        dict.getText("wrong_login_2") + "<br />" +
                        dict.getText("wrong_login_3") + "</html>",
                SwingConstants.CENTER
        );
        wrongLoginDialog.add(wrongLoginText);
        wrongLoginDialog.setVisible(true);
    }

}