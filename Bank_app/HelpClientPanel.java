import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class HelpClientPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    public HelpClientPanel(AppFrame mparent, Client mclient) {
        super();
        parent = mparent;
        client = mclient;
        dict = parent.dict;
        try {
            initialize();
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        } catch (WrongId e) {
        	System.err.format(e.getMessage());
		}
    }

    private void initialize() throws SQLException, WrongId {
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

        WhiteButton updateInfoButton = new WhiteButton("<html><center>" + dict.getText("update_data_1") +
                "<br />" + dict.getText("update_data_2") +"</center></html>");
        updateInfoButton.addActionListener(e -> new UpdateDataDialog(parent, client));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        this.add(updateInfoButton, c);

        WhiteButton mailOfferButton = new WhiteButton("<html><center>" + dict.getText("mail_offer_receive_1") +
                "<br />"+dict.getText("mail_offer_receive_2")+"</center></html>");
        mailOfferButton.addActionListener(e -> AppDialog.emailDialog(parent));
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.gridx = 0;
        c.gridy = 1;
        this.add(mailOfferButton, c);

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.5;

        JLabel phoneLabel = new JLabel(
                "<html><center>"+ dict.getText("contact_us_1") +"<br />" +
                    dict.getText("contact_us_2")+" <b style='color:red'>" +
                    client.getEmployee().getPhone_no() + "</b><br />" +
                    dict.getText("contact_us_3")+"</center></html>",
                SwingConstants.CENTER);

        phoneLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        c.gridx = 0;
        c.gridy = 2;
        this.add(phoneLabel, c);

        JLabel placeLabel = new JLabel(
                "<html><center>" + dict.getText("real_meeting") + "<br />" +
                    dict.getText("find_closest_place")+ "</center></html>",
                SwingConstants.CENTER);
        placeLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        c.gridx = 0;
        c.gridy = 3;
        this.add(placeLabel, c);

        WhiteButton findPlaceButton = new WhiteButton(dict.getText("search"));
        findPlaceButton.addActionListener(e -> {
            try {
                AppDialog.branchDetailsDialog(parent, client.getEmployee().getBranch());
            } catch (SQLException ex) {
                System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            } catch (WrongId ex) {
                System.err.format(ex.getMessage());
            }
        });

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
        emailLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        c.gridx = 0;
        c.gridy = 5;
        this.add(emailLabel, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridy = 6;
        c.weighty = 0.02;
        c.gridheight = 1;
        this.add(new LanguageButtonsPanel(parent, "helpClient", client), c);
    }
}
