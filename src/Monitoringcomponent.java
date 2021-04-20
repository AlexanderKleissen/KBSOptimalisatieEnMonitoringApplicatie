public class Monitoringcomponent extends Component{
    private String beschikbaarheidsstatus;
    private int beschikbaarheidsduur;
    private int processorbelasting;
    private int diskruimte;
    private int periodiekeKosten;

    //Constructors
    public Monitoringcomponent(String beschikbaarheidsstatus, int beschikbaarheidsduur, int processorbelasting, int diskruimte, int periodiekeKosten, double beschikbaarheidspercentage) {
        super(beschikbaarheidspercentage);
        this.beschikbaarheidsstatus = beschikbaarheidsstatus;
        this.beschikbaarheidsduur = beschikbaarheidsduur;
        this.processorbelasting = processorbelasting;
        this.diskruimte = diskruimte;
        this.periodiekeKosten = periodiekeKosten;
    }
}
