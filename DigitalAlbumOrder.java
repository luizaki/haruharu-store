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

    public void display(){
        System.out.println("[Displaying Order Ref. " + refID + "]");
        System.out.println("\tAlbum: " + album.getAlName() + " (" + album.getRelease() + ")");
        System.out.println("\tArtist: " + album.getArtist() + " (" + album.getArtistType() + ")");
        System.out.println();
        System.out.println("\tBuyer Name: " + this.buyerName);
        System.out.println("\tBuyer Contact: " + this.buyerContact);
        System.out.println("\tBuyer Email: " + this.buyerEmail);
        System.out.println("\tSubtotal: " + phpFormat.format(this.subTotal));
        System.out.println();
        System.out.println("\tHas 10% Discount: " + this.discounted);
        System.out.println("\tTotal Price: " + phpFormat.format(this.totalPrice));        
        System.out.println();
        System.out.println("\tDate of Purchase: " + this.datePurchased);
    }
}