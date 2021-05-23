import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class MonitoringFrame extends JFrame implements ActionListener {
    private JComboBox<String> jcDropDownMenu;
    private String[] comboBoxContent;
    private JButton jbMonitoren, jbOntwerpen; //buttons voor footer
    private Color backClr1 = new Color(60, 63, 65); //de kleur van de rest
    private Color backClr2 = new Color(43, 43, 43); //de kleur van het midden
    private ArrayList<Monitoringcomponent> monitoringcomponenten = new ArrayList<>();
    private Monitoringnetwerk monitoringnetwerk;
    private int arraylengte;
    private boolean pfSenseStatus;
    private ArrayList <JLabel> status = new ArrayList<>();
    private ArrayList <JLabel> processor = new ArrayList<>();
    private ArrayList <JLabel> werkgeheugen = new ArrayList<>();
    private ArrayList <JLabel> totaalgeheugen = new ArrayList<>();
    private ArrayList <JLabel> gebruiktegeheugen = new ArrayList<>();
    private ArrayList <JLabel> beschikbaregeheugen = new ArrayList<>();
    private ArrayList <JLabel> uptime = new ArrayList<>();
    private ArrayList <JLabel> beschikbaarheid = new ArrayList<>();
    private ArrayList <JLabel> kosten = new ArrayList<>();


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
        this.monitoringnetwerk = netwerk;
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
            ArrayList<Monitoringcomponent> monitoringcomponents = new ArrayList<>();
            for (Component component : groep.componenten) {
                if (component instanceof Monitoringcomponent) {
                    monitoringcomponents.add((Monitoringcomponent) component);
                }
            }
            for (Monitoringcomponent monitoringcomponent : monitoringcomponents) {
                monitoringcomponenten.add(monitoringcomponent);
                GridLayout gridLayout = new GridLayout(12, 1);
                JPanel jPanel = new JPanel(gridLayout);
                jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1, true), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
                jPanel.setBackground(backClr1);

                JLabel naam = new JLabel("Naam: " + monitoringcomponent.getNaam() + " (" + monitoringcomponent.getType() + ")");
                naam.setForeground(Color.white);
                jPanel.add(naam);

                try {
                    InetAddress inet = InetAddress.getByName(monitoringcomponent.getIpaddress());
                    if (inet.isReachable(500)) {
                        status.add(new JLabel("Status: online"));
                    } else {
                        status.add(new JLabel("Status: offline"));
                    }
                } catch (UnknownHostException e) {
                    status.add(new JLabel("Status: offline"));
                } catch (IOException e) {
                    status.add(new JLabel("Status: offline"));
                }
                status.get(arraylengte).setForeground(Color.white);
                jPanel.add(status.get(arraylengte));

                JLabel enter1 = new JLabel("");
                jPanel.add(enter1);

                processor.add(new JLabel("CPU gebruik: " + monitoringcomponent.getProcessorbelasting() + "%"));
                processor.get(arraylengte).setForeground(Color.white);
                jPanel.add(processor.get(arraylengte));

                werkgeheugen.add(new JLabel("Werkgeheugen gebruik: " + monitoringcomponent.getWerkgeheugenVerbruik() + "%"));
                werkgeheugen.get(arraylengte).setForeground(Color.white);
                jPanel.add(werkgeheugen.get(arraylengte));

                totaalgeheugen.add(new JLabel("Totaal geheugen: " + monitoringcomponent.getTotaleDiskruimte() + " GB"));
                totaalgeheugen.get(arraylengte).setForeground(Color.white);
                jPanel.add(totaalgeheugen.get(arraylengte));

                gebruiktegeheugen.add(new JLabel("Gebruikte geheugen: " + monitoringcomponent.getGebruikteDiskruimte() + " GB"));
                gebruiktegeheugen.get(arraylengte).setForeground(Color.white);
                jPanel.add(gebruiktegeheugen.get(arraylengte));

                beschikbaregeheugen.add(new JLabel("Beschikbare geheugen: " + monitoringcomponent.getBeschikbareDiskruimte() + " GB"));
                beschikbaregeheugen.get(arraylengte).setForeground(Color.white);
                jPanel.add(beschikbaregeheugen.get(arraylengte));

                uptime.add(new JLabel("Uptime sinds laatste reboot: " + monitoringcomponent.getBeschikbaarheidsduur() + " minuten"));
                uptime.get(arraylengte).setForeground(Color.white);
                jPanel.add(uptime.get(arraylengte));

                JLabel enter2 = new JLabel("");
                jPanel.add(enter2);

                beschikbaarheid.add(new JLabel("Beschikbaarheid: " + monitoringcomponent.getBeschikbaarheidspercentage() + "%"));
                beschikbaarheid.get(arraylengte).setForeground(Color.white);
                jPanel.add(beschikbaarheid.get(arraylengte));

                kosten.add(new JLabel("Periodieke kosten: €" + monitoringcomponent.getPeriodiekeKosten() + " per jaar"));
                kosten.get(arraylengte).setForeground(Color.white);
                jPanel.add(kosten.get(arraylengte));

                componentenPanel.add(jPanel);
                arraylengte++;
            }
        }
        getContentPane().setBackground(backClr2);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        javax.swing.Timer t = new javax.swing.Timer(1000, e1 -> {
            arraylengte = 0;
            try {
                for (Monitoringcomponent monitoringcomponent: monitoringcomponenten) {
                    if (monitoringcomponent.getType().equals("Firewall")) {
                        InetAddress inet = InetAddress.getByName(monitoringcomponent.getIpaddress());
                        pfSenseStatus = inet.isReachable(500);
                    }
                }
            } catch (UnknownHostException exception) {
                pfSenseStatus = false;
            } catch (IOException exception) {
                pfSenseStatus = false;
            }

            for (Monitoringcomponent monitoringcomponent1: monitoringcomponenten) {
                System.out.println(pfSenseStatus);
                try {
                    InetAddress inet = InetAddress.getByName(monitoringcomponent1.getIpaddress());
                    if (inet.isReachable(500) && pfSenseStatus) {
                        status.get(arraylengte).setText("Status: online");
                    } else if (!inet.isReachable(500) && pfSenseStatus) {
                        status.get(arraylengte).setText("Status: offline");
                    } else if (!inet.isReachable(500) && !pfSenseStatus && !monitoringcomponent1.getType().equals("Firewall")) {
                        status.get(arraylengte).setText("Status: ?");
                    } else {
                        status.get(arraylengte).setText("Status: offline");
                    }
                } catch (UnknownHostException exception1) {
                    status.get(arraylengte).setText("Status: offline");
                } catch (IOException exception2) {
                    status.get(arraylengte).setText("Status: offline");
                }
                monitoringcomponent1.setGegevensUitDatabase();
                processor.get(arraylengte).setText("CPU gebruik: " + monitoringcomponent1.getProcessorbelasting() + "%");
                werkgeheugen.get(arraylengte).setText("Werkgeheugen gebruik: " + monitoringcomponent1.getWerkgeheugenVerbruik() + "%");
                totaalgeheugen.get(arraylengte).setText("Totaal geheugen: " + monitoringcomponent1.getTotaleDiskruimte() + " GB");
                gebruiktegeheugen.get(arraylengte).setText("Gebruikte geheugen: " + monitoringcomponent1.getGebruikteDiskruimte() + " GB");
                beschikbaregeheugen.get(arraylengte).setText("Beschikbare geheugen: " + monitoringcomponent1.getBeschikbareDiskruimte() + " GB");
                uptime.get(arraylengte).setText("Uptime sinds laatste reboot: " + monitoringcomponent1.getBeschikbaarheidsduur() + " minuten");
                beschikbaarheid.get(arraylengte).setText("Beschikbaarheid: " + monitoringcomponent1.getBeschikbaarheidspercentage() + "%");
                kosten.get(arraylengte).setText("Periodieke kosten: €" + monitoringcomponent1.getPeriodiekeKosten() + " per jaar");
                arraylengte++;
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