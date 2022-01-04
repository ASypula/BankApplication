import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Vector;

public class CreateAccountDialog extends AppDialog {

    public Client client;
    public Employee employee;
    public String newId;

    private final JTextField nameTf = new JTextField();
    private final JTextField surnameTf = new JTextField();
    private final JTextField peselTf = new JTextField();
    private final JTextField phoneTf = new JTextField();
    private final JTextField streetTf = new JTextField();
    private final JTextField houseNrTf = new JTextField();
    private final JTextField cityTf = new JTextField();
    private final JPasswordField passwordPf = new JPasswordField();
    private final JTextField salaryTf = new JTextField();
    private JComboBox<String> professionCb;
    private JComboBox<String> branchCb;

    private final AppFrame owner;
    private final Boolean createClient;
    private final Dictionary dict;

    CreateAccountDialog(AppFrame mowner, String title, Boolean createCli) {
        super(mowner, title, 200, 400);
        owner = mowner;
        createClient = createCli;
        dict = owner.dict;

        if (!createCli) {
            this.setSize(200, 500);
            this.setLocationRelativeTo(owner);

            try {
                professionCb = new JComboBox<String>(new Vector<String>(Employee.getProfessionNames()));
                professionCb.insertItemAt("", 0);
                professionCb.setSelectedIndex(0);

                branchCb = new JComboBox<String>(new Vector<String>(Branch.getBranches()));
                branchCb.insertItemAt("", 0);
                branchCb.setSelectedIndex(0);
            } catch (SQLException ex) {
                System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            }
        }

        ActionListener okListener = e -> createPerson();

        JPanel creAccPan = new JPanel();
        creAccPan.setBackground(owner.bgColor);
        creAccPan.setLayout(new BoxLayout(creAccPan, BoxLayout.Y_AXIS));
        this.add(creAccPan);

        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel(dict.getText("name"));
        nameBox.add(Box.createRigidArea(new Dimension(10, 0)));
        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalGlue());
        creAccPan.add(nameBox);

        nameTf.setMaximumSize(new Dimension(150, 25));
        nameTf.addActionListener(okListener);
        creAccPan.add(nameTf);

        Box surnameBox = Box.createHorizontalBox();
        JLabel surnameLabel = new JLabel(dict.getText("surname"));
        surnameBox.add(Box.createRigidArea(new Dimension(10, 0)));
        surnameBox.add(surnameLabel);
        surnameBox.add(Box.createHorizontalGlue());
        creAccPan.add(surnameBox);

        surnameTf.setMaximumSize(new Dimension(150, 25));
        surnameTf.addActionListener(okListener);
        creAccPan.add(surnameTf);

        Box peselBox = Box.createHorizontalBox();
        JLabel peselLabel = new JLabel(dict.getText("pesel"));
        peselBox.add(Box.createRigidArea(new Dimension(10, 0)));
        peselBox.add(peselLabel);
        peselBox.add(Box.createHorizontalGlue());
        creAccPan.add(peselBox);

        peselTf.setMaximumSize(new Dimension(150, 25));
        peselTf.addActionListener(okListener);
        creAccPan.add(peselTf);

        Box phoneBox = Box.createHorizontalBox();
        JLabel phoneLabel = new JLabel(dict.getText("phone_no"));
        phoneBox.add(Box.createRigidArea(new Dimension(10, 0)));
        phoneBox.add(phoneLabel);
        phoneBox.add(Box.createHorizontalGlue());
        creAccPan.add(phoneBox);

        phoneTf.setMaximumSize(new Dimension(150, 25));
        phoneTf.addActionListener(okListener);
        creAccPan.add(phoneTf);

        Box streetBox = Box.createHorizontalBox();
        JLabel streetLabel = new JLabel(dict.getText("street"));
        streetBox.add(Box.createRigidArea(new Dimension(10, 0)));
        streetBox.add(streetLabel);
        streetBox.add(Box.createHorizontalGlue());
        creAccPan.add(streetBox);

        streetTf.setMaximumSize(new Dimension(150, 25));
        streetTf.addActionListener(okListener);
        creAccPan.add(streetTf);

        Box houseNrBox = Box.createHorizontalBox();
        JLabel houseNrLabel = new JLabel(dict.getText("house_no"));
        houseNrBox.add(Box.createRigidArea(new Dimension(10, 0)));
        houseNrBox.add(houseNrLabel);
        houseNrBox.add(Box.createHorizontalGlue());
        creAccPan.add(houseNrBox);

        houseNrTf.setMaximumSize(new Dimension(150, 25));
        houseNrTf.addActionListener(okListener);
        creAccPan.add(houseNrTf);

        Box cityBox = Box.createHorizontalBox();
        JLabel cityLabel = new JLabel(dict.getText("city"));
        cityBox.add(Box.createRigidArea(new Dimension(10, 0)));
        cityBox.add(cityLabel);
        cityBox.add(Box.createHorizontalGlue());
        creAccPan.add(cityBox);

        cityTf.setMaximumSize(new Dimension(150, 25));
        cityTf.addActionListener(okListener);
        creAccPan.add(cityTf);

        Box passwordBox = Box.createHorizontalBox();
        JLabel passwordLabel = new JLabel(dict.getText("pwd") + ":");
        passwordBox.add(Box.createRigidArea(new Dimension(10, 0)));
        passwordBox.add(passwordLabel);
        passwordBox.add(Box.createHorizontalGlue());
        creAccPan.add(passwordBox);

        passwordPf.setMaximumSize(new Dimension(150, 25));
        passwordPf.addActionListener(okListener);
        creAccPan.add(passwordPf);

        if (!createCli) {
            Box salaryBox = Box.createHorizontalBox();
            JLabel salaryLabel = new JLabel(dict.getText("salary"));
            salaryBox.add(Box.createRigidArea(new Dimension(10, 0)));
            salaryBox.add(salaryLabel);
            salaryBox.add(Box.createHorizontalGlue());
            creAccPan.add(salaryBox);

            salaryTf.setMaximumSize(new Dimension(150, 25));
            salaryTf.addActionListener(okListener);
            creAccPan.add(salaryTf);

            Box professionBox = Box.createHorizontalBox();
            JLabel professionLabel = new JLabel(dict.getText("profession"));
            professionBox.add(Box.createRigidArea(new Dimension(10, 0)));
            professionBox.add(professionLabel);
            professionBox.add(Box.createHorizontalGlue());
            creAccPan.add(professionBox);

            professionCb.setMaximumSize(new Dimension(150, 25));
            creAccPan.add(professionCb);

            Box branchBox = Box.createHorizontalBox();
            JLabel branchLabel = new JLabel(dict.getText("branch_id"));
            branchBox.add(Box.createRigidArea(new Dimension(10, 0)));
            branchBox.add(branchLabel);
            branchBox.add(Box.createHorizontalGlue());
            creAccPan.add(branchBox);

            branchCb.setMaximumSize(new Dimension(150, 25));
            creAccPan.add(branchCb);
        }

        OkCancelButtonsPanel okCanButPan = new OkCancelButtonsPanel(owner, this, okListener);
        creAccPan.add(okCanButPan);

        this.setVisible(true);
    }

    private void createPerson() {
        try {
            Address address = new Address(
                    cityTf.getText(),
                    streetTf.getText(),
                    Integer.parseInt(houseNrTf.getText())
            );
            address.insert();

            if (createClient) {
                client = new Client(
                        nameTf.getText(),
                        surnameTf.getText(),
                        peselTf.getText(),
                        phoneTf.getText(),
                        address.getAddress_id()
                );
                client.insert(String.valueOf(passwordPf.getPassword()));
                newId = client.getData_id();
            } else {
                employee = new Employee(
                        nameTf.getText(),
                        surnameTf.getText(),
                        peselTf.getText(),
                        phoneTf.getText(),
                        address.getAddress_id(),
                        salaryTf.getText(),
                        Employee.getProfessionId((String) professionCb.getSelectedItem()),
                        (String) branchCb.getSelectedItem()
                );
                employee.insert(String.valueOf(passwordPf.getPassword()));
                newId = employee.getData_id();
            }

            successDialog();
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            fillInAllDialog();
        }
        catch (NumberFormatException ex) {
            System.err.format(ex.getMessage());
            houseNumberDialog();
        }
    }

    private void houseNumberDialog() {
        AppDialog houseNumDialog = new AppDialog(owner, dict.getText("invalid_data"), 160, 150);
        JLabel houseNumText = new JLabel(
                "<html><div style='text-align: center;'>"+dict.getText("invalid_house_no_1")+"<br />" +
                dict.getText("invalid_house_no_2")+"</div></html>",
                SwingConstants.CENTER
        );
        houseNumDialog.add(houseNumText);
        houseNumDialog.setVisible(true);
    }

    private void fillInAllDialog() {
        AppDialog fillInDialog = new AppDialog(owner, dict.getText("invalid_data"), 160, 150);
        JLabel fillInText = new JLabel(
                "<html><div style='text-align: center;'>"+dict.getText("not_all_data_1")+"<br />" +
                dict.getText("not_all_data_2")+"</div></html>",
                SwingConstants.CENTER
        );
        fillInDialog.add(fillInText);
        fillInDialog.setVisible(true);
    }

    private void successDialog() {
        AppDialog succDialog = new AppDialog(owner, dict.getText("your_login"), 200, 200);
        JPanel succPan = new JPanel();
        succPan.setBackground(owner.bgColor);
        succPan.setLayout(new BoxLayout(succPan, BoxLayout.PAGE_AXIS));
        succDialog.add(succPan);

        JLabel idText = new JLabel(
                "<html><div style='text-align: center;'>"+ dict.getText("created_acc_1") +"<br />" +
                dict.getText("created_acc_2") +"<b style='color:red'>" + newId + "</b><br />" +
                dict.getText("created_acc_3") +"<br />" +
                dict.getText("created_acc_4") +"<br />" +
                dict.getText("created_acc_5"),
                SwingConstants.CENTER
        );
        idText.setAlignmentX(Component.CENTER_ALIGNMENT);
        succPan.add(idText);

        WhiteButton okButton = new WhiteButton(dict.getText("ok"));
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> {
            succDialog.setVisible(false);
            succDialog.dispatchEvent(
                    new WindowEvent(succDialog, WindowEvent.WINDOW_CLOSING)
            );

            this.setVisible(false);
            this.dispatchEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
            );
        });
        succPan.add(okButton);

        succDialog.setVisible(true);
    }
}
