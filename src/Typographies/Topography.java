package Typographies;

import Network.Link;
import Network.Node;

import java.util.List;

public interface Topography {

    public List<Node> getNodes();

    public List<Link> getLinks(Node node);

    public Node getRandomNode();

    public void updateTypography();

}
