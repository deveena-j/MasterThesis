import java.io.*;
import java.util.*;


//private ServerAssignm..... obj;  https://www.javatpoint.com/strategy-pattern

//This class defines every client and its associated, access points, clusters and servers to which it connects during its movement
public class ClientTrack implements ServerAssignmentStrategyInterface{

    public static ArrayList<Integer> unservicedClientID=new ArrayList<Integer>();

    //uniqueServerIDs
    public static  ArrayList<MECServer> UsedServerID=new ArrayList<MECServer>();
    public static ArrayList<Integer> totalMigrationPerClient = new ArrayList<Integer>();
    public  ArrayList<MECServer> connectedMECServers= new ArrayList<MECServer>();
    ArrayList<Client> movingClient;
    ArrayList<Cluster> clusters;

    public ClientTrack(ArrayList<Client> clientCSV, ArrayList<Cluster> clusters){
        movingClient=clientCSV;
        this.clusters=clusters;
        //connectedMECServers=new ArrayList<MECServer>();
    }

    public void calculate(){

        assign_AP_Server();

        //displays client coordinates, connected Access Points and associated Server for all timestamps
        //displayConnectionStats();

        //Calculate unique server to avoid redundant calculations
        calcUniServers();
    }




    public void assign_AP_Server() {

        int migration=0;
        boolean success=true;
        for (Client movingclient : movingClient) {

            double max = Double.MAX_VALUE;
            double min = max;
            double distance = 0.0;
            AccessPoint AP = null;
            //Cluster closest_cluster=null;
            MECServer closest_server = null;

            for (Cluster cluster : clusters) {
                for (int i = 0; i < cluster.accessPointsCluster.size(); i++) {

                    distance = Client.distance(movingclient, cluster.accessPointsCluster.get(i));
                    if (distance < min) {

                        min = distance;
                        AP = cluster.accessPointsCluster.get(i);
                        //closest_cluster=cluster;
                        closest_server=cluster.clusterServer;
                    }
                }
                //checks if server has capacity for reservation, if yes allocates the calls for the second otherwise server points to null for the timestamp
            }
            if(closest_server.checkReservation(movingclient.ID,movingclient.req_calls,movingclient.timestamp))
            {
                if(connectedMECServers.size()>0)
                {
                    if(connectedMECServers.get(connectedMECServers.size()-1).ID!=closest_server.ID)
                        migration++;
                }
                connectedMECServers.add(closest_server);
                //connectedAccessPoint.add(AP);
            }
            else
            {
                calcUniServers();
                success=false;
                unservicedClientID.add(movingclient.ID);
                //when the closest server is not available for reservation, instead of adding null it would clear all reservation and reject the client as Unserviced

                for (MECServer MEC : ClientTrack.UsedServerID) {
                     MEC.clearAllReservation(movingclient);
                }
                System.out.println("Reservation cleared for client: " + movingclient.ID + " from all previously connected Servers");
                break;
            }
        }
        if(success)
            totalMigrationPerClient.add(migration);
        calcUniServers();
        //System.out.println("Total Number of Migrations for this client: " +migration);
    }

    //Adds all servers in the usedServer list to use them later for calculating efficiency
    public void calcUniServers(){

        for (MECServer Serv: connectedMECServers) {
            boolean present=false;

            for (int i=0;i<UsedServerID.size();i++)
            {
                if(UsedServerID.get(i).ID==Serv.ID) {
                    present = true;
                    break;
                }
            }

            if (present==false)
            {
                //System.out.println("\n Server ID: " + Serv.ID );
                UsedServerID.add(Serv);
                /*
                for(int i =0;i<Serv.calls_snapshot.length;i++)
                {
                    for (int j = 0; j < Serv.calls_snapshot[i].size(); j++);
                    //System.out.print(Serv.calls_snapshot[i].get(j) + " ");

                }
               */
            }
        }
    }





}
