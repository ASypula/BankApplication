import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class LoansClientPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    public LoansClientPanel(AppFrame mparent, Client mclient) {
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

        JLabel loansLabel = new JLabel(dict.getText("my_credits"));
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        this.add(loansLabel, c);

        WhiteButton newLoanButton = new WhiteButton(dict.getText("new_credit"));
        newLoanButton.addActionListener(e -> AppDialog.contactEmployeeDialog(parent));
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 1;
        this.add(newLoanButton, c);

        java.util.List<Loan> loans = client.getLoans();
        if (loans.isEmpty()) {
            JLabel noneLabel = new JLabel(dict.getText("no_credits"), SwingConstants.CENTER);
            c.fill = GridBagConstraints.BOTH;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weighty = 0.8;
            c.gridx = 0;
            c.gridy = 2;
            this.add(noneLabel, c);
        } else {
            JPanel loansPanel = new JPanel();
            loansPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            loansPanel.setBackground(parent.bgColor);
            loansPanel.setLayout(new BoxLayout(loansPanel, BoxLayout.Y_AXIS));

//        TODO: Verify after adding test values
            for (Loan loan : loans) {
                JPanel loanPanel = new JPanel();
                loanPanel.setBackground(parent.bgColor);
                loanPanel.setLayout(new GridBagLayout());
                c.fill = GridBagConstraints.NONE;
                c.anchor = GridBagConstraints.WEST;
                c.weightx = 0.5;
                c.weighty = 0.5;

                JLabel nextInstallmentAmountLabel = new JLabel(
                        "<html>"+dict.getText("installment_amount")+"<b>" +
                                loan.getInstallment() + "</b></html>"
                );
                nextInstallmentAmountLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 0;
                loanPanel.add(nextInstallmentAmountLabel, c);

                JLabel periodLabel = new JLabel(
                    "<html>" + dict.getText("period") + " <b>" +
                            loan.getAccum_period() +
                            " yrs</b> ("+dict.getText("from")+" <b>" +
                            loan.getStart_date().toString() +
                            "</b> "+dict.getText("to")+" <b>" +
                            loan.getEndDate().toString() +
                            "</b>)</html>"
                );
                periodLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 1;
                loanPanel.add(periodLabel, c);

                JLabel interestLabel = new JLabel(
                        "<html>"+dict.getText("interest")+"<b>" +
                                loan.getInterest_rate() + "%</b></html>"
                );
                interestLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 2;
                loanPanel.add(interestLabel, c);

                c.anchor = GridBagConstraints.EAST;

                JLabel currentLabel = new JLabel(dict.getText("left_payment"));
                currentLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 1;
                c.gridy = 0;
                loanPanel.add(currentLabel, c);

                JLabel currentAmountLabel = new JLabel(Integer.toString(loan.getBalance()));
                currentAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
                c.gridx = 1;
                c.gridy = 1;
                loanPanel.add(currentAmountLabel, c);

                JLabel initialAmountLabel = new JLabel(
                        "<html>"+dict.getText("initial_amount")+"<b>" +
                                loan.getInitial_value() + "</b></html>"
                );
                initialAmountLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 1;
                c.gridy = 2;
                loanPanel.add(initialAmountLabel, c);

                loansPanel.add(loanPanel);
                loansPanel.add(Box.createRigidArea(new Dimension(0, 60)));
            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weighty = 0.8;
            this.add(loansPanel, c);
        }
    }
}
