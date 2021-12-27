import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class OkCancelButtonsPanel extends JPanel {

    OkCancelButtonsPanel(AppFrame parent, JDialog owner, ActionListener okActionListener) {
        super();

        this.setBackground(parent.bgColor);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;

        WhiteButton okButton = new WhiteButton("Zatwierdź");
        okButton.addActionListener(okActionListener);
        c.gridx = 0;
        c.gridy = 0;
        this.add(okButton, c);

        WhiteButton cancelButton = new WhiteButton("Anuluj");
        cancelButton.addActionListener(e -> {
            owner.setVisible(false);
            owner.dispatchEvent(
                    new WindowEvent(owner, WindowEvent.WINDOW_CLOSING)
            );
        });
        c.gridx = 1;
        c.gridy = 0;
        this.add(cancelButton, c);
    }
}
