package Network;

import Monitors.FailureCondition;

import java.util.Random;

/**
 * Link abstraction that represents any type of link connection on a computer network.
 * Links are unidirectional.
 */
public class Link {
    private static final Random r = new Random();

    private final Node   src, dst;
    private final int    distance;

    public Link(Node src, Node dst, int distance) throws IllegalArgumentException {
        if (distance <= 0) throw new IllegalArgumentException("Distance must be positive value");

        this.src      = src;
        this.dst      = dst;
        this.distance = distance;
    }

    public Node getSrc()      { return this.src;      }
    public Node getDst()      { return this.dst;      }
    public int  getDistance() { return this.distance; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;

        Link link = (Link) o;

        if (!dst.equals(link.dst)) return false;
        if (!src.equals(link.src)) return false;

        return true;
    }

    public int hashCode() {
        int result = src.hashCode();
        result = 31 * result + dst.hashCode();
        return result;
    }

    public String toString() {
        return "Link:{src=" + src + "dst=" + dst + ", distance=" + distance + "}";
    }
}
