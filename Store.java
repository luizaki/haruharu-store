import java.util.*;

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
            // initialise indices to fetch albums from based on current page
            int startIndex = page * 10;
            int endIndex = Math.min(startIndex + 10, albums.length); // get min to prevent extra indices on last page

            System.out.println("Page " + (page+1));
            System.out.println("Name " + "Artist " + "Artist Type " + "Release " + "Price");

            // for loop to print 10 Album objects at a time
            for(int i = startIndex; i < endIndex; i++){
                System.out.print((i + 1) + ". ");
                System.out.println(String.join(" | ", albums[i].getAlName(), albums[i].getArtist(),
                albums[i].getArtistType(), albums[i].getRelease().toString(), Double.toString(albums[i].getPrice())));
            }

            // print next possible options of user
            System.out.print("[P]revious, [N]ext, [F]ilter, [O]rder, [E]xit > "); input = in.nextLine().toUpperCase().charAt(0);

            switch(input){
                case 'P':{ // prev
                    if(startIndex == 0){
                        // throw error?
                    }
                    else page--;
                    break;
                }
                case 'N':{ // next
                    if(endIndex == albums.length){
                        // throw error?
                    }
                    else page++;
                    break;
                }
                case 'F':{ // filter
                    keepDisplaying = false; // to avoid nested displayCatalog()s
                    filterCatalog();
                    break;
                }
                case 'O':{ // order
                    // System.out.print("Input the number of the album you're ordering > "); int index = in.nextInt();
                    // placeOrder(index-1);
                    break;
                }
                case 'E':{ // exit
                    keepDisplaying = false;
                    break;
                }
                default:{
                    // throw error?
                }
            }
        } while(keepDisplaying);
    }

    public static void filterCatalog(){
        System.out.print("Filter on: [A]rtist, [T]ype, or [R]eset > "); char filter = in.nextLine().toUpperCase().charAt(0);

        switch(filter){
            case 'A':{ // artist
                System.out.print("Input name of artist > "); String artist = in.nextLine().toUpperCase();

                // retrieve list of artists in catalog and remove dupes
                ArrayList<String> names = new ArrayList<String>();
                for(Album al : catalog) names.add(al.getArtist().toUpperCase());
                Set<String> unique = new HashSet<String>(names);
                names.clear(); names.addAll(unique);

                // check if match exists
                boolean match = false;
                for(int j = 0; j < names.size(); j++){
                    if(artist.equals(names.get(j))) match = true;
                }

                if(match){
                    Album[] updated = Arrays.stream(catalog)
                                            .filter(a -> artist.equals(a.getArtist().toUpperCase()))
                                            .toArray(Album[]::new);
                    displayCatalog(updated);
                }
                else{
                    // throw error?
                }
                break;
            }
            case 'T':{ // type
                System.out.print("Input [BG] Boy Group, [GG] Girl Group, [CE] CO-ED Group, [BS] Boy Soloist, [GS] Girl Soloist > "); String type = in.nextLine().toUpperCase();

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
            default:{
                // throw error?
            }
        }
    }

    public static void placeOrder(int index){
        // set default values for each field
        // set album to index based on catalog
        // confirm if they'd like to place order on the given album

        // while loop for inputs
            // input physical or digital
            // input quantity
            // input buyer details
            // branch inputs between physiscal or digital
            // create object
            // AlbumOrder.display()
            // ask user to confirm details, exit loop if confirmed

        // readOrders()
        // convert orders array to ArrayList
        // add new AlbumOrder object
        // convert ArrayList back to array
        // writeOrders()

        // success message
    }

    // viewOrderHistory()
                    // if not none/reset, make new array from catalog and filter based on input
                    // displayCatalog(newArray)
                // exit
                    // end loop and method

    // cancelOrder(index)
        // while loop for input validation
            // AlbumOrder.display()
            // ask user to confirm cancellation by reinputting refID, exit loop if confirmed

        // readOrders()
        // find index of object using refID
        // convert orders array to ArrayList
        // remove object based on index
        // convert ArrayList back to array
        // writeOrders()

        // success message

    // displayOrderHistory(orders)
        // initialise page to 0
        // while loop to keep displaying
            // set up starting and ending indices based on page

            // print header
            // for loop to print each AlbumOrder object (number each to 1-10)

            // print options to prev page, next page, cancel order, exit display
                // next page
                    // page += 1 if not in max page
                    // loop restarts
                // prev page
                    // page -= 1 if not in min page
                    // loop restarts
                // cancel order
                    // let user input which order based on current numbering and fetch that order
                    // match refID to catalog and retrieve index
                    // call cancelOrder(index)
                // exit
                    // end loop and method

    public static void main(String[] args){
        displayCatalog(catalog);
        // logIn() (TENTATIVE FEATURE)

        // do displayMenu() while input is invalid
        // display menu until input is valid

        // switch case through displayMenu() inputs
            // search available albums
            // place an order
            // see available albums (displayCatalog())
            // cancel an order
            // view purchase history
            // log out/exit
    }

    // exception classes go here
        // THINGS TO THROW:
        // Invalid Menu Input
        // Invalid DataTypes
        // Invalid Email
        // Other general invalid inputs
}