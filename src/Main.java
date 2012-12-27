/**
 * Created with IntelliJ IDEA.
 * User: mdolan
 * Date: 12/17/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static final int PACKETS = 1;

    public static void main(String[] args) {
        Topography t = new GeographicTopography(1000, 10, 100);
        Router     r = new RandomRouter();
        Network    n = new Network(t, r);

        for (int i = 0; i < PACKETS; i++) {
            t.getRandomNode().send(t.getRandomNode(), "Test Packet " + i);
        }

        System.out.println(n);

        n.run(100000);
    }

}
