package Routers;

import Network.Link;
import Network.Node;
import Network.Packet;
import Typographies.Topography;

public interface Router {

    public void setTypography(Topography t);

    public Node getNextStep(Node src, Packet packet);

    public void updateRouter();

}
