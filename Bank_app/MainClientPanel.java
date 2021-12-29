import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
                        dict.getText("no_funds_acc_2")+"</div></html>",
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

    private void getAmountFromUserAndUpdate(BankAccount account, JLabel balanceLabel, boolean inc) {
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
                        balanceLabel.setText(Integer.toString(account.getBalance()));
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

        PageButtonsPanel pageButPan = new PageButtonsPanel(parent, client);
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(pageButPan, c);

        JLabel accountsLabel = new JLabel(dict.getText("my_accounts"), SwingConstants.CENTER);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.75;
        c.gridwidth = 1;
        this.add(accountsLabel, c);

        JLabel historyLabel = new JLabel(dict.getText("history"), SwingConstants.CENTER);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.25;
        this.add(historyLabel, c);

        c.weighty = 0.8;

        JLabel info = new JLabel(client.getName()+client.getSurname(), SwingConstants.CENTER);
//        TODO: Add history
        c.gridx = 1;
        c.gridy = 2;
        this.add(info, c);

        JPanel accountsPanel = new JPanel();
        accountsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        accountsPanel.setBackground(parent.bgColor);
        accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));

        java.util.List<BankAccount> accounts = client.getBankAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            int finalI = i;

            JPanel accountPanel = new JPanel();
            accountPanel.setBackground(parent.bgColor);
            accountPanel.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 0.5;
            c.weighty = 0.5;

            JLabel accountIdLabel = new JLabel(
                    "<html>" + dict.getText("account_id") +
                            "<b>" + accounts.get(i).getAccount_id() + "</b></html>"
            );
            accountIdLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            accountPanel.add(accountIdLabel, c);

            JLabel accountNrLabel = new JLabel(
                    "<html>Numer konta: " +
                            "<b>" + accounts.get(i).getAccount_no() + "</b></html>"
            );
            accountNrLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 1;
            accountPanel.add(accountNrLabel, c);

            JLabel accountTypeLabel = new JLabel(
                    "<html>Typ konta: " +
                            "<b>" + accounts.get(i).getAccount_type() + "</b></html>"
            );
            accountTypeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 2;
            accountPanel.add(accountTypeLabel, c);

            JLabel accountBalanceLabel = new JLabel(dict.getText("available_funds"));
            accountBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.anchor = GridBagConstraints.EAST;
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 2;
            accountPanel.add(accountBalanceLabel, c);

            JLabel balanceLabel = new JLabel(Integer.toString(accounts.get(i).getBalance()));
            balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
            c.gridx = 1;
            c.gridy = 1;
            c.gridheight = 2;
            accountPanel.add(balanceLabel, c);

            c.anchor = GridBagConstraints.CENTER;
            c.gridx = 0;
            c.gridy = 3;
            c.gridheight = 1;
            accountPanel.add(Box.createRigidArea(new Dimension(0, 10)), c);

            WhiteButton depositButton = new WhiteButton(dict.getText("deposit"));
            depositButton.addActionListener(
                    e -> getAmountFromUserAndUpdate(accounts.get(finalI), balanceLabel, true)
            );
            c.gridx = 0;
            c.gridy = 4;
            c.gridwidth = 1;
            accountPanel.add(depositButton, c);

            WhiteButton withdrawButton = new WhiteButton(dict.getText("withdraw"));
            withdrawButton.addActionListener(
                    e -> getAmountFromUserAndUpdate(accounts.get(finalI), balanceLabel, false)
            );
            c.gridx = 1;
            c.gridy = 4;
            accountPanel.add(withdrawButton, c);

            WhiteButton transferButton = new WhiteButton(dict.getText("transfer"));
            transferButton.addActionListener(
                    e -> getTransferDetailsAndTransfer(accounts.get(finalI))
            );
            c.gridx = 2;
            c.gridy = 4;
            accountPanel.add(transferButton, c);

            accountsPanel.add(accountPanel);
            accountsPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.75;
        c.weighty = 0.8;
        this.add(accountsPanel, c);
    }
}
