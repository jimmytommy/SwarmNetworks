package Typographies;

import Network.Link;
import Network.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GenericStaticTypography implements Topography {

    private static final Random r = new Random();

    private List<Node> nodes = new ArrayList<Node>();
    private HashMap<Node, List<Link>> linkMap = new HashMap<Node, List<Link>>();

    public void addNode(Node node) {
        nodes.add(node);
        linkMap.put(node, new ArrayList<Link>());
    }

    public void addLink(Node node, Link link) {
        linkMap.get(node).add(link);
    }

    public List<Node> getNodes() { return nodes; }
    public List<Link> getLinks(Node node) { return linkMap.get(node); }
    public Node getRandomNode() { return nodes.get(r.nextInt(nodes.size())); }
    public void updateTypography() { /* Static Typography */ }
}
