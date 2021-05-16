import java.sql.*;
import java.text.DecimalFormat;

public class Monitoringcomponent extends Component{
    private String beschikbaarheidsstatus;
    private int beschikbaarheidsduur;
    private int processorbelasting;
    private int diskruimte;
    private String periodiekeKosten;

    //Constructors
    public Monitoringcomponent(String naam, String type, String beschikbaarheidsstatus, int beschikbaarheidsduur, int processorbelasting, int diskruimte, double periodiekeKosten, double beschikbaarheidspercentage) {
        super(naam, type, beschikbaarheidspercentage);
        this.beschikbaarheidsstatus = beschikbaarheidsstatus;
        this.beschikbaarheidsduur = beschikbaarheidsduur;
        this.processorbelasting = processorbelasting;
        this.diskruimte = diskruimte;
        DecimalFormat df = new DecimalFormat("0.00");
        String dfPeriodiekeKosten = df.format(periodiekeKosten);
        this.periodiekeKosten = dfPeriodiekeKosten;
    }

    public Monitoringcomponent(String naam, String type, double beschikbaarheidspercentage) throws SQLException {
        super(naam, type, beschikbaarheidspercentage);
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
        Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
        ResultSet rs = statement.executeQuery("select CPU_Usage, Memory_Usage, Disk_Total, Disk_Used, Disk_Free, Uptime from infrastructure_monitoring where Object_Name = '" + naam + "'"); //Query uitvoeren
        rs.next(); //Hierdoor gaat de Resultset naar de volgende regel. Als dit er niet in staat dan zal er geen resultaat uit komen.
        this.beschikbaarheidsstatus = "Online";
        this.beschikbaarheidsduur = rs.getInt(6);
        this.processorbelasting = rs.getInt(1);
        this.diskruimte = rs.getInt(5);
        double periodiekeKosten = 500;
        DecimalFormat df = new DecimalFormat("0.00");
        String dfPeriodiekeKosten = df.format(periodiekeKosten);
        this.periodiekeKosten = dfPeriodiekeKosten;
        rs.close();
    }


    //Getters
    public String getBeschikbaarheidsstatus() {
        return beschikbaarheidsstatus;
    }

    public int getBeschikbaarheidsduur() {
        return beschikbaarheidsduur;
    }

    public int getProcessorbelasting() {
        return processorbelasting;
    }

    public int getDiskruimte() {
        return diskruimte;
    }

    public String getPeriodiekeKosten() {
        return periodiekeKosten;
    }
}
