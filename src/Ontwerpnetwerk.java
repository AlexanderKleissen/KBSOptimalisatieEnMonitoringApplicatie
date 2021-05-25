import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Ontwerpnetwerk extends Netwerk {
    private String kosten;
    private static ArrayList<Ontwerpnetwerk> ontwerpNetwerken = new ArrayList<>();

    //Constructors
    public Ontwerpnetwerk(String naam, double kosten, double beschikbaarheidspercentage) {
        super(naam, beschikbaarheidspercentage, new ArrayList<>());
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
        groepen = new ArrayList<>();
        ontwerpNetwerken.add(this);
    }

    public Ontwerpnetwerk(String naam) {
        super(naam, 0, new ArrayList<>());
        groepen = new ArrayList<>();
        ontwerpNetwerken.add(this);
    }

    public Ontwerpnetwerk() {
           super(null, 0, new ArrayList<>());
           groepen = new ArrayList<>();
           ontwerpNetwerken.add(this);
       }

    //Getters en setters
    public static ArrayList<Ontwerpnetwerk> getOntwerpNetwerken() {
        return ontwerpNetwerken;
    }

    public String getKosten() {
        return kosten;
    }

    public void setKosten(double kosten) {
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
    }


    //omdat we groepen gebruiken, kun je de kosten en beschikbaarheid pas achteraf berekenen en in database zetten, dus dat wordt hier gedaan
    public void setCorrecteKostenEnBeschikbaarheid() throws SQLException {
        double beschikbaarheidDB = 100, beschikbaarheidFirewall = 100, beschikbaarheidWS = 100;
        double kosten = 0;
        double beschikbaarheidGroep = 1;

        ArrayList<Ontwerpcomponent> componentenNetwerk = new ArrayList<>();
        ArrayList<String> netwerkComponenten = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt

        //aangezien er mogelijk al oude informatie over dit netwerk in de tabel staat, moet dit eerst worden verwijderd
        Statement delete = connection.createStatement();
        boolean deleteGelukt = delete.execute("delete from ontwerpnetwerk where NaamNetwerk = '" + this.getNaam() + "'");

        for (Groep groep : groepen) { //hiermee wordt de beschikbaarheid berekent per groep
            if (groep.componenten.size() == 1) { //als er maar 1 van is, is de beschikbaarheid van de groep
                if (groep.componenten.get(0) instanceof Ontwerpcomponent) {
                    beschikbaarheidGroep = Double.parseDouble(groep.componenten.get(0).getBeschikbaarheidspercentage().replaceAll(",", "."));
                    componentenNetwerk.add((Ontwerpcomponent) groep.componenten.get(0));
                }
                if (groep.equals(groepen.get(0))) {
                    beschikbaarheidDB = beschikbaarheidGroep;
                } else if (groep.equals(groepen.get(1))) {
                    beschikbaarheidWS = beschikbaarheidGroep;
                } else {
                    beschikbaarheidFirewall = beschikbaarheidGroep;
                }
            } else {
                //als er meerdere zijn, moet je eerst (100-beschikbaarheid) voor iedere component doen en dit op het laatste aftrekken van 100
                for (Component component : groep.componenten) {
                    if (component instanceof Ontwerpcomponent) {
                        Ontwerpcomponent component1 = (Ontwerpcomponent) component;
                        beschikbaarheidGroep *= (100 - Double.parseDouble(component1.getBeschikbaarheidspercentage().replaceAll(",", ".")));
                        componentenNetwerk.add(component1);
                    }
                }
                if (groep.equals(groepen.get(0))) {
                    beschikbaarheidDB -= beschikbaarheidGroep;
                } else if (groep.equals(groepen.get(1))) {
                    beschikbaarheidWS -= beschikbaarheidGroep;
                } else {
                    beschikbaarheidFirewall -= beschikbaarheidGroep;
                }
            }
            beschikbaarheidGroep = 1;
        }

        for (Ontwerpcomponent component : componentenNetwerk) { //het database-deel en de kosten
            kosten += Double.parseDouble(component.getKosten().replaceAll(",", "."));

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select NaamComponent from ontwerpnetwerk where NaamNetwerk = '" + this.getNaam() + "'");
            while (rs.next()){
                String componentinNetwerk = rs.getString(1);
                netwerkComponenten.add(componentinNetwerk);
            }
            rs.close();

            //als dat component nog niet in de tabel staat, moet deze worden toegevoegd, anders moet de teller worden opgehoogd
            if (!netwerkComponenten.contains(component.getNaam())) {
                netwerkComponenten.add(component.getNaam());
                Statement insert = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
                boolean insertGelukt = insert.execute("insert into ontwerpnetwerk values" +
                        "('" + this.getNaam() + "', '" + component.getNaam() + "', 1, 0, 0)");
            } else {
                Statement update1 = connection.createStatement();
                boolean update1Gelukt = update1.execute(
                        "update ontwerpnetwerk set AantalGebruikt = AantalGebruikt+1  where NaamComponent = '" + component.getNaam() + "' AND NaamNetwerk = '" + this.getNaam() + "'");
            }
        }

        double beschikbaarheid = (beschikbaarheidDB * beschikbaarheidFirewall * beschikbaarheidWS) / 10000;
//        System.out.printf("%f\n%f\n%f\n%f\n%f\n", beschikbaarheidDB, beschikbaarheidWS, beschikbaarheidFirewall, beschikbaarheid,kosten);
        setBeschikbaarheidspercentage(beschikbaarheid);
        setKosten(kosten);


        Statement update2 = connection.createStatement();
        boolean update2Gelukt = update2.execute(
                "update ontwerpnetwerk set Beschikbaarheid = " + beschikbaarheid + ", Kosten = " + kosten + " where NaamNetwerk = '" + this.getNaam() + "'");

        connection.close();
    }
}