import java.text.DecimalFormat;
import java.time.*;

public class Album{
    private String alName;
    private String artist;
    private String artistType;
    private LocalDate release;
    private double price;

    protected static DecimalFormat phpFormat = new DecimalFormat("Php #,##0.00");

    // constructor
    public Album(String alName, String artist, String artistType, LocalDate release, double price){
        this.alName = alName;
        this.artist = artist;
        this.artistType = artistType;
        this.release = release;
        this.price = price;
    }

    // getters
    public String getAlName(){return this.alName;}
    public String getArtist(){return this.artist;}
    public String getArtistType(){return this.artistType;}
    public LocalDate getRelease(){return this.release;}
    public double getPrice(){return this.price;}

    // temporary display method
    public void display(){
        System.out.println("Album Name: " + this.alName);
        System.out.println("Aritst: " + this.artist);
        System.out.println("Artist Type: " + this.artistType);
        System.out.println("Release Date: " + this.release);
        System.out.println("Unit Price: " + phpFormat.format(this.price));
    }
}
