import java.sql.*;
import java.text.DecimalFormat;

public class Ontwerpcomponent extends Component {
    private String kosten;

    //Constructors
    public Ontwerpcomponent(String naam, String type, double kosten, double beschikbaarheidspercentage) {
        super(naam, type, beschikbaarheidspercentage);
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
    }

    public Ontwerpcomponent(String naam, String officieleNaam, String type, double kosten, double beschikbaarheidspercentage) throws SQLException{
        super(naam, type, beschikbaarheidspercentage);
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
        Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
        boolean insertGelukt = statement.execute("insert into component_ontwerpen values ('" + naam + "', '" + type + "', '" + kosten + "' , '" + beschikbaarheidspercentage + "')");

        if(insertGelukt) {
            System.out.println("gelukt");
            connection.close();
        }
    }

    //Getters en setters


    public String getKosten() {
        return kosten;
    }
}