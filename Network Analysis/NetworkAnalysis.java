/*Main driver program for the Network Analysis program
*
* Created by Rob Schwartz on 10/31/2019
*
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class NetworkAnalysis {

    public static void main(String[] args) throws FileNotFoundException {

        //intro
        System.out.println("Welcome to the Network Analysis program!");

        String fileName = args[0];
        //read in the input data
        File file = new File(fileName);

        //new scanner object
        Scanner scanner = new Scanner(file);

        //read in the first line which is the number of vertices
        int numVertices = scanner.nextInt();
        scanner.nextLine();

        //adjacency list for each vertex, at each index, a list of edges will follow
        LinkedList<Edge>[] edgeList = new LinkedList[numVertices];

        //read in the input file and create the adjacency list
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] edge = line.split(" ");

            if (edgeList[Integer.parseInt(edge[0])] == null) {
                edgeList[Integer.parseInt(edge[0])] = new LinkedList<Edge>();
            }

            if (edgeList[Integer.parseInt(edge[1])] == null) {
                edgeList[Integer.parseInt(edge[1])] = new LinkedList<Edge>();
            }

            //add the edge to the first vertex's LL
            edgeList[Integer.parseInt(edge[0])].add(new Edge(edge[2], Integer.parseInt(edge[3])
                    , Integer.parseInt(edge[4]), Integer.parseInt(edge[1]), Integer.parseInt(edge[0])));
            //add the edge to the second vertex's LL
            edgeList[Integer.parseInt(edge[1])].add(new Edge(edge[2], Integer.parseInt(edge[3])
                    , Integer.parseInt(edge[4]), Integer.parseInt(edge[0]), Integer.parseInt(edge[1])));
        }

        while (true) {
            System.out.println("Please choose an option:\n(1) Find the Lowest Latency Path Between 2 Nodes\n" +
                    "(2) Is the graph copper only connected?\n" +
                    "(3) Find the lowest average latency spanning tree for the graph\n" +
                    "(4) Determine whether or not the graph would remain connected if any two vertices in the graph were to fail\n" +
                    "(5) Quit the program\n");

            Scanner userIn = new Scanner(System.in);
            int userChoice = userIn.nextInt();

            switch (userChoice){
                case 1:
                    //Find lowest latency
                    lowestLatency(edgeList, numVertices);
                    break;
                case 2:
                    //Copper only connected
                    copperOnly(edgeList, numVertices);
                    break;
                case 3:
                    //Find the lowest average latency spanning tree
                    lowestAverageLatency(edgeList, numVertices);
                    break;
                case 4:
                    //Determine whether or not the it is bi connected
                    biConnected(edgeList, numVertices);
                    break;
                case 5:
                    //quit the program
                    System.out.println("Thank you for using the Network Analysis program! Quitting...");
                    System.exit(0);
                    break;
                default:
                    //invalid entry
                    System.out.println("Please Enter a valid response!");
            }
        }
    }

    //tell the user if any two edges were to fail, if the graph would still be connected
    private static void biConnected(LinkedList<Edge>[] edgeList, int numVertices) {
        //initial check if there are at least 3 edges on each vertex, otherwise it will fail, return
        /*for (int i = 0; i < edgeList.length; i++){
            if (edgeList[i].size() <= 2){
                System.out.println("The graph would not be connected if any 2 vertices were removed!\n");
                return;
            }
        }*/

        //initialize T to contain the starting vertex
        //start at vertex 0
        Edge[] spanningList = new Edge[numVertices];
        LinkedList<Edge> currentVertex = edgeList[0];

        int totalNumEdges = 0;
        //get number of total edges
        for (int i = 0; i< numVertices; i ++){
            totalNumEdges += edgeList[i].size();
        }

        //array to see what verticies we have visited
        boolean[] visitedVertex = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++){
            visitedVertex[i] = false;
        }

        //what edge list are we on for a specific vertex
        int edgeListEdge1 = 0, edgeListEdge2 = 0;
        //what edge  are we on for a specific vertex
        int edgeEdge1 = 0, edgeEdge2 = 1;

        Edge edgeToDisable1;
        Edge edgeToDisable2;

        for (int edge1 = 0; edge1 < totalNumEdges - 1; edge1++) {
            for (int edge2 = 1 + edge1; edge2 < totalNumEdges; edge2++) {
                //set the edges to disable
                if(edgeEdge1 >= edgeList[edgeListEdge1].size()){
                    //go to the next edgeList
                    edgeListEdge1++;
                    edgeListEdge2 = edgeListEdge1;
                    //start at the first and second edge in the edgeList
                    edgeEdge2 = 1;
                    edgeEdge1 = 0;
                }

                if(edgeEdge2 >= edgeList[edgeListEdge2].size()){
                    //go to the next edgeList
                    edgeListEdge2++;
                    //start at the first edge in the new edge list
                    edgeEdge2 = 0;
                }

                edgeToDisable1 = edgeList[edgeListEdge1].get(edgeEdge1);
                edgeToDisable1.setCanUse(false);
                edgeToDisable2 = edgeList[edgeListEdge2].get(edgeEdge2);
                edgeToDisable2.setCanUse(false);

                //set the visited vertex array every time
                for (int i = 0; i < numVertices; i++){
                    visitedVertex[i] = false;
                }
                visitedVertex[edgeListEdge1] = true;

                //set vertices visited everytime
                int verticiesVisited = 1;

                //now try to make a spanning tree without using these nodes
                while (verticiesVisited < numVertices) {
                    //we need to look for an edge in T that we haven't visited and can be added
                    //loop through the vertices
                    boolean foundAVertexToAdd = false;
                    for (int vertex = 0; vertex < numVertices && !foundAVertexToAdd; vertex++) {
                        currentVertex = edgeList[vertex];
                        //Have we visited the vertex? If we have, we can look for edges there
                        if (visitedVertex[vertex]) {
                            //if we have visited this vertex, enumerate its neighbors while we havent found a vertex to add
                            for (int neighbor = 0; neighbor < currentVertex.size() && !foundAVertexToAdd; neighbor++) {
                                //can we use this edge and if we haven't been to this neighbor before, add the edge to the spanning tree
                                if (currentVertex.get(neighbor).isCanUse() && !visitedVertex[currentVertex.get(neighbor).getConnectedVertex()]) {
                                    //System.out.println("Added Vertex " + currentVertex.get(neighbor).getFromVertex() + currentVertex.get(neighbor).getConnectedVertex());
                                    //we have found a vertex to add
                                    foundAVertexToAdd = true;
                                    //we have now visited the vertex connected
                                    visitedVertex[currentVertex.get(neighbor).getConnectedVertex()] = true;
                                    //add the edge to the spanning list
                                    spanningList[verticiesVisited - 1] = currentVertex.get(neighbor);
                                    //increase the number of vertices
                                    verticiesVisited++;
                                }
                            }
                        }
                    }
                    //if we cannot connect a spanning tree, exit
                    if (!foundAVertexToAdd) {
                        System.out.println("\nCould not create a connected network graph if the following 2 edges were to fail:\n");
                        System.out.println("Edge 1: (" + edgeList[edgeListEdge1].get(edgeEdge1).getFromVertex() + ","
                                + edgeList[edgeListEdge1].get(edgeEdge1).getConnectedVertex() + ")");
                        System.out.println("Edge 2: (" + edgeList[edgeListEdge2].get(edgeEdge2).getFromVertex() + ","
                                + edgeList[edgeListEdge2].get(edgeEdge2).getConnectedVertex() + ")\n");
                        return;
                    }
                }
                //The edge can now be used again
                edgeToDisable2 = edgeList[edgeListEdge2].get(edgeEdge2);
                edgeToDisable2.setCanUse(true);
                //increase the second edge
                edgeEdge2++;
            }
            //the edges can be used again
            edgeToDisable1 = edgeList[edgeListEdge1].get(edgeEdge1);
            edgeToDisable1.setCanUse(true);
            edgeToDisable2 = edgeList[edgeListEdge2].get(edgeEdge2 - 1);
            edgeToDisable2.setCanUse(true);

            //increase the first edge
            edgeEdge1++;
            //set the second edge to start right after the first
            //it will go to the next edgeList if needed
            edgeEdge2 = edgeEdge1 + 1;

            //set the edgelist we want edge 2 to look at now
            if (edge1 == edgeList[edgeListEdge1].size() - 1){
                //set edgeList for vertex 2 to the next edgeList
                edgeListEdge2 = edgeListEdge1 + 1;
                edgeEdge2 = 0;
            }
            //otherwise we want to set the edgelist on 2 to the same one as edgelist 1
            else{
                edgeListEdge2 = edgeListEdge1;
            }
        }

        System.out.println("\nAny two connections in the graph can fail and the graph will still be connected!\n");
        return;

        }

    //Tell the user if a copper only connected graph can be made without making more than 1 graph
    private static void copperOnly(LinkedList<Edge>[] edgeList, int numVertices) {
        //initialize T to contain the starting vertex
        //start at vertex 0
        Edge[] lowList = new Edge[numVertices];
        LinkedList<Edge> currentVertex = edgeList[0];

        //array to see what vertices we have visited
        boolean[] vistedVertex = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++){
            vistedVertex[i] = false;
        }

        //bool to track if the graph is copper connected
        boolean connected = true;

        int vertexWeAreGoingTo = 0;
        int vertexWeAreComingFrom = 0;

        //we have visited vertex 0 because we started there
        vistedVertex[0] = true;

        //var to track how many vertex's we have in our list
        int verticiesVisited = 1;

        //While there are vertices that are not in T
        while(verticiesVisited < numVertices) {
            Edge lowestEdge = null;
            double lowestLatency = 1000000;
            //Find a minimum weight edge that connects a vertex in T to a vertex not yet in T
            //loop through each vertex, if we have been there, enumerate its neighbors
            for (int vertex = 0; vertex< numVertices; vertex++){
                //System.out.println("edgelist! " + vertex);
                //update the vertex we are looking at
                currentVertex = edgeList[vertex];
                //have we visited this vertex? if we haven't can't look there
                if(vistedVertex[vertex]){
                    //if we have visited this vertex, enumerate its neighbors for the min edgeweight
                    for (int neighbor = 0; neighbor < currentVertex.size(); neighbor++){
                        //check each edgeweight and get the time to travel if we haven't seen that vertex yet
                        //ALSO WANT TO CHECK if it is a copper wire, if it isn't we don't want to check it
                        if (!vistedVertex[currentVertex.get(neighbor).getConnectedVertex()]
                                && currentVertex.get(neighbor).getTypeOfCable().equals("copper")) {
                            //System.out.println("Looking at "+ currentVertex.get(neighbor).getConnectedVertex());
                            //if we haven't been to the neighbor yet check if the latency is low enough
                            double tempLatency = timeToTravel(currentVertex.get(neighbor));
                            //if it is less than the previous edge weight latency then update the edge
                            if (tempLatency < lowestLatency) {
                                //update the lowest latency to the new lowest
                                lowestLatency = tempLatency;
                                //update the lowest edge to the lowest edgeweight
                                lowestEdge = currentVertex.get(neighbor);
                                vertexWeAreComingFrom = vertex;
                                vertexWeAreGoingTo = currentVertex.get(neighbor).getConnectedVertex();
                            }
                        }
                    }
                }
            }
            //Add the edge and the vertex to T
            if (lowestEdge == null){
                connected = false;
                System.out.println("\nThe graph is not Copper Connected.\n");
                return;
            }
            else {
                vistedVertex[vertexWeAreGoingTo] = true;
                lowList[verticiesVisited - 1] = (lowestEdge);

                //increase the number of verticies
                verticiesVisited++;
            }
        }
        if(connected) {
            System.out.println("\nThe graph is Copper Connected!");
        }
        else{
            System.out.println("\nThe graph is not Copper Connected.");
        }
        System.out.println("");

    }

    //calculate the minumum spanning tree for the given graph based on time to travel
    private static void lowestAverageLatency(LinkedList<Edge>[] edgeList, int numVertices) {
        System.out.println("\nThe lowest average latency spanning tree contains the following edges:");
        //initialize T to contain the starting vertex
        //start at vertex 0
        Edge[] lowList = new Edge[numVertices];
        LinkedList<Edge> currentVertex = edgeList[0];

        //array to see what verticies we have visited
        boolean[] vistedVertex = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++){
            vistedVertex[i] = false;
        }

        //tracking the average latency
        double averageLatency = 0.0;

        int vertexWeAreGoingTo = 0;
        int vertexWeAreComingFrom = 0;

        //we have visited vertex 0 because we started there
        vistedVertex[0] = true;

        //var to track how many vertex's we have in our list
        int verticiesVisited = 1;

        //While there are vertices that are not in T
        while(verticiesVisited < numVertices) {
            Edge lowestEdge = currentVertex.get(0);
            double lowestLatency = 1000000;
            //Find a minimum weight edge that connects a vertex in T to a vertex not yet in T
            //loop through each vertex, if we have been there, enumerate its neighbors
            for (int vertex = 0; vertex< numVertices; vertex++){
                //System.out.println("edgelist! " + vertex);
                //update the vertex we are looking at
                currentVertex = edgeList[vertex];
                //have we visited this vertex? if we haven't can't look there
                if(vistedVertex[vertex]){
                    //if we have visited this vertex, enumerate its neighbors for the min edgeweight
                    for (int neighbor = 0; neighbor < currentVertex.size(); neighbor++){
                        //check each edgeweight and get the time to travel if we haven't seen that vertex yet
                        if (!vistedVertex[currentVertex.get(neighbor).getConnectedVertex()]) {
                            //System.out.println("Looking at "+ currentVertex.get(neighbor).getConnectedVertex());
                            //if we haven't been to the neighbor yet check if the latency is low enough
                            double tempLatency = timeToTravel(currentVertex.get(neighbor));
                            //if it is less than the previous edge weight latency then update the edge
                            if (tempLatency < lowestLatency) {
                                //update the lowest latency to the new lowest
                                lowestLatency = tempLatency;
                                //update the lowest edge to the lowest edgeweight
                                lowestEdge = currentVertex.get(neighbor);
                                vertexWeAreComingFrom = vertex;
                                vertexWeAreGoingTo = currentVertex.get(neighbor).getConnectedVertex();
                            }
                        }
                    }
                }
            }
            //Add the edge and the vertex to T
            vistedVertex[vertexWeAreGoingTo] = true;
            lowList[verticiesVisited - 1] = (lowestEdge);
            System.out.println("Edge (" + vertexWeAreComingFrom + "," + vertexWeAreGoingTo + ")");
            averageLatency += timeToTravel(lowestEdge);

            //increase the number of verticies
            verticiesVisited++;
        }
        System.out.println("\nThe average Latency is: " + (averageLatency/ ((double) verticiesVisited-1)));
        System.out.println("");

    }

    //calculate the lowest latency between two nodes in the graph
    public static void lowestLatency(LinkedList<Edge>[] edgeList, int numVertices){
        //get the 2 vertexes from the user
        System.out.println("\nEnter the first Vertex:");
        Scanner scanner = new Scanner(System.in);
        int vertex1 = scanner.nextInt();
        System.out.println("Enter the second Vertex");
        int vertex2 = scanner.nextInt();

        //set up min bandwidth variable to track the minimum bandwidth wire
        int minBandwidth = 10000000;

        //set up the minPQ
        IndexMinPQ<Edge> minPQ = new IndexMinPQ(numVertices * (numVertices - 1));

        //the array that will contain the min distance for each vertex
        double[] minDistance = new double[numVertices];
        //the lowest bandwidth to each vertex
        int[] lowestBandwidth = new int[numVertices];
        //Where the edge is coming from for the shortest path
        int[] via = new int [numVertices];
        //set each index to a large time to travel
        for (int i = 0; i < numVertices; i++){
            minDistance[i] = -1.0;
            lowestBandwidth[i] = 100000000;
            via[i] = -1;
        }

        minDistance[vertex1] = 0;

        //have we been to a given vertex
        boolean[] beenToVertex = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++){
            //init all to false, haven't been to any yet
            beenToVertex[i] = false;
        }

        //start at the current vertex's linked list
        LinkedList<Edge> currentVertex = edgeList[vertex1];
        int currentVertextInt = vertex1;

        //mark that we have been to this vertex
        beenToVertex[vertex1] = true;

        //keep track at how many items are in the PQ
        int itemsInPQ = 0;

        //track how many vertices we have visited
        int verticesVisited = 0;

        //bool for first time entry to loop
        boolean firstTime = true;

        while (verticesVisited < numVertices && (!minPQ.isEmpty() || firstTime)) {
            //we have entered for the first time
            firstTime = false;

            //iterate through the neighbors, adding each one to the PQ
            for (int n = 0; n < currentVertex.size(); n++) {
                //add the nth neighbor to the minPQ
                minPQ.insert(itemsInPQ, currentVertex.get(n));
                itemsInPQ++;

                //the time to travel from the current node to another vertex
                double timeToTravel = timeToTravel(currentVertex.get(n));
                //check if it needs to be updated as the shortest time to travel to that node
                //also update it if the mindistance to a given node hasn't been used
                if (timeToTravel + minDistance[currentVertextInt] < minDistance[currentVertex.get(n).getConnectedVertex()]
                        || minDistance[currentVertex.get(n).getConnectedVertex()] == -1.0
                        && !beenToVertex[currentVertex.get(n).getConnectedVertex()]) {
                    //it needs to be updated
                    //if nothing is there yet
                    if (minDistance[currentVertex.get(n).getConnectedVertex()] == -1.0) {
                        minDistance[currentVertex.get(n).getConnectedVertex()] = timeToTravel + minDistance[currentVertextInt];
                        //need to update via
                        via[currentVertex.get(n).getConnectedVertex()] = currentVertex.get(n).getFromVertex();
                    }
                    else{
                        minDistance[currentVertex.get(n).getConnectedVertex()] = timeToTravel + minDistance[currentVertextInt];
                        via[currentVertex.get(n).getConnectedVertex()] = currentVertex.get(n).getFromVertex();
                    }
                }
            }

            //Now take the minimum time to travel edge to the next vertex that we haven't visited yet
            currentVertex = edgeList[minPQ.minKey().getConnectedVertex()];
            currentVertextInt = minPQ.minKey().getConnectedVertex();
            //have we been to this vertex already?
            //If we have, need to go to a different one
            int counter = 1;
            while (beenToVertex[minPQ.minKey().getConnectedVertex()] && !minPQ.isEmpty()) {
                //delete the min value, and get the next min value
                minPQ.delMin();
                //itemsInPQ--;
                //have we been to all of the vertices already? Complete!
                if (counter == numVertices - 1){
                    System.out.println("\nLowest latency between Node "+ vertex1 + " and Node " + vertex2 + " is "+ minDistance[vertex2]);
                    calcMinBandwidth(edgeList, via, vertex1, vertex2, numVertices);
                    return;
                }
                try {
                    currentVertex = edgeList[minPQ.minKey().getConnectedVertex()];
                    currentVertextInt = minPQ.minKey().getConnectedVertex();
                    counter++;
                }
                catch (ArrayIndexOutOfBoundsException E){
                    System.out.println("\nLowest latency between Node "+ vertex1 + " and Node " + vertex2 + " is "+ minDistance[vertex2]);
                    calcMinBandwidth(edgeList, via, vertex1, vertex2, numVertices);
                    return;
                }
            }
            //once we are at another vertex, mark it is visited
            beenToVertex[minPQ.minKey().getConnectedVertex()] = true;

            //increase the number of vertices we have visited
            verticesVisited++;
        }

        //The algorithm is complete, we have the min distance from vertex 1 to any other vertex in the graph
        //print out the min distance
        System.out.println("\nLowest latency between Node "+ vertex1 + " and Node " + vertex2 + " is "+ minDistance[vertex2]);

        //need to calculate the lowest bandwidth along the path... backtrack along via!
        //get to the correct vertex in the adj list to start backtracking
        calcMinBandwidth(edgeList, via, vertex1, vertex2, numVertices);


    }

    //Calculate the min bandwidth based on the VIA data structure and print the edges used for the min path
    private static void calcMinBandwidth(LinkedList<Edge>[] edgeList, int[] via, int vertex1, int vertex2, int numVerticies) {

        //min bandwidth available on the path
        int minBandwidth = 0;

        //get to the starting LL for vertex 2
        LinkedList<Edge> startingEdgeMin = edgeList[vertex2];

        //The end node starting point
        int currentNode = vertex2;

        //store the data
        String[] edgesToPrint = new String[numVerticies * (numVerticies - 1)];
        int numString = 0;

        //loop through LL until we have the edge from vertex 2 to its via node, start at 0
        Edge currentEdge;
        for (int i = 0; i<startingEdgeMin.size(); i++){
            //if we are at the edge that vertex 2's path is connected to in via
            currentEdge = startingEdgeMin.get(i);
            if (currentEdge.getConnectedVertex() == via[vertex2]){
                //get the bandwidth
                minBandwidth = currentEdge.getBandWidth();
                break;
            }
        }

        edgesToPrint[numString] = "(" + via[vertex2] + "," + vertex2 + ")";
        numString++;

        //we have only checked the first edge in the back path
        //while the current nodes previous node isnt equal to the start, loop through the via matrix
        while (via[currentNode] != vertex1){
            //while we have not backtracked back to vertex 1
            //update the currentNode to the
            currentNode = via[currentNode];
            //update the LL to look at
            edgesToPrint[numString] = "(" + via[currentNode] + "," + currentNode + ")";
            numString++;
            startingEdgeMin = edgeList[currentNode];
            for (int i = 0; i<startingEdgeMin.size(); i++){
                //if we are at the edge that vertex 2's path is connected to in via
                currentEdge = startingEdgeMin.get(i);
                if (currentEdge.getConnectedVertex() == via[vertex2]){
                    //get the bandwidth and compare, update if we need to!
                    if (currentEdge.getBandWidth() < minBandwidth) {
                        minBandwidth = currentEdge.getBandWidth();
                    }
                    break;
                }
            }

        }
        System.out.println("\nEdges used:");
        for (int j = numString -1; j >=0; j--){
            System.out.println(edgesToPrint[j]);
        }

        System.out.println("\nThe Bandwidth Available is: " + minBandwidth + "\n");
    }

    //time to travel each edge is length of the cable/230000000 -> copper
    //                            length of the cable/200000000 -> optic
    private static double timeToTravel(Edge edge) {
        double timeToTravel = 0.0;
        //calculate the time to travel if it is an optical cable
        if (edge.getTypeOfCable().equals("optical")){
            timeToTravel = (double) edge.getCableLength()/200000000;
        }
        //otherwise it is a copper cable
        else{
            timeToTravel = (double) edge.getCableLength()/230000000;
        }

        return timeToTravel;
    }
}
