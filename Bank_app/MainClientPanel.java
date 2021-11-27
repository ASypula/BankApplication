import javax.swing.*;
import java.awt.*;
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

        java.util.List<BankAccount> accounts = client.getBankAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            JLabel accountLabel = new JLabel(
                    "Dostępne środki: " +
                            accounts.get(i).getBalance()
            );
            accountLabel.setFont(new Font("Arial", Font.BOLD, 18));
            c.gridx = 0;
            c.gridy = (i+1)*2;
            c.gridwidth = 4;
            this.add(accountLabel, c);

            JButton depositButton = new JButton("Wpłać");
            c.gridx = 1;
            c.gridy = (i+1)*2 + 1;
            c.gridwidth = 1;
            this.add(depositButton, c);

            JButton withdrawButton = new JButton("Wypłać");
            c.gridx = 2;
            c.gridy = (i+1)*2 + 1;
            this.add(withdrawButton, c);

            JButton transferButton = new JButton("Przelew");
            c.gridx = 3;
            c.gridy = (i+1)*2 + 1;
            this.add(transferButton, c);
        }
    }
}
