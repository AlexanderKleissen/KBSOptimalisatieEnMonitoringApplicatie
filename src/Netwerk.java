import java.util.ArrayList;

public abstract class Netwerk {
    private String naam;
    private double beschikbaarheidspercentage;
    protected ArrayList<Groep> groepen;

    //Constructor
    public Netwerk(String naam, double beschikbaarheidspercentage, ArrayList<Groep> groepen){
        this.naam = naam;
        this.beschikbaarheidspercentage=beschikbaarheidspercentage;
    };

    //Getters en setters
    public String getNaam() {
        return naam;
    }
}
