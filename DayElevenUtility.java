import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class DayElevenUtility {
    public static void main(String[] args) throws IOException, InterruptedException {

        // this is a utility to convert the input into a graphviz format for
        // visualization
        List<String> lines = Files.readAllLines(new File("input11.txt").toPath());
        String outcome = "";
        for (String line : lines) {

            String[] parts = line.split(" ");
            String origin = parts[0].substring(0, 3);
            for (int i = 1; i < parts.length; i++) {
                String destination = parts[i].substring(0, 3);
                System.out.println(origin + "->" + destination);
                outcome += origin + "->" + destination + ";\n";

            }
        }
        Files.writeString(new File("graph11.txt").toPath(), outcome);
    }
}