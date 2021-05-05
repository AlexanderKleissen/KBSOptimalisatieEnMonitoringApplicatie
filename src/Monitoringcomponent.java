import java.text.DecimalFormat;

public class Monitoringcomponent extends Component{
    private String beschikbaarheidsstatus;
    private int beschikbaarheidsduur;
    private int processorbelasting;
    private int diskruimte;
    private String periodiekeKosten;

    //Constructors
    public Monitoringcomponent(String naam, String type, String beschikbaarheidsstatus, int beschikbaarheidsduur, int processorbelasting, int diskruimte, double periodiekeKosten, double beschikbaarheidspercentage) {
        super(naam, type, beschikbaarheidspercentage);
        this.beschikbaarheidsstatus = beschikbaarheidsstatus;
        this.beschikbaarheidsduur = beschikbaarheidsduur;
        this.processorbelasting = processorbelasting;
        this.diskruimte = diskruimte;
        DecimalFormat df = new DecimalFormat("0.00");
        String dfPeriodiekeKosten = df.format(periodiekeKosten);
        this.periodiekeKosten = dfPeriodiekeKosten;
    }

    //Getters
    public String getBeschikbaarheidsstatus() {
        return beschikbaarheidsstatus;
    }

    public int getBeschikbaarheidsduur() {
        return beschikbaarheidsduur;
    }

    public int getProcessorbelasting() {
        return processorbelasting;
    }

    public int getDiskruimte() {
        return diskruimte;
    }

    public String getPeriodiekeKosten() {
        return periodiekeKosten;
    }
}
