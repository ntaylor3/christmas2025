import java.io.*;

public class DayOne2 {
    private static int dial = 50;
    private static int counter = 0;

    public static void main(String[] args) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader("input.txt"));
            System.out.println("Start:" + dial);
            while (bf.ready()) {
                String line = bf.readLine();

                // separate the direction and distance
                int distance = Integer.parseInt(line.substring(1));

                // set increment as positive or negative as appropriate
                int increment = 1;
                if (line.charAt(0) == 'L') {
                    increment = -1;
                }

                // step through each tick of the dial
                for (int i = 0; i < distance; i++) {
                    dial += increment;

                    // deal with overflow/underflow
                    if (dial == 100)
                        dial = 0;
                    else if (dial == -1)
                        dial = 99;

                    // check for dial at zero
                    if (dial == 0) {
                        counter++;
                    }
                }

                System.out.println(distance);
                System.out.println(dial);
                System.out.println(counter);
            }

            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}