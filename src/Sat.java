import java.util.*;

class Sat {
    public ArrayList<int[]> clauses;
    public int N; // Number of variables

    public Sat() {
        clauses = new ArrayList<int[]>();
        N = 0;
    }
    
    public String toString() {
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
     * @param index An array of indices of the variables that are involved
     * @param pos True if the variable itself shows up, false if its
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
     * Check an assignment of the variables to see if it satisfies
     * the clauses. (This overloaded version can print debugging info)
     * 
     * @param vals An array of the assignments of each variable
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
     * Check an assignment of the variables to see if it satisfies
     * the clauses. (This overloaded version can print debugging info)
     * 
     * @param vals An array of the assignments of each variable
     * @return Whether this assignment satisfies the clauses
     */
    public boolean isSatisfied(boolean[] vals) {
        return isSatisfied(vals, false);
    }
    
    /**
     * Try every possible assignment and return true
     * @return Whether there is an assignment
     */
    public boolean bruteSolve() {
        boolean res = false;
        boolean finished = false;
        boolean[] aa = new boolean[N];
        for (int i = 0; i < N; i++) {
            aa[i] = false;
        }
        while (!finished && !res) {
            // Do binary addition to get to the next
            int k = 0;
            while (k < N) {
                
            }
        }
        return res;
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
    
    public static void main(String[] args) {
        Example1();
    }
}
