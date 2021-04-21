import java.lang.reflect.Array;
import java.util.ArrayList;

public class Groep {
    private String type;
    private double beschikbaarheidspercentage;
    protected ArrayList<Component> componenten;

    //Constructors
    public Groep(String type, double beschikbaarheidspercentage) {
        this.type = type;
        this.beschikbaarheidspercentage = beschikbaarheidspercentage;
        this.componenten = new ArrayList();
    }
}
