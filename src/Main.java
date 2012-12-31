public class Main {

    public static void main(String[] args) {
        TreeTopography tt = new TreeTopography(20);
        AntRouter      ar = new AntRouter();

        Topography t = tt;
        Router     r = ar;
        Mailer     m = new SimpleMailer(tt.getRoot(), t.getRandomNode());

        Network    n = new Network(t, r, m);

        n.registerMonitor(ar);
        n.registerMonitor(new PrintMonitor());

        System.out.println(n);

        n.run(10000);

        ar.getStats();
    }

}
