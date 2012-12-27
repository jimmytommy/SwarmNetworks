import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates a topography in the form a of a simple connected tree with root.
 */
public class TreeTopography implements Topography {

    private static final double LINK_UPTIME = 0.8;

    private static final Random r = new Random();

    private Node             root;
    private List<Node>       nodes;
    private List<List<Link>> links;

    public TreeTopography(int nodes) throws IllegalArgumentException {
        if (nodes < 1) throw new IllegalArgumentException("Tree Topographies must have at least one node");

        Random r = new Random();

        this.nodes = new ArrayList<Node>(nodes);

        this.links = new ArrayList<List<Link>>(nodes);
        for (int i = 0; i < nodes; i++) this.links.add(new ArrayList<Link>());

        int i = 0;
        this.root = new Node(i);
        this.nodes.add(root);

        while (i++ < nodes) {
            Node newNode = new Node(i);
            Node oldNode = this.nodes.get(r.nextInt(this.nodes.size()));
            this.getLinks(oldNode).add(new Link(newNode, LINK_UPTIME));
        }
    }

    public Node getRoot()                 { return root;                               }
    public List<Node> getNodes()          { return nodes;                              }
    public List<Link> getLinks(Node node) { return links.get(node.getAddr());          }
    public Node getRandomNode()           { return nodes.get(r.nextInt(nodes.size())); }
    public void updateTypography()        { /* Static topography so no migration */    }
}
