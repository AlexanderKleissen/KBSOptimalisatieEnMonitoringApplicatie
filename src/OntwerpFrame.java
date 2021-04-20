import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OntwerpFrame extends JFrame implements ActionListener {
    private JComboBox jcDropDownMenu;
    private String[] comboBoxContent;

    public OntwerpFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLayout(new FlowLayout());
        setVisible(true);
        setTitle("Monitoringapplicatie (ontwerpen)");

        comboBoxContent = new String[] {"Nieuw ontwerp", "Open ontwerp", "Optimaliseer ontwerp" ,"Sluit ontwerp", "Programma sluiten"}; //Array voor de teksts binnen de JComboBox
        jcDropDownMenu = new JComboBox<>(comboBoxContent);
        jcDropDownMenu.addActionListener(this);
        add(jcDropDownMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(jcDropDownMenu.getSelectedItem());
        if (jcDropDownMenu.getSelectedItem().equals("Programma sluiten")) {
            dispose(); //Programma sluit als er op programma sluiten wordt gedrukt in het drop down menu.
        }
        if (jcDropDownMenu.getSelectedItem().equals("Nieuw ontwerp")) {
            dispose();
            OptimalisatieFrame optimalisatieFrame = new OptimalisatieFrame();
            //Als er op nieuw ontwerp wordt geklikt in het drop down menu dan word een optimalisatiescherm geopent.

        }
    }
}
