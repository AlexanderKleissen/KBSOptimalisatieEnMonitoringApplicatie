import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KiesNetwerkDialog extends JDialog implements ActionListener {
    private int aantal = Monitoringnetwerk.getMonitoringNetwerken().size();
    private JButton netwerkKeuze;

    KiesNetwerkDialog(Frame frame){
        super(frame, true);
        setTitle("Kies Netwerk");
        setSize(300,aantal*45);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
        add(jPanel);

        for(Monitoringnetwerk netwerk : Monitoringnetwerk.getMonitoringNetwerken()){
            netwerkKeuze = new JButton(netwerk.getNaam());
            jPanel.add(netwerkKeuze);
            netwerkKeuze.addActionListener(this);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Het netwerk waar op geklikt is wordt geopend
        Monitoringnetwerk gekozenNetwerk = null;
        for(Monitoringnetwerk netwerk : Monitoringnetwerk.getMonitoringNetwerken()){
            if(netwerk.getNaam().equals(e.getActionCommand())){
                gekozenNetwerk = netwerk;
            }
        }
        MonitoringFrame monitoringFrame = new MonitoringFrame(gekozenNetwerk);
        dispose();
    }
}
