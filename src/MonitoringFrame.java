import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonitoringFrame extends JFrame implements ActionListener {
    private JComboBox jcDropDownMenu; //
    private String[] comboBoxContent;

    public MonitoringFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLayout(new FlowLayout());
        setVisible(true);
        setTitle("Monitoringapplicatie");

        comboBoxContent = new String[] {"Nieuw ontwerp", "Open ontwerp", "Sluit ontwerp", "Opslaan", "Opslaan als", "Programma sluiten"}; //Array voor de teksts binnen de JComboBox
        jcDropDownMenu = new JComboBox<>(comboBoxContent);
        jcDropDownMenu.addActionListener(this);
        jcDropDownMenu.setEditable(true); //Om de teksts aan te passen moet het eerst enabled worden
        jcDropDownMenu.setSelectedItem("Bestand");
        jcDropDownMenu.setEditable(false); //Als dit enabled blijft dan kan de gebruiker ook de tekst aanpassen
//        jcDropDownMenu.setLocation(0,0);
        add(jcDropDownMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(jcDropDownMenu.getSelectedItem());
        if (jcDropDownMenu.getSelectedItem().equals("Programma sluiten")) {
            dispose();
        }
    }
}
