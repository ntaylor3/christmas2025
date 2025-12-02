import java.io.*;

public class DayTwo2 {
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
                    
                    for(int j=1; j<=length/2; j++ )
                    {
                        if(length % j == 0)
                        {
                            
                        //get first substring
                        String fragment = number.substring(0, j);
                        boolean match = true;

                        for(int k=j; k<=length - j; k+=j)
                        {
                            String nextFragment = number.substring(k, k + j);
                            if(!fragment.equals(nextFragment))
                            {
                                match = false;
                                break;
                            }
                        }

                        if(match)
                        {
                            counter += i;
                            System.out.println("Match: " + number);
                            break;
                        }
                        
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