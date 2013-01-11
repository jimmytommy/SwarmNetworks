package Typographies;

import Mailers.Mailer;
import Network.Network;
import Network.Node;

import java.util.ArrayList;
import java.util.List;

public class CongestionExampleTypography extends GenericStaticTypography implements Mailer {

    int round = 0;

    Node Matt, Alice, JT;

    public CongestionExampleTypography() {
        List<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < 5; i++) {
            Node n = new Node(i);
            addNode(n);
            nodes.add(n);
        }

        addLink(nodes.get(0), nodes.get(3), 1);
        addLink(nodes.get(1), nodes.get(3), 1);
        addLink(nodes.get(2), nodes.get(3), 1);
        addLink(nodes.get(0), nodes.get(4), 12);
        addLink(nodes.get(3), nodes.get(4), 10);
        addLink(nodes.get(2), nodes.get(4), 12);

        Matt  = nodes.get(0);
        Alice = nodes.get(1);
        JT    = nodes.get(2);
    }

    public void mail(Network network, int packets) {
        network.sendPacket(Matt,  4, "Round " + round);
        network.sendPacket(Alice, 4, "Round " + round);
        network.sendPacket(JT,    4, "Round " + round);

        round++;
    }
}
