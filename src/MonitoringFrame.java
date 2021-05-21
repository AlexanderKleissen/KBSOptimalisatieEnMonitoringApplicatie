import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;


public class MonitoringFrame extends JFrame implements ActionListener {
    private JComboBox<String> jcDropDownMenu;
    private String[] comboBoxContent;
    private JButton jbMonitoren, jbOntwerpen; //buttons voor footer
    private Color backClr1 = new Color(60, 63, 65); //de kleur van de rest
    private Color backClr2 = new Color(43, 43, 43); //de kleur van het midden
    ArrayList<Monitoringcomponent> monitoringcomponents;

    private JLabel status;
    private JLabel processor;
    private JLabel werkgeheugen;
    private JLabel totaalgeheugen;
    private JLabel gebruiktegeheugen;
    private JLabel beschikbaregeheugen;
    private JLabel uptime;
    private JLabel beschikbaarheid;
    private JLabel kosten;


    public MonitoringFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setTitle("Monitoringapplicatie (monitoren)");

        //Panel voor header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(backClr1);
        this.add(header, BorderLayout.NORTH);

        //dropdownmenu
        comboBoxContent = new String[] {"Programma sluiten"}; //Array voor de teksten binnen de JComboBox

        jcDropDownMenu = new JComboBox<>(comboBoxContent);
        jcDropDownMenu.addActionListener(this);
        jcDropDownMenu.setEditable(true); //Om de teksten aan te passen moet het eerst enabled worden
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
        jbOntwerpen.addActionListener(this);
        footer.add(jbOntwerpen);
        jbMonitoren = new JButton("Monitoren");
        jbMonitoren.setBackground(backClr1);
        jbMonitoren.setForeground(Color.white);
        jbMonitoren.setEnabled(false);
        footer.add(jbMonitoren);

        getContentPane().setBackground(backClr2);
        setVisible(true);
    }

    public MonitoringFrame(Monitoringnetwerk netwerk) {
        this();
        //Panel voor midden van het scherm
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(backClr2);
        this.add(center);

        //Panel voor onderkant van midden van scherm
        JPanel onderkantCenter = new JPanel(new BorderLayout());
        onderkantCenter.setBackground(backClr2);
        center.add(onderkantCenter, BorderLayout.SOUTH);

        //Summary veld rechtsonderin
        JPanel summary = new JPanel(new GridLayout(2, 1));

        summary.setBorder(BorderFactory.createLineBorder(Color.white));

        summary.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 7, 20), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white), BorderFactory.createEmptyBorder(5, 5, 5, 5))));
        summary.setBackground(backClr2);
        onderkantCenter.add(summary, BorderLayout.EAST);

        //Totale kosten berekenen
        double totalePeriodiekeKosten = 0;
        for (Groep groep : netwerk.groepen) {
            //Lijst met alleen de Monitoringcomponenten
            ArrayList<Monitoringcomponent> monitoringcomponents = new ArrayList<>();
            for (Component component : groep.componenten) {
                if (component instanceof Monitoringcomponent) {
                    monitoringcomponents.add((Monitoringcomponent) component);
                }
            }
            for (Monitoringcomponent component : monitoringcomponents) {
                totalePeriodiekeKosten += Double.parseDouble(component.getPeriodiekeKosten().replaceAll(",", "."));
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String dfTotalePeriodiekeKosten = df.format(totalePeriodiekeKosten);

        JLabel totaleKosten = new JLabel("Totale periodieke kosten: €" + dfTotalePeriodiekeKosten);
        totaleKosten.setForeground(Color.white);

        JLabel totaleBeschikbaarheid = new JLabel("Totale beschikbaarheid: " + df.format(Groep.berekenBeschikbaarheidNetwerk(netwerk)) + "%");
        totaleBeschikbaarheid.setForeground(Color.white);
        summary.add(totaleKosten);
        summary.add(totaleBeschikbaarheid);

        //Panel voor componenten
        JPanel componentenPanel = new JPanel();
        componentenPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        componentenPanel.setBackground(backClr2);
        center.add(componentenPanel);

        //Componenten info op het scherm
        for (Groep groep : netwerk.groepen) {
            //Lijst met alleen de Monitoringcomponenten
            monitoringcomponents = new ArrayList<>();
            for (Component component : groep.componenten) {
                if (component instanceof Monitoringcomponent) {
                    monitoringcomponents.add((Monitoringcomponent) component);
                }
            }
            for (Monitoringcomponent monitoringcomponent : monitoringcomponents) {
                GridLayout gridLayout = new GridLayout(12, 1);
                JPanel jPanel = new JPanel(gridLayout);
                jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1, true), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
                jPanel.setBackground(backClr1);

                JLabel naam = new JLabel("Naam: " + monitoringcomponent.getNaam() + " (" + monitoringcomponent.getType() + ")");
                naam.setForeground(Color.white);
                jPanel.add(naam);

                status = new JLabel("Status: online");
                status.setForeground(Color.white);
                jPanel.add(status);

                JLabel enter1 = new JLabel("");
                jPanel.add(enter1);

                processor = new JLabel("CPU gebruik: " + monitoringcomponent.getProcessorbelasting() + "%");
                processor.setForeground(Color.white);
                jPanel.add(processor);

                werkgeheugen = new JLabel("Werkgeheugen gebruik: " + monitoringcomponent.getWerkgeheugenVerbruik() + "%");
                werkgeheugen.setForeground(Color.white);
                jPanel.add(werkgeheugen);

                totaalgeheugen = new JLabel("Totaal geheugen: " + monitoringcomponent.getTotaleDiskruimte() + " GB");
                totaalgeheugen.setForeground(Color.white);
                jPanel.add(totaalgeheugen);

                gebruiktegeheugen = new JLabel("Gebruikte geheugen: " + monitoringcomponent.getGebruikteDiskruimte() + " GB");
                gebruiktegeheugen.setForeground(Color.white);
                jPanel.add(gebruiktegeheugen);

                beschikbaregeheugen = new JLabel("Beschikbare geheugen: " + monitoringcomponent.getBeschikbareDiskruimte() + " GB");
                beschikbaregeheugen.setForeground(Color.white);
                jPanel.add(beschikbaregeheugen);

                uptime = new JLabel("Uptime sinds laatste reboot: " + monitoringcomponent.getBeschikbaarheidsduur() + " minuten");
                uptime.setForeground(Color.white);
                jPanel.add(uptime);

                JLabel enter2 = new JLabel("");
                jPanel.add(enter2);

                beschikbaarheid = new JLabel("Beschikbaarheid: " + monitoringcomponent.getBeschikbaarheidspercentage() + "%");
                beschikbaarheid.setForeground(Color.white);
                jPanel.add(beschikbaarheid);

                kosten = new JLabel("Periodieke kosten: €" + monitoringcomponent.getPeriodiekeKosten() + " per jaar");
                kosten.setForeground(Color.white);
                jPanel.add(kosten);

                componentenPanel.add(jPanel);
            }
        }
        getContentPane().setBackground(backClr2);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        javax.swing.Timer t = new javax.swing.Timer(5000, e1 -> {
            for (Monitoringcomponent monitoringcomponent: monitoringcomponents) {
                monitoringcomponent.setGegevensUitDatabase();
                status.setText("Online");//Gaat door middel van pingen behaald worden
                processor.setText("CPU gebruik: " + monitoringcomponent.getProcessorbelasting() + "%");
                werkgeheugen.setText("Werkgeheugen gebruik: " + monitoringcomponent.getWerkgeheugenVerbruik() + "%");
                totaalgeheugen.setText("Totaal geheugen: " + monitoringcomponent.getTotaleDiskruimte() + " GB");
                gebruiktegeheugen.setText("Gebruikte geheugen: " + monitoringcomponent.getGebruikteDiskruimte() + " GB");
                beschikbaregeheugen.setText("Beschikbare geheugen: " + monitoringcomponent.getBeschikbareDiskruimte() + " GB");
                uptime.setText("Uptime sinds laatste reboot: " + monitoringcomponent.getBeschikbaarheidsduur() + " minuten");
                beschikbaarheid.setText("Beschikbaarheid: " + monitoringcomponent.getBeschikbaarheidspercentage() + "%");
                kosten.setText("Periodieke kosten: €" + monitoringcomponent.getPeriodiekeKosten() + " per jaar");
            }
        });
        t.start();

        if(e.getSource()==jcDropDownMenu) {
//            System.out.println(jcDropDownMenu.getSelectedItem()); Kan gebruikt worden als testfunctie op welk dropdownmenu item geklikt is.
        }

        if(jcDropDownMenu.getSelectedItem().equals("Programma sluiten")){
            dispose();
        }

        if (e.getSource()==jbOntwerpen){
            dispose();
            OntwerpFrame ontwerpFrame = new OntwerpFrame();
        }
    }
}