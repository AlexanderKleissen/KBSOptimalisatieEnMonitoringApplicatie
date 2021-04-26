import java.util.ArrayList;

public class Monitoringnetwerk extends Netwerk{
    private int periodiekeKosten;
    private static ArrayList<Monitoringnetwerk> monitoringNetwerken =  new ArrayList<>();

    //Constructors
    public Monitoringnetwerk(String naam, int periodiekeKosten, double beschikbaarheidspercentage) {
        super(naam, beschikbaarheidspercentage, new ArrayList());
        this.periodiekeKosten = periodiekeKosten;
        groepen = new ArrayList<>();
        monitoringNetwerken.add(this);
    }

    //Getters en Setters
    public static ArrayList<Monitoringnetwerk> getMonitoringNetwerken() {
        return monitoringNetwerken;
    }
}
