package Monitors;

import Network.Packet;

public interface Monitor {

    public void dropped(Packet packet, FailureCondition fc);

    public void arrived(Packet packet);

}
