public class Check {
    static String inputMismatchMsg = "Invalid data type.";
    static String prevPageMsg = "You cannot move any behind. Returning to page 1.";
    static String nextPageMsg = "You cannot move any further. Returning to last page.";

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

    // alternate checkOptionInput to default validInputs to Y/N
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
        
    }
}
