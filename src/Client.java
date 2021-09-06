import java.io.Serializable;
import java.sql.ClientInfoStatus;
import java.util.List;

public class Client implements Serializable {

    public int ID;
    int req_calls;
    public int xCoord,yCoord;
    String client_SLA;
    String zoneBand;
    boolean client_state;
    int timestamp;

    //List<AccessPoint> connectedAccessPoint;
    //List<MECServer> connectedMECServers;

    public Client(int ID, int xCoord, int yCoord,String zoneBand, boolean client_state,int timestamp) {
        this.ID=ID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zoneBand=zoneBand;
        this.client_state=client_state;
        this.timestamp=timestamp;
        req_calls=2;
    }

    public Client() {

    }

    public static double distance(Client c, AccessPoint AP) {
        return Math.sqrt(Math.pow((c.getYCoord() - AP.getyCoord()), 2) + Math.pow((c.getXCoord() - AP.getxCoord()), 2));
    }

    public int getXCoord() {
        return xCoord;
    }

    public void setXCoord(int XCoord) {
        this.xCoord = XCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setYCoord(int YCoord) {
        this.yCoord = YCoord;
    }

    public String getClient_SLA() {
        return client_SLA;
    }

    public void setClient_SLA(String client_SLA) {
        this.client_SLA = client_SLA;
    }

    public String getZoneBand() {
        return zoneBand;
    }

    public void setZoneBand(String zoneBand) {
        this.zoneBand = zoneBand;
    }

    public boolean isClient_state() {
        return client_state;
    }

    public void setClient_state(boolean client_state) {
        this.client_state = client_state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }


    public String toString() {
        return "Client [ID=" + this.ID+ ", X-Coord=" + this.xCoord + " , Y-Coord=" + this.yCoord + ", ZoneBand=" + this.zoneBand + ", Client State=" + this.client_state + ", Timestamp=" + this.timestamp + "]";
    }
}
