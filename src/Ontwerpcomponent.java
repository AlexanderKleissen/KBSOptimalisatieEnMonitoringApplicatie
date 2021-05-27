import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Ontwerpcomponent extends Component {
    private String kosten;
    private static ArrayList<Ontwerpcomponent> ontwerpcomponenten = new ArrayList<>();
    private Icon image;

    //Constructors
    public Ontwerpcomponent(String naam, String type, double kosten, double beschikbaarheidspercentage) throws SQLException {
        super(naam, type, beschikbaarheidspercentage);
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
        ontwerpcomponenten.add(this);
    //    naarDatabase();
    }


    public Ontwerpcomponent(String naam, String type, double kosten, double beschikbaarheidspercentage, String image) throws SQLException{
        super(naam, type, beschikbaarheidspercentage);
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
        this.image = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(50,80, Image.SCALE_DEFAULT));
        ontwerpcomponenten.add(this);
     //   naarDatabase();
    }

    //Getters en setters


    public String getKosten() {
        return kosten;
    }

    public Icon getImage() {
        return image;
    }

    public static ArrayList<Ontwerpcomponent> getOntwerpcomponenten() {
        return ontwerpcomponenten;
    }

    private void naarDatabase() throws SQLException {
        ArrayList<String> mogelijkeComponenten = new ArrayList<>(); //welke componenten al bestaan zodat je alleen nieuwe componenten toevoegt

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "f");//Verbinding met database wordt gemaakt

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select NaamComponent from component_ontwerpen");
        while (rs.next()){
            String component = rs.getString(1);
            mogelijkeComponenten.add(component);
        }
        rs.close();

        if(!mogelijkeComponenten.contains(this.getNaam())){
            Statement statement2 = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
            boolean insertGelukt = statement2.execute("insert into component_ontwerpen values ('"+ this.getNaam() + "', '" + this.getType() + "', '" +
                    Double.parseDouble(this.kosten.replaceAll(",", ".")) + "' , '" + Double.parseDouble(this.getBeschikbaarheidspercentage().replaceAll(",",".")) + "')");

            if (insertGelukt) {
                System.out.println("gelukt");
                connection.close();
            }
        }
    }
}