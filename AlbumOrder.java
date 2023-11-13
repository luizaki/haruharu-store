import java.time.*;
import java.util.Random;

public abstract class AlbumOrder{
    protected Album album;
    protected int quantity;
    protected double totalPrice;
    protected LocalDate datePurchased;
    protected String buyerName;
    protected long buyerContact;
    protected String discounted;
    protected int refID;

    // constructor
    public AlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, String discounted){
        this.album = album;
        this.quantity = quantity;
        this.datePurchased = datePurchased;
        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.discounted = discounted;

        this.refID = generateRefID();
        this.totalPrice = calculateTotal(quantity, album.getPrice());
    }

    private int generateRefID(){
        Random rnd = new Random();
        return rnd.nextInt((9999999 - 1000000) + 1) + 1000000;
    }

    private double calculateTotal(int quantity, double price){
        return quantity * price;
    }

    public abstract void display();
}