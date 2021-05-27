import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OptimalisatieDialog extends JDialog implements ActionListener {
    private OntwerpFrame ontwerpFrame;
    private JLabel minimaalTotaleBeschikbaarheid;
    private JTextField vulNaamIn;
    private JTextField jtMinimaalTotaleBeschikbaarheid;  //hierin wordt minimaal gewenste beschikbaarheidspercentage opgegeven
    private JButton bereken;
    private Double beschikbaarheidspercentage = 0.000;   //berekende beschikbaarheidspercentage uit optimalisatiefunctie
    private String totaleBeschikbaarheidNetwerkTekst;
    private JLabel beschikbaarheidspercentageLabel;
    private Double totaleBedrag = 0.00;                  //berekende totale bedrag uit optimalisatiefunctie
    private static JLabel totaleBedragLabel;
    private JButton voegToe;
    private Color backClr1 = new Color(60, 63, 65); //achtergrondkleur
    private Color backClr2 = new Color(43, 43, 43); //tabelkleur
    private ArrayList<Ontwerpcomponent> ontwerpcomponenten = new ArrayList<>();
    private ArrayList<Ontwerpcomponent> webservers = new ArrayList<>();
    private ArrayList<Ontwerpcomponent> dbservers = new ArrayList<>();
    private ArrayList<Ontwerpcomponent> optimaleWaarden = new ArrayList<>();
    private static double totaleBeschikbaarheid;
    private static int maxLoop = 8;
    private static int[] aantalWebservers = {0,0,0};
    private JLabel componentToeVoegFoutmelding;
    private static double[] webserverBeschikbaarheid = {0.8,0.9,0.95};
    private static double[] wbKosten = {2200,3200,5100};
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

        componentToeVoegFoutmelding = new JLabel();
        componentToeVoegFoutmelding.setForeground(Color.WHITE);

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


        statement.close();
        connection.close();

        for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
            if (ontwerpcomponent.getType().equals("database")) {
                dbservers.add(ontwerpcomponent);
            }
            if (ontwerpcomponent.getType().equals("webserver")) {
                webservers.add(ontwerpcomponent);
            }
        }



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

        //rechter tabel:
        String[] kolomnamen2 = {"Naam component", "Type", "Kosten (€)", "Beschikbaarheid (%)", "Aantal"};
        data2 = new Object[ontwerpcomponenten.size()][5];

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


        tabel2 = new JTable(data2, kolomnamen2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (row==0 && column == 4||row==1 && column == 4||row==1 && column == 4||row==2 && column == 4||row==3 && column == 4||row==4 && column == 4||row==5 && column == 4||row==6 && column == 4) {
                    return true;
                } else
                    return false;
            }
        };
        tabel2.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int rijEen = Integer.parseInt(tabel2.getValueAt(0, 4).toString());
                int rijTwee = Integer.parseInt(tabel2.getValueAt(1, 4).toString());
                int rijDrie = Integer.parseInt(tabel2.getValueAt(2, 4).toString());
                int rijVier = Integer.parseInt(tabel2.getValueAt(3, 4).toString());
                int rijVijf = Integer.parseInt(tabel2.getValueAt(4, 4).toString());
                int rijZes = Integer.parseInt(tabel2.getValueAt(5, 4).toString());
                int rijZeven = Integer.parseInt(tabel2.getValueAt(6, 4).toString());

                double beschikbaarRijEen = Math.pow((1 - 0.9), rijEen);
                double beschikbaarRijDrie = Math.pow((1 - 0.95), rijDrie);
                double beschikbaarRijVijf = Math.pow((1 - 0.98), rijVijf);
                if (beschikbaarRijEen == 0) {
                    beschikbaarRijEen = 1;
                }
                if (beschikbaarRijDrie == 0) {
                    beschikbaarRijEen = 1;
                }
                if (beschikbaarRijVijf == 0) {
                    beschikbaarRijEen = 1;
                }

                double beschikbaarRijTwee = Math.pow((1 - 0.9), rijTwee);
                double beschikbaarRijVier = Math.pow((1 - 0.95), rijVier);
                double beschikbaarRijZes = Math.pow((1 - 0.98), rijZes);

                if (beschikbaarRijTwee == 0) {
                    beschikbaarRijTwee = 1;
                }
                if (beschikbaarRijVier == 0) {
                    beschikbaarRijVier = 1;
                }
                if (beschikbaarRijZes == 0) {
                    beschikbaarRijZes = 1;
                }
                double beschikbaarheidWB = 1 - (beschikbaarRijTwee * beschikbaarRijVier * beschikbaarRijZes);
                double beschikbaarheidFW = 1 - Math.pow((1 - 0.99998), 1);
                double beschikbaarheidDB = 1 - (beschikbaarRijEen * beschikbaarRijDrie * beschikbaarRijVijf);

                double beschikbaarheidNetwerk = beschikbaarheidFW * beschikbaarheidDB * beschikbaarheidWB;

                DecimalFormat df = new DecimalFormat("0.000");
                String beschikbaarheidnetwerk = df.format(beschikbaarheidNetwerk*100);
                beschikbaarheidspercentageLabel.setText("" + beschikbaarheidnetwerk);

                double totaleKostenOntwerp = ((5100 * rijEen) + (2200 * rijTwee) + (7700 * rijDrie) + (3200 * rijVier) + (12200 * rijVijf) + (5100 * rijZes) + (4000 * rijZeven));
                totaleBedragLabel.setText("" + totaleKostenOntwerp);
            }
        });
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

        vulNaamIn = new JTextField(15);
        vulNaamIn.addActionListener(this);
        vulNaamIn.setBackground(backClr1);
        vulNaamIn.setForeground(Color.white);

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

        beschikbaarheidspercentageLabel = new JLabel("0");
        beschikbaarheidspercentageLabel.setForeground(Color.white);


        JLabel totaleKosten = new JLabel("Totale kosten ontwerp (€):");
        totaleKosten.setForeground(Color.white);


        for (Ontwerpcomponent ontwerpcomponent : optimaleWaarden) {
            totaleBedrag += Double.parseDouble(ontwerpcomponent.getKosten().replaceAll(",", "."));
        }

        DecimalFormat df2 = new DecimalFormat("0.00");

        totaleBedragLabel = new JLabel("0,00");
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

        GridBagConstraints _6 = new GridBagConstraints();
        _6.anchor = GridBagConstraints.LAST_LINE_END;                                            // control bevindt zich rechts onderin de cel
        _6.gridx = 2;                                                                            // control bevindt zich in de derde kolom
        _6.gridy = 2;                                                                            // control bevindt zich in de derde rij
        _6.weighty = 1.0;                                                                        // control krijgt extra verticale ruimte
        _6.insets = new Insets(0, 0, 5, 5);                                 // padding tussen control en cel voor de onder- en rechterkant


        GridBagConstraints _7 = new GridBagConstraints();
        _7.anchor = GridBagConstraints.EAST;                                                     // control bevindt zich links in de cel
        _7.fill = GridBagConstraints.LAST_LINE_END;                                                 // control bevindt zich in de derde kolom
        _7.gridx = 2;                                                                            // control bevindt zich in de derde rij
        _7.gridy = 5;
        _7.weighty = 1;                                                                          // control krijgt extra verticale ruimte
        _7.insets = new Insets(5, 20, 0, 0);                                // padding tussen control en cel voor de onder- en rechterkant

        GridBagConstraints _5 = new GridBagConstraints();
        _5.anchor = GridBagConstraints.NORTH;                                                     // control bevindt zich links in de cel
        _5.fill = GridBagConstraints.LAST_LINE_END;                                                 // control bevindt zich in de derde kolom
        _5.gridy = 1;                                                                            // control bevindt zich in de derde rij
        _5.weighty = 1;                                                                          // control krijgt extra verticale ruimte
        _5.insets = new Insets(0, 0, 0, 0);                                // padding tussen control en cel voor de onder- en rechterkant

        panelRechtsOnder.setPreferredSize(dimensie);
        panelRechtsOnder.setBorder(witteRand);
        panelRechtsOnder.add(totaleBeschikbaarheid, _1);                                          // gemaakte label toevoegen
        panelRechtsOnder.add(beschikbaarheidspercentageLabel, _2);                                     // gemaakte label toevoegen
        panelRechtsOnder.add(totaleKosten, _3);                                                   // gemaakte label toevoegen
        panelRechtsOnder.add(totaleBedragLabel, _4);                                                   // gemaakte label toevoegen
        panelRechtsOnder.add(voegToe, _7);                                                        // gemaakte knop toevoegen
        panelRechtsOnder.add(componentToeVoegFoutmelding, _5);
        panelRechtsOnder.add(vulNaamIn, _6);

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
                totaleBedragLabel.setText(minKosten + "");
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
                minBeschikbaarheid = (Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText().replaceAll(",", ".")) / 100);
                LoopWB(0, 0);
                System.out.println(totaalTeller + " combinaties onderzocht " + minKosten + " - " + resultaat);
//                totaleBedragLabel.setText(minKosten+ "");

                char[] nummers = resultaat.toCharArray();

                //hier worden de waardes in de tabel er in gezet op basis van het resultaat van het algoritme
                tabel2.setValueAt(nummers[14], 0, 4);
                tabel2.setValueAt(nummers[6], 1, 4);
                tabel2.setValueAt(nummers[16], 2, 4);
                tabel2.setValueAt(nummers[8], 3, 4);
                tabel2.setValueAt(nummers[18], 4, 4);
                tabel2.setValueAt(nummers[10], 5, 4);
                tabel2.setValueAt(nummers[2], 6, 4);

                //de inhoud van de tabel is een object, dus er moet eerst een string van gemaakt worden en dan kan er pas een Int van worden gemaakt
                int rijEen = Integer.parseInt(tabel2.getValueAt(0, 4).toString());
                int rijTwee = Integer.parseInt(tabel2.getValueAt(1, 4).toString());
                int rijDrie = Integer.parseInt(tabel2.getValueAt(2, 4).toString());
                int rijVier = Integer.parseInt(tabel2.getValueAt(3, 4).toString());
                int rijVijf = Integer.parseInt(tabel2.getValueAt(4, 4).toString());
                int rijZes = Integer.parseInt(tabel2.getValueAt(5, 4).toString());

                //het (1-beschikbaarheid A) deel van de formule per rij uitrekenen
                double beschikbaarRijEen = Math.pow((1 - 0.9), rijEen);
                double beschikbaarRijDrie = Math.pow((1 - 0.95), rijDrie);
                double beschikbaarRijVijf = Math.pow((1 - 0.98), rijVijf);
                if (beschikbaarRijEen == 0) {
                    beschikbaarRijEen = 1;
                }
                if (beschikbaarRijDrie == 0) {
                    beschikbaarRijEen = 1;
                }
                if (beschikbaarRijVijf == 0) {
                    beschikbaarRijEen = 1;
                }

                //het (1-beschikbaarheid A) deel van de formule per rij uitrekenen
                double beschikbaarRijTwee = Math.pow((1 - 0.9), rijTwee);
                double beschikbaarRijVier = Math.pow((1 - 0.95), rijVier);
                double beschikbaarRijZes = Math.pow((1 - 0.98), rijZes);

                if (beschikbaarRijTwee == 0) {
                    beschikbaarRijTwee = 1;
                }
                if (beschikbaarRijVier == 0) {
                    beschikbaarRijVier = 1;
                }
                if (beschikbaarRijZes == 0) {
                    beschikbaarRijZes = 1;
                }

                //berekenen beschikbaarheid van huidig ontwerp
                double beschikbaarheidWB = 1 - (beschikbaarRijTwee * beschikbaarRijVier * beschikbaarRijZes);
                double beschikbaarheidFW = 1 - Math.pow((1 - 0.99998), 1);
                double beschikbaarheidDB = 1 - (beschikbaarRijEen * beschikbaarRijDrie * beschikbaarRijVijf);
                double beschikbaarheidNetwerk = beschikbaarheidFW * beschikbaarheidDB * beschikbaarheidWB;

                DecimalFormat df = new DecimalFormat("0.000");
                String beschikbaarheidnetwerk = df.format(beschikbaarheidNetwerk * 100);
                beschikbaarheidspercentageLabel.setText("" + beschikbaarheidnetwerk);
            }
        } catch (NumberFormatException exception) {
            minimaalTotaleBeschikbaarheid.setText("<html>Minimaal totaal beschikbaarheidspercentage: <br> <font color = 'red'> Voer een percentage in <font/><html/>");
        }

        try {
            if (e.getSource() == voegToe) {
                String naamOntwerpnetwerk = vulNaamIn.getText();
                beschikbaarheidspercentage = Double.parseDouble(beschikbaarheidspercentageLabel.getText());

                //query's worden uitgevoerd om het netwerkontwerp in de database op te slaan.
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
                Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('" + naamOntwerpnetwerk + "', '" + "HAL9001DB" + "', '" + tabel2.getValueAt(0, 4) + "','" + beschikbaarheidspercentage + "', '" + totaleBedrag + "' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('" + naamOntwerpnetwerk + "', '" + "HAL9001W" + "', '" + tabel2.getValueAt(1, 4) + "','" + beschikbaarheidspercentage + "', '" + totaleBedrag + "' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('" + naamOntwerpnetwerk + "', '" + "HAL9002DB" + "', '" + tabel2.getValueAt(2, 4) + "','" + beschikbaarheidspercentage + "', '" + totaleBedrag + "' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('" + naamOntwerpnetwerk + "', '" + "HAL9002W" + "', '" + tabel2.getValueAt(3, 4) + "','" + beschikbaarheidspercentage + "', '" + totaleBedrag + "' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('" + naamOntwerpnetwerk + "', '" + "HAL9003DB" + "', '" + tabel2.getValueAt(4, 4) + "','" + beschikbaarheidspercentage + "', '" + totaleBedrag + "' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('" + naamOntwerpnetwerk + "', '" + "HAL9003W" + "', '" + tabel2.getValueAt(5, 4) + "','" + beschikbaarheidspercentage + "', '" + totaleBedrag + "' )");
                statement.executeUpdate("INSERT INTO Ontwerpnetwerk VALUES " + "('" + naamOntwerpnetwerk + "', '" + "pfSense" + "', '" + tabel2.getValueAt(6, 4) + "','" + beschikbaarheidspercentage + "', '" + totaleBedrag + "' )");

                Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk();
                ontwerpnetwerk.setNaam(naamOntwerpnetwerk);

                connection.close();
                statement.close();

                dispose();
            }
        } catch(NumberFormatException nfe){
            minimaalTotaleBeschikbaarheid.setText("<html>Minimaal totaal beschikbaarheidspercentage: <br> <font color = 'red'> Voer een percentage in <font/><html/>");
            // melding dat er een percentage ingevuld moet worden
        } catch (SQLIntegrityConstraintViolationException exception) {
            componentToeVoegFoutmelding.setText("<html><font color = 'red'>U heeft nog geen naam ingevuld of de opgegeven naam bestaat al<font/><html/>");
        } catch (SQLException sql) {
        sql.printStackTrace();
        }
    }
}