import java.time.LocalDate;

public class PhysicalAlbumOrder extends AlbumOrder{
    protected String shippingAddress;
    protected double shippingFee;

    public PhysicalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, boolean discounted, String shippingAddress){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted);

        this.shippingAddress = shippingAddress;

        // recalculate shipping fee (add 50 for every 5 unit quantity)
        this.shippingFee = (50 * Math.floorDiv(this.quantity, 5));
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
        if(this.discounted) this.totalPrice = (subTotal * discount) + this.shippingFee;
        else this.totalPrice = subTotal + this.shippingFee;
    }

    public void display(){
        String discount;
        if(discounted) discount = "Yes";
        else discount = "No";

        System.out.println("[Displaying Order Ref. " + refID + "]");
        System.out.println("\tAlbum: " + album.getAlName() + " (" + album.getRelease() + ")");
        System.out.println("\tArtist: " + album.getArtist() + " (" + album.getArtistType() + ")");
        System.out.println("\tUnit Price: " + phpFormat.format(album.getPrice()));
        System.out.println("\tQuantity: " + this.quantity);
        System.out.println();
        System.out.println("\tBuyer Name: " + this.buyerName);
        System.out.println("\tBuyer Contact: " + contactFormat.format(this.buyerContact));
        System.out.println("\tShipping Address: " + this.shippingAddress);
        System.out.println();
        System.out.println("\tHas 10% Discount: " + discount);
        System.out.println("\tSubtotal: " + phpFormat.format(this.subTotal));
        System.out.println();
        System.out.println("\tShipping Fee: " + phpFormat.format(this.shippingFee));
        System.out.println("\tTotal Price: " + phpFormat.format(this.totalPrice));
        System.out.println();
        System.out.println("\tDate of Purchase: " + this.datePurchased);
    }
}