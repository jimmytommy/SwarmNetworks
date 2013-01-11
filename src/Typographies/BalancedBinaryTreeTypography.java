package Typographies;

import Network.Node;

public class BalancedBinaryTreeTypography extends GenericStaticTypography {

    private int i = 0;

    public BalancedBinaryTreeTypography(int depth) {
        Node root = new Node(i++);
        addLevel(root, 0, depth);
    }

    public void addLevel(Node root, int level, int depth) {
        if (level > depth) return;

        Node n1 = new Node(i++);
        Node n2 = new Node(i++);

        addLink(root, n1, 3);
        addLink(root, n2, 3);

        addLevel(n1, level+1, depth);
        addLevel(n2, level+1, depth);
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
