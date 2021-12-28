import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class MainClientPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    public MainClientPanel(AppFrame mparent, Client mclient) {
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

    private void showTooLowBalanceDialog() {
        AppDialog lowBalanceDialog = new AppDialog(parent, dict.getText("no_funds"), 160, 150);
        JLabel lowBalanceText = new JLabel(
                "<html><div style='text-align: center;'>" +
                        dict.getText("no_funds_acc_1") + "<br /> " +
                        dict.getText("no_funds_acc_2")+"</html>",
                SwingConstants.CENTER
        );
        lowBalanceDialog.add(lowBalanceText);
        lowBalanceDialog.setVisible(true);
    }

    private void getTransferDetailsAndTransfer(BankAccount account) {
        AppDialog transferDialog = new AppDialog(parent, dict.getText("transfer_details"), 250, 300);
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
                String receiver = receiverTf.getText();
                if (amount > account.getBalance())
                    showTooLowBalanceDialog();
                else if (amount > 0 && !receiver.isEmpty()) {
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

        JLabel receiverLabel = new JLabel(dict.getText("recipient_id"));
        c.gridx = 0;
        c.gridy = 0;
        transferDialog.add(receiverLabel, c);

        receiverTf.addActionListener(okActionListener);
        c.gridx = 0;
        c.gridy = 1;
        transferDialog.add(receiverTf, c);

        JLabel amountLabel = new JLabel(dict.getText("amount"));
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
            dialogTitle = dict.getText("account_deposit");
        else
            dialogTitle = dict.getText("account_withdrawal");
        AppDialog amountDialog = new AppDialog(parent, dialogTitle, 250, 200);
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
                if (!inc && amount > account.getBalance())
                    showTooLowBalanceDialog();
                else if (amount > 0) {
                    try {
                        if (inc)
                            account.balanceInc(amount);
                        else
                            account.balanceDec(amount);
                        accountBalanceLabel.setText(
                                dict.getText("available_funds") +
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

        JLabel amountLabel = new JLabel(dict.getText("amount"));
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

        PageButtonsPanel pageButPan = new PageButtonsPanel(parent, client);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(pageButPan, c);

        JLabel accountsLabel = new JLabel(dict.getText("my_accounts"));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        this.add(accountsLabel, c);

        JLabel historyLabel = new JLabel(dict.getText("history"));
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
                    dict.getText("account_id") +
                            accounts.get(i).getAccount_id()
            );
            c.gridx = 0;
            c.gridy = i*rowsPerAcc + 2;
            c.gridwidth = 4;
            this.add(accountNrLabel, c);

            JLabel accountBalanceLabel = new JLabel(
                    dict.getText("available_funds")+
                            accounts.get(i).getBalance()
            );
            accountBalanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
            c.gridx = 0;
            c.gridy = i*rowsPerAcc + 3;
            c.gridwidth = 4;
            this.add(accountBalanceLabel, c);

            WhiteButton depositButton = new WhiteButton(dict.getText("deposit"));
            depositButton.addActionListener(
                    e -> getAmountFromUserAndUpdate(accounts.get(finalI), accountBalanceLabel, true)
            );
            c.gridx = 1;
            c.gridy = i*rowsPerAcc + 4;
            c.gridwidth = 1;
            this.add(depositButton, c);

            WhiteButton withdrawButton = new WhiteButton(dict.getText("withdraw"));
            withdrawButton.addActionListener(
                    e -> getAmountFromUserAndUpdate(accounts.get(finalI), accountBalanceLabel, false)
            );
            c.gridx = 2;
            c.gridy = i*rowsPerAcc + 4;
            this.add(withdrawButton, c);

            WhiteButton transferButton = new WhiteButton(dict.getText("transfer"));
            transferButton.addActionListener(
                    e -> getTransferDetailsAndTransfer(accounts.get(finalI))
            );
            c.gridx = 3;
            c.gridy = i*rowsPerAcc + 4;
            this.add(transferButton, c);
        }
    }
}
