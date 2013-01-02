import Mailers.*;
import Monitors.*;
import Network.*;
import Routers.*;
import Typographies.*;

public class Main {

    public static void main(String[] args) {
        /*TreeTopography tt = new TreeTopography(20);
        AntRouter      ar = new AntRouter();

        Topography t = tt;
        Router     r = ar;
        Mailer     m = new SimpleMailer(tt.getRoot(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        n.registerMonitor(ar);
        n.registerMonitor(new PrintMonitor());

        System.out.println(n);

        n.run(10000);

        ar.getStats();*/


        Topography t = new TreeTopography(8);
        Router     r = new BFRouter();
        Mailer     m = new SimpleMailer(t.getRandomNode(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        n.registerMonitor(new PrintMonitor());

        System.out.println(n);

        n.run(5);
    }

}

