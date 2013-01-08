package Routers;

import Monitors.FailureCondition;
import Monitors.Monitor;
import Network.Link;
import Network.Node;
import Network.Packet;
import Typographies.Topography;

import java.util.*;

//algorithm taken from http://en.wikipedia.org/wiki/Ant_colony_optimization_algorithms
public class AntRouter implements Router, Monitor {

    private static final Random r          = new Random();
    private static final double weightPh   = 3.0; // weight factor for pheromone trails - should be >= 0
    private static final double weightDist = 1.0; // weight factor for link distances - should be >= 1
    private static final double addPh      = 20.0; // weights how much pheromone is added to trail
    private static final double evapRate   = 0.1; // evaporation rate of pheromone trails - should be in [0,1)
    private static final double punishPh   = .9; // weights how much dropped paths are punished - should be in [0,1]

    private Topography t                       = null;
    private Hashtable<Integer, Hashtable<Link, Double>> pheromones = null;

    private int    arrivedNum        = 0;
    private int    droppedNum        = 0;
    private double partAvgPathLength = 0;
    private double totPathLength     = 0;
    private double bestPathLength    = Double.MAX_VALUE;


    public void setTypography(Topography t) {
        this.t          = t;
        this.pheromones = new Hashtable<Integer, Hashtable<Link, Double>>();
    }

    // edge selection
    public Node getNextStep(Node src, Packet packet) throws RuntimeException {
        if (t == null) new RuntimeException("Typography not set");

        List<Link> links = new ArrayList<Link>(t.getLinks(src));
        if (links.size() <= 0) return null;

        Hashtable routes = pheromones.get(packet.getDstAddr());
        if (routes == null) routes = new Hashtable<Link, Double>();

        double[] weights = new double[links.size()];
        double sum = 0;

        int index = 0;
        for (Link l : links)
        {
            double ph   = (Double) (routes.containsKey(l) ? routes.get(l) : 1.0);
            double dist = l.getDistance();
            double w    = Math.pow(ph, weightPh) * Math.pow(dist, weightDist);

            weights[index++] = w;

            sum += w;
        }

        double rand = r.nextDouble() * sum;

        double tot = 0;
        for (int i = 0; i < weights.length; i++)
        {
            tot += weights[i];
            if (rand < tot) return links.get(i).getDst();
        }

        return null;
    }

    //pheromone update - trails evaporate over time
    public void updateRouter() {
        for (Hashtable routes : pheromones.values())
        {
            for (Object pher : routes.values())
            {
                double ph = (Double) pher * (1 - evapRate); //(Double) (routes.containsKey(l) ? routes.get(l) : 1);
                if (ph < 1.0) ph = 1.0;
                //routes.put(l, (ph * (1 - evapRate)));
            }
        }
    }

    public void getStats()
    {
        System.out.println("----------------Ant Router Performance----------------------------");
        System.out.println("Packets Arrived: " + arrivedNum + "/" + (arrivedNum + droppedNum) + " total");
        System.out.println("Best found path: " + (bestPathLength != Double.MAX_VALUE ? bestPathLength : "None found"));
        System.out.println("Average path length: " + totPathLength / arrivedNum);
    }

    public void dropped(Packet packet, FailureCondition fc) {
        droppedNum++;

        double length = packet.getLinkRoute().size();

        Hashtable routes = pheromones.get(packet.getDstAddr());
        if (routes == null) routes = new Hashtable<Link, Double>();

        double i = length;
        for (Link l : packet.getLinkRoute())
        {
            double punish = (i > 0 ? i / length : 0) * punishPh;
            double ph = (Double) (routes.containsKey(l) ? routes.get(l) : 1.0);
            ph *= (1 - punish);
            routes.put(l, ph);
            i--;
        }

        pheromones.put(packet.getDstAddr(), routes);


        // System.out.println("Dropped: " + " - " + fc);
        // Ignore dropped packets
    }

    //If arrived, lay down a pheromone path
    public void arrived(Packet packet) {
        double dist = 0;
        for (Link l : packet.getLinkRoute())
        {
            dist += l.getDistance();
        }

        double delta = addPh / dist;

        Hashtable routes = pheromones.get(packet.getDstAddr());
        if (routes == null) routes = new Hashtable<Link, Double>();

        for (Link l : packet.getLinkRoute())
        {
            double ph = (Double) (routes.containsKey(l) ? routes.get(l) : 1.0);
            ph += delta;
            routes.put(l, ph);
        }

        pheromones.put(packet.getDstAddr(), routes);

        int printNum = 1;
        arrivedNum++;
        partAvgPathLength += dist;
        totPathLength += dist;
        if (dist < bestPathLength) bestPathLength = dist;
        /*
        if (arrivedNum % printNum == 0)
        {
            System.out.println((arrivedNum - printNum) + "-" + arrivedNum + " Average = " + (partAvgPathLength / (double) printNum));
            partAvgPathLength = 0;
        }
        */

        //System.out.print("Arrived: Path Length: " + dist + ", " + packet.getPayload().toString() + ", Route: {");
        //for (Node n : packet.getNodeRoute())
        //    System.out.print(n.getAddr() + ", ");
        //System.out.println();
    }

    public String toString() {
        return "Ant Router:{typography=" + t + "}";
    }
}