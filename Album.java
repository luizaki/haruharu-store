import java.text.DecimalFormat;
import java.time.*;

public class Album{
    private String alName;
    private String artist;
    private String artistType;
    private LocalDate release;
    private double price;

    protected static final DecimalFormat phpFormat = new DecimalFormat("Php #,##0.00");

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

    public void display(){
        System.out.println("[Displaying Album Details]");
        System.out.println("\tAlbum Name: " + this.alName);
        System.out.println("\tAritst: " + this.artist);
        System.out.println("\tArtist Type: " + this.artistType);
        System.out.println("\tRelease Date: " + this.release);
        System.out.println("\tUnit Price: " + phpFormat.format(this.price));
    }
}
