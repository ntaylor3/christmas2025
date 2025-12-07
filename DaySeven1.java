import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;

public class DaySeven1 {
    public static void main(String[] args) throws IOException {

        //using a hash set to take care of merging beams
        HashSet<Long> beams = new HashSet<Long>();
        List<String> lines = Files.readAllLines(new File("input7.txt").toPath());
        long splits = 0;

        for (String line : lines) {

            HashSet<Long> newBeams = new HashSet<Long>();

            // special case for first line
            if (beams.size() == 0) {
                newBeams.add((long) line.indexOf('S'));
            } else {
                //where a split is found, add a pair of beams to the set
                for (Long beam : beams) {
                    char path = line.charAt(beam.intValue());
                    if (path == '^') {
                        newBeams.add(beam - 1);
                        newBeams.add(beam + 1);
                        splits++;
                    } else
                        newBeams.add(beam);
                }
            }

            //replace old beams set with the new one
            beams = newBeams;
        }

        System.out.println(splits);

    }
}
