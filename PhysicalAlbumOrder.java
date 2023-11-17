import java.time.LocalDate;

public class PhysicalAlbumOrder extends AlbumOrder{
    protected String shippingAddress;
    protected double shippingFee = 50.0;

    public PhysicalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, boolean discounted, String shippingAddress){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted);

        this.shippingAddress = shippingAddress;

        // recalculate shipping fee (add 50 for every 5 unit quantity)
        this.shippingFee += (50 * Math.floorDiv(this.quantity, 5));
        calculateTotal();
    }

    // alt constructor
    public PhysicalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, boolean discounted, double subTotal, double totalPrice, int refID, String shippingAddress, double shippingFee){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted, subTotal, totalPrice, refID);

        this.shippingAddress = shippingAddress;
        this.shippingFee = shippingFee;
    }

    // getters
    public String getShippingAddress(){return this.shippingAddress;}
    public double getShippingFee(){return this.shippingFee;}

    protected void calculateTotal(){
        this.subTotal = this.quantity * album.getPrice();
        if(this.discounted) this.totalPrice = (subTotal + this.shippingFee) * discount;
        else this.totalPrice = subTotal + this.shippingFee;
    }

    public void display(){ // WIP
        System.out.println("[Displaying Order Ref. " + refID + " ]");
        System.out.println("\tAlbum Name: " + album.getAlName());
        System.out.println("\tAlbum Artist: " + album.getArtist() + " (" + album.getArtistType() + ")");
        System.out.println("\tAlbum Release: " + album.getRelease());
        System.out.println("\tTotal Price: " + phpFormat.format(this.totalPrice));
    }
}