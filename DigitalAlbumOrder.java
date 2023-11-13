import java.time.LocalDate;

public class DigitalAlbumOrder extends AlbumOrder{
    protected String buyerEmail;

    public DigitalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, String discounted, String buyerEmail){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted);

        this.buyerEmail = buyerEmail; // verifier TBD
    }

    public void display(){ // WIP
        System.out.println("[Displaying Order Ref. " + refID + " ]");
        System.out.println("\tAlbum Name: " + album.getAlName());
        System.out.println("\tAlbum Artist: " + album.getArtist() + " (" + album.getArtistType() + ")");
        System.out.println("\tAlbum Release: " + album.getRelease());
    }
}