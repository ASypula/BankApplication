import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class EmployeePanel extends JPanel {

    AppFrame parent;
    Employee employee;
    private Dictionary dict;

    public EmployeePanel(AppFrame mparent, Employee memployee) {
        super();
        parent = mparent;
        employee = memployee;
        dict = parent.dict;
        try {
            initialize();
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        } catch (WrongId e) {
            System.err.format(e.getMessage());
        }
    }

    private void initialize() throws SQLException, WrongId {
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

        JLabel professionLabel = new JLabel(dict.getText("profession") + employee.getProfessionName(), SwingConstants.CENTER);
        professionLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weighty = 0.2;
        this.add(professionLabel, c);

        java.util.List<Client> clients = employee.getClients();

        if(!clients.isEmpty()) {
            JLabel clientsLabel = new JLabel(dict.getText("my_clients"));
            clientsLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridx = 0;
            c.gridy = 2;
            c.weighty = 0.1;
            this.add(clientsLabel, c);

            JPanel clientsPanel = new JPanel();
            clientsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            clientsPanel.setBackground(parent.bgColor);
            clientsPanel.setLayout(new BoxLayout(clientsPanel, BoxLayout.Y_AXIS));

            for (Client client : clients) {

                JPanel clientPanel = new JPanel();
                clientPanel.setBackground(parent.bgColor);
                clientPanel.setLayout(new GridBagLayout());
                c.fill = GridBagConstraints.NONE;
                c.anchor = GridBagConstraints.FIRST_LINE_START;
                c.weightx = 0.5;
                c.weighty = 0.5;

                JLabel clientIdLabel = new JLabel(
                        "<html>"+dict.getText("client_id")+"<b>" +
                                client.getClient_id() + "</b></html>"
                );
                clientIdLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 0;
                clientPanel.add(clientIdLabel, c);

                JLabel clientNameLabel = new JLabel(client.getName() + " " + client.getSurname());
                clientNameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
                c.gridx = 0;
                c.gridy = 1;
                clientPanel.add(clientNameLabel, c);

                String accStr = dict.getText("client_account");
                java.util.List<BankAccount> accounts = client.getBankAccounts();

                if (accounts.isEmpty()) {
                    accStr += dict.getText("lack");
                } else {
                    for (BankAccount account : accounts)
                        accStr += account.getAccount_no() + ", ";
                    accStr = accStr.substring(0, accStr.length() - 2);
                }

                JLabel clientAccountsLabel = new JLabel(accStr);
                clientAccountsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                c.gridx = 0;
                c.gridy = 2;
                clientPanel.add(clientAccountsLabel, c);

                WhiteButton addAccountButton = new WhiteButton(dict.getText("add_bank_acc"));
                addAccountButton.addActionListener(e -> new NewBankAccountDialog(parent, employee, client));
                c.anchor = GridBagConstraints.FIRST_LINE_END;
                c.gridx = 1;
                c.gridy = 1;
                clientPanel.add(addAccountButton, c);

                WhiteButton removeClientButton = new WhiteButton(dict.getText("remove_client"));
                removeClientButton.addActionListener(e -> {
                    try {
                        employee.removeClient(client.getClient_id());
                        parent.changeToEmployee(employee);
                    } catch (SQLException ex) {
                        System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                        AppDialog.cannotRemoveDialog(parent, dict);
                    }
                });
                c.gridx = 2;
                c.gridy = 2;
                clientPanel.add(removeClientButton, c);

                clientsPanel.add(clientPanel);
                clientsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }


            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weighty = 0.8;

            if (clients.size() > 5) {
                JScrollPane clientsScroll = new JScrollPane(clientsPanel);
                c.fill = GridBagConstraints.BOTH;
                this.add(clientsScroll, c);
            } else {
                c.fill = GridBagConstraints.HORIZONTAL;
                this.add(clientsPanel, c);
            }
        }

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        if(!clients.isEmpty())
            c.gridy = 4;
        else
            c.gridy = 2;
        c.weighty = 0.02;
        this.add(new LanguageButtonsPanel(parent, "employee", employee), c);
    }
}
