import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*
    An aborted attempt at part 2 with various experients to try and speed up pathfinding
*/

public class DayEleven2a {
    public static void main(String[] args) throws IOException, InterruptedException {

        HashMap<String, Server1> servers = new HashMap<String, Server1>();

        // create set of empty servers
        List<String> lines = Files.readAllLines(new File("input11.txt").toPath());
        for (String line : lines) {

            String[] parts = line.split(" ");
            parts[0] = parts[0].substring(0, 3);
            servers.put(parts[0], new Server1(parts[0]));
        }
        servers.put("out", new Server1("out"));

        // add connections
        for (String line : lines) {
            String[] parts = line.split(" ");
            Server1 origin = null;
            // System.out.println("Processing line: " + line);
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].substring(0, 3);
                Server1 temp = servers.get(parts[i]);

                if (i == 0) {
                    origin = temp;
                } else {
                    origin.addOutput(temp);
                }
            }
        }

        System.out.println("Graph built.");

        // calculate which servers can reach DAC
        servers.get("dac").setCanReachDac();
        servers.get("fft").setCanReachFft();
        servers.get("dac").setCanBeReachedFromDac();
        servers.get("fft").setCanBeReachedFromFft();

        Server1 out = servers.get("out");
        out.formRouteToOut(out);

        for (Server1 s : servers.values())
            System.out.println("Server " + s.name + " has " + s.ouputs.size() + " outputs");

        // Server1 svr = servers.get("svr");
        // System.out.println("Total paths from SVR to OUT: " + svr.routesToOut);

        /*
         * Server1 svr = servers.get("svr");
         * Server1 out = servers.get("out");
         * System.out.println(svr.getPathsTo(out, new HashSet<String>()));
         */
    }
}

class Server1 {
    String name;
    boolean canReachOut = false;
    boolean canReachDac = false;
    boolean canReachFft = false;
    boolean canBeReachedFromDac = false;
    boolean canBeReachedFromFft = false;
    HashSet<Server1> ouputs = new HashSet<Server1>();
    HashSet<Server1> inputs = new HashSet<Server1>();
    HashSet<Server1> visited = new HashSet<Server1>();
    long routesToOut = 0;
    HashSet<Server1> downstream = new HashSet<Server1>();
    int downstreamCount = 0;

    public Server1(String name) {
        this.name = name;
    }

    public void addInput(Server1 server) {
        inputs.add(server);
        downstream.addAll(server.downstream);
        downstream.add(server);
        downstreamCount = downstream.size();

    }

    public void addOutput(Server1 server) {
        ouputs.add(server);
        server.addInput(this);
    }

    public void formRouteToOut(Server1 next) throws IOException, InterruptedException {

        if (!visited.contains(next)) {
            for (Server1 s : inputs) {
                if (!visited.contains(s))

                    visited.add(next);

                System.out.println("Adding route to OUT from " + name + " for server " + s.name);
                routesToOut++;
                System.out.println("Current routes to OUT via server " + name + ": " + routesToOut);
                s.formRouteToOut(this);
            }
        }
    }

    public void setCanReachDac() {
        if (!canReachDac) {
            System.out.println("Server " + name + " can reach DAC");
            canReachDac = true;

            for (Server1 s : inputs) {
                if (!s.canReachDac) {
                    s.setCanReachDac();
                }
            }

        }
    }

    public void setCanReachFft() {
        if (!canReachFft) {
            canReachFft = true;
            System.out.println("Server " + name + " can reach FFT");

            for (Server1 s : inputs) {
                if (!s.canReachFft) {
                    s.setCanReachFft();
                }
            }

        }
    }

    public void setCanBeReachedFromDac() {
        if (!canBeReachedFromDac) {
            canBeReachedFromDac = true;

            for (Server1 s : ouputs) {
                if (!s.canBeReachedFromDac) {
                    s.setCanBeReachedFromDac();
                }
            }

        }
    }

    public void setCanBeReachedFromFft() {
        if (!canBeReachedFromFft) {
            canBeReachedFromFft = true;

            for (Server1 s : ouputs) {
                if (!s.canBeReachedFromFft) {
                    s.setCanBeReachedFromFft();
                }
            }

        }
    }

    public long getPathsTo(Server1 target, HashSet<String> visited) {
        HashSet<String> newVisited = new HashSet<String>(visited);
        newVisited.add(this.name);

        // System.out.println("Visiting server " + name + ", visited: " + newVisited);

        if (target == this) {
            // System.out.println("Path found!");
            return 1;
        } else {
            long total = 0;
            for (Server1 s : ouputs) {
                if (!newVisited.contains(s.name)) {
                    // work out if this is a viable path...
                    boolean viable = false;
                    // System.out.println(s.name + " canReachDac: " + s.canReachDac + ",
                    // canReachFft: " + s.canReachFft);
                    if (s.canReachDac && s.canReachFft) {
                        viable = true;
                        // System.out.println("Server " + s.name + " can reach both DAC and FFT");
                    } else if (s.canReachDac && s.canBeReachedFromFft) {
                        viable = true;
                        // System.out.println("Visited FFT and " + s.name + " can reach DAC");
                    } else if (s.canReachFft && s.canBeReachedFromDac) {
                        viable = true;
                        // System.out.println("Visited DAC and " + s.name + " can reach FFT");
                    } else if (visited.contains("fft") && newVisited.contains("dac")) {
                        viable = true;
                        // System.out.println("Visited both DAC and FFT already");
                    }

                    if (viable) {
                        long count = s.getPathsTo(target, newVisited);
                        total += count;
                        if (count % 10 == 0) {
                            System.out
                                    .println("Found " + total + " paths from server " + s.name + " to " + target.name);
                        }
                        // System.out.println(total + " paths found so far from server " + name);
                    }
                }
            }
            return total;
        }
    }

    public boolean equals(Object o) {
        if (o instanceof Server1) {
            Server1 other = (Server1) o;
            return this.name.equals(other.name);
        }
        return false;
    }
}
