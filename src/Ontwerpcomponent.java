import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

public class Ontwerpcomponent extends Component {
    private String kosten;
    private Icon image;

    //Constructors
//    public Ontwerpcomponent(String naam, String type, double kosten, double beschikbaarheidspercentage) {
//        super(naam, type, beschikbaarheidspercentage);
//        DecimalFormat df = new DecimalFormat("0.00");
//        String dfKosten = df.format(kosten);
//        this.kosten = dfKosten;
//    }

    public Ontwerpcomponent(String naam, String type, double kosten, double beschikbaarheidspercentage, String image){
        super(naam, type, beschikbaarheidspercentage);
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
        this.image = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(50,80, Image.SCALE_DEFAULT));

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");//Verbinding met database wordt gemaakt
            Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
            boolean insertGelukt = statement.execute("insert into component_ontwerpen values ('" + naam + "', '" + type + "', '" + kosten + "' , '" + beschikbaarheidspercentage + "')");

            if (insertGelukt) {
                System.out.println("gelukt");
                connection.close();
            }
        } catch (SQLException sql){
            if (sql.getMessage().contains("Duplicate entry")) {
                System.out.println(sql.getMessage());
            }
        }
    }

    //Getters en setters


    public String getKosten() {
        return kosten;
    }

    public Icon getImage() {
        return image;
    }
}