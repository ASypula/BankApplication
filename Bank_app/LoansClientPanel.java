import javax.swing.*;
import java.awt.*;

public class LoansClientPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    public LoansClientPanel(AppFrame mparent, Client mclient) {
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

        PageButtonsPanel pageButPan = new PageButtonsPanel(parent, client);
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(pageButPan, c);

        JLabel loansLabel = new JLabel("Moje kredyty");
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        this.add(loansLabel, c);

        WhiteButton newLoanButton = new WhiteButton("Złóż wniosek o nowy kredyt");
        newLoanButton.addActionListener(e -> AppDialog.contactEmployeeDialog(parent));
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.gridx = 1;
        c.gridy = 1;
        this.add(newLoanButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weighty = 0.8;

        JPanel loansPanel = new JPanel();
        loansPanel.setBackground(parent.bgColor);
        loansPanel.setLayout(new BoxLayout(loansPanel, BoxLayout.Y_AXIS));

//        TODO: Uncomment everything and check after necessary functions implemented; Remove dummy values
//        java.util.List<Loan> loans = client.getLoans();
//        for (Loan loan : loans) {
        for (int i = 0; i < 4; i++) {
            JPanel loanPanel = new JPanel();
            loanPanel.setBackground(parent.bgColor);
            loanPanel.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 0.5;
            c.weighty = 0.5;

//            JLabel nextInstallmentAmountLabel = new JLabel(
//                    "<html>Następna rata: <b>" +
//                            loan.getNextInstallmentAmount() +
//                            "</b></html>"
//            );
            JLabel nextInstallmentAmountLabel = new JLabel(
                    "<html>Następna rata: <b>" +
                            "[NextInstallmentAmount]" +
                            "</b></html>"
            );
            nextInstallmentAmountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 0;
            loanPanel.add(nextInstallmentAmountLabel, c);

//            JLabel nextInstallmentDateLabel = new JLabel(
//                    "<html>Płatna dnia: <b>" +
//                            loan.getNextInstallmentDate().toString() +
//                            "</b></html>"
//            );
            JLabel nextInstallmentDateLabel = new JLabel(
                    "<html>Płatna dnia: <b>" +
                            "[NextInstallmentDate]" +
                            "</b></html>"
            );
            nextInstallmentDateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 1;
            loanPanel.add(nextInstallmentDateLabel, c);

//            JLabel interestLabel = new JLabel(
//                    "<html>Oprocentowanie: <b>" +
//                            loan.getInterest() +
//                            "</b></html>"
//            );
            JLabel interestLabel = new JLabel(
                    "<html>Oprocentowanie: <b>" +
                            "[Interest]" +
                            "%</b></html>"
            );
            interestLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 2;
            loanPanel.add(interestLabel, c);

            c.anchor = GridBagConstraints.EAST;

            JLabel currentLabel = new JLabel("Do spłaty pozostało:");
            currentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 1;
            c.gridy = 0;
            loanPanel.add(currentLabel, c);

//            JLabel currentAmountLabel = new JLabel(loan.getCurrentAmount());
            JLabel currentAmountLabel = new JLabel("[Amount]");
            currentAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
            c.gridx = 1;
            c.gridy = 1;
            loanPanel.add(currentAmountLabel, c);

//            JLabel initialAmountLabel = new JLabel(
//                    "<html>Kwota początkowa: <b>" +
//                            loan.getInitialAmount() +
//                            "</b></html>"
//            );
            JLabel initialAmountLabel = new JLabel(
                    "<html>Kwota początkowa: <b>" +
                            "[InitialAmount]" +
                            "</b></html>"
            );
            initialAmountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
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
        c.weightx = 0.5;
        c.weighty = 0.8;
        this.add(loansPanel, c);
    }
}
