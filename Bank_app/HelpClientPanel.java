import javax.swing.*;
import java.awt.*;

public class HelpClientPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    public HelpClientPanel(AppFrame mparent, Client mclient) {
        super();
        parent = mparent;
        client = mclient;
        dict = parent.dict;
        initialize();
    }

    private void initialize() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.02;

        PageButtonsPanel pageButPan = new PageButtonsPanel(parent, client);
        c.gridx = 0;
        c.gridy = 0;
        this.add(pageButPan, c);

        WhiteButton updateInfoButton = new WhiteButton("<html>" + dict.getText("update_data_1") +
        "<br />" + dict.getText("update_data_2") +"</html>");
        updateInfoButton.addActionListener(e -> AppDialog.wipDialog(parent));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        this.add(updateInfoButton, c);

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.5;

        JLabel phoneLabel = new JLabel(
                "<html><center>"+ dict.getText("contact_us_1") +"<br />" +
                dict.getText("contact_us_2")+" <b style='color:red'>123 456 789</b><br />" +
                dict.getText("contact_us_3")+"</center></html>",
                SwingConstants.CENTER);
//        TODO: Numer przypisanego pracownika?
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridx = 0;
        c.gridy = 2;
        this.add(phoneLabel, c);

        JLabel placeLabel = new JLabel(
                "<html><center>" + dict.getText("real_meeting") + "<br />" +
                dict.getText("find_closest_place")+ "</center></html>",
                SwingConstants.CENTER);
        placeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        c.gridx = 0;
        c.gridy = 3;
        this.add(placeLabel, c);

        WhiteButton findPlaceButton = new WhiteButton(dict.getText("search"));
        findPlaceButton.addActionListener(e -> AppDialog.wipDialog(parent));
//        TODO: Adres przypisanego pracownika?
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.gridy = 4;
        this.add(findPlaceButton, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        JLabel emailLabel = new JLabel(
                "<html><center>"+ dict.getText("app_comments_1") +"<br />" +
                dict.getText("app_comments_2") +"<b style='color:red'>app@casadepapel.com</b></center></html>",
                SwingConstants.CENTER);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        c.gridx = 0;
        c.gridy = 5;
        this.add(emailLabel, c);
    }
}
