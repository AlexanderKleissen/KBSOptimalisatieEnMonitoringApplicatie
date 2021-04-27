public abstract class Component {
    private String naam;
    private String type;
    private double beschikbaarheidspercentage;

    //Constructor
    public Component(String naam, String type, double beschikbaarheidspercentage) {
        this.naam = naam;
        this.type = type;
        this.beschikbaarheidspercentage = beschikbaarheidspercentage;
    }

    //Getters en setters
    public String getType() {
        return type;
    }

    public String getNaam() {
        return naam;
    }

    public double getBeschikbaarheidspercentage() {
        return beschikbaarheidspercentage;
    }
}
