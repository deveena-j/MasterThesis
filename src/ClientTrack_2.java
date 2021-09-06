import java.io.*;
import java.util.*;


//private ServerAssignm..... obj;  https://www.javatpoint.com/strategy-pattern

//This class defines every client and its associated, access points, clusters and servers to which it connects during its movement
public class ClientTrack_2 implements ServerAssignmentStrategyInterface{

    public static ArrayList<Integer> unservicedClientID=new ArrayList<Integer>();

    //uniqueServerIDs
    public static  ArrayList<MECServer> UsedServerID=new ArrayList<MECServer>();
    public static ArrayList<Integer> totalMigrationPerClient = new ArrayList<Integer>();
    public  ArrayList<MECServer> connectedMECServers= new ArrayList<MECServer>();
    ArrayList<Client> movingClient;
    ArrayList<Cluster> clusters;

    //AccessPoint AP = null;
    Cluster closest_cluster=null;
    MECServer closest_server = null;
    MECServer second_server=null;


    public ClientTrack_2(ArrayList<Client> clientCSV, ArrayList<Cluster> clusters){
        movingClient=clientCSV;
        this.clusters=clusters;
        //connectedMECServers=new ArrayList<MECServer>();
    }

    public void closest_server_AP(Client movingclient)
    {
        double min = Double.MAX_VALUE;

        for (Cluster cluster : clusters) {
            double distance = 0.0;
            for (int i = 0; i < cluster.accessPointsCluster.size(); i++) {

                distance = Client.distance(movingclient, cluster.accessPointsCluster.get(i));
                if (distance < min) {

                    min = distance;
                    //AP = cluster.accessPointsCluster.get(i);
                    if(second_server!=closest_server)
                        second_server=closest_server;
                    closest_cluster=cluster;
                    closest_server=cluster.clusterServer;

                }
            }
        }
    }

    public void assign_AP_Server() {

        int migration=0;
        boolean success=true;
        int ctr=0;

        for (Client movingclient : movingClient) {

        closest_server_AP(movingclient);

          if(ctr++==0){
              if (closest_server.checkReservation(movingclient.ID,movingclient.req_calls,movingclient.timestamp))
                  connectedMECServers.add(closest_server);
              else
              if(second_server.checkReservation(movingclient.ID,movingclient.req_calls,movingclient.timestamp))
                          connectedMECServers.add(second_server);
              else
              {
                  success=false;
                  calcUniServers();
                  unservicedClientID.add(movingclient.ID);
                  //when the closest server is not available for reservation, instead of adding null it would clear all reservation and reject the client as Unserviced
                  /*
                  for (MECServer MEC : ClientTrack.UsedServerID) {
                      MEC.clearAllReservation(movingclient);}
                   */
                  System.out.println("Reservation denied: " + movingclient.ID + " because " + closest_server + " & "+ second_server + " were unavailable");
                  break;
              }
          }

          if(ctr>0) {
              //System.out.println("In the loop where ctr is greater than 0");
              if(connectedMECServers.get(connectedMECServers.size()-1).checkReservation(movingclient.ID,movingclient.req_calls,movingclient.timestamp)) {
              }
              else
                  if(closest_server.checkReservation(movingclient.ID,movingclient.req_calls,movingclient.timestamp)) {
                  if(connectedMECServers.size()>0) {
                      if(connectedMECServers.get(connectedMECServers.size()-1).ID!=closest_server.ID) {
                              migration++;
                              System.out.println("Migration step");
                              connectedMECServers.add(closest_server);
                          }
                      }
                  }
                  else
                  if(second_server.checkReservation(movingclient.ID,movingclient.req_calls,movingclient.timestamp)) {
                      if(connectedMECServers.size()>0) {
                          if(connectedMECServers.get(connectedMECServers.size()-1).ID!=second_server.ID) {
                              migration++;
                              System.out.println("Migration step");
                              connectedMECServers.add(second_server);
                          }
                      }
                  }
                  else
                      {
                          success=false;
                          calcUniServers();
                          unservicedClientID.add(movingclient.ID);
                          //when the closest server is not available for reservation, instead of adding null it would clear all reservation and reject the client as Unserviced
                          for (MECServer MEC : ClientTrack.UsedServerID) {
                              MEC.clearAllReservation(movingclient);
                          }
                          System.out.println("Reservation cleared for client: " + movingclient.ID + " from all previously connected Servers");
                          break;
                      }
                }
        }
        /*
        for (MECServer m: connectedMECServers) {
            System.out.print(m.ID+" ");
        }

         */
        if(success)
            totalMigrationPerClient.add(migration);

        calcUniServers();
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
                UsedServerID.add(Serv);
        }
    }





}
