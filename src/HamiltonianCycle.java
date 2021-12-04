public class HamiltonianCycle extends NPCompleteProblem {

    public HamiltonianCycle() {
    }

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
     */
    public void makeRandomProblem(int N) {
        
    }

    /**
     * Reduce this problem to SAT by constructing a CNF clause
     * that is satisfied iff there is a solution to this decision
     * problem
     * 
     * @return CNF Clause
     */
    public CNF getCNF() {
        CNF c = new CNF();

        return c;
    }

    /**
     * 
     * @param vals Assignment of CNF
     * @return true if vals is a solution to the problem
     */
    public boolean verifyCNFCertificate(boolean[] vals) {
        return false;
    }

    public static void main(String[] args) {
        HamiltonianCycle h = new HamiltonianCycle();
        h.testProblems(10, 20);
    }
}