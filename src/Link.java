import java.util.Random;

/**
 * Link abstraction that represents any type of link connection on a computer network.
 * Links are unidirectional.
 */
public class Link {
    private static final Random r = new Random();

    private final Node   dest;
    private final double uptime;

    /**
     * Create a new link.
     * @param dest Node at the receiving end of the link.
     * @param uptime Percent time link is active.
     * @throws IllegalArgumentException Thrown if the uptime is not in the range 0 to 1.
     */
    public Link(Node dest, double uptime) throws IllegalArgumentException {
        if (uptime > 1 || uptime < 0) throw new IllegalArgumentException("Uptime out of range 0 to 1");
        this.dest   = dest;
        this.uptime = uptime;
    }

    private boolean canTransmit() {
        return r.nextDouble() < uptime;
    }

    /**
     * Used to send a packet on the link.  Does not return.
     * If the packet fails to send it will be lost (monitor will be notified).
     * @param packet
     */
    public void transmit(Packet packet) {
        if (canTransmit()) {
            dest.recv(packet);
        } else {
            packet.drop(FailureCondition.LINK_FAILURE);
        }
    }

    public String toString() {
        return "Link:{dest=" + dest + ", uptime=" + uptime + "}";
    }
}
