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
import Typographies.BalancedBinaryTreeTypography;
import Typographies.Topography;

public class BinaryTreeTest implements Runnable {

    public void run() {
        System.out.println("BinaryTreeTest");
        System.out.println("Running Test with BF Router");
        BalancedBinaryTreeTypography bbt = new BalancedBinaryTreeTypography(5);
        HashBFRouter                hbf = new HashBFRouter();

        Topography t = bbt;
        Router     r = hbf;
        Mailer     m = new SimpleMailer(bbt.getRoot(), bbt.getLeaf());

        Network n = new Network(t, r, m, 10, 10, 1);
        r.updateRouter();

        ConclusionMonitor bfcm = new ConclusionMonitor();
        n.registerMonitor(bfcm);
        // n.registerMonitor(new PrintMonitor());
        // n.registerMonitor(new CsvMonitor("CongestionHashBFRouter.csv"));

        n.run(10000);   

        System.out.println("Conclusions");
        System.out.println("BF Router");
        bfcm.printStats(); 

        for (int i = 5; i < 51; i+=5) {
            runAnt(i);
        }    
    }

    private void runAnt(int weightPH) {
        System.out.println("Running Test with Ant Router");
        BalancedBinaryTreeTypography bbt = new BalancedBinaryTreeTypography(5);
        AntRouter ar = new AntRouter(weightPH, 1, 1, 0, .9, "BinaryTreeTest");

        Topography t = bbt;
        Router r = ar;
        Mailer m = new SimpleMailer(bbt.getRoot(), bbt.getLeaf());

        Network n = new Network(t, r, m, 10, 10, 1);

        ConclusionMonitor arcm = new ConclusionMonitor();
        n.registerMonitor(arcm);
        n.registerMonitor(ar);
         //n.registerMonitor(new PrintMonitor());
        // n.registerMonitor(new CsvMonitor("CongestionAntRouter.csv"));

        n.run(10000);

        System.out.println("Ant Router " + weightPH);
        arcm.printStats();
    }

}
