import java.util.*;
import java.time.LocalDate;

public class Store{
    static Scanner in = new Scanner(System.in);
    static Album[] catalog = RWcsv.readCatalog();
    static AlbumOrder[] orders = RWcsv.readOrders();

    public static void displayCatalog(Album[] albums){
        // setups
        int page = 0;
        boolean keepDisplaying = true;
        char input;

        do{
            try{
                // initialise indices to fetch albums from based on current page
                int startIndex = page * 10;
                int endIndex = Math.min(startIndex + 10, albums.length); // get min to prevent extra indices on last page

                System.out.println("\nPage " + (page+1));
                System.out.println(String.join(" | ", "Name", "Artist", "Artist Type", "Release", "Price"));

                // for loop to print 10 Album objects at a time
                for(int i = startIndex; i < endIndex; i++){
                    System.out.print((i + 1) + ". ");
                    System.out.println(String.join(" | ", albums[i].getAlName(), albums[i].getArtist(),
                    albums[i].getArtistType(), albums[i].getRelease().toString(), Double.toString(albums[i].getPrice())));
                }

                // print next possible options of user
                System.out.print("\n[P]revious, [N]ext, [F]ilter, [O]rder, [E]xit > "); input = in.nextLine().toUpperCase().charAt(0);
                Check.checkOptionInput(input, new char[]{'P', 'N', 'F', 'O', 'E'});

                switch(input){
                    case 'P':{ // prev
                        if(startIndex == 0) throw new PageIndexException(Check.prevPageMsg);
                        else page--;
                        break;
                    }
                    case 'N':{ // next
                        if(endIndex == albums.length) throw new PageIndexException(Check.nextPageMsg);
                        else page++;
                        break;
                    }
                    case 'F':{ // filter
                        keepDisplaying = false; // to avoid nested displayCatalog()s
                        filterCatalog();
                        break;
                    }
                    case 'O':{ // order
                        System.out.print("Input the number of the album you're ordering > "); int index = in.nextInt(); in.nextLine();
                        
                        if(albums.length == catalog.length) keepDisplaying = placeOrder(index - 1); // if displayCatalog is currently displaying catalog
                        else keepDisplaying = placeOrder(Arrays.asList(catalog).indexOf(albums[index - 1]));
                        break;
                    }
                    case 'E':{ // exit
                        keepDisplaying = false;
                        break;
                    }
                }
            }
            catch(OptionInputException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
            catch(PageIndexException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
        } while(keepDisplaying);
    }

    public static void filterCatalog(){
        try{
            System.out.print("Filter on: [A]rtist, [T]ype, or [R]eset > "); char filter = in.nextLine().toUpperCase().charAt(0);
            Check.checkOptionInput(filter, new char[]{'A', 'T', 'R'});

            switch(filter){
                case 'A':{ // artist
                    System.out.print("Input name of artist > "); String artist = in.nextLine().toUpperCase();

                    Check.matchArtist(artist);

                    // proceed to filter if no errors
                    Album[] updated = Arrays.stream(catalog)
                                    .filter(a -> artist.equals(a.getArtist().toUpperCase()))
                                    .toArray(Album[]::new);
                    displayCatalog(updated);
                    break;
                }
                case 'T':{ // type
                    System.out.print("Input [BG] Boy Group, [GG] Girl Group, [CE] CO-ED Group, [BS] Boy Soloist, [GS] Girl Soloist > "); String type = in.nextLine().toUpperCase();
                    Check.checkOptionInput(type, new String[]{"BG", "GG", "CE", "BS", "GS"});

                    // hashmap of types and their input codes
                    HashMap<String, String> types = new HashMap<String, String>();
                    types.put("BG", "Boy Group");
                    types.put("GG", "Girl Group");
                    types.put("CE", "CO-ED Group");
                    types.put("BS", "Boy Soloist");
                    types.put("GS", "Girl Soloist");

                    // filter based on key/value
                    Album[] updated = Arrays.stream(catalog)
                                            .filter(a -> types.get(type).equals(a.getArtistType()))
                                            .toArray(Album[]::new);
                    displayCatalog(updated);
                    break;
                }
                case 'R':{ // reset back to printing entire catalog
                    displayCatalog(catalog);
                }
            }
        }
        // caught exceptions will go back to displaying default catalog
        catch(OptionInputException mali){
            System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            displayCatalog(catalog);
        }
        catch(DataInputException mali){
            System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            displayCatalog(catalog);
        }
    }

    public static boolean placeOrder(int index){
        // setups
        Album album = catalog[index];
        String input;
        boolean returnToDisplay = true;

        char confirm, albumType, hasDiscount;
        String buyerName, shippingAddress, buyerEmail;
        int quantity; long buyerContact;
        boolean discounted = false;
        AlbumOrder order = null;

        System.out.println("\nYou will be ordering the following (feel free to type 'cancel' any time)\n");
        album.display();

        try{ // inputs incl. cancel typing to return to display
            // physical or digital
            System.out.print("\nWould you like [P]hysical or [D]igital copies? > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else{
                albumType = input.toUpperCase().charAt(0);                    
                Check.checkOptionInput(albumType, new char[]{'P', 'D'});
            }

            // quantity
            System.out.print("Input quantity > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else quantity = Integer.parseInt(input);

            // name
            System.out.print("Input full name > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else buyerName = input;

            // contact num
            System.out.print("Input contact # > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else buyerContact = Long.parseLong(input);

            // discount
            System.out.print("Are you a student/senior citizen? [Y/N] > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else{
                hasDiscount = input.toUpperCase().charAt(0);
                Check.checkOptionInput(hasDiscount);

                if(hasDiscount == 'Y') discounted = true;
                else if(hasDiscount == 'N') discounted = false;
            }

            // branch to physical and digital, check inputs and create Object
            if(albumType == 'P'){
                System.out.print("Input shipping address > "); shippingAddress = in.nextLine();

                Check.checkOrderInput(albumType, quantity, buyerContact, shippingAddress);

                order = new PhysicalAlbumOrder(album, quantity, LocalDate.now(), buyerName, buyerContact, discounted, shippingAddress);
            }
            else if (albumType == 'D'){
                System.out.print("Input email > "); buyerEmail = in.nextLine();

                Check.checkOrderInput(albumType, quantity, buyerContact, buyerEmail);

                order = new DigitalAlbumOrder(album, quantity, LocalDate.now(), buyerName, buyerContact, discounted, buyerEmail);
            }

            System.out.println();
            order.display();
            System.out.print("\nConfirm order? [Y/N] > "); confirm = in.nextLine().toUpperCase().charAt(0);
            Check.checkOptionInput(confirm);

            // adding the new order to orders.csv
            if(confirm == 'Y'){
                // fetch updated orders
                orders = RWcsv.readOrders();

                // convert orders to arraylist, add the new order, and convert back to array to write back to orders.csv
                ArrayList<AlbumOrder> orderList = new ArrayList<AlbumOrder>(Arrays.asList(orders));
                orderList.add(order);
                AlbumOrder[] updatedOrderList = orderList.toArray(new AlbumOrder[orderList.size()]);
                RWcsv.writeOrders(updatedOrderList);

                System.out.println("Order #" + order.getRefID() + " successfully added!");
                returnToDisplay = false; // end displayCatalog()
            }
            else if(confirm == 'N') returnToDisplay = true; // go back to displayCatalog()
        }
        catch(InputMismatchException mali){
            System.out.println("Error (" + mali.getClass().getName() + ") : " + Check.inputMismatchMsg);
        }
        catch(OptionInputException mali){
            System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
        }
        catch(OrderInputException mali){
            System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
        }

        return returnToDisplay;
    }

    public static void displayOrderHistory(AlbumOrder[] ords){
        // setups
        int page = 0;
        boolean keepDisplaying = true;
        char input, albumType;
        AlbumOrder[] orders = RWcsv.readOrders(); // ensure updated orders.csv is loaded

        do{
            try{
                // initialise indices to fetch albums from based on current page
                int startIndex = page * 10;
                int endIndex = Math.min(startIndex + 10, ords.length); // get min to prevent extra indices on last page

                System.out.println("Page " + (page+1));
                System.out.println(String.join(" | ", "\nRef #", "Album", "P/D", "Quant", "Buyer", "Purchased"));

                // for loop to print 10 Album objects at a time
                for(int i = startIndex; i < endIndex; i++){
                    if(ords[i] instanceof PhysicalAlbumOrder) albumType = 'P';
                    else albumType = 'D';

                    System.out.print((i + 1) + ". ");
                    System.out.println(String.join(" | ", Integer.toString(ords[i].getRefID()), ords[i].getAlbum().getAlName(),
                                        Character.toString(albumType), Integer.toString(ords[i].getQuantity()), ords[i].getBuyerName(),
                                        ords[i].getDatePurchased().toString()));
                }

                // print next possible options of user
                System.out.print("\n[P]revious, [N]ext, [V]iew Album Details, [C]ancel Order, [E]xit > "); input = in.nextLine().toUpperCase().charAt(0);
                Check.checkOptionInput(input, new char[]{'P', 'N', 'V', 'C', 'E'});

                switch(input){
                    case 'P':{ // prev
                        if(startIndex == 0) throw new PageIndexException(Check.prevPageMsg);
                        else page--;
                        break;
                    }
                    case 'N':{ // next
                        if(endIndex == ords.length) throw new PageIndexException(Check.nextPageMsg);
                        else page++;
                        break;
                    }
                    case 'V':{ // view album details
                        ords[page].getAlbum().display();
                        break;
                    }
                    case 'C':{ // cancel order
                        System.out.print("Input the number of the order you're cancelling (1-10) > "); int index = in.nextInt(); in.nextLine();
                        if(ords.length == orders.length) keepDisplaying = cancelOrder(index - 1);
                        else keepDisplaying = cancelOrder(Arrays.asList(orders).indexOf(ords[index - 1]));
                        break;
                    }
                    case 'E':{ // exit
                        keepDisplaying = false;
                        break;
                    }
                }
            }
            catch(OptionInputException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
            catch(PageIndexException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
        } while(keepDisplaying);
    }

    public static boolean cancelOrder(int index){
        AlbumOrder[] orders = RWcsv.readOrders();
        int confirm = 0;
        int refID = orders[index].getRefID();
        System.out.println("You are about to cancel the following order:\n");
        orders[index].display();

        do{
            try{
                System.out.print("To confirm, please re-type the reference ID > ");
                confirm = in.nextInt(); in.nextLine();
            }
            catch(InputMismatchException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
        } while(confirm != refID);

        ArrayList<AlbumOrder> orderList = new ArrayList<AlbumOrder>(Arrays.asList(orders));
        orderList.remove(orders[index]);
        AlbumOrder[] updatedOrderList = orderList.toArray(new AlbumOrder[orderList.size()]);
        RWcsv.writeOrders(updatedOrderList);

        System.out.println("Successfully cancelled Order #" + refID);
        return false; // exit keepDisplaying
    }

    public static void main(String[] args){
        boolean running = true;

        // menu
        while(running){
            try{
                System.out.println("\n"+
                                        "======================================================\n" +
                                    ".█░██░██░██░██░██░██░██░██░██░██░██░██░██░██░██░██░██░█.\n"+
                                    "| █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █|\n"+
                                    "'░██░██░██░██░,..__________---_________.,░██░██░██░██░░'\n"+
                                    "|.,.,.,.,.,.,.,.|||: Haru-Haru Store :||.,.,.,.,.,.,.,.|\n"+
                                    "'█░██ █ █ ███░█,..---------[.]--------.,''█░██ █ █ ███░|\n"+
                                    "|█░██░██░██░██░██░██░██░██░██░██░██░██░██░██░██░██░██░█|\n"+
                                    "'-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;|'\n"+
                                        "======================================================\n" +
                                    "||                                                     |\n"+
                                    "'|                                                     '\n"+
                                    "||        [1]: View and Order Available Albums :       |\n"+
                                    "'|                                                     '\n"+
                                    "||        [2]: View and Cancel Orders          :       |\n"+
                                    "'|                                                     '\n"+
                                    "||        [3]: About                           :       |\n"+
                                    "'|                                                     '\n"+
                                    "||        [4]: Exit                            :       |\n"+
                                    "'|                                                     '\n"+
                                    "||                                                     |\n"+
                                    "'|                                                     '\n"+
                                    "||                                                     |\n"+
                                    "'|...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..'\n"+
                                        "======================================================\n");

                System.out.print("Please Select Your Choice > "); char menuInput = in.nextLine().charAt(0);
                Check.checkOptionInput(menuInput, new char[]{'1', '2', '3', '4'});
                
                // switch case through displayMenu() inputs
                switch(menuInput){
                    case '1':{ // catalog
                        displayCatalog(catalog);
                        break;
                    }
                    case '2':{ // orders
                        displayOrderHistory(orders);
                        break;
                    }
                    case '3':{ // about
                        System.out.println("About Us");

                        System.out.println("\nHaru-Haru Store (PH) is your ultimate destination for K-pop enthusiasts in the Philippines. Immerse yourself in the world of Korean pop music with our extensive collection of both physical and digital K-pop albums. From the latest releases to timeless classics, Haru-Haru Store is your go-to haven for all things K-pop, bringing the vibrant beats and visuals of your favorite artists directly to you in the heart of the Philippines.");

                        System.out.println("\nStudent Programmers:" +
                        "\n\tPalao, Maria Athaliah December G." +
                        "\n\tSanchez, Francine Louis B.");

                        System.out.println("\nContact Us:" +
                        "\n\tEmail: haruharu_store231118@gmail.com" +
                        "\n\tFacebook: Haru-Haru Store (PH)" +
                        "\n\tTelephone Number: +639276578680" +
                        "\n\nCopyright 2023 HHS100s. All Rights Reserved.");
                        break;
                    }
                    case '4':{ // exit
                        System.out.print("Are you sure you want to quit the program? [Y/N] > ");
                        char userInput = in.nextLine().toUpperCase().charAt(0);
                        Check.checkOptionInput(userInput);

                        if(userInput == 'Y'){
                            System.out.println("\nExiting the program. Thank you for using Haru-Haru Store!");
                            running = false;
                        } 
                        else if(userInput == 'N'){
                            System.out.println("\nContinuing with the program.\n");
                        }
                    }
                }
            }
            catch(OptionInputException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
        }
    }
}

// exception classes
  // for menu/char inputs
class OptionInputException extends Exception{
    OptionInputException(String msg){super(msg);}
}

  // for next/prev page inputs
class PageIndexException extends Exception{
    PageIndexException(String msg){super(msg);}
}

  // for order inputs
class OrderInputException extends Exception{
    OrderInputException(String msg){super(msg);}
}

  // other general input errors
class DataInputException extends Exception{
    DataInputException(String msg){super(msg);}
}