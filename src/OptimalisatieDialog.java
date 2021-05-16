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
    private Color backClr1 = new Color(60, 63, 65); //achtergrondkleur
    private Color backClr2 = new Color(43, 43, 43); //tabelkleur
    private ArrayList<Ontwerpcomponent> ontwerpcomponenten = new ArrayList<>();
    private ArrayList<Ontwerpcomponent> optimaleWaarden = new ArrayList<>();

    public OptimalisatieDialog(OntwerpFrame frame) {
        super(frame, false);
        this.ontwerpFrame = frame;
        setSize(1500, 1000);
        setLayout(new BorderLayout());
        UIManager.getLookAndFeelDefaults().put("TextField.caretForeground" , Color. white);
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

        //de beschikbare ontwerpcomponenten (nu hard gecodeerd, in de toekomst uit database halen)
        Ontwerpcomponent firewallpfSense = new Ontwerpcomponent("pfSense", "firewall", 4000, 99.998);
        Ontwerpcomponent HAL9001DB = new Ontwerpcomponent("HAL9001DB", "database", 5100,  90);
        Ontwerpcomponent HAL9002DB = new Ontwerpcomponent("HAL9002DB", "database", 7700,  95);
        Ontwerpcomponent HAL9003DB = new Ontwerpcomponent("HAL9003DB", "database", 12200,  98);
        Ontwerpcomponent HAL9001W = new Ontwerpcomponent("HAL9001W", "webserver", 2200,  80);
        Ontwerpcomponent HAL9002W = new Ontwerpcomponent("HAL9002W", "webserver", 3200,  90);
        Ontwerpcomponent HAL9003W = new Ontwerpcomponent("HAL9003W", "webserver", 5100,  95);

        //De componenten toevoegen aan de arraylist van ontwerpcomponenten
        ontwerpcomponenten.add(firewallpfSense);
        ontwerpcomponenten.add(HAL9001DB);
        ontwerpcomponenten.add(HAL9002DB);
        ontwerpcomponenten.add(HAL9003DB);
        ontwerpcomponenten.add(HAL9001W);
        ontwerpcomponenten.add(HAL9002W);
        ontwerpcomponenten.add(HAL9003W);




        //Linker tabel:
        String[] kolomnamen = {"Naam component", "Type", "Kosten (€)", "Beschikbaarheid (%)"};     //een array met kolomnamen
        Object[][] data = new Object[ontwerpcomponenten.size()][4];
        /*Een array van het type Object maakt het mogelijk meerdere soorten data op te slaan in de array.*/

        // van elk bestaand ontwerpcomponent de naam, het type, de kosten en de beschikbaarheid in een tweedimensionale array zetten
        int j = 0;
        for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten) {
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

        //De optimale componenten naar aanleiding van optimalisatiefunctie toevoegen aan de arraylist van optimaleCombinatieOntwerpcomponenten
        //Nu een voorbeeld van een optimaal ontwerp met een beschikbaarheid van 99.990% gebruikt.
        optimaleWaarden.add(HAL9001DB);
        optimaleWaarden.add(HAL9001DB);
        optimaleWaarden.add(HAL9001DB);
        optimaleWaarden.add(HAL9002DB);
        optimaleWaarden.add(HAL9001W);
        optimaleWaarden.add(HAL9002W);
        optimaleWaarden.add(HAL9002W);
        optimaleWaarden.add(HAL9002W);
        optimaleWaarden.add(HAL9002W);
        optimaleWaarden.add(firewallpfSense);


        //rechter tabel:
        String[] kolomnamen2 = {"Naam component", "Type", "Kosten (€)", "Beschikbaarheid (%)", "Aantal"};
        Object[][] data2 = new Object[ontwerpcomponenten.size()][5];

            for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten) {
                int index = ontwerpcomponenten.indexOf(ontwerpcomponent);
                int k = 0;
                for (Ontwerpcomponent ontwerpcomponent2 : optimaleWaarden) {
                    if (ontwerpcomponent2.equals(ontwerpcomponent)) {
                        k++;
                        data2[index][0] = ontwerpcomponent.getNaam();
                        data2[index][1] = ontwerpcomponent.getType();
                        data2[index][2] = ontwerpcomponent.getKosten();
                        data2[index][3] = ontwerpcomponent.getBeschikbaarheidspercentage();
                        data2[index][4] = k;
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

        beschikbaarheidspercentage = new JLabel("99,990");          //  voorbeelddata, echte data volgt uit optimalisatiefunctie
        beschikbaarheidspercentage.setForeground(Color.white);

        JLabel totaleKosten = new JLabel("Totale kosten ontwerp (€):");
        totaleKosten.setForeground(Color.white);

        totaleBedrag = new JLabel("42000,00");                       // voorbeelddata, echte data volgt uit optimalisatiefunctie
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

    private double berekenBeschikbaarheid(ArrayList<Ontwerpcomponent> firewalls, ArrayList<Ontwerpcomponent> webservers, ArrayList<Ontwerpcomponent> dbservers){
        double beschikbaarheidTotaal =1;
        double beschikbaarheidFirewalls =1;
        double beschikbaarheidWebservers =1;
        double beschikbaarheidDbservers =1;
        for(Ontwerpcomponent firewall : firewalls){
            beschikbaarheidFirewalls = beschikbaarheidFirewalls * (1-Double.parseDouble(firewall.getBeschikbaarheidspercentage().replaceAll(",", ".")));
        } beschikbaarheidFirewalls = 1 - beschikbaarheidFirewalls;
        for(Ontwerpcomponent webserver : webservers){
            beschikbaarheidWebservers = beschikbaarheidWebservers * (1-Double.parseDouble(webserver.getBeschikbaarheidspercentage().replaceAll(",", ".")));
        } beschikbaarheidWebservers = 1 - beschikbaarheidWebservers;
        for(Ontwerpcomponent dbserver : dbservers){
            beschikbaarheidDbservers = beschikbaarheidDbservers * (1-Double.parseDouble(dbserver.getBeschikbaarheidspercentage().replaceAll("," ,".")));
        } beschikbaarheidDbservers = 1 - beschikbaarheidDbservers;
        beschikbaarheidTotaal = beschikbaarheidFirewalls * beschikbaarheidWebservers * beschikbaarheidDbservers;
        return beschikbaarheidTotaal;
    }

    private void optimaliseer(double minBeschikbaarheidspercentage){
        /* Backtrackingalgoritme optimalisatie */
        int kosten = 0;
        int laagsteKosten = 0;
        double beschikbaarheid = 0;
        ArrayList<Ontwerpcomponent> besteOplossing = new ArrayList<>();
        ArrayList<Ontwerpcomponent> huidigeOplossing = new ArrayList<>();
        int component = 0;
        ArrayList<Ontwerpcomponent> firewalls = new ArrayList<>();
        ArrayList<Ontwerpcomponent> webservers = new ArrayList<>();
        ArrayList<Ontwerpcomponent> dbservers = new ArrayList<>();

        //while(){
            //false: Controle om te kijken of de beschikbaarheid stijgt als een component wordt toegevoegd.
            while(beschikbaarheid < minBeschikbaarheidspercentage) {
                huidigeOplossing.add(ontwerpcomponenten.get(component)); //een nieuw component wordt toegevoegd
                //het component wordt ook aan de juiste categorie toegevoegd
                if (ontwerpcomponenten.get(component).getType().equals("firewall")) {
                    firewalls.add(ontwerpcomponenten.get(component));
                } else if (ontwerpcomponenten.get(component).getType().equals("database")) {
                    dbservers.add(ontwerpcomponenten.get(component));
                } else if (ontwerpcomponenten.get(component).getType().equals("webserver")) {
                    webservers.add(ontwerpcomponenten.get(component));
                }//true: component toevoegen en volgende component gebruiken
                if (berekenBeschikbaarheid(firewalls, dbservers, webservers) > beschikbaarheid) { //als de beschikbaarheid met het nieuwe component hoger is dan zonder, worden de beschikbaarheid en kosten aangepast.
                    beschikbaarheid = berekenBeschikbaarheid(firewalls, dbservers, webservers);
                    kosten += Integer.parseInt(ontwerpcomponenten.get(component).getKosten());
                }//false: component weghalen en volgende component gebruiken
                else {//als de beschikbaarheid met het nieuwe component niet hoger is dan zonder, wordt het component verwijderd
                    huidigeOplossing.add(ontwerpcomponenten.remove(component)); //het nieuwe component wordt weer verwijderd
                    //het component wordt ook bij zijn categorie verwijderd
                    if (ontwerpcomponenten.get(component).getType().equals("firewall")) {
                        firewalls.add(ontwerpcomponenten.remove(component));
                    } else if (ontwerpcomponenten.get(component).getType().equals("database")) {
                        dbservers.add(ontwerpcomponenten.remove(component));
                    } else if (ontwerpcomponenten.get(component).getType().equals("webserver")) {
                        webservers.add(ontwerpcomponenten.remove(component));
                    }
                }
                if(component<ontwerpcomponenten.size()){
                    component++;
                }else{
                    component = 0;
                }
            }//Controle om te kijken of de beschikbaarheid hoog genoeg is.
            if (beschikbaarheid >= minBeschikbaarheidspercentage && firewalls.size()>=1 && webservers.size()>=2 && dbservers.size()>=2) {
                //Dit is dan een mogelijke oplossing
                if (kosten < laagsteKosten) {//als de oplossing goedkoper is dan de vorige opgeslagen oplossing, wordt deze opgeslagen
                    besteOplossing = huidigeOplossing;
                    laagsteKosten = kosten;
                    kosten =0;
                    huidigeOplossing.clear();
                }
            }
        //}
        //true: componenten toevoegen aan het optimalisatiescherm aan de rechterkant.

        //einde backtrackingalgoritme
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        minimaalTotaleBeschikbaarheid.setText("Minimaal totaal beschikbaarheidspercentage:");     // standaard labeltekst zonder melding
        try {
            if (e.getSource() == bereken) {
                // ingevoerd percentage wordt van String naar Double omgezet
                double ingevoerdPercentage = Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText().replaceAll(",", "."));
                // voor nu even om te testen of ingevoerd percentage opgehaald kan worden
                System.out.println(ingevoerdPercentage);

                //backtracking algoritme gebruiken om optimale waarden te bepalen
               // optimaliseer(ingevoerdPercentage);
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
                       if (ontwerpnetwerk1.getBeschikbaarheidspercentage().replaceAll(",", ".").equals(beschikbaarheidspercentage.getText().replaceAll(",", "."))) {
                           ontwerpNetwerkMetDezelfdeBeschikbaarheid = ontwerpnetwerk1;
                       }
                   }

                   // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt er een nieuw ontwerpnetwerk gemaakt
                       if (ontwerpNetwerkMetDezelfdeBeschikbaarheid == null) {
                           //nieuw ontwerpnetwerk aanmaken
                           // kosten en beschikbaarheidspercentage zijn bekend naar aanleiding van optimalisatiefunctie, nu voorbeelddata gebruikt
                           //ontwerpnetwerk moet in database opgeslagen worden
                           Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk("Ontwerpnetwerk " + beschikbaarheidspercentage.getText() + "%",
                                   Double.parseDouble(totaleBedrag.getText().replaceAll(",",  ".")),
                                   Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText().replaceAll(",", ".")),
                                   Double.parseDouble(beschikbaarheidspercentage.getText().replaceAll(",", ".")));
                           ontwerpnetwerk.groepen.add(databasegroep);
                           ontwerpnetwerk.groepen.add(webservergroep);
                           ontwerpnetwerk.groepen.add(firewall);
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
                        if (ontwerpnetwerk1.getBeschikbaarheidspercentage().replaceAll(",", ".").equals(beschikbaarheidspercentage.getText().replaceAll(",", "."))) {
                            ontwerpNetwerkMetZelfdeBeschikbaarheid = ontwerpnetwerk1;
                        }
                    }

                    // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt er een nieuw ontwerpnetwerk gemaakt
                    if (ontwerpNetwerkMetZelfdeBeschikbaarheid == null) {
                        //nieuw ontwerpnetwerk aanmaken
                        // kosten en beschikbaarheidspercentage zijn bekend naar aanleiding van optimalisatiefunctie, nu voorbeelddata gebruikt
                        //ontwerpnetwerk moet in database opgeslagen worden

                        Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk("Ontwerpnetwerk " + beschikbaarheidspercentage.getText() + "%",
                                Double.parseDouble(totaleBedrag.getText().replaceAll(",", ".")),
                                Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText().replaceAll(",", ".")),
                                Double.parseDouble(beschikbaarheidspercentage.getText().replaceAll(",", ".")));
                        ontwerpnetwerk.groepen.add(databasegroep);
                        ontwerpnetwerk.groepen.add(webservergroep);
                        ontwerpnetwerk.groepen.add(firewall);
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
                        if (ontwerpnetwerk1.getBeschikbaarheidspercentage().replaceAll(",", ".").equals(beschikbaarheidspercentage.getText().replaceAll(",", "."))) {
                            ontwerpNetwerkMetZelfdeBeschikbaarheid = ontwerpnetwerk1;
                        }
                    }

                    // wanneer er niet een ontwerp met dezelfde beschikbaarheid is, wordt het ontwerpnetwerk geüpdatet
                    // ontwerpnetwerk moet in database geüpdatet worden
                    if (ontwerpNetwerkMetZelfdeBeschikbaarheid == null) {
                        ontwerpFrame.getOntwerpnetwerk().setNaam("Ontwerpnetwerk " + beschikbaarheidspercentage.getText() + "%");
                        ontwerpFrame.getOntwerpnetwerk().setKosten(Double.parseDouble(totaleBedrag.getText().replaceAll(",", ".")));
                        ontwerpFrame.getOntwerpnetwerk().setOpgegevenBeschikbaarheid(Double.parseDouble(jtMinimaalTotaleBeschikbaarheid.getText().replaceAll(",", ".")));
                        ontwerpFrame.getOntwerpnetwerk().setBeschikbaarheidspercentage(Double.parseDouble(beschikbaarheidspercentage.getText().replaceAll(",", ".")));
                        ontwerpFrame.getOntwerpnetwerk().groepen.set(0, databasegroep);
                        ontwerpFrame.getOntwerpnetwerk().groepen.set(1, webservergroep);
                        ontwerpFrame.getOntwerpnetwerk().groepen.set(2, firewall);
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
        }
    }
}