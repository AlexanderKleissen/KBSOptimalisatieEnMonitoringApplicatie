import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonitoringFrame extends JFrame implements ActionListener {
    private JComboBox<String> jcDropDownMenu;
    private String[] comboBoxContent;
    private JButton jbMonitoren, jbOntwerpen; //buttons voor footer

    public MonitoringFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setTitle("Monitoringapplicatie (monitoren)");

        //Panel voor header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(header, BorderLayout.NORTH);

        //dropdownmenu
        comboBoxContent = new String[] {"Nieuw netwerk", "Monitor netwerk", "Sluit Netwerk", "Programma sluiten"}; //Array voor de teksten binnen de JComboBox

        jcDropDownMenu = new JComboBox<>(comboBoxContent);
        jcDropDownMenu.addActionListener(this);
        jcDropDownMenu.setEditable(true); //Om de teksten aan te passen moet het eerst enabled worden
        jcDropDownMenu.setSelectedItem("Bestand");
        jcDropDownMenu.setEditable(false); //Als dit enabled blijft dan kan de gebruiker ook de tekst aanpassen
        header.add(jcDropDownMenu);

        //Panel voor footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(footer, BorderLayout.SOUTH);

        //Ontwerpen-Monitoren keuzebox linksonderin
        jbOntwerpen = new JButton("Ontwerpen");
        jbOntwerpen.addActionListener(this);
        footer.add(jbOntwerpen);
        jbMonitoren = new JButton("Monitoren");
        jbMonitoren.setEnabled(false);
        footer.add(jbMonitoren);

        setVisible(true);
    }

    public MonitoringFrame(Monitoringnetwerk netwerk){
        this();

        //Panel voor midden van het scherm
        JPanel center = new JPanel(new BorderLayout());
        this.add(center);

        //Panel voor onderkant van midden van scherm
        JPanel onderkantCenter = new JPanel(new BorderLayout());
        center.add(onderkantCenter, BorderLayout.SOUTH);

        //Summary veld rechtsonderin
        JPanel summary = new JPanel(new GridLayout(2,1));

        summary.setBorder(BorderFactory.createLineBorder(Color.black));

        summary.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,0,7,20), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(5,5,5,5))));
        onderkantCenter.add(summary, BorderLayout.EAST);
        JLabel totaleKosten = new JLabel("Totale periodieke kosten: ");

        JLabel totaleBeschikbaarheid = new JLabel("Totale beschikbaarheid: ");
        summary.add(totaleKosten);
        summary.add(totaleBeschikbaarheid);

        //Scrollbar
        JScrollBar scrollBar = new JScrollBar();
        center.add(scrollBar, BorderLayout.EAST);

        //Panel voor componenten
        JPanel componentenPanel = new JPanel();
        componentenPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10,10));
        center.add(componentenPanel);

        //Componenten info op het scherm
        for(Groep groep: netwerk.groepen) {
            for (Component component : groep.componenten) {
                JPanel jPanel = new JPanel(new GridLayout(6, 1));
                jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1, true), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
                JLabel naam = new JLabel("Naam: ");
                jPanel.add(naam);
                JLabel disk = new JLabel("Overige diskruimte van (): ");
                jPanel.add(disk);
                JLabel processor = new JLabel("Huidige processorbelasting: ");
                jPanel.add(processor);
                JLabel status = new JLabel("Status: ");
                jPanel.add(status);
                JLabel beschikbaarheid = new JLabel("Beschikbaarheidspercentage: ");
                jPanel.add(beschikbaarheid);
                JLabel uptime = new JLabel("Uptime sinds laatste reboot: ");
                jPanel.add(uptime);
                componentenPanel.add(jPanel);
            }
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jcDropDownMenu) {
//            System.out.println(jcDropDownMenu.getSelectedItem()); Kan gebruikt worden als testfunctie op welk dropdownmenu item geklikt is.
            if (jcDropDownMenu.getSelectedItem().equals("Sluit netwerk")) {
                dispose();
                MonitoringFrame monitoringFrame = new MonitoringFrame();
                //Het monitoringframe blijft, maar zonder inhoud.
            }
            if  (jcDropDownMenu.getSelectedItem().equals("Monitor netwerk")){
                KiesNetwerkDialog kiesNetwerk = new KiesNetwerkDialog(this); //Een keuzelijst voor alle beschikbare netwerken wordt weergegeven
            }
            if  (jcDropDownMenu.getSelectedItem().equals("Nieuw netwerk")){
                dispose();
                MonitoringFrame monitoringFrame = new MonitoringFrame();
            }
        }
        if (e.getSource()==jbOntwerpen){
            dispose();
            OntwerpFrame ontwerpFrame = new OntwerpFrame();
        }
    }
}