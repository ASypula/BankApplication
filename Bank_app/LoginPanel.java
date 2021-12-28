import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginPanel extends JPanel {

    AppFrame parent;
    private JTextField usernameTf = null;
    private JPasswordField passwordPf = null;
    private Dictionary dict;

    public LoginPanel(AppFrame mparent) {
        super();
        parent = mparent;
        dict = parent.dict;
        initialize();
    }

    private void changeLanguage(String lang) {
        parent.lang = lang;
        parent.dict = new Dictionary(lang);
        dict = parent.dict;
        parent.changeToLogin();
    }

    private void loginButtonClickedClient() throws SQLException {
        String login = usernameTf.getText();
        String pass = String.valueOf(passwordPf.getPassword());

        try {
            PersonalData client = Main.login(login, pass);

            if (client == null)
                wrongLoginDialog();
            else
                parent.changeToMainClient((Client) client);
        }
        catch (SQLException e) {
            wrongLoginDialog();
        }
    }

    private void wrongLoginDialog() {
        AppDialog wrongLoginDialog = new AppDialog(parent, "Błędne dane");
        JLabel wrongLoginText = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "Użytkownk o podanym<br />ID i haśle<br />nie istnieje</html>",
                SwingConstants.CENTER
        );
        wrongLoginDialog.add(wrongLoginText);
        wrongLoginDialog.setVisible(true);
    }

    private void wipDialog() {
//        TODO: Remove this when no longer necessary
        AppDialog wipDialog = new AppDialog(parent, "WIP");
        JLabel wip = new JLabel("WIP");
        wipDialog.add(wip);
        wipDialog.setVisible(true);
    }

    private void initialize(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.weighty = 0.05;

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.9;
        this.add(Box.createRigidArea(new Dimension(0, 0)), c);

        c.weightx = 0.05;

        WhiteButton polButton = new WhiteButton("Polski");
        polButton.addActionListener(e -> changeLanguage("Pol"));
        c.gridx = 1;
        c.gridy = 0;
        this.add(polButton, c);

        WhiteButton engButton = new WhiteButton("English");
        engButton.addActionListener(e -> changeLanguage("Eng"));
        c.gridx = 2;
        c.gridy = 0;
        this.add(engButton, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridwidth = GridBagConstraints.REMAINDER;

        JLabel bankNameLabel = new JLabel("Casa de PAPel", SwingConstants.CENTER);
        bankNameLabel.setFont(new Font("Serif", Font.BOLD, 60));
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.6;
        this.add(bankNameLabel, c);

        JPanel loginDetPan = new JPanel();
        loginDetPan.setLayout(new BoxLayout(loginDetPan, BoxLayout.PAGE_AXIS));
        loginDetPan.setBackground(parent.bgColor);
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.35;
        this.add(loginDetPan, c);

        ActionListener loginActionListener = e -> {
            try {
                loginButtonClickedClient();
            } catch (SQLException ex) {
                wrongLoginDialog();
//                TODO: Add try for employee login
            }
        };

        usernameTf = new JTextField("ID");
        usernameTf.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameTf.setMaximumSize(new Dimension(150, 25));
        usernameTf.addActionListener(loginActionListener);
        loginDetPan.add(usernameTf);

        passwordPf = new JPasswordField(dict.getText("pwd"));
        passwordPf.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPf.setMaximumSize(new Dimension(150, 25));
        passwordPf.addActionListener(loginActionListener);
        loginDetPan.add(passwordPf);

        WhiteButton loginButton = new WhiteButton(dict.getText("login"));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(loginActionListener);
        loginDetPan.add(loginButton);

        loginDetPan.add(Box.createRigidArea(new Dimension(0, 80)));

        WhiteButton newClientButton = new WhiteButton("Załóż konto: klient");
        newClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newClientButton.addActionListener(e -> wipDialog());
//        TODO: Add adding client
        loginDetPan.add(newClientButton);

        WhiteButton newEmployeeButton = new WhiteButton("Załóż konto: pracownik");
        newEmployeeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newEmployeeButton.addActionListener(e -> wipDialog());
//        TODO: Add adding employee
        loginDetPan.add(newEmployeeButton);
    }

}
