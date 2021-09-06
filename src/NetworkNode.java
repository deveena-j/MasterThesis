import java.io.Serializable;

public class NetworkNode implements Serializable {
    public int xCoord;
    public int yCoord;
    public int ID;
    public String zoneBand="10S";

    private static int counter;

    public NetworkNode() {
        //...
        counter++;
    }

    public static int getNumOfInstances() {
        return counter;
    }

    public int getID() {  return ID;   }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int XCoord) {
        this.xCoord = XCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public String getZoneBand() {   return zoneBand;   }

    public void setZoneBand(String zoneBand) {  this.zoneBand = zoneBand;   }


    public String toString() {
        return "Network Node [ID=" + ID + " ,Zone Band=" + zoneBand + ", xCoord=" + xCoord + ", yCoord=" + yCoord + "]";

    }
}
