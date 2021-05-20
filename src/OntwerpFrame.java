import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class OntwerpFrame extends JFrame implements ActionListener {
    DragAndDrop dragAndDrop = new DragAndDrop();
    private JComboBox<String> jcDropDownMenu;
    private String[] comboBoxContent;
    private JButton jbMonitoren, jbOntwerpen, jbOptimaliseren; //buttons
    private Ontwerpnetwerk ontwerpnetwerk;
    private boolean gedruktOpOptimalisatieknop;
    private Color backClr1 = new Color(60, 63, 65); //de kleur van de rest
    private Color backClr2 = new Color(43, 43, 43); //de kleur van het midden



    public OntwerpFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setTitle("Monitoringapplicatie (ontwerpen)");

        //Panel voor header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(backClr1);
        this.add(header, BorderLayout.NORTH);

        //dropdownmenu
        comboBoxContent = new String[] {"Nieuw ontwerp", "Open ontwerp", "Optimaliseer ontwerp" , "Sluit ontwerp", "Programma sluiten"}; //Array voor de teksten binnen de JComboBox

        jcDropDownMenu = new JComboBox<>(comboBoxContent);
        jcDropDownMenu.addActionListener(this);
        jcDropDownMenu.setEditable(true); //Om de teksts aan te passen moet het eerst enabled worden
        jcDropDownMenu.setSelectedItem("Bestand");
        jcDropDownMenu.setEditable(false); //Als dit enabled blijft dan kan de gebruiker ook de tekst aanpassen
        jcDropDownMenu.setForeground(Color.white);
        jcDropDownMenu.setBackground(backClr1);
        jcDropDownMenu.setUI(new ColorArrowUI());
        header.add(jcDropDownMenu);

        //Panel voor footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBorder(BorderFactory.createLineBorder(Color.white));
        footer.setBackground(backClr1);
        this.add(footer, BorderLayout.SOUTH);

        //Ontwerpen-Monitoren keuzebox linksonderin
        jbOntwerpen = new JButton("Ontwerpen");
        jbOntwerpen.setBackground(backClr1);
        jbOntwerpen.setForeground(Color.white);
        jbOntwerpen.setEnabled(false);
        footer.add(jbOntwerpen);
        jbMonitoren = new JButton("Monitoren");
        jbMonitoren.setBackground(backClr1);
        jbMonitoren.setForeground(Color.white);
        jbMonitoren.addActionListener(this);
        footer.add(jbMonitoren);

        getContentPane().setBackground(backClr2);
        this.add(dragAndDrop);
        setVisible(true);
    }

    public OntwerpFrame(Ontwerpnetwerk netwerk){
        this();

        //netwerk aan frame koppelen
        this.ontwerpnetwerk = netwerk;

        //Panel voor midden van het scherm
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(backClr2);
        this.add(center);

        //Panel voor onderkant van midden van scherm
        JPanel onderkantCenter = new JPanel(new BorderLayout());
        onderkantCenter.setBackground(backClr2);
        center.add(onderkantCenter, BorderLayout.SOUTH);

        //Optimaliseer ontwerp-knop
        jbOptimaliseren = new JButton("Optimaliseer ontwerp");
        jbOptimaliseren.addActionListener(this);
        jbOptimaliseren.setForeground(Color.white);
        jbOptimaliseren.setBackground(backClr2);
        onderkantCenter.add(jbOptimaliseren, BorderLayout.WEST);

        //Summary veld rechtsonderin
        JPanel summary = new JPanel(new GridLayout(2,1));

        summary.setBorder(BorderFactory.createLineBorder(Color.white));

        summary.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,0,7,20), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white), BorderFactory.createEmptyBorder(5,5,5,5))));
        summary.setBackground(backClr2);
        onderkantCenter.add(summary, BorderLayout.EAST);
        JLabel totaleKosten = new JLabel("Totale kosten: €" + netwerk.getKosten());
        totaleKosten.setForeground(Color.white);

        JLabel totaleBeschikbaarheid = new JLabel("Totale beschikbaarheid: " + netwerk.getBeschikbaarheidspercentage() + "%");
        totaleBeschikbaarheid.setForeground(Color.white);
        summary.add(totaleKosten);
        summary.add(totaleBeschikbaarheid);

        //Panel voor componenten
        JPanel componentenPanel = new JPanel();
        componentenPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10,10));
        componentenPanel.setBackground(backClr2);
        center.add(componentenPanel);

        //Componenten info op het scherm
        for(Groep groep: netwerk.groepen) {
            for (Component component : groep.componenten) {
                JPanel jPanel = new JPanel(new GridLayout(4, 1));
                jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1, true), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
                jPanel.setBackground(backClr2);
                if(component instanceof Ontwerpcomponent) {
                    JLabel naam = new JLabel("Naam: " + component.getNaam());
                    naam.setForeground(Color.white);
                    jPanel.add(naam);
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: " + component.getBeschikbaarheidspercentage() + "%");
                    beschikbaarheid.setForeground(Color.white);
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €" + ((Ontwerpcomponent) component).getKosten());
                    kosten.setForeground(Color.white);
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

    public boolean isGedruktOpOptimalisatieknop() {
        return gedruktOpOptimalisatieknop;
    }

    public void setGedruktOpOptimalisatieknop(boolean gedruktOpOptimalisatieknop) {
        this.gedruktOpOptimalisatieknop = gedruktOpOptimalisatieknop;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jcDropDownMenu){
//            System.out.println(jcDropDownMenu.getSelectedItem()); Kan gebruikt worden als testfunctie op welk dropdownmenu item geklikt is.
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
                gedruktOpOptimalisatieknop = true;
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
            gedruktOpOptimalisatieknop = true;
            OptimalisatieDialog optimalisatieDialog = new OptimalisatieDialog(this);
        }
    }
}