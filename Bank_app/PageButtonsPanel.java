import javax.swing.*;
import java.awt.*;

public class PageButtonsPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    PageButtonsPanel(AppFrame mparent, Client mclient) {
        super();
        parent = mparent;
        client = mclient;
        dict = parent.dict;
        initialize();
    }

    private void initialize() {
        this.setBackground(parent.bgColor);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;

        JLabel bankNameLabel = new JLabel("Casa de PAPel", SwingConstants.CENTER);
        bankNameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        c.gridx = 0;
        c.gridy = 0;
        this.add(bankNameLabel, c);

        WhiteButton mainPageButton = new WhiteButton(dict.getText("main"));
        mainPageButton.addActionListener(e -> parent.changeToMainClient(client));
        c.gridx = 1;
        c.gridy = 0;
        this.add(mainPageButton, c);

        WhiteButton cardsButton = new WhiteButton(dict.getText("cards"));
        cardsButton.addActionListener(e -> parent.changeToCardsClient(client));
        c.gridx = 2;
        c.gridy = 0;
        this.add(cardsButton, c);

        WhiteButton creditsButton = new WhiteButton(dict.getText("credits"));
        creditsButton.addActionListener(e -> parent.changeToLoansClient(client));
        c.gridx = 3;
        c.gridy = 0;
        this.add(creditsButton, c);

        WhiteButton depositsButton = new WhiteButton(dict.getText("deposits"));
        depositsButton.addActionListener(e -> parent.changeToDepositsClient(client));
        c.gridx = 4;
        c.gridy = 0;
        this.add(depositsButton, c);

        WhiteButton helpButton = new WhiteButton(dict.getText("help"));
        helpButton.addActionListener(e -> parent.changeToHelpClient(client));
        c.gridx = 5;
        c.gridy = 0;
        this.add(helpButton, c);

        WhiteButton logoutButton = new WhiteButton(dict.getText("logout"));
        logoutButton.addActionListener(e -> parent.changeToLogin());
        c.gridx = 6;
        c.gridy = 0;
        this.add(logoutButton, c);
    }
}
