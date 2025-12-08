import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class DayEight {
    public static void main(String[] args) throws IOException {
        // create list of all nodes
        ArrayList<Node> nodes = new ArrayList<Node>();
        List<String> lines = Files.readAllLines(new File("input8.txt").toPath());
        for (String line : lines) {
            String[] parts = line.split(",");
            Node node = new Node(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            nodes.add(node);
        }

        // create all edges between nodes
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                edges.add(new Edge(nodes.get(i), nodes.get(j)));
            }
        }
        edges.sort(null);

        // create initial circuits with one node per circuit
        ArrayList<Circuit> circuits = new ArrayList<Circuit>();
        for (Node node : nodes) {
            circuits.add(new Circuit(node));
        }

        // process edges from shortest to longest building circuits until solved
        int i = 0;
        for (Edge edge : edges) {
            HashSet<Circuit> toMerge = new HashSet<Circuit>();

            // find circuits containing either node of the edge
            for (Circuit circuit : circuits) {
                if (circuit.contains(edge.a) || circuit.contains(edge.b)) {
                    toMerge.add(circuit);
                }
            }

            // merge circuits containing either node of the edge, removing the original
            Circuit merged = new Circuit(edge);
            for (Circuit circuit : toMerge) {
                merged.mergeCircuit(circuit);
                circuits.remove(circuit);
            }
            circuits.add(merged);

            // if we've done 1000, answer part 1 by multiplying three biggest circuits
            if (i++ == 1000) {
                circuits.sort(null);
                Collections.reverse(circuits);
                System.out.println(
                        "Part 1: " + circuits.get(0).getSize() * circuits.get(1).getSize() * circuits.get(2).getSize());
            }

            // if we've merged down to one circuit, stop and multiply x coords of final edge
            if (circuits.size() == 1) {
                System.out.println("Part 2: " + edge.a.x * edge.b.x);
                break;
            }
        }
    }
}

// an edge between two nodes with a distance
class Edge implements Comparable<Edge> {
    public Node a;
    public Node b;
    public float distance;

    public Edge(Node a, Node b) {
        this.a = a;
        this.b = b;
        this.distance = (float) Math.sqrt(
                Math.pow(a.x - b.x, 2) +
                        Math.pow(a.y - b.y, 2) +
                        Math.pow(a.z - b.z, 2));
    }

    public int compareTo(Edge other) {
        return Float.compare(this.distance, other.distance);
    }
}

// a point in 3D space
class Node {
    public int x;
    public int y;
    public int z;

    public Node(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

// a collection of nodes and edges representing a single circuit
class Circuit implements Comparable<Circuit> {
    public HashSet<Node> nodes;
    public HashSet<Edge> edges;

    // create from a single node
    public Circuit(Node node) {
        nodes = new HashSet<Node>();
        edges = new HashSet<Edge>();
        nodes.add(node);
    }

    // create from a single edge, adding both nodes to the circuit
    public Circuit(Edge edge) {
        this(edge.a);
        this.addEdge(edge);
    }

    // check if circuit contains a node
    public boolean contains(Node node) {
        return nodes.contains(node);
    }

    // add an edge and its nodes to the circuit
    public void addEdge(Edge edge) {
        edges.add(edge);
        nodes.add(edge.b);
        nodes.add(edge.a);
    }

    // merge another circuit into this one
    public void mergeCircuit(Circuit other) {
        nodes.addAll(other.nodes);
        edges.addAll(other.edges);
    }

    // number of nodes in the circuit
    public int getSize() {
        return nodes.size();
    }

    public int compareTo(Circuit other) {
        return Integer.compare(this.getSize(), other.getSize());
    }
}