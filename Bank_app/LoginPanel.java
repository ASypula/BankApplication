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

    private void loginButtonClicked() throws SQLException {
        String login = usernameTf.getText();
        String pass = String.valueOf(passwordPf.getPassword());

        try {
            PersonalData person = Main.login(login, pass);

            if (person == null)
                wrongLoginDialog();
            else {
                try {
                    Client cli = new Client(login);
                    parent.changeToMainClient(cli);
                } catch (WrongId e) {
                    try {
                        Employee emp = Employee.getEmployeeFromPersonalDataId(login);
                        parent.changeToEmployee(emp);
                    } catch (WrongId ignored) {}
                }
            }
        }
        catch (SQLException e) {
            wrongLoginDialog();
        }
    }

    private void wrongLoginDialog() {
        AppDialog wrongLoginDialog = new AppDialog(parent, dict.getText("invalid_data"), 160, 150);
        JLabel wrongLoginText = new JLabel(
                "<html><div style='text-align: center;'>" +
                        dict.getText("wrong_login_1")+ "<br />ID "+
                        dict.getText("wrong_login_2") + "<br />" +
                        dict.getText("wrong_login_3") + "</html>",
                SwingConstants.CENTER
        );
        wrongLoginDialog.add(wrongLoginText);
        wrongLoginDialog.setVisible(true);
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

        Icon polFlag = new ImageIcon("Flags/flag_Poland.png");
        JButton polButton = new JButton(polFlag);
        polButton.addActionListener(e -> changeLanguage("Pol"));
        polButton.setPreferredSize(new Dimension(45, 30));
        c.gridx = 1;
        c.gridy = 0;
        this.add(polButton, c);

        Icon engFlag = new ImageIcon("Flags/flag_UK.png");
        JButton engButton = new JButton(engFlag);
        engButton.addActionListener(e -> changeLanguage("Eng"));
        engButton.setPreferredSize(new Dimension(45, 30));
        c.gridx = 2;
        c.gridy = 0;
        this.add(engButton, c);

        Icon japFlag = new ImageIcon("Flags/flag_Japan.png");
        JButton japButton = new JButton(japFlag);
        japButton.addActionListener(e -> changeLanguage("Jap"));
        japButton.setIcon(japFlag);
        japButton.setPreferredSize(new Dimension(45, 30));
        c.gridx = 3;
        c.gridy = 0;
        this.add(japButton, c);

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
                loginButtonClicked();
            } catch (SQLException ex) {
                wrongLoginDialog();
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

        WhiteButton newClientButton = new WhiteButton(dict.getText("create_acc_client"));
        newClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newClientButton.addActionListener(e ->
                new CreateAccountDialog(parent, dict.getText("create_acc_client"), true)
        );
        loginDetPan.add(newClientButton);

        WhiteButton newEmployeeButton = new WhiteButton(dict.getText("create_acc_emp"));
        newEmployeeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newEmployeeButton.addActionListener(e ->
                new CreateAccountDialog(parent, dict.getText("create_acc_emp"), false)
        );
        loginDetPan.add(newEmployeeButton);
    }
}
