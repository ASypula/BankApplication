import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class PageButtonsPanel extends JPanel {

    AppFrame parent;

    PageButtonsPanel(AppFrame mparent) {
        super();
        parent = mparent;
        initialize();
    }

    private void initialize() {
        this.setBackground(parent.bgColor);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.5;

        JLabel bankNameLabel = new JLabel("Casa de PAPel", SwingConstants.CENTER);
        bankNameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        c.gridx = 0;
        c.gridy = 0;
        this.add(bankNameLabel, c);

        WhiteButton mainPageButton = new WhiteButton("Główna");
        c.gridx = 1;
        c.gridy = 0;
        this.add(mainPageButton, c);

        WhiteButton cardsButton = new WhiteButton("Karty");
        c.gridx = 2;
        c.gridy = 0;
        this.add(cardsButton, c);

        WhiteButton creditsButton = new WhiteButton("Kredyty");
        c.gridx = 3;
        c.gridy = 0;
        this.add(creditsButton, c);

        WhiteButton depositsButton = new WhiteButton("Lokaty");
        c.gridx = 4;
        c.gridy = 0;
        this.add(depositsButton, c);

        WhiteButton helpButton = new WhiteButton("Pomoc");
        c.gridx = 5;
        c.gridy = 0;
        this.add(helpButton, c);

        WhiteButton logoutButton = new WhiteButton("Wyloguj");
        logoutButton.addActionListener(e -> parent.changeToLogin());
        c.gridx = 6;
        c.gridy = 0;
        this.add(logoutButton, c);
    }
}
