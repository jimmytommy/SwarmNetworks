package Monitors;

import Network.Link;
import Network.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConclusionMonitor implements Monitor {
    int arrived = 0;
    int dropped = 0;
    double best = Double.MAX_VALUE;
    double sum = 0;
    List<Link> bestPath;

    HashMap<FailureCondition, Integer> causes = new HashMap<FailureCondition, Integer>();

    public void dropped(Packet packet, FailureCondition fc) {
        dropped++;
        int i = causes.containsKey(fc) ? causes.get(fc) : 0;
        i++;
        causes.put(fc, i);
    }

    public void arrived(Packet packet) {
        arrived++;

        int length = 0;
        List<Link> route = packet.getLinkRoute();
        for (Link l : route)
        {
            length += l.getDistance();
        }

        sum += length;
        if (length < best) {best = length; bestPath = route;}

    }

    private int total() { return arrived + dropped; }

    public void printStats() {
        System.out.println("===================================================================");
        System.out.println("Arrived - " + arrived + "/" + total());
        System.out.println("Dropped - " + dropped + "/" + total());
        System.out.println("Causes - ");
        for (Map.Entry<FailureCondition, Integer> e : causes.entrySet()) {
            System.out.println("\t" + e.getKey() + " - " + e.getValue());
        }
        System.out.println("Best found path - " + (best != Double.MAX_VALUE ? best : "No solution found"));
        System.out.println("Average found path - " + (sum / arrived));
        System.out.println("===================================================================");
    }
}
