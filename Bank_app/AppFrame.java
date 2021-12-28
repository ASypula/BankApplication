import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AppFrame extends JFrame {

    Color bgColor = new Color(255, 230, 230);
    String lang = "Jap";
    Dictionary dict = new Dictionary(lang);

    public AppFrame() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setTitle("Casa de PAPel");
        changeToLogin();
    }

    public void changeToLogin() {
        this.setContentPane(new LoginPanel(this));
        this.getContentPane().setBackground(bgColor);
        this.revalidate();
    }

    public void changeToMainClient(Client client) {
        this.setContentPane(new MainClientPanel(this, client));
        this.getContentPane().setBackground(bgColor);
        this.revalidate();
    }

    public void changeToCardsClient(Client client) {
        this.setContentPane(new CardsClientPanel(this, client));
        this.getContentPane().setBackground(bgColor);
        this.revalidate();
    }

    public void changeToCreditsClient(Client client) {
        this.setContentPane(new CreditsClientPanel(this, client));
        this.getContentPane().setBackground(bgColor);
        this.revalidate();
    }

    public void changeToDepositsClient(Client client) {
        this.setContentPane(new DepositsClientPanel(this, client));
        this.getContentPane().setBackground(bgColor);
        this.revalidate();
    }

    public void changeToHelpClient(Client client) {
        this.setContentPane(new HelpClientPanel(this, client));
        this.getContentPane().setBackground(bgColor);
        this.revalidate();
    }
}
