public class HamiltonianPathCert extends Certificate {
    private int[] path;
    private int N;

    /**
     * 
     * @param vals SAT Certificate
     * @param N Number of vertices in graph
     */
    public HamiltonianPathCert(boolean[] vals, int N){
        super(vals);
        // Convert into a hamiltonian path
        this.N = N;
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

    public boolean satisfiesProblem(NPCompleteProblem npcproblem) {
        HamiltonianPath problem = (HamiltonianPath)npcproblem;
        return false;
    }
}
