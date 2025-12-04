import java.io.*;
import java.util.Vector;

public class DayFour2 {
    public static void main(String[] args) throws IOException {

        // read all lines from input file
        BufferedReader bf = new BufferedReader(new FileReader("input4.txt"));
        Vector<String> lines = new Vector<String>();
        while (bf.ready()) {
            String line = bf.readLine();
            lines.add(line);
        }

        // figure the height and width of the grid
        int height = lines.size();
        int width = lines.get(0).length();

        // feed characters into an array
        char[][] shelves = new char[height][width];
        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            for (int j = 0; j < width; j++) {
                shelves[i][j] = line.charAt(j);
            }
        }

        int accessible = 0;

        // iterate array and check each direction from the current position
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (shelves[i][j] == '@') {
                    int blocked = 0;

                    if (i > 0) {

                        if (j > 0 && shelves[i - 1][j - 1] == '@')
                            blocked++;
                        if (shelves[i - 1][j] == '@')
                            blocked++;
                        if (j < width - 1 && shelves[i - 1][j + 1] == '@')
                            blocked++;
                    }

                    if (j > 0 && shelves[i][j - 1] == '@')
                        blocked++;
                    if (j < width - 1 && shelves[i][j + 1] == '@')
                        blocked++;

                    if (i < height - 1) {
                        if (j > 0 && shelves[i + 1][j - 1] == '@')
                            blocked++;
                        if (shelves[i + 1][j] == '@')
                            blocked++;
                        if (j < width - 1 && shelves[i + 1][j + 1] == '@')
                            blocked++;
                    }

                    // where successful, remove the roll, then rewind the counters
                    if (blocked < 4) {
                        shelves[i][j] = '.';
                        i -= 1;
                        if (i < 0)
                            i = 0;
                        j = -1;
                        accessible++;
                    }
                }

                System.out.println(accessible);
            }
        }

        bf.close();

    }
}