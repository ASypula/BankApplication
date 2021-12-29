import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppDialog extends JDialog {

    AppDialog(AppFrame owner, String title, int width, int height) {
        super(owner, title);
        this.getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setSize(width, height);
        this.setLocationRelativeTo(owner);
        this.getRootPane().setBackground(owner.bgColor);
        this.getContentPane().setBackground(owner.bgColor);
    }

    public static void wipDialog(AppFrame parent) {
//        TODO: Remove this when no longer necessary
        AppDialog wipDialog = new AppDialog(parent, "WIP", 100, 100);
        JLabel wip = new JLabel("WIP");
        wipDialog.add(wip);
        wipDialog.setVisible(true);
    }

    public static void contactEmployeeDialog(AppFrame parent) {
        AppDialog contactDialog = new AppDialog(parent, "Skontaktuj się z pracownikiem", 250, 250);
        JLabel contactLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                    "Ta operacja nie jest dostępna<br />" +
                    "w wersji online.<br />" +
                    "Aby dokonać wybranej czynności<br />" +
                    "skontaktuj się z naszym pracownikiem.<br />" +
                    "Informację jak to zrobić<br />" +
                    "można znaleźć w zakładce <b>Pomoc</b>.</div></html>",
                SwingConstants.CENTER
        );
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        contactDialog.add(contactLabel);
        contactDialog.setVisible(true);
    }
}
