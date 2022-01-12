import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class DepositsClientPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    public DepositsClientPanel(AppFrame mparent, Client mclient) {
        super();
        parent = mparent;
        client = mclient;
        dict = parent.dict;
        try {
            initialize();
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
    }

    private void initialize() throws SQLException {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;

        PageButtonsPanel pageButPan = new PageButtonsPanel(parent, client);
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(pageButPan, c);

        JLabel depositsLabel = new JLabel(dict.getText("my_deposits"));
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        this.add(depositsLabel, c);

        WhiteButton newDepositButton = new WhiteButton(dict.getText("new_deposit"));
        newDepositButton.addActionListener(e -> AppDialog.contactEmployeeDialog(parent));
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 1;
        this.add(newDepositButton, c);

        java.util.List<BankAccount> deposits = client.getDeposits();
        if (deposits.isEmpty()) {
            JLabel noneLabel = new JLabel(dict.getText("no_deposits"), SwingConstants.CENTER);
            c.fill = GridBagConstraints.BOTH;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weighty = 0.8;
            c.gridx = 0;
            c.gridy = 2;
            this.add(noneLabel, c);
        } else {
            JPanel depositsPanel = new JPanel();
            depositsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            depositsPanel.setBackground(parent.bgColor);
            depositsPanel.setLayout(new BoxLayout(depositsPanel, BoxLayout.Y_AXIS));

            for (BankAccount deposit : deposits) {
                JPanel depositPanel = new JPanel();
                depositPanel.setBackground(parent.bgColor);
                depositPanel.setLayout(new GridBagLayout());
                c.fill = GridBagConstraints.NONE;
                c.anchor = GridBagConstraints.WEST;
                c.weightx = 0.5;
                c.weighty = 0.5;

                JLabel periodLabel = new JLabel(
                        "<html>" + dict.getText("period") + " <b>" +
                                deposit.getAccum_period() + " msc</b></html>"
                );
                periodLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 0;
                depositPanel.add(periodLabel, c);

                JLabel startDateLabel = new JLabel(
                        "<html>"+dict.getText("start_date")+"<b>" +
                                deposit.getStart_date().toString() + "</b></html>"
                );
                startDateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 1;
                depositPanel.add(startDateLabel, c);

                JLabel interestLabel = new JLabel(
                        "<html>" + dict.getText("interest") + "<b>" +
                                deposit.getInterest_rate() + "%</b></html>"
                );
                interestLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 2;
                depositPanel.add(interestLabel, c);

                c.anchor = GridBagConstraints.EAST;

                JLabel currentLabel = new JLabel(dict.getText("current_amount"));
                currentLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 1;
                c.gridy = 0;
                depositPanel.add(currentLabel, c);

                JLabel currentAmountLabel = new JLabel(
                        Integer.toString(deposit.getBalance()) + " " + deposit.getCurrency_abbr()
                );
                currentAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));
                c.gridx = 1;
                c.gridy = 1;
                c.gridheight = GridBagConstraints.REMAINDER;
                depositPanel.add(currentAmountLabel, c);

                c.gridheight = 1;

                depositsPanel.add(depositPanel);
                depositsPanel.add(Box.createRigidArea(new Dimension(0, 60)));
            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weighty = 0.8;
            this.add(depositsPanel, c);
        }

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridy = 3;
        c.weighty = 0.02;
        c.gridheight = 1;
        this.add(new LanguageButtonsPanel(parent, "depositsClient", client), c);
    }
}
