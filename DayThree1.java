import java.io.*;

public class DayThree1 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input3.txt"));
        String line = reader.readLine();
        long total = 0;
        while (line != null) {
            char largest = 0;
            int largestIndex = 0;
            char follower = 0;

            // find largest digit that's not the last one
            for (int i = 0; i < line.length() - 1; i++) {
                char c = line.charAt(i);
                if (c > largest) {
                    largest = c;
                    largestIndex = i;
                }
            }

            // find next largest number ocuring after the first
            for (int i = largestIndex + 1; i < line.length(); i++) {
                if (line.charAt(i) > follower) {
                    follower = line.charAt(i);
                }
            }

            System.out.println(largest + "" + follower);
            total += Long.parseLong(largest + "" + follower);

            line = reader.readLine();
        }

        System.out.println("Total: " + total);

        reader.close();

    }
}