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

    public static double berekenBeschikbaarheidNetwerk(Netwerk netwerk) {
        double beschikbaarheidDatabaseservers = berekenBeschikbaarheidGroep(netwerk, "Databaseservers");
        double beschikbaarheidWebservers = berekenBeschikbaarheidGroep(netwerk, "Databaseservers");
        double beschikbaarheidFirewall = berekenBeschikbaarheidGroep(netwerk, "Firewall");
        double beschikbaarheidNetwerk = 100*(beschikbaarheidDatabaseservers*beschikbaarheidFirewall*beschikbaarheidWebservers);
        return beschikbaarheidNetwerk;
    }

    public static double berekenBeschikbaarheidGroep(Netwerk netwerk, String type) {
        for (Groep groep : netwerk.groepen) {
            ArrayList<Monitoringcomponent> monitoringcomponents = new ArrayList<>();
            ArrayList<Double> beschikbaarheidArrayList = new ArrayList<Double>();

            for (Component component : groep.componenten) {
                if (component instanceof Monitoringcomponent) {
                    monitoringcomponents.add((Monitoringcomponent) component);
                }
            }

            if (groep.getType().equals(type)) {
                for (Component component : monitoringcomponents) {
                    double beschikbaarheidComponent = (Double.parseDouble(component.getBeschikbaarheidspercentage()) / 100);
                    beschikbaarheidArrayList.add((1 - beschikbaarheidComponent));
                }
                double d = beschikbaarheidArrayList.get(0);
                for (int i = 0; i < beschikbaarheidArrayList.size(); i++) {
                    if (i + 1 != beschikbaarheidArrayList.size()) {
                        d = d * beschikbaarheidArrayList.get(i + 1);
                    } else {
                        d = 1 - d;
                        return d;
                    }
                }
            }
        }
        return 0;
    }
}

