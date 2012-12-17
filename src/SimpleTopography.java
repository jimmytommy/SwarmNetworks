import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleTopography implements Topography {

    private static final double LINK_UPTIME = 0.8;
    private static final Random r = new Random();

    private List<Node>       nodes;
    private List<List<Link>> links;

    public SimpleTopography(int nodes, int links) {
        this.nodes = new ArrayList<Node>(nodes);
        for (int i = 0; i < nodes; i++) this.nodes.add(new Node(i));

        this.links = new ArrayList<List<Link>>(nodes);
        for (int i = 0; i < nodes; i++) this.links.add(new ArrayList<Link>());

        while (links-- > 0) {
            Node src  = getRandomNode();
            Node dest = getRandomNode();
            this.links.get(src.getAddr()).add(new Link(dest, LINK_UPTIME));
        }
    }

    public List<Node> getNodes()          { return nodes;                              }
    public List<Link> getLinks(Node node) { return links.get(node.getAddr());          }
    public Node getRandomNode()           { return nodes.get(r.nextInt(nodes.size())); }
    public void updateTypography()        { /* Static topography so no migration */    }

    public String toString() {
        String s = "SimpleTypography:{nodes:[";

        for (Node node : nodes) {
            s += node + ":[";
            for (Link link : links.get(node.getAddr())) {
                s += link + ", ";
            }
            s += "], ";
        }

        return s + "]}";
    }
}
