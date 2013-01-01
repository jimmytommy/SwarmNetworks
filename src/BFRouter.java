import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class BFRouter implements Router {

	private Topography t;
	private Map<Node, Integer> nodeToInt;
	private PathTable[] pathtables;
	private Map<Integer, Node> intToNode;


	public void setTypography(Topography t){
		this.t = t;
	}

    public Link getNextStep(Node src, Packet packet) throws RuntimeException {
        if (t == null) new RuntimeException("Typography not set");
    	Node n = intToNode.get(packet.getDstAddr());
    	return pathtables[nodeToInt.get(src)].via[nodeToInt.get(n)];

    }

    public void updateRouter(){
    	// create mapping of node to index
    	nodeToInt = new HashMap<Node, Integer>();
    	intToNode = new HashMap<Integer, Node>();
    	// create tables for each node source
    	pathtables = new PathTable[t.getNodes().size()];
    	int i = 0;
    	for (Node n : t.getNodes()) {
    		nodeToInt.put(n, i);
    		intToNode.put(n.getAddr(), n);
    		pathtables[i] = new PathTable(n);
    		i++;
    	}
    	// build out the tables
    	List<Node> updateNodes = new ArrayList<Node>(t.getNodes());
    	while (!updateNodes.isEmpty()) {
    		printTables();
    		updateNodes = bellmanFord(updateNodes);
    	}

    }

    private class PathTable {
    	public Node src;
    	public int[] costs;
    	public Link[] via;

    	public PathTable(Node src) {
    		this.src = src;
    		costs = new int[t.getNodes().size()];
    		via = new Link[t.getNodes().size()];
    		Arrays.fill(costs, Integer.MAX_VALUE);
    		Arrays.fill(via, null);
    		int index = nodeToInt.get(src);
    		costs[index] = 0;
    	}
    }

    private List<Node> bellmanFord(List<Node> updateNodes) {
    	List<Node> changedNodes = new ArrayList<Node>();
    	// iterate through each updateNodes as src
    	// iterate through all destinations and see if a better path exists
    	for (Node src : t.getNodes()) {
    		boolean altered = false;
    		int src_index = nodeToInt.get(src);
    		for (Node dest : t.getNodes()) {
    			int dest_index = nodeToInt.get(dest);
    			// go through neighbor nodes and see if someone has a shorter path
    			int best_cost = pathtables[src_index].costs[dest_index];
    			Link best_path = pathtables[src_index].via[dest_index];
    			for (Link link : t.getLinks(src)) {
    				int link_node_index = nodeToInt.get(link.getDest());
    				int cost = link.getDistance() + pathtables[link_node_index].costs[dest_index];
    				if (cost < best_cost && pathtables[link_node_index].via[dest_index] != null) {
    					best_cost = cost;
    					best_path = link;
    					altered = true;
    				}
    			}
    			pathtables[src_index].costs[dest_index] = best_cost;
    			pathtables[src_index].via[dest_index] = best_path;
    		}
    		if (altered) {
    			for (Link link : t.getLinks(src)) {
    				if (!changedNodes.contains(link.getDest())) {
    					changedNodes.add(link.getDest());
    				}
    			}
    		}
    	}
    	return changedNodes;
    }

    private void printTables() {
    	for (int i = 0; i < pathtables.length; i++) {
    		System.out.println(i);
    		for (int j = 0; j < pathtables.length; j++) {
    			System.out.print(j + " " + pathtables[i].costs[j] + " ");
    			if (pathtables[i].via[j] == null) {
					System.out.println("-");
				}
				else {
					System.out.println(nodeToInt.get(pathtables[i].via[j].getDest()));
				}
    		}
    		System.out.println();
    	}
    }


}