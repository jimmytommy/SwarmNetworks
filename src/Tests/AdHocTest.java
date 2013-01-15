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

import java.util.Random;

public class AdHocTest implements Runnable {

    private long seed;

    public void run() {
        seed = new Random().nextLong();

        System.out.println("AdHocTest");
        System.out.println("Running Test with BF Router");
        GeographicTopography gt = new GeographicTopography(seed, 10, 2.0, 5);
        HashBFRouter        hbf = new HashBFRouter();

        Node a = gt.getRandomNode();
        Node b = gt.getRandomNode();
        while (a == b) b = gt.getRandomNode();

        Topography t = gt;
        Router     r = hbf;
        Mailer     m = new SimpleMailer(a, b);

        Network n = new Network(t, r, m, 10, 100, 20);
        //System.out.println(n);

        for (int i = 0; i < 100; i++) r.updateRouter();

        ConclusionMonitor bfcm = new ConclusionMonitor();
        n.registerMonitor(bfcm);
        //n.registerMonitor(new PrintMonitor());
         //n.registerMonitor(new CsvMonitor("CongestionHashBFRouter.csv"));

        n.run(10000);

        System.out.println("BF Router");
        bfcm.printStats();

        System.out.println("Running Test with Ant Router");

        for (int i = 5; i < 51; i+=5) {
            runAnt(i);
        }
        
    }

    private void runAnt(int weightPh) {
        GeographicTopography gt = new GeographicTopography(seed, 10, 2.0, 5);
        AntRouter ar = new AntRouter(weightPh,1.0,1.0,.1,1.0, "AdHocPheromones.csv");

        Node a = gt.getRandomNode();
        Node b = gt.getRandomNode();
        while (a == b) b = gt.getRandomNode();

        Topography t = gt;
        Router r = ar;
        Mailer m = new SimpleMailer(a, b);

        Network n = new Network(t, r, m, 10, 100, 20);
        //System.out.println(n);

        ConclusionMonitor arcm = new ConclusionMonitor();
        n.registerMonitor(arcm);
        n.registerMonitor(ar);
        // n.registerMonitor(new PrintMonitor());
         //n.registerMonitor(new CsvMonitor("CongestionAntRouter.csv"));

        n.run(10000);

        System.out.println("Ant Router " + weightPh);
        arcm.printStats();
    }

}


