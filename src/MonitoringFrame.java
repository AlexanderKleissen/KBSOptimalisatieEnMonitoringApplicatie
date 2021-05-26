import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class MonitoringFrame extends JFrame implements ActionListener {
    private JComboBox<String> jcDropDownMenu;
    private String[] comboBoxContent;
    private JButton jbMonitoren, jbOntwerpen, jbPfSenseLink; //buttons voor footer
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

                status.add(new JLabel("Status: proberen te verbinden..."));
                status.get(arraylengte).setForeground(Color.white);
                jPanel.add(status.get(arraylengte));


                if (!monitoringcomponent.getType().equals("Firewall")) {
                    JLabel enter1 = new JLabel("");
                    jPanel.add(enter1);

                    processor.add(new JLabel("CPU gebruik: " + monitoringcomponent.getProcessorbelasting() + "%"));
                    processor.get(arraylengte-1).setForeground(Color.white);
                    jPanel.add(processor.get(arraylengte-1));

                    werkgeheugen.add(new JLabel("Werkgeheugen gebruik: " + monitoringcomponent.getWerkgeheugenVerbruik() + "%"));
                    werkgeheugen.get(arraylengte-1).setForeground(Color.white);
                    jPanel.add(werkgeheugen.get(arraylengte-1));

                    totaalgeheugen.add(new JLabel("Totaal geheugen: " + monitoringcomponent.getTotaleDiskruimte() + " GB"));
                    totaalgeheugen.get(arraylengte-1).setForeground(Color.white);
                    jPanel.add(totaalgeheugen.get(arraylengte-1));

                    gebruiktegeheugen.add(new JLabel("Gebruikte geheugen: " + monitoringcomponent.getGebruikteDiskruimte() + " GB"));
                    gebruiktegeheugen.get(arraylengte-1).setForeground(Color.white);
                    jPanel.add(gebruiktegeheugen.get(arraylengte-1));

                    beschikbaregeheugen.add(new JLabel("Beschikbare geheugen: " + monitoringcomponent.getBeschikbareDiskruimte() + " GB"));
                    beschikbaregeheugen.get(arraylengte-1).setForeground(Color.white);
                    jPanel.add(beschikbaregeheugen.get(arraylengte-1));

                    uptime.add(new JLabel("Uptime sinds laatste reboot: " + monitoringcomponent.getBeschikbaarheidsduur() + " minuten"));
                    uptime.get(arraylengte-1).setForeground(Color.white);
                    jPanel.add(uptime.get(arraylengte-1));

                    JLabel enter2 = new JLabel("");
                    jPanel.add(enter2);

                    beschikbaarheid.add(new JLabel("Beschikbaarheid: " + monitoringcomponent.getBeschikbaarheidspercentage() + "%"));
                    beschikbaarheid.get(arraylengte-1).setForeground(Color.white);
                    jPanel.add(beschikbaarheid.get(arraylengte-1));

                    kosten.add(new JLabel("Periodieke kosten: €" + monitoringcomponent.getPeriodiekeKosten() + " per jaar"));
                    kosten.get(arraylengte-1).setForeground(Color.white);
                    jPanel.add(kosten.get(arraylengte-1));
                } else {
                    JLabel enter = new JLabel("                                                                           ", SwingConstants.CENTER);
                    enter.setForeground(Color.WHITE);
                    jPanel.add(enter);

                    jbPfSenseLink = new JButton("PfSense link");
                    jbPfSenseLink.setPreferredSize(new Dimension(1,17));

                    jbPfSenseLink.addActionListener(this);
                    jPanel.add(jbPfSenseLink);
                    jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1, true), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
                }
                componentenPanel.add(jPanel);
                arraylengte++;
            }
        }
        getContentPane().setBackground(backClr2);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        javax.swing.Timer t = new javax.swing.Timer(5000, e1 -> {
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
                try {
                    boolean monitoringComponentBeschikbaar = false;

                    if (monitoringcomponent1.getType().equals("HAL9001W")||monitoringcomponent1.getType().equals("HAL9002W")||monitoringcomponent1.getType().equals("HAL9003W")) {
                        try (Socket socket = new Socket()) {
                            socket.connect(new InetSocketAddress(monitoringcomponent1.getIpaddress(), 80), 500);
                             monitoringComponentBeschikbaar = true;
                        } catch (IOException exception) {
                            monitoringComponentBeschikbaar = false;
                        }
                    } else {
                        InetAddress inet = InetAddress.getByName(monitoringcomponent1.getIpaddress());
                        monitoringComponentBeschikbaar = inet.isReachable(500);
                    }


                    if (monitoringComponentBeschikbaar && pfSenseStatus) {
                        status.get(arraylengte).setText("Status: online");
                    } else if (!monitoringComponentBeschikbaar && pfSenseStatus) {
                        status.get(arraylengte).setText("Status: offline");
                    } else if (!monitoringComponentBeschikbaar && !pfSenseStatus && !monitoringcomponent1.getType().equals("Firewall")) {
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
                if (!monitoringcomponent1.getType().equals("Firewall")) {
                    processor.get(arraylengte-1).setText("CPU gebruik: " + monitoringcomponent1.getProcessorbelasting() + "%");
                    werkgeheugen.get(arraylengte-1).setText("Werkgeheugen gebruik: " + monitoringcomponent1.getWerkgeheugenVerbruik() + "%");
                    totaalgeheugen.get(arraylengte-1).setText("Totaal geheugen: " + monitoringcomponent1.getTotaleDiskruimte() + " GB");
                    gebruiktegeheugen.get(arraylengte-1).setText("Gebruikte geheugen: " + monitoringcomponent1.getGebruikteDiskruimte() + " GB");
                    beschikbaregeheugen.get(arraylengte-1).setText("Beschikbare geheugen: " + monitoringcomponent1.getBeschikbareDiskruimte() + " GB");
                    uptime.get(arraylengte-1).setText("Uptime sinds laatste reboot: " + monitoringcomponent1.getBeschikbaarheidsduur() + " minuten");
                    beschikbaarheid.get(arraylengte-1).setText("Beschikbaarheid: " + monitoringcomponent1.getBeschikbaarheidspercentage() + "%");
                    kosten.get(arraylengte-1).setText("Periodieke kosten: €" + monitoringcomponent1.getPeriodiekeKosten() + " per jaar");
                }
                arraylengte++;
            }
        });
        t.start();

        if(e.getSource()==jcDropDownMenu) {
//            System.out.println(jcDropDownMenu.getSelectedItem()); //Kan gebruikt worden als testfunctie op welk dropdownmenu item geklikt is.
        }

        if(jcDropDownMenu.getSelectedItem().equals("Programma sluiten")){
            dispose();
        }

        if (e.getSource()==jbOntwerpen){
            dispose();
            OntwerpFrame ontwerpFrame = new OntwerpFrame();
            ontwerpFrame.add(monitoringnetwerk);
        }

        if (e.getSource()==jbPfSenseLink) {
            URI uri= null;
            try {
                uri = new URI("http://11.11.200.1");
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }

            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException exception) {
                System.out.println("De webpagina is niet beschikbaar");
            }
        }
    }
}