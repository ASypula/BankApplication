import javax.swing.*;

public class AppFrame extends JFrame {

    public AppFrame() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(400, 400);
        this.setTitle("Casa de PAPel");
        this.setContentPane(new LoginPanel(this));
    }

    public void changeToMainClient(PersonalData client) {
        this.setContentPane(new MainClientPanel(this, client));
        this.revalidate();
    }
}
