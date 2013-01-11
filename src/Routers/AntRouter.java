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

    private static final Random r = new Random();
    private final double weightPh; // weight factor for pheromone trails - should be >= 0
    private final double weightDist; // weight factor for link distances - should be >= 1
    private final double addPh; // weights how much pheromone is added to trail
    private final double evapRate; // evaporation rate of pheromone trails - should be in [0,1]
    private final double punishPh; // weights how much dropped paths are punished - should be in [0,1]

    private Topography t                       = null;
    private Hashtable<Integer, Hashtable<Link, Double>> pheromones = null;

    private int    arrivedNum        = 0;
    private int    droppedNum        = 0;
    private double partAvgPathLength = 0;
    private double totPathLength     = 0;
    private double bestPathLength    = Double.MAX_VALUE;

    public AntRouter()
    {
        this.weightPh = 1.0;
        this.weightDist = 1.0;
        this.addPh = 1.0;
        this.evapRate = 0.9;
        this.punishPh = 0.9;
    }

    public AntRouter(double weightPh, double weightDist, double addPh, double evapRate, double punishPh)
    {
        if ((weightPh < 0) || (weightDist < 1) || (evapRate < 0) || (evapRate > 1) ||
                (punishPh < 0) || (punishPh > 1))
            throw new IllegalArgumentException("Argument Out of Bounds");

        this.weightPh = weightPh;
        this.weightDist = weightDist;
        this.addPh = addPh;
        this.evapRate = evapRate;
        this.punishPh = punishPh;
    }

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

        for (Node n : t.getNodes())
        {
            Collection<Link> links = t.getLinks(n);
            Hashtable<Link, Double> hash = pheromones.get(n.getAddr());
            if (hash != null)
            {
                for (Link l : hash.keySet())
                {
                    if (!links.contains(l)) pheromones.remove(l);
                }
            }
        }

        for (Integer i : pheromones.keySet())
        {
            Hashtable<Link, Double> hash = pheromones.get(i);
            for (Link link: hash.keySet())
            {
                double ph = hash.get(link);
                ph *= (1 - evapRate);
                if (ph < 1.0) ph = 1.0;
                hash.put(link, ph);
            }
            pheromones.put(i, hash);
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
        double queuePunish = 1.0;
        double linkPunish = 1.0;
        double routePunish = 1.0;
        double TTLPunish = 10.0;

        droppedNum++;

        double length = packet.getLinkRoute().size();

        Hashtable routes = pheromones.get(packet.getDstAddr());
        if (routes == null) routes = new Hashtable<Link, Double>();

        double weight;
        if (fc.toString() == "LINK_FAILURE")
            weight = punishPh * linkPunish;
        else if ((fc.toString() == "ROUTING_FAILURE") || (fc.toString() == "OBSOLETE_ROUTE"))
            weight = punishPh * routePunish;
        else if (fc.toString() == "TTL_EXPIRATION")
            weight = punishPh * TTLPunish;
        else
            weight = punishPh * queuePunish;

        double i = length;
        HashSet<Link> linkRoute = new HashSet<Link>();
        for (Link l : packet.getLinkRoute())
        {
            linkRoute.add(l);
        }

        for (Link l : linkRoute)
        {
            double punish = (i > 0 ? i / length : 0) * weight;
            double ph = (Double) (routes.containsKey(l) ? routes.get(l) : 1.0);
            ph *= (1 - punish);
            if (ph < 1.0) ph = 1.0;
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

        HashSet<Link> linkRoute = new HashSet<Link>();
        for (Link l : packet.getLinkRoute())
        {
            linkRoute.add(l);
        }

        for (Link l : linkRoute)
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