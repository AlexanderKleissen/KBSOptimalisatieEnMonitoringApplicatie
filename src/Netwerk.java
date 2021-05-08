import java.util.ArrayList;

public abstract class Netwerk {
    private double beschikbaarheidspercentage;
    protected ArrayList<Groep> groepen;
    private String naam;

    //Constructor
    public Netwerk(String naam, double beschikbaarheidspercentage, ArrayList<Groep> groepen){
        this.naam = naam;
        this.beschikbaarheidspercentage=beschikbaarheidspercentage;
        this.groepen = groepen;
    }

    //Getters en setters
    public String getNaam() {
        return naam;
    }

    public double getBeschikbaarheidspercentage() {
        return beschikbaarheidspercentage;
    }

    public void setBeschikbaarheidspercentage(double beschikbaarheidspercentage) {
        this.beschikbaarheidspercentage = beschikbaarheidspercentage;
    }
}