
public class FloydWarshall
{
    public int distancematrix[][];
    private int numberofvertices;
    public static final int INFINITY = 999;

    public FloydWarshall(int numberofvertices)
    {

        distancematrix = new int[numberofvertices][numberofvertices];
        this.numberofvertices = numberofvertices;
    }

    public void floydwarshall(int adjacencymatrix[][])
    {
        //for loop to convert 0 values to infinity for non connecting vertices
        for (int source = 0; source < numberofvertices; source++)
        {
            for (int destination = 0; destination < numberofvertices; destination++)
            {
                if (source == destination)
                {
                    adjacencymatrix[source][destination] = 0;
                    continue;
                }
                if (adjacencymatrix[source][destination] == 0)
                {
                    adjacencymatrix[source][destination] = INFINITY;
                }
            }
        }


        for (int source = 0; source < numberofvertices; source++)
        {
            for (int destination = 0; destination < numberofvertices; destination++)
            {
                distancematrix[source][destination] = adjacencymatrix[source][destination];
            }
        }

        for (int intermediate = 0; intermediate < numberofvertices; intermediate++)
        {
            for (int source = 0; source < numberofvertices; source++)
            {
                for (int destination = 0; destination < numberofvertices; destination++)
                {
                    if (distancematrix[source][intermediate] + distancematrix[intermediate][destination]
                            < distancematrix[source][destination])
                        distancematrix[source][destination] = distancematrix[source][intermediate]
                                + distancematrix[intermediate][destination];
                }
            }
        }

        //Prints distance matrix
        System.out.print("Derived Distance Matrix" + "\n");
        for (int source = 0; source < numberofvertices; source++)
            System.out.print("\t" + source);

        System.out.println();
        for (int source = 0; source < numberofvertices; source++)
        {
            System.out.print(source + "\t");
            for (int destination = 0; destination < numberofvertices; destination++)
            {
                System.out.print(distancematrix[source][destination] + "\t");
            }
            System.out.println();
        }
    }

    /*public static void main(String... arg)
    {
        int adjacency_matrix[][];
        int numberofvertices;

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the number of vertices");
        numberofvertices = scan.nextInt();

        adjacency_matrix = new int[numberofvertices + 1][numberofvertices + 1];
        System.out.println("Enter the Weighted Matrix for the graph");
        for (int source = 1; source <= numberofvertices; source++)
        {
            for (int destination = 1; destination <= numberofvertices; destination++)
            {
                adjacency_matrix[source][destination] = scan.nextInt();
                if (source == destination)
                {
                    adjacency_matrix[source][destination] = 0;
                    continue;
                }
                if (adjacency_matrix[source][destination] == 0)
                {
                    adjacency_matrix[source][destination] = INFINITY;
                }
            }
        }

        System.out.println("The Transitive Closure of the Graph");
        FloydWarshall floydwarshall = new FloydWarshall(numberofvertices);
        floydwarshall.floydwarshall(adjacency_matrix);
    }*/
}