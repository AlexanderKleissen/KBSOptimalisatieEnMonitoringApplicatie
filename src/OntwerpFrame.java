import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OntwerpFrame extends JFrame implements ActionListener {
    private JComboBox<String> jcDropDownMenu;
    private String[] comboBoxContent;
    private JButton jbMonitoren, jbOntwerpen, jbOptimaliseren; //buttons
    private Ontwerpnetwerk ontwerpnetwerk;

    public OntwerpFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setTitle("Monitoringapplicatie (ontwerpen)");

        //Panel voor header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(header, BorderLayout.NORTH);

        //dropdownmenu
        comboBoxContent = new String[] {"Nieuw ontwerp", "Open ontwerp", "Optimaliseer ontwerp" , "Sluit ontwerp", "Programma sluiten"}; //Array voor de teksten binnen de JComboBox

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

        setVisible(true);
    }

    public OntwerpFrame(Ontwerpnetwerk netwerk){
        this();

        //netwerk aan frame koppelen
        this.ontwerpnetwerk = netwerk;

        //Panel voor midden van het scherm
        JPanel center = new JPanel(new BorderLayout());
        this.add(center);

        //Panel voor onderkant van midden van scherm
        JPanel onderkantCenter = new JPanel(new BorderLayout());
        center.add(onderkantCenter, BorderLayout.SOUTH);

        //Optimaliseer ontwerp-knop
        jbOptimaliseren = new JButton("Optimaliseer ontwerp");
        jbOptimaliseren.addActionListener(this);
        onderkantCenter.add(jbOptimaliseren, BorderLayout.WEST);

        //Summary veld rechtsonderin
        JPanel summary = new JPanel(new GridLayout(2,1));

        summary.setBorder(BorderFactory.createLineBorder(Color.black));

        summary.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,0,7,20), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(5,5,5,5))));
        onderkantCenter.add(summary, BorderLayout.EAST);
        JLabel totaleKosten = new JLabel("Totale kosten: €" + netwerk.getKosten());

        JLabel totaleBeschikbaarheid = new JLabel("Totale beschikbaarheid: " + netwerk.getBeschikbaarheidspercentage() + " %");
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
                JPanel jPanel = new JPanel(new GridLayout(4, 1));
                jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1, true), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
                if(component instanceof Ontwerpcomponent) {
                    JLabel naam = new JLabel("Naam: " + component.getNaam() + " " + "(" + component.getType() + ")");
                    jPanel.add(naam);
                    JLabel disk = new JLabel("Totale diskruimte: " + ((Ontwerpcomponent) component).getTotaleDiskruimte() + " GB");
                    jPanel.add(disk);
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: " + component.getBeschikbaarheidspercentage() + " %");
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €" + ((Ontwerpcomponent) component).getKosten());
                    jPanel.add(kosten);
                    componentenPanel.add(jPanel);
                }
            }
        }

        setVisible(true);
    }

    //Getters en setters
    public Ontwerpnetwerk getOntwerpnetwerk() {
        return ontwerpnetwerk;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jcDropDownMenu){
            System.out.println(jcDropDownMenu.getSelectedItem());
            if(jcDropDownMenu.getSelectedItem().equals("Programma sluiten")){
                dispose();
            }
            if (jcDropDownMenu.getSelectedItem().equals("Nieuw ontwerp")) {
                OptimalisatieDialog optimalisatieDialog = new OptimalisatieDialog(this);
                //Als er op nieuw ontwerp wordt geklikt in het drop down menu dan word een optimalisatiescherm geopent.
            }
            if(jcDropDownMenu.getSelectedItem().equals("Open ontwerp")){
                KiesNetwerkDialog kiesNetwerk = new KiesNetwerkDialog(this); //Een keuzelijst voor alle beschikbare netwerken wordt weergegeven
            }
            if(jcDropDownMenu.getSelectedItem().equals("Optimaliseer ontwerp")){
                //Zelfde functie als de optimalisatieknop.
                OptimalisatieDialog optimalisatieDialog = new OptimalisatieDialog(this);
            }
            if(jcDropDownMenu.getSelectedItem().equals("Sluit ontwerp")){
                OntwerpFrame ontwerpFrame = new OntwerpFrame();
                dispose();
            }
        } else if (e.getSource()==jbMonitoren){
            MonitoringFrame monitoringFrame = new MonitoringFrame();
            dispose();
        } else if(e.getSource()==jbOptimaliseren){
            OptimalisatieDialog optimalisatieDialog = new OptimalisatieDialog(this);
        }
    }
}