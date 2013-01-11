package Typographies;

import Network.Link;
import Network.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStaticTopography extends GenericStaticTypography {

    private static final int LINK_DIST = 1;

    public RandomStaticTopography(int nodes, int links) {
        for (int i = 0; i < nodes; i++) {
            addNode(new Node(i));
        }

        for (int i = 0; i < links; i++) {
            Node src  = getRandomNode();
            Node dst = getRandomNode();
            addLink(src, dst, LINK_DIST);
        }
    }

}
