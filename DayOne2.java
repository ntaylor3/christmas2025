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
                int distance = Integer.parseInt(line.substring(1));
                int increment = 1;

                if (line.charAt(0) == 'L') {
                    increment = -1;
                }

                for(int i=0; i<distance; i++)
                {
                    dial += increment;
                    if(dial==100)
                        dial=0;
                    else if(dial==-1)
                        dial=99;

                    if(dial==0)
                    {
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