package Typographies;

import Network.Link;
import Network.Node;

import java.util.*;

public class GenericStaticTypography implements Topography {

    private static final Random r = new Random();

    private static final double LINK_UPTIME = 1.0;

    private List<Node> nodes = new ArrayList<Node>();
    private HashMap<Node, HashMap<Node, Link>> map = new HashMap<Node, HashMap<Node, Link>>();

    public void addNode(Node node) {
        nodes.add(node);
        map.put(node, new HashMap<Node, Link>());
    }

    public void addLink(Node src, Node dst, int weight) {
        map.get(src).put(dst, new Link(src, dst, weight));
    }

    public Set<Node> getNodes() {
        return map.keySet();
    }

    public Collection<Link> getLinks(Node node) {
        return map.get(node).values();
    }

    public boolean isConnected(Node src, Node dst) {
        return map.get(src).containsKey(dst);
    }

    public boolean canTransmit(Node src, Node dst) {
        return isConnected(src, dst) && r.nextDouble() < LINK_UPTIME;
    }

    public Node getRandomNode() {
        return nodes.get(r.nextInt(nodes.size()));
    }

    public void updateTypography() { /* Static Typography */ }
}
