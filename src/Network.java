import java.util.Random;

public class Network {

    private static final int PACKETS_PER_ROUND = 10;

    private final Topography t;
    private final Router     r;

    public Network(Topography t, Router r) {
        this.t = t;
        this.r = r;
        r.setTypography(t);
    }

    private void sendNextPackets(int packets) {
        while (packets-- > 0) {
            Node node     = t.getRandomNode();
            Packet packet = node.nextToSend();
            if (packet != null) {
                Link link = r.getNextStep(node, packet);

                if (link == null) packet.drop(FailureCondition.ROUTING_FAILURE);
                else              link.transmit(packet);
            }
        }

    }

    public void run(int rounds) {
        while (rounds-- > 0) {
            t.updateTypography();
            r.updateRouter();
            sendNextPackets(PACKETS_PER_ROUND);
        }
    }

    public String toString() {
        return "Network:{typography=" + t +", router=" + r + "}";
    }
}
