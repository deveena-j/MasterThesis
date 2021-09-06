import java.io.*;
import java.util.*;

public class AccessPoint  extends NetworkNode implements Serializable {

    public int cluster_ID;

    public AccessPoint(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;

    }

    public AccessPoint(int ID, String zoneBand, int xCoord, int yCoord) {
        this.ID = ID;
        this.zoneBand = zoneBand;
        this.xCoord = xCoord;
        this.yCoord = yCoord;

    }

    public int getCluster_ID() {
        return cluster_ID;
    }

    public void setCluster_ID(int cluster_ID) {
        this.cluster_ID = cluster_ID;
    }

    public static double distance(Client c, AccessPoint AP) {
        return Math.sqrt(Math.pow((AP.getyCoord() - c.getYCoord()), 2) + Math.pow((AP.getxCoord() - c.getXCoord()), 2));
    }


    public String toString() {
        return "Access Point [ID=" + ID + " ,Zone Band=" + zoneBand + ", xCoord=" + xCoord + ", yCoord=" + yCoord + "]";

    }
}

