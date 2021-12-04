import java.util.*;

class Sat {
    public ArrayList<int[]> clauses;
    public int N; // Number of literals

    public Sat() {
        clauses = new ArrayList<int[]>();
        N = 0;
    }
    
    public String toString() {
        String s = "";
        for (int i = 0; i < clauses.size(); i++) {
            s += "(";
            int[] clause = clauses.get(i);
            for (int k = 0; k < clause.length; k++) {
                int idx = clause[k];
                if (idx < 0) {
                    s += "¬";
                    idx *= -1;
                }
                idx--;
                s += "x" + idx;
                if (k < clause.length-1) {
                    s += " V ";
                }
            }
            s += ")";
            if (i < clauses.size() - 1) {
                s += " ^ ";
            }
        }
        return s;
    }
    
    /**
     * Add a clause to the expression
     * 
     * @param index An array of indices of the literals that are involved
     * @param pos True if the literal itself shows up, false if its
     *            complement shows up
     */
    public void addClause(int[] index, boolean[] pos) {
        int[] clause = new int[index.length];
        for (int i = 0; i < index.length; i++) {
            int idx = index[i]+1;
            if (idx > N) {
                N = idx;
            }
            if (!pos[i]) {
                idx *= -1;
            }
            clause[i] = idx;
        }
        clauses.add(clause);
    }
    

    /**
     * Generate a random 3-sat clause
     * 
     * @param NLiterals Number of literals to use
     * @param NClauses Number of clauses to create
     * @param seed Seed to use for repeatability
     */
    public void makeRandom3Sat(int NLiterals, int NClauses, long seed) {
        Random r = new Random();
        r.setSeed(seed);
        int[] index = new int[3];
        boolean[] pos = new boolean[3];
        for (int i = 0; i < NClauses; i++) {
            for (int j = 0; j < 3; j++) {
                index[j] = r.nextInt(NLiterals);
                pos[j] = r.nextBoolean();
            }
            addClause(index, pos);
        }
    }

    /**
     * Check an assignment of the literals to see if it satisfies
     * the clauses. (This overloaded version can print debugging info)
     * 
     * @param vals An array of the assignments of each literal
     * @param verbose Whether to print a bunch of info
     * @return Whether this assignment satisfies the clauses
     */
    public boolean isSatisfied(boolean[] vals, boolean verbose) {
        boolean res = true;
        if (verbose) {
            for (int i = 0; i < vals.length; i++) {
                System.out.print("x"+i+" = " + vals[i]);
                if (i < vals.length - 1) {
                    System.out.print(", ");
                }
                else {
                    System.out.println("");
                }
            }
        }
        int i = 0;
        while(res && i < clauses.size()) {
            boolean clauseTrue = false;
            int k = 0;
            int[] clause = clauses.get(i);
            while(!clauseTrue && k < clause.length) {
                int idx = clause[k];
                boolean flip = false;
                if (idx < 0) {
                    flip = true;
                    idx *= -1;
                }
                idx--;
                if (verbose) {
                    System.out.println("clause " + i + ", k = " + k + ", idx = " + idx);
                }
                boolean term = vals[idx];
                if (flip) {
                    term = !term;
                }
                clauseTrue = clauseTrue || term;
                k++;
            }
            if (verbose) {
                System.out.println("clause " + i + ": " + clauseTrue);
            }
            res = res && clauseTrue;
            i++;
        }
        if (verbose) {
            System.out.println("Result: " + res);
        }
        return res;
    }
    
    /**
     * Check an assignment of the literals to see if it satisfies
     * the clauses. (This overloaded version can print debugging info)
     * 
     * @param vals An array of the assignments of each literal
     * @return Whether this assignment satisfies the clauses
     */
    public boolean isSatisfied(boolean[] vals) {
        return isSatisfied(vals, false);
    }
    

    public boolean bruteSolveRec(boolean[] vals, int index) {
        boolean res = false;
        if (index == N) {
            res = isSatisfied(vals);
        }
        else {
            if (bruteSolveRec(vals, index+1)) {
                res = true;
            }
            else {
                vals[index] = !vals[index];
                if (bruteSolveRec(vals, index+1)) {
                    res = true;
                }
            }
        }
        return res;
    }

    /**
     * Try every possible assignment and return true
     * Use a memory efficient in-place version of depth-first search
     * @return Whether there is an assignment
     */
    public boolean[] bruteSolve() {
        boolean[] vals = new boolean[N];
        for (int i = 0; i < N; i++) {
            vals[i] = false;
        }
        if (!bruteSolveRec(vals, 0)) {
            vals = null;
        }
        return vals;
    }
    
    /**
     * Test out the example in 7.33
     */
    public static void bookTest() {
        Sat s = new Sat();
        // (x0 V x0 V x1) ^ (not x0 V not x1 V not x1) ^ (not x0 V x1 V x1) 
        int[] c1 = {0, 0, 1};
        boolean[] d1 = {true, true, true};
        s.addClause(c1, d1);
        int[] c2 = {0, 1, 1};
        boolean[] d2 = {false, false, false};
        s.addClause(c2, d2);
        int[] c3 = {0, 1, 1};
        boolean[] d3 = {false, true, true};
        s.addClause(c3, d3);
        System.out.println("");
        boolean x0 = true, x1 = true;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                boolean[] vals = new boolean[2];
                vals[0] = x0;
                vals[1] = x1;
                s.isSatisfied(vals, true);
                x0 = !x0;
                System.out.println("-------------------------");
            }
            x1 = !x1;
        }
    }
    
    public static void Example1() {
        // (x0 V not x1 V x2) ^ (x0 V x1 V x2) ^ (not x0 V not x1 V not x2) ^ (not x0)
        Sat s = new Sat();
        int[] c1 = {0, 1, 2};
        boolean[] d1 = {true, false, true};
        s.addClause(c1, d1);
        int[] c2 = {0, 1, 2};
        boolean[] d2 = {true, true, true};
        s.addClause(c2, d2);
        int[] c3 = {0, 1, 2};
        boolean[] d3 = {false, false, false};
        s.addClause(c3, d3);
        int[] c4 = {0};
        boolean[] d4 = {false};
        s.addClause(c4, d4);
        System.out.println(s);
        boolean[] res = s.bruteSolve();
        for (int i = 0; i < res.length; i++) {
            System.out.print(res[i] + ", ");
        }
        boolean x0 = true, x1 = true, x2 = true;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    boolean[] vals = new boolean[3];
                    vals[0] = x0;
                    vals[1] = x1;
                    vals[2] = x2;
                    s.isSatisfied(vals, true);
                    x0 = !x0;
                    System.out.println("-------------------------");                    
                }
                x1 = !x1;
            }
            x2 = !x2;
        }
    }
    
    public static void testRandomClauses() {
        for (long seed = 0; seed < 100; seed++) {
            Sat s = new Sat();
            s.makeRandom3Sat(5, 40, seed);
            boolean[] vals = s.bruteSolve();
            //System.out.println(s);
            if (vals != null) {
                System.out.println("Should be satisfied: " + s.isSatisfied(vals));
            }
            else {
                System.out.println("Not satisfiable");
            }
        }

    }

    public static void main(String[] args) {
        testRandomClauses();
    }
}
