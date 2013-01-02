package Routers;

import Network.Link;
import Network.Node;
import Network.Packet;
import Typographies.Topography;

import java.util.HashMap;

public class HashBFRouter implements Router {

    private class PathTableEntry {
        public final Node via;
        public final int  cost;

        public PathTableEntry(Node via, int cost) {
            this.via  = via;
            this.cost = cost;
        }
    }

    private class PathTable extends HashMap<Integer, PathTableEntry> {
        public PathTable(Node src) {
            put(src.getAddr(), new PathTableEntry(src, 0));
        }

        public Node via(Integer dstAddr) {
            return (containsKey(dstAddr) ? get(dstAddr).via : null);
        }

        public int cost(Integer dstAddr) {
            return (containsKey(dstAddr) ? get(dstAddr).cost : Integer.MAX_VALUE);
        }
    }

	private Topography t;
    private HashMap<Node, PathTable> pathtables = new HashMap<Node, PathTable>();


	public void setTypography(Topography t){
		this.t = t;
	}

    public Node getNextStep(Node src, Packet packet) throws RuntimeException {
        if (t == null) new RuntimeException("Typography not set");
    	return pathtables.get(src).via(packet.getDstAddr());
    }

    public void updateRouter(){
        pathtables = bellmanFord();
    }

    public HashMap<Node, PathTable> bellmanFord() {
        HashMap<Node, PathTable> pts = new HashMap<Node, PathTable>();

        for (Node src : t.getNodes()) {
            // Create a new fresh page table for this node
            PathTable spt = new PathTable(src);
            pts.put(src, spt);

            for (Link link : t.getLinks(src)) {
                // Pull the old page table for that node
                Node dst = link.getDst();
                PathTable dpt = (pathtables.containsKey(dst) ? pathtables.get(dst) : new PathTable(dst));

                for (int addr : dpt.keySet()) {
                    // Calculate new cost going through this node vs. old cost through old path
                    int newCost = dpt.cost(addr) + link.getDistance();
                    int oldCost = spt.cost(addr);

                    // If less, update page table entry.
                    if (newCost  < oldCost) {
                        spt.put(addr, new PathTableEntry(dst, newCost));
                    }
                }
            }
        }

        return pts;
    }
}