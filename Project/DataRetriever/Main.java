package DataRetriever;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        // TODO CHANGE pathToDataDir TO SHARED FILESYSTEM
        DataRetriever dataRetriever = new DataRetriever(Path.of("D:\\Programmershit\\Project2.2-2021\\Data"));
        System.out.println(dataRetriever.retrieveStp(Path.of("85600")).stream().sorted(Comparator.reverseOrder()).limit(10).collect(Collectors.toList()));
    }
}
