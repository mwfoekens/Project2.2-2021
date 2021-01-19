package DataRetriever;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // CHANGE pathToDataDir TO SHARED FILESYSTEM
        DataRetriever dataRetriever = new DataRetriever("D:\\Programmershit\\Project2.2-2021\\Data");
        dataRetriever.retrieveData("85600");
    }
}
