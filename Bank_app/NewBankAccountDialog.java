import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Vector;

public class NewBankAccountDialog extends AppDialog {

    private JComboBox<String> accountTypeCb;
    private JComboBox<String> currencyCb;

    private final AppFrame owner;
    private final Employee employee;
    private final Client client;
    private final Dictionary dict;

    NewBankAccountDialog(AppFrame mowner, Employee emp, Client cli) {
        super(mowner, mowner.dict.getText("add_bank_acc"), 200, 200);
        owner = mowner;
        employee = emp;
        client = cli;
        dict = owner.dict;

        try {
            accountTypeCb = new JComboBox<String>(new Vector<String>(BankAccount.getNotDepositAccountTypes()));
            accountTypeCb.insertItemAt("", 0);
            accountTypeCb.setSelectedIndex(0);

            currencyCb = new JComboBox<String>(new Vector<String>(Account.getCurrencyAbbreviations()));
            currencyCb.insertItemAt("", 0);
            currencyCb.setSelectedIndex(0);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }

        ActionListener okListener = e -> addAccount();

        JPanel addAccPan = new JPanel();
        addAccPan.setBackground(owner.bgColor);
        addAccPan.setLayout(new BoxLayout(addAccPan, BoxLayout.Y_AXIS));
        this.add(addAccPan);

        Box accountTypeBox = Box.createHorizontalBox();
        JLabel accountTypeLabel = new JLabel(dict.getText("account_type"));
        accountTypeBox.add(Box.createRigidArea(new Dimension(10, 0)));
        accountTypeBox.add(accountTypeLabel);
        accountTypeBox.add(Box.createHorizontalGlue());
        addAccPan.add(accountTypeBox);

        accountTypeCb.setMaximumSize(new Dimension(150, 25));
        addAccPan.add(accountTypeCb);

        Box currencyBox = Box.createHorizontalBox();
        JLabel currencyLabel = new JLabel(dict.getText("currency"));
        currencyBox.add(Box.createRigidArea(new Dimension(10, 0)));
        currencyBox.add(currencyLabel);
        currencyBox.add(Box.createHorizontalGlue());
        addAccPan.add(currencyBox);

        currencyCb.setMaximumSize(new Dimension(150, 25));
        addAccPan.add(currencyCb);

        OkCancelButtonsPanel okCanButPan = new OkCancelButtonsPanel(owner, this, okListener);
        addAccPan.add(okCanButPan);

        this.setVisible(true);
    }

    private void addAccount() {
        try {
            BankAccount account = new BankAccount(
                    (String) currencyCb.getSelectedItem(),
                    null,
                    (String) accountTypeCb.getSelectedItem(),
                    client.getClient_id(),
                    0, 0, 0
            );
            account.insert();

            owner.changeToEmployee(employee);
            this.setVisible(false);
            this.dispatchEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
            );
        } catch (SQLException ex) {
            fillInAllDialog();
        } catch (WrongId ex) {
            System.err.format(ex.getMessage());
        }
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
