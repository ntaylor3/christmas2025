import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

// A previous attempt trying to be clever with recursion, 
// works for the sample data but never completes for the real data!!!

public class DaySeven2a {

    private static List<String> lines;
    public static void main(String[] args) throws IOException {
        
        lines = Files.readAllLines(new File("input7.txt").toPath());
        int s = lines.getFirst().indexOf("S");
        long timelines = countTimelines(s, 1);
        System.out.println(timelines);
    }

    public static long countTimelines(int beamIndex, int lineNo) {
        
        //reached the end of the line!
        if(lineNo >= lines.size()) {
            return 1;
        }

        //either spawn two new timelines and return their combined total, or continue this timeline
        char path = lines.get(lineNo).charAt(beamIndex);
        if (path == '^') {
            return countTimelines(beamIndex - 1, lineNo + 1) + countTimelines(beamIndex + 1, lineNo + 1);
        } else {
            return countTimelines(beamIndex, lineNo + 1);
        }
    }
}
