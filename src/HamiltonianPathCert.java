public class HamiltonianPathCert extends Certificate {
    private int[] path;

    /**
     *  e.g I have 4 vertices in my graph
     *  
     *   xij: i indexes the choices I make, 
     *        j indexes the vertices in the graph
     * 
     *   x00      x01     **x02** x03
     *   **x10**  x11     x12     x13
     *   x20     **x21**  x22     x23
     *   x30      x31     x32     **x33**
     * 
     * 
     * 
     * @param vals SAT Certificate
     * @param N Problem size
     */
    public HamiltonianPathCert(boolean[] vals, int N) {
        super(vals, N);
        // Convert into a hamiltonian path
        path = new int[N];
        // Initialize as -1 values, which will indicate that no vertex
        // showed up at a particular position, which would be a violation
        for (int i = 0; i < N; i++) {
            path[i] = -1;
        }
        // Fill in the vertices that were actually chosen
        for (int idx = 0; idx < vals.length; idx++) {
            if (vals[idx]) {
                int j = idx%N;
                int i = (idx-j)/N;
                path[i] = j;
            }
        }
        NPCompleteProblem.printArray(path);
    }

    public void draw() {
        double[][] X = new double[N][2];
        for (int i = 0; i < N; i++) {
            X[i][0] = 0.1 + 0.4*(1+Math.cos(2*Math.PI*i/N));
            X[i][1] = 0.1 + 0.4*(1+Math.sin(2*Math.PI*i/N));
        }
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.RED);
        for (int i = 0; i < N-1; i++) {
            if (path[i] > -1 && path[i+1] > -1) {
                StdDraw.line(X[path[i]][0], X[path[i]][1], 
                             X[path[i+1]][0], X[path[i+1]][1]);
            }
        }
    }

    /**
     * Implement a verifier to check whether this certificate actually
     * solves the problem
     * 
     * @param npcproblem Graph in which we're checking the Hamiltonian path
     * @return true if this certificate solves this Hamiltonian path, or
     *         false otherwise
     */
    public boolean satisfiesProblem(NPCompleteProblem npcproblem) {
        HamiltonianPath graph = (HamiltonianPath)npcproblem;
        // As a sanity check, first make sure the certificate has the same
        // number of elements as there are vertices in the graph
        boolean satisfies = graph.getNumVertices() == N;
        if (satisfies) {
            // Step 1: Make sure every vertex in this graph was chosen
            // e.g. N = 4 and path = [2, 0, 1, 3]
            int numVertices = graph.getNumVertices();
            int[] seen = new int[N];
            for (int i = 0; i < N; i++) {
                seen[path[i]] = 1;
            }
            int verticesSeen = 0;
            for (int i = 0; i < N; i++) {
                verticesSeen += seen[i];
            }
            satisfies = (verticesSeen == numVertices);

            // Step 2: Make sure each edge in the path is actually in the graph
            int idx = 0;
            while (satisfies && idx < N-1) {
                satisfies = satisfies && graph.containsEdge(path[idx], path[idx+1]);
                idx++;
            }
        }

        return satisfies;
    }
}
