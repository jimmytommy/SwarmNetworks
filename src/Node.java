/**
 * Created with IntelliJ IDEA.
 * User: mdolan
 * Date: 12/17/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Node {

    private static final int QUEUE_SIZE = 10;

    private final int addr;
    private final BoundedQueue<Packet> queue = new BoundedQueue<Packet>(QUEUE_SIZE);

    public Node(int addr) {
        this.addr = addr;
    }

    public int getAddr() { return addr; }

    public void send(Node dest, Object payload) {
        Packet packet = new Packet(this.addr, dest.addr, payload);
        recv(packet);
    }

    public void recv(Packet packet) {
        packet.addToRoute(this);
        if (packet.decrTTL() <= 0) {
            packet.drop(FailureCondition.TTL_EXPIRATION);
        } else {
            if (!queue.enqueue(packet)) {
                packet.drop(FailureCondition.QUEUE_OVERFLOW);
            }
        }
    }

    /**
     * Get's the next packet to be sent.
     * Returns null if no packet to be found.
     * @return
     */
    public Packet nextToSend() {
        return queue.dequeue();
    }

    public String toString() {
        return "Node:{addr=" + addr + ", queue=" + queue + "}";
    }
}
