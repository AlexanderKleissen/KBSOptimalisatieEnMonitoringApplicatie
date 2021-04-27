public class Monitoringcomponent extends Component{
    private String beschikbaarheidsstatus;
    private int beschikbaarheidsduur;
    private int processorbelasting;
    private int diskruimte;
    private double periodiekeKosten;

    //Constructors
    public Monitoringcomponent(String naam, String type, String beschikbaarheidsstatus, int beschikbaarheidsduur, int processorbelasting, int diskruimte, double periodiekeKosten, double beschikbaarheidspercentage) {
        super(naam, type, beschikbaarheidspercentage);
        this.beschikbaarheidsstatus = beschikbaarheidsstatus;
        this.beschikbaarheidsduur = beschikbaarheidsduur;
        this.processorbelasting = processorbelasting;
        this.diskruimte = diskruimte;
        this.periodiekeKosten = periodiekeKosten;
    }
}
