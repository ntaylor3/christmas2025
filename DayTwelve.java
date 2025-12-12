import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class DayTwelve {
    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(new File("input12.txt").toPath());
        int successCount = 0;
        for (String line : lines) {
            if(line.contains("x")){
                
                String[] parts = line.split(":");
                
                //figure out how many simple 3x3 boxes fit under the tree
                String[] dimensions = parts[0].trim().split("x");
                int simpleBoxCount = (Integer.parseInt(dimensions[0]) / 3) * (Integer.parseInt(dimensions[1]) / 3);

                //count the total presents
                parts[1] = parts[1].trim();
                parts = parts[1].split(" ");
                int presentCount = 0;
                for(int j=0; j<6; j++) {
                    presentCount += Integer.parseInt(parts[j]);
                }

                //check if the presents fit
                if(presentCount <= simpleBoxCount) {
                    successCount++;
                }
            }
        }

        System.out.println(successCount);
    }
}