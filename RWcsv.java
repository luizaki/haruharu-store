import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RWcsv{
    public static Album[] readCatalog() { // nakaw from last sem
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
            LocalDate release = LocalDate.parse(albumInfo[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            double price = Double.parseDouble(albumInfo[4]);

            // construct Album object
            albums[i] = new Album(alName, artist, artistType, release, price);
        }

        return albums;
    }
}
