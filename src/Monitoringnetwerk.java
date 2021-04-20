import java.util.ArrayList;

public class Monitoringnetwerk extends Netwerk{
    private int periodiekeKosten;

    //Constructors
    public Monitoringnetwerk(int periodiekeKosten, double beschikbaarheidspercentage) {
        super(beschikbaarheidspercentage, new ArrayList());
        this.periodiekeKosten = periodiekeKosten;
    }
}
