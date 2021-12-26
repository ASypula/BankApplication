import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginPanel extends JPanel {

    AppFrame parent;
    private JTextField usernameTf = null;
    private JPasswordField passwordPf = null;

    public LoginPanel(AppFrame mparent) {
        super();
        parent = mparent;
        initialize();
    }

    private void loginButtonClicked() throws SQLException {
        String login = usernameTf.getText();
        String pass = String.valueOf(passwordPf.getPassword());

        PersonalData client = Main.login(login, pass);

        if (client == null)
            wrongLoginDialog();
        else
            parent.changeToMainClient((Client) client);
    }

    private void wrongLoginDialog() {
        JDialog wrongLoginDialog = new JDialog(parent, "Błędne dane logowania");
        wrongLoginDialog.setSize(160, 150);
        JLabel wrongLoginText = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "Użytkownk o podanym<br />ID i haśle<br />nie istnieje</html>",
                SwingConstants.CENTER
        );
        wrongLoginDialog.add(wrongLoginText);
        wrongLoginDialog.setVisible(true);
    }

    private void wipDialog() {
        JDialog wipDialog = new JDialog(parent, "WIP");
        JLabel wip = new JLabel("WIP");
        wipDialog.add(wip);
        wipDialog.setSize(150, 150);
        wipDialog.setVisible(true);
    }

    private void initialize(){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel bankNameLabel = new JLabel("Casa de PAPel");
        bankNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankNameLabel.setMinimumSize(new Dimension(300, 300));
        bankNameLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        bankNameLabel.setForeground(Color.RED);
//            TODO: Define font more globally
        this.add(bankNameLabel);

        usernameTf = new JTextField("ID");
        usernameTf.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameTf.setMaximumSize(new Dimension(150, 25));
//            TODO: Define input field
        this.add(usernameTf);

        passwordPf = new JPasswordField("Hasło");
        passwordPf.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPf.setMaximumSize(new Dimension(150, 25));
        this.add(passwordPf);

        JButton loginButton = new JButton("Zaloguj się");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> {
            try {
                loginButtonClicked();
            } catch (SQLException ex) {
                System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            }
        });
        this.add(loginButton);

        JButton newClientButton = new JButton("Załóż konto: klient");
        newClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newClientButton.addActionListener(e -> wipDialog());
        this.add(newClientButton);

        JButton newEmployeeButton = new JButton("Załóż konto: pracownik");
        newEmployeeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newEmployeeButton.addActionListener(e -> wipDialog());
        this.add(newEmployeeButton);
    }

}
