import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KiesNetwerkDialog extends JDialog implements ActionListener {
    private int aantalM = Monitoringnetwerk.getMonitoringNetwerken().size();
    private int aantalO = Ontwerpnetwerk.getOntwerpNetwerken().size();
    private JButton netwerkKeuzeM, netwerkKeuzeO;
    private Frame frame;
    private Color backClr1 = new Color(60, 63, 65); //achtergrondkleur

    //Constructor
    KiesNetwerkDialog(MonitoringFrame frame){ //constructor vanuit het MonitoringFrame
        super(frame, true);
        setTitle("Kies Netwerk");
        setSize(300,aantalM*26+40);
        this.frame = frame;

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
        jPanel.setBackground(backClr1);
        add(jPanel);

        for(Monitoringnetwerk netwerk : Monitoringnetwerk.getMonitoringNetwerken()){
            netwerkKeuzeM = new JButton(netwerk.getNaam());
            netwerkKeuzeM.setBackground(backClr1);
            netwerkKeuzeM.setForeground(Color.white);
            netwerkKeuzeM.addActionListener(this);
            jPanel.add(netwerkKeuzeM);
        }

        setVisible(true);
    }

    KiesNetwerkDialog(OntwerpFrame frame){ //constructor vanuit het OntwerpFrame
        super(frame, true);
        setTitle("Kies Netwerk");
        setSize(300,aantalO*26+40);
        this.frame = frame;

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
        jPanel.setBackground(backClr1);
        add(jPanel);

        for(Ontwerpnetwerk netwerk : Ontwerpnetwerk.getOntwerpNetwerken()){
            netwerkKeuzeO = new JButton(netwerk.getNaam());
            netwerkKeuzeO.setBackground(backClr1);
            netwerkKeuzeO.setForeground(Color.white);
            jPanel.add(netwerkKeuzeO);
            netwerkKeuzeO.addActionListener(this);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Het netwerk waar op geklikt is wordt geopend
        Netwerk gekozenNetwerk = null;
        for(Ontwerpnetwerk netwerk : Ontwerpnetwerk.getOntwerpNetwerken()){
            if(netwerk.getNaam().equals(e.getActionCommand())){
                gekozenNetwerk = netwerk;
            }
        }
        for(Monitoringnetwerk netwerk : Monitoringnetwerk.getMonitoringNetwerken()){
            if(netwerk.getNaam().equals(e.getActionCommand())){
                gekozenNetwerk = netwerk;
            }
        }
        if(gekozenNetwerk instanceof Monitoringnetwerk){
            MonitoringFrame monitoringFrame = new MonitoringFrame((Monitoringnetwerk) gekozenNetwerk);
        }else{
            OntwerpFrame ontwerpFrame = new OntwerpFrame((Ontwerpnetwerk) gekozenNetwerk);
        }
        frame.dispose();
        dispose();
    }
}