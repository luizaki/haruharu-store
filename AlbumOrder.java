import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.text.DecimalFormat;

public abstract class AlbumOrder{
    protected Album album;
    protected int quantity;
    protected double subTotal;
    protected double totalPrice;
    protected LocalDate datePurchased;
    protected String buyerName;
    protected long buyerContact;
    protected boolean discounted;
    protected int refID;

    protected static double discount = 0.9; // 10% but translates to 90% of subTotal
    protected static DecimalFormat phpFormat = new DecimalFormat("Php #,##0.00");
    protected static DecimalFormat contactFormat = new DecimalFormat("'0'##########");

    // constructor
    public AlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, boolean discounted){
        this.album = album;
        this.quantity = quantity;
        this.datePurchased = datePurchased;
        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.discounted = discounted;

        generateRefID();
    }

    // alternate constructor with given refID and total
    public AlbumOrder(Album album, int quantity, LocalDate datePurchased, String buyerName, long buyerContact, boolean discounted, double subTotal, double totalPrice, int refID){
        this.album = album;
        this.quantity = quantity;
        this.datePurchased = datePurchased;
        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.discounted = discounted;
        this.subTotal = subTotal;
        this.totalPrice = totalPrice;
        this.refID = refID;
    }

    // getters
    public int getRefID(){return this.refID;}
    public Album getAlbum(){return this.album;}
    public int getQuantity(){return this.quantity;}
    public double getSubTotal(){return this.subTotal;}
    public double getTotalPrice(){return this.totalPrice;}
    public LocalDate getDatePurchased(){return this.datePurchased;}
    public String getBuyerName(){return this.buyerName;}
    public long getBuyerContact(){return this.buyerContact;}
    public boolean getDiscounted(){return this.discounted;}

    protected void generateRefID(){
        Random rnd = new Random();
        int generated = 0;

        // fetch all refIDs to prevent duplicates
        AlbumOrder[] orders = RWcsv.readOrders();
        ArrayList<Integer> refIDs= new ArrayList<Integer>();
        for(AlbumOrder ord : orders) refIDs.add(ord.getRefID());

        do{
            generated = rnd.nextInt((9999999 - 1000000) + 1) + 1000000;
        } while(refIDs.contains(generated));

        this.refID = generated;
    }

    protected abstract void calculateTotal();

    public abstract void display();
}