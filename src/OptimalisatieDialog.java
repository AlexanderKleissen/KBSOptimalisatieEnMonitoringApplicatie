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
    ArrayList<Ontwerpcomponent> serversHuidigeOplossing = new ArrayList<>();
    ArrayList<Ontwerpcomponent> serversBesteOplossing = new ArrayList<>();
    private ArrayList<ArrayList<Ontwerpcomponent>> netwerkHuidigeOplossing = new ArrayList<>();
    private ArrayList<ArrayList<Ontwerpcomponent>> netwerkBesteOplossing = new ArrayList<>();
    private ArrayList<ArrayList<Ontwerpcomponent>> netwerkEersteOplossing = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Ontwerpcomponent>>> netwerkOplossingOpslag = new ArrayList<>();
    private ArrayList<Ontwerpcomponent> besteOplossing = new ArrayList<>();
    private double beschikbaarheid = 0;
    private double kosten = 0;
    private double laagsteKosten = 10000000;
    private double laagsteKostenNetwerk = 100000000;
    private int diepte = 100;//Het aantal servers van de opgeslagen oplossing

    private static int maxLoop = 8;
    private static int[] wbAantal = {0,0,0};

    private static double[] wbBeschikbaarheid = {0.9,0.8,0.95};
    private static double[] wbKosten = {3200,2200,5100};
    private static int maxWBSrt = 2;
    private static int[] dbAantal = {0,0,0};

    private static double[] dbBeschikbaarheid = {0.9,0.95,0.98};
    private static double[] dbKosten = {5100,7700,12200};
    private static int maxDBSrt = 2;
    private static double minBeschikbaarheid = 0;
    private static double minKosten = Double.MAX_VALUE;
    private static int totaalTeller = 0;
    private static String serverset;

    private int aantalHal9001DB;
    private int aantalHal9002DB;
    private int aantalHal9003DB;

    private int aantalHal9001W;
    private int aantalHal9002W;
    private int aantalHal9003W;


    public OptimalisatieDialog(OntwerpFrame frame) throws SQLException {
        super(frame, false);
        this.ontwerpFrame = frame;
        setSize(1500, 1000);
        setLayout(new BorderLayout());
        UIManager.getLookAndFeelDefaults().put("TextField.caretForeground", Color.white);
        setTitle("Monitoringapplicatie (optimaliseren)");

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

        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
            if (ontwerpcomponent.getNaam().equals("HAL9001DB")) {
                for (int i = 0; i < 3; i++) {
                    optimaleWaarden.add(ontwerpcomponent);
                }
            }
            if (ontwerpcomponent.getNaam().equals("HAL9002DB")) {
                optimaleWaarden.add(ontwerpcomponent);
            }

            if (ontwerpcomponent.getNaam().equals("HAL9003DB")) {
                optimaleWaarden.add(ontwerpcomponent);
            }

            if (ontwerpcomponent.getNaam().equals("HAL9001W")) {
                optimaleWaarden.add(ontwerpcomponent);
            }

            if (ontwerpcomponent.getNaam().equals("HAL9002W")) {
                for (int k = 0; k < 4; k++) {
                    optimaleWaarden.add(ontwerpcomponent);
                }
            }

            if (ontwerpcomponent.getNaam().equals("HAL9003W")) {
                optimaleWaarden.add(ontwerpcomponent);
            }

            if (ontwerpcomponent.getNaam().equals("pfSense")) {
                optimaleWaarden.add(ontwerpcomponent);
            }
        }


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
        Object[][] data2 = new Object[ontwerpcomponenten.size()][5];

        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
            int index = ontwerpcomponenten.indexOf(ontwerpcomponent);
            int k = 0;
            for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
                if (ontwerpcomponent2.equals(ontwerpcomponent)) {
                    k++;
                    data2[index][0] = ontwerpcomponent.getNaam();
                    data2[index][1] = ontwerpcomponent.getType();
                    data2[index][2] = ontwerpcomponent.getKosten();
                    data2[index][3] = ontwerpcomponent.getBeschikbaarheidspercentage();
                    data2[index][4] = 0;
                }
            }
        }


        JTable tabel2 = new JTable(data2, kolomnamen2);
        tabel2.setEnabled(false);
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

        beschikbaarheidspercentage = 99.990;                                                          //  voorbeelddata, echte data volgt uit optimalisatiefunctie

        DecimalFormat df = new DecimalFormat("0.000");
        totaleBeschikbaarheidNetwerkTekst = df.format(beschikbaarheidspercentage);

        beschikbaarheidspercentageLabel = new JLabel(totaleBeschikbaarheidNetwerkTekst);
        beschikbaarheidspercentageLabel.setForeground(Color.white);


        JLabel totaleKosten = new JLabel("Totale kosten ontwerp (€):");
        totaleKosten.setForeground(Color.white);


        for (Ontwerpcomponent ontwerpcomponent : optimaleWaarden) {
            totaleBedrag += Double.parseDouble(ontwerpcomponent.getKosten().replaceAll(",", "."));
        }

        DecimalFormat df2 = new DecimalFormat("0.00");
        String totaleBedragNetwerkTekst = df2.format(totaleBedrag);

        totaleBedragLabel = new JLabel(totaleBedragNetwerkTekst);
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

    /* Het backtracking algoritme */

    private double berekenBeschikbaarheid(ArrayList<Ontwerpcomponent> firewalls, ArrayList<Ontwerpcomponent> webservers, ArrayList<Ontwerpcomponent> dbservers) {
        double beschikbaarheidTotaal = 1;
        double beschikbaarheidFirewalls = 1;
        double beschikbaarheidWebservers = 1;
        double beschikbaarheidDbservers = 1;
        for (Ontwerpcomponent firewall : firewalls) {
            beschikbaarheidFirewalls = beschikbaarheidFirewalls * (1 - Double.parseDouble(firewall.getBeschikbaarheidspercentage().replaceAll(",", ".")) / 100);
        }
        beschikbaarheidFirewalls = 1 - beschikbaarheidFirewalls;
        for (Ontwerpcomponent webserver : webservers) {
            beschikbaarheidWebservers = beschikbaarheidWebservers * (1 - Double.parseDouble(webserver.getBeschikbaarheidspercentage().replaceAll(",", ".")) / 100);
        }
        beschikbaarheidWebservers = 1 - beschikbaarheidWebservers;
        for (Ontwerpcomponent dbserver : dbservers) {
            beschikbaarheidDbservers = beschikbaarheidDbservers * (1 - Double.parseDouble(dbserver.getBeschikbaarheidspercentage().replaceAll(",", ".")) / 100);
        }
        beschikbaarheidDbservers = 1 - beschikbaarheidDbservers;
        beschikbaarheidTotaal = beschikbaarheidFirewalls * beschikbaarheidWebservers * beschikbaarheidDbservers;
        return beschikbaarheidTotaal * 100;
    }

    private double berekenBeschikbaarheid(ArrayList<Ontwerpcomponent> componenten) {
        double beschikbaarheid = 1;
        for (Ontwerpcomponent component : componenten) {
            beschikbaarheid = beschikbaarheid * (1 - Double.parseDouble(component.getBeschikbaarheidspercentage().replaceAll(",", ".")) / 100);
        }
        beschikbaarheid = 1 - beschikbaarheid;
        return beschikbaarheid * 100;
    }
//
//    /* De uitvoering van de oplossing: Alle rijen worden per kolom gecontroleerd met behulp van backtracking */
//    private boolean uitvoering(ArrayList<Ontwerpcomponent> componenten, double minBeschikbaarheidspercentage) {
//        /* Probeer alle rijen in de gegeven kolom */
//        for (int i = 0; i < 3; i++) {
//            /* De server wordt toegevoegd */
//            serversHuidigeOplossing.add(serversHuidigeOplossing.size(), componenten.get(i));
//            beschikbaarheid=berekenBeschikbaarheid(serversHuidigeOplossing);
//            this.kosten+=Double.parseDouble(componenten.get(i).getKosten());
//
//            /* Controle of het beschikbaarheidspercentage al hoog genoeg is */
//            if (beschikbaarheid>=minBeschikbaarheidspercentage && kosten<laagsteKosten) {
//                laagsteKosten=kosten;
//                kosten=0;
//                beschikbaarheid=0;
//                diepte = serversHuidigeOplossing.size();
//                for(Ontwerpcomponent component: serversHuidigeOplossing){
//                    serversBesteOplossing.add(component);
//                }
//                serversHuidigeOplossing.clear();
//                if(uitvoering(componenten, minBeschikbaarheidspercentage)){
//                    return true;
//                }
//            }
//
//            //Als de kosten te hoog zijn, wordt een ander component geprobeert
//            if(kosten<laagsteKosten){
//                /* Voer deze methode uit voor de volgende toevoeging */
//                if(uitvoering(componenten, minBeschikbaarheidspercentage)){
//                    return true;
//                }
//            }
//
//            /* Er komt geen oplossing uit deze positie, dus de server wordt verwijderd */
//            if(serversHuidigeOplossing.size()>0){
//                serversHuidigeOplossing.remove(serversHuidigeOplossing.size()-1);
//            }
//            //Als alle drie de servers niet tot een oplossing leiden, wordt er een stap teruggedaan en daar een andere server geprobeerd.
//            //Ook als de oplossing niet goedkoper kan worden dan de huidige laagste kosten, wordt een stap teruggedaan
//            if(i==2 || serversHuidigeOplossing.size()>=diepte-1){
//                if(serversHuidigeOplossing.size()==0){//Er zijn geen nieuwe oplossingen meer
//                    laagsteKosten=10000000;
//                    kosten =0;
//                    diepte = serversHuidigeOplossing.size();
//                    return true;
//                }
//                int teller =0;
//                for(Ontwerpcomponent component: componenten){
//                    if(serversHuidigeOplossing.get(serversHuidigeOplossing.size()-1).getNaam().equals(component.getNaam())){
//                        i=teller+1;
//                        teller++;
//                    }
//                }
//                serversHuidigeOplossing.remove(serversHuidigeOplossing.size()-1);
//            }
//        }
//
//        //Als er geen oplossingen te vinden zijn
//        return false;
//    }
//
//    //Zelfde methode, maar dan om de op één na beste oplossing enz. te zoeken
//    private boolean uitvoering2(ArrayList<Ontwerpcomponent> componenten, double minBeschikbaarheidspercentage, double kostenBesteOplossing) {
//
//        /* Probeer alle server opties */
//        for (int i = 0; i < 3; i++) {
//            /* De server wordt toegevoegd */
//            serversHuidigeOplossing.add(serversHuidigeOplossing.size(), componenten.get(i));
//            beschikbaarheid=berekenBeschikbaarheid(serversHuidigeOplossing);
//            kosten+=Double.parseDouble(componenten.get(i).getKosten());
//
//            /* Controle of het beschikbaarheidspercentage al hoog genoeg is */
//            if (beschikbaarheid>=minBeschikbaarheidspercentage && kosten<laagsteKosten && kosten>kostenBesteOplossing) {
//                laagsteKosten=kosten;
//                kosten=0;
//                beschikbaarheid=0;
//                for(Ontwerpcomponent component: serversHuidigeOplossing){
//                    serversBesteOplossing.add(component);
//                }
//                serversHuidigeOplossing.clear();
//                if(uitvoering2(componenten, minBeschikbaarheidspercentage, kostenBesteOplossing)){
//                    return true;
//                }
//            }
//
//            //Als de kosten te hoog zijn, wordt een ander component geprobeert
//            if(kosten<laagsteKosten && kosten != kostenBesteOplossing){
//                /* Voer deze methode uit voor de volgende toevoeging */
//                if(uitvoering2(componenten, minBeschikbaarheidspercentage, kostenBesteOplossing)){
//                    return true;
//                }
//            }
//
//            /* Er komt geen oplossing uit deze positie, dus de server wordt verwijderd */
//            if(serversHuidigeOplossing.size()>0){
//                serversHuidigeOplossing.remove(serversHuidigeOplossing.size()-1);
//            }
//
//            //Als alle drie de servers niet tot een oplossing leiden, wordt er een stap teruggedaan en daar een andere server geprobeerd.
//            if(i==2){
//                if(serversHuidigeOplossing.size()==0){//Er zijn geen nieuwe oplossingen meer
//                    laagsteKosten=10000000;
//                    kosten =0;
//                    return true;
//                }
//                int teller =0;
//                for(Ontwerpcomponent component: componenten){
//                    if(serversHuidigeOplossing.get(serversHuidigeOplossing.size()-1).getNaam().equals(component.getNaam())){
//                        i=teller+1;
//                        teller++;
//                    }
//                }
//                serversHuidigeOplossing.remove(serversHuidigeOplossing.size()-1);
//            }
//        }
//
//        //Als er geen oplossingen te vinden zijn
//        return false;
//    }
//
//    private boolean uitvoeringHeleNetwerk(double minBeschikbaarheidspercentage){
//        double vorigeKosten;
//        /* Een andere oplossing proberen */
//        for (int i = 0; i < 2; i++) {
//            /* De beschikbaarheid en kosten van een groep worden berekend */
//            if (i == 0) {
//                //Webservers
//                if(netwerkHuidigeOplossing.get(0).equals(netwerkEersteOplossing.get(0))){
//                    uitvoering(webservers, minBeschikbaarheidspercentage);
//                    vorigeKosten=0;
//                    for(Ontwerpcomponent component: serversBesteOplossing){
//                        vorigeKosten+=Double.parseDouble(component.getKosten());
//                    }
//                }else{
//                    vorigeKosten=0;
//                    for(Ontwerpcomponent component: serversBesteOplossing){
//                        vorigeKosten+=Double.parseDouble(component.getKosten());
//                    }
//                    serversBesteOplossing.clear();
//                    uitvoering2(webservers, minBeschikbaarheidspercentage, vorigeKosten);
//                    vorigeKosten=0;
//                    for(Ontwerpcomponent component: serversBesteOplossing){
//                        vorigeKosten+=Double.parseDouble(component.getKosten());
//                    }
//                }
//                serversBesteOplossing.clear();
//                uitvoering2(webservers, minBeschikbaarheidspercentage, vorigeKosten);
//            }else{//dbservers
//                if(netwerkHuidigeOplossing.get(1).equals(netwerkEersteOplossing.get(1))){
//                    uitvoering(dbservers, minBeschikbaarheidspercentage);
//                    vorigeKosten=0;
//                    for(Ontwerpcomponent component: serversBesteOplossing){
//                        vorigeKosten+=Double.parseDouble(component.getKosten());
//                    }
//                }else{
//                    vorigeKosten=0;
//                    for(Ontwerpcomponent component: serversBesteOplossing){
//                        vorigeKosten+=Double.parseDouble(component.getKosten());
//                    }
//                    serversBesteOplossing.clear();
//                    uitvoering2(dbservers, minBeschikbaarheidspercentage, vorigeKosten);
//                    vorigeKosten=0;
//                    for(Ontwerpcomponent component: serversBesteOplossing){
//                        vorigeKosten+=Double.parseDouble(component.getKosten());
//                    }
//                }
//                serversBesteOplossing.clear();
//                uitvoering2(dbservers, minBeschikbaarheidspercentage, vorigeKosten);
//            }
//
//            //de vorige oplossing wordt opgeslagen om later naar terug te kunnen gaan
//            netwerkOplossingOpslag.add(new ArrayList<>());
//            for(ArrayList<Ontwerpcomponent> component: netwerkHuidigeOplossing){
//                netwerkOplossingOpslag.get(netwerkOplossingOpslag.size()-1).add(component);
//            }
//            //de huidige oplossing wordt aangepast door de zojuist berekende nieuwe waarden in te vullen
//            netwerkHuidigeOplossing.get(i).clear();
//            for(Ontwerpcomponent component: serversBesteOplossing){
//                netwerkHuidigeOplossing.get(i).add(component);
//            }
//
//            //berekenen beschikbaarheid
//            beschikbaarheid=berekenBeschikbaarheid(netwerkHuidigeOplossing.get(2), netwerkHuidigeOplossing.get(0), netwerkHuidigeOplossing.get(1));
//            for(ArrayList<Ontwerpcomponent> groep: netwerkHuidigeOplossing){
//                for(Ontwerpcomponent component: groep){
//                    kosten+= Double.parseDouble(component.getKosten());
//                }
//            }
//
//            /* Controle of het beschikbaarheidspercentage al hoog genoeg is */
//            if (beschikbaarheid>=minBeschikbaarheidspercentage && kosten<laagsteKostenNetwerk) {
//                laagsteKostenNetwerk=kosten;
//                kosten=0;
//                beschikbaarheid=0;
//                netwerkBesteOplossing.clear();
//                for(ArrayList<Ontwerpcomponent> groep: netwerkHuidigeOplossing){
//                    netwerkBesteOplossing.add(groep);
//                }
//                netwerkHuidigeOplossing=netwerkEersteOplossing;
//                if(uitvoeringHeleNetwerk(minBeschikbaarheidspercentage)){
//                    return true;
//                }
//            }
//
//            //Als de kosten te hoog zijn, wordt een andere oplossing geprobeert
//            if(kosten<laagsteKostenNetwerk){
//                /* Voer deze methode uit voor de volgende toevoeging */
//                if(uitvoeringHeleNetwerk(minBeschikbaarheidspercentage)){
//                    return true;
//                }
//            }
//
//            /* Er komt geen oplossing uit deze positie, dus de oplossing wordt verwijderd */
//            netwerkHuidigeOplossing = netwerkOplossingOpslag.get(netwerkOplossingOpslag.size() - 1);
//            netwerkOplossingOpslag.remove(netwerkOplossingOpslag.size() - 1);
//
//            //Als er geen oplossing uit komt, wordt een stap terug gedaan
//            if(i==1){
//                if(netwerkHuidigeOplossing.equals(netwerkEersteOplossing)){//Er zijn geen nieuwe oplossingen meer //misschien pas de tweede keer dat hij daar is???
//                    laagsteKostenNetwerk=10000000;
//                    kosten =0;
//                    return true;
//                }
//                netwerkHuidigeOplossing=netwerkOplossingOpslag.get(netwerkOplossingOpslag.size()-1);
//            }
//        }
//
//        //Als er geen oplossingen te vinden zijn
//        return false;
//    }
//
//    private void optimaliseer(double minBeschikbaarheidspercentage){
//        double beschikbaarheidFirewalls = 0;
//        ArrayList<Ontwerpcomponent> firewallsOplossing = new ArrayList<>();
//        ArrayList<Ontwerpcomponent> webserversBesteOplossing = new ArrayList<>();
//        ArrayList<Ontwerpcomponent> dbserversBesteOplossing = new ArrayList<>();
//
//        //firewalls toevoegen
//        while(beschikbaarheidFirewalls<minBeschikbaarheidspercentage){
//            firewallsOplossing.add(ontwerpcomponenten.get(0));//een nieuw component wordt toegevoegd
//            beschikbaarheidFirewalls = berekenBeschikbaarheid(firewallsOplossing);
//        }
//        //webservers toevoegen
//        if (uitvoering(webservers, minBeschikbaarheidspercentage) == false) {
//            //Er zijn geen oplossingen
//        }else{
//            for(Ontwerpcomponent component: serversBesteOplossing){
//                webserversBesteOplossing.add(component);
//            }
//            serversBesteOplossing.clear();
//            laagsteKosten = 10000000;
//        }
//
//        //dbservers toevoegen
//        if (uitvoering(dbservers, minBeschikbaarheidspercentage) == false) {
//            //Er zijn geen oplossingen
//        }else{
//            for(Ontwerpcomponent component: serversBesteOplossing){
//                dbserversBesteOplossing.add(component);
//            }
//            serversBesteOplossing.clear();
//            laagsteKosten = 10000000;
//        }
//
//        //hele netwerk
//        if(berekenBeschikbaarheid(firewallsOplossing, webserversBesteOplossing, dbserversBesteOplossing)>=minBeschikbaarheidspercentage){
//            for(Ontwerpcomponent firewall: firewallsOplossing){
//                besteOplossing.add(firewall);
//            }
//            for(Ontwerpcomponent webserver: webserversBesteOplossing){
//                besteOplossing.add(webserver);
//            }
//            for(Ontwerpcomponent dbserver: dbserversBesteOplossing){
//                besteOplossing.add(dbserver);
//            }
//        }else{
//            netwerkHuidigeOplossing.add(webserversBesteOplossing);
//            netwerkHuidigeOplossing.add(dbserversBesteOplossing);
//            netwerkHuidigeOplossing.add(firewallsOplossing);
//            netwerkEersteOplossing.add(webserversBesteOplossing);
//            netwerkEersteOplossing.add(dbserversBesteOplossing);
//            netwerkEersteOplossing.add(firewallsOplossing);
//            uitvoeringHeleNetwerk(minBeschikbaarheidspercentage);
//            for(ArrayList<Ontwerpcomponent> groep: netwerkBesteOplossing){
//                for(Ontwerpcomponent component: groep){
//                    besteOplossing.add(component);
//                }
//            }
//        }


    //einde backtrackingalgoritme

    private static int LoopDB(int totaalSrvrDB, int srvnr){
        int teller = 0;
        while(teller<maxLoop-totaalSrvrDB){
            dbAantal[srvnr]= teller;
            teller++;
            if (srvnr<maxDBSrt) {

                LoopDB(teller+totaalSrvrDB,srvnr+1);
            }

            if(srvnr==maxDBSrt) {
                totaalTeller++;
                System.out.println(totaalTeller + " W " + wbAantal[0] + "-" + wbAantal[1] + "-" + wbAantal[2] + "-" + "D " + dbAantal[0] + "-" + dbAantal[1] + "-" + dbAantal[2] + " ->" + BerekenBeschikbaarheid() + " - " + Berekenkosten() + " " + minKosten + serverset);

                double berekendeBeschikbaarheid = BerekenBeschikbaarheid();
                double berekendeKosten = Berekenkosten();

                if (berekendeBeschikbaarheid > minBeschikbaarheid) {
                    if (berekendeKosten < minKosten) {
                        minKosten = berekendeKosten;
                        serverset = "F 1 W " + wbAantal[0] + "-" + wbAantal[1] + "-" + wbAantal[2] + "-" + "D " + dbAantal[0] + "-" + dbAantal[1] + "-" + dbAantal[2];
                    }
                    return srvnr;
                }
            }
        }
        return srvnr;
    }

    private static int LoopWB(int totaalSrvrWB,int srvnr){
        int teller = 0;
        while(teller<maxLoop-totaalSrvrWB){
            wbAantal[srvnr]= teller;
            if (srvnr<maxWBSrt) {
                LoopWB(teller+totaalSrvrWB,srvnr+1);
            }
            if(srvnr==maxWBSrt){
                LoopDB(0,0);

            }

            teller++;
        }
        return srvnr;
    }

    private static double BerekenBeschikbaarheid(){
        double beschikbaarheidFW = 1 - Math.pow((1 - 0.99998), 1);
        double beschikbaarheidDB = 1 - Math.pow((1 - dbBeschikbaarheid[0]), dbAantal[0]) * Math.pow((1 - dbBeschikbaarheid[1]), dbAantal[1]) * Math.pow((1 - dbBeschikbaarheid[2]), dbAantal[2]);
        double beschikbaarheidWB = 1 - Math.pow((1 - wbBeschikbaarheid[0]), wbAantal[0]) * Math.pow((1 - wbBeschikbaarheid[1]), wbAantal[1]) * Math.pow((1 - wbBeschikbaarheid[2]), wbAantal[2]);
        double totaleBeschikbaarheid = beschikbaarheidFW * beschikbaarheidDB * beschikbaarheidWB;

        return totaleBeschikbaarheid;
    }

    private static double Berekenkosten(){
        double kostenFW = 4000;
        double kostenDB = (dbAantal[0] * dbKosten[0]) + (dbAantal[1] * dbKosten[1]) + (dbAantal[2] *dbKosten[2]);
        double kostenWB = (wbAantal[0] * wbKosten[0]) + (wbAantal[1] * wbKosten[1]) + (wbAantal[2] * wbKosten[2]);
        double totalekosten = kostenFW + kostenDB + kostenWB;

        return totalekosten;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        minimaalTotaleBeschikbaarheid.setText("Minimaal totaal beschikbaarheidspercentage:");     // standaard labeltekst zonder melding
        try {
            if (e.getSource() == bereken) {
                // ingevoerd percentage wordt van String naar Double omgezet
//                double ingevoerdPercentage = Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText().replaceAll(",", "."));
                minBeschikbaarheid = (Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText().replaceAll(",", "."))/100);
                LoopWB(0,0);
                System.out.println(totaalTeller + " combinaties onderzocht " + minKosten + " - " + serverset);

                char[] nummers = serverset.toCharArray();
                for(char nummer: nummers) {
                    System.out.println(nummer);
                }
                System.out.println("pfSense: " + nummers[2]);
                System.out.println("Webserver 1: " + nummers[6]);
                System.out.println("Webserver 2: " + nummers[8]);
                System.out.println("Webserver 3: " + nummers[10]);
                System.out.println("Database 1: " + nummers[14]);
                System.out.println("Database 2: " + nummers[16]);
                System.out.println("Database 3: " + nummers[18]);
                

                //backtracking algoritme gebruiken om optimale waarden te bepalen

               //testcode
//                uitvoering(webservers, ingevoerdPercentage);
//                for(Ontwerpcomponent component: serversBesteOplossing){
//                    System.out.println(component.getNaam());
//                }
//                System.out.println();
//                serversBesteOplossing.clear();
//                uitvoering2(webservers, ingevoerdPercentage, 13200);
//                for(Ontwerpcomponent component: serversBesteOplossing){
//                    System.out.println(component.getNaam());
//                }
//                serversBesteOplossing.clear();
//                System.out.println();
//                ArrayList<Ontwerpcomponent> firewalls = new ArrayList<>();
//                firewalls.add(ontwerpcomponenten.get(0));
//                ArrayList<Ontwerpcomponent> webserverTest = new ArrayList<>();
//                webserverTest.add(webservers.get(0));
//                webserverTest.add(webservers.get(0));
//                webserverTest.add(webservers.get(2));
//                ArrayList<Ontwerpcomponent> dbserverTest = new ArrayList<>();
//                dbserverTest.add(webservers.get(0));
//                dbserverTest.add(webservers.get(0));
//                dbserverTest.add(webservers.get(1));
//                netwerkHuidigeOplossing.clear();
//                netwerkHuidigeOplossing.add(0,webserverTest);
//                netwerkHuidigeOplossing.add(1,dbserverTest);
//                netwerkHuidigeOplossing.add(2,firewalls);
//                netwerkEersteOplossing.add(webserverTest);
//                netwerkEersteOplossing.add(dbserverTest);
//                netwerkEersteOplossing.add(firewalls);
//                uitvoeringHeleNetwerk(ingevoerdPercentage);
//                for(ArrayList<Ontwerpcomponent> groep: netwerkBesteOplossing){
//                    for(Ontwerpcomponent component: groep){
//                        System.out.println(component.getNaam());
//                    }
//                }
//                System.out.println();
//                optimaliseer(ingevoerdPercentage);
//                System.out.println(besteOplossing);
//                for(Ontwerpcomponent component: besteOplossing){
//                    System.out.println(component.getNaam());
//                }
            }
            if (e.getSource() == voegToe) {

                Groep databasegroep = new Groep("databases", 90);
                // beschikbaarheidspercentage moet bekend worden naar aanleiding van de databases
                // die uit optimalisatiefunctie komen, nu voorbeelddata gebruikt
                Groep webservergroep = new Groep("webservers", 94);
                // beschikbaarheidspercentage moet bekend worden naar aanleiding van de webservers
                // die uit optimalisatiefunctie komen, nu voorbeelddata gebruikt
                Groep firewall = new Groep("firewall", 99.998);

                for (Ontwerpcomponent ontwerpcomponent : optimaleWaarden) {
                    if (ontwerpcomponent.getType().equals("database")) {
                        databasegroep.componenten.add(ontwerpcomponent);
                    }
                    if (ontwerpcomponent.getType().equals("webserver")) {
                        webservergroep.componenten.add(ontwerpcomponent);
                    }
                    if (ontwerpcomponent.getType().equals("firewall")) {
                        firewall.componenten.add(ontwerpcomponent);
                    }
                }

                //als het ontwerpframe van waaruit je een nieuw ontwerp wil aanmaken
                // nog geen ontwerpnetwerk had, wordt deze gesloten en wordt er een nieuw ontwerpframe met het
                //nieuwe netwerk aangemaakt (wanneer er niet al een ontwerpnetwerk met dezelfde beschikbaarheid bestaat)
                if(ontwerpFrame.getOntwerpnetwerk() == null) {


                   // bepalen of er al een ontwerp met dezelfde beschikbaarheid is
                   Ontwerpnetwerk ontwerpNetwerkMetDezelfdeBeschikbaarheid = null;
                   for(Ontwerpnetwerk ontwerpnetwerk1: Ontwerpnetwerk.getOntwerpNetwerken()) {
                       if (ontwerpnetwerk1.getBeschikbaarheidspercentage().equals(totaleBeschikbaarheidNetwerkTekst)) {
                           ontwerpNetwerkMetDezelfdeBeschikbaarheid = ontwerpnetwerk1;
                       }
                   }

                   // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt er een nieuw ontwerpnetwerk gemaakt
                       if (ontwerpNetwerkMetDezelfdeBeschikbaarheid == null) {
                           //nieuw ontwerpnetwerk aanmaken
                           // kosten en beschikbaarheidspercentage zijn bekend naar aanleiding van optimalisatiefunctie, nu voorbeelddata gebruikt

                           String naamOntwerpnetwerk = "Ontwerpnetwerk " + totaleBeschikbaarheidNetwerkTekst;
                           Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk(naamOntwerpnetwerk + "%", totaleBedrag, beschikbaarheidspercentage);
                           ontwerpnetwerk.groepen.add(databasegroep);
                           ontwerpnetwerk.groepen.add(webservergroep);
                           ontwerpnetwerk.groepen.add(firewall);

                           //ontwerpnetwerk wordt in database opgeslagen
                           for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten) {
                               int k = 0;
                               for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
                                   if (ontwerpcomponent2.equals(ontwerpcomponent)) {
                                       k++;
                                   }
                               }
                               if(k != 0) {

//                                   // Testcode of de juiste data in de database zal worden opgeslagen
//                                   System.out.println(naamOntwerpnetwerk + " "
//                                          + ontwerpcomponent.getNaam() + " " + k + " " + beschikbaarheidspercentage.getText() +
//                                         " " + totaleBedrag.getText());

                                   Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");
                                   Statement statement = connection.createStatement();
                                   int gelukt = statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " +
                                           "('"+ naamOntwerpnetwerk +"', '"+ ontwerpcomponent.getNaam()+"', '"+ k + "','"
                                           + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");
                                   statement.close();
                                   connection.close();
                               }
                           }

                           ontwerpFrame.dispose();
                           OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpnetwerk);
                       }

                       // wanneer er wel een ontwerp met dezelfde beschikbaarheid is, wordt er geen nieuw ontwerpnetwerk aangemaakt,
                       // maar wordt het al bestaande netwerk geopend
                   else {
                       ontwerpFrame.dispose();
                       OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpNetwerkMetDezelfdeBeschikbaarheid);
                    }
                }

                //als het ontwerpframe van waaruit je een nieuw ontwerp wil aanmaken al een ontwerpnetwerk heeft, wordt er een nieuw ontwerpframe met het nieuwe
                //netwerk aangemaakt zonder het ontwerpframe eerst te sluiten (wanneer er niet al een ontwerpnetwerk met dezelfde beschikbaarheid bestaat)
                if(ontwerpFrame.getOntwerpnetwerk() != null && !ontwerpFrame.isGedruktOpOptimalisatieknop()) {

                    // bepalen of er al een ontwerp met dezelfde beschikbaarheid is
                    Ontwerpnetwerk ontwerpNetwerkMetZelfdeBeschikbaarheid = null;
                    for(Ontwerpnetwerk ontwerpnetwerk1: Ontwerpnetwerk.getOntwerpNetwerken()) {
                        if (ontwerpnetwerk1.getBeschikbaarheidspercentage().equals(totaleBeschikbaarheidNetwerkTekst)) {
                            ontwerpNetwerkMetZelfdeBeschikbaarheid = ontwerpnetwerk1;
                        }
                    }

                    // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt er een nieuw ontwerpnetwerk gemaakt
                    if (ontwerpNetwerkMetZelfdeBeschikbaarheid == null) {
                        //nieuw ontwerpnetwerk aanmaken
                        // kosten en beschikbaarheidspercentage zijn bekend naar aanleiding van optimalisatiefunctie, nu voorbeelddata gebruikt
                        //ontwerpnetwerk moet in database opgeslagen worden

                        String naamOntwerpnetwerk = "Ontwerpnetwerk " + totaleBeschikbaarheidNetwerkTekst;
                        Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk(naamOntwerpnetwerk + "%", totaleBedrag, beschikbaarheidspercentage);
                        ontwerpnetwerk.groepen.add(databasegroep);
                        ontwerpnetwerk.groepen.add(webservergroep);
                        ontwerpnetwerk.groepen.add(firewall);
                     //   ontwerpnetwerk.setCorrecteKostenEnBeschikbaarheid();

                        for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten) {
                            int k = 0;
                            for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
                                if (ontwerpcomponent2.equals(ontwerpcomponent)) {
                                    k++;
                                }
                            }
                            if(k != 0) {

                                // Testcode of de juiste data in de database zal worden opgeslagen
//                          System.out.println(naamOntwerpnetwerk + " "
//                          + ontwerpcomponent.getNaam() + " " + k + " " + beschikbaarheidspercentage.getText() +
//                          " " + totaleBedrag.getText());

                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");
                        Statement statement = connection.createStatement();
                        int gelukt = statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " +
                        "('"+ naamOntwerpnetwerk +"', '"+ ontwerpcomponent.getNaam()+"', '"+ k + "','"
                        + beschikbaarheidspercentage+ "', '"+ totaleBedrag +"' )");

                        statement.close();
                        connection.close();
                            }
                        }

                        OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpnetwerk);
                    }
                    // wanneer er wel een ontwerp met dezelfde beschikbaarheid is, wordt er geen nieuw ontwerpnetwerk aangemaakt,
                    // maar wordt het al bestaande netwerk geopend
                    else {
                        OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpNetwerkMetZelfdeBeschikbaarheid);
                    }
                }

                //wanneer je een al bestaand ontwerpnetwerk wilt optimaliseren, wordt het bestaande ontwerpnetwerk geüpdatet
                //(wanneer er niet al een ontwerpnetwerk met dezelfde beschikbaarheid bestaat)
                if(ontwerpFrame.getOntwerpnetwerk() != null && ontwerpFrame.isGedruktOpOptimalisatieknop()) {

                    // bepalen of er al een ontwerp met dezelfde beschikbaarheid is
                    Ontwerpnetwerk ontwerpNetwerkMetZelfdeBeschikbaarheid = null;
                    for(Ontwerpnetwerk ontwerpnetwerk1: Ontwerpnetwerk.getOntwerpNetwerken()) {
                        if (ontwerpnetwerk1.getBeschikbaarheidspercentage().equals(totaleBeschikbaarheidNetwerkTekst)) {
                            ontwerpNetwerkMetZelfdeBeschikbaarheid = ontwerpnetwerk1;
                        }
                    }

                    // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt het ontwerpnetwerk geüpdatet
                    // ontwerpnetwerk moet in database geüpdatet worden
                    if (ontwerpNetwerkMetZelfdeBeschikbaarheid == null) {
                        String naamOntwerpnetwerk = "Ontwerpnetwerk " + totaleBeschikbaarheidNetwerkTekst;
                        ontwerpFrame.getOntwerpnetwerk().setNaam(naamOntwerpnetwerk + "%");
                        ontwerpFrame.getOntwerpnetwerk().setKosten(totaleBedrag);
                        ontwerpFrame.getOntwerpnetwerk().setBeschikbaarheidspercentage(beschikbaarheidspercentage);
                        ontwerpFrame.getOntwerpnetwerk().groepen.set(0, databasegroep);
                        ontwerpFrame.getOntwerpnetwerk().groepen.set(1, webservergroep);
                        ontwerpFrame.getOntwerpnetwerk().groepen.set(2, firewall);

                        for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten) {
                            int k = 0;
                            for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
                                if (ontwerpcomponent2.equals(ontwerpcomponent)) {
                                    k++;
                                }
                            }
                            if(k != 0) {

//                                // Testcode of de juiste data in de database zal worden geüpdatet
//                                System.out.println(naamOntwerpnetwerk + " "
//                                       + ontwerpcomponent.getNaam() + " " + k + " " + beschikbaarheidspercentage.getText() +
//                                      " " + totaleBedrag.getText() + " vervangt ontwerpnetwerk met beschikbaarheidspercentage van: "
//                                        + ontwerpFrame.getOntwerpnetwerk().getBeschikbaarheidspercentage());
//
                                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");
                                PreparedStatement statement = connection.prepareStatement("UPDATE Ontwerpnetwerk SET NaamNetwerk = '"+naamOntwerpnetwerk+" ',' NaamComponent = "+ontwerpcomponent.getNaam()
                                        +" ',' AantalGebruikt = "+k+" ',' Beschikbaarheid = "+beschikbaarheidspercentage
                                        +" ',' Kosten = "+ totaleBedrag+ "' WHERE Beschikbaarheid = ? ");

                                statement.setString(1, ontwerpFrame.getOntwerpnetwerk().getBeschikbaarheidspercentage());

                                int gelukt = statement.executeUpdate();

                                statement.close();
                                connection.close();

                            }
                        }




                        ontwerpFrame.setGedruktOpOptimalisatieknop(false);
                        Ontwerpnetwerk geoptimaliseerdOntwerpnetwerk = ontwerpFrame.getOntwerpnetwerk();
                        ontwerpFrame.dispose();
                        OntwerpFrame ontwerpFrame = new OntwerpFrame(geoptimaliseerdOntwerpnetwerk);

                    }
                    else {
                        // wanneer er wel eerder een ontwerp met dezelfde beschikbaarheid is, wordt het ontwerpnetwerk niet geüpdatet,
                        // maar wordt het al bestaande netwerk geopend
                        OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpNetwerkMetZelfdeBeschikbaarheid);
                        }

                    }
                }

                dispose();

        } catch(NumberFormatException nfe){
                minimaalTotaleBeschikbaarheid.setText("<html>Minimaal totaal beschikbaarheidspercentage: <br> <font color = 'red'> Voer een percentage in <font/><html/>");
                // melding dat er een percentage ingevuld moet worden
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }
}