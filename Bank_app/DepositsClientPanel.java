import javax.swing.*;
import java.awt.*;

public class DepositsClientPanel extends JPanel {

    AppFrame parent;
    Client client;
    private Dictionary dict;

    public DepositsClientPanel(AppFrame mparent, Client mclient) {
        super();
        parent = mparent;
        client = mclient;
        dict = parent.dict;
        initialize();
    }

    private void initialize() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 0.5;

        PageButtonsPanel pageButPan = new PageButtonsPanel(parent, client);
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(pageButPan, c);

        JLabel depositsLabel = new JLabel("Moje lokaty");
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        this.add(depositsLabel, c);

        WhiteButton newDepositButton = new WhiteButton("Złóż wniosek o nową lokatę");
        newDepositButton.addActionListener(e -> AppDialog.contactEmployeeDialog(parent));
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.gridx = 1;
        c.gridy = 1;
        this.add(newDepositButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weighty = 0.8;

        JPanel depositsPanel = new JPanel();
        depositsPanel.setBackground(parent.bgColor);
        depositsPanel.setLayout(new BoxLayout(depositsPanel, BoxLayout.Y_AXIS));

//        TODO: Uncomment everything and check after necessary functions implemented; Remove dummy values
//        java.util.List<Deposit> deposits = client.getDeposits();
//        for (int i = 0; i < deposits.size(); i++) {
        for (int i = 0; i < 3; i++) {
            JPanel depositPanel = new JPanel();
            depositPanel.setBackground(parent.bgColor);
            depositPanel.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 0.5;
            c.weighty = 0.5;

//            JLabel nextInstallmentAmountLabel = new JLabel(
//                    "<html>Następna wpłata: <b>" +
//                            deposits.get(i).getNextInstallmentAmount() +
//                            "</b></html>"
//            );
            JLabel nextInstallmentAmountLabel = new JLabel(
                    "<html>Następna wpłata: <b>" +
                            "[NextInstallmentAmount]" +
                            "</b></html>"
            );
            nextInstallmentAmountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 0;
            depositPanel.add(nextInstallmentAmountLabel, c);

//            JLabel nextInstallmentDateLabel = new JLabel(
//                    "<html>Płatna dnia: <b>" +
//                            deposits.get(i).getNextInstallmentDateString() +
//                            "</b></html>"
//            );
            JLabel nextInstallmentDateLabel = new JLabel(
                    "<html>Płatna dnia: <b>" +
                            "[NextInstallmentDate]" +
                            "</b></html>"
            );
            nextInstallmentDateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 1;
            depositPanel.add(nextInstallmentDateLabel, c);

//            JLabel interestLabel = new JLabel(
//                    "<html>Oprocentowanie: <b>" +
//                            deposits.get(i).getInterest() +
//                            "</b></html>"
//            );
            JLabel interestLabel = new JLabel(
                    "<html>Oprocentowanie: <b>" +
                            "[Interest]" +
                            "%</b></html>"
            );
            interestLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 0;
            c.gridy = 2;
            depositPanel.add(interestLabel, c);

            c.anchor = GridBagConstraints.EAST;

            JLabel currentLabel = new JLabel("Obecna kwota:");
            currentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 1;
            c.gridy = 0;
            depositPanel.add(currentLabel, c);

//            JLabel currentAmountLabel = new JLabel(deposits.get(i).getCurrentAmount());
            JLabel currentAmountLabel = new JLabel("[Amount]");
            currentAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
            c.gridx = 1;
            c.gridy = 1;
            depositPanel.add(currentAmountLabel, c);

//            JLabel periodLabel = new JLabel(
//                    "<html>Okres: <b>" +
//                            deposits.get(i).getPeriodMonths() +
//                            " msc</b> (od <b>" +
//                            deposits.get(i).getStartDateString() +
//                            "</b> do <b>" +
//                            deposits.get(i).getEndDateString() +
//                            "</b>)</html>"
//            );
            JLabel periodLabel = new JLabel(
                    "<html>Okres: <b>" +
                            "[Months]" +
                            " msc</b> (od <b>" +
                            "[StartDate]" +
                            "</b> do <b>" +
                            "[EndDate]" +
                            "</b>)</html>"
            );
            periodLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            c.gridx = 1;
            c.gridy = 2;
            depositPanel.add(periodLabel, c);

            depositsPanel.add(depositPanel);
            depositsPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        c.weighty = 0.8;
        this.add(depositsPanel, c);
    }
}
