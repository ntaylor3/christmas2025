import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

public class DayNine {
    public static void main(String[] args) throws IOException {

        // create list of all points and define a polygon based on them
        ArrayList<Point> points = new ArrayList<Point>();
        Polygon polygon = new Polygon();
        List<String> lines = Files.readAllLines(new File("input9.txt").toPath());
        for (String line : lines) {
            String[] parts = line.split(",");
            Point point = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            points.add(point);
            polygon.addPoint(point.x, point.y);
        }

        long biggestArea1 = 0;
        long biggestArea2 = 0;

        // run through all possible rectangles defined by two nodes
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {

                // work out area of each rectangle
                int width = Math.abs(points.get(i).x - points.get(j).x) + 1;
                int height = Math.abs(points.get(i).y - points.get(j).y) + 1;
                long area = (long) width * (long) height;

                // Part 1: find the biggest one for
                if (area > biggestArea1) {
                    biggestArea1 = area;
                }

                // Part 2: define a rectangle that's actually very slightly smaller than the
                // target due to how contains() works with edges
                int x1 = Math.min(points.get(i).x, points.get(j).x);
                int y1 = Math.min(points.get(i).y, points.get(j).y);
                // need to remove the 1 that was added to the width and the height!
                Rectangle2D.Double rect = new Rectangle2D.Double(x1 + 0.1, y1 + 0.1, width - 1.2, height - 1.2);

                // check if rectangle is in the polygon
                if (polygon.contains(rect)) {
                    if (area > biggestArea2) {
                        biggestArea2 = area;
                    }
                }
            }
        }

        System.out.println("Part 1: " + biggestArea1);
        System.out.println("Part 2: " + biggestArea2);
    }
}