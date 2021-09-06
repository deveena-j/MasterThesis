import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class L2_Switch extends NetworkNode
{

    public L2_Switch(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public L2_Switch() {}

    public static double distance(AccessPoint p, L2_Switch centroid) {
        return Math.sqrt(Math.pow((centroid.getyCoord() - p.getyCoord()), 2) + Math.pow((centroid.getxCoord() - p.getxCoord()), 2));
    }
    public static double distance(L2_Switch last_centroid, L2_Switch centroid) {
        return Math.sqrt(Math.pow((centroid.getyCoord() - last_centroid.getyCoord()), 2) + Math.pow((centroid.getxCoord() - last_centroid.getxCoord()), 2));
    }

    public static L2_Switch createRandomPoint(int min_x, int max_x, int min_y, int max_y) {
        Random r = new Random();
        int x = (int) (min_x + (max_x - min_x) * r.nextDouble());
        int y = (int) (min_y + (max_y - min_y) * r.nextDouble());
        return new L2_Switch(x,y);
    }




}
