import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Vector;

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
                        if  (account.transfer(amount, receiver)) {
                            parent.changeToMainClient(client);
                            transferDialog.setVisible(false);
                            transferDialog.dispatchEvent(
                                    new WindowEvent(transferDialog, WindowEvent.WINDOW_CLOSING)
                            );
                        }
                    } catch (SQLException ex) {
                        System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                    } catch (WrongId ex) {
                        System.err.format(ex.getMessage());
                    }
                }
            } catch (NumberFormatException ignored) {}
        };

        JLabel receiverLabel = new JLabel(dict.getText("account_no_recipient"));
        c.gridx = 0;
        c.gridy = 0;
        transferDialog.add(receiverLabel, c);

        receiverTf.addActionListener(okActionListener);
        c.gridx = 0;
        c.gridy = 1;
        transferDialog.add(receiverTf, c);

        JLabel amountLabel = new JLabel(dict.getText("amount") + " (" + account.getCurrency_abbr() + ")");
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
                            account.payment(amount);
                        else
                            account.withdrawal(amount);
                        parent.changeToMainClient(client);
                        amountDialog.setVisible(false);
                        amountDialog.dispatchEvent(
                                new WindowEvent(amountDialog, WindowEvent.WINDOW_CLOSING)
                        );
                    } catch (SQLException ex) {
                        System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                    } catch (WrongId ex) {
                        System.err.format(ex.getMessage());
                    }
                }
            } catch (NumberFormatException ignored) {}
        };

        JLabel amountLabel = new JLabel(dict.getText("amount") + " (" + account.getCurrency_abbr() + ")");
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

        java.util.List<BankAccount> accounts = client.getBankAccounts();
        if (accounts.isEmpty()) {
            JLabel noneLabel = new JLabel(dict.getText("no_accounts"), SwingConstants.CENTER);
            c.fill = GridBagConstraints.BOTH;
            c.weighty = 0.8;
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = GridBagConstraints.REMAINDER;
            this.add(noneLabel, c);
        } else {
            JPanel accountsPanel = new JPanel();
            accountsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            accountsPanel.setBackground(parent.bgColor);
            accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));

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
                accountIdLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 0;
                c.gridwidth = GridBagConstraints.REMAINDER;
                accountPanel.add(accountIdLabel, c);

                JLabel accountNrLabel = new JLabel(
                        "<html>" + dict.getText("account_no") +
                                "<b>" + accounts.get(i).getAccount_no() + "</b></html>"
                );
                accountNrLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 1;
                accountPanel.add(accountNrLabel, c);

                JLabel accountTypeLabel = new JLabel(
                        "<html>" + dict.getText("account_type") +
                                "<b>" + dict.getText("account_type_"+accounts.get(i).getAccount_type()) + "</b></html>"
                );
                accountTypeLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 2;
                accountPanel.add(accountTypeLabel, c);

                JLabel accountBalanceLabel = new JLabel(dict.getText("available_funds"));
                accountBalanceLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.anchor = GridBagConstraints.EAST;
                c.gridx = 1;
                c.gridy = 0;
                c.gridwidth = 2;
                accountPanel.add(accountBalanceLabel, c);

                JPanel balancePanel = new JPanel();
                balancePanel.setBackground(parent.bgColor);
                balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.X_AXIS));

                JLabel balanceLabel = new JLabel(Integer.toString(accounts.get(i).getBalance()));
                balanceLabel.setFont(new Font("Dialog", Font.BOLD, 20));
                balancePanel.add(balanceLabel);

                JComboBox<String> currencyCb = new JComboBox<String>(new Vector<String>(Account.getCurrencyAbbreviations()));
                currencyCb.setSelectedItem(accounts.get(i).getCurrency_abbr());
                currencyCb.setMaximumSize(new Dimension(60, 25));
                currencyCb.addActionListener(e -> {
                    try {
                        accounts.get(finalI).changeCurrency((String) currencyCb.getSelectedItem());
                        balanceLabel.setText(Integer.toString(accounts.get(finalI).getBalance()));
                    } catch (SQLException ex) {
                        System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                    } catch (WrongId ex) {
                        System.err.format(ex.getMessage());
                    }
                });
                balancePanel.add(currencyCb);

                c.gridx = 1;
                c.gridy = 1;
                c.gridheight = 2;
                accountPanel.add(balancePanel, c);

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

                java.util.List<Transaction> transactions = accounts.get(i).getTransactions();
                if (transactions.isEmpty()) {
                    JLabel noneLabel = new JLabel(
                            "<html><center>" + dict.getText("no_transactions_1") +
                                    "<br />" + dict.getText("no_transactions_2") +
                                    "<br />"+ dict.getText("no_transactions_3") +"</center></html>",
                            SwingConstants.CENTER
                    );
                    c.fill = GridBagConstraints.BOTH;
                    c.weightx = 0.25;
                    c.gridx = 3;
                    c.gridy = 0;
                    c.gridheight = GridBagConstraints.REMAINDER;
                    accountPanel.add(noneLabel, c);
                } else {
                    JPanel historyPanel = new HistoryPanel(parent, transactions, 3);
                    c.fill = GridBagConstraints.BOTH;
                    c.gridx = 3;
                    c.gridy = 0;
                    c.gridheight = GridBagConstraints.REMAINDER;
                    accountPanel.add(historyPanel, c);
                }
                c.gridheight = 1;

                accountsPanel.add(accountPanel);
                accountsPanel.add(Box.createRigidArea(new Dimension(0, 60)));
            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weighty = 0.8;
            this.add(accountsPanel, c);

            WhiteButton showFullButton = new WhiteButton(dict.getText("show_history"));
            showFullButton.addActionListener(e -> new FullHistoryDialog(parent, accounts));
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            c.gridx = 0;
            c.gridy = 3;
            this.add(showFullButton, c);
        }

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        if(accounts.isEmpty())
            c.gridy = 3;
        else
            c.gridy = 4;
        c.weighty = 0.02;
        c.gridheight = 1;
        this.add(new LanguageButtonsPanel(parent, "mainClient", client), c);
    }
}
