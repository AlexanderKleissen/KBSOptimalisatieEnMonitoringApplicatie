import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class Component {
    private String naam;
    private String type;
    private String beschikbaarheidspercentage;

    //Constructor
    public Component(String naam, String type, double beschikbaarheidspercentage) {
        this.naam = naam;
        this.type = type;
        DecimalFormat df = new DecimalFormat("0.000");
        String dfBeschikbaarheidspercentage = df.format(beschikbaarheidspercentage);
        this.beschikbaarheidspercentage = dfBeschikbaarheidspercentage;
    }

    //Getters en setters
    public String getNaam() {
        return naam;
    }

    public String getType(){return type;}

    public String getBeschikbaarheidspercentage() {
        return beschikbaarheidspercentage;
    }
}
