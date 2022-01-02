import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class UpdateDataDialog extends AppDialog {

    public Client client;

    private final JTextField phoneTf = new JTextField();
    private final JTextField streetTf = new JTextField();
    private final JTextField houseNrTf = new JTextField();
    private final JTextField cityTf = new JTextField();
    private final JPasswordField passwordPf = new JPasswordField();

    private final AppFrame owner;
    private final Dictionary dict;

    UpdateDataDialog(AppFrame mowner, Client mclient) {
        super(mowner,
                mowner.dict.getText("update_data_1") + " " + mowner.dict.getText("update_data_2"),
                200, 300
        );
        owner = mowner;
        client = mclient;
        dict = owner.dict;
        try {
            initialize();
            this.setVisible(true);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        } catch (WrongId ex) {
            System.err.format(ex.getMessage());
        }
    }

    private void initialize() throws SQLException, WrongId {
        ActionListener okListener = e -> updateClient();

        JPanel updatePanel = new JPanel();
        updatePanel.setBackground(owner.bgColor);
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));
        this.add(updatePanel);

        Box phoneBox = Box.createHorizontalBox();
        JLabel phoneLabel = new JLabel(dict.getText("phone_no"));
        phoneBox.add(Box.createRigidArea(new Dimension(10, 0)));
        phoneBox.add(phoneLabel);
        phoneBox.add(Box.createHorizontalGlue());
        updatePanel.add(phoneBox);

        phoneTf.setText(client.getPhone_no());
        phoneTf.setMaximumSize(new Dimension(150, 25));
        phoneTf.addActionListener(okListener);
        updatePanel.add(phoneTf);

        Box streetBox = Box.createHorizontalBox();
        JLabel streetLabel = new JLabel(dict.getText("street"));
        streetBox.add(Box.createRigidArea(new Dimension(10, 0)));
        streetBox.add(streetLabel);
        streetBox.add(Box.createHorizontalGlue());
        updatePanel.add(streetBox);

        streetTf.setText(client.getAddress().getStreet());
        streetTf.setMaximumSize(new Dimension(150, 25));
        streetTf.addActionListener(okListener);
        updatePanel.add(streetTf);

        Box houseNrBox = Box.createHorizontalBox();
        JLabel houseNrLabel = new JLabel(dict.getText("house_no"));
        houseNrBox.add(Box.createRigidArea(new Dimension(10, 0)));
        houseNrBox.add(houseNrLabel);
        houseNrBox.add(Box.createHorizontalGlue());
        updatePanel.add(houseNrBox);

        houseNrTf.setText(Integer.toString(client.getAddress().getApartment_no()));
        houseNrTf.setMaximumSize(new Dimension(150, 25));
        houseNrTf.addActionListener(okListener);
        updatePanel.add(houseNrTf);

        Box cityBox = Box.createHorizontalBox();
        JLabel cityLabel = new JLabel(dict.getText("city"));
        cityBox.add(Box.createRigidArea(new Dimension(10, 0)));
        cityBox.add(cityLabel);
        cityBox.add(Box.createHorizontalGlue());
        updatePanel.add(cityBox);

        cityTf.setText(client.getAddress().getCity_name());
        cityTf.setMaximumSize(new Dimension(150, 25));
        cityTf.addActionListener(okListener);
        updatePanel.add(cityTf);

        Box passwordBox = Box.createHorizontalBox();
        JLabel passwordLabel = new JLabel(dict.getText("pwd") + ":");
        passwordBox.add(Box.createRigidArea(new Dimension(10, 0)));
        passwordBox.add(passwordLabel);
        passwordBox.add(Box.createHorizontalGlue());
        updatePanel.add(passwordBox);

        passwordPf.setMaximumSize(new Dimension(150, 25));
        passwordPf.addActionListener(okListener);
        updatePanel.add(passwordPf);

        OkCancelButtonsPanel okCanButPan = new OkCancelButtonsPanel(owner, this, okListener);
        updatePanel.add(okCanButPan);

        this.setVisible(true);
    }

    private void updateClient() {
        try {
            Address address = new Address(
                    cityTf.getText(),
                    streetTf.getText(),
                    Integer.parseInt(houseNrTf.getText())
            );
            address.insert();

            client.updateAddress(address.getAddress_id());
            client.updatePhone(phoneTf.getText());
            if (!String.valueOf(passwordPf.getPassword()).isEmpty())
                client.updatePassword(String.valueOf(passwordPf.getPassword()));

            this.setVisible(false);
            this.dispatchEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
            );
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            fillInAllDialog();
        } catch (NumberFormatException ex) {
            System.err.format(ex.getMessage());
            houseNumberDialog();
        } catch (WrongId ex) {
            System.err.format(ex.getMessage());
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
}
