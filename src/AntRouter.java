import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

//algorithm taken from http://en.wikipedia.org/wiki/Ant_colony_optimization_algorithms
public class AntRouter implements Router, Monitor {

    private static final Random r = new Random();
    private static final double weightPh = 100; //weight factor for pheromone trails
    private static final double weightDist = .1; //weight factor for link distances
    private static final double addPh = 10; //weights how much pheromone is added to trail
    private static final double evapRate = 0; //evaporation rate of pheromone trails

    private Topography t = null;
    private Hashtable<Link, Double> pheromones = null;
    private int arrivedNum = 0;
    private double avgPathLength = 0;


    public void setTypography(Topography t) {
        this.t = t;
        this.pheromones = new Hashtable<Link, Double>();
    }

    //edge selection
    public Link getNextStep(Node src, Packet packet) throws RuntimeException {
        if (t == null) new RuntimeException("Typography not set");
        List<Link> links = t.getLinks(src);
        if (links.size() <= 0) return null;

        double[] weights = new double[links.size()];
        double sum = 0;

        int index = 0;
        for (Link l : links)
        {
            double ph = (pheromones.containsKey(l) ? pheromones.get(l) : 1);
            double dist = l.getDistance();
            double w = Math.pow(ph, weightPh)* Math.pow(dist, weightDist);
            weights[index++] = w;
            sum += w;
        }

        double rand = r.nextDouble() * sum;

        double tot = 0;
        for (int i = 0; i < weights.length; i++)
        {
            tot += weights[i];
            if (rand < tot) return links.get(i);
        }

        return null;
    }

    //pheromone update - trails evaporate over time
    public void updateRouter() {
        for (Link l : pheromones.keySet())
        {
            double ph = (pheromones.containsKey(l) ? pheromones.get(l) : 1);
            pheromones.put(l, (ph * (1 - evapRate)));
        }
    }

    public String toString() {
        return "Ant Router:{typography=" + t + "}";
    }

    @Override
    public void dropped(Packet packet, FailureCondition fc) {
        //System.out.println("Dropped: " + packet + " - " + fc);
        //Ignore dropped packets
    }

    @Override
    //If arrived, lay down a pheromone path
    public void arrived(Packet packet) {

        double dist = 0;
        for (Link l : packet.getLinkRoute())
        {
            dist += l.getDistance();
        }

        double delta = (addPh / dist);

        for (Link l : packet.getLinkRoute())
        {
            double ph = (pheromones.containsKey(l) ? pheromones.get(l) : 1);
            ph += delta;

            pheromones.put(l, ph);
        }

        int printNum = 1000;
        arrivedNum++;
        avgPathLength += dist;
        if (arrivedNum % printNum == 0)
        {
            System.out.println((arrivedNum - printNum) + "-" + arrivedNum + " Average = " + (avgPathLength / (double) printNum));
            avgPathLength = 0;
        }

        //System.out.print("Arrived: Path Length: " + dist + ", " + packet.getPayload().toString() + ", Route: {");
        //for (Node n : packet.getNodeRoute())
        //    System.out.print(n.getAddr() + ", ");
        //System.out.println();
    }
}