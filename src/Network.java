import java.util.ArrayList;
import java.util.List;

public class Network {

    private static final int TYPOGRAPHY_UPDATE = 30;
    private static final int ROUTER_UPDATE     = 50;
    private static final int SEND_MAIL         = 10;

    private static final int PACKETS_PER_MAIL  = 1;

    private final Topography t;
    private final Router     r;
    private final Mailer     m;

    private final BroadcastMonitor broadcastMonitor = new BroadcastMonitor();

    private long i = 0;

    public Network(Topography t, Router r, Mailer m) {
        this.t = t;
        this.r = r;
        this.m = m;

        r.setTypography(t);
    }

    public void registerMonitor(Monitor monitor) { broadcastMonitor.registerMonitor(monitor); }

    public void sendPacket(Node src, Node dst, Object payload) {
        src.send(dst, payload, broadcastMonitor);
    }

    private void movePackets(int moves) {
        while (moves-- > 0) {
            Node node = t.getRandomNode();
            Packet packet = node.nextToSend();
            if (packet != null) {
                Link link = r.getNextStep(node, packet);
                if (link == null) {
                    packet.drop(FailureCondition.ROUTING_FAILURE);
                } else {
                    link.transmit(packet);
                }
            }
        }
    }

    public void run(int rounds) {
        while (rounds-- > 0) {
            if (i % TYPOGRAPHY_UPDATE == 0) t.updateTypography();
            if (i % ROUTER_UPDATE     == 0) r.updateRouter();
            if (i % SEND_MAIL         == 0) m.mail(this, PACKETS_PER_MAIL);

            movePackets(t.getNodes().size());

            i++;
        }
    }

    public String toString() {
        return "Network:{typography=" + t +", router=" + r + "}";
    }
}
