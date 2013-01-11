package Network;

/**
 * Link abstraction that represents any type of link connection on a computer network.
 * Links are unidirectional.
 */
public class Link {

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

        if (distance != link.distance) return false;
        if (dst != null ? !dst.equals(link.dst) : link.dst != null) return false;
        if (src != null ? !src.equals(link.src) : link.src != null) return false;

        return true;
    }

    public int hashCode() {
        int result = src != null ? src.hashCode() : 0;
        result = 31 * result + (dst != null ? dst.hashCode() : 0);
        result = 31 * result + distance;
        return result;
    }

    public String toString() {
        return "Link:{src=" + src + "dst=" + dst + ", distance=" + distance + "}";
    }
}
