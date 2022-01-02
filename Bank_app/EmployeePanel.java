import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {

    AppFrame parent;
    Employee employee;
    private Dictionary dict;

    public EmployeePanel(AppFrame mparent, Employee memployee) {
        super();
        parent = mparent;
        employee = memployee;
        dict = parent.dict;
        initialize();
    }

    private void initialize() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.weighty = 0.02;

        JLabel bankNameLabel = new JLabel("Casa de PAPel");
        bankNameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        this.add(bankNameLabel, c);

        WhiteButton logoutButton = new WhiteButton(dict.getText("logout"));
        logoutButton.addActionListener(e -> parent.changeToLogin());
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.gridx = 1;
        c.gridy = 0;
        this.add(logoutButton, c);

        JLabel employeeLabel = new JLabel(dict.getText("am_employee"), SwingConstants.CENTER);
        employeeLabel.setFont(new Font("Dialog", Font.BOLD, 40));
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weighty = 0.98;
        this.add(employeeLabel, c);
    }
}
