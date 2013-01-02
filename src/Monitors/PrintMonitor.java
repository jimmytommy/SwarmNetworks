package Monitors;

import Network.Packet;

public class PrintMonitor implements Monitor {
    public void dropped(Packet packet, FailureCondition fc) {
        System.out.println("Dropped: " + packet + " - " + fc);
    }

    public void arrived(Packet packet) {
        System.out.println("Arrived: " + packet);
        System.out.println(packet.getLinkRoute().size());
    }
}
