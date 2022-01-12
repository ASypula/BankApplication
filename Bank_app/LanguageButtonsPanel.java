import javax.swing.*;
import java.awt.*;

public class LanguageButtonsPanel extends JPanel{

    private final AppFrame parent;
    private Dictionary dict;
    private final String panel;
    private final PersonalData person;

    private void changeLanguage(String lang) {
        parent.lang = lang;
        parent.dict = new Dictionary(lang);
        dict = parent.dict;
        switch (panel) {
            case "login" -> parent.changeToLogin();
            case "mainClient" -> parent.changeToMainClient((Client) person);
            case "cardsClient" -> parent.changeToCardsClient((Client) person);
            case "loansClient" -> parent.changeToLoansClient((Client) person);
            case "depositsClient" -> parent.changeToDepositsClient((Client) person);
            case "helpClient" -> parent.changeToHelpClient((Client) person);
            case "employee" -> parent.changeToEmployee((Employee) person);
        }
    }

    LanguageButtonsPanel(AppFrame parent, String panel, PersonalData person) {
        super();
        this.parent = parent;
        this.dict = parent.dict;
        this.panel = panel;
        this.person = person;

        initialize();
    }

    private void initialize() {
        this.setBackground(parent.bgColor);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTHEAST;

        Font font = new Font("Dialog", Font.BOLD, 9);

        WhiteButton polButton = new WhiteButton("Polski");
        polButton.setFont(font);
        polButton.setForeground(Color.RED);
        polButton.addActionListener(e -> changeLanguage("Pol"));
        c.gridx = 0;
        c.gridy = 0;
        this.add(polButton, c);

        WhiteButton engButton = new WhiteButton("English");
        engButton.setFont(font);
        engButton.setForeground(Color.RED);
        engButton.addActionListener(e -> changeLanguage("Eng"));
        c.gridx = 1;
        c.gridy = 0;
        this.add(engButton, c);

        WhiteButton japButton = new WhiteButton("日本");
        japButton.setFont(font);
        japButton.setForeground(Color.RED);
        japButton.addActionListener(e -> changeLanguage("Jap"));
        c.gridx = 2;
        c.gridy = 0;
        this.add(japButton, c);
    }
}
