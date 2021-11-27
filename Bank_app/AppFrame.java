import javax.swing.*;
import java.sql.SQLException;

public class AppFrame extends JFrame {

    public AppFrame() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(600, 600);
        this.setTitle("Casa de PAPel");
        changeToLogin();
    }

    public void changeToLogin() {
        this.setContentPane(new LoginPanel(this));
        this.revalidate();
    }

    public void changeToMainClient(Client client) {
        this.setContentPane(new MainClientPanel(this, client));
        this.revalidate();
    }
}
