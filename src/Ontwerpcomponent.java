public class Ontwerpcomponent extends Component {
    private double kosten;
    private int totaleDiskruimte;

    //Constructors
    public Ontwerpcomponent(double kosten, int totaleDiskruimte, double beschikbaarheidspercentage) {
        super(beschikbaarheidspercentage);
        this.kosten = kosten;
        this.totaleDiskruimte = totaleDiskruimte;
    }
}
