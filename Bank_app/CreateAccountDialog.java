import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class CreateAccountDialog extends AppDialog {

    public PersonalData personalData;
    public String newId;

    private JTextField nameTf = new JTextField();
    private JTextField surnameTf = new JTextField();
    private JTextField peselTf = new JTextField();
    private JTextField phoneTf = new JTextField();
    private JTextField streetTf = new JTextField();
    private JTextField houseNrTf = new JTextField();
    private JTextField cityTf = new JTextField();
    private JPasswordField passwordPf = new JPasswordField();

    private AppFrame owner;
    private Boolean createClient;
    private Dictionary dict;

    CreateAccountDialog(AppFrame mowner, String title, Boolean createCli) {
        super(mowner, title, 200, 400);
        owner = mowner;
        createClient = createCli;
        dict = owner.dict;

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
        JLabel passwordLabel = new JLabel(dict.getText("pwd"));
        passwordBox.add(Box.createRigidArea(new Dimension(10, 0)));
        passwordBox.add(passwordLabel);
        passwordBox.add(Box.createHorizontalGlue());
        creAccPan.add(passwordBox);

        passwordPf.setMaximumSize(new Dimension(150, 25));
        passwordPf.addActionListener(okListener);
        creAccPan.add(passwordPf);

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
            personalData = new PersonalData(
                    nameTf.getText(),
                    surnameTf.getText(),
                    peselTf.getText(),
                    phoneTf.getText(),
                    address.getAddress_id()
            );
//            TODO: Verify input?
            personalData.insert(String.valueOf(passwordPf.getPassword()));

            successDialog();
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            incorrectDataDialog();
//                TODO: Tell what is wrong?
        }
        catch (NumberFormatException ex) {
            incorrectDataDialog();
//                TODO: Tell what is wrong?
        }
    }

    private void incorrectDataDialog() {
        AppDialog incorrDataDialog = new AppDialog(owner, dict.getText("invalid_data"), 160, 150);
        JLabel incorrDataText = new JLabel(
                "<html><div style='text-align: center;'>"+ dict.getText("given_invalid_data_1") +"<br />" +
                dict.getText("given_invalid_data_2") +"<br />" +
                dict.getText("given_invalid_data_3") +"</html>",
                SwingConstants.CENTER
        );
        incorrDataDialog.add(incorrDataText);
        incorrDataDialog.setVisible(true);
    }

    private void successDialog() {
        try {
            if (createClient) {
                Client cli = new Client(personalData.getData_id());
                newId = cli.getClient_id();
            } else {
                Employee emp = new Employee(personalData.getData_id());
                newId = emp.getEmployees_id();
            }
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            incorrectDataDialog();
            return;
        } catch(WrongId ex) {
            incorrectDataDialog();
            return;
        }

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
