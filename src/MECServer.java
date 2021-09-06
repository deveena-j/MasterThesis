import java.util.ArrayList;
import java.util.List;

public class MECServer extends NetworkNode {

    public static int counter;
    public int callSize;
    public int i=0;

    //Total timestamps for which server can service, in this case 24h or 86400 seconds
    private int n=43200;

    // Here calls_Snapshot is an array of arraylist having
    // n number of rows.The number of columns on
    // each row depends on the user.
    // al[i].size() will give the size of the
    // i'th row
    ArrayList<Reservation>[] calls_snapshot = new ArrayList[n];

    //Reservation calls_snapshot [ ] [ ] = new Reservation [12] [15];
    List<AccessPoint> accessPointList;

    int reserved_calls[];

    public MECServer(int xCoord, int yCoord, int ID) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.ID = ID;
        //calls Size of all servers is 64GB or 65536MB
        callSize=16;
        counter++;
        reserved_calls=new int[n];
        for (int i = 0; i < n; i++) {
            calls_snapshot[i] = new ArrayList<Reservation>();
        //row_ctr=new int[n];
    }}

    public List<AccessPoint> getAccessPoints() {
        return accessPointList;
    }

    public void setServer(List<AccessPoint> accessPointList) {
        this.accessPointList = accessPointList;
    }

    public void addReservation(int clientID, int timestamp) {

        calls_snapshot[timestamp].add(new Reservation(clientID,timestamp));
        //System.out.println("Reservation successful for client ID: "+ clientID + " for calls: " +req_calls+ " at timestamp: "+timestamp +  " on server with ID: "+ ID);
    }

    public boolean checkReservation(int clientID,int req_calls, int timestamp) {
        //System.out.println("Checking reservation for client ID: "+ clientID + " for calls: " +req_calls+ " at timestamp: "+timestamp);
        boolean reserved=false;

        //checks whether the calls required by the client is lesser than the unreserved calls at the current timestamp
        if (req_calls<=(callSize - reserved_calls[timestamp]))
        {
            //System.out.println("Reservation possible for client ID: "+ clientID + " for calls: " +req_calls+ " at timestamp: "+timestamp + " on server with ID: "+ ID);
            //int mem= reserved_calls[timestamp]+req_calls;
            reserved_calls[timestamp]+=req_calls;
            addReservation(clientID,timestamp);
            reserved=true;
        }
        return reserved;
    }

    public void clearAllReservation(Client Client)
    {
        for(int i =0;i<calls_snapshot.length;i++)
        {
            int total_i=0;
            for (int j = 0; j < calls_snapshot[i].size(); j++)
                if(calls_snapshot[i].get(j).client_ID==Client.ID)
                {
                    calls_snapshot[i].remove(j);
                    total_i++;

                }
            reserved_calls[i]-= (total_i*Client.req_calls);
        }
    }




    @Override
    public String toString() {
        return "MECServer [ ID=" + ID + ", xCoord=" + xCoord + ", yCoord=" + yCoord + ']';
    }




}

