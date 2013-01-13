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

import java.util.Random;

public class AdHocTest implements Runnable {

    public void run() {
        long seed = new Random().nextLong();

        System.out.println("Running Test with BF Router");
        GeographicTopography gt = new GeographicTopography(seed, 10, 1.0, 10);
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

        //System.out.println("Running Test with BF Router");
        gt = new GeographicTopography(seed, 10, 10.0, 100);
        AntRouter ar = new AntRouter(10.0,1.0,1.0,.9,.9);

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
