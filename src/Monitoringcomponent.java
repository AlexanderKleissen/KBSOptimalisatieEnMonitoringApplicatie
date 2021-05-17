import java.sql.*;
import java.text.DecimalFormat;

public class Monitoringcomponent extends Component{
    private String beschikbaarheidsstatus;
    private int beschikbaarheidsduur;
    private double processorbelasting;
    private double totaleDiskruimte;
    private double gebruikteDiskruimte;
    private double beschikbareDiskruimte;
    private String periodiekeKosten;
    private double werkgeheugenVerbruik;
    private String ipaddress;

    //Constructors
    public Monitoringcomponent(String naam, String type, String beschikbaarheidsstatus, int beschikbaarheidsduur, int processorbelasting, int diskruimte, double periodiekeKosten, double beschikbaarheidspercentage) {
        super(naam, type, beschikbaarheidspercentage);
        this.beschikbaarheidsstatus = beschikbaarheidsstatus;
        this.beschikbaarheidsduur = beschikbaarheidsduur;
        this.processorbelasting = processorbelasting;
        this.beschikbareDiskruimte = diskruimte;
        DecimalFormat df = new DecimalFormat("0.00");
        String dfPeriodiekeKosten = df.format(periodiekeKosten);
        this.periodiekeKosten = dfPeriodiekeKosten;
    }

    public Monitoringcomponent(String naam, String type, double beschikbaarheidspercentage, double periodiekeKosten, String ipaddress) throws SQLException {
        super(naam, type, beschikbaarheidspercentage);
        this.ipaddress = ipaddress;

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
        Statement statement = connection.createStatement(); //Statement object maken met connection zodat er een statement uitgevoerd kan worden
        ResultSet rs = statement.executeQuery("select CPU_Usage, Memory_Usage, Disk_Total, Disk_Used, Disk_Free, Uptime from infrastructure_monitoring where Object_Name = '" + naam + "'"); //Query uitvoeren
        rs.next(); //Hierdoor gaat de Resultset naar de volgende regel. Als dit er niet in staat dan zal er geen resultaat uit komen.

        this.beschikbaarheidsstatus = "Online";
        this.processorbelasting = rs.getDouble(1);
        this.werkgeheugenVerbruik = rs.getDouble(2);
        this.totaleDiskruimte = rs.getDouble(3);
        this.gebruikteDiskruimte = rs.getDouble(4);
        this.beschikbareDiskruimte = rs.getDouble(5);
        this.beschikbaarheidsduur = rs.getInt(6);

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

    public double getProcessorbelasting() {
        return processorbelasting;
    }

    public double getTotaleDiskruimte() {
        return totaleDiskruimte;
    }

    public double getGebruikteDiskruimte() {
        return gebruikteDiskruimte;
    }

    public double getBeschikbareDiskruimte() {
        return beschikbareDiskruimte;
    }

    public String getPeriodiekeKosten() {
        return periodiekeKosten;
    }

    public double getWerkgeheugenVerbruik() {
        return werkgeheugenVerbruik;
    }
}
