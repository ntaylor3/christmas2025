import java.io.*;

public class DayTwo1 {
    public static void main(String[] args) {

        long counter = 0;

        try {
            BufferedReader bf = new BufferedReader(new FileReader("input2.txt"));
            String input = bf.readLine();
            
            String[] ranges = input.split(",");
            for(String range : ranges) {
                System.out.println("Range:" + range);    
                String[] extremes = range.split("-");
                long start = Long.parseLong(extremes[0]);
                long end = Long.parseLong(extremes[1]);

                for(long i=start; i<=end; i++) {

                    String number = Long.toString(i);
                    int length = number.length();
                    if(length%2 == 0) {
                        
                        String one = number.substring(0, length/2);
                        String two = number.substring(length/2, length);
                        if(one.equals(two))
                        {
                            counter += i;
                            System.out.println("Match: " + number);
                        }
                    }                    
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(counter);

    }
}