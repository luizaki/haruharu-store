import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Check {
    final static String inputMismatchMsg = "Invalid data type.";
    final static String oobIndexMsg = "Invalid input. There is no item listed at that number.";
    final static String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; // regex from https://howtodoinjava.com/

    public static void checkOptionInput(char input, char[] validInputs) throws OptionInputException{
        boolean flag = false;
        String validInputString = "";

        // check if input is part of valid inputs
        for(char c : validInputs){
            if(c == input) flag = true;
        }

        // combine valid inputs into a String for error msg
        for(int i = 0; i < validInputs.length - 1; i++){
            validInputString += "'" + validInputs[i] + "', ";
        }

        if(!flag){
            throw new OptionInputException("Invalid option. Please only input " + validInputString + "or '" + validInputs[validInputs.length-1] + "'.");
        }
    }

    // alternate checkOptionInput for String options
    public static void checkOptionInput(String input, String[] validInputs) throws OptionInputException{
        boolean flag = false;
        String validInputString = "";
        
        for(String s : validInputs){
            if(s.equals(input)) flag = true;
        }

        // combine valid inputs into a String for error msg
        for(int i = 0; i < validInputs.length - 1; i++){
            validInputString += "'" + validInputs[i] + "', ";
        }

        if(!flag){
            throw new OptionInputException("Invalid option. Please only input " + validInputString + "or '" + validInputs[validInputs.length-1] + "'.");
        }
    }

    // another alternate checkOptionInput to default validInputs to Y/N
    public static void checkOptionInput(char input) throws OptionInputException{
        char[] validInputs = {'Y', 'N'};
        boolean flag = false;

        for(char c : validInputs){
            if(c == input) flag = true;
        }

        if(!flag){
            throw new OptionInputException("Invalid option. Please only input 'Y', or 'N'.");
        }
    }

    public static void checkOrderInput(char albumType, int quantity, long contact, String addEmail) throws OrderInputException{
        // check quantity range
        if(quantity < 1) throw new OrderInputException("Quantity is too low. You must order at least one unit of an album.");
        if(quantity > 50) throw new OrderInputException("Quantity is too high. You cannot order more than 50 albums at once.");

        // check contact length
        if(contact <  9000000000L || contact > 9999999999L) throw new OrderInputException("Invalid contact number. Contact should be 10-11 digits (11 including 0).");

        // branch if String input is address or email
        if(albumType == 'P'){ // check address
            if(addEmail.matches(regex)) throw new OrderInputException("Invalid address. Do not input an email as your shipping address.");
            if(addEmail.contains(",")) throw new OrderInputException("Invalid address. Address should not contain commas (replace with ';' if needed)");
        }
        else if(albumType == 'D'){ // check email
            if(!(addEmail.matches(regex))) throw new OrderInputException("Invalid email. Please use a working email address.");
        }
    }

    public static void matchArtist(String input) throws DataInputException{
        Album[] catalog = RWcsv.readCatalog();
        
        // retrieve list of artists in catalog and remove dupes
        ArrayList<String> names = new ArrayList<String>();
        for(Album al : catalog) names.add(al.getArtist().toUpperCase());
        Set<String> unique = new HashSet<String>(names);
        names.clear(); names.addAll(unique);

        // check if match exists
        boolean match = false;
        for(int j = 0; j < names.size(); j++){
            if(input.equals(names.get(j))) match = true;
        }

        if(!match) throw new DataInputException("Artist not found.");
    }
}
