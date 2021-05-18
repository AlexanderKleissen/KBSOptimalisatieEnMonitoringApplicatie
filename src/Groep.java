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

    public double getBeschikbaarheidspercentage() {
        return beschikbaarheidspercentage;
    }

    public String getType() {
        return type;
    }

//    public static double berekenBeschikbaarheidsPercentageNetwerk(Netwerk netwerk) {
//        for (Groep groep : netwerk.groepen) {
//            ArrayList<Monitoringcomponent> monitoringcomponents = new ArrayList<>();
//            ArrayList<Double> databasesserverBeschikbaarheidArrayList = new ArrayList<Double>();
//            ArrayList<Double> webserversBeschikbaarheidArrayList = new ArrayList<Double>();
//            ArrayList<Double> firewallBeschikbaarheidArrayList = new ArrayList<Double>();
//            double totaleDatabaseserversBeschikbaarheid;
//
//            String formule = "";
//            for (Component component : groep.componenten) {
//                if (component instanceof Monitoringcomponent) {
//                    monitoringcomponents.add((Monitoringcomponent) component);
//                }
//            }
//            if (groep.getType().equals("Databaseservers")) {
//                for (Component component : monitoringcomponents) {
//                    double databasePercentage = Double.parseDouble(component.getBeschikbaarheidspercentage());
//                    databasesserverBeschikbaarheidArrayList.add(databasePercentage);
////                    if (monitoringcomponents.size() == databasesserverBeschikbaarheidArrayList.size()) {
////                        formule += "(1-" +databasePercentage + ")";
////                    } else {
////                        formule += "(1-" +databasePercentage + ")*";
////                    }
////                    System.out.println(monitoringcomponents.size());
//////                    System.out.println(formule.substring(formule.length() + 1));
////                    System.out.println(formule);
//                }
//            }
//            int databasesserverBeschikbaarheidArrayListAantal = 0;
//            for (Double databasesserverBeschikbaarheid : databasesserverBeschikbaarheidArrayList) {
//                if (databasesserverBeschikbaarheidArrayList.size() != databasesserverBeschikbaarheidArrayListAantal) {
//                    totaleDatabaseserversBeschikbaarheid = (1 - databasesserverBeschikbaarheid);
//                }
//            }
//
//            if (groep.getType().equals("Webservers")) {
//                for (Component component : monitoringcomponents) {
//                    webserversBeschikbaarheidArrayList.add(Double.parseDouble(component.getBeschikbaarheidspercentage()));
////                    System.out.println(component.getBeschikbaarheidspercentage());
//                }
//            }
//            if (groep.getType().equals("Firewall")) {
//                for (Component component : monitoringcomponents) {
//                    firewallBeschikbaarheidArrayList.add(Double.parseDouble(component.getBeschikbaarheidspercentage()));
////                    System.out.println(component.getBeschikbaarheidspercentage());
//                }
//            }
//            double databasesserverBeschikbaarheid = 0;
//        }
//        return 1;
//    }

//    public static double berekenBeschikbaarheidGroep(Netwerk netwerk, String type) {
//        for (Groep groep : netwerk.groepen) {
//            ArrayList<Monitoringcomponent> monitoringcomponents = new ArrayList<>();
//            ArrayList<Double> beschikbaarheidArrayList = new ArrayList<Double>();
//            for (Component component : groep.componenten) {
//                if (component instanceof Monitoringcomponent) {
//                    monitoringcomponents.add((Monitoringcomponent) component);
//                }
//            }
//            int arraylisttest = 0;
//            double A = 0;
//            double B = 0;
//            double C = 0;
//            if (groep.getType().equals(type)) {
//                for (Component component : monitoringcomponents) {
//                    double databasePercentage = Double.parseDouble(component.getBeschikbaarheidspercentage());
//                    beschikbaarheidArrayList.add(databasePercentage);
//                    if (A == 0) {
//                        A = (Double.parseDouble(component.getBeschikbaarheidspercentage()) / 100);
//                        System.out.println(A);
//                    } else if (A != 0 && B == 0) {
//                        B = (Double.parseDouble(component.getBeschikbaarheidspercentage()) / 100);
//                    } else if (A != 0 && B != 0) {
//                        C = (1-A)*(1-B);
//                        A = 0;
//                        B = 0;
//                        System.out.println(C);
//                    }
//                }
//            }
////                for (Double databasepercentage: beschikbaarheidArrayList) {
//////                    System.out.println(databasepercentage);
////                    }
//        }
//
//        return 0;
//    }
}

