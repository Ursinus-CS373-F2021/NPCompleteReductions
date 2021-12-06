import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;

public class HamiltonianPath extends GraphProblem {
    /**
     * Return your pseudonym for the class-wide experiments
     * @return Your pseudonym
     */
    public String getPseudonym() {
        return "C-Pain";
    }

    /**
     * Return a description of the problem you're solving
     * @return Description of problem
     */
    public String getDescription() {
        return "Hamiltonian cycle";
    }

    /**
     * Initialize a random problem of size N which has a solution
     * @param N Size of problem
     * @param seed Seed for repeatability
     */
    public void makeRandomProblem(int N, long seed) {
        assert(N > 0);
        Random r = new Random();
        r.setSeed(seed);
        this.N = N;
        edges = new ArrayList<int[]>();
        edgeSet = new HashSet<String>();
        // Step 1: Construct the edges that would create a hamiltonian path
        int[] order = getShuffledNumbers(N, r);
        for (int i = 0; i < N-1; i++) {
            int[] edge = {order[i], order[i+1]};
            if (edge[0] < edge[1]) {
                swap(edge, 0, 1);
            }
            edges.add(edge);
            edgeSet.add(edge[1] + "_" + edge[0]);
        }
        // Step 2: Add some extra edges that haven't been added yet
        int K = (int)(Math.sqrt(N)*(N-1)/2);
        K = Math.min(K, N*(N-1)/2-N);
        // Make a list of edges that haven't been added yet
        int L = N*(N-1)/2 - (N-1);
        int[][] edgesLeft = new int[L][2];
        int idx = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                if (!edgeSet.contains(i + "_" + j)) {
                    edgesLeft[idx][0] = i;
                    edgesLeft[idx][1] = j;
                    idx++;
                }
            }
        }
        for (int k = 0; k < K; k++) {
            idx = r.nextInt(L-k);
            int[] edge = edgesLeft[idx];
            edgesLeft[idx] = edgesLeft[L-k-1];
            edgeSet.add(edge[0]+"_"+edge[1]);
            edges.add(edge);
        }
    }

    /**
     * Each node j must appear in the path: (x1j v x2j v ... v xNj)
     * 
     * @param c CNF to which to add clauses
     */
    private void addExistenceClauses(CNF c) {
        int[] index = new int[N];
        boolean[] pos = new boolean[N];
        for (int i = 0; i < N; i++) {
            pos[i] = true;
        }
        for (int j = 0; j < N; j++) { // For each vertex j
            for (int i = 0; i < N; i++) { // For each sequence index i
                index[i] = i*N + j;
            }
            c.addClause(index, pos);
        }
    }

    /**
     * No node can appear twice in a path
     * Xij -> not Xkj for all i, j, k with i != k
     * Or, in other words, (not Xij or not Xkj)
     * With DeMorgan's, we can say more intuitively not(Xij and Xkj), though
     * this is not CNF
     * 
     * This is one place where the cubic blowup of clauses comes in
     * 
     * @param c CNF to which to add clauses
     */
    private void addNodeAtMostOnceClauses(CNF c) {
        boolean[] pos = {false, false};
        int[] index = new int[2];
        for (int i = 0; i < N; i++) {
            for (int k = i+1; k < N; k++) {
                if (i != k) {
                    // For each pair (i, k) of sequence indices, make sure
                    // that vertex j does not show up in both
                    for (int j = 0; j < N; j++) {
                        index[0] = i*N + j;
                        index[1] = k*N + j;
                        c.addClause(index, pos);
                    }
                }
            }
        }
    }

    /**
     * No sequence index can be occupied by more than one node
     * Xij -> not Xik for all i, j, k with j != k
     * Or, in other words, (not Xij or not Xik)
     * With DeMorgan's, we can say more intuitively not(Xij and Xik), though
     * this is not CNF
     * 
     * This is one place where the cubic blowup of clauses comes in
     * 
     * @param c CNF to which to add clauses
     */
    private void addPositionAtMostOnceClauses(CNF c) {
        boolean[] pos = {false, false};
        int[] index = new int[2];
        for (int j = 0; j < N; j++) {
            for (int k = j+1; k < N; k++) {
                if (j != k) {
                    for (int i = 0; i < N; i++) {
                        index[0] = i*N + j;
                        index[1] = i*N + k;
                        c.addClause(index, pos);
                    }
                }
            }
        }
    }

    /**
     * Every position i on the path must be occupied: 
     * xi1 V xi2 V ... V xiN for i = 1, ..., n
     * 
     * @param c CNF to which to add clauses
     */
    private void addEveryPositionOccupiedClauses(CNF c) {
        boolean[] pos = new boolean[N];
        int[] index = new int[N];
        for (int i = 0; i < N; i++) {
            pos[i] = true;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                index[j] = i*N + j;
            }
            c.addClause(index, pos);
        }
    }

    /**
     * Add the clauses that enforce that two nodes at adjacent
     * time indices must be connected by an (undirected) edge
     * 
     * xij -> !x_{i+1}k for i = 1, ..., n-1 and (j, k) not an edge
     * This is equivalent to (!xij or !x_{i+1}k) if (j, k) is not an edge
     * 
     * @param c CNF to which to add clauses
     */
    private void addEdgeEnforcingClauses(CNF c) {
        boolean[] pos = {false, false};
        int[] index = new int[2];
        for (int i = 0; i < N-1; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = j+1; k < N; k++) {
                    String s = j + "_" + k;
                    if (!edgeSet.contains(s)) {
                        index[0] = i*N + j;
                        index[1] = (i+1)*N+k;
                        c.addClause(index, pos);
                        index[0] = i*N + k;
                        index[1] = (i+1)*N+j;
                        c.addClause(index, pos);
                    }
                }
            }
        }
    }

    /**
     * Reduce this problem to SAT by constructing a CNF clause
     * that is satisfied iff there is a solution to this decision
     * problem
     * 
     * @return CNF Clause
     */
    public CNF getCNF() {
        // Create a literal xij that is true if the ith vertex in the
        // Hamiltonian path is vertex j
        CNF c = new CNF();
        addExistenceClauses(c);
        addNodeAtMostOnceClauses(c);
        addPositionAtMostOnceClauses(c);
        addEveryPositionOccupiedClauses(c);
        addEdgeEnforcingClauses(c);
        return c;
    }

    public static void main(String[] args) {
        HamiltonianPath h = new HamiltonianPath();
        int N = 20;
        h.makeRandomProblem(N, 9);
        CNF c = h.getCNF();
        System.out.println(c);
        boolean[] vals = c.solveDPLL();
        HamiltonianPathCert cert = new HamiltonianPathCert(vals, N);
        h.draw();
        cert.draw();
    }
}
