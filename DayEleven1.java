import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DayEleven1 {
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

        // work out paths from you to out
        Server you = servers.get("you");
        Server out = servers.get("out");
        System.out.println(you.name + "-" + out.name + ": " + you.getPathsTo(out));
    }
}

class Server {
    String name;
    HashSet<Server> outputs = new HashSet<Server>();

    // server with three letter code
    public Server(String name) {
        this.name = name;
    }

    // add output connection to another server
    public void addOutput(Server server) {
        outputs.add(server);
    }

    // get number of paths from here to the target server
    public long getPathsTo(Server target) {
        if (target == this) {
            // if this is the target, path is 1
            return 1;
        } else {
            // otherwise, sum paths from all outputs to the target
            long total = 0;
            for (Server s : outputs) {
                total += s.getPathsTo(target);
            }
            return total;
        }
    }
}
