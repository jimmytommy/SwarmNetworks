package Typographies;

import Network.Link;
import Network.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates a topography in the form a of a simple connected tree with root.
 */
public class TreeTopography extends GenericStaticTypography {

    private static final int LINK_DIST = 1;

    private Node root;

    public TreeTopography(int n) throws IllegalArgumentException {
        if (n < 1) throw new IllegalArgumentException("Tree Topographies must have at least one node");

        Random r = new Random();
        List<Node> nodes = new ArrayList<Node>(n);

        int i = 0;
        this.root = new Node(i);
        addNode(root);
        nodes.add(root);

        while (++i < n) {
            Node newNode = new Node(i);
            Node oldNode = nodes.get(r.nextInt(nodes.size()));

            addLink(oldNode, newNode, LINK_DIST);

            addNode(newNode);
            nodes.add(newNode);
        }
    }

    public Node getRoot() { return root; }

    public String print(Node root) {
        String s = "N" + root.getAddr();

        if (getLinks(root).size() != 0) {
            s += "{";

            for (Link l : getLinks(root)) {
                s += print(l.getDst()) + ", ";
            }

            s += "}";
        }

        return s;
    }

    public String toString() {
        String s = "TreeTopography:{";

        s += print(this.root);

        return s + "}";
    }
}
