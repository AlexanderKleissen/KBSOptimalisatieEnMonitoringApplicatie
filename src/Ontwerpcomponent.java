public class Ontwerpcomponent extends Component {
    private double kosten;
    private int totaleDiskruimte;

    //Constructors
    public Ontwerpcomponent(String naam, String type, double kosten, int totaleDiskruimte, double beschikbaarheidspercentage) {
        super(naam, type, beschikbaarheidspercentage);
        this.kosten = kosten;
        this.totaleDiskruimte = totaleDiskruimte;
    }

    //Getters en setters
    public int getTotaleDiskruimte() {
        return totaleDiskruimte;
    }

    public double getKosten() {
        return kosten;
    }
}
