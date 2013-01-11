package Typographies;

import Mailers.Mailer;
import Network.Network;
import Network.Node;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancingTypography extends GenericStaticTypography implements Mailer {

    private Node root;
    private int round = 0;

    public LoadBalancingTypography() {
        List<Node> servers = new ArrayList<Node>();
        for (int i = 0; i < 8; i++) {
            Node n = new Node(0);
            addNode(n);
            servers.add(n);
        }
        root = new Node(1);
        Node two = new Node(2);
        addLink(root, two, 1);

        Node three = new Node(3);
        addLink(root, three, 1);

        Node four = new Node(4);
        addLink(root, four, 1);

        for (int i = 0; i < 4; i++) {
            addLink(two, servers.get(i), 1);
        }

        for (int i = 2; i < 6; i++) {
            addLink(three, servers.get(i), 1);
        }

        for (int i = 4; i < 8; i++) {
            addLink(four, servers.get(i), 1);
        }

    }

    public void mail(Network network, int packets) {
        network.sendPacket(root,  0, "Round " + round);

        round++;
    }
}
