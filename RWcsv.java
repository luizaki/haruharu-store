import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RWcsv{
    static DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Album[] readCatalog(){
        BufferedReader buffread;
        String line = "";
        Album[] albums;

        ArrayList<String> albumList = new ArrayList<String>();

        try{
            // ready to read through catalog.csv
            buffread = new BufferedReader(new FileReader("catalog.csv"));

            // add each line to albumList until empty
            while((line = buffread.readLine()) != null){
                albumList.add(line);
            }

            buffread.close();
        }
        catch(IOException mali){
            mali.printStackTrace();
        }

        // make array of Albums based on length of albumList
        albums = new Album[albumList.size()];

        // loop to construct each line into an Album object
        for(int i = 0; i < albumList.size(); i++){
            // make String array to separate each data within the line
            String[] albumInfo = albumList.get(i).split(",");

            // retrieve and convert non-String attributes
            String alName = albumInfo[0];
            String artist = albumInfo[1];
            String artistType = albumInfo[2];
            LocalDate release = LocalDate.parse(albumInfo[3], DATEFORMATTER);
            double price = Double.parseDouble(albumInfo[4]);

            // construct Album object
            albums[i] = new Album(alName, artist, artistType, release, price);
        }

        return albums;
    }

    public static AlbumOrder[] readOrders(){
        BufferedReader buffread;
        String line = "";
        AlbumOrder[] orders;

        ArrayList<String> orderList = new ArrayList<String>();

        try{
            // ready to read through orders.csv
            buffread = new BufferedReader(new FileReader("orders.csv"));

            // add each line to orderList until empty
            while((line = buffread.readLine()) != null){
                orderList.add(line);
            }

            buffread.close();
        }
        catch(IOException mali){
            mali.printStackTrace();
        }

        // make array of AlbumOrders based on length of albumList
        orders = new AlbumOrder[orderList.size()];

        // loop to construct each line into an Album object
        for(int i = 0; i < orderList.size(); i++){
            // make String array to separate each data within the line
            String[] albumInfo = orderList.get(i).split(",");

            // match album field to catalog to retrieve info
            Album[] catalog = readCatalog();
            ArrayList<String> alNames = new ArrayList<String>();
            for(int j = 0; j < catalog.length; j++) alNames.add(catalog[i].getAlName());

            // retrieve basic AlbumOrder() attributes
            int refID = Integer.parseInt(albumInfo[0]);
            Album album = catalog[alNames.indexOf(albumInfo[1])];
            int quantity = Integer.parseInt(albumInfo[2]);
            double totalPrice = Double.parseDouble(albumInfo[3]);
            LocalDate datePurchased = LocalDate.parse(albumInfo[4], DATEFORMATTER);
            String buyerName = albumInfo[5];
            long buyerContact = Long.parseLong(albumInfo[6]);
            String discounted = albumInfo[7];

            // categorize orders into PhysicalAlbumOrder and DigitalAlbumOrder
            if((albumInfo[8] != "N/A" && albumInfo[9] != "N/A") && albumInfo[10] == "N/A"){
                String shippingAddress = albumInfo[8];
                double shippingFee = Double.parseDouble(albumInfo[9]);

                orders[i] = new PhysicalAlbumOrder(album, quantity, datePurchased, buyerName, buyerContact, discounted, totalPrice, refID, shippingAddress, shippingFee);
            }
            else if((albumInfo[8] == "N/A" && albumInfo[9] == "N/A") && albumInfo[10] != "N/A"){
                String buyerEmail = albumInfo[10];

                orders[i] = new DigitalAlbumOrder(album, quantity, datePurchased, buyerName, buyerContact, discounted, totalPrice, refID, buyerEmail);
            }
        }

        return orders;
    }

    public static void writeOrders(AlbumOrder[] orders){
        String line, file;
        ArrayList<String> orderList = new ArrayList<String>();

        // fill up orderList with each AlbumOrder(), converting each to one line
        for(AlbumOrder ord : orders){
            // make initial line
            line = String.join(",", Integer.toString(ord.getRefID()), ord.getAlbum().getAlName(), Integer.toString(ord.getQuantity()), Double.toString(ord.getTotalPrice()), ord.getDatePurchased().toString(), ord.getBuyerName(), Long.toString(ord.getBuyerContact()), ord.getDiscounted());

            // add additional fields of physical and digital
            if(ord instanceof PhysicalAlbumOrder){
                line += "," + String.join(",", ((PhysicalAlbumOrder) ord).getShippingAddress(), Double.toString(((PhysicalAlbumOrder) ord).getShippingFee()));
            }
            else if(ord instanceof DigitalAlbumOrder){
                line += "," + ((DigitalAlbumOrder) ord).getBuyerEmail();
            }
        }

        // formulate entire csv file by joining all lines
        file = String.join("\n", orderList);

        // write to orders.csv
        try{
            FileWriter write = new FileWriter("orders.csv");
            write.append(file);
            write.close();
        }
        catch(IOException mali){
            mali.printStackTrace();
        }
    }
}
