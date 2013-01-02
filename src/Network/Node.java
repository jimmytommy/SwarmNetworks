/**
 * Node class used to represent users on a ad-hoc network.
 * These users can both route and send traffic.
 */

package Network;

import Monitors.FailureCondition;
import Monitors.Monitor;
import util.BoundedQueue;

public class Node {

    private static final int QUEUE_SIZE = 10;

    private final int addr;

    private final BoundedQueue<Packet> queue = new BoundedQueue<Packet>(QUEUE_SIZE);

    public Node(int addr) {
        this.addr = addr;
    }

    /**
     * Getter method for the address of the Node.  Address is analogous to IP address.
     * Address must be a positive integer.
     * @return The address of the node.
     */
    public int getAddr() { return addr; }

    /**
     * Sends the packet to given destination.
     * @param dest Destination node on the network.
     * @param payload Arbitrary payload may be supplied to identify the packet.
     *                If no payload desired, supply null.
     * @param monitor The monitor to notify when packet dropped/arrived.
     */
    public void send(int dstAddr, Object payload, Monitor monitor) {
        Packet packet = new Packet(this.addr, dstAddr, payload, monitor);
        recv(packet);
    }

    /**
     * Notify the node that a packet has arrived for it.
     * Node will handle the packet from there including checking for
     * destination, TTL expiration, and enqueuing the packet onto its
     * queue.
     *
     * This function should only be called by Link.
     *
     * @param packet Packet to be received.
     */
    public void recv(Packet packet) {
        // Add this node to the packets route.
        packet.addToNodeRoute(this);

        // If packet has arrived, signal monitor.
        if (packet.getDstAddr() == addr) {
            packet.arrive();
            return;
        }

        // If TTL has expired, drop packet.
        if (packet.decrTTL() <= 0) {
            packet.drop(FailureCondition.TTL_EXPIRATION);
            return;
        }

        // Enqueue packet but if queue is full, drop packet.
        if (!queue.enqueue(packet)) {
            packet.drop(FailureCondition.QUEUE_OVERFLOW);
        }
    }

    /**
     * Gets the next packet to be sent.
     * @return A packet if one is available, null if no packet found to send.
     */
    public Packet nextToSend() {
        return queue.dequeue();
    }

    public String toString() {
        return "Node:{addr=" + addr + ", queue=" + queue + "}";
    }
}
