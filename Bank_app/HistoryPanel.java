import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class HistoryPanel extends JPanel {

    AppFrame parent;
    private Dictionary dict;

    public HistoryPanel(AppFrame mparent, java.util.List<Transaction> transactions, int maxDisplayed) {
        super();
        parent = mparent;
        dict = parent.dict;
        try {
            initialize(transactions, maxDisplayed);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
    }

    private void initialize(java.util.List<Transaction> transactions, int maxDisplayed) throws SQLException {
        this.setBorder(new EmptyBorder(0, 10, 0, 0));
        this.setBackground(parent.bgColor);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        GridBagConstraints c = new GridBagConstraints();

        for (int j = 0; j < maxDisplayed && j < transactions.size(); j++) {
            Transaction transaction = transactions.get(j);

            JPanel transactionPanel = new JPanel();
            transactionPanel.setBackground(parent.bgColor);
            transactionPanel.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.weightx = 0.5;
            c.weighty = 0.5;

            JLabel tranTypeLabel = new JLabel(dict.getText("trans_type_"+transaction.getTransaction_type()));
            tranTypeLabel.setFont(new Font("Dialog", Font.BOLD, 12));
            c.gridx = 0;
            c.gridy = 0;
            transactionPanel.add(tranTypeLabel, c);

            JLabel tranTargetLabel = new JLabel(transaction.getTargetAccNo());
            tranTargetLabel.setFont(new Font("Dialog", Font.PLAIN, 9));
            c.gridx = 0;
            c.gridy = 1;
            transactionPanel.add(tranTargetLabel, c);

            JLabel tranDateLabel = new JLabel(transaction.getDate().toString());
            tranDateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 2;
            transactionPanel.add(tranDateLabel, c);

            JLabel tranAmountLabel = new JLabel(
                    Integer.toString(transaction.getAmount()) + " " +
                            transaction.getCurrency_abbr()
            );
            tranAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
            c.anchor = GridBagConstraints.EAST;
            c.gridx = 1;
            c.gridy = 0;
            c.gridheight = GridBagConstraints.REMAINDER;
            transactionPanel.add(tranAmountLabel, c);
            c.gridheight = 1;

            this.add(transactionPanel);
            this.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
}
