import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStaticTopography implements Topography {

    private static final double LINK_UPTIME = 1.0;
    private static final int    LINK_DIST   = 1;

    private static final Random r = new Random();

    private List<Node>       nodes;
    private List<List<Link>> links;

    public RandomStaticTopography(int nodes, int links) {
        this.nodes = new ArrayList<Node>(nodes);
        for (int i = 0; i < nodes; i++) this.nodes.add(new Node(i));

        this.links = new ArrayList<List<Link>>(nodes);
        for (int i = 0; i < nodes; i++) this.links.add(new ArrayList<Link>());

        for (int i = 0; i < links; i++) {
            Node src  = getRandomNode();
            Node dest = getRandomNode();
            this.links.get(src.getAddr()).add(new Link(dest, LINK_DIST, LINK_UPTIME));
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
