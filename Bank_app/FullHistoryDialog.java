import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class FullHistoryDialog extends AppDialog {

    AppFrame parent;
    private Dictionary dict;

    public FullHistoryDialog(AppFrame mparent, java.util.List<BankAccount> bankAccounts) {
        super(mparent, mparent.dict.getText("full_history"), 600, 400);
        parent = mparent;
        dict = parent.dict;
        try {
            initialize(bankAccounts);
            this.setVisible(true);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
    }

    private void initialize(java.util.List<BankAccount> bankAccounts) throws SQLException {
        JPanel fullHistPan = new JPanel();
        fullHistPan.setBackground(parent.bgColor);
        fullHistPan.setLayout(new GridBagLayout());
        this.add(fullHistPan);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        for (int i = 0; i < bankAccounts.size(); i++) {
            BankAccount account = bankAccounts.get(i);

            c.fill = GridBagConstraints.NONE;
            c.weightx = 0.5;
            c.weighty = 0.1;

            JLabel accountLabel = new JLabel(account.getAccount_no());
            c.gridx = i;
            c.gridy = 0;
            fullHistPan.add(accountLabel, c);

            java.util.List<Transaction> transactions = account.getTransactions();
            c.weighty = 0.9;
            c.fill = GridBagConstraints.BOTH;

            if (transactions.isEmpty()) {
                JLabel noneLabel = new JLabel(
                        "<html><center>" + dict.getText("no_transactions_1") +
                        "<br />" + dict.getText("no_transactions_2") +
                        "<br />" + dict.getText("no_transactions_3") + "</center></html>",
                        SwingConstants.CENTER
                );
                c.gridx = i;
                c.gridy = 1;
                fullHistPan.add(noneLabel, c);
            } else {
                HistoryPanel histPanel = new HistoryPanel(parent, transactions, transactions.size());
                JScrollPane scrollPane = new JScrollPane(histPanel);
                c.gridx = i;
                c.gridy = 1;
                fullHistPan.add(scrollPane, c);
            }
        }
    }
}
