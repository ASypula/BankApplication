import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import javax.mail.*;

public class AppDialog extends JDialog {

    AppDialog(AppFrame owner, String title, int width, int height) {
        super(owner, title);
        this.getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setSize(width, height);
        this.setLocationRelativeTo(owner);
        this.getRootPane().setBackground(owner.bgColor);
        this.getContentPane().setBackground(owner.bgColor);
    }

    public static void wipDialog(AppFrame parent) {
//        TODO: Remove this when no longer necessary
        AppDialog wipDialog = new AppDialog(parent, "WIP", 100, 100);
        JLabel wip = new JLabel("WIP");
        wipDialog.add(wip);
        wipDialog.setVisible(true);
    }

    public static void wrongMailDialog(AppFrame parent, Dictionary dict) {
        AppDialog wrongLoginDialog = new AppDialog(parent, dict.getText("invalid_data"), 160, 150);
        JLabel wrongLoginText = new JLabel(
                "<html><div style='text-align: center;'>" +
                        dict.getText("wrong_mail_1")+ "<br />"+
                        dict.getText("wrong_mail_2") + "<br />" +
                        "</html>",
                SwingConstants.CENTER
        );
        wrongLoginDialog.add(wrongLoginText);
        wrongLoginDialog.setVisible(true);
    }

    public static void emailDialog(AppFrame parent) {
        Dictionary dict = parent.dict;

        AppDialog emailDialog = new AppDialog(parent, dict.getText("give_mail_address"), 250, 200);
        emailDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.5;

        JTextField emailTf = new JTextField();

        ActionListener okActionListener = e -> {
            String email = emailTf.getText();
            if (!email.isEmpty()) {
                HashMap<String, String> mail_info = new HashMap<String, String>();
                mail_info.put("title", dict.getText("mail_offer"));
                mail_info.put("msg", "Advert");
                try {
                    Mail.send(email, mail_info, true, dict);

                    emailDialog.setVisible(false);
                    emailDialog.dispatchEvent(
                            new WindowEvent(emailDialog, WindowEvent.WINDOW_CLOSING)
                    );
                } catch (MessagingException ex) {
                    wrongMailDialog(parent, dict);
                }
            }
        };

        JLabel emailLabel = new JLabel(dict.getText("mail_address"));
        c.gridx = 0;
        c.gridy = 0;
        emailDialog.add(emailLabel, c);

        emailTf.addActionListener(okActionListener);
        c.gridx = 0;
        c.gridy = 1;
        emailDialog.add(emailTf, c);

        OkCancelButtonsPanel okCanButPan = new OkCancelButtonsPanel(parent, emailDialog, okActionListener);
        c.gridx = 0;
        c.gridy = 2;
        emailDialog.add(okCanButPan, c);

        emailDialog.setVisible(true);
    }

    public static void contactEmployeeDialog(AppFrame parent) {
        Dictionary dict = parent.dict;
        AppDialog contactDialog = new AppDialog(parent, dict.getText("contact_employee"), 250, 250);
        JLabel contactLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                    dict.getText("contact_oper_impossible_1") + "<br />" +
                    dict.getText("contact_oper_impossible_2") + "<br />" +
                    dict.getText("contact_oper_impossible_3") + "<br />" +
                    dict.getText("contact_oper_impossible_4") + "<br />" +
                    dict.getText("contact_oper_impossible_5") + "<br />" +
                    dict.getText("contact_oper_impossible_6") + "<b>"+dict.getText("help")+"</b>.</div></html>",
                SwingConstants.CENTER
        );
        contactLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        contactDialog.add(contactLabel);
        contactDialog.setVisible(true);
    }

    private static String getHoursOrClosed(Branch branch, String day, Dictionary dict) {
        String hours = branch.getOpening_hours(day);
        if (hours == null)
            hours = "Zamknięte";
        return hours;
    }

    public static void branchDetailsDialog(AppFrame parent, Branch branch) throws SQLException, WrongId {
        Dictionary dict = parent.dict;
        AppDialog branchDialog = new AppDialog(parent, "Szczegóły oddziału", 250, 275);

        JPanel branchPanel = new JPanel();
        branchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        branchPanel.setBackground(parent.bgColor);
        branchPanel.setLayout(new BoxLayout(branchPanel, BoxLayout.PAGE_AXIS));
        branchDialog.add(branchPanel);

        JLabel addressLabel = new JLabel(
                "<html><center><b>Adres oddziału:</b> " +
                        branch.getAddress().getStreet() + " " +
                        branch.getAddress().getApartment_no() + ", " +
                        branch.getAddress().getCity_name() + "</center></html>",
                SwingConstants.CENTER
        );
        addressLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        branchPanel.add(addressLabel);

        branchPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel hoursTitleLabel = new JLabel("Godziny otwarcia", SwingConstants.CENTER);
        hoursTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        branchPanel.add(hoursTitleLabel);

        branchPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel hoursLabel = new JLabel(
                "<html><div style=\"text-align:center\">" +
                        "Poniedziałek: " + getHoursOrClosed(branch, "MON", dict) + "<br />" +
                        "Wtorek: " + getHoursOrClosed(branch, "TUE", dict) + "<br />" +
                        "Środa: " + getHoursOrClosed(branch, "WED", dict) + "<br />" +
                        "Czwartek: " + getHoursOrClosed(branch, "THU", dict) + "<br />" +
                        "Piątek: " + getHoursOrClosed(branch, "FRI", dict) + "<br />" +
                        "Sobota: " + getHoursOrClosed(branch, "SAT", dict) + "<br />" +
                        "Niedziela: " + getHoursOrClosed(branch, "SUN", dict) +
                        "</div></html>",
                SwingConstants.CENTER
        );
        hoursLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        hoursLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        branchPanel.add(hoursLabel);

        branchDialog.setVisible(true);
    }
}
