import java.util.ArrayList;
import java.util.List;

public class Cluster extends Parent_Cluster_Class{

    List<AccessPoint> accessPointsCluster;
    public int parent_cluster_ID;
    public MECServer clusterServer;

    //Creates a new Cluster
    public Cluster(int id) {
        this.id = id;
        this.accessPointsCluster = new ArrayList<AccessPoint>();
        this.centroid = null;
    }

    public List<AccessPoint> getPoints() {
        return accessPointsCluster;
    }

    public void addPoint(AccessPoint AP) {
        accessPointsCluster.add(AP);
    }

    public void setPoints(List<AccessPoint> APs) {
        this.accessPointsCluster = APs;
    }

    public void clear() {
        accessPointsCluster.clear();
    }

    public MECServer getClusterServer() {
        return clusterServer;
    }

    public void setClusterServer(MECServer clusterServer) {
        this.clusterServer = clusterServer;
    }

    public void plotCluster() {
        System.out.println("[Cluster: " + id+"]");
        System.out.println("[Parent Cluster ID: " + parent_cluster_ID);
        System.out.println("[Centroid: " + centroid + "]");
        System.out.println("[Server Specs: " + clusterServer + "]");
        System.out.println("[Points: \n");

        for(AccessPoint p : accessPointsCluster) {
            System.out.println(p);
        }
        System.out.println("]");
    }

    public String toString() {
        return "Cluster [ID=" + id + " ,Centroid=" + centroid + " ,Server Specs" + clusterServer + "]";

    }

}