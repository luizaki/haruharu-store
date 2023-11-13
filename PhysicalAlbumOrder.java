import java.time.LocalDate;

public class PhysicalAlbumOrder extends AlbumOrder{
    protected String shippingAddress;
    protected double shippingFee;

    public PhysicalAlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, String discounted, String shippingAddress){
        super(album, quantity, datePurchased, buyerName, buyerContact, discounted);

        this.shippingAddress = shippingAddress;

        // this.shippingFee to depend on quantity(?)
    }

    public void display(){ // WIP
        System.out.println("[Displaying Order Ref. " + refID + " ]");
        System.out.println("\tAlbum Name: " + album.getAlName());
        System.out.println("\tAlbum Artist: " + album.getArtist() + " (" + album.getArtistType() + ")");
        System.out.println("\tAlbum Release: " + album.getRelease());
    }
}