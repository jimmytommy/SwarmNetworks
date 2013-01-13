package Monitors;

import Network.Packet;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.Exception;

public class CsvMonitor implements Monitor {

    private BufferedWriter out;

    public CsvMonitor(String filename) {
        try {
            out = new BufferedWriter(new FileWriter(filename), 32768);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String s = "status, source address, destination address, ttl, payload, FailureCondition\n";
        try {
            out.write(s);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropped(Packet packet, FailureCondition fc) {
        String s = "";
        s += "dropped, ";
        s += packet.getSrcAddr() + ", ";
        s += packet.getDstAddr() + ", ";
        s += packet.getTtl() + ", ";
        s += packet.getPayload() + ", ";
        s += fc + "\n";

        try {
            out.write(s);
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void arrived(Packet packet) {
        String s = "";
        s += "arrived, ";
        s += packet.getSrcAddr() + ", ";
        s += packet.getDstAddr() + ", ";
        s += packet.getTtl() + ", ";
        s += packet.getPayload() + "\n";

        try  {
            out.write(s);
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}