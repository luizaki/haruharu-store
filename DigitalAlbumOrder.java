import java.time.LocalDate;

public class DigitalAlbumOrder extends AlbumOrder{
    protected String buyerEmail;

    public DigitalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, boolean discounted, String buyerEmail){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted);

        this.buyerEmail = buyerEmail; // verifier TBD
        calculateTotal();
    }

    // alt constructor
    public DigitalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, boolean discounted, double subTotal, double totalPrice, int refID, String buyerEmail){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted, subTotal, totalPrice, refID);

        this.buyerEmail = buyerEmail;
    }

    // getters
    public String getBuyerEmail(){return this.buyerEmail;}

    // WIP (discounted not counted for)
    protected void calculateTotal(){
        this.subTotal = this.quantity * album.getPrice();
        if(this.discounted) this.totalPrice = subTotal * discount;
        else this.totalPrice = subTotal;
    }

    public void display(){ // WIP
        System.out.println("[Displaying Order Ref. " + refID + " ]");
        System.out.println("\tAlbum Name: " + album.getAlName());
        System.out.println("\tAlbum Artist: " + album.getArtist() + " (" + album.getArtistType() + ")");
        System.out.println("\tAlbum Release: " + album.getRelease());
    }
}