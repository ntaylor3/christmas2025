import java.io.*;

public class DayOne {
    private static int dial = 50;
    private static int counter = 0;

    public static void main(String[] args) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader("input.txt"));
            while (bf.ready()) {
                String line = bf.readLine();

                // separate the direction and distance
                int distance = Integer.parseInt(line.substring(1));

                // increment or decrement dial as appropriate
                if (line.charAt(0) == 'R') {
                    dial += distance;
                } else if (line.charAt(0) == 'L') {
                    dial -= distance;
                }

                // check for over/underflow
                while (dial > 99)
                    dial -= 100;
                while (dial < 0)
                    dial += 100;

                // check for dial at zero
                if (dial == 0)
                    counter++;

                System.out.println(line);
                System.out.println(counter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}