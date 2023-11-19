import java.util.*;
import java.time.LocalDate;
import java.text.DecimalFormat;

public class Store{
    static Scanner in = new Scanner(System.in);
    static Album[] catalog = RWcsv.readCatalog();
    static AlbumOrder[] orders = RWcsv.readOrders();

    public static void displayCatalog(Album[] albums){
        // setups
        int page = 0;
        int maxPage = (albums.length+10-1)/10;
        boolean keepDisplaying = true;
        char input;
        
        DecimalFormat df = new DecimalFormat("Php #,##0.00");

        do{
            try{
                // initialise indices to fetch albums from based on current page
                int startIndex = page * 10;
                int endIndex = Math.min(startIndex + 10, albums.length); // get min to prevent extra indices on last page

                System.out.println("\n[ PAGE " + (page+1) + " / " + maxPage + " ]\n");
                System.out.printf("     %-43s | %-19s | %-12s | %-10s | %-1s ","Name", "Artist", "Artist Type", "Release", "Price");
                System.out.println("");
                System.out.println("------------------------------------------------------------------------------------------------------------------");

                // for loop to print 10 Album objects at a time
                for(int i = startIndex; i < endIndex; i++){
                    System.out.printf("%-4s %-43s | %-19s | %-12s | %-10s | %-1s ",((i + 1) + ". "),albums[i].getAlName(), albums[i].getArtist(),
                    albums[i].getArtistType(), albums[i].getRelease().toString(),(df.format(albums[i].getPrice())));

                    System.out.println("");
            }

                // print next possible options of user
                System.out.print("\n[P]revious, [N]ext, [F]ilter, [O]rder, [E]xit > "); input = in.nextLine().toUpperCase().charAt(0);
                Check.checkOptionInput(input, new char[]{'P', 'N', 'F', 'O', 'E'});
                System.out.println("");
                
                switch(input){
                    case 'P':{ // prev
                        if(page == 0) page = maxPage - 1; // go to last page if Prev is on page 1
                        else page--;
                        break;
                    }
                    case 'N':{ // next
                        if(page == maxPage - 1) page = 0; // go to page 1 if Next is on last page
                        else page++;
                        break;
                    }
                    case 'F':{ // filter
                        keepDisplaying = false; // to avoid nested displayCatalog()s
                        filterCatalog();
                        break;
                    }
                    case 'O':{ // order
                        System.out.print("Input the number of the album you're ordering > "); String indexInput = in.nextLine();
                        
                        int index = Integer.parseInt(indexInput);
                        if(index > albums.length || index < 1) throw new DataInputException(Check.oobIndexMsg);
                        
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
            catch(NumberFormatException mali){
                System.out.println("Error (NumberFormatException) : " + Check.inputMismatchMsg);
            }
            catch(OptionInputException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
            catch(DataInputException mali){
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

        System.out.println("\nYou will be ordering the following:\n");
        album.display();

        try{ // inputs incl. cancel typing to return to display
            // physical or digital
            System.out.println("\n[Input Details] (feel free to type 'cancel' any time)");
            System.out.print("\tWould you like [P]hysical or [D]igital copies? > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else{
                albumType = input.toUpperCase().charAt(0);                    
                Check.checkOptionInput(albumType, new char[]{'P', 'D'});
            }

            // quantity
            System.out.print("\tInput quantity > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else quantity = Integer.parseInt(input);

            // name
            System.out.print("\tInput full name > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else buyerName = input;

            // contact num
            System.out.print("\tInput contact # > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else buyerContact = Long.parseLong(input);

            // discount
            System.out.print("\tAre you a student/senior citizen/PWD? [Y/N] > "); input = in.nextLine();
            if(input.toLowerCase().equals("cancel")) return true;
            else{
                hasDiscount = input.toUpperCase().charAt(0);
                Check.checkOptionInput(hasDiscount);

                if(hasDiscount == 'Y') discounted = true;
                else if(hasDiscount == 'N') discounted = false;
            }

            // branch to physical and digital, check inputs and create Object
            if(albumType == 'P'){
                System.out.print("\tInput shipping address > "); shippingAddress = in.nextLine();

                Check.checkOrderInput(albumType, quantity, buyerContact, shippingAddress);

                order = new PhysicalAlbumOrder(album, quantity, LocalDate.now(), buyerName, buyerContact, discounted, shippingAddress);
            }
            else if (albumType == 'D'){
                System.out.print("\tInput email > "); buyerEmail = in.nextLine();

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
        catch(NumberFormatException mali){
            System.out.println("Error (NumberFormatException) : " + Check.inputMismatchMsg);
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
        int maxPage = (ords.length+10-1)/10;
        boolean keepDisplaying = true;
        char input, albumType;
        AlbumOrder[] updated = null;
        AlbumOrder[] orders = RWcsv.readOrders();

        do{
            try{
                // initialise indices to fetch albums from based on current page
                int startIndex = page * 10;
                int endIndex = Math.min(startIndex + 10, ords.length); // get min to prevent extra indices on last page

                System.out.println("\n[ PAGE " + (page+1) + " / " + maxPage + " ]\n");
                System.out.printf("     %-8s | %-43s | %-3s | %-8s | %-18s | %-1s ", "Ref #", "Album", "P/D", "Quantity", "Buyer", "Purchased");
                System.out.println("");
                System.out.println("------------------------------------------------------------------------------------------------------------------");

                // for loop to print 10 Album objects at a time
                for(int i = startIndex; i < endIndex; i++){
                    if(ords[i] instanceof PhysicalAlbumOrder) albumType = 'P';
                    else albumType = 'D';

                    System.out.printf("%-4s %-8s | %-43s | %-3s | %-8s | %-18s | %-1s ",((i + 1) + ". "), Integer.toString(ords[i].getRefID()), ords[i].getAlbum().getAlName(),
                                        Character.toString(albumType), Integer.toString(ords[i].getQuantity()), ords[i].getBuyerName(),
                                        ords[i].getDatePurchased().toString());

                                        System.out.println("");
                }

                // print next possible options of user
                System.out.print("\n[P]revious, [N]ext, [V]iew Details, [F]ilter, [C]ancel Order, [E]xit > "); input = in.nextLine().toUpperCase().charAt(0);
                Check.checkOptionInput(input, new char[]{'P', 'N', 'V', 'F', 'C', 'E'});
                System.out.println("");

                switch(input){
                    case 'P':{ // prev
                        if(page == 0) page = maxPage - 1; // go to last page if Prev is on page 1
                        else page--;
                        break;
                    }
                    case 'N':{ // next
                        if(page == maxPage - 1) page = 0; // go to page 1 if Next is on last page
                        else page++;
                        break;
                    }
                    case 'V':{ // view order details
                        System.out.print("Input the number of the order you'd like to view > "); String indexInput = in.nextLine();
                        
                        int index = Integer.parseInt(indexInput);
                        if(index > ords.length || index < 1) throw new DataInputException(Check.oobIndexMsg);

                        if(ords.length == orders.length){System.out.println(); ords[index - 1].display();}
                        else orders[Arrays.asList(orders).indexOf(ords[index - 1])].display();
                        break;
                    }
                    case 'F':{ // filter
                        System.out.print("Would you like to filter orders by [P]hysical, [D]igital, or [R]eset? > "); char type = in.nextLine().toUpperCase().charAt(0);
                        Check.checkOptionInput(type, new char[]{'P', 'D', 'R'});

                        // filter based on physical/digital
                        if(type == 'P')
                            updated = Arrays.stream(orders)
                                    .filter(a -> a instanceof PhysicalAlbumOrder)
                                    .toArray(AlbumOrder[]::new);
                        else if(type == 'D')
                            updated = Arrays.stream(orders)
                                    .filter(a -> a instanceof DigitalAlbumOrder)
                                    .toArray(AlbumOrder[]::new);
                        else if(type == 'R'){
                            updated = RWcsv.readOrders();
                        }
                        
                        keepDisplaying = false; // aovid nested displays
                        displayOrderHistory(updated);
                        break;
                    }
                    case 'C':{ // cancel order
                        System.out.print("Input the number of the order you're cancelling > "); String indexInput = in.nextLine();
                        
                        int index = Integer.parseInt(indexInput);
                        if(index > ords.length || index < 1) throw new DataInputException(Check.oobIndexMsg);

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
            catch(InputMismatchException mali){
                System.out.println("Error (InputMismatchException) : " + Check.inputMismatchMsg);
            }
            catch(NumberFormatException mali){
                System.out.println("Error (NumberFormatException) : " + Check.inputMismatchMsg);
            }
            catch(OptionInputException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
            catch(DataInputException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
        } while(keepDisplaying);
    }

    public static boolean cancelOrder(int index){
        AlbumOrder[] orders = RWcsv.readOrders();
        String input;
        int confirm = 0;
        boolean returnToDisplay = true;

        int refID = orders[index].getRefID();
        System.out.println("You are about to cancel the following order:\n");
        orders[index].display();

        while(true){
            try{
                System.out.print("\nTo confirm, please type the reference ID or 'cancel' > "); input = in.nextLine();
                
                if(input.equals("cancel")){
                    returnToDisplay = true;
                    break;
                }

                confirm = Integer.parseInt(input);
                if(confirm != refID){
                    throw new DataInputException("Reference ID did not match.");
                }
                else{
                    ArrayList<AlbumOrder> orderList = new ArrayList<AlbumOrder>(Arrays.asList(orders));
                    orderList.remove(orders[index]);
                    AlbumOrder[] updatedOrderList = orderList.toArray(new AlbumOrder[orderList.size()]);
                    RWcsv.writeOrders(updatedOrderList);

                    System.out.println("Successfully cancelled Order #" + refID);

                    returnToDisplay = false;
                    break;
                }
            }
            catch(InputMismatchException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + Check.inputMismatchMsg);
            }
            catch(DataInputException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + mali.getMessage());
            }
        }

        return returnToDisplay; // exit keepDisplaying
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

                System.out.print("\nPlease Select Your Choice > "); char menuInput = in.nextLine().charAt(0);
                Check.checkOptionInput(menuInput, new char[]{'1', '2', '3', '4'});
                System.out.println("");
                
                // switch case through displayMenu() inputs
                switch(menuInput){
                    case '1':{ // catalog
                        displayCatalog(catalog);
                        break;
                    }
                    case '2':{ // orders
                        displayOrderHistory(RWcsv.readOrders());
                        break;
                    }
                    case '3':{ // about
                        System.out.println("\nAbout Us");

                        System.out.println("\nHaru-Haru Store (PH) is your ultimate destination for K-pop enthusiasts in the Philippines. Immerse yourself in the world of Korean pop music with our extensive collection of both physical and digital K-pop albums. From the latest releases to timeless classics, Haru-Haru Store is your go-to haven for all things K-pop, bringing the vibrant beats and visuals of your favorite artists directly to you in the heart of the Philippines.");

                        System.out.println("\nStudent Programmers:" +
                        "\n\tPalao, Maria Athaliah December G." +
                        "\n\tSanchez, Francine Louise B.");

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
            catch(InputMismatchException mali){
                System.out.println("Error (" + mali.getClass().getName() + ") : " + Check.inputMismatchMsg);
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

  // for order inputs
class OrderInputException extends Exception{
    OrderInputException(String msg){super(msg);}
}

  // other general input errors
class DataInputException extends Exception{
    DataInputException(String msg){super(msg);}
}