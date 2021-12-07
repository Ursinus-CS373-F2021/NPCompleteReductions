public class HamiltonianPathCert extends Certificate {
    private int[] path;

    /**
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
        boolean satisfies = true;
        int idx = 0;
        while (satisfies && idx < N-1) {
            satisfies = satisfies && graph.containsEdge(path[idx], path[idx+1]);
            idx++;
        }
        return satisfies;
    }
}
