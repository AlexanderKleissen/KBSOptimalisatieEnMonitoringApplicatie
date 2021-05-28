import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KiesNetwerkDialog extends JDialog implements ActionListener {
    private int aantalO = Ontwerpnetwerk.getOntwerpNetwerken().size();
    private JButton netwerkKeuzeO;
    private Frame frame;
    private Color backClr1 = new Color(60, 63, 65); //achtergrondkleur

    //Constructor

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

            OntwerpFrame ontwerpFrame = new OntwerpFrame((Ontwerpnetwerk) gekozenNetwerk);

        frame.dispose();
        dispose();
    }
}