import javax.swing.*;

public class AppDialog extends JDialog {

    AppDialog(AppFrame owner, String title) {
        super(owner, title);
        this.setSize(160, 150);
        this.setLocationRelativeTo(owner);
        this.getContentPane().setBackground(owner.bgColor);
    }

    public static void wipDialog(AppFrame parent) {
//        TODO: Remove this when no longer necessary
        AppDialog wipDialog = new AppDialog(parent, "WIP");
        JLabel wip = new JLabel("WIP");
        wipDialog.add(wip);
        wipDialog.setVisible(true);
    }
}
