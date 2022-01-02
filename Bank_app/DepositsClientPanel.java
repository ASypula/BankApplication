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

//        TODO: Uncomment everything and check after necessary functions implemented; Remove dummy values
//        java.util.List<Deposit> deposits = client.getDeposits();
            for (BankAccount deposit : deposits) {
//            for (int i = 0; i < 3; i++) {
                JPanel depositPanel = new JPanel();
                depositPanel.setBackground(parent.bgColor);
                depositPanel.setLayout(new GridBagLayout());
                c.fill = GridBagConstraints.NONE;
                c.anchor = GridBagConstraints.WEST;
                c.weightx = 0.5;
                c.weighty = 0.5;

//            JLabel nextInstallmentAmountLabel = new JLabel(
//                    "<html>Następna wpłata: <b>" +
//                            deposit.getNextInstallmentAmount() +
//                            "</b></html>"
//            );
                JLabel nextInstallmentAmountLabel = new JLabel(
                        "<html>" + dict.getText("next_payment") + "<b>" +
                                "[NextInstallmentAmount]" + "</b></html>"
                );
                nextInstallmentAmountLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 0;
                depositPanel.add(nextInstallmentAmountLabel, c);

//            JLabel nextInstallmentDateLabel = new JLabel(
//                    "<html>Płatna dnia: <b>" +
//                            deposit.getNextInstallmentDate().toString() +
//                            "</b></html>"
//            );
                JLabel nextInstallmentDateLabel = new JLabel(
                        "<html>" + dict.getText("payment_day") + "<b>" +
                                "[NextInstallmentDate]" + "</b></html>"
                );
                nextInstallmentDateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 1;
                depositPanel.add(nextInstallmentDateLabel, c);

//            JLabel interestLabel = new JLabel(
//                    "<html>Oprocentowanie: <b>" +
//                            deposit.getInterest() +
//                            "</b></html>"
//            );
                JLabel interestLabel = new JLabel(
                        "<html>" + dict.getText("interest") + "<b>" +
                                "[Interest]" + "%</b></html>"
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

//            JLabel currentAmountLabel = new JLabel(deposit.getCurrentAmount());
                JLabel currentAmountLabel = new JLabel("[Amount]");
                currentAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
                c.gridx = 1;
                c.gridy = 1;
                depositPanel.add(currentAmountLabel, c);

//            JLabel periodLabel = new JLabel(
//                    "<html>Okres: <b>" +
//                            deposit.getPeriodMonths() +
//                            " msc</b> (od <b>" +
//                            deposit.getStartDate().toString() +
//                            "</b> do <b>" +
//                            deposit.getEndDate().toString() +
//                            "</b>)</html>"
//            );
                JLabel periodLabel = new JLabel(
                        "<html>" + dict.getText("period") + " <b>" +
                                "[Months]" +
                                " msc</b> (" + dict.getText("from") + " <b>" +
                                "[StartDate]" +
                                "</b>" + dict.getText("to") + "<b>" +
                                "[EndDate]" +
                                "</b>)</html>"
                );
                periodLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 1;
                c.gridy = 2;
                depositPanel.add(periodLabel, c);

                depositsPanel.add(depositPanel);
                depositsPanel.add(Box.createRigidArea(new Dimension(0, 60)));
            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridx = 0;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.8;
            this.add(depositsPanel, c);
        }
    }
}
