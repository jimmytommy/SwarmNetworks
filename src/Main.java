import Mailers.*;
import Monitors.*;
import Network.*;
import Routers.*;
import Typographies.*;

public class Main {

    public static void main(String[] args) {
        GeographicTopography t = new GeographicTopography(10, 3, 10);
        AntRouter      r = new AntRouter();
        Mailer         m = new SimpleMailer(t.getRandomNode(), t.getRandomNode());

        Network n = new Network(t, r, m);

        n.registerMonitor(r);
        n.registerMonitor(new PrintMonitor());

        System.out.println(n);

        n.run(10000);

        r.getStats();

        /*
        TreeTopography t = new TreeTopography(8);
        Router         r = new BFRouter();
        Mailer         m = new SimpleMailer(t.getRoot(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        n.registerMonitor(new PrintMonitor());

        System.out.println(n);

        for (int i = 0; i < 100; i++) {
            r.updateRouter();
        }

        n.run(500);
        */
    }

}

