import java.text.DecimalFormat;

public class Ontwerpcomponent extends Component {
    private String kosten;

    //Constructors
    public Ontwerpcomponent(String naam, String type, double kosten, double beschikbaarheidspercentage) {
        super(naam, type, beschikbaarheidspercentage);
        DecimalFormat df = new DecimalFormat("0.00");
        String dfKosten = df.format(kosten);
        this.kosten = dfKosten;
    }

    //Getters en setters


    public String getKosten() {
        return kosten;
    }
}
