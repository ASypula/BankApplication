import javax.swing.*;

public class AppDialog extends JDialog {

    AppDialog(AppFrame owner, String title) {
        super(owner, title);
        this.setSize(160, 150);
        this.setLocationRelativeTo(owner);
        this.getContentPane().setBackground(owner.bgColor);
    }
}
