import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*
    A slightly hacky approach today. Used DayElevenUtility to output the graph
    in graphviz format, then used graphviz to generate an image of the graph.
    Tool: https://dreampuf.github.io/GraphvizOnline
    Output: DayElevenViz.svg

    From there it is clear there are a small number of key nodes that
    can be used to break down the graph traversal into manageable chunks.
    At each stage, the key nodes keep track of how many paths lead to them.
*/

public class DayEleven2 {
    public static void main(String[] args) throws IOException {

        HashMap<String, Server> servers = new HashMap<String, Server>();

        // create each server
        List<String> lines = Files.readAllLines(new File("input11.txt").toPath());
        for (String line : lines) {

            String[] parts = line.split(" ");
            Server origin = null;
            // run through all servers and connections on this line
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].substring(0, 3);

                // try to pull the server if it already exists, or creatre it
                Server temp = servers.get(parts[i]);
                if (temp == null) {
                    temp = new Server(parts[i]);
                    servers.put(parts[i], temp);
                }

                if (i == 0) {
                    // special case where this is the origin server for the line
                    origin = temp;
                } else {
                    // in other cases, add a connection from origin to the new server
                    origin.addOutput(temp);
                }
            }
        }

        // these are the key nodes identified from the graph visualisation
        String[][] keyNodes = {
                { "svr" },
                { "flm", "cas", "eqq" },
                { "fft" },
                { "fhz", "qzp", "gxv" },
                { "xhu", "mdm", "cjm", "jmk", "sdo" },
                { "hll", "ovp", "exc", "wze", "ktf" },
                { "dac" },
                { "ngs", "tml", "you", "daz" },
                { "out" }
        };

        for (int stage = 0; stage < keyNodes.length - 1; stage++) {
            String[] sources = keyNodes[stage];
            String[] destinations = keyNodes[stage + 1];

            for (int i = 0; i < sources.length; i++) {
                for (int j = 0; j < destinations.length; j++) {
                    Server source = servers.get(sources[i]);
                    Server dest = servers.get(destinations[j]);

                    long paths = source.getPathsTo(dest, 10);
                    if (stage == 0) {
                        dest.incomingPaths += paths; // first stage
                    } else {
                        dest.incomingPaths += (paths * source.incomingPaths);
                    }
                    System.out.println(source.name + "-" + dest.name + ": " + paths + " " + dest.incomingPaths);
                }
            }
        }

        /*
         * String[] sources = { "svr" };
         * String[] destinations = { "flm", "cas", "eqq" };
         * 
         * for (int i = 0; i < sources.length; i++) {
         * for (int j = 0; j < destinations.length; j++) {
         * Server source = servers.get(sources[i]);
         * Server dest = servers.get(destinations[j]);
         * 
         * long paths = source.getPathsTo(dest, 10);
         * dest.incomingPaths += paths;
         * System.out.println(source.name + "-" + dest.name + ": " + paths + " " +
         * dest.incomingPaths);
         * }
         * }
         * 
         * sources = destinations;
         * destinations = new String[] { "fft" };
         * 
         * for (int i = 0; i < sources.length; i++) {
         * for (int j = 0; j < destinations.length; j++) {
         * Server source = servers.get(sources[i]);
         * Server dest = servers.get(destinations[j]);
         * 
         * long paths = source.getPathsTo(dest, 10);
         * dest.incomingPaths += (paths * source.incomingPaths);
         * System.out.println(source.name + "-" + dest.name + ": " + paths + " " +
         * dest.incomingPaths);
         * }
         * }
         * 
         * sources = destinations;
         * destinations = new String[] { "fhz", "qzp", "gxv" };
         * 
         * for (int i = 0; i < sources.length; i++) {
         * for (int j = 0; j < destinations.length; j++) {
         * Server source = servers.get(sources[i]);
         * Server dest = servers.get(destinations[j]);
         * 
         * long paths = source.getPathsTo(dest, 10);
         * dest.incomingPaths += (paths * source.incomingPaths);
         * System.out.println(source.name + "-" + dest.name + ": " + paths + " " +
         * dest.incomingPaths);
         * }
         * }
         * 
         * sources = destinations;
         * destinations = new String[] { "xhu", "mdm", "cjm", "jmk", "sdo" };
         * 
         * for (int i = 0; i < sources.length; i++) {
         * for (int j = 0; j < destinations.length; j++) {
         * Server source = servers.get(sources[i]);
         * Server dest = servers.get(destinations[j]);
         * 
         * long paths = source.getPathsTo(dest, 10);
         * dest.incomingPaths += (paths * source.incomingPaths);
         * System.out.println(source.name + "-" + dest.name + ": " + paths + " " +
         * dest.incomingPaths);
         * }
         * }
         * 
         * sources = destinations;
         * destinations = new String[] { "hll", "ovp", "exc", "wze", "ktf" };
         * 
         * for (int i = 0; i < sources.length; i++) {
         * for (int j = 0; j < destinations.length; j++) {
         * Server source = servers.get(sources[i]);
         * Server dest = servers.get(destinations[j]);
         * 
         * long paths = source.getPathsTo(dest, 10);
         * dest.incomingPaths += (paths * source.incomingPaths);
         * System.out.println(source.name + "-" + dest.name + ": " + paths + " " +
         * dest.incomingPaths);
         * }
         * }
         * 
         * sources = destinations;
         * destinations = new String[] { "dac" };
         * 
         * for (int i = 0; i < sources.length; i++) {
         * for (int j = 0; j < destinations.length; j++) {
         * Server source = servers.get(sources[i]);
         * Server dest = servers.get(destinations[j]);
         * 
         * long paths = source.getPathsTo(dest, 10);
         * dest.incomingPaths += (paths * source.incomingPaths);
         * System.out.println(source.name + "-" + dest.name + ": " + paths + " " +
         * dest.incomingPaths);
         * }
         * }
         * 
         * sources = destinations;
         * destinations = new String[] { "ngs", "tml", "you", "daz" };
         * 
         * for (int i = 0; i < sources.length; i++) {
         * for (int j = 0; j < destinations.length; j++) {
         * Server source = servers.get(sources[i]);
         * Server dest = servers.get(destinations[j]);
         * 
         * long paths = source.getPathsTo(dest, 10);
         * dest.incomingPaths += (paths * source.incomingPaths);
         * System.out.println(source.name + "-" + dest.name + ": " + paths + " " +
         * dest.incomingPaths);
         * }
         * }
         * 
         * sources = destinations;
         * destinations = new String[] { "out" };
         * 
         * for (int i = 0; i < sources.length; i++) {
         * for (int j = 0; j < destinations.length; j++) {
         * Server source = servers.get(sources[i]);
         * Server dest = servers.get(destinations[j]);
         * 
         * long paths = source.getPathsTo(dest, 10);
         * dest.incomingPaths += (paths * source.incomingPaths);
         * System.out.println(source.name + "-" + dest.name + ": " + paths + " " +
         * dest.incomingPaths);
         * }
         * }
         */

    }
}

class Server {
    String name;
    HashSet<Server> outputs = new HashSet<Server>();

    // incoming paths is used only by key nodes to track number of paths leading to
    // them
    long incomingPaths = 0;

    // server with three letter code
    public Server(String name) {
        this.name = name;
    }

    // add output connection to another server
    public void addOutput(Server server) {
        outputs.add(server);
    }

    // get number of paths from here to the target server
    // the limit serves to prevent us going too deep now that the
    // graph is segmented
    public long getPathsTo(Server target, int limit) {
        if (target == this) {
            // if this is the target, path is 1
            return 1;
        } else if (limit == 0) {
            // give up once limit reached
            return 0;
        } else {
            // otherwise, sum paths from all outputs to the target
            long total = 0;
            for (Server s : outputs) {
                total += s.getPathsTo(target, limit - 1);
            }
            return total;
        }
    }
}
