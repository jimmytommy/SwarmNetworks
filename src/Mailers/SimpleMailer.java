package Mailers;

import Network.Network;
import Network.Node;

public class SimpleMailer implements Mailer {

    private int i = 0;
    private Node src, dst;

    public SimpleMailer(Node src, Node dst) {
        this.src = src;
        this.dst = dst;
    }

    public void mail(Network network, int packets) {
        while (packets-- > 0) network.sendPacket(src, dst.getAddr(), "Packet " + i++ + "from " + src + " to " + dst);
    }

    public String toString() {
        return "SimpleMailer:{src=" + src + ", dst=" + dst + ", i=" + i + "}";
    }

}
