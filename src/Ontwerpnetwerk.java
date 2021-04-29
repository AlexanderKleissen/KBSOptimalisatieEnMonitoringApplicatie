import java.util.ArrayList;

public class Ontwerpnetwerk extends Netwerk{
    private double kosten;
    private double opgegevenBeschikbaarheid;
    private static ArrayList<Ontwerpnetwerk> ontwerpNetwerken =  new ArrayList<>();

    //Constructors
    public Ontwerpnetwerk(String naam, double kosten, double opgegevenBeschikbaarheid, double beschikbaarheidspercentage) {
        super(naam, beschikbaarheidspercentage, new ArrayList<>());
        this.kosten = kosten;
        this.opgegevenBeschikbaarheid = opgegevenBeschikbaarheid;
        groepen = new ArrayList<>();
        ontwerpNetwerken.add(this);
    }

    //Getters en setters
    public static ArrayList<Ontwerpnetwerk> getOntwerpNetwerken() {
        return ontwerpNetwerken;
    }

    public double getKosten() {
        return kosten;
    }
}