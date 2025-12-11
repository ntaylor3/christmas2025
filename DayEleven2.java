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

        HashMap<String, Server2> servers = new HashMap<String, Server2>();

        // create each server
        List<String> lines = Files.readAllLines(new File("input11.txt").toPath());
        for (String line : lines) {

            String[] parts = line.split(" ");
            Server2 origin = null;
            // run through all servers and connections on this line
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].substring(0, 3);

                // try to pull the server if it already exists, or creatre it
                Server2 temp = servers.get(parts[i]);
                if (temp == null) {
                    temp = new Server2(parts[i]);
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

        // run through each stage finding connections ot the next
        for (int stage = 0; stage < keyNodes.length - 1; stage++) {
            String[] sources = keyNodes[stage];
            String[] destinations = keyNodes[stage + 1];

            for (int i = 0; i < sources.length; i++) {
                for (int j = 0; j < destinations.length; j++) {
                    Server2 source = servers.get(sources[i]);
                    Server2 dest = servers.get(destinations[j]);

                    // in the first stage just add the new paths
                    // after that, we need to mutiply them with the previous set
                    long paths = source.getPathsTo(dest, 10);
                    if (stage == 0) {
                        dest.incomingPaths += paths;
                    } else {
                        dest.incomingPaths += (paths * source.incomingPaths);
                    }
                }
            }
        }

        System.out.println("Part 2: " + servers.get("out").incomingPaths);
    }
}

class Server2 {
    String name;
    HashSet<Server2> outputs = new HashSet<Server2>();

    // incoming paths is used only by key nodes to track number of paths leading to
    // them
    long incomingPaths = 0;

    // server with three letter code
    public Server2(String name) {
        this.name = name;
    }

    // add output connection to another server
    public void addOutput(Server2 server) {
        outputs.add(server);
    }

    // get number of paths from here to the target server
    // the limit serves to prevent us going too deep now that the
    // graph is segmented
    public long getPathsTo(Server2 target, int limit) {
        if (target == this) {
            // if this is the target, path is 1
            return 1;
        } else if (limit == 0) {
            // give up once limit reached
            return 0;
        } else {
            // otherwise, sum paths from all outputs to the target
            long total = 0;
            for (Server2 s : outputs) {
                total += s.getPathsTo(target, limit - 1);
            }
            return total;
        }
    }
}
