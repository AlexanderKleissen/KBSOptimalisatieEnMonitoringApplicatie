import java.util.ArrayList;

public class Ontwerpnetwerk extends Netwerk{
    private double kosten;
    private double opgegevenBeschikbaarheid;

    public Ontwerpnetwerk(double kosten, double opgegevenBeschikbaarheid, double beschikbaarheidspercentage) {
        super(beschikbaarheidspercentage, new ArrayList());
        this.kosten = kosten;
        this.opgegevenBeschikbaarheid = opgegevenBeschikbaarheid;
    }
}
