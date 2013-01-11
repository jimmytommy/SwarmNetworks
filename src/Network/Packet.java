package Network;

import Monitors.FailureCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction of IP packets being sent on a network.
 */
public class Packet {

    // Starting Packet Time to Live
    private static final int INIT_TTL = 20;

    // Callback for events that happen to packet
    private final Network network;

    // Contents of the IP Packet Header
    private int srcAddr;
    private int dstAddr;
    private int ttl;

    // Can be used to transfer information
    private Object payload;

    // Route packet took through the network
    private List<Node> nodeRoute = new ArrayList<Node>();
    private List<Link> linkRoute = new ArrayList<Link>();

    // Time Packet Sent and Received on Network
    private long start = -1;
    private long end   = -1;

    public Packet(int srcAddr, int dstAddr, Object payload, Network network) {
        this.srcAddr  = srcAddr;
        this.dstAddr  = dstAddr;
        this.ttl      = INIT_TTL;
        this.payload  = payload;
        this.network  = network;
        this.start    = network.getClock();
    }

    public int    getSrcAddr()  { return srcAddr;    }
    public int    getDstAddr()  { return dstAddr;    }
    public int    getTtl()      { return ttl;        }
    public Object getPayload()  { return payload;    }

    /**
     * Decrements the TTL by 1.
     * @return The updated TTL.
     */
    public int decrTTL() { return --ttl; }

    public long getStart() { return start; }
    public long getEnd()   { return end;   }

    public void drop(FailureCondition fc) {
        end = network.getClock();
        network.getBroadcastMonitor().dropped(this, fc);
    }
    public void arrive() {
        end = network.getClock();
        network.getBroadcastMonitor().arrived(this);
    }

    public void addToNodeRoute(Node node) { this.nodeRoute.add(node);  }
    public List<Node> getNodeRoute()      { return this.nodeRoute;     }

    public void addToLinkRoute(Link link) { this.linkRoute.add(link);  }
    public List<Link> getLinkRoute()      { return this.linkRoute;     }

    public String toString() {
        String s = "Packet:{";

        s += "srcAddr:"  + this.getSrcAddr() + ", ";
        s += "dstAddr:"  + this.getDstAddr() + ", ";
        s += "ttl:"      + this.getTtl()     + ", ";
        s += "payload:{" + this.getPayload() + "}, ";

        s += "nodeRoute:[";
        for (Node node : this.getNodeRoute()) {
            s += "Node:{addr=" + node.getAddr() + "}, ";
        }
        s += "]";

        return s + "}";
    }
}
