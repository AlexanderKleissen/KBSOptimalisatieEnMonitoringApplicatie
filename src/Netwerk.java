import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class Netwerk {
    private String beschikbaarheidspercentage;
    protected ArrayList<Groep> groepen;
    private String naam;

    //Constructor
    public Netwerk(String naam, double beschikbaarheidspercentage, ArrayList<Groep> groepen){
        this.naam = naam;
        DecimalFormat df = new DecimalFormat("0.000");
        String dfBeschikbaarheidspercentage = df.format(beschikbaarheidspercentage);
        this.beschikbaarheidspercentage= dfBeschikbaarheidspercentage;
        this.groepen = groepen;
    }

    //Getters en setters
    public String getNaam() {
        return naam;
    }

    public String getBeschikbaarheidspercentage() {
        return beschikbaarheidspercentage;
    }

    public void setBeschikbaarheidspercentage(double beschikbaarheidspercentage) {
        DecimalFormat df = new DecimalFormat("0.000");
        String dfBeschikbaarheidspercentage = df.format(beschikbaarheidspercentage);
    }
}