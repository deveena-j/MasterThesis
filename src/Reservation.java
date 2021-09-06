public class Reservation {

    public int client_ID;
    //public float res_calls;
    public int timestamp;

    public Reservation (int client_ID, int timestamp) {
        this.client_ID=client_ID;
        //this.res_calls=res_calls;
        this.timestamp=timestamp;
    }


    public String toString(){
        return "[ Client ID: " + this.client_ID + " Timestamp: " + this.timestamp + "]";
    }

}
