import java.io.*;
import java.util.*;

public class DayFive2 {

    private long low;
    private long high;

    public static void main(String[] args) throws IOException {

        // read all lines and create range objects
        BufferedReader bf = new BufferedReader(new FileReader("input5.txt"));
        Vector<DayFive2> ranges = new Vector<DayFive2>();
        while (bf.ready()) {
            String line = bf.readLine();

            // stop when we get to blank line
            if (line.equals(""))
                break;

            String[] parts = line.split("-");
            ranges.add(new DayFive2(Long.parseLong(parts[0]), Long.parseLong(parts[1])));

        }

        // check for overlapping ranges and combine them
        boolean overlapFound = true;
        while (overlapFound) {
            overlapFound = false;
            for (int i = 0; i < ranges.size(); i++) {
                for (int j = i + 1; j < ranges.size(); j++) {
                    DayFive2 range1 = ranges.get(i);
                    DayFive2 range2 = ranges.get(j);
                    if (range1.canCombine(range2)) {
                        range1.expand(range2.low, range2.high);
                        ranges.remove(range2);
                        overlapFound = true;
                    }
                }
            }
        }

        long totalSize = 0;
        for (DayFive2 range : ranges) {
            System.out.println("Range: " + range.low + "-" + range.high + " Size: " + range.size());
            totalSize += range.size();
        }

        System.out.println(totalSize);

        bf.close();

    }

    public DayFive2(long low, long high) {
        this.low = low;
        this.high = high;
    }

    // check if the specified range overlaps with this one
    public boolean canCombine(long low, long high) {
        return (low <= this.low && high >= this.low) || (low <= this.high && high >= this.low);
    }

    // check if the specified range overlaps with this one
    public boolean canCombine(DayFive2 other) {
        return canCombine(other.low, other.high);
    }

    // expand this range to include the specified range
    public void expand(long low, long high) {
        if (low < this.low)
            this.low = low;
        if (high > this.high)
            this.high = high;
    }

    // number of values in this range
    public long size() {
        return high - low + 1;
    }
}