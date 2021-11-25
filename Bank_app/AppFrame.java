import javax.swing.*;

public class AppFrame extends JFrame {

    public AppFrame() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(400, 400);
        this.setContentPane(new LoginPanel(this));
        this.setTitle("Casa de PAPel");
    }
}
