import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptimalisatieFrame extends JFrame implements ActionListener {

    public OptimalisatieFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLayout(new FlowLayout());
        setVisible(true);
        setTitle("Monitoringapplicatie (optimaliseren");

       JButton bereken = new JButton("Bereken optimale waarden");
       add(bereken);
       JButton voegToe = new JButton("Voeg componenten toe aan ontwerp");
       add(voegToe);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
