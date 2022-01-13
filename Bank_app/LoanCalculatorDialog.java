import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class LoanCalculatorDialog extends AppDialog {

    private JSlider amountSlider;
    private JSlider periodSlider;
    private JLabel amountLabel;
    private JLabel periodLabel;
    private JLabel installmentLabel;
    private JLabel totalLabel;

    private final AppFrame owner;
    private final Dictionary dict;

    LoanCalculatorDialog(AppFrame mowner) {
        super(mowner, mowner.dict.getText("credit_calculator"), 500, 300);
        owner = mowner;
        dict = owner.dict;

        JPanel loanCalcPanel = new JPanel();
        loanCalcPanel.setBackground(owner.bgColor);
        loanCalcPanel.setLayout(new BoxLayout(loanCalcPanel, BoxLayout.Y_AXIS));
        this.add(loanCalcPanel);

        Box amountBox = Box.createHorizontalBox();
        amountLabel = new JLabel(dict.getText("amount") + "0");
        amountBox.add(Box.createRigidArea(new Dimension(10, 0)));
        amountBox.add(amountLabel);
        amountBox.add(Box.createHorizontalGlue());
        loanCalcPanel.add(amountBox);

        amountSlider = new JSlider(0, 1000000, 0);
        amountSlider.setBackground(owner.bgColor);
        amountSlider.addChangeListener(this::stateChanged);
        amountSlider.setMajorTickSpacing(100000);
        amountSlider.setMinorTickSpacing(10000);
        amountSlider.setPaintTicks(true);
        amountSlider.setPaintLabels(true);
        loanCalcPanel.add(amountSlider);

        Box periodBox = Box.createHorizontalBox();
        periodLabel = new JLabel(dict.getText("period") + " 0 yrs");
        periodBox.add(Box.createRigidArea(new Dimension(10, 0)));
        periodBox.add(periodLabel);
        periodBox.add(Box.createHorizontalGlue());
        loanCalcPanel.add(periodBox);

        periodSlider = new JSlider(0, 30, 0);
        periodSlider.setBackground(owner.bgColor);
        periodSlider.addChangeListener(this::stateChanged);
        periodSlider.setMajorTickSpacing(3);
        periodSlider.setMinorTickSpacing(1);
        periodSlider.setPaintTicks(true);
        periodSlider.setPaintLabels(true);
        loanCalcPanel.add(periodSlider);

        loanCalcPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        Box installmentBox = Box.createHorizontalBox();
        installmentLabel = new JLabel(dict.getText("installment_amount") + "-");
        installmentBox.add(Box.createRigidArea(new Dimension(150, 0)));
        installmentBox.add(installmentLabel);
        installmentBox.add(Box.createHorizontalGlue());
        loanCalcPanel.add(installmentBox);

        loanCalcPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        Box totalBox = Box.createHorizontalBox();
        totalLabel = new JLabel(dict.getText("total_cost") + "-");
        totalBox.add(Box.createRigidArea(new Dimension(150, 0)));
        totalBox.add(totalLabel);
        totalBox.add(Box.createHorizontalGlue());
        loanCalcPanel.add(totalBox);

        this.setVisible(true);
    }

    public void stateChanged(ChangeEvent e) {
        int amount = (((int) amountSlider.getValue()) / 100) * 100;
        int period = (int) periodSlider.getValue();

        amountLabel.setText(dict.getText("amount") + amount);
        periodLabel.setText(dict.getText("period") + " " + period + " yrs");

        if (period == 0) {
            installmentLabel.setText(dict.getText("installment_amount") + "-");
            totalLabel.setText(dict.getText("total_cost") + "-");
        } else {
            double total = (double) Math.round(Loan.newLoanTotal(amount) * 100) / 100;
            double installment = (double) Math.round(Loan.newInstallment(period * 12, total) * 100) / 100;

            installmentLabel.setText(dict.getText("installment_amount") + installment);
            totalLabel.setText(dict.getText("total_cost") + total);
        }
    }
}
