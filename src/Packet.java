import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction of IP packets being sent on a network.
 */
public class Packet {

    // Starting Packet Time to Live
    private static final int INIT_TTL = 255;

    // Callback for events that happen to packet
    private static final Monitor monitor = new PrintMonitor();

    // Contents of the IP Packet Header
    private int srcAddr;
    private int destAddr;
    private int ttl;

    // Can be used to transfer information
    private Object payload;

    // Route packet took through the network
    private List<Node> nodeRoute = new ArrayList<Node>();
    private List<Link> linkRoute = new ArrayList<Link>();

    public Packet(int srcAddr, int destAddr, Object payload) {
        this.srcAddr  = srcAddr;
        this.destAddr = destAddr;
        this.ttl      = INIT_TTL;
        this.payload  = payload;
    }

    public int    getSrcAddr()  { return srcAddr;    }
    public int    getDestAddr() { return destAddr;   }
    public int    getTtl()      { return ttl;        }
    public Object getPayload()  { return payload;    }

    /**
     * Decrements the TTL by 1.
     * @return The updated TTL.
     */
    public int decrTTL()       { return --ttl;      }

    public void drop(FailureCondition fc) { monitor.dropped(this, fc); }
    public void arrive()                  { monitor.arrived(this);     }

    public void addToNodeRoute(Node node) { this.nodeRoute.add(node); }
    public Iterable<Node> getNodeRoute()  { return this.nodeRoute;    }

    public void addToLinkRoute(Link link) { this.linkRoute.add(link); }
    public Iterable<Link> getLinkRoute()  { return this.linkRoute;    }

    public String toString() {
        String s = "Packet:{";

        s += "srcAddr:"  + this.getSrcAddr()  + ", ";
        s += "destAddr:" + this.getDestAddr() + ", ";
        s += "ttl:"      + this.getTtl()      + ", ";
        s += "payload:{"  + this.getPayload()  + "}, ";
        s += "nodeRoute:[";
        for (Node node : this.getNodeRoute()) {
            s += "Node:{addr=" + node.getAddr() + "}, ";
        }
        s += "]";
        s += "linkRoute:[";
        for (Link link : this.getLinkRoute()) {
            s += link;
        }
        s += "]";

        return s + "}";
    }
}
