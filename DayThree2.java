import java.io.*;

public class DayThree2 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input3.txt"));
        String line = reader.readLine();
        long total = 0;
        while (line != null) {
            char[] numbers = new char[12];
            int currentPosition = -1;

            for (int i = 0; i < 12; i++) {
                // calculuate space needed to leave for remaining numbers
                int spaceNeeded = 12 - i - 1;

                for (int j = currentPosition + 1; j < line.length() - spaceNeeded; j++) {
                    // check for bigger numbers
                    char c = line.charAt(j);
                    if (c > numbers[i]) {
                        numbers[i] = c;
                        currentPosition = j;
                    }
                }
            }

            String number = "";
            for (int i = 0; i < 12; i++) {
                number = number + numbers[i];
            }

            System.out.println(number);
            total += Long.parseLong(number + "");

            line = reader.readLine();
        }

        System.out.println("Total: " + total);

        reader.close();

    }
}