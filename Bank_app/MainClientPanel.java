import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class MainClientPanel extends JPanel {

    AppFrame parent;
    Client client;

    public MainClientPanel(AppFrame mparent, Client mclient) {
        super();
        parent = mparent;
        client = mclient;
        try {
            initialize();
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
    }

    private void getAmountFromUserAndUpdate(BankAccount account, JLabel accountBalanceLabel, boolean inc) {
//        if inc is true - deposit; else withdrawal
        String dialogTitle;
        if (inc)
            dialogTitle = "Wpłać na konto";
        else
            dialogTitle = "Wypłać z konta";
        JDialog amountDialog = new JDialog(parent, dialogTitle);
        amountDialog.setSize(250, 200);
        amountDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.5;

        JLabel amountLabel = new JLabel("Kwota:");
        JTextField amountTf = new JTextField();
        amountTf.setMaximumSize(new Dimension(150, 25));
//        TODO: accept only number input

        JButton okButton = new JButton("Zatwierdź");
        okButton.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(amountTf.getText());
                try {
                    if (inc)
                        account.balanceInc(amount);
                    else
                        account.balanceDec(amount);
//                    TODO: show info about too low balance
                    accountBalanceLabel.setText(
                            "Dostępne środki: " +
                                    account.getBalance()
                    );
                } catch (SQLException ex) {
                    System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                }
                amountDialog.setVisible(false);
                amountDialog.dispatchEvent(
                        new WindowEvent(amountDialog, WindowEvent.WINDOW_CLOSING)
                );
            } catch (NumberFormatException ignored) {}
        });

        JButton cancelButton = new JButton("Anuluj");
        cancelButton.addActionListener(e -> {
            amountDialog.setVisible(false);
            amountDialog.dispatchEvent(
                    new WindowEvent(amountDialog, WindowEvent.WINDOW_CLOSING)
            );
        });

        c.gridx = 0;
        c.gridy = 0;
        amountDialog.add(amountLabel, c);
        c.gridx = 0;
        c.gridy = 1;
        amountDialog.add(amountTf, c);
        c.gridx = 0;
        c.gridy = 2;
        amountDialog.add(okButton, c);
        c.gridx = 1;
        c.gridy = 2;
        amountDialog.add(cancelButton, c);
        amountDialog.setVisible(true);
    }

    private void initialize() throws SQLException {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.5;

        JLabel bankNameLabel = new JLabel("Casa de PAPel", SwingConstants.CENTER);
        c.gridx = 0;
        c.gridy = 0;
        this.add(bankNameLabel, c);

        JButton mainPageButton = new JButton("Główna");
        c.gridx = 1;
        c.gridy = 0;
        this.add(mainPageButton, c);

        JButton cardsButton = new JButton("Karty");
        c.gridx = 2;
        c.gridy = 0;
        this.add(cardsButton, c);

        JButton creditsButton = new JButton("Kredyty");
        c.gridx = 3;
        c.gridy = 0;
        this.add(creditsButton, c);

        JButton depositsButton = new JButton("Lokaty");
        c.gridx = 4;
        c.gridy = 0;
        this.add(depositsButton, c);

        JButton helpButton = new JButton("Pomoc");
        c.gridx = 5;
        c.gridy = 0;
        this.add(helpButton, c);

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> parent.changeToLogin());
        c.gridx = 6;
        c.gridy = 0;
        this.add(logoutButton, c);

        JLabel accountsLabel = new JLabel("Moje konta");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        this.add(accountsLabel, c);

        JLabel historyLabel = new JLabel("Historia");
        c.gridx = 5;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(historyLabel, c);

        JLabel info = new JLabel(client.getName()+client.getSurname(), SwingConstants.CENTER);
        c.gridx = 5;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(info, c);

        int rowsPerAcc = 3;
        java.util.List<BankAccount> accounts = client.getBankAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            int finalI = i;

            JLabel accountNrLabel = new JLabel(
                    "ID konta: " +
                            accounts.get(i).getAccount_id()
            );
            c.gridx = 0;
            c.gridy = i*rowsPerAcc + 2;
            c.gridwidth = 4;
            this.add(accountNrLabel, c);

            JLabel accountBalanceLabel = new JLabel(
                    "Dostępne środki: " +
                            accounts.get(i).getBalance()
            );
            accountBalanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
            c.gridx = 0;
            c.gridy = i*rowsPerAcc + 3;
            c.gridwidth = 4;
            this.add(accountBalanceLabel, c);

            JButton depositButton = new JButton("Wpłać");
            depositButton.addActionListener(
                    e -> getAmountFromUserAndUpdate(accounts.get(finalI), accountBalanceLabel, true)
            );
            c.gridx = 1;
            c.gridy = i*rowsPerAcc + 4;
            c.gridwidth = 1;
            this.add(depositButton, c);

            JButton withdrawButton = new JButton("Wypłać");
            withdrawButton.addActionListener(
                    e -> getAmountFromUserAndUpdate(accounts.get(finalI), accountBalanceLabel, false)
            );
            c.gridx = 2;
            c.gridy = i*rowsPerAcc + 4;
            this.add(withdrawButton, c);

            JButton transferButton = new JButton("Przelew");
            c.gridx = 3;
            c.gridy = i*rowsPerAcc + 4;
            this.add(transferButton, c);
        }
    }
}