import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
//03.06.21
public class ReadLatLongCSV_BufferedReader
{
    //Delimiters used in the CSV file
    private String COMMA_DELIMITER = ",";
    private String SPACE_DELIMITER=" ";
    public List<AccessPoint> AccessPointList;
    String csvFile;
    private static int counter_obj=-1;

    public ReadLatLongCSV_BufferedReader(String csvFile){
        this.csvFile=csvFile;
        counter_obj++;
    }

    public ArrayList<AccessPoint> parsingAP_CSV(ArrayList AccessPointList)
    {
        //String csvFile = "/Users/DEDJAIN1/Desktop/Personal/Master_Thesis/Thesis_Writing/Converted_Lat_Longs.csv";
        BufferedReader br = null;
        try
        {
            //Reading the csv file
            br = new BufferedReader(new FileReader(csvFile));

            //Create List for holding AccessPoint objects
            AccessPointList = new ArrayList<AccessPoint>();

            String line = "";
            //Read to skip the header
            br.readLine();
            //Reading from the second line
            while ((line = br.readLine()) != null)
            {
                String[] AccessPointDetails = line.split(COMMA_DELIMITER);

                if(AccessPointDetails.length > 0 )
                {
                    //Save the employee details in Employee object
                    AccessPoint AP = new AccessPoint(Integer.parseInt(AccessPointDetails[0]),
                            AccessPointDetails[1],Integer.parseInt(AccessPointDetails[2])%100000,
                            Integer.parseInt(AccessPointDetails[3])%100000);
                    AccessPointList.add(AP);
                }
            }


                /*Lets print the Access Point List
                for(AccessPoint AP : AccessPointList)
                    System.out.println(AP.getID() + "   " + AP.getZoneBand() + "   "

                            + AP.getxCoord() + "   " + AP.getyCoord());

                 */


        }

        catch(Exception ee)
        {
            ee.printStackTrace();
        }
        finally
        {
            try
            {
                br.close();
            }
            catch(IOException ie)
            {
                System.out.println("Error occured while closing the BufferedReader");
                ie.printStackTrace();
            }
        }
        return AccessPointList;
    }


    public ArrayList<Client> parsing_Client_CSV()
    {
        //String csvFile = "/Users/DEDJAIN1/Desktop/Personal/Master_Thesis/Thesis_Writing/Converted_Lat_Longs.csv";
        int ID=0;
        BufferedReader br = null;
        ArrayList<Client> clientCSV = null;
        try
        {
            //Reading the csv file
            br = new BufferedReader(new FileReader(csvFile));
            //Create List for holding AccessPoint objects

            clientCSV = new ArrayList<Client>();

            String line = "";

            //Reading from the first line as there is no header
            while ((line = br.readLine()) != null)
            {
                String[] clientDetails = line.split(SPACE_DELIMITER);

                //Selecting the 12 hour data from the two week data starting at 18.05.2008 00:00 US Pacific Time
                if(Integer.parseInt(clientDetails[3])>=1211094000 && Integer.parseInt(clientDetails[3])<1211137200){

                    if(clientDetails.length > 0)
                    {
                        //Save the client details in client object and add to client array list
                        boolean client_state=true;
                        String UTM;
                        UTM = GeoConvert.toMGRS(Double.parseDouble(clientDetails[0]),Double.parseDouble(clientDetails[1]));
                        String zoneBand= UTM.substring(0,3);
                        int xCoord = Integer.parseInt(UTM.substring(3,8));
                        int ycoord = Integer.parseInt(UTM.substring(9));
                        Integer client_stateI= Integer.parseInt(clientDetails[2]);

                        if(client_stateI==0)
                            client_state=false;

                        Client client_row = new Client(counter_obj, xCoord,ycoord,zoneBand,client_state,Integer.parseInt(clientDetails[3]));
                        clientCSV.add(client_row);
                    }
                }
            }

            if(clientCSV.size()>0)
            {
                //Since the file HAS UNIX TIMESTAMPS, this section coverts it to normal timestamp starting from 0,1,2 and so on
                //long smallest_time=clientCSV.get(clientCSV.size()-1).timestamp;
                long smallest_time=1211094000;
                for (Client client_row:clientCSV) {
                    client_row.timestamp-=smallest_time;

                }

                //This section introduces missing client coordinates with values same as the previous time stamp coordinates
                //int counter= (int) clientCSV.get(0).timestamp;
                int difference=clientCSV.get(0).timestamp - (clientCSV.get(clientCSV.size()-1).timestamp)+1;

                int pos=0;
                int i=(int) clientCSV.get(0).timestamp;;
                while(difference>0){
                    int timestamp_i=clientCSV.get(pos).timestamp;
                    if(timestamp_i!=i){
                        clientCSV.add(pos,new Client(counter_obj, clientCSV.get(pos-1).xCoord,clientCSV.get(pos-1).yCoord,clientCSV.get(pos-1).zoneBand, clientCSV.get(pos-1).client_state,i));
                    }
                    pos++;
                    i--;
                    difference--;
                }

                //Adding missing timestamps for records lesser than the initial timestamp to cover all 86400 seconds incase the start time is greater than 1211094000
                int remaining= (clientCSV.get(clientCSV.size()-1).timestamp)-1;
                //System.out.println("First Remaining: " + remaining);
                while(remaining>=0) {
                    clientCSV.add(new Client(counter_obj, clientCSV.get(clientCSV.size()-1).xCoord,clientCSV.get(clientCSV.size()-1).yCoord,clientCSV.get(clientCSV.size()-1).zoneBand, clientCSV.get(clientCSV.size()-1).client_state,remaining));
                    remaining--;

                }

                //to start from 0 timestamp upwards
                Collections.reverse(clientCSV);

            }
                /*
                for (Client clientobj: clientCSV) {
                  System.out.println(clientobj);
                }
                */
        }

        catch(Exception ee)
        {
            ee.printStackTrace();
        }
        finally
        {
            try
            {
                br.close();
            }
            catch(IOException ie)
            {
                System.out.println("Error occured while closing the BufferedReader");
                ie.printStackTrace();
            }
        }

        return clientCSV;
    }




}

