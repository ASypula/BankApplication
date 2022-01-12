import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class CardsClientPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    public CardsClientPanel(AppFrame mparent, Client mclient) {
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

        JLabel cardsLabel = new JLabel(dict.getText("my_cards"));
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        this.add(cardsLabel, c);

        WhiteButton newCardButton = new WhiteButton(dict.getText("order_new_card"));
        newCardButton.addActionListener(e -> AppDialog.contactEmployeeDialog(parent));
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 1;
        this.add(newCardButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weighty = 0.8;

        java.util.List<Card> allCards = new ArrayList<Card>();
        java.util.List<BankAccount> accounts = client.getBankAccounts();
        for (BankAccount account : accounts) {
            java.util.List<Card> cards = account.getCards();
            allCards.addAll(cards);
        }

        if (allCards.isEmpty()) {
            JLabel noneLabel = new JLabel(dict.getText("no_cards"), SwingConstants.CENTER);
            c.fill = GridBagConstraints.BOTH;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weighty = 0.8;
            c.gridx = 0;
            c.gridy = 2;
            this.add(noneLabel, c);
        } else {
            JPanel cardsPanel = new JPanel();
            cardsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            cardsPanel.setBackground(parent.bgColor);
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));

            for (BankAccount account : accounts) {
                java.util.List<Card> cards = account.getCards();
                for (Card card : cards) {
                    JPanel cardPanel = new JPanel();
                    cardPanel.setBackground(parent.bgColor);
                    cardPanel.setLayout(new GridBagLayout());
                    c.fill = GridBagConstraints.NONE;
                    c.anchor = GridBagConstraints.WEST;
                    c.weightx = 0.5;
                    c.weighty = 0.5;

                    JLabel cardIdLabel = new JLabel(
                            "<html>" + dict.getText("card_id") + "<b>" +
                                    card.getCard_id() + "</b></html>"
                    );
                    cardIdLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                    c.gridx = 0;
                    c.gridy = 0;
                    cardPanel.add(cardIdLabel, c);

                    JLabel cardTypeLabel = new JLabel(
                            "<html>" + dict.getText("card_type") + "<b>" +
                                    dict.getText("card_type_"+card.getCard_type()) + "</b></html>"
                    );
                    cardTypeLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                    c.gridx = 0;
                    c.gridy = 1;
                    cardPanel.add(cardTypeLabel, c);

                    JLabel expirationDateLabel = new JLabel(
                            "<html>" + dict.getText("card_valid_until") + "<b>" +
                                    card.getExpiration_date().toString() + "</b></html>"
                    );
                    expirationDateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                    c.gridx = 0;
                    c.gridy = 2;
                    cardPanel.add(expirationDateLabel, c);

                    JLabel accountNrLabel = new JLabel(
                            "<html>" + dict.getText("linked_acc_no") + "<b>" +
                                    account.getAccount_no() + "</b></html>"
                    );
                    accountNrLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                    c.gridx = 0;
                    c.gridy = 3;
                    cardPanel.add(accountNrLabel, c);

                    c.anchor = GridBagConstraints.EAST;

                    JLabel currentLabel = new JLabel(dict.getText("available_funds"));
                    currentLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                    c.gridx = 1;
                    c.gridy = 0;
                    cardPanel.add(currentLabel, c);

                    JLabel currentAmountLabel = new JLabel(
                            Integer.toString(account.getBalance()) + " " + account.getCurrency_abbr()
                    );
                    currentAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));
                    c.gridx = 1;
                    c.gridy = 1;
                    c.gridheight = 3;
                    cardPanel.add(currentAmountLabel, c);

                    c.gridheight = 1;

                    cardsPanel.add(cardPanel);
                    cardsPanel.add(Box.createRigidArea(new Dimension(0, 60)));
                }
            }

            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridx = 0;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.8;
            this.add(cardsPanel, c);
        }

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridy = 3;
        c.weighty = 0.02;
        c.gridheight = 1;
        this.add(new LanguageButtonsPanel(parent, "cardsClient", client), c);
    }
}
