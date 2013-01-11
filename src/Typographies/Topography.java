package Typographies;

import Network.Link;
import Network.Node;

import java.util.Collection;
import java.util.Set;

public interface Topography {

    public Set<Node> getNodes();

    public Collection<Link> getLinks(Node node);

    public boolean isConnected(Node src, Node dst);

    public Link getLink(Node src, Node dst);

    public boolean canTransmit(Node src, Node dst);

    public void updateTypography();

}
