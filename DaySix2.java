import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Vector;

public class DaySix2 {

    private Vector<Long> values = new Vector<Long>();
    private char operand = ' ';

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new FileReader("input6.txt"));

        // figure out the length of the longest line
        int maxLength = 0;
        String line = "";
        while (bf.ready()) {
            line = bf.readLine();
            if (line.length() > maxLength)
                maxLength = line.length();
        }
        bf.close();

        // check the number of operands to figure out how many sums we have
        line = line.replace(" ", "").trim();
        int numOfSums = line.length();

        // create empty sum objects
        Vector<DaySix2> sums = new Vector<DaySix2>(numOfSums);
        for (int i = 0; i < numOfSums; i++)
            sums.add(new DaySix2());

        // count backwards from the end of the longest line...
        List<String> lines = Files.readAllLines(new File("input6.txt").toPath());
        int currentSum = numOfSums - 1;
        boolean endFound = false;
        for (int i = maxLength - 1; i >= 0; i--) {

            // for each column, read down all the lines building the number as we go
            String buffer = "";
            for (String l : lines) {

                // check for short lines
                if (l.length() > i) {
                    char c = l.charAt(i);
                    // add operand if found, note that we've reached the end of this sum
                    if (c == '*' || c == '+') {
                        sums.get(currentSum).setOperand(c);
                        endFound = true;
                    }
                    // otherwise, if its not blank, add to the buffer
                    else if (c != ' ')
                        buffer += c;
                }
            }

            // if we have a number in the buffer, add it to the current sum
            if (!buffer.equals("")) {
                sums.get(currentSum).addValue(Long.parseLong(buffer));
            }
            // if we found an operand, move to the next sum
            if (endFound) {
                currentSum--;
                endFound = false;
            }
        }

        // add up the total
        long solution = 0;
        for (DaySix2 sum : sums) {
            solution += sum.calculate();
        }

        System.out.println(solution);

    }

    // add a value to be include in this sum
    public void addValue(long value) {
        values.add(value);
    }

    // set + or * as the operand
    public void setOperand(char operand) {
        this.operand = operand;
    }

    // work out the answer for this su
    public long calculate() {
        if (operand == '+') {
            long sum = 0;
            for (long val : values) {
                sum += val;
            }
            return sum;
        } else {
            long product = 1;
            for (long val : values) {
                product *= val;
            }
            return product;
        }
    }
}