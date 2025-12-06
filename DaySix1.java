import java.io.*;
import java.util.Vector;

public class DaySix1 {

    private Vector<Long> values = new Vector<Long>();
    private char operator = ' ';

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new FileReader("input6.txt"));
        Vector<DaySix1> sums = new Vector<DaySix1>();

        while (bf.ready()) {

            // trim the line down and replace any double spaces
            String line = bf.readLine();
            while (line.contains("  "))
                line = line.replace("  ", " ").trim();
            String[] parts = line.split(" ");

            for (int i = 0; i < parts.length; i++) {
                // if we don't have a sum object for this column yet, create it
                if (sums.size() <= i)
                    sums.add(new DaySix1());

                // add value or operator as appropriate
                if (parts[i].equals("+") || parts[i].equals("*")) {
                    sums.get(i).setOperator(parts[i].charAt(0));
                } else
                    sums.get(i).addValue(Long.parseLong(parts[i]));
            }
        }

        long solution = 0;

        // add up all the sums
        for (DaySix1 sum : sums)
            solution += sum.calculate();

        System.out.println(solution);

        bf.close();
    }

    // add a value to be include in this sum
    public void addValue(long value) {
        values.add(value);
    }

    // set + or * as the operator
    public void setOperator(char operator) {
        this.operator = operator;
    }

    // work out the answer for this sum
    public long calculate() {
        if (operator == '+') {
            long sum = 0;
            for (long val : values)
                sum += val;
            return sum;
        } else {
            long product = 1;
            for (long val : values)
                product *= val;
            return product;
        }
    }
}