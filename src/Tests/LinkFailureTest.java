package Tests;

import Mailers.Mailer;
import Mailers.SimpleMailer;
import Monitors.ConclusionMonitor;
import Monitors.CsvMonitor;
import Monitors.PrintMonitor;
import Network.Node;
import Network.Network;
import Routers.AntRouter;
import Routers.HashBFRouter;
import Routers.Router;
import Typographies.CongestionExampleTypography;
import Typographies.GeographicTopography;
import Typographies.LinkFailureExample;
import Typographies.Topography;

public class LinkFailureTest implements Runnable {

    public void run() {
        System.out.println("Running Test with BF Router");
        LinkFailureExample lft = new LinkFailureExample();
        HashBFRouter       hbf = new HashBFRouter();

        Topography t = lft;
        Router     r = hbf;
        Mailer     m = lft;

        Network n = new Network(t, r, m, 7, 10, 1);
        r.updateRouter();

        ConclusionMonitor bfcm = new ConclusionMonitor();
        n.registerMonitor(bfcm);
        // n.registerMonitor(new PrintMonitor());
        // n.registerMonitor(new CsvMonitor("CongestionHashBFRouter.csv"));

        n.run(2000);

        System.out.println("Running Test with Ant Router");

        lft = new LinkFailureExample();
        AntRouter ar = new AntRouter(1.0,1.0,7.0,.1,1.0,"LinkFailurePheromones.csv");

        t = lft;
        r = ar;
        m = lft;

        n = new Network(t, r, m, 70, 100, 1);

        ConclusionMonitor arcm = new ConclusionMonitor();
        n.registerMonitor(arcm);
        n.registerMonitor(ar);
        // n.registerMonitor(new PrintMonitor());
        // n.registerMonitor(new CsvMonitor("CongestionAntRouter.csv"));

        n.run(2000);

        System.out.println("Conclusions");
        System.out.println("BF Router");
        bfcm.printStats();
        System.out.println("Ant Router");
        arcm.printStats();
    }
}
