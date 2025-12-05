import java.io.*;
import java.util.Vector;

public class DayFive1 {

    private long low;
    private long high;

    public static void main(String[] args) throws IOException {

        // read first block of lines from input file and create range objects
        BufferedReader bf = new BufferedReader(new FileReader("input5.txt"));
        Vector<DayFive1> ranges = new Vector<DayFive1>();
        while (bf.ready()) {
            String line = bf.readLine();
            if (line.equals(""))
                break;
            String[] parts = line.split("-");
            ranges.add(new DayFive1(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        }

        long count = 0;

        //read second block of lines and check if they are in any of the ranges
        while (bf.ready()) {
            String line = bf.readLine();
            long value = Long.parseLong(line);

            for (DayFive1 range : ranges) {
                if (range.isInRange(value)) {
                    count++;
                    break;
                }
            }
        }

        System.out.println(count);

        bf.close();
    }

    public DayFive1(long low, long high) {
        this.low = low;
        this.high = high;
    }

    //check if the specified value is in this range
    public boolean isInRange(long value) {
        return (value >= low && value <= high);
    }
}