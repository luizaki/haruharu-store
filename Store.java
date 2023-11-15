import java.util.*;

public class Store{
    static Scanner in = new Scanner(System.in);
    static Album[] catalog = RWcsv.readCatalog();

    
  // displayCatalog(catalog)
  public static void displayCatalog(Album catalog){
  // initialise page to 0
    int page = 0;
  //while loop to keep displaying
    while(true){
      // set up starting and ending indices based on page
      int startind = page;
      int endind = catalog.length();
      // print header
      System.our.println("Album Catalog:");
      // for loop to print each Album object (number each to 1-10)
      for (int i = startind; i < endind){
        System.out.println("Album #", page);
        System.out.println("Album name: ", catalog[i].getAlName());
        System.out.println("Artist name: ", catalog[i].getArtist());
        System.out.println("Artist Type: ", catalog[i].getArtistType());
        System.out.println("Release Date: ", catalog[i].getReleaseDate());
        System.our.println("Price: ", catalog[i].getPrice());
      
      
      // print options to prev page, next page, place order, filter, exit display
        System.out.println("1. Previous page\n2. Next page\n3. Place order\n4. Filter\n5. Exit display"); int options = in.nextInt();
        switch(options){
        // next page
            case 1:
            // page += 1 if not in max page
              page++;
              break;
            // loop restarts
         // prev page
            case 2:
            // page -= 1 if not in min page
              page--;
              break;
            // loop restarts
        // place order
            case 3:
              int index = page - 1;
            // let user input which album based on current numbering and fetch that album
            // match album to catalog and retrieve index
            // call placeOrder(index)
            placeOrder(index)
        // filter
            case 4:
            // ask to filter on album name/artist/artist type or none/reset
            // if none/reset, displayCatalog(catalog)
            // if album name/artist, have user input and match if those exist in catalog
            // if type, menu for boy group/girl group/soloist/coed
      
            // if not none/reset, make new array from catalog and filter based on input
            // displayCatalog(newArray)
        // exit
            // end loop and method
        }
      }
  }
}

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
      int code = 0;
      System.out.println("==== Menu ====");
      System.out.print("[1] See available albums\n[2] Cancel an order\n[3] View purchase history\n[4] Exit");
      String choice = in.nextLine();
      while(true){
          try{
              switch(choice){
                  case "1":
                      displayCatalog(catalog);
                      break;
                  case "2":
                      cancelOrder(index);
                      break;
                  case "3":
                      displayOrderHistory(orders);
                      break;
                  case "4":
                      System.out.print("Terminating Program.");
                      System.exit(0);
                  default:
                      System.out.print("Invalid input. Only allowed input --> 1-4.")
              }

          } catch(InputMismatchException e){
            code = 1;
              System.out.println(StoreMessages.msg[code]);
          } catch(StoreException e){
            System.out.println(e.getMessage());
          }
      }
  }   

  public static void check(int refID, String email) throws StoreException{
    int code= 2;
    if ((refID instanceof Integer) == false)
      throw new StoreException(StoreMessage.msg[code]);
    
  }
  public static void check(String email) throws StoreException{
    int code = 3;
    if (!email.contains("@") || !email.contains("."))
      throw new StoreException(StoreMessage.msg[code]);
  }

    // exception classes go here
        // THINGS TO THROW:
        // Invalid Menu Input
        // Invalid DataTypes
        // Invalid Email
        // Other general invalid inputs
  
  public class StoreMessages {
    public static String[] msg = {
      "Valid input. Please Proceed", //0
      "Input is invalid.", //1
      "Invalid Data Type", //2
      "Invalid Email", //3
    };
  }

  public class StoreException extends Exception{
      public StoreException(String s){
          super(s);
      }
  }
  
}
