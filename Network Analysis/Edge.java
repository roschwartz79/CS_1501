//This class implements an Edge class
//Written by Rob Schwartz
public class Edge implements Comparable<Edge> {
    //0 is optical, 1 is copper
    private String typeOfCable;
    //amount of bandwidth
    private int bandWidth;
    //length of the cable
    private int cableLength;
    //what vertex is the edge connecting to
    private int connectedVertex;
    //what vertex we are coming from
    private int fromVertex;

    //can we use this edge is the MST
    boolean canUse = true;

    public Edge(String typeOfCable, int bw, int cl, int endPoint, int startingPoint){
        this.typeOfCable = typeOfCable;
        this.bandWidth = bw;
        this.cableLength = cl;
        this.connectedVertex = endPoint;
        this.fromVertex = startingPoint;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public int getFromVertex() {
        return fromVertex;
    }

    public void setFromVertex(int fromVertex) {
        this.fromVertex = fromVertex;
    }

    public String getTypeOfCable() {
        return typeOfCable;
    }

    public void setTypeOfCable(String typeOfCable) {
        this.typeOfCable = typeOfCable;
    }

    public int getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(int bandWidth) {
        this.bandWidth = bandWidth;
    }

    public int getCableLength() {
        return cableLength;
    }

    public void setCableLength(int cableLength) {
        this.cableLength = cableLength;
    }

    public int getConnectedVertex() {
        return connectedVertex;
    }

    public void setConnectedVertex(int connectedVertex) {
        this.connectedVertex = connectedVertex;
    }

    /** a.CompareTo(b)
    * if it is positive, a is larger
    * if it is negative, b is larger
    *
    */
    @Override
    public int compareTo(Edge o) {
        double timeToTravelA = 0.0, timeToTravelB = 0.0;
        //calculate the time to travel if it is an optical cable
        if (this.getTypeOfCable().equals("optical")){
            timeToTravelA = (double) this.getCableLength()/200000000;
        }
        //otherwise it is a copper cable
        else{
            timeToTravelA = (double) this.getCableLength()/230000000;
        }

        //calculate the time to travel if it is an optical cable
        if (this.getTypeOfCable().equals("optical")){
            timeToTravelB = (double) o.getCableLength()/200000000;
        }
        //otherwise it is a copper cable
        else{
            timeToTravelB = (double) o.getCableLength()/230000000;
        }

        //if B is larger return a negative number
        if (timeToTravelA < timeToTravelB){
            return -1;
        }
        //if A is larger return a positive number
        else{
            return 1;
        }




    }
}
