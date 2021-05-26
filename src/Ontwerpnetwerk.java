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

    private void naarDatabase(double beschikbaarheid, double kosten) throws SQLException {
        ArrayList<Ontwerpcomponent> componentenNetwerk = new ArrayList<>();
        ArrayList<String> netwerkComponentenUitTabel = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt

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