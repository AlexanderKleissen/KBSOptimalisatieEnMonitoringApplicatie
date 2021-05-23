import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Groep {
    private String type;
    private double beschikbaarheidspercentage;
    protected ArrayList<Component> componenten;


    //Constructors
    public Groep(String type, double beschikbaarheidspercentage) {
        this.type = type;
        this.beschikbaarheidspercentage = beschikbaarheidspercentage;
        this.componenten = new ArrayList();
    }


    public String getType() {
        return type;
    }

    //methode voor het berekenen van de beschikbaarheid over heel het gegeven netwerk
    public static double berekenBeschikbaarheidNetwerk(Netwerk netwerk) {
        //beschikbaarheid per netwerkdeel berekenen
        double beschikbaarheidDatabaseservers = berekenBeschikbaarheidGroep(netwerk, "Databaseservers");
        double beschikbaarheidWebservers = berekenBeschikbaarheidGroep(netwerk, "Webservers");
        double beschikbaarheidFirewall = berekenBeschikbaarheidGroep(netwerk, "Firewall");

        //beschikbaarheid van het netwerk berekenen en returnen
        double beschikbaarheidNetwerk = (beschikbaarheidDatabaseservers*beschikbaarheidFirewall*beschikbaarheidWebservers)/10000;
        return beschikbaarheidNetwerk;
    }

    //methode voor het berekenen van de beschikbaarheid over een deel van het netwerk door middel van de onderstaande formule
    //beschikbaarheid = 1-(1-beschikbaarheid A) x (1-beschikbaarheid B)…x (1-beschikbaarheid n):
    //in de constructor kan "String type" het volgende zijn: Databaseservers, Webservers of Firewall
    public static double berekenBeschikbaarheidGroep(Netwerk netwerk, String type) {
        for (Groep groep : netwerk.groepen) {
            ArrayList<Monitoringcomponent> monitoringcomponents = new ArrayList<>();
            ArrayList<Double> beschikbaarheidArrayList = new ArrayList<>();

            //alle componenten die binnen groep.componenten staan en een Monitoringscomponent zijn worden aan de ArrayList toegevoegd
            for (Component component : groep.componenten) {
                if (component instanceof Monitoringcomponent) {
                    monitoringcomponents.add((Monitoringcomponent) component);
                }
            }

            //voegt alle (1-beschikbaarheid) van de componenten in de ArrayList
            if (groep.getType().equals(type)) {
                for (Component component : monitoringcomponents) {
                    double beschikbaarheidComponent = (Double.parseDouble(component.getBeschikbaarheidspercentage().replaceAll(",", ".")) / 100);
                    beschikbaarheidArrayList.add((1 - beschikbaarheidComponent));
                }

                //vermenigvuldigd alle beschikbaarheden binnen de ArrayList met elkaar
                //het resultaat van de vermenigvuldigde wordt van 1 afgetrokken en dat is dan de beschikbaarheid van de groep
                double d = beschikbaarheidArrayList.get(0);
                for (int i = 0; i < beschikbaarheidArrayList.size(); i++) {
                    if (i + 1 != beschikbaarheidArrayList.size()) {
                        d = d * beschikbaarheidArrayList.get(i + 1);
                    } else {
                        d = (1 - d)*100;
                        return d;
                    }
                }
            }
        }
        return 0; //standaard return
    }
}