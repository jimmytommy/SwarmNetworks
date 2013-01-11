package Network;

import Mailers.Mailer;
import Monitors.BroadcastMonitor;
import Monitors.FailureCondition;
import Monitors.Monitor;
import Routers.Router;
import Typographies.Topography;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Network {

    private static final int TYPOGRAPHY_UPDATE = 30;
    private static final int ROUTER_UPDATE     = 50;
    private static final int SEND_MAIL         = 10;

    private static final int PACKETS_PER_MAIL  = 1;

    private final Topography t;
    private final Router     r;
    private final Mailer     m;

    private final BroadcastMonitor broadcastMonitor = new BroadcastMonitor();

    private long clock = 0;

    public Network(Topography t, Router r, Mailer m) {
        this.t = t;
        this.r = r;
        this.m = m;

        r.setTypography(t);
    }

    public void registerMonitor(Monitor monitor) { broadcastMonitor.registerMonitor(monitor); }

    public void sendPacket(Node src, int dstAddr, Object payload) {
        src.send(dstAddr, payload, broadcastMonitor);
    }

    public void run(int ticks) {
        while (ticks-- > 0) {
            if (clock % TYPOGRAPHY_UPDATE == 0) t.updateTypography();
            if (clock % ROUTER_UPDATE     == 0) r.updateRouter();
            if (clock % SEND_MAIL         == 0) m.mail(this, PACKETS_PER_MAIL);

            List<Node> nodes = new ArrayList<Node>(t.getNodes());
            Collections.shuffle(nodes);
            for (Node src : nodes) {
                Packet packet = src.nextToSend();
                if (packet != null) {
                    Node dst = r.getNextStep(src, packet);
                    if (dst == null) {
                        packet.drop(FailureCondition.ROUTING_FAILURE);
                        break;
                    }
                    if (!t.isConnected(src, dst)) {
                        packet.drop(FailureCondition.OBSOLETE_ROUTE);
                        break;
                    }
                    if (!t.canTransmit(src, dst)) {
                        packet.drop(FailureCondition.LINK_FAILURE);
                        break;
                    }
                    dst.recv(packet);
                    packet.addToLinkRoute(t.getLink(src, dst));
                }
            }

            clock++;
        }
    }

    public String toString() {
        return "Network:{typography=" + t +", router=" + r + ", mailer=" + m + "}";
    }
}