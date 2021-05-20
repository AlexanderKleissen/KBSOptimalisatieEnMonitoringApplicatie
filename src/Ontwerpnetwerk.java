import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Ontwerpnetwerk extends Netwerk{
    private String kosten;
    private double opgegevenBeschikbaarheid;
    private static ArrayList<Ontwerpnetwerk> ontwerpNetwerken =  new ArrayList<>();

    //Constructors
    public Ontwerpnetwerk(String naam, double kosten, double opgegevenBeschikbaarheid, double beschikbaarheidspercentage) {
        super(naam, beschikbaarheidspercentage, new ArrayList<>());
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
        this.opgegevenBeschikbaarheid = opgegevenBeschikbaarheid;
        groepen = new ArrayList<>();
        ontwerpNetwerken.add(this);
    }

    public Ontwerpnetwerk(String naam, double opgegevenBeschikbaarheid){
        super (naam, opgegevenBeschikbaarheid, new ArrayList<>());
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

    public void setOpgegevenBeschikbaarheid(double opgegevenBeschikbaarheid) {
        this.opgegevenBeschikbaarheid = opgegevenBeschikbaarheid;
    }

    //omdat we groepen gebruiken, kun je de kosten en beschikbaarheid pas achteraf berekenen en in database zetten, dus dat wordt hier gedaan
    public void setCorrecteKostenEnBeschikbaarheid() throws SQLException {
        double beschikbaarheidDB=100, beschikbaarheidFirewall=100, beschikbaarheidWS=100, kosten=0, beschikbaarheidGroep = 1;
        String netwerk = this.getNaam();
        ArrayList<Ontwerpcomponent> componentenNetwerk = new ArrayList<>();

        for(Groep groep: groepen) {
            if (groep.componenten.size() == 1) { //als er maar 1 van is, is de beschikbaarheid van de groep
                if(groep.componenten.get(0) instanceof Ontwerpcomponent){
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
                    if(component instanceof Ontwerpcomponent){
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

        for (Ontwerpcomponent component: componentenNetwerk) {
            kosten += Double.parseDouble(component.getKosten().replaceAll(",", "."));

//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
//            Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
//            boolean insertGelukt = statement.execute("insert into ontwerpnetwerk (NaamNetwerk, NaamComponent, AantalGebruikt) values " +
//                    "('" + netwerk + "', '" + component.getNaam() + "', " + 1 + ")");
//            //officieel moet dit ergens anders, want dan moet er eerst worden geteld hoeveel van ieder component het netwerk heeft
//
//            if(insertGelukt) {
//                System.out.println("gelukt" + component.getNaam());
//            }
        }

        double beschikbaarheid = (beschikbaarheidDB * beschikbaarheidFirewall * beschikbaarheidWS)/10000;
//        System.out.printf("%f\n%f\n%f\n%f\n$%f", beschikbaarheidDB, beschikbaarheidWS, beschikbaarheidFirewall, beschikbaarheid,kosten);
        setBeschikbaarheidspercentage(beschikbaarheid);
        setKosten(kosten);

//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
//        Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
//        boolean insertGelukt = statement.execute(
//                "insert into ontwerpnetwerk (Beschikbaarheid, Kosten) values ("+ beschikbaarheid + ", "+ kosten +") where NaamNetwerk = '" + netwerk + "'");
//
//        if(insertGelukt) {
//            System.out.println("gelukt");
//            connection.close();
//        }
    }
}