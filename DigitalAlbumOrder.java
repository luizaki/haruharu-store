import java.time.LocalDate;

public class DigitalAlbumOrder extends AlbumOrder{
    protected String buyerEmail;

    public DigitalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, String discounted, String buyerEmail){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted);

        this.buyerEmail = buyerEmail; // verifier TBD
    }

    // alt constructor
    public DigitalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, String discounted, double totalPrice, int refID, String buyerEmail){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted, totalPrice, refID);

        this.totalPrice = totalPrice;
        this.refID = refID;
        this.buyerEmail = buyerEmail;
    }

    // getters
    public String getBuyerEmail(){return this.buyerEmail;}

    // WIP (discounted not counted for)
    protected double calculateTotal(int quantity, double price, String discounted){
        return quantity * price;
    }

    public void display(){ // WIP
        System.out.println("[Displaying Order Ref. " + refID + " ]");
        System.out.println("\tAlbum Name: " + album.getAlName());
        System.out.println("\tAlbum Artist: " + album.getArtist() + " (" + album.getArtistType() + ")");
        System.out.println("\tAlbum Release: " + album.getRelease());
    }
}