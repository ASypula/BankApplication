import javax.swing.*;

public class MainClientPanel extends JPanel {

    AppFrame parent;
    PersonalData client;

    public MainClientPanel(AppFrame mparent, PersonalData mclient) {
        super();
        parent = mparent;
        client = mclient;
        initialize();
    }

    private void initialize(){
        JLabel info = new JLabel(client.getName()+client.getSurname());
        this.add(info);
    }
}
