import Monitors.PrintMonitor;
import Network.Network;
import Routers.AntRouter;
import Typographies.LoadBalancing;

import Mailers.*;
import Monitors.*;
import Network.*;
import Routers.*;
import Typographies.*;

public class Main {

    public static void main(String[] args) {
        LoadBalancing lb = new LoadBalancing();
        Network network = new Network(lb, new AntRouter(), lb);
        lb.registerNetwork(network);
        network.registerMonitor(new PrintMonitor());

        network.run(100);

        for (Node node : lb.getNodes()) System.out.println(node);
    }

    /*
    public static void main(String[] args) {
        TreeTopography tt = new TreeTopography(20);
        AntRouter      ar = new AntRouter();

        Topography t = tt;
        Router     r = ar;
        Mailer     m = new SimpleMailer(tt.getRoot(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        n.registerMonitor(ar);
        n.registerMonitor(new Monitors.PrintMonitor());

        System.out.println(n);

        n.run(10000);

        ar.getStats();
    }
    */
}
