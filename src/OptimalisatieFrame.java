import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptimalisatieFrame extends JFrame implements ActionListener {
    private JLabel minimaalTotaleBeschikbaarheid ;
    private JTextField jtminimaalTotaleBeschikbaarheid;
    private JButton bereken;
    private JButton voegToe;


    public OptimalisatieFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLayout(new BorderLayout());
        setTitle("Monitoringapplicatie (optimaliseren)");


       //Boveste twee labels:
       Border zwarteRand = BorderFactory.createLineBorder(Color.BLACK,1);                   //zwarte rand voor de bovenste twee labels
       Dimension dimensie = new Dimension(700,100);                                     // hoogte en breedte voor de bovenste twee labels
       JLabel mogelijkeComponenten = new JLabel("Specificaties mogelijke ontwerpcomponenten");
       mogelijkeComponenten.setBorder(zwarteRand);
       mogelijkeComponenten.setPreferredSize(dimensie);

       JLabel optimaleWaardenOntwerp = new JLabel("Optimale waarden ontwerp");
       optimaleWaardenOntwerp.setBorder(zwarteRand);
       optimaleWaardenOntwerp.setPreferredSize(dimensie);


       //Linker tabel:
       String[] kolomnamen = {"Componenten", "Kosten", "Beschikbaarheid", "Totale Diskruimte"};     //een array met kolomnamen
       Object[][] data = {{"Component1", "€100", "99.98%", "5Gb"}, {"Component2","€150","99.12%","7.5Gb"}};  //voorbeelddata in een tweedimensionale array (matrix)
                                                                                                    /*Een array van het type Object maakt het mogelijk meerdere
                                                                                                      soorten data op te slaan in de array.*/
       JTable tabel1 = new JTable(data,kolomnamen);                                                 // kolomnamen en data meegeven aan de tabel
        tabel1.setEnabled(false);                                                                   // data kan nu niet door gebruiker aangepast worden

        //Van kolommen in tabel1 een TableColumnobject maken en vervolgens de gewenste breedte meegeven:
        TableColumn Tabel1kolom1 = tabel1.getColumnModel().getColumn(0);                 // kolom 1
        Tabel1kolom1.setPreferredWidth(174);

        TableColumn Tabel1kolom2 = tabel1.getColumnModel().getColumn(1);                 // kolom 2
        Tabel1kolom2.setPreferredWidth(88);

        TableColumn Tabel1kolom3 = tabel1.getColumnModel().getColumn(2);                 // kolom 3
        Tabel1kolom3.setPreferredWidth(219);

        TableColumn Tabel1kolom4 = tabel1.getColumnModel().getColumn(3);                 // kolom 4
        Tabel1kolom4.setPreferredWidth(219);

        Dimension dimensieScrollPaneTabel1en2 = new Dimension(700,500);                // vaste hoogte en breedte voor de scrollPane (container voor de tabel)
        JScrollPane scrollPaneTabel1 = new JScrollPane(tabel1);
        scrollPaneTabel1.setPreferredSize(dimensieScrollPaneTabel1en2);
        tabel1.setFillsViewportHeight(true);                                                       //tabel vult de hele scrollPane ongeacht aantal rijen


       //rechter tabel:
       String[] kolomnamen2 = {"Componenten", "Kosten", "Beschikbaarheid", "Totale Diskruimte", "aantal"};
       Object[][] data2 = new Object[][] {{"Component1", "€100", "99.98%", "5Gb", 4}, {"Component2","€150","99.12%","7.5Gb", 3}};
       JTable tabel2 = new JTable(data2,kolomnamen2);
       tabel2.setEnabled(false);

       //kolommen in tabel2 de gewenste breedte geven:
        TableColumn Tabel2kolom1 = tabel2.getColumnModel().getColumn(0);                // kolom 1
        Tabel2kolom1.setPreferredWidth(174);

        TableColumn Tabel2kolom2 = tabel2.getColumnModel().getColumn(1);                // kolom 2
        Tabel2kolom2.setPreferredWidth(88);

        TableColumn Tabel2kolom3 = tabel2.getColumnModel().getColumn(2);                // kolom 3
        Tabel2kolom3.setPreferredWidth(175);

        TableColumn Tabel2kolom4 = tabel2.getColumnModel().getColumn(3);                // kolom 4
        Tabel2kolom4.setPreferredWidth(175);

        TableColumn Tabel2kolom5 = tabel2.getColumnModel().getColumn(4);                // kolom 5
        Tabel2kolom5.setPreferredWidth(88);

        JScrollPane scrollPaneTabel2 = new JScrollPane(tabel2);
        scrollPaneTabel2.setPreferredSize(dimensieScrollPaneTabel1en2);
        tabel2.setFillsViewportHeight(true);


        //Controls van JPanel links onderin
       minimaalTotaleBeschikbaarheid = new JLabel("Minimaal totaal beschikbaarheidspercentage:");

       jtminimaalTotaleBeschikbaarheid = new JTextField(7);

       bereken = new JButton("Bereken optimale waarden");
       bereken.addActionListener(this);                                                        //knop handelt ingevoerd percentage af in 'jtminimaalTotaleBeschikbaarheid'

       //Jpanel links onderin
       JPanel panelLinksOnder = new JPanel();
       panelLinksOnder.setLayout(new GridBagLayout());                                           //gridbaglayout

       //GridBagConstraints aanmaken (bestaat uit kolommen en rijen) en vervolgens aan de controls in het panel koppelen om ze de gewenste grootte en positie te geven
       GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
       gridBagConstraints1.gridx = 0;                                                            // control bevindt zich in eerste kolom
       gridBagConstraints1.gridy = 0;                                                            // control bevindt zich in eerste rij

       GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
       gridBagConstraints2.gridx = 1;                                                            // control bevindt zich in de tweede kolom
       gridBagConstraints2.gridy = 0;                                                            // control bevindt zich in de eerste rij
       gridBagConstraints2.weightx = 0.1;                                                        // zorgt ervoor dat de controls niet 'vastgeplakt' zitten aan elkaar
       gridBagConstraints2.anchor = GridBagConstraints.WEST;                                     // control wordt links geplaatst in de cel
       gridBagConstraints2.insets = new Insets(0,5,0,0);                    // padding tussen control en cel voor de linkerkant

       GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
       gridBagConstraints3.anchor = GridBagConstraints.LAST_LINE_END;                            // control bevindt zich rechts onderin de cel
       gridBagConstraints3.gridx = 2;                                                            // control bevindt zich in derde kolom
       gridBagConstraints3.gridy = 2;                                                            // control bevindt zich in derde rij
       gridBagConstraints3.weighty = 1.0;                                                        // control krijgt extra verticale ruimte
       gridBagConstraints3.insets = new Insets(0,0,5,5);                    // padding tussen control en cel voor de onder- en rechterkant

       panelLinksOnder.add(minimaalTotaleBeschikbaarheid, gridBagConstraints1);                  // gemaakte label toevoegen
       panelLinksOnder.add(jtminimaalTotaleBeschikbaarheid, gridBagConstraints2);                // gemaakte tekstveld toevoegen
       panelLinksOnder.add(bereken, gridBagConstraints3);                                        // gemaakte knop toevoegen
       panelLinksOnder.setPreferredSize(dimensie);
       panelLinksOnder.setBorder(zwarteRand);

       //Controls van JPanel rechts onderin
       JLabel totaleBeschikbaarheid = new JLabel("Totale beschikbaarheid ontwerp:");

       JLabel percentage = new JLabel("~Hier komt berekende beschikbaarheid~");

       JLabel totaleKosten = new JLabel("Totale kosten ontwerp:");

       JLabel bedrag = new JLabel("~Hier komen de totale kosten~");

       voegToe = new JButton("Voeg componenten toe aan ontwerp");

       //JPanel rechts onderin
        JPanel panelRechtsOnder = new JPanel();
        panelRechtsOnder.setLayout(new GridBagLayout());

        //GridBagConstraints aanmaken (bestaat uit kolommen en rijen) en vervolgens aan de controls in het panel koppelen om ze de gewenste grootte en positie te geven
        GridBagConstraints _1 = new GridBagConstraints();
        _1.gridx = 0;                                                                            // control bevindt zich in de eerste kolom
        _1.gridy = 0;                                                                            // control bevindt zich in de eerste rij

        GridBagConstraints _2 = new GridBagConstraints();
        _2.gridx = 1;                                                                            // control bevindt zich in tweede kolom
        _2.gridy = 0;                                                                            // control bevindt zich in eerste rij
        _2.weightx = 0.1;                                                                        // zorgt ervoor dat de controls niet 'vastgeplakt' zitten aan elkaar
        _2.anchor = GridBagConstraints.WEST;                                                     // control bevindt zich links in de cel
        _2.insets = new Insets(0,10,0,0);                                   // padding tussen control en cel voor de linkerkant

        GridBagConstraints _3 = new GridBagConstraints();
        _3.anchor = GridBagConstraints.WEST;                                                     // control bevindt zich links in de cel
        _3.gridx = 0;                                                                            // control bevindt zich in eerste kolom
        _3.gridy = 1;                                                                            // control bevindt zich in tweede rij

        GridBagConstraints _4 = new GridBagConstraints();
        _4.anchor = GridBagConstraints.WEST;                                                     // control bevindt zich links in de cel
        _4.gridx = 1;                                                                            // control bevindt zich in tweede kolom
        _4.gridy = 1;                                                                            // control bevindt zich in tweede rij
        _4.insets = new Insets(0,10,0,0);                                   // padding tussen control en cel voor de linkerkant

        GridBagConstraints _5 = new GridBagConstraints();
        _5.anchor = GridBagConstraints.LAST_LINE_END;                                            // control bevindt zich rechts onderin de cel
        _5.gridx = 2;                                                                            // control bevindt zich in de derde kolom
        _5.gridy = 2;                                                                            // control bevindt zich in de derde rij
        _5.weighty = 1.0;                                                                        // control krijgt extra verticale ruimte
        _5.insets = new Insets(0,0,5,5);                                    // padding tussen control en cel voor de onder- en rechterkant

        panelRechtsOnder.setPreferredSize(dimensie);
        panelRechtsOnder.setBorder(zwarteRand);
        panelRechtsOnder.add(totaleBeschikbaarheid,_1);                                          // gemaakte label toevoegen
        panelRechtsOnder.add(percentage,_2);                                                     // gemaakte label toevoegen
        panelRechtsOnder.add(totaleKosten,_3);                                                   // gemaakte label toevoegen
        panelRechtsOnder.add(bedrag,_4);                                                         // gemaakte label toevoegen
        panelRechtsOnder.add(voegToe, _5);                                                       // gemaakte knop toevoegen


        //de labels, tabellen en panels toevoegen aan een panel
        //Hierdoor is het mogelijk het panel aan een scrollPane toe te voegen en het scherm scrollbaar te maken wanneer de gebruiker de grootte van het scherm verandert.
        Dimension dimensiePanelScherm = new Dimension(1500,700);
        JPanel panelScherm = new JPanel();
        panelScherm.add(mogelijkeComponenten);
        panelScherm.add(optimaleWaardenOntwerp);
        panelScherm.add(scrollPaneTabel1);
        panelScherm.add(scrollPaneTabel2);
        panelScherm.add(panelLinksOnder);
        panelScherm.add(panelRechtsOnder);
        panelScherm.setPreferredSize(dimensiePanelScherm);

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
        try{
            if(e.getSource().equals(bereken)) {
                double ingevoerdPercentage = Double.parseDouble(jtminimaalTotaleBeschikbaarheid.getText()); // ingevoerd percentage wordt van String naar Double omgezet
                System.out.println(ingevoerdPercentage);                                          // voor nu even om te testen of ingevoerd percentage opgehaald kan worden
            }
        } catch (NumberFormatException nfe) {
            minimaalTotaleBeschikbaarheid.setText("<html>Minimaal totaal beschikbaarheidspercentage: <br> <font color = 'red'> Voer een percentage in (gebruik een punt)!<font/><html/>");
            // melding dat er een percentage ingevuld moet worden
        }
        }
}
