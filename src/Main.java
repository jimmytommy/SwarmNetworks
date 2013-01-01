<<<<<<< HEAD
public class Main {

    public static void main(String[] args) {
        TreeTopography tt = new TreeTopography(20);
        AntRouter      ar = new AntRouter();

        Topography t = tt;
        Router     r = ar;
        Mailer     m = new SimpleMailer(tt.getRoot(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        n.registerMonitor(ar);
        n.registerMonitor(new PrintMonitor());

        System.out.println(n);

        n.run(10000);

        ar.getStats();
    }

}
=======
/**
 * Created with IntelliJ IDEA.
 * User: mdolan
 * Date: 12/17/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        /*Topography t = new GeographicTopography(10, 2, 10);
        Router     r = new RandomRouter();
        Mailer     m = new SimpleMailer(t.getRandomNode(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        n.registerMonitor(new PrintMonitor());

        System.out.println(n);

        n.run(100000);*/

        Topography t = new TreeTopography(5);
        Router     r = new BFRouter();
        Mailer     m = new SimpleMailer(t.getRandomNode(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        n.registerMonitor(new PrintMonitor());

        System.out.println(n);

        n.run(5);
    }

}
>>>>>>> initial BFRouter code
