import java.io.*;

public class DayTwo2 {
    public static void main(String[] args) {

        long counter = 0;

        try {
            BufferedReader bf = new BufferedReader(new FileReader("input2.txt"));
            String input = bf.readLine();

            // break into ranges
            String[] ranges = input.split(",");
            for (String range : ranges) {
                System.out.println("Range:" + range);

                // get start and end of range
                String[] extremes = range.split("-");
                long start = Long.parseLong(extremes[0]);
                long end = Long.parseLong(extremes[1]);

                // check each number in range
                for (long i = start; i <= end; i++) {

                    String number = Long.toString(i);
                    int length = number.length();

                    // find divisors of length
                    for (int j = 1; j <= length / 2; j++) {
                        if (length % j == 0) {

                            // for current divisor, get first chunk to compare against
                            String fragment = number.substring(0, j);

                            // compare each subsequent chunk, fail if we find one that doesn't match
                            boolean match = true;
                            for (int k = j; k <= length - j; k += j) {
                                String nextFragment = number.substring(k, k + j);
                                if (!fragment.equals(nextFragment)) {
                                    match = false;
                                    break;
                                }
                            }

                            // if it was a match, count it and stop checking further divisors
                            if (match) {
                                counter += i;
                                System.out.println("Match: " + number);
                                break;
                            }

                        }
                    }
                }

            }

            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(counter);

    }
}