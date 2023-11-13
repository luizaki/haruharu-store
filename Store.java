import java.util.*;

public class Store{
    static Scanner in = new Scanner(System.in);
    static Album[] catalog = RWcsv.readCatalog();

    // displayCatalog(catalog)
        // initialise page to 0
        // while loop to keep displaying
            // set up starting and ending indices based on page

            // print header
            // for loop to print each Album object (number each to 1-10)

            // print options to prev page, next page, place order, filter, exit display
                // next page
                    // page += 1 if not in max page
                    // loop restarts
                // prev page
                    // page -= 1 if not in min page
                    // loop restarts
                // place order
                    // let user input which album based on current numbering and fetch that album
                    // match album to catalog and retrieve index
                    // call placeOrder(index)
                // filter
                    // ask to filter on album name/artist/artist type or none/reset
                    // if none/reset, displayCatalog(catalog)
                    // if album name/artist, have user input and match if those exist in catalog
                    // if type, menu for boy group/girl group/soloist/coed

                    // if not none/reset, make new array from catalog and filter based on input
                    // displayCatalog(newArray)
                // exit
                    // end loop and method


    // placeOrder(index)
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
        // logIn() (TENTATIVE FEATURE)

        // display menu until input is valid
        
        // switch case through displayMenu() inputs
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