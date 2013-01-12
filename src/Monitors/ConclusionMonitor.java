package Monitors;

import Network.Packet;

import java.util.HashMap;
import java.util.Map;

public class ConclusionMonitor implements Monitor {
    int arrived = 0;
    int dropped = 0;

    HashMap<FailureCondition, Integer> causes = new HashMap<FailureCondition, Integer>();

    public void dropped(Packet packet, FailureCondition fc) {
        dropped++;
        int i = causes.containsKey(fc) ? causes.get(fc) : 0;
        i++;
        causes.put(fc, i);
    }

    public void arrived(Packet packet) {
        arrived++;
    }

    private int total() { return arrived + dropped; }

    public void printStats() {
        System.out.println("===================================================================");
        System.out.println("Arrived - " + arrived + "/" + total());
        System.out.println("Dropped - " + dropped + "/" + total());
        System.out.println("Causes - ");
        for (Map.Entry<FailureCondition, Integer> e : causes.entrySet()) {
            System.out.println(e.getKey() + " - " + e.getValue());
        }
        System.out.println("===================================================================");
    }
}
