import java.text.DecimalFormat;

public class Ontwerpcomponent extends Component {
    private String kosten;
    private int totaleDiskruimte;

    //Constructors
    public Ontwerpcomponent(String naam, String type, double kosten, int totaleDiskruimte, double beschikbaarheidspercentage) {
        super(naam, type, beschikbaarheidspercentage);
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
        this.totaleDiskruimte = totaleDiskruimte;
    }

    //Getters en setters
    public int getTotaleDiskruimte() {
        return totaleDiskruimte;
    }

    public String getKosten() {
        return kosten;
    }
}
