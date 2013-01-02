package Typographies;

import Mailers.*;
import Monitors.*;
import Network.*;

public class LoadBalancing extends GenericStaticTypography implements Mailer, Monitor {

    private static final int    LINK_DISTANCE  = 1;
    private static final double LINK_UPTIME    = 1.0;
    private static final int    DEPTH          = 3;

    private Network n = null;
    private int p = 0;
    private Node gateway;

    private int serverAddr = 0;
    private int addr = 1;

    public LoadBalancing() {
        gateway = new Node(addr++);
        addNode(gateway);

        newLevel(gateway, DEPTH);
    }

    public void registerNetwork(Network network) { this.n = network; }

    public void newLevel(Node root, int depth) {
        for (int i = 0; i < 2; i++) {
            Node node;
            if (depth == 0) {
                node = new Node(serverAddr);
            } else {
                node = new Node(addr++);
            }
            addNode(node);
            addLink(root, new Link(node, LINK_DISTANCE, LINK_UPTIME));

            if (depth > 0) newLevel(node, depth-1);
        }
    }

    public void mail(Network network, int packets) {
        if (n == null) new RuntimeException("Network not set");
        for (int i = 0; i < packets; i++) {
            network.sendPacket(gateway, serverAddr, p++);
        }
    }

    public void dropped(Packet packet, FailureCondition fc) { /* Ignore Dropped Packets */ }

    public void arrived(Packet packet) {
        if (n == null) new RuntimeException("Network not set");
        if (packet.getDstAddr() == serverAddr) {
            Node server = packet.getNodeRoute().get(packet.getNodeRoute().size()-1);
            n.sendPacket(server, gateway.getAddr(), "Ack -" + packet.getPayload());
        }
    }

}
