package Typographies;

import Mailers.Mailer;
import Network.Link;
import Network.Node;
import Network.Network;

import java.util.*;

public class LinkFailureExample implements Topography, Mailer {

    private static int dstAddr = 5;
    private int round = 0;

    private boolean up = true;
    private List<Node> nodes = new ArrayList<Node>();

    public LinkFailureExample() {
        for (int i = 0; i < dstAddr+1; i++) {
            nodes.add(new Node(i));
        }
    }

    public Set<Node> getNodes() {
        return new HashSet<Node>(nodes);
    }

    public Collection<Link> getLinks(Node node) {
        List<Link> links = new ArrayList<Link>();

        if (node.getAddr() == 0 && up) {
            links.add(new Link(nodes.get(0), nodes.get(dstAddr), 7));
        }
        if (node.getAddr() != dstAddr) {
            links.add(new Link(nodes.get(node.getAddr()), nodes.get(node.getAddr()+1), 7));
        }

        return links;
    }

    public boolean isConnected(Node src, Node dst) {
        if (src.getAddr() == 0 && dst.getAddr() == dstAddr) return up;
        if (src.getAddr() == dstAddr)                       return false;
        if (src.getAddr() == dst.getAddr()-1)               return true;
        return false;
    }

    public Link getLink(Node src, Node dst) {
        if (isConnected(src, dst)) return new Link(src, dst, 7);
        else return null;
    }

    public boolean canTransmit(Node src, Node dst) {
        return isConnected(src, dst);
    }

    public void updateTypography() {
        up = !up;
    }

    public void mail(Network network, int packets) {
        network.sendPacket(nodes.get(0), 5, "Round " + round);

        round++;
    }
}
