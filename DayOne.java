import java.io.*;

public class DayOne {
    private static int dial = 50;
    private static int counter = 0;

    public static void main(String[] args) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader("input.txt"));
            while (bf.ready()) {
                String line = bf.readLine();
                int distance = Integer.parseInt(line.substring(1));

                if (line.charAt(0) == 'R') {
                    dial += distance;
                }
                else   if (line.charAt(0) == 'L') {
                    dial -= distance;
                }

                while(dial>99)
                    dial -=100;
                while(dial<0)
                    dial +=100;

                if(dial==0)
                    counter++;

                System.out.println(line);
                System.out.println(counter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}