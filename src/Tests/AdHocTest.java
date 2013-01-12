package Tests;

import Mailers.Mailer;
import Mailers.SimpleMailer;
import Monitors.ConclusionMonitor;
import Monitors.CsvMonitor;
import Monitors.PrintMonitor;
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
        GeographicTopography gt = new GeographicTopography(20, 4.0, 20);
        HashBFRouter        hbf = new HashBFRouter();

        Topography t = gt;
        Router     r = hbf;
        Mailer     m = new SimpleMailer(gt.getRandomNode(), gt.getRandomNode());

        Network n = new Network(t, r, m, 100, 1, 5);

        ConclusionMonitor bfcm = new ConclusionMonitor();
        n.registerMonitor(bfcm);
        // n.registerMonitor(new PrintMonitor());
        // n.registerMonitor(new CsvMonitor("CongestionHashBFRouter.csv"));

        n.run(10000);

        System.out.println("Running Test with Ant Router");

        System.out.println("Running Test with BF Router");
        gt = new GeographicTopography(12, 2.0, 10);
        AntRouter ar = new AntRouter();

        t = gt;
        r = ar;
        m = new SimpleMailer(gt.getRandomNode(), gt.getRandomNode());

        n = new Network(t, r, m, 100, 1, 5);

        ConclusionMonitor arcm = new ConclusionMonitor();
        n.registerMonitor(arcm);
        n.registerMonitor(ar);
        // n.registerMonitor(new PrintMonitor());
        // n.registerMonitor(new CsvMonitor("CongestionAntRouter.csv"));

        n.run(10000);

        System.out.println("Conclusions");
        System.out.println("BF Router");
        bfcm.printStats();
        System.out.println("Ant Router");
        arcm.printStats();
    }

}
