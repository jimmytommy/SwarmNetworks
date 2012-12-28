import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    }

    private static final double LINK_UPTIME = 1;
    private static final int    LINK_DIST   = 1;

    public static Random r = new Random();

    public double      range;
    public int         size;
    public List<Node>  nodes;
    public List<Point> points;

    public GeographicTopography(int nodes, double range, int size) {
        this.range = range;
        this.size  = size;

        this.nodes  = new ArrayList<Node>(nodes);
        this.points = new ArrayList<Point>(nodes);

        for (int i = 0; i < nodes; i++) {
            this.nodes.add(new Node(i));
            this.points.add(new Point(r.nextInt(size), r.nextInt(size)));
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Returns all the nodes that are within transmission distance.
     * @param node
     * @return
     */
    public List<Link> getLinks(Node node) {
        Point point = points.get(node.getAddr());

        List<Link> links = new ArrayList<Link>();
        for (int i = 0; i < nodes.size(); i++) {
            if (i != node.getAddr()) {
                if (point.distanceTo(points.get(i)) < range) {
                    links.add(new Link(nodes.get(i), LINK_DIST, LINK_UPTIME));
                }
            }
        }

        return links;
    }

    public Node getRandomNode() {
        return nodes.get(r.nextInt(nodes.size()));
    }

    /**
     * Everyone takes a random walk of a certain number of steps.
     */
    public void updateTypography() {
        for (int step = 0; step < 5; step++) {
            for (Point p : points) {
                switch (r.nextInt(4)) {
                    case 0:
                        if (p.x < size-1) p.x++;
                    case 1:
                        if (p.y < size-1) p.y++;
                    case 2:
                        if (p.x > 0)      p.x--;
                    case 3:
                        if (p.y > 0)      p.y--;
                }
            }
        }
    }
}
