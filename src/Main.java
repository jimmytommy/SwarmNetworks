/**
 * Created with IntelliJ IDEA.
 * User: mdolan
 * Date: 12/17/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        Topography t = new RandomStaticTopography(100, 1000);
        AntRouter  r = new AntRouter();
        Mailer     m = new SimpleMailer(t.getRandomNode(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        //n.registerMonitor(new PrintMonitor());
        n.registerMonitor(r);

        System.out.println(n);

        n.run(10000);

        r.getStats();
    }

}
