import java.io.*;
import java.lang.*;
import java.util.*;
//03.06.21
public class Network {
    //public static int n=531;
    public static ArrayList<ArrayList<Client>> clientMasterData_ser = new ArrayList<>();
    //public static ArrayList<Client>[] clientMasterData_ser = new ArrayList[n];


    public static void main (String args[]) throws Exception {
        long start = System.currentTimeMillis();
        //Initial Client Configuration

        System.out.println("Client File Parsing - Initiated");
        //Parses all cab files placed in the directory location - space delimited READY
        File folder = new File("/Users/DEDJAIN1/OneDrive - EY/Desktop/Personal/Master_Thesis/Thesis_Writing/External_Tools/Cab_Conversions/CabDirectoryTest/");
        String pathname = "/Users/DEDJAIN1/OneDrive - EY/Desktop/Personal/Master_Thesis/Thesis_Writing/External_Tools/Cab_Conversions/CabDirectoryTest/";
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String file = pathname + listOfFiles[i].getName();
                long len = listOfFiles[i].length();
                //System.out.println(file);
                ReadLatLongCSV_BufferedReader latLongFile = new ReadLatLongCSV_BufferedReader(file);
                ArrayList<Client> singleClientFile = latLongFile.parsing_Client_CSV();

                //We only take files which has the time range of movement between 18.05.2008 and 18.05.2008 12:00 US Pacific
                if (singleClientFile.size() > 0)
                    clientMasterData_ser.add(singleClientFile);
            }
        }

        System.out.println("Client File Parsing - Done");
        System.out.println("1. Client Master Data Size: " + clientMasterData_ser.size());


        //Intial AP Configuration
            /*
            //ReadsandParsestheAccessPointsfile
            ReadLatLongCSV_BufferedReader APlatLongFile=new ReadLatLongCSV_BufferedReader("/Users/DEDJAIN1/Desktop/Personal/Master_Thesis/Thesis_Writing/Converted_Lat_Longs.csv");
            ArrayList<AccessPoint>dummyAccessPointList=new ArrayList<AccessPoint>();
            ArrayList<AccessPoint>AccessPointList_ser=new ArrayList<AccessPoint>();
            AccessPointList_ser=APlatLongFile.parsingAP_CSV(dummyAccessPointList);

            System.out.println("2.APFileSize:"+AccessPointList_ser.size());


            //Serialisation of the AP File
            try{
                //Creatingthebytestreamandwritingtheobjectintoafile
                FileOutputStream file=new FileOutputStream("Serialised_Access_Point.ser");
                ObjectOutputStream output=new ObjectOutputStream(file);
                output.writeObject(AccessPointList_ser);
                output.flush();

                output.close();
                file.close();
                }
            catch(Exception e)
            {
            System.out.println("Problem serialising:"+e);
            }
            System.out.println("Serialisation is done");
            */

        System.out.println("De-serialisation of AP File - Initiated");

        //De-serialisation of the AP file
        ArrayList<AccessPoint> AccessPointList = null;
        try {

            FileInputStream fis = new FileInputStream("Serialised_Access_Point.ser");
            ObjectInputStream input = new ObjectInputStream(fis);
            AccessPointList = (ArrayList<AccessPoint>) input.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("De-serialisation of AP File - Done.");

        System.out.println("1. Access Point Data Size: " + AccessPointList.size());

        System.out.println("K-Means clustering - Initiated");
        // Performs a K-Means clustering algorithm on the APs and generates cluster to depict star hierarchy in the network
        KMeans kmeans = new KMeans(AccessPointList);
        kmeans.init();
        kmeans.calculate();
        System.out.println("K-Means clustering - Done");

        System.out.println("Client Tracking Reservation Model 1: Choosing closest Infrastructure for Reservation - Initiated");

        File file = new File("ServerCallsSnapshot.csv");


        // Runs every client sequentially through the network to make reservations and conclude if SLA is held (or not)
        for (int i = 1; i <= 1; i++) {

            for (ArrayList<Client> clientCSV : clientMasterData_ser) {
                Context context = new Context(new ClientTrack_2(clientCSV, kmeans.clusters));
                context.executeStrategy();
            }
            System.out.println("Client Tracking Reservation Model 1: Choosing closest Infrastructure for Reservation - Done");

            System.out.println("Outputting Server calls Status - Initiated");
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            //Outputs server status as a csv file to compute server utilisation
            int summed_up_reservation[]=new int[43200];
            for (MECServer MEC : ClientTrack_2.UsedServerID) {

                //br.newLine();
                //br.write(String.valueOf(MEC));
                br.newLine();

                int n = MEC.reserved_calls.length;
                int summed_up_every_server=0;

                for (int k = 0; k < n; k++)
                     {
                         summed_up_every_server+=MEC.reserved_calls[k];
                         //br.write(String.valueOf(res_call_time+","));
                         summed_up_reservation[k]+=MEC.reserved_calls[k];
                     }


                //3600*12*16 total calls for 12 hours for every server
                br.write(String.valueOf(summed_up_every_server));
            }

            br.newLine();
            br.write("Total reserved calls");
            br.newLine();
            for (int k = 0; k < summed_up_reservation.length; k++)
            {
             br.write(String.valueOf(summed_up_reservation[k])+",");
             br.newLine();
            }
            br.flush();
            br.close();
        }

        
        System.out.println("Outputting Server calls Status - Done\n\n");
        System.out.println("File location: " + file.getAbsolutePath());
        System.out.println("-------------------------------Statistics------------------------------");
        System.out.println("\nUsed Servers: " + ClientTrack_2.UsedServerID.size() + " Total Servers: " + MECServer.counter + " Infrastructure usage: " + ((int) (ClientTrack_2.UsedServerID.size() * 100) / MECServer.counter) + "%");
        System.out.println("Number of clients which could not be serviced: " + ClientTrack_2.unservicedClientID.size());
        System.out.println("Successful Client Reservation: " + (int) (((clientMasterData_ser.size() - ClientTrack_2.unservicedClientID.size()) * 100) / clientMasterData_ser.size()) + "%");
        System.out.println("Total Number of Migrations for clients (one after another): " + ClientTrack_2.totalMigrationPerClient);
        
/*
        System.out.println("Outputting Server calls Status - Done\n\n");
        System.out.println("File location: " + file.getAbsolutePath());
        System.out.println("-------------------------------Statistics------------------------------");
        System.out.println("\nUsed Servers: " + ClientTrack_2.UsedServerID.size() + " Total Servers: " + MECServer.counter + " Infrastructure usage: " + ((int) (ClientTrack_2.UsedServerID.size() * 100) / MECServer.counter) + "%");
        System.out.println("Number of clients which could not be serviced: " + ClientTrack_2.unservicedClientID.size());
        System.out.println("Successful Client Reservation: " + (int) (((clientMasterData_ser.size() - ClientTrack_2.unservicedClientID.size()) * 100) / clientMasterData_ser.size()) + "%");
        System.out.println("Total Number of Migrations for clients (one after another): " + ClientTrack_2.totalMigrationPerClient);
*/
        //Floyd-Warshall - not used
            /*
            int vertices= NetworkNode.getNumOfInstances();
            FloydWarshall floydwarshall = new FloydWarshall(vertices);
            floydwarshall.floydwarshall(adjacencymatrix);
             */

        long end = System.currentTimeMillis();
        System.out.println("Execution Time: " + ((end - start) / 60000) + " minutes");
    }
}


