import java.time.LocalDate;

public class PhysicalAlbumOrder extends AlbumOrder{
    protected String shippingAddress;
    protected double shippingFee = 50.0;

    public PhysicalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, String discounted, String shippingAddress){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted);

        this.shippingAddress = shippingAddress;

        // recalculate shipping fee (add 50 for every 1k unit album price)
        this.shippingFee += (50 * Math.floorDiv((int) album.getPrice(), 1000));
        this.totalPrice = calculateTotal(quantity, buyerContact, discounted);
    }

    // alt constructor
    public PhysicalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, String discounted, double totalPrice, int refID, String shippingAddress, double shippingFee){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted, totalPrice, refID);

        this.totalPrice = totalPrice;
        this.refID = refID;
        this.shippingAddress = shippingAddress;
        this.shippingFee = shippingFee;
    }

    // getters
    public String getShippingAddress(){return this.shippingAddress;}
    public double getShippingFee(){return this.shippingFee;}

    // WIP (discounted not counted for)
    protected double calculateTotal(int quantity, double price, String discounted){
        return quantity * price + this.shippingFee;
    }

    public void display(){ // WIP
        System.out.println("[Displaying Order Ref. " + refID + " ]");
        System.out.println("\tAlbum Name: " + album.getAlName());
        System.out.println("\tAlbum Artist: " + album.getArtist() + " (" + album.getArtistType() + ")");
        System.out.println("\tAlbum Release: " + album.getRelease());
    }
}