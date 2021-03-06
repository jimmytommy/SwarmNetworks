package Routers;

import Network.Link;
import Network.Node;
import Network.Packet;
import Typographies.Topography;

import java.util.Collection;
import java.util.Random;

public class RandomRouter implements Router {

    private static final Random r = new Random();

    private Topography t = null;

    public void setTypography(Topography t) {
        this.t = t;
    }

    public Node getNextStep(Node src, Packet packet) throws RuntimeException {
        if (t == null) new RuntimeException("Typography not set");
        Collection<Link> links = t.getLinks(src);
        if (links.size() == 0) return null;
        else                   return (Node) links.toArray()[r.nextInt(links.size())];
    }

    public void updateRouter() {
        /* Nothing to do because nothing is pre-calculated */
    }

    public String toString() {
        return "Random Router:{typography=" + t + "}";
    }
}
