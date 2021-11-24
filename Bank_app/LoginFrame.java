import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JPanel jContentPane = null;
    private JTextField usernameTf = null;
    private JPasswordField passwordPf = null;
    private JButton loginButton = null;

    public LoginFrame() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(400, 400);
        this.setContentPane(getJContentPane());
        this.setTitle("Casa de PAPel");
    }

    private void loginButtonClicked() {
        JDialog loginDialog = new JDialog(this, "Login Credentials");
        loginDialog.setLayout(new FlowLayout());
        JLabel login = new JLabel(usernameTf.getText());
        JLabel pass = new JLabel(String.valueOf(passwordPf.getPassword()));
        loginDialog.add(login);
        loginDialog.add(pass);
        loginDialog.setSize(150, 150);
        loginDialog.setVisible(true);
    }

    private void wipDialog() {
        JDialog wipDialog = new JDialog(this, "WIP");
        JLabel wip = new JLabel("WIP");
        wipDialog.add(wip);
        wipDialog.setSize(150, 150);
        wipDialog.setVisible(true);
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.PAGE_AXIS));

            JLabel bankNameLabel = new JLabel("Casa de PAPel");
            bankNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            bankNameLabel.setMinimumSize(new Dimension(300, 300));
            bankNameLabel.setFont(new Font("Serif", Font.PLAIN, 48));
//            TODO: Define font more globally
            jContentPane.add(bankNameLabel);

            usernameTf = new JTextField("Login");
            usernameTf.setAlignmentX(Component.CENTER_ALIGNMENT);
            usernameTf.setMaximumSize(new Dimension(150, 25));
//            TODO: Define input field
            jContentPane.add(usernameTf);

            passwordPf = new JPasswordField("Hasło");
            passwordPf.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordPf.setMaximumSize(new Dimension(150, 25));
            jContentPane.add(passwordPf);

            loginButton = new JButton("Zaloguj się");
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loginButtonClicked();
                }
            });
            jContentPane.add(loginButton);

            JButton newClientButton = new JButton("Załóż konto: klient");
            newClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            newClientButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    wipDialog();
                }
            });
            jContentPane.add(newClientButton);

            JButton newEmployeeButton = new JButton("Załóż konto: pracownik");
            newEmployeeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            newEmployeeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    wipDialog();
                }
            });
            jContentPane.add(newEmployeeButton);
        }

        return jContentPane;
    }

}
