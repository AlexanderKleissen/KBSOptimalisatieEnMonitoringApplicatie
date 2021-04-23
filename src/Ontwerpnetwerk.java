import java.util.ArrayList;

public class Ontwerpnetwerk extends Netwerk{
    private double kosten;
    private double opgegevenBeschikbaarheid;

    public Ontwerpnetwerk(String naam, double kosten, double opgegevenBeschikbaarheid, double beschikbaarheidspercentage) {
        super(naam, beschikbaarheidspercentage, new ArrayList());
        this.kosten = kosten;
        this.opgegevenBeschikbaarheid = opgegevenBeschikbaarheid;
    }
}
