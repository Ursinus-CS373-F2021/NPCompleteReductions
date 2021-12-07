public class Certificate {
    protected boolean[] vals;
    protected int N;

    /**
     * Initialize a certificate from a CNF certificate
     * @param vals CNF certificate (array of true/false values for each literal)
     * @param N Problem size
     */
    public Certificate(boolean[] vals, int N) {
        this.vals = vals;
        this.N = N;
    }

    /**
     * See whether this certificate actually satisfies the problem in question
     * @param prob An NP complete problem definition
     * @return True if this certificate satisfies the problem, and false otherwise
     */
    public boolean satisfiesProblem(NPCompleteProblem prob) {
        System.out.println("Warning: Calling satisfiesProblem on base certificate");
        return false;
    }

    /**
     * For debugging
     */
    public void draw() {};
}