import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;

public class GraphProblem extends NPCompleteProblem {

    protected int N; // Number of vertices
    protected ArrayList<int[]> edges; // List of undirected edges
    protected HashSet<String> edgeSet; // A redundant dataset for easy lookup of edges

    public GraphProblem() {
    }

    /**
     * Return number of vertices in the graph
     * @return Number of vertices
     */
    public int getNumVertices() {
        return N;
    }

    /**
     * Return number of edges in the graph
     * @return Number of edges
     */
    public int getNumEdges() {
        return edges.size();
    }

    /**
     * Check to see whether an edge between two vertices 
     * exists in this class
     * @param i Index of first vertex
     * @param j Index of second vertex
     * @return Whether this edge is in the graph
     */
    public boolean containsEdge(int i, int j) {
        String s = "";
        if (i < j) {
            s = i + "_" + j;
        }
        else {
            s = j + "_" + i;
        }
        return edgeSet.contains(s);
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < edges.size(); i++) {
            int[] e = edges.get(i);
            s += "[" + e[0] + "," + e[1] + "]";
            if (i < edges.size() - 1) {
                s += ", ";
            }
        }
        return s;
    }

    /**
     * Draw the vertices as dots evenly spaced on a circle and the edges
     * as line segments between them
     */
    public void draw() {
        double[][] X = new double[N][2];
        for (int i = 0; i < N; i++) {
            X[i][0] = 0.1 + 0.4*(1+Math.cos(2*Math.PI*i/N));
            X[i][1] = 0.1 + 0.4*(1+Math.sin(2*Math.PI*i/N));
        }
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int k = 0; k < edges.size(); k++) {
            int i = edges.get(k)[0];
            int j = edges.get(k)[1];
            StdDraw.line(X[i][0], X[i][1], X[j][0], X[j][1]);
        }
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (int i = 0; i < N; i++) {
            StdDraw.point(X[i][0], X[i][1]);
        }
    }
}