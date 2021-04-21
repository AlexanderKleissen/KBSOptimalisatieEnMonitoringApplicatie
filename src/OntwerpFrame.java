import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OntwerpFrame extends JFrame implements ActionListener {
    private JComboBox jcDropDownMenu;
    private String[] comboBoxContent;
    private JButton jbMonitoren, jbOntwerpen, jbOptimaliseren; //buttons

    public OntwerpFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setTitle("Monitoringapplicatie (ontwerpen)");

        //Panel voor header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(header, BorderLayout.NORTH);

        //dropdownmenu
        comboBoxContent = new String[] {"Nieuw ontwerp", "Open ontwerp", "Sluit ontwerp", "Opslaan", "Opslaan als", "Programma sluiten"}; //Array voor de teksts binnen de JComboBox
        jcDropDownMenu = new JComboBox<>(comboBoxContent);
        jcDropDownMenu.addActionListener(this);
        jcDropDownMenu.setEditable(true); //Om de teksts aan te passen moet het eerst enabled worden
        jcDropDownMenu.setSelectedItem("Bestand");
        jcDropDownMenu.setEditable(false); //Als dit enabled blijft dan kan de gebruiker ook de tekst aanpassen
        header.add(jcDropDownMenu);

        //Panel voor footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(footer, BorderLayout.SOUTH);

        //Ontwerpen-Monitoren keuzebox linksonderin
        jbOntwerpen = new JButton("Ontwerpen");
        jbOntwerpen.setEnabled(false);
        footer.add(jbOntwerpen);
        jbMonitoren = new JButton("Monitoren");
        jbMonitoren.addActionListener(this);
        footer.add(jbMonitoren);

        //Panel voor midden van het scherm
        JPanel center = new JPanel(new BorderLayout());
        this.add(center);

        //Panel voor onderkant van midden van scherm
        JPanel onderkantCenter = new JPanel(new BorderLayout());
        center.add(onderkantCenter, BorderLayout.SOUTH);

        //Optimaliseer ontwerp-knop
        jbOptimaliseren = new JButton("Optimaliseer ontwerp");
        onderkantCenter.add(jbOptimaliseren, BorderLayout.WEST);

        //Summary veld rechtsonderin
        JPanel summary = new JPanel(new GridLayout(2,1));
        summary.setBorder(BorderFactory.createLineBorder(Color.black));
        onderkantCenter.add(summary, BorderLayout.EAST);
        JLabel totaleKosten = new JLabel("Totale kosten: ");
        JLabel totaleBeschikbaarheid = new JLabel("Totale beschikbaarheid: ");
        summary.add(totaleKosten);
        summary.add(totaleBeschikbaarheid);

        //Scrollbar
        JScrollBar scrollBar = new JScrollBar();
        center.add(scrollBar, BorderLayout.EAST);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jcDropDownMenu){
            System.out.println(jcDropDownMenu.getSelectedItem());
            if (jcDropDownMenu.getSelectedItem().equals("Programma sluiten")) {
                dispose();
            }else if (jcDropDownMenu.getSelectedItem().equals("Nieuw ontwerp")) {
                dispose();
                OptimalisatieFrame optimalisatieFrame = new OptimalisatieFrame();
                //Als er op nieuw ontwerp wordt geklikt in het drop down menu dan word een optimalisatiescherm geopent.
            }
        } else if (e.getSource()==jbMonitoren){
            dispose();
            MonitoringFrame monitoringFrame = new MonitoringFrame();
        }

    }
}
