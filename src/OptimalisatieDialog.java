import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class OptimalisatieDialog extends JDialog implements ActionListener {
    private OntwerpFrame ontwerpFrame;
    private JLabel minimaalTotaleBeschikbaarheid;
    private JTextField jtMinimaalTotaleBeschikbaarheid;  //hierin wordt minimaal gewenste beschikbaarheidspercentage opgegeven
    private JButton bereken;
    private Double beschikbaarheidspercentage = 0.000;   //berekende beschikbaarheidspercentage uit optimalisatiefunctie
    private String totaleBeschikbaarheidNetwerkTekst;
    private JLabel beschikbaarheidspercentageLabel;
    private Double totaleBedrag = 0.00;                  //berekende totale bedrag uit optimalisatiefunctie
    private JLabel totaleBedragLabel;
    private JButton voegToe;
    private Color backClr1 = new Color(60, 63, 65); //achtergrondkleur
    private Color backClr2 = new Color(43, 43, 43); //tabelkleur
    private ArrayList<Ontwerpcomponent> ontwerpcomponenten = new ArrayList<>();
    private ArrayList<Ontwerpcomponent> webservers = new ArrayList<>();
    private ArrayList<Ontwerpcomponent> dbservers = new ArrayList<>();
    private ArrayList<Ontwerpcomponent> optimaleWaarden = new ArrayList<>();
//    ArrayList<Ontwerpcomponent> serversHuidigeOplossing = new ArrayList<>();
//    ArrayList<Ontwerpcomponent> serversBesteOplossing = new ArrayList<>();
//    private ArrayList<ArrayList<Ontwerpcomponent>> netwerkHuidigeOplossing = new ArrayList<>();
//    private ArrayList<ArrayList<Ontwerpcomponent>> netwerkBesteOplossing = new ArrayList<>();
//    private ArrayList<ArrayList<Ontwerpcomponent>> netwerkEersteOplossing = new ArrayList<>();
//    private ArrayList<ArrayList<ArrayList<Ontwerpcomponent>>> netwerkOplossingOpslag = new ArrayList<>();
//    private ArrayList<Ontwerpcomponent> besteOplossing = new ArrayList<>();
//    private double beschikbaarheid = 0;
//    private double kosten = 0;
//    private double laagsteKosten = 10000000;
//    private double laagsteKostenNetwerk = 100000000;
//    private int diepte = 100;//Het aantal servers van de opgeslagen oplossing

    private static int maxLoop = 8;
    private static int[] aantalWebservers = {0,0,0};

    private static double[] webserverBeschikbaarheid = {0.9,0.8,0.95};
    private static double[] wbKosten = {3200,2200,5100};
    private static int maxWBSrt = 2;
    private static int[] aantalDatabases = {0,0,0};

    private static double[] databaseBeschikbaarheid = {0.9,0.95,0.98};
    private static double[] dbKosten = {5100,7700,12200};
    private static int maxDBSrt = 2;
    private static double minBeschikbaarheid = 0;
    private static double minKosten = Double.MAX_VALUE;
    private static int totaalTeller = 0;
    private static String resultaat;

    private JTable tabel2;

    private Object[][] data2;


    public OptimalisatieDialog(OntwerpFrame frame) throws SQLException {
        super(frame, false);
        this.ontwerpFrame = frame;
        setSize(1500, 1000);
        setLayout(new BorderLayout());
        UIManager.getLookAndFeelDefaults().put("TextField.caretForeground", Color.white);
        setTitle("Monitoringapplicatie (optimaliseren)");
        DatabaseConnectieErrorDialog databaseConnectieErrorDialog = new DatabaseConnectieErrorDialog();

        //Bovenste twee labels:
        Border witteRand = BorderFactory.createLineBorder(Color.WHITE, 1);                   //rand voor de meeste onderdelen
        Dimension dimensie = new Dimension(700, 100);                                     // hoogte en breedte voor de bovenste twee labels
        JLabel mogelijkeComponenten = new JLabel("Specificaties mogelijke ontwerpcomponenten");
        mogelijkeComponenten.setBorder(witteRand);
        mogelijkeComponenten.setPreferredSize(dimensie);
        mogelijkeComponenten.setForeground(Color.white);

        JLabel optimaleWaardenOntwerp = new JLabel("Netwerk ontwerp");
        optimaleWaardenOntwerp.setBorder(witteRand);
        optimaleWaardenOntwerp.setPreferredSize(dimensie);
        optimaleWaardenOntwerp.setForeground(Color.white);

        //Ontwerpcomponenten uit database halen
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM component_ontwerpen");
        while (rs.next()) {
            ontwerpcomponenten.add(new Ontwerpcomponent(rs.getString("NaamComponent"), rs.getString("Type"),
                    rs.getDouble("Kosten"), rs.getDouble("Beschikbaarheid")));

        }

        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
            System.out.println(ontwerpcomponent.getNaam() + " " + ontwerpcomponent.getType() + " " + ontwerpcomponent.getKosten() +
                    " " + ontwerpcomponent.getBeschikbaarheidspercentage());
        }

        statement.close();
        connection.close();
        //de beschikbare ontwerpcomponenten (nu hard gecodeerd, in de toekomst uit database halen)

//        Ontwerpcomponent firewallpfSense = new Ontwerpcomponent("pfSense", "firewall", 4000, 99.998, "firewallImage");
//        Ontwerpcomponent HAL9001DB = new Ontwerpcomponent("HAL9001DB", "database", 5100,  90, "dbserverImage");
//        Ontwerpcomponent HAL9002DB = new Ontwerpcomponent("HAL9002DB", "database", 7700,  95, "dbserverImage");
//        Ontwerpcomponent HAL9003DB = new Ontwerpcomponent("HAL9003DB", "database", 12200,  98, "dbserverImage");
//        Ontwerpcomponent HAL9001W = new Ontwerpcomponent("HAL9001W", "webserver", 2200,  80, "webserverImage");
//        Ontwerpcomponent HAL9002W = new Ontwerpcomponent("HAL9002W", "webserver", 3200,  90, "webserverImage");
//        Ontwerpcomponent HAL9003W = new Ontwerpcomponent("HAL9003W", "webserver", 5100,  95, "webserverImage");

        //De componenten toevoegen aan de arraylist van ontwerpcomponenten
//        ontwerpcomponenten.add(firewallpfSense);
//        ontwerpcomponenten.add(HAL9001DB);
//        ontwerpcomponenten.add(HAL9002DB);
//        ontwerpcomponenten.add(HAL9003DB);
//        ontwerpcomponenten.add(HAL9001W);
//        ontwerpcomponenten.add(HAL9002W);
//        ontwerpcomponenten.add(HAL9003W);

        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
            if (ontwerpcomponent.getType().equals("database")) {
                dbservers.add(ontwerpcomponent);
            }
            if (ontwerpcomponent.getType().equals("webserver")) {
                webservers.add(ontwerpcomponent);
            }
        }
//        dbservers.add(HAL9001DB);
//        dbservers.add(HAL9002DB);
//        dbservers.add(HAL9003DB);
//        webservers.add(HAL9001W);
//        webservers.add(HAL9002W);
//        webservers.add(HAL9003W);


        //Linker tabel:
        String[] kolomnamen = {"Naam component", "Type", "Kosten (€)", "Beschikbaarheid (%)"};     //een array met kolomnamen
        Object[][] data = new Object[ontwerpcomponenten.size()][4];
        /*Een array van het type Object maakt het mogelijk meerdere soorten data op te slaan in de array.*/

        // van elk bestaand ontwerpcomponent de naam, het type, de kosten en de beschikbaarheid in een tweedimensionale array zetten
        int j = 0;
        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
            data[j][0] = ontwerpcomponent.getNaam();
            data[j][1] = ontwerpcomponent.getType();
            data[j][2] = ontwerpcomponent.getKosten();
            data[j][3] = ontwerpcomponent.getBeschikbaarheidspercentage();
            j++;
        }


        JTable tabel1 = new JTable(data, kolomnamen);                                                 // kolomnamen en data meegeven aan de tabel
        tabel1.setEnabled(false);                                                                     // data kan nu niet door gebruiker aangepast worden
        tabel1.setBackground(backClr2);
        tabel1.setForeground(Color.white);

        //de bovenstaande code verandert alleen de gegevens, dus hieronder worden de headers apart ook gekleurd
        JTableHeader header1 = tabel1.getTableHeader();
        header1.setBackground(backClr1);
        header1.setForeground(Color.white);

        //Van kolommen in tabel1 een TableColumnobject maken en vervolgens de gewenste breedte meegeven:
        TableColumn tabel1kolom1 = tabel1.getColumnModel().getColumn(0);                 // kolom 1
        tabel1kolom1.setPreferredWidth(174);

        TableColumn tabel1kolom2 = tabel1.getColumnModel().getColumn(1);                 // kolom 2
        tabel1kolom2.setPreferredWidth(98);

        TableColumn tabel1kolom3 = tabel1.getColumnModel().getColumn(2);                 // kolom 3
        tabel1kolom3.setPreferredWidth(98);

        TableColumn tabel1kolom4 = tabel1.getColumnModel().getColumn(3);                 // kolom 4
        tabel1kolom4.setPreferredWidth(145);

        Dimension dimensieScrollPaneTabel1en2 = new Dimension(700, 500);                // vaste hoogte en breedte voor de scrollPane (container voor de tabel)
        JScrollPane scrollPaneTabel1 = new JScrollPane(tabel1);
        scrollPaneTabel1.setPreferredSize(dimensieScrollPaneTabel1en2);
        tabel1.setFillsViewportHeight(true);                                                       //tabel vult de hele scrollPane ongeacht aantal rijen


        //De optimale componenten naar aanleiding van optimalisatiefunctie toevoegen aan de arraylist van optimaleWaarden
        //Nu een voorbeeld van een optimaal ontwerp met een beschikbaarheid van 99.990% gebruikt.

//        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
//            if (ontwerpcomponent.getNaam().equals("HAL9001DB")) {
//                for (int i = 0; i < 3; i++) {
//                    optimaleWaarden.add(ontwerpcomponent);
//                }
//            }
//            if (ontwerpcomponent.getNaam().equals("HAL9002DB")) {
//                optimaleWaarden.add(ontwerpcomponent);
//            }
//
//            if (ontwerpcomponent.getNaam().equals("HAL9003DB")) {
//                optimaleWaarden.add(ontwerpcomponent);
//            }
//
//            if (ontwerpcomponent.getNaam().equals("HAL9001W")) {
//                optimaleWaarden.add(ontwerpcomponent);
//            }
//
//            if (ontwerpcomponent.getNaam().equals("HAL9002W")) {
//                for (int k = 0; k < 4; k++) {
//                    optimaleWaarden.add(ontwerpcomponent);
//                }
//            }
//
//            if (ontwerpcomponent.getNaam().equals("HAL9003W")) {
//                optimaleWaarden.add(ontwerpcomponent);
//            }
//
//            if (ontwerpcomponent.getNaam().equals("pfSense")) {
//                optimaleWaarden.add(ontwerpcomponent);
//            }
//        }


//        optimaleWaarden.add(HAL9001DB);
//        optimaleWaarden.add(HAL9001DB);
//        optimaleWaarden.add(HAL9001DB);
//        optimaleWaarden.add(HAL9002DB);
//        optimaleWaarden.add(HAL9001W);
//        optimaleWaarden.add(HAL9002W);
//        optimaleWaarden.add(HAL9002W);
//        optimaleWaarden.add(HAL9002W);
//        optimaleWaarden.add(HAL9002W);
//        optimaleWaarden.add(firewallpfSense);


        //rechter tabel:
        String[] kolomnamen2 = {"Naam component", "Type", "Kosten (€)", "Beschikbaarheid (%)", "Aantal"};
        data2 = new Object[ontwerpcomponenten.size()][5];

//        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
//            int index = ontwerpcomponenten.indexOf(ontwerpcomponent);
////            int k = 0;
//            for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
//                if (ontwerpcomponent2.equals(ontwerpcomponent)) {
////                    k++;
//                    data2[index][0] = ontwerpcomponent.getNaam();
//                    data2[index][1] = ontwerpcomponent.getType();
//                    data2[index][2] = ontwerpcomponent.getKosten();
//                    data2[index][3] = ontwerpcomponent.getBeschikbaarheidspercentage();
//                    data2[index][4] = 0;
//                }
//            }
//        }
//
               int j2 = 0;
               for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
                   data2[j2][0] = ontwerpcomponent.getNaam();
                   data2[j2][1] = ontwerpcomponent.getType();
                   data2[j2][2] = ontwerpcomponent.getKosten();
                   data2[j2][3] = ontwerpcomponent.getBeschikbaarheidspercentage();
                   data2[j2][4] = 0;
                   j2++;
               }


        tabel2 = new JTable(data2, kolomnamen2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (row==0 && column == 4||row==1 && column == 4||row==1 && column == 4||row==2 && column == 4||row==3 && column == 4||row==4 && column == 4||row==5 && column == 4||row==6 && column == 4) {
                    return true;
                } else
                    return false;
            }
        };
        tabel2.isCellEditable(0,4);
        tabel2.setBackground(backClr2);
        tabel2.setForeground(Color.white);

        JTableHeader header2 = tabel2.getTableHeader();
        header2.setBackground(backClr1);
        header2.setForeground(Color.white);


        //kolommen in tabel2 de gewenste breedte geven:
        TableColumn tabel2kolom1 = tabel2.getColumnModel().getColumn(0);                // kolom 1
        tabel2kolom1.setPreferredWidth(164);

        TableColumn tabel2kolom2 = tabel2.getColumnModel().getColumn(1);                // kolom 2
        tabel2kolom2.setPreferredWidth(87);

        TableColumn tabel2kolom3 = tabel2.getColumnModel().getColumn(2);                // kolom 3
        tabel2kolom3.setPreferredWidth(88);

        TableColumn tabel2kolom4 = tabel2.getColumnModel().getColumn(3);                // kolom 4
        tabel2kolom4.setPreferredWidth(136);

        TableColumn tabel2kolom5 = tabel2.getColumnModel().getColumn(4);                // kolom 5
        tabel2kolom5.setPreferredWidth(155);


        JScrollPane scrollPaneTabel2 = new JScrollPane(tabel2);
        scrollPaneTabel2.setPreferredSize(dimensieScrollPaneTabel1en2);
        tabel2.setFillsViewportHeight(true);


        //Controls van JPanel links onderin
        minimaalTotaleBeschikbaarheid = new JLabel("Minimaal totaal beschikbaarheidspercentage:");
        minimaalTotaleBeschikbaarheid.setForeground(Color.white);

        jtMinimaalTotaleBeschikbaarheid = new JTextField(7);
        jtMinimaalTotaleBeschikbaarheid.setBackground(backClr1);
        jtMinimaalTotaleBeschikbaarheid.setForeground(Color.white);

        bereken = new JButton("Bereken optimale waarden");
        bereken.setBackground(backClr1);
        bereken.setForeground(Color.white);
        bereken.addActionListener(this);                                                        //knop handelt ingevoerd percentage af in 'jtMinimaalTotaleBeschikbaarheid'

        //Jpanel links onderin
        JPanel panelLinksOnder = new JPanel();
        panelLinksOnder.setLayout(new GridBagLayout());                                           //gridbaglayout
        panelLinksOnder.setBackground(backClr1);

        //GridBagConstraints aanmaken (bestaat uit kolommen en rijen) en vervolgens aan de controls in het panel koppelen om ze de gewenste grootte en positie te geven
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;                                                            // control bevindt zich in eerste kolom
        gridBagConstraints1.gridy = 0;                                                            // control bevindt zich in eerste rij

        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;                                                            // control bevindt zich in de tweede kolom
        gridBagConstraints2.gridy = 0;                                                            // control bevindt zich in de eerste rij
        gridBagConstraints2.weightx = 0.1;                                                        // zorgt ervoor dat de controls niet 'vastgeplakt' zitten aan elkaar
        gridBagConstraints2.anchor = GridBagConstraints.WEST;                                     // control wordt links geplaatst in de cel
        gridBagConstraints2.insets = new Insets(0, 5, 0, 0);                 // padding tussen control en cel voor de linkerkant

        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.anchor = GridBagConstraints.LAST_LINE_END;                            // control bevindt zich rechts onderin de cel
        gridBagConstraints3.gridx = 2;                                                            // control bevindt zich in derde kolom
        gridBagConstraints3.gridy = 2;                                                            // control bevindt zich in derde rij
        gridBagConstraints3.weighty = 1.0;                                                        // control krijgt extra verticale ruimte
        gridBagConstraints3.insets = new Insets(0, 0, 5, 5);                 // padding tussen control en cel voor de onder- en rechterkant

        panelLinksOnder.add(minimaalTotaleBeschikbaarheid, gridBagConstraints1);                  // gemaakte label toevoegen
        panelLinksOnder.add(jtMinimaalTotaleBeschikbaarheid, gridBagConstraints2);                // gemaakte tekstveld toevoegen
        panelLinksOnder.add(bereken, gridBagConstraints3);                                        // gemaakte knop toevoegen
        panelLinksOnder.setPreferredSize(dimensie);
        panelLinksOnder.setBorder(witteRand);

        //Controls van JPanel rechts onderin
        JLabel totaleBeschikbaarheid = new JLabel("Totale beschikbaarheid ontwerp (%):");
        totaleBeschikbaarheid.setForeground(Color.white);

//        beschikbaarheidspercentage = 0.000;                                                          //  voorbeelddata, echte data volgt uit optimalisatiefunctie

//        DecimalFormat df = new DecimalFormat("0.000");
//        totaleBeschikbaarheidNetwerkTekst = df.format(beschikbaarheidspercentage);

        beschikbaarheidspercentageLabel = new JLabel("0.000");
        beschikbaarheidspercentageLabel.setForeground(Color.white);


        JLabel totaleKosten = new JLabel("Totale kosten ontwerp (€):");
        totaleKosten.setForeground(Color.white);


//        for (Ontwerpcomponent ontwerpcomponent : optimaleWaarden) {
//            totaleBedrag += Double.parseDouble(ontwerpcomponent.getKosten().replaceAll(",", "."));
//        }
//
//        DecimalFormat df2 = new DecimalFormat("0.00");
//        String totaleBedragNetwerkTekst = df2.format(totaleBedrag);

        totaleBedragLabel = new JLabel("0.00");
        totaleBedragLabel.setForeground(Color.white);

        voegToe = new JButton("Voeg componenten toe aan ontwerp");
        voegToe.addActionListener(this);
        voegToe.setForeground(Color.white);
        voegToe.setBackground(backClr1);

        //JPanel rechts onderin
        JPanel panelRechtsOnder = new JPanel();
        panelRechtsOnder.setLayout(new GridBagLayout());
        panelRechtsOnder.setBackground(backClr1);

        //GridBagConstraints aanmaken (bestaat uit kolommen en rijen) en vervolgens aan de controls in het panel koppelen om ze de gewenste grootte en positie te geven
        GridBagConstraints _1 = new GridBagConstraints();
        _1.gridx = 0;                                                                            // control bevindt zich in de eerste kolom
        _1.gridy = 0;                                                                            // control bevindt zich in de eerste rij

        GridBagConstraints _2 = new GridBagConstraints();
        _2.anchor = GridBagConstraints.WEST;                                                     // control bevindt zich links in de cel
        _2.fill = GridBagConstraints.HORIZONTAL;                                                 // control vult hele cel horizontaal gezien
        _2.gridx = 1;                                                                            // control bevindt zich in tweede kolom
        _2.gridy = 0;                                                                            // control bevindt zich in eerste rij
        _2.weightx = 0.1;                                                                        // zorgt ervoor dat de controls niet 'vastgeplakt' zitten aan elkaar
        _2.insets = new Insets(0, 10, 0, 0);                                // padding tussen control en cel voor de linkerkant

        GridBagConstraints _3 = new GridBagConstraints();
        _3.anchor = GridBagConstraints.WEST;                                                     // control bevindt zich links in de cel
        _3.gridx = 0;                                                                            // control bevindt zich in eerste kolom
        _3.gridy = 1;                                                                            // control bevindt zich in tweede rij

        GridBagConstraints _4 = new GridBagConstraints();
        _4.anchor = GridBagConstraints.WEST;                                                     // control bevindt zich links in de cel
        _4.fill = GridBagConstraints.HORIZONTAL;                                                 // control vult hele cel horizontaal gezien
        _4.gridx = 1;                                                                            // control bevindt zich in tweede kolom
        _4.gridy = 1;                                                                            // control bevindt zich in tweede rij
        _4.insets = new Insets(0, 10, 0, 0);                                // padding tussen control en cel voor de linkerkant

        GridBagConstraints _5 = new GridBagConstraints();
        _5.anchor = GridBagConstraints.LAST_LINE_END;                                            // control bevindt zich rechts onderin de cel
        _5.gridx = 2;                                                                            // control bevindt zich in de derde kolom
        _5.gridy = 2;                                                                            // control bevindt zich in de derde rij
        _5.weighty = 1.0;                                                                        // control krijgt extra verticale ruimte
        _5.insets = new Insets(0, 0, 5, 5);                                 // padding tussen control en cel voor de onder- en rechterkant

        panelRechtsOnder.setPreferredSize(dimensie);
        panelRechtsOnder.setBorder(witteRand);
        panelRechtsOnder.add(totaleBeschikbaarheid, _1);                                          // gemaakte label toevoegen
        panelRechtsOnder.add(beschikbaarheidspercentageLabel, _2);                                     // gemaakte label toevoegen
        panelRechtsOnder.add(totaleKosten, _3);                                                   // gemaakte label toevoegen
        panelRechtsOnder.add(totaleBedragLabel, _4);                                                   // gemaakte label toevoegen
        panelRechtsOnder.add(voegToe, _5);                                                        // gemaakte knop toevoegen


        //de labels, tabellen en panels toevoegen aan een panel
        //Hierdoor is het mogelijk het panel aan een scrollPane toe te voegen en het scherm scrollbaar te maken wanneer de gebruiker de grootte van het scherm verandert.
        Dimension dimensiePanelScherm = new Dimension(1500, 700);
        JPanel panelScherm = new JPanel();
        panelScherm.add(mogelijkeComponenten);
        panelScherm.add(optimaleWaardenOntwerp);
        panelScherm.add(scrollPaneTabel1);
        panelScherm.add(scrollPaneTabel2);
        panelScherm.add(panelLinksOnder);
        panelScherm.add(panelRechtsOnder);
        panelScherm.setPreferredSize(dimensiePanelScherm);
        panelScherm.setBackground(backClr1);

        //JScrollpane aanmaken
        JScrollPane scrollPane = new JScrollPane(panelScherm);                                    //panelScherm toevoegen aan scrollPane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);          //verticale scrollbar verschijnt indien nodig
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);      //horizontale scrollbar verschijnt indien nodig
        add(scrollPane);                                                                          //scrollpane toevoegen aan dit frame

        setVisible(true);
    }



    private static int LoopDB(int totaalSrvrDB, int serverNummer){
        int teller = 0;
        while(teller<maxLoop-totaalSrvrDB){
            aantalDatabases[serverNummer]= teller;
            teller++;
            if (serverNummer<maxDBSrt) {
                LoopDB(teller+totaalSrvrDB,serverNummer+1);
            }

            if(serverNummer==maxDBSrt) {
                totaalTeller++;
                System.out.println(totaalTeller + " W " + aantalWebservers[0] + "-" + aantalWebservers[1] + "-" + aantalWebservers[2] + "-" + "D " + aantalDatabases[0] + "-" + aantalDatabases[1] + "-" + aantalDatabases[2] + " ->" + BerekenBeschikbaarheid() + " - " + Berekenkosten() + " " + minKosten + resultaat);

                double berekendeBeschikbaarheid = BerekenBeschikbaarheid();
                double berekendeKosten = Berekenkosten();

                if (berekendeBeschikbaarheid > minBeschikbaarheid) {
                    if (berekendeKosten < minKosten) {
                        minKosten = berekendeKosten;
                        resultaat = "F 1 W " + aantalWebservers[0] + "-" + aantalWebservers[1] + "-" + aantalWebservers[2] + "-" + "D " + aantalDatabases[0] + "-" + aantalDatabases[1] + "-" + aantalDatabases[2];
                    }
                    return serverNummer;
                }
            }
        }
        return serverNummer;
    }

    //berekent de juiste database aantal
    private static int LoopWB(int totaalSrvrWB,int serverNummer){
        int teller = 0;
        while(teller<maxLoop-totaalSrvrWB){
            aantalWebservers[serverNummer]= teller;
            if (serverNummer<maxWBSrt) {
                LoopWB(teller+totaalSrvrWB,serverNummer+1);
            }
            if(serverNummer==maxWBSrt){
                LoopDB(0,0);
            }
            teller++;
        }
        return serverNummer;
    }

    //berekent beschikbaarheid van heel het ontwerp
    private static double BerekenBeschikbaarheid(){
        double beschikbaarheidFW = 1 - Math.pow((1 - 0.99998), 1);
        double beschikbaarheidDB = 1 - Math.pow((1 - databaseBeschikbaarheid[0]), aantalDatabases[0]) * Math.pow((1 - databaseBeschikbaarheid[1]), aantalDatabases[1]) * Math.pow((1 - databaseBeschikbaarheid[2]), aantalDatabases[2]);
        double beschikbaarheidWB = 1 - Math.pow((1 - webserverBeschikbaarheid[0]), aantalWebservers[0]) * Math.pow((1 - webserverBeschikbaarheid[1]), aantalWebservers[1]) * Math.pow((1 - webserverBeschikbaarheid[2]), aantalWebservers[2]);
        double totaleBeschikbaarheid = beschikbaarheidFW * beschikbaarheidDB * beschikbaarheidWB;

        return totaleBeschikbaarheid;
    }

    //berekentkosten van heel het ontwerp
    private static double Berekenkosten(){
        double kostenFirewalls = 4000;
        double kostenDatabases = (aantalDatabases[0] * dbKosten[0]) + (aantalDatabases[1] * dbKosten[1]) + (aantalDatabases[2] *dbKosten[2]);
        double kostenWebservers = (aantalWebservers[0] * wbKosten[0]) + (aantalWebservers[1] * wbKosten[1]) + (aantalWebservers[2] * wbKosten[2]);
        double totalekosten = kostenFirewalls + kostenDatabases + kostenWebservers;

        return totalekosten;
    }



    @Override
    public void actionPerformed(ActionEvent e){
        tabel2.isCellEditable(0,4);

        minimaalTotaleBeschikbaarheid.setText("Minimaal totaal beschikbaarheidspercentage:");     // standaard labeltekst zonder melding
        try {
            if (e.getSource() == bereken) {
                // ingevoerd percentage wordt van String naar Double omgezet
                minBeschikbaarheid = (Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText().replaceAll(",", "."))/100);
                LoopWB(0,0);
                System.out.println(totaalTeller + " combinaties onderzocht " + minKosten + " - " + resultaat);

                char[] nummers = resultaat.toCharArray();
                for(char nummer: nummers) {
                    System.out.println(nummer);
                }
                tabel2.setValueAt(nummers[14], 0,4);
                tabel2.setValueAt(nummers[6], 1,4);
                tabel2.setValueAt(nummers[16], 2,4);
                tabel2.setValueAt(nummers[8], 3,4);
                tabel2.setValueAt(nummers[18], 4,4);
                tabel2.setValueAt(nummers[10], 5,4);
                tabel2.setValueAt(nummers[2], 6,4);


                beschikbaarheidspercentageLabel.setText(jtMinimaalTotaleBeschikbaarheid.getText());
                totaleBedrag = minKosten;
                totaleBedragLabel.setText("" + minKosten);

                System.out.println("pfSense: " + nummers[2]);
                System.out.println("Webserver 1: " + nummers[6]);
                System.out.println("Webserver 2: " + nummers[8]);
                System.out.println("Webserver 3: " + nummers[10]);
                System.out.println("Database 1: " + nummers[14]);
                System.out.println("Database 2: " + nummers[16]);
                System.out.println("Database 3: " + nummers[18]);
            }

            if (e.getSource() == voegToe) {
                String naamOntwerpnetwerk = "Ontwerpnetwerk " + beschikbaarheidspercentageLabel.getText();
                beschikbaarheidspercentage = Double.parseDouble(beschikbaarheidspercentageLabel.getText());

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
                Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('"+ naamOntwerpnetwerk +"', '"+ "HAL9001DB" +"', '"+ tabel2.getValueAt(0,4) + "','" + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('"+ naamOntwerpnetwerk +"', '"+ "HAL9001W" +"', '"+ tabel2.getValueAt(1,4) + "','" + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('"+ naamOntwerpnetwerk +"', '"+ "HAL9002DB" +"', '"+ tabel2.getValueAt(2,4) + "','" + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('"+ naamOntwerpnetwerk +"', '"+ "HAL9002W" +"', '"+ tabel2.getValueAt(3,4) + "','" + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('"+ naamOntwerpnetwerk +"', '"+ "HAL9003DB" +"', '"+ tabel2.getValueAt(4,4) + "','" + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('"+ naamOntwerpnetwerk +"', '"+ "HAL9003W" +"', '"+ tabel2.getValueAt(5,4) + "','" + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('"+ naamOntwerpnetwerk +"', '"+ "pfSense" +"', '"+ tabel2.getValueAt(6,4) + "','" + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");


                Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk(naamOntwerpnetwerk + "%", totaleBedrag, beschikbaarheidspercentage);
                Groep firewallgroep1 = new Groep("firewallgroep");
                Groep webservergroep1 = new Groep("webservergroep");
                Groep databasegroep1 = new Groep("databasegroep");




                if(Integer.parseInt(String.valueOf(tabel2.getValueAt(6,4 )))   > 0) {
                    for (int i = 0; i < Integer.parseInt(String.valueOf(tabel2.getValueAt(6,4 ))); i++) {
                        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
                            if (ontwerpcomponent.getType().equals("firewall"))
                                firewallgroep1.componenten.add(ontwerpcomponent);
                        }
                    }
                }

                if(Integer.parseInt(String.valueOf(tabel2.getValueAt(1,4)))  > 0) {
                    for (int j = 0; j < Integer.parseInt(String.valueOf(tabel2.getValueAt(1,4))); j++) {
                        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
                            if (ontwerpcomponent.getNaam().equals("HAL9001W")) {
                                webservergroep1.componenten.add(ontwerpcomponent);
                            }
                        }
                    }
                }

                if(Integer.parseInt(String.valueOf(tabel2.getValueAt(3,4))) > 0) {
                    for (int j = 0; j < Integer.parseInt(String.valueOf(tabel2.getValueAt(3,4))); j++) {
                        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
                            if (ontwerpcomponent.getNaam().equals("HAL9002W")) {
                                webservergroep1.componenten.add(ontwerpcomponent);
                            }
                        }
                    }
                }

                if(Integer.parseInt(String.valueOf(tabel2.getValueAt(5,4)))  > 0) {
                    for (int j = 0; j < Integer.parseInt(String.valueOf(tabel2.getValueAt(5,4))); j++) {
                        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
                            if (ontwerpcomponent.getNaam().equals("HAL9003W")) {
                                webservergroep1.componenten.add(ontwerpcomponent);
                            }
                        }
                    }
                }

                if(Integer.parseInt(String.valueOf(tabel2.getValueAt(0,4)))> 0) {
                    for (int j = 0; j < Integer.parseInt(String.valueOf(tabel2.getValueAt(0,4))); j++) {
                        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
                            if (ontwerpcomponent.getNaam().equals("HAL9001DB")) {
                                databasegroep1.componenten.add(ontwerpcomponent);
                            }
                        }
                    }
                }
                if(Integer.parseInt(String.valueOf(tabel2.getValueAt(2,4))) > 0) {
                    for (int j = 0; j < Integer.parseInt(String.valueOf(tabel2.getValueAt(2,4))); j++) {
                        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
                            if (ontwerpcomponent.getNaam().equals("HAL9002DB")) {
                                databasegroep1.componenten.add(ontwerpcomponent);
                            }
                        }
                    }
                }

                if(Integer.parseInt(String.valueOf(tabel2.getValueAt(4,4))) > 0) {
                    for (int j = 0; j < Integer.parseInt(String.valueOf(tabel2.getValueAt(4,4))); j++) {
                        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
                            if (ontwerpcomponent.getNaam().equals("HAL9003DB")) {
                                databasegroep1.componenten.add(ontwerpcomponent);
                            }
                        }
                    }
                }

                ontwerpnetwerk.groepen.add(firewallgroep1);
                ontwerpnetwerk.groepen.add(webservergroep1);
                ontwerpnetwerk.groepen.add(databasegroep1);

                OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpnetwerk);

                Ontwerpnetwerk.uitDatabase();


//                ResultSet rs2 = statement.executeQuery("SELECT * FROM ontwerpnetwerk");
//
//                Groep firewallgroep = new Groep("firewallgroep");
//                Groep webservergroep = new Groep("webservergroep");
//                Groep databasegroep = new Groep("databasegroep");
//
//
////                Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk();
////                if (!Ontwerpnetwerk.getOntwerpNetwerken().contains(ontwerpnetwerk)) {
////                                ontwerpnetwerk = new Ontwerpnetwerk(rs2.getString("NaamNetwerk") + "%", rs2.getDouble("Kosten"),
////                                        rs2.getDouble("Beschikbaarheid"));
////                            }
//
//                String naamNetwerk = null;
//                Double kosten = 0.00;
//                Double beschikbaarheid = 0.000;
//
//                while (rs2.next()) {
//                    naamNetwerk = rs2.getString("NaamNetwerk") + "%";
//                    kosten = rs2.getDouble("Kosten");
//                    beschikbaarheid = rs2.getDouble("Beschikbaarheid");
//
//                    for (Ontwerpcomponent ontwerpcomponent : Ontwerpcomponent.getOntwerpcomponenten()) {
//                        if (ontwerpcomponent.getNaam().equals(rs2.getString("NaamComponent"))) {
//                            for (int i = 0; i < rs2.getInt("AantalGebruikt"); i++) {
//                                if (ontwerpcomponent.getType().equals("firewall")) {
//                                    firewallgroep.componenten.add(ontwerpcomponent);
//
//                                }
//
//                                if (ontwerpcomponent.getType().equals("webserver")) {
//                                    webservergroep.componenten.add(ontwerpcomponent);
//
//                                }
//
//                                if (ontwerpcomponent.getType().equals("database")) {
//                                    databasegroep.componenten.add(ontwerpcomponent);
//                                }
//                            }
//                        }
//                    }
//                    }
//
//                Ontwerpnetwerk ontwerpnetwerk2 = new Ontwerpnetwerk(naamNetwerk, kosten, beschikbaarheid);
//                ontwerpnetwerk2.groepen.add(firewallgroep);
//                ontwerpnetwerk2.groepen.add(webservergroep);
//                ontwerpnetwerk2.groepen.add(databasegroep);
//
////                if(Ontwerpnetwerk.getOntwerpNetwerken().size() > 0) {
////                                        for(Ontwerpnetwerk ontwerpnetwerk: Ontwerpnetwerk.getOntwerpNetwerken()) {
////                                            if (ontwerpnetwerk.getNaam().equals(ontwerpnetwerk2.getNaam())) {
////                                                Ontwerpnetwerk.getOntwerpNetwerken().remove(ontwerpnetwerk2);
////                                            }
////                                        }}
//
//
//                connection.close();
//                statement.close();

                dispose();

//                rs.next(); //Hierdoor gaat de Resultset naar de volgende regel. Als dit er niet in staat dan zal er geen resultaat uit komen.
//                System.out.println(tabel2.getValueAt(0, 4));
//
//                String naamOntwerpnetwerk = "Ontwerpnetwerk " + beschikbaarheidspercentageLabel.getText();
//
//                ResultSet rs = statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('"+ naamOntwerpnetwerk +"', '"+ "HAL9001DB" +"', '"+ aantalHal9001DB + "','" + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");


//                Groep databasegroep = new Groep("databases", 90);
//                // beschikbaarheidspercentage moet bekend worden naar aanleiding van de databases
//                // die uit optimalisatiefunctie komen, nu voorbeelddata gebruikt
//                Groep webservergroep = new Groep("webservers", 94);
//                // beschikbaarheidspercentage moet bekend worden naar aanleiding van de webservers
//                // die uit optimalisatiefunctie komen, nu voorbeelddata gebruikt
//                Groep firewall = new Groep("firewall", 99.998);
//
//                for (Ontwerpcomponent ontwerpcomponent : optimaleWaarden) {
//                    if (ontwerpcomponent.getType().equals("database")) {
//                        databasegroep.componenten.add(ontwerpcomponent);
//                    }
//                    if (ontwerpcomponent.getType().equals("webserver")) {
//                        webservergroep.componenten.add(ontwerpcomponent);
//                    }
//                    if (ontwerpcomponent.getType().equals("firewall")) {
//                        firewall.componenten.add(ontwerpcomponent);
//                    }
//                }
//
//                //als het ontwerpframe van waaruit je een nieuw ontwerp wil aanmaken
//                // nog geen ontwerpnetwerk had, wordt deze gesloten en wordt er een nieuw ontwerpframe met het
//                //nieuwe netwerk aangemaakt (wanneer er niet al een ontwerpnetwerk met dezelfde beschikbaarheid bestaat)
//                if(ontwerpFrame.getOntwerpnetwerk() == null) {
//
//
//                    // bepalen of er al een ontwerp met dezelfde beschikbaarheid is
//                    Ontwerpnetwerk ontwerpNetwerkMetDezelfdeBeschikbaarheid = null;
//                    for(Ontwerpnetwerk ontwerpnetwerk1: Ontwerpnetwerk.getOntwerpNetwerken()) {
//                        if (ontwerpnetwerk1.getBeschikbaarheidspercentage().equals(totaleBeschikbaarheidNetwerkTekst)) {
//                            ontwerpNetwerkMetDezelfdeBeschikbaarheid = ontwerpnetwerk1;
//                        }
//                    }
//
//                    // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt er een nieuw ontwerpnetwerk gemaakt
//                    if (ontwerpNetwerkMetDezelfdeBeschikbaarheid == null) {
//                        //nieuw ontwerpnetwerk aanmaken
//                        // kosten en beschikbaarheidspercentage zijn bekend naar aanleiding van optimalisatiefunctie, nu voorbeelddata gebruikt
//
//                        String naamOntwerpnetwerk = "Ontwerpnetwerk " + totaleBeschikbaarheidNetwerkTekst;
//                        Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk(naamOntwerpnetwerk + "%", totaleBedrag, beschikbaarheidspercentage);
//                        ontwerpnetwerk.groepen.add(databasegroep);
//                        ontwerpnetwerk.groepen.add(webservergroep);
//                        ontwerpnetwerk.groepen.add(firewall);
//
//                        //ontwerpnetwerk wordt in database opgeslagen
//                        for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten) {
//                            int k = 0;
//                            for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
//                                if (ontwerpcomponent2.equals(ontwerpcomponent)) {
//                                    k++;
//                                }
//                            }
//                            if(k != 0) {
//
////                                   // Testcode of de juiste data in de database zal worden opgeslagen
////                                   System.out.println(naamOntwerpnetwerk + " "
////                                          + ontwerpcomponent.getNaam() + " " + k + " " + beschikbaarheidspercentage.getText() +
////                                         " " + totaleBedrag.getText());
//
//                                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");
//                                Statement statement = connection.createStatement();
//                                int gelukt = statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " +
//                                        "('"+ naamOntwerpnetwerk +"', '"+ ontwerpcomponent.getNaam()+"', '"+ k + "','"
//                                        + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
//                                statement.close();
//                                connection.close();
//                            }
//                        }
//
//                        ontwerpFrame.dispose();
//                        OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpnetwerk);
//                    }
//
//                    // wanneer er wel een ontwerp met dezelfde beschikbaarheid is, wordt er geen nieuw ontwerpnetwerk aangemaakt,
//                    // maar wordt het al bestaande netwerk geopend
//                    else {
//                        ontwerpFrame.dispose();
//                        OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpNetwerkMetDezelfdeBeschikbaarheid);
//                    }
//                }
//
//                //als het ontwerpframe van waaruit je een nieuw ontwerp wil aanmaken al een ontwerpnetwerk heeft, wordt er een nieuw ontwerpframe met het nieuwe
//                //netwerk aangemaakt zonder het ontwerpframe eerst te sluiten (wanneer er niet al een ontwerpnetwerk met dezelfde beschikbaarheid bestaat)
//                if(ontwerpFrame.getOntwerpnetwerk() != null && !ontwerpFrame.isGedruktOpOptimalisatieknop()) {
//
//                    // bepalen of er al een ontwerp met dezelfde beschikbaarheid is
//                    Ontwerpnetwerk ontwerpNetwerkMetZelfdeBeschikbaarheid = null;
//                    for(Ontwerpnetwerk ontwerpnetwerk1: Ontwerpnetwerk.getOntwerpNetwerken()) {
//                        if (ontwerpnetwerk1.getBeschikbaarheidspercentage().equals(totaleBeschikbaarheidNetwerkTekst)) {
//                            ontwerpNetwerkMetZelfdeBeschikbaarheid = ontwerpnetwerk1;
//                        }
//                    }
//
//                    // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt er een nieuw ontwerpnetwerk gemaakt
//                    if (ontwerpNetwerkMetZelfdeBeschikbaarheid == null) {
//                        //nieuw ontwerpnetwerk aanmaken
//                        // kosten en beschikbaarheidspercentage zijn bekend naar aanleiding van optimalisatiefunctie, nu voorbeelddata gebruikt
//                        //ontwerpnetwerk moet in database opgeslagen worden
//
//                        String naamOntwerpnetwerk = "Ontwerpnetwerk " + totaleBeschikbaarheidNetwerkTekst;
//                        Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk(naamOntwerpnetwerk + "%", totaleBedrag, beschikbaarheidspercentage);
//                        ontwerpnetwerk.groepen.add(databasegroep);
//                        ontwerpnetwerk.groepen.add(webservergroep);
//                        ontwerpnetwerk.groepen.add(firewall);
//                        //   ontwerpnetwerk.setCorrecteKostenEnBeschikbaarheid();
//
//                        for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten) {
//                            int k = 0;
//                            for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
//                                if (ontwerpcomponent2.equals(ontwerpcomponent)) {
//                                    k++;
//                                }
//                            }
//                            if(k != 0) {
//
//                                // Testcode of de juiste data in de database zal worden opgeslagen
////                          System.out.println(naamOntwerpnetwerk + " "
////                          + ontwerpcomponent.getNaam() + " " + k + " " + beschikbaarheidspercentage.getText() +
////                          " " + totaleBedrag.getText());
//
//                                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");
//                                Statement statement = connection.createStatement();
//                                int gelukt = statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " +
//                                        "('"+ naamOntwerpnetwerk +"', '"+ ontwerpcomponent.getNaam()+"', '"+ k + "','"
//                                        + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
//
//                                statement.close();
//                                connection.close();
//                            }
//                        }
//
//                        OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpnetwerk);
//                    }
//                    // wanneer er wel een ontwerp met dezelfde beschikbaarheid is, wordt er geen nieuw ontwerpnetwerk aangemaakt,
//                    // maar wordt het al bestaande netwerk geopend
//                    else {
//                        OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpNetwerkMetZelfdeBeschikbaarheid);
//                    }
//                }
//
//                //wanneer je een al bestaand ontwerpnetwerk wilt optimaliseren, wordt het bestaande ontwerpnetwerk geüpdatet
//                //(wanneer er niet al een ontwerpnetwerk met dezelfde beschikbaarheid bestaat)
//                if(ontwerpFrame.getOntwerpnetwerk() != null && ontwerpFrame.isGedruktOpOptimalisatieknop()) {
//
//                    // bepalen of er al een ontwerp met dezelfde beschikbaarheid is
//                    Ontwerpnetwerk ontwerpNetwerkMetZelfdeBeschikbaarheid = null;
//                    for(Ontwerpnetwerk ontwerpnetwerk1: Ontwerpnetwerk.getOntwerpNetwerken()) {
//                        if (ontwerpnetwerk1.getBeschikbaarheidspercentage().equals(totaleBeschikbaarheidNetwerkTekst)) {
//                            ontwerpNetwerkMetZelfdeBeschikbaarheid = ontwerpnetwerk1;
//                        }
//                    }
//
//                    // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt het ontwerpnetwerk geüpdatet
//                    // ontwerpnetwerk moet in database geüpdatet worden
//                    if (ontwerpNetwerkMetZelfdeBeschikbaarheid == null) {
//                        String naamOntwerpnetwerk = "Ontwerpnetwerk " + totaleBeschikbaarheidNetwerkTekst;
//                        ontwerpFrame.getOntwerpnetwerk().setNaam(naamOntwerpnetwerk + "%");
//                        ontwerpFrame.getOntwerpnetwerk().setKosten(totaleBedrag);
//                        ontwerpFrame.getOntwerpnetwerk().setBeschikbaarheidspercentage(beschikbaarheidspercentage);
//                        ontwerpFrame.getOntwerpnetwerk().groepen.set(0, databasegroep);
//                        ontwerpFrame.getOntwerpnetwerk().groepen.set(1, webservergroep);
//                        ontwerpFrame.getOntwerpnetwerk().groepen.set(2, firewall);
//
//                        for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten) {
//                            int k = 0;
//                            for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
//                                if (ontwerpcomponent2.equals(ontwerpcomponent)) {
//                                    k++;
//                                }
//                            }
//                            if(k != 0) {
//
////                                // Testcode of de juiste data in de database zal worden geüpdatet
////                                System.out.println(naamOntwerpnetwerk + " "
////                                       + ontwerpcomponent.getNaam() + " " + k + " " + beschikbaarheidspercentage.getText() +
////                                      " " + totaleBedrag.getText() + " vervangt ontwerpnetwerk met beschikbaarheidspercentage van: "
////                                        + ontwerpFrame.getOntwerpnetwerk().getBeschikbaarheidspercentage());
////
//                                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");
//                                PreparedStatement statement = connection.prepareStatement("UPDATE Ontwerpnetwerk SET NaamNetwerk = '"+naamOntwerpnetwerk+" ',' NaamComponent = "+ontwerpcomponent.getNaam()
//                                        +" ',' AantalGebruikt = "+k+" ',' Beschikbaarheid = "+beschikbaarheidspercentage
//                                        +" ',' Kosten = "+ totaleBedrag+ "' WHERE Beschikbaarheid = ? ");
//
//                                statement.setString(1, ontwerpFrame.getOntwerpnetwerk().getBeschikbaarheidspercentage());
//
//                                int gelukt = statement.executeUpdate();
//
//                                statement.close();
//                                connection.close();
//
//                            }
//                        }
//
//
//
//
//                        ontwerpFrame.setGedruktOpOptimalisatieknop(false);
//                        Ontwerpnetwerk geoptimaliseerdOntwerpnetwerk = ontwerpFrame.getOntwerpnetwerk();
//                        ontwerpFrame.dispose();
//                        OntwerpFrame ontwerpFrame = new OntwerpFrame(geoptimaliseerdOntwerpnetwerk);
//
//                    }
//                    else {
//                        // wanneer er wel eerder een ontwerp met dezelfde beschikbaarheid is, wordt het ontwerpnetwerk niet geüpdatet,
//                        // maar wordt het al bestaande netwerk geopend
//                        OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpNetwerkMetZelfdeBeschikbaarheid);
//                    }
//
//                }
//            }
//
//                dispose();
            }
        } catch(NumberFormatException nfe){
            minimaalTotaleBeschikbaarheid.setText("<html>Minimaal totaal beschikbaarheidspercentage: <br> <font color = 'red'> Voer een percentage in <font/><html/>");
            // melding dat er een percentage ingevuld moet worden
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }
}