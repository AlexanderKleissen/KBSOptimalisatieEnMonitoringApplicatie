import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Ontwerpnetwerk extends Netwerk {
    private String kosten;
    private static ArrayList<Ontwerpnetwerk> ontwerpNetwerken = new ArrayList<>();

    //Constructors

    public Ontwerpnetwerk(String naam, double kosten, double beschikbaarheidspercentage) throws SQLException {
        super(naam, beschikbaarheidspercentage, new ArrayList<>());
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
        groepen = new ArrayList<>();
        ontwerpNetwerken.add(this);
    //    naarDatabase(beschikbaarheidspercentage, kosten);
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

    public static void uitDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://11.11.20.100:3306/nerdygadgets", "root", "m2okbsd1");
        Statement statement = connection.createStatement();
        ResultSet rs2 = statement.executeQuery("SELECT * FROM ontwerpnetwerk");

        Ontwerpnetwerk ontwerpnetwerk = null;

        Groep firewallgroep = new Groep("firewallgroep");
        Groep webservergroep = new Groep("webservergroep");
        Groep databasegroep = new Groep("databasegroep");

        while (rs2.next()) {
            String naamNetwerk = rs2.getString("NaamNetwerk") + "%";
            Double kosten = rs2.getDouble("Kosten");
            Double beschikbaarheid = rs2.getDouble("Beschikbaarheid");

            ArrayList<Ontwerpnetwerk> bestaatAl = new ArrayList<>();
            for(Ontwerpnetwerk ontwerpnetwerk2 : Ontwerpnetwerk.getOntwerpNetwerken()) {
                if(ontwerpnetwerk2.getNaam().equals(naamNetwerk)) {
                    bestaatAl.add(ontwerpnetwerk2);
                }
            }

            if(bestaatAl.size() == 0) {
                ontwerpnetwerk = new Ontwerpnetwerk(naamNetwerk, kosten, beschikbaarheid);
            }

            while (rs2.getString("NaamNetwerk").equals(ontwerpnetwerk.getNaam())) {
                for (Ontwerpcomponent ontwerpcomponent : Ontwerpcomponent.getOntwerpcomponenten()) {
                    if (ontwerpcomponent.getNaam().equals(rs2.getString("NaamComponent"))) {
                        for (int i = 0; i < rs2.getInt("AantalGebruikt"); i++) {
                            if (ontwerpcomponent.getType().equals("Firewall")) {
                                firewallgroep.componenten.add(ontwerpcomponent);

                            }
                             if (ontwerpcomponent.getType().equals("Webserver")) {
                                               webservergroep.componenten.add(ontwerpcomponent);

                             }

                             if (ontwerpcomponent.getType().equals("Database")) {
                                 databasegroep.componenten.add(ontwerpcomponent);
                             }
                        }
                    }
            }

        }

//        connection.close();
//        statement.close();

        }
    }

//        Groep firewallgroep = new Groep("firewallgroep");
//        Groep webservergroep = new Groep("webservergroep");
//        Groep databasegroep = new Groep("databasegroep");
//
//
//        String naamNetwerk;
//        Double kosten;
//        Double beschikbaarheid;


//        while (rs2.next()) {
//            naamNetwerk = rs2.getString("NaamNetwerk") + "%";
//            kosten = rs2.getDouble("Kosten");
//            beschikbaarheid = rs2.getDouble("Beschikbaarheid");
//
//            for (Ontwerpnetwerk ontwerpnetwerk2 : Ontwerpnetwerk.getOntwerpNetwerken()) {
//                if (ontwerpnetwerk2.getNaam().equals(naamNetwerk)) {
//                    naamNetwerk = null;
//                    kosten = 0.00;
//                    beschikbaarheid = 0.000;
//                }
//            }
//
//            if (naamNetwerk != null) {
//                Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk(naamNetwerk, kosten, beschikbaarheid);
//                ontwerpnetwerk.groepen.add(firewallgroep);
//                ontwerpnetwerk.groepen.add(webservergroep);
//                ontwerpnetwerk.groepen.add(databasegroep);
//            }
//
//
//
//                for (Ontwerpcomponent ontwerpcomponent : Ontwerpcomponent.getOntwerpcomponenten()) {
//                    if (ontwerpcomponent.getNaam().equals(rs2.getString("NaamComponent"))) {
//                        for (int i = 0; i < rs2.getInt("AantalGebruikt"); i++) {
//                            if (ontwerpcomponent.getType().equals("firewall")) {
//                                firewallgroep.componenten.add(ontwerpcomponent);
//
//                            }
//
//                            if (ontwerpcomponent.getType().equals("webserver")) {
//                                webservergroep.componenten.add(ontwerpcomponent);
//
//                            }
//
//                            if (ontwerpcomponent.getType().equals("database")) {
//                                databasegroep.componenten.add(ontwerpcomponent);
//                            }
//                        }
//                    }
//
//            }
//        }
//
//
//        //                        if(Ontwerpnetwerk.getOntwerpNetwerken().size() > 0) {
//        //                        for(Ontwerpnetwerk ontwerpnetwerk: Ontwerpnetwerk.getOntwerpNetwerken()) {
//        //                            if (ontwerpnetwerk.getNaam().equals(ontwerpnetwerk2.getNaam())) {
//        //                                Ontwerpnetwerk.getOntwerpNetwerken().remove(ontwerpnetwerk2);
//        //                            }
//        //                        }
//
//
//        connection.close();
//        statement.close();
//    }

    private void naarDatabase(double beschikbaarheid, double kosten) throws SQLException {
        ArrayList<Ontwerpcomponent> componentenNetwerk = new ArrayList<>();
        ArrayList<String> netwerkComponentenUitTabel = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:mysql://11.11.20.100:3306/nerdygadgets", "root", "m2okbsd1"); //Verbinding met database wordt gemaakt

        for (Groep groep : groepen) { //hiermee worden alle componenten van het netwerk verzamelt
            for (Component component : groep.componenten) {
                if (component instanceof Ontwerpcomponent) {
                    Ontwerpcomponent component1 = (Ontwerpcomponent) component;
                    componentenNetwerk.add(component1);
                }
            }
        }

        for (Ontwerpcomponent component : componentenNetwerk) { //het database-deel
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select NaamComponent from ontwerpnetwerk where NaamNetwerk = '" + this.getNaam() + "'");
            while (rs.next()){
                String componentinNetwerk = rs.getString(1);
                netwerkComponentenUitTabel.add(componentinNetwerk);
            }
            rs.close();

            //als dat component nog niet in de tabel staat, moet deze worden toegevoegd, anders moet de teller worden opgehoogd
            if (!netwerkComponentenUitTabel.contains(component.getNaam())) {
                netwerkComponentenUitTabel.add(component.getNaam());
                Statement insert = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
                boolean insertGelukt = insert.execute("insert into ontwerpnetwerk values" +
                        "('" + this.getNaam() + "', '" + component.getNaam() + "', 1, " + beschikbaarheid + ", " + kosten + ")");
            } else {
                Statement update1 = connection.createStatement();
                boolean update1Gelukt = update1.execute(
                        "update ontwerpnetwerk set AantalGebruikt = AantalGebruikt+1  where NaamComponent = '" + component.getNaam() + "' AND NaamNetwerk = '" + this.getNaam() + "'");
            }
        }

        connection.close();
    }
}