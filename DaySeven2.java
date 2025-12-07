import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DaySeven2 {
    public static void main(String[] args) throws IOException {

        //list with count for current number of timelines at each column
        ArrayList<Long> timelines = new ArrayList<Long>();

        List<String> lines = Files.readAllLines(new File("input7.txt").toPath());
        int columns = lines.getFirst().length();

        for (String line : lines) {

            //build an empty list of timeline counts
            ArrayList<Long> newTimeLines = new ArrayList<Long>(columns);
            for (int i = 0; i < columns; i++)
                newTimeLines.add(0L);

            //special case for first line
            if (line.contains("S")) {
                newTimeLines.set(line.indexOf("S"), Long.valueOf(1));
            } else {

                for (int i = 0; i < columns; i++) {
                    //for each active timeline, check for a new split
                    Long timelineCount = timelines.get(i);
                    if (timelineCount != 0) {

                        char path = line.charAt(i);
                        if (path == '^') {
                            //create new splits, merging count with the old count
                            newTimeLines.set(i - 1, newTimeLines.get(i - 1) + timelineCount);
                            newTimeLines.set(i + 1, newTimeLines.get(i + 1) + timelineCount);
                        } else {
                            newTimeLines.set(i, newTimeLines.get(i) + timelineCount);
                        }
                    }
                }
            }

            //replace list of timelines with new list
            timelines = newTimeLines;
        }

        //count em up
        long total = 0;
        for (Long timelineCount : timelines) {
            total += timelineCount;
        }
        System.out.println(total);
    }
}
