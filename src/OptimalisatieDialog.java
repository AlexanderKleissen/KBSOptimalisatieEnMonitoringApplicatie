import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OptimalisatieDialog extends JDialog implements ActionListener {
    private OntwerpFrame ontwerpFrame;
    private JLabel minimaalTotaleBeschikbaarheid;
    private JTextField jtMinimaalTotaleBeschikbaarheid;  //hierin wordt minimaal gewenste beschikbaarheidspercentage opgegeven
    private JButton bereken;
    private JLabel beschikbaarheidspercentage;  //berekende beschikbaarheidspercentage uit optimalisatiefunctie
    private JLabel totaleBedrag;                //berekende totale bedrag uit optimalisatiefunctie
    private JButton voegToe;
    private ArrayList<Ontwerpcomponent> ontwerpcomponenten = new ArrayList<>();
    private Color backClr1 = new Color(60, 63, 65); //achtergrondkleur
    private Color backClr2 = new Color(43, 43, 43); //tabelkleur

    public OptimalisatieDialog(OntwerpFrame frame) {
        super(frame, false);
        this.ontwerpFrame = frame;
        setSize(1500, 1000);
        setLayout(new BorderLayout());
        setTitle("Monitoringapplicatie (optimaliseren)");

        //Bovenste twee labels:
        Border witteRand = BorderFactory.createLineBorder(Color.WHITE, 1);                   //rand voor de meeste onderdelen
        Dimension dimensie = new Dimension(700, 100);                                     // hoogte en breedte voor de bovenste twee labels
        JLabel mogelijkeComponenten = new JLabel("Specificaties mogelijke ontwerpcomponenten");
        mogelijkeComponenten.setBorder(witteRand);
        mogelijkeComponenten.setPreferredSize(dimensie);
        mogelijkeComponenten.setForeground(Color.white);

        JLabel optimaleWaardenOntwerp = new JLabel("Optimale waarden ontwerp");
        optimaleWaardenOntwerp.setBorder(witteRand);
        optimaleWaardenOntwerp.setPreferredSize(dimensie);
        optimaleWaardenOntwerp.setForeground(Color.white);


        //Linker tabel:
        String[] kolomnamen = {"Naam component", "type", "Kosten (€)", "Beschikbaarheid (%)", "Totale Diskruimte (GB)"};     //een array met kolomnamen
        Object[][] data = {{"HAL9001DB", "database", 5100, 90, 5}, {"HAL9002DB", "database", 7700, 95, 7}, {"HAL9003DB", "database", 12200, 98, 10},  //voorbeelddata in een tweedimensionale array (matrix)
                {"HAL9001W", "webserver", 2200, 80, 8}, {"HAL9002W", "webserver", 3200, 90, 7}, {"HAL9003W", "webserver", 5100, 95, 9}, {"firewall pfSense", "firewall", 4000, 99.998, 11}};
        /*(data moet eigenlijk uit database gehaald worden)*/                                              /*Een array van het type Object maakt het mogelijk meerdere
                                                                                                      soorten data op te slaan in de array.*/
        JTable tabel1 = new JTable(data, kolomnamen);                                                 // kolomnamen en data meegeven aan de tabel
        tabel1.setEnabled(false);                                                                   // data kan nu niet door gebruiker aangepast worden
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

        TableColumn tabel1kolom5 = tabel1.getColumnModel().getColumn(4);                 // kolom 5
        tabel1kolom5.setPreferredWidth(185);

        Dimension dimensieScrollPaneTabel1en2 = new Dimension(700, 500);                // vaste hoogte en breedte voor de scrollPane (container voor de tabel)
        JScrollPane scrollPaneTabel1 = new JScrollPane(tabel1);
        scrollPaneTabel1.setPreferredSize(dimensieScrollPaneTabel1en2);
        tabel1.setFillsViewportHeight(true);                                                       //tabel vult de hele scrollPane ongeacht aantal rijen


        //rechter tabel:
        String[] kolomnamen2 = {"Naam component", "type", "Kosten (€)", "Beschikbaarheid (%)", "Totale Diskruimte (GB)", "aantal"};
        Object[][] data2 = new Object[][]{{"HAL9001DB", "database", 5100, 90, 5, 3}, {"HAL9002DB", "database", 7700, 95, 7, 1}, {"HAL9001W", "webserver", 2200, 80, 8, 1},
                {"HAL9002W", "webserver", 3200, 90, 7, 4}, {"firewall pfSense", "firewall", 4000, 99.998, 11, 1}}; //voorbeelddata
        // data is eigenlijk pas bekend na gebruik van optimalisatiefunctie

        JTable tabel2 = new JTable(data2, kolomnamen2);
        tabel2.setEnabled(false);
        tabel2.setBackground(backClr2);
        tabel2.setForeground(Color.white);

        JTableHeader header2 = tabel2.getTableHeader();
        header2.setBackground(backClr1);
        header2.setForeground(Color.white);

        //De optimale componenten (uit de rechter tabel) toevoegen aan de arraylist van ontwerpcomponenten
        for (int i = 0; i < data2.length; i++) {
            for (int j = 0; j < (int) data2[i][5]; j++) {
                Ontwerpcomponent ontwerpcomponent = new Ontwerpcomponent((String) data2[i][0], (String) data2[i][1],
                        Double.parseDouble(data2[i][2].toString()), Integer.parseInt(data2[i][4].toString()), Double.parseDouble(data2[i][3].toString()));
                ontwerpcomponenten.add(ontwerpcomponent);
            }
        }


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

        TableColumn tabel2kolom6 = tabel2.getColumnModel().getColumn(5);                // kolom 6
        tabel2kolom6.setPreferredWidth(70);

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

        beschikbaarheidspercentage = new JLabel("99.99");          //  voorbeelddata, echte data volgt uit optimalisatiefunctie
        beschikbaarheidspercentage.setForeground(Color.white);

        JLabel totaleKosten = new JLabel("Totale kosten ontwerp (€):");
        totaleKosten.setForeground(Color.white);

        totaleBedrag = new JLabel("42000");                       // voorbeelddata, echte data volgt uit optimalisatiefunctie
        totaleBedrag.setForeground(Color.white);

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
        panelRechtsOnder.add(beschikbaarheidspercentage, _2);                                     // gemaakte label toevoegen
        panelRechtsOnder.add(totaleKosten, _3);                                                   // gemaakte label toevoegen
        panelRechtsOnder.add(totaleBedrag, _4);                                                   // gemaakte label toevoegen
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


    @Override
    public void actionPerformed(ActionEvent e) {
        minimaalTotaleBeschikbaarheid.setText("Minimaal totaal beschikbaarheidspercentage:");     // standaard labeltekst zonder melding
        try {
            if (e.getSource() == bereken) {
                double ingevoerdPercentage = Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText()); // ingevoerd percentage wordt van String naar Double omgezet
                System.out.println(ingevoerdPercentage);                                                // voor nu even om te testen of ingevoerd percentage opgehaald kan worden

                /* Backtrackingalgoritme optimalisatie */

                //Controle om te kijken of de beschikbaarheid 99,99% is.

                    //false: Controle om te kijken of de beschikbaarheid stijgt als een component wordt toegevoegd.

                        //false: component weghalen en volgende component gebruiken

                        //true: component toevoegen en volgende component gebruiken

                    //true: componenten toevoegen aan het optimalisatiescherm aan de rechterkant.

                //einde backtrackingalgoritme
            }
            if (e.getSource() == voegToe) {

                Groep databasegroep = new Groep("databases", 90);
                // beschikbaarheidspercentage moet bekend worden naar aanleiding van de databases
                // die uit optimalisatiefunctie komen, nu voorbeelddata gebruikt
                Groep webservergroep = new Groep("webservers", 94);
                // beschikbaarheidspercentage moet bekend worden naar aanleiding van de webservers
                // die uit optimalisatiefunctie komen, nu voorbeelddata gebruikt
                Groep firewall = new Groep("firewall", 99.998);

                for (Ontwerpcomponent ontwerpcomponent : ontwerpcomponenten) {
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
                //nieuwe netwerk aangemaakt
                if(ontwerpFrame.getOntwerpnetwerk() == null) {
                    int aantalOntwerpnetwerken = Ontwerpnetwerk.getOntwerpNetwerken().size();

                    //nieuw ontwerpnetwerk aanmaken
                    // kosten en beschikbaarheidspercentage zijn bekend naar aanleiding van optimalisatiefunctie, nu voorbeelddata gebruikt
                    Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk("Ontwerpnetwerk " + (aantalOntwerpnetwerken + 1), Double.parseDouble(totaleBedrag.getText()),
                            Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText()), Double.parseDouble(beschikbaarheidspercentage.getText()));
                    ontwerpnetwerk.groepen.add(databasegroep);
                    ontwerpnetwerk.groepen.add(webservergroep);
                    ontwerpnetwerk.groepen.add(firewall);
                    ontwerpFrame.dispose();
                    OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpnetwerk);
                }

                //als het ontwerpframe van waaruit je een nieuw ontwerp wil aanmaken al een ontwerpnetwerk heeft, wordt er een nieuw ontwerpframe met het nieuwe
                //netwerk aangemaakt zonder het ontwerpframe eerst te sluiten
                if(ontwerpFrame.getOntwerpnetwerk() != null && !ontwerpFrame.isGedruktOpOptimalisatieknop()) {
                    int aantalOntwerpnetwerken = Ontwerpnetwerk.getOntwerpNetwerken().size();

                 //nieuw ontwerpnetwerk aanmaken
                 // kosten en beschikbaarheidspercentage zijn bekend naar aanleiding van optimalisatiefunctie, nu voorbeelddata gebruikt
                 Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk("Ontwerpnetwerk " + (aantalOntwerpnetwerken + 1), Double.parseDouble(totaleBedrag.getText()),
                         Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText()), Double.parseDouble(beschikbaarheidspercentage.getText()));
                 ontwerpnetwerk.groepen.add(databasegroep);
                 ontwerpnetwerk.groepen.add(webservergroep);
                 ontwerpnetwerk.groepen.add(firewall);
                 OntwerpFrame ontwerpFrame = new OntwerpFrame(ontwerpnetwerk);
                }

                //wanneer je een al bestaand ontwerpnetwerk wilt optimaliseren, wordt het bestaande ontwerpnetwerk geüpdatet
                if(ontwerpFrame.getOntwerpnetwerk() != null && ontwerpFrame.isGedruktOpOptimalisatieknop()) {
                    ontwerpFrame.getOntwerpnetwerk().setKosten(Double.parseDouble(totaleBedrag.getText()));
                    ontwerpFrame.getOntwerpnetwerk().setOpgegevenBeschikbaarheid(Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText()));
                    ontwerpFrame.getOntwerpnetwerk().setBeschikbaarheidspercentage(Double.parseDouble(beschikbaarheidspercentage.getText()));
                    ontwerpFrame.getOntwerpnetwerk().groepen.set(0, databasegroep);
                    ontwerpFrame.getOntwerpnetwerk().groepen.set(1, webservergroep);
                    ontwerpFrame.getOntwerpnetwerk().groepen.set(2, firewall);
                    ontwerpFrame.setGedruktOpOptimalisatieknop(false);
                    Ontwerpnetwerk geoptimaliseerdOntwerpnetwerk = ontwerpFrame.getOntwerpnetwerk();
                    ontwerpFrame.dispose();
                    OntwerpFrame ontwerpFrame = new OntwerpFrame(geoptimaliseerdOntwerpnetwerk);
                }

                dispose();
            }
        } catch(NumberFormatException nfe){
                minimaalTotaleBeschikbaarheid.setText("<html>Minimaal totaal beschikbaarheidspercentage: <br> <font color = 'red'> Voer een percentage in (gebruik een punt)!<font/><html/>");
                // melding dat er een percentage ingevuld moet worden
        }
    }
}