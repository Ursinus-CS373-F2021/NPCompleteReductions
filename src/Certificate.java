public class Certificate {
    protected boolean[] vals;

    /**
     * Initialize a certificate from a CNF certificate
     * @param vals CNF certificate (array of true/false values for each literal)
     */
    public Certificate(boolean[] vals) {
        this.vals = vals;
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