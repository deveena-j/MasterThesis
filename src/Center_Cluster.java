import java.util.*;

public class Center_Cluster extends Parent_Cluster_Class{

    List<Cluster> levelOneCluster=new ArrayList<Cluster>();

    //Creates a new Cluster
    public Center_Cluster(int id) {
        this.id = id;
        this.levelOneCluster = new ArrayList<Cluster>();
        this.centroid = new L2_Switch();
    }

    public List<Cluster> getPoints() {
        return levelOneCluster;
    }

    public void addClusterPoint(Cluster C) {
        levelOneCluster.add(C);
    }

    public void setClusterPoints(List<Cluster> Cs) {
        this.levelOneCluster = Cs;
    }

    public void clear() {
        levelOneCluster.clear();
    }

    public void plotCluster() {
        System.out.println("[Cluster: " + id+"]");
        System.out.println("[Centroid: " + centroid + "]");
        System.out.println("[Connected Cluster Points: \n");
        for(Cluster p : levelOneCluster) {
            System.out.println(p);
        }
        System.out.println("]");
    }


}
