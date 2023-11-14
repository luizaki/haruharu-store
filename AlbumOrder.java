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
    }

    // alternate constructor with given refID and total
    public AlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, String discounted, double totalPrice, int refID){
        this.album = album;
        this.quantity = quantity;
        this.datePurchased = datePurchased;
        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.discounted = discounted;
        this.totalPrice = totalPrice;
        this.refID = refID;
    }

    // getters
    public int getRefID(){return this.refID;}
    public Album getAlbum(){return this.album;}
    public int getQuantity(){return this.quantity;}
    public double getTotalPrice(){return this.totalPrice;}
    public LocalDate getDatePurchased(){return this.datePurchased;}
    public String getBuyerName(){return this.buyerName;}
    public long getBuyerContact(){return this.buyerContact;}
    public String getDiscounted(){return this.discounted;}

    protected int generateRefID(){
        Random rnd = new Random();
        return rnd.nextInt((9999999 - 1000000) + 1) + 1000000;
    }

    protected abstract double calculateTotal(int quantity, double price, String discounted);

    public abstract void display();
}