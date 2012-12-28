import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

//algorithm taken from http://en.wikipedia.org/wiki/Ant_colony_optimization_algorithms
public class AntRouter implements Router, Monitor {

    private static final Random r = new Random();
    private static final double weightPh = .5; //weight factor for pheromone trails
    private static final double weightDist = .5; //weight factor for link distances
    private static final double addPh = 1; //weights how much pheromone is added to trail
    private static final double evapRate = .1; //evaporation rate of pheromone trails

    private Topography t = null;
    private Hashtable<Link, Double> pheromones = null;


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
        //Ignore dropped packets
    }

    @Override
    //If arrived, lay down a pheromone path
    public void arrived(Packet packet) {

        System.out.println("arrived!!");
        double dist = 0;
        for (Link l : packet.getLinkRoute())
        {
            dist += l.getDistance();
        }

        double delta = (addPh / dist);

        for (Link l : packet.getLinkRoute())
        {
            double ph = pheromones.get(l);
            ph += delta;

            pheromones.put(l, ph);
        }

    }
}