//https://www.dataonfocus.com/k-means-clustering-java-code/

import java.util.ArrayList;
import java.util.List;

public class KMeans {

    //Number of Clusters. This metric should be related to the number of points
    private int NUM_CLUSTERS = 70;
    //Number of Points
    private int NUM_POINTS = 1999;

    public ArrayList<AccessPoint> points;
    public ArrayList<Cluster> clusters;
    public Center_Cluster levelTwoCluster;

    public KMeans(ArrayList<AccessPoint> points) {
        this.points = points;
        this.clusters = new ArrayList<>();
        this.levelTwoCluster = new Center_Cluster(999);
    }

    //Initializes the process
    public void init() {

        /*Create Points
        points = Point.createRandomPoints(MIN_COORDINATE,MAX_COORDINATE,NUM_POINTS);
         */

        //Create Clusters
        //Set Random Centroids
        for (int i = 0; i< NUM_CLUSTERS; i++) {
            Cluster cluster = new Cluster(i);
            //Check min and max values in coordinate_convertor_file

            L2_Switch centroid=L2_Switch.createRandomPoint(42958,56459,61634,84845);

            cluster.setCentroid(centroid);
            clusters.add(cluster);
        }

        //Print Initial state
        //plotClusters();
    }

    private void plotClusters() {
        for (int i = 0; i<NUM_CLUSTERS; i++) {
            Cluster c = clusters.get(i);
            //c.plotCluster();
        }
    }

    //The process to calculate the K Means, with iterating method.
    public void calculate() {
        boolean finish = false;
        int iteration = 0;
        double distance = 0;
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(!finish) {
            //Clear cluster state
            clearClusters();

            List<L2_Switch> lastCentroids = getCentroids();

            //Assign points to the closer cluster
            assignCluster();

            //Calculate new centroids.
            calculateCentroids();

            iteration++;

            List<L2_Switch> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            distance=0;
            for(int i = 0; i < lastCentroids.size(); i++) {
                distance += L2_Switch.distance(lastCentroids.get(i),currentCentroids.get(i));
            }
            //Calculates and tags all cluster points to the Level two center point to follow the typical 2 layer star hierarchy connections
            calculateLevelTwoCentroid();
            /*
            System.out.println("#################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distances: " + distance);
            plotClusters();
              */
            if(distance == 0) {
                finish = true;
            }

        }

        //System.out.println("#################");
        //System.out.println("Iteration: " + iteration);
        //System.out.println("Centroid distances: " + distance);


        assignServer();
        //plotClusters();
        //System.out.println("--------------------------------------------");
        //levelTwoCluster.plotCluster();

    }

    public void assignServer(){
        for (Cluster c: clusters ) {
            c.clusterServer= new MECServer(c.centroid.getxCoord(),c.centroid.getyCoord(),c.centroid.getID());
            //c.clusterServer.memSize=200;
        }
    }


    public void calculateLevelTwoCentroid(){

        List<L2_Switch> currentCentroids = getCentroids();
        levelTwoCluster.levelOneCluster=clusters;
        levelTwoCluster.centroid.setID(999);

        for(Cluster cluster : clusters) {
            double sumX = 0;
            double sumY = 0;
            cluster.parent_cluster_ID=999;

            int n_points = currentCentroids.size();

            for(L2_Switch Centroids : currentCentroids) {
                sumX += Centroids.getxCoord();
                sumY += Centroids.getyCoord();

            }

                if(n_points > 0) {
                double newX = sumX / n_points;
                double newY = sumY / n_points;
                levelTwoCluster.centroid.setxCoord((int)newX);
                levelTwoCluster.centroid.setyCoord((int) newY);
            }
        }
    }


    private void clearClusters() {
        for(Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private List<L2_Switch> getCentroids() {
        List<L2_Switch> centroids = new ArrayList<L2_Switch>(NUM_CLUSTERS);
        for(Cluster cluster : clusters) {
            L2_Switch aux = cluster.getCentroid();
            L2_Switch point = new L2_Switch(aux.getxCoord(),aux.getyCoord());
            centroids.add(point);
        }
        return centroids;
    }

    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max;
        int cluster = 0;
        double distance = 0.0;

        for(AccessPoint point : points) {
            min = max;
            for(int i = 0; i < NUM_CLUSTERS; i++) {
                Cluster c = clusters.get(i);
                distance = L2_Switch.distance(point, c.getCentroid());
                if(distance < min){
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster_ID(cluster);
            clusters.get(cluster).addPoint(point);
        }
    }

    private void calculateCentroids() {
        for(Cluster cluster : clusters) {
            double sumX = 0;
            double sumY = 0;
            List<AccessPoint> list = cluster.getPoints();
            int n_points = list.size();

            for(AccessPoint point : list) {
                sumX += point.getxCoord();
                sumY += point.getyCoord();
            }

            L2_Switch centroid = cluster.getCentroid();
            if(n_points > 0) {
                double newX = sumX / n_points;
                double newY = sumY / n_points;
                centroid.setxCoord((int)newX);
                centroid.setyCoord((int) newY);
                centroid.setID(cluster.id);
            }
        }
    }
}
