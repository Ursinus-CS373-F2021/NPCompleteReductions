/**
 * Programmer: Chris Tralie
 * Purpose: To provide a basic CNF class, along with a vanilla
 * implementation of a Davis-Putnam-Logemann-Loveland (DPLL) SAT solver
 */

import java.util.*;

class CNF {
    /**
     * Devise a string representation of the CNF
     * @param clauses List of clauses
     * @return String representation of clauses
     */
    public static String getClausesString(ArrayList<int[]> clauses) {
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

    public ArrayList<int[]> clauses; // Clauses
    public int N; // Number of literals

    public CNF() {
        clauses = new ArrayList<int[]>();
        N = 0;
    }
    
    public String toString() {
        return getClausesString(clauses);
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
    public void makeRandom3CNF(int NLiterals, int NClauses, long seed) {
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
    

    /**
     * Helper for brute force solving
     * 
     * @param vals Array of assignments of literals in working memory
     * @param index Index of current literal we're examining
     * @return Whether the clauses are satisfiable given the choices up to this index
     */
    public boolean solveBruteRec(boolean[] vals, int index) {
        boolean res = false;
        if (index == N) {
            res = isSatisfied(vals);
        }
        else {
            if (solveBruteRec(vals, index+1)) {
                res = true;
            }
            else {
                vals[index] = !vals[index];
                if (solveBruteRec(vals, index+1)) {
                    res = true;
                }
            }
        }
        return res;
    }

    /**
     * Try every possible assignment and return true
     * Use a memory efficient in-place version of depth-first search
     * @return The assignment of literals that satisfies the clauses,
     *         or null if they are not satisfiable
     */
    public boolean[] solveBrute() {
        boolean[] vals = new boolean[N];
        for (int i = 0; i < N; i++) {
            vals[i] = false;
        }
        if (!solveBruteRec(vals, 0)) {
            vals = null;
        }
        return vals;
    }

    /**
     * A class for keeping track of a particular branch of the DPLL computation
     */
    public class DPLLState {
        private boolean satisfiable; // Whether it is still possible to satisfy 
        private ArrayList<int[]> clauses; // Clauses that are left
        private ArrayList<Integer> literals; // Literals that are left
        private HashMap<Integer, Boolean> model; // Literals that have been assigned
        public DPLLState() {
            satisfiable = true; // Yet to be proven unsatisfiable
        }
        public String toString() {
            String s = getClausesString(clauses) + "\n";
            for (int i: model.keySet()) {
                s += "x" + i + "=" + model.get(i) + ", ";
            }
            s += "\nLiteralsLeft: ";
            for (int i = 0; i < literals.size(); i++) {
                s += literals.get(i) + ",";
            }
            return s;
        }
    }

    /**
     * Recursively apply the DPLL algorithm to find an assignment of literals 
     * making this true
     * @param state Input current configuration of the problem
     * @return A solution to the input configuration
     */
    public DPLLState DPLL(DPLLState state) {
        boolean anyFalse = false;
        int numTrueClauses = 0;
        int i = 0;
        // Keep track of clauses that need to be solved still
        ArrayList<int[]> clausesNext = new ArrayList<int[]>();
        while (i < state.clauses.size() && !anyFalse) {
            ArrayList<Integer> newClause = new ArrayList<Integer>();
            int[] clause = state.clauses.get(i);
            boolean clauseTrue = false;
            int falseTerms = 0; // Number of assigned literals that are mismatched in this clause
            int k = 0;
            while(!clauseTrue && k < clause.length) {
                int idx = clause[k];
                boolean val = true;
                if (idx < 0) {
                    val = false;
                    idx *= -1;
                }
                idx--;
                if (state.model.containsKey(idx)) {
                    if (state.model.get(idx) == val) {
                        clauseTrue = true;
                        numTrueClauses++;
                    }
                    else {
                        falseTerms++;
                    }
                }
                else {
                    newClause.add(clause[k]); // Keep track of unassigned literals
                }
                k++;
            }
            if (falseTerms == clause.length) {
                // If all of the literals in a clause have been assigned in opposition
                // with that clause, then the entire expression is false
                anyFalse = true;
            }
            else if (!clauseTrue) {
                // Test what's left of this clause at the next call
                int[] nextClause = new int[newClause.size()];
                for (k = 0; k < newClause.size(); k++) {
                    nextClause[k] = newClause.get(k);
                }
                clausesNext.add(nextClause);
            }
            i++;
        }
        DPLLState ret = state;
        if (anyFalse) {
            // If a single clause is false in model, return false
            state.satisfiable = false;
        }
        else if (numTrueClauses != state.clauses.size() && state.literals.size() > 0) {
            // If not all of the clauses have been shown to be true yet, 
            // and there are still literals left to try, then we have to keep trying things
            ret = new DPLLState();
            ret.literals = (ArrayList<Integer>)state.literals.clone();
            ret.model = (HashMap<Integer, Boolean>)state.model.clone();
            ret.clauses = clausesNext;

            // Step 1: Find pure symbols
            boolean[] pure = new boolean[N];
            int[] pureStatus = new int[N];
            for (i = 0; i < N; i++) {
                pure[i] = true;
                pureStatus[i] = -1;
            }
            for (i = 0; i < ret.clauses.size(); i++) {
                int[] clause = ret.clauses.get(i);
                for (int k = 0; k < clause.length; k++) {
                    int idx = clause[k];
                    boolean val = true;
                    if (idx < 0) {
                        val = false;
                        idx *= -1;
                    }
                    idx--;
                    if (pureStatus[idx] == -1) {
                        pureStatus[idx] = val?1:0;
                    }
                    else if (pure[idx]) {
                        if (val != (pureStatus[idx] == 1)) {
                            pure[idx] = false;
                        }
                    }
                }
            }
            // Step 2: Assign pure symbols
            int numPure = 0;
            for (i = 0; i < N; i++) {
                if (pure[i] && pureStatus[i] > -1) {
                    numPure++;
                    ret.model.put(i, pureStatus[i] == 1);
                    // Want to remove this literal itself, not index i
                    ret.literals.remove((Integer)i); 
                }
            }
            // Step 3: Find a unit clause and put the first one into the model
            i = 0;
            boolean foundUnit = false;
            while (i < ret.clauses.size() && !foundUnit) {
                if (ret.clauses.get(i).length == 1) {
                    foundUnit = true;
                    int idx = ret.clauses.get(i)[0];
                    boolean val = true;
                    if (idx < 0) {
                        val = false;
                        idx *= -1;
                    }
                    idx--;
                    ret.model.put(idx, val);
                    // Want to remove this literal itself, not index i
                    ret.literals.remove((Integer)idx);
                }
                i++;
            }
            if (numPure > 0 || foundUnit) {
                ret = DPLL(ret);
            }
            else {
                // Pick the first unused literal (at index 0) and try both options
                int idx = ret.literals.remove(0);
                ret.model.put(idx, false);
                DPLLState tryFalse = DPLL(ret);
                if (tryFalse.satisfiable) {
                    ret = tryFalse;
                }
                else {
                    ret.model.put(idx, true);
                    ret = DPLL(ret);
                }
            }
        }
        return ret;
    }

    /**
     * Use the DPLL algorithm to find an assignment of values to literals
     * that makes the expression true
     * 
     * @return The assignment of literals that satisfies the clauses,
     *         or null if they are not satisfiable
     */
    public boolean[] solveDPLL() {
        boolean[] res = null;
        DPLLState state = new DPLLState();
        state.clauses = clauses;
        state.literals = new ArrayList<Integer>();
        for (int i = 0; i < N; i++) {
            state.literals.add(i);
        }
        state.model = new HashMap<Integer, Boolean>();
        DPLLState ret = DPLL(state);
        if (ret.satisfiable) {
            res = new boolean[N];
            for (int i = 0; i < N; i++) {
                res[i] = true; // Assign as true by default, unless specified otherwise
            }
            for (int i: ret.model.keySet()) {
                res[i] = ret.model.get(i);
            }
        }
        return res;
    }
    
    /**
     * Test out the example in 7.33
     */
    public static void bookTest() {
        CNF s = new CNF();
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
        CNF s = new CNF();
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
        boolean[] res = s.solveBrute();
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
            CNF s = new CNF();
            s.makeRandom3CNF(4, 10, seed);
            boolean[] vals = s.solveDPLL();
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
