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
import Typographies.Topography;

public class AdHocTest implements Runnable {

    public void run() {
        System.out.println("Running Test with BF Router");
        GeographicTopography gt = new GeographicTopography(10, 2.0, 5);
        HashBFRouter        hbf = new HashBFRouter();

        Node a = gt.getRandomNode();
        Node b = gt.getRandomNode();
        while (a == b) b = gt.getRandomNode();

        Topography t = gt;
        Router     r = hbf;
        Mailer     m = new SimpleMailer(a, b);

        Network n = new Network(t, r, m, 1, 10000, 20);
        System.out.println(n);

        for (int i = 0; i < 100; i++) r.updateRouter();

        ConclusionMonitor bfcm = new ConclusionMonitor();
        n.registerMonitor(bfcm);
        //n.registerMonitor(new PrintMonitor());
        // n.registerMonitor(new CsvMonitor("CongestionHashBFRouter.csv"));

        n.run(10000);

        System.out.println("BF Router");
        bfcm.printStats();

        System.out.println("Running Test with Ant Router");
        gt = new GeographicTopography(5, 2.0, 4);
        AntRouter ar = new AntRouter();

        a = gt.getRandomNode();
        b = gt.getRandomNode();
        while (a == b) b = gt.getRandomNode();

        t = gt;
        r = ar;
        m = new SimpleMailer(a, b);

        n = new Network(t, r, m, 1, 10000, 20);
        System.out.println(n);

        ConclusionMonitor arcm = new ConclusionMonitor();
        n.registerMonitor(arcm);
        n.registerMonitor(ar);
        // n.registerMonitor(new PrintMonitor());
        // n.registerMonitor(new CsvMonitor("CongestionAntRouter.csv"));

        n.run(10000);

        System.out.println("Ant Router");
        arcm.printStats();
    }

}

