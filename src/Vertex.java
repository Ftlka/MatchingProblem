import java.util.ArrayList;

public class Vertex
{
    private int point;
    private boolean isAttached;
    private Vertex attachedVertex;
    private boolean isFirstSet;
    private ArrayList<Vertex> connectedVertices = new ArrayList<>();

    public Vertex(int point)
    {
        this.point = point;
    }

    //TODO: mb something's wrong here
    public void connectVertices(Vertex vertex)
    {
        connectedVertices.add(vertex);
//        vertex.getConnectedVertices().add(this);
    }

    public void setAttachedVertex(Vertex attachedVertex) {
        this.attachedVertex = attachedVertex;
    }

    public Vertex getAttachedVertex() {
        return attachedVertex;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean attached) {
        isAttached = attached;
    }

    public boolean getSet()
    {
        return this.isFirstSet;
    }

    public void setFirstSet(boolean flag)
    {
        isFirstSet = flag;
    }

    public int getPoint() {
        return point;
    }

    public ArrayList<Vertex> getConnectedVertices() {
        return connectedVertices;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setConnectedVertices(ArrayList<Vertex> connectedVertices) {
        this.connectedVertices = connectedVertices;
    }
}
