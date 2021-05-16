import java.text.DecimalFormat;
import java.util.ArrayList;

public class Ontwerpnetwerk extends Netwerk{
    private String kosten;
    private double opgegevenBeschikbaarheid;
    private static ArrayList<Ontwerpnetwerk> ontwerpNetwerken =  new ArrayList<>();

    //Constructors
    public Ontwerpnetwerk(String naam, double kosten, double opgegevenBeschikbaarheid, double beschikbaarheidspercentage) {
        super(naam, beschikbaarheidspercentage, new ArrayList<>());
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
        this.opgegevenBeschikbaarheid = opgegevenBeschikbaarheid;
        groepen = new ArrayList<>();
        ontwerpNetwerken.add(this);
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

    public void setOpgegevenBeschikbaarheid(double opgegevenBeschikbaarheid) {
        this.opgegevenBeschikbaarheid = opgegevenBeschikbaarheid;
    }
}