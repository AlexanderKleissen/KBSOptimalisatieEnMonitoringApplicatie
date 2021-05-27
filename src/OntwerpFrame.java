import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;

public class OntwerpFrame extends JFrame implements ActionListener {
    private JComboBox<String> jcDropDownMenu;
    private String[] comboBoxContent;
    private JButton jbMonitoren, jbOntwerpen; //buttons
    private Ontwerpnetwerk ontwerpnetwerk;
    private boolean gedruktOpOptimalisatieknop;
    private Color backClr1 = new Color(60, 63, 65); //de kleur van de rest
    private Color backClr2 = new Color(43, 43, 43); //de kleur van het midden
    private Monitoringnetwerk monitoringnetwerk;



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
        comboBoxContent = new String[] {"Nieuw ontwerp", "Open ontwerp", "Sluit ontwerp", "Programma sluiten"}; //Array voor de teksten binnen de JComboBox

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
        setVisible(true);
    }

    public OntwerpFrame(Ontwerpnetwerk netwerk){
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
        JPanel summary = new JPanel(new GridLayout(2,1));

        summary.setBorder(BorderFactory.createLineBorder(Color.white));

        summary.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,0,7,20), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white), BorderFactory.createEmptyBorder(5,5,5,5))));
        summary.setBackground(backClr2);
        onderkantCenter.add(summary, BorderLayout.EAST);


        //Panel voor componenten
        JPanel componentenPanel = new JPanel();
        componentenPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10,10));
        componentenPanel.setBackground(backClr2);
        center.add(componentenPanel);

        //netwerk aan frame koppelen
        this.ontwerpnetwerk = netwerk;

        JLabel totaleBeschikbaarheid = new JLabel();
        totaleBeschikbaarheid.setForeground(Color.white);
        summary.add(totaleBeschikbaarheid);

        JLabel totaleKosten = new JLabel();
        totaleKosten.setForeground(Color.white);
        summary.add(totaleKosten);


    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
        Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
        //ResultSet rs = statement.executeQuery("select * from ontwerpnetwerk where NaamNetwerk = '" + netwerk.getNaam() +"'"); //Query uitvoeren
        ResultSet rs = statement.executeQuery("select * from ontwerpnetwerk where NaamNetwerk = '" + netwerk.getNaam() + "'");
        //Hierdoor gaat de Resultset naar de volgende regel. Als dit er niet in staat dan zal er geen resultaat uit komen.

        while (rs.next()) {
            DecimalFormat drieDecimalen = new DecimalFormat("0.000");
            DecimalFormat tweeDecimalen = new DecimalFormat("0.00");

            double totaleBeschikbaarheidText = rs.getDouble(4);
            double totaleKostenText = rs.getDouble(5);

            totaleBeschikbaarheid.setText("Totale beschikbaarheid: " + drieDecimalen.format(totaleBeschikbaarheidText));
            totaleKosten.setText("Totale kosten: €" + tweeDecimalen.format(totaleKostenText));

            int aantalComponenten = rs.getInt(3);
            while (aantalComponenten > 0) {
                JPanel jPanel = new JPanel(new GridLayout(4, 1));
                jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1, true), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
                jPanel.setBackground(backClr2);

                JLabel naam = new JLabel("Naam: " + rs.getString(2));
                naam.setForeground(Color.white);
                jPanel.add(naam);

                if (rs.getString(2).equals("HAL9001DB")) {
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 90%");
                    beschikbaarheid.setForeground(Color.white);
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €5100,-");
                    kosten.setForeground(Color.white);
                    jPanel.add(kosten);
                } else if (rs.getString(2).equals("HAL9002DB")) {
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 95%");
                    beschikbaarheid.setForeground(Color.white);
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €7700,-");
                    kosten.setForeground(Color.white);
                    jPanel.add(kosten);
                } else if (rs.getString(2).equals("HAL9003DB")) {
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 95%");
                    beschikbaarheid.setForeground(Color.white);
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €12200,-");
                    kosten.setForeground(Color.white);
                    jPanel.add(kosten);
                } else if (rs.getString(2).equals("HAL9001W")) {
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 80%");
                    beschikbaarheid.setForeground(Color.white);
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €2200,-");
                    kosten.setForeground(Color.white);
                    jPanel.add(kosten);
                } else if (rs.getString(2).equals("HAL9002W")) {
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 90%");
                    beschikbaarheid.setForeground(Color.white);
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €3200,-");
                    kosten.setForeground(Color.white);
                    jPanel.add(kosten);
                } else if (rs.getString(2).equals("HAL9003W")) {
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 95%");
                    beschikbaarheid.setForeground(Color.white);
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €5100,-");
                    kosten.setForeground(Color.white);
                    jPanel.add(kosten);
                } else if (rs.getString(2).equals("pfSense")) {
                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 99,998%");
                    beschikbaarheid.setForeground(Color.white);
                    jPanel.add(beschikbaarheid);
                    JLabel kosten = new JLabel("Kosten: €4000,-");
                    kosten.setForeground(Color.white);
                    jPanel.add(kosten);
                }
                aantalComponenten--;
                componentenPanel.add(jPanel);
            }
        }
        rs.close();
        connection.close();


    } catch (SQLException e) {
        System.out.println(e);
    }


//
//
//
//        //Componenten info op het scherm
//        for(Groep groep: netwerk.groepen) {
//            for (Component component : groep.componenten) {
//                JPanel jPanel = new JPanel(new GridLayout(4, 1));
//                jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1, true), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
//                jPanel.setBackground(backClr2);
//                if(component instanceof Ontwerpcomponent) {
//                    JLabel naam = new JLabel("Naam: " + component.getNaam());
//                    naam.setForeground(Color.white);
//                    jPanel.add(naam);
//                    JLabel beschikbaarheid = new JLabel("Beschikbaarheid: " + component.getBeschikbaarheidspercentage() + "%");
//                    beschikbaarheid.setForeground(Color.white);
//                    jPanel.add(beschikbaarheid);
//                    JLabel kosten = new JLabel("Kosten: €" + ((Ontwerpcomponent) component).getKosten());
//                    kosten.setForeground(Color.white);
//                    jPanel.add(kosten);
//        //                   center.add(jPanel);
//                    componentenPanel.add(jPanel);
//                }
//            }
//        }
                setVisible(true);
    }


    //Getters en setters
    public Ontwerpnetwerk getOntwerpnetwerk() {
        return ontwerpnetwerk;
    }

    public void setMonitoringnetwerk(Monitoringnetwerk monitoringnetwerk) {
        this.monitoringnetwerk = monitoringnetwerk;
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
                try {
                    OptimalisatieDialog optimalisatieDialog = new OptimalisatieDialog(this);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                //Als er op nieuw ontwerp wordt geklikt in het drop down menu dan word een optimalisatiescherm geopent.
            }
            if(jcDropDownMenu.getSelectedItem().equals("Open ontwerp")){
                KiesNetwerkDialog kiesNetwerk = new KiesNetwerkDialog(this); //Een keuzelijst voor alle beschikbare netwerken wordt weergegeven
            }
            if(jcDropDownMenu.getSelectedItem().equals("Optimaliseer ontwerp")){
                //Zelfde functie als de optimalisatieknop.
                gedruktOpOptimalisatieknop = true;
                try {
                    OptimalisatieDialog optimalisatieDialog = new OptimalisatieDialog(this);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(jcDropDownMenu.getSelectedItem().equals("Sluit ontwerp")){
                OntwerpFrame ontwerpFrame = new OntwerpFrame();
                dispose();
            }
        } else if (e.getSource()==jbMonitoren){
            MonitoringFrame monitoringFrame = new MonitoringFrame(monitoringnetwerk);
            dispose();
        }
    }

    public void add(Monitoringnetwerk netwerk) {
        this.monitoringnetwerk = netwerk;
    }
}