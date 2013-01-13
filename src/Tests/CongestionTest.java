package Tests;

import Mailers.Mailer;
import Monitors.ConclusionMonitor;
import Monitors.CsvMonitor;
import Monitors.PrintMonitor;
import Network.Network;
import Routers.AntRouter;
import Routers.HashBFRouter;
import Routers.Router;
import Typographies.CongestionExampleTypography;
import Typographies.Topography;

public class CongestionTest implements Runnable {

    public void run() {
        System.out.println("CongestionTest");
        System.out.println("Running Test with BF Router");
        CongestionExampleTypography cet = new CongestionExampleTypography();
        HashBFRouter                hbf = new HashBFRouter();

        Topography t = cet;
        Router     r = hbf;
        Mailer     m = cet;

        Network n = new Network(t, r, m, 10, 10, 1);
        r.updateRouter();

        ConclusionMonitor bfcm = new ConclusionMonitor();
        n.registerMonitor(bfcm);
        // n.registerMonitor(new PrintMonitor());
         n.registerMonitor(new CsvMonitor("CongestionHashBFRouter.csv"));

        n.run(200);

        System.out.println("Running Test with Ant Router");
        cet = new CongestionExampleTypography();
        AntRouter ar = new AntRouter(1.0,1.0,1.0,.1,1.0,"CongestionPheromones.csv");

        t = cet;
        r = ar;
        m = cet;

        n = new Network(t, r, m, 10, 10, 1);

        ConclusionMonitor arcm = new ConclusionMonitor();
        n.registerMonitor(arcm);
        n.registerMonitor(ar);
        // n.registerMonitor(new PrintMonitor());
         n.registerMonitor(new CsvMonitor("CongestionAntRouter.csv"));

        n.run(200);

        System.out.println("Conclusions");
        System.out.println("BF Router");
        bfcm.printStats();
        System.out.println("Ant Router");
        arcm.printStats();
    }

}

