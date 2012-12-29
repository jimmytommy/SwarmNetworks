import java.util.ArrayList;
import java.util.List;

public class BroadcastMonitor implements Monitor {

    private List<Monitor> monitors = new ArrayList<Monitor>();

    public void registerMonitor(Monitor monitor) {
        this.monitors.add(monitor);
    }

    public void dropped(Packet packet, FailureCondition fc) {
        for (Monitor monitor : this.monitors) {
            monitor.dropped(packet, fc);
        }
    }

    public void arrived(Packet packet) {
        for (Monitor monitor : this.monitors) {
            monitor.arrived(packet);
        }
    }
}
