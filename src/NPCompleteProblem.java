public class NPCompleteProblem {

    /**
     * Return your pseudonym for the class-wide experiments
     * @return Your pseudonym
     */
    public String getPseudonym() {
        return "Base class pseudonym";
    }

    /**
     * Return a description of the problem you're solving
     * @return Description of problem
     */
    public String getDescription() {
        return "Base class description";
    }


    /**
     * Initialize a random problem of size N which has a solution
     * @param N Size of problem
     */
    public void makeRandomProblem(int N) {
        System.out.println("Warning: Calling makeRandomProblem() on base class");
    }

    /**
     * Reduce this problem to SAT by constructing a CNF clause
     * that is satisfied iff there is a solution to this decision
     * problem
     * 
     * @return CNF Clause
     */
    public CNF getCNF() {
        System.out.println("Warning: Calling getCNF() on base class");
        return null;
    }

    /**
     * 
     * @param vals Assignment of CNF
     * @return true if vals transformed into the problem is a solution
     *         to the problem
     */
    public boolean verifyCNFCertificate(boolean[] vals) {
        return false;
    }


    public void testProblems(int N, int numProblems) {
        for (int i = 0; i < numProblems; i++) {
            makeRandomProblem(N);
            CNF c = getCNF();
            
        }
    }
}
