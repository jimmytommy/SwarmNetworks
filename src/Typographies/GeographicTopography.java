package Typographies;

import Network.Link;
import Network.Node;

import java.util.*;

public class GeographicTopography implements Topography {
    private class Point {
        public int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Point that) {
            double dx = this.x - that.x, dy = this.y - that.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public String toString() {
            return "Point:{x=" + x + ", y=" + y + "}";
        }
    }

    private static final double LINK_UPTIME = 1;
    private static final int    LINK_DIST   = 1;
    private static final int    STEPS       = 5;

    public static Random r = new Random();

    public double range;
    public int    size;

    public HashMap<Node, Point> map = new HashMap<Node, Point>();

    public GeographicTopography(int nodes, double range, int size) {
        this.range = range;
        this.size  = size;

        for (int i = 0; i < nodes; i++) {
            this.map.put(new Node(i), new Point(r.nextInt(size), r.nextInt(size)));
        }
    }

    public GeographicTopography(long seed, int nodes, double range, int size) {
        r = new Random(seed);

        this.range = range;
        this.size  = size;

        for (int i = 0; i < nodes; i++) {
            this.map.put(new Node(i), new Point(r.nextInt(size), r.nextInt(size)));
        }

        //r = new Random();
    }

    public Set<Node> getNodes() {
        return map.keySet();
    }

    private Point getPoint(Node node) {
        return map.get(node);
    }

    public Collection<Link> getLinks(Node src) {
        List<Link> links = new ArrayList<Link>();
        for (Node dst : getNodes()) {
            if (isConnected(src, dst)) links.add(new Link(src, dst, LINK_DIST));
        }
        //System.out.println(links.size());
        return links;
    }

    public boolean isConnected(Node src, Node dst) {
        //System.out.println("src = " + getPoint(src) + " dest = " + getPoint(dst));
        //System.out.println(getPoint(src).distanceTo(getPoint(dst)) > range);
        if (src == dst) return false;
        if (getPoint(src).distanceTo(getPoint(dst)) > range) return false;
        return true;
    }

    public boolean canTransmit(Node src, Node dst) {
        return isConnected(src, dst) && r.nextDouble() < LINK_UPTIME;
    }

    public Link getLink(Node src, Node dst) {
        if (isConnected(src, dst)) return new Link(src, dst, LINK_DIST);
        else return null;
    }

    /**
     * Everyone takes a random walk of a certain number of steps.
     */
    public void updateTypography() {
        for (int step = 0; step < STEPS; step++) {
            for (Point p : map.values()) {
                switch (r.nextInt(4)) {
                    case 0:
                        if (p.x < size-1) p.x++;
                        break;
                    case 1:
                        if (p.y < size-1) p.y++;
                        break;
                    case 2:
                        if (p.x > 0)      p.x--;
                        break;
                    case 3:
                        if (p.y > 0)      p.y--;
                        break;
                }
            }
        }
    }

    public Node getRandomNode() {
        int rand = r.nextInt(getNodes().size());
        for (Node node : getNodes()) {
            if (rand-- == 0) return node;
        }
        return null;
    }

    public String toString() {
        String s = "GeographicTopography:{range=" + range + ", size=" + size + ", nodes=[";

        for (Map.Entry<Node, Point> entry : map.entrySet()) {
            s += "(" + entry.getKey() + ", " + entry.getValue() + "), ";
        }

        return s + "]}";
    }
}
