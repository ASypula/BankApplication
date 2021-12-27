import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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

    private void getTransferDetailsAndTransfer(BankAccount account) {
//        TODO: add seperate class for dialog
        AppDialog transferDialog = new AppDialog(parent, "Szczegóły przelewu");
        transferDialog.setSize(250, 300);
        transferDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.5;

        JTextField receiverTf = new JTextField();
        JTextField amountTf = new JTextField();

        ActionListener okActionListener = e -> {
            try {
                int amount = Integer.parseInt(amountTf.getText());
//                TODO: Block transfers when too low balance, show dialog
                String receiver = receiverTf.getText();
                if (amount > 0 && !receiver.isEmpty()) {
                    try {
                        account.transfer("1000", "4", amount, receiver);
//                    TODO: unhardcode these values!
                        parent.changeToMainClient(client);
                    } catch (SQLException ex) {
                        System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                    }
                    transferDialog.setVisible(false);
                    transferDialog.dispatchEvent(
                            new WindowEvent(transferDialog, WindowEvent.WINDOW_CLOSING)
                    );
                }
            } catch (NumberFormatException ignored) {}
        };

        JLabel receiverLabel = new JLabel("ID konta odbiorcy:");
        c.gridx = 0;
        c.gridy = 0;
        transferDialog.add(receiverLabel, c);

        receiverTf.addActionListener(okActionListener);
        c.gridx = 0;
        c.gridy = 1;
        transferDialog.add(receiverTf, c);

        JLabel amountLabel = new JLabel("Kwota:");
        c.gridx = 0;
        c.gridy = 2;
        transferDialog.add(amountLabel, c);

        amountTf.addActionListener(okActionListener);
        c.gridx = 0;
        c.gridy = 3;
        transferDialog.add(amountTf, c);

        OkCancelButtonsPanel okCanButPan = new OkCancelButtonsPanel(parent, transferDialog, okActionListener);
        c.gridx = 0;
        c.gridy = 4;
        transferDialog.add(okCanButPan, c);

        transferDialog.setVisible(true);
    }

    private void getAmountFromUserAndUpdate(BankAccount account, JLabel accountBalanceLabel, boolean inc) {
//        if inc is true - deposit; else withdrawal
        String dialogTitle;
        if (inc)
            dialogTitle = "Wpłać na konto";
        else
            dialogTitle = "Wypłać z konta";
        AppDialog amountDialog = new AppDialog(parent, dialogTitle);
        amountDialog.setSize(250, 200);
        amountDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.5;

        JTextField amountTf = new JTextField();

        ActionListener okActionListener = e -> {
            try {
                int amount = Integer.parseInt(amountTf.getText());
                if (amount > 0) {
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
                }
            } catch (NumberFormatException ignored) {}
        };

        JLabel amountLabel = new JLabel("Kwota:");
        c.gridx = 0;
        c.gridy = 0;
        amountDialog.add(amountLabel, c);

        amountTf.addActionListener(okActionListener);
        c.gridx = 0;
        c.gridy = 1;
        amountDialog.add(amountTf, c);

        OkCancelButtonsPanel okCanButPan = new OkCancelButtonsPanel(parent, amountDialog, okActionListener);
        c.gridx = 0;
        c.gridy = 2;
        amountDialog.add(okCanButPan, c);

        amountDialog.setVisible(true);
    }

    private void initialize() throws SQLException {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.5;

        PageButtonsPanel pageButPan = new PageButtonsPanel(parent);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(pageButPan, c);

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

            WhiteButton depositButton = new WhiteButton("Wpłać");
            depositButton.addActionListener(
                    e -> getAmountFromUserAndUpdate(accounts.get(finalI), accountBalanceLabel, true)
            );
            c.gridx = 1;
            c.gridy = i*rowsPerAcc + 4;
            c.gridwidth = 1;
            this.add(depositButton, c);

            WhiteButton withdrawButton = new WhiteButton("Wypłać");
            withdrawButton.addActionListener(
                    e -> getAmountFromUserAndUpdate(accounts.get(finalI), accountBalanceLabel, false)
            );
            c.gridx = 2;
            c.gridy = i*rowsPerAcc + 4;
            this.add(withdrawButton, c);

            WhiteButton transferButton = new WhiteButton("Przelew");
            transferButton.addActionListener(
                    e -> getTransferDetailsAndTransfer(accounts.get(finalI))
            );
            c.gridx = 3;
            c.gridy = i*rowsPerAcc + 4;
            this.add(transferButton, c);
        }
    }
}
