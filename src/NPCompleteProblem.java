import java.util.Random;

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
     * 
     * @param N Size of problem
     * @param seed Seed for repeatability
     */
    public void makeRandomProblem(int N, long seed) {
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
        for (long i = 0; i < numProblems; i++) {
            makeRandomProblem(N, i);
            CNF c = getCNF();
            
        }
    }

    /** Some other utility functions */

    /**
     * Print out the elements of an array, separated by commas
     * @param arr int array
     */
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length-1) {
                System.out.print(", ");
            }
            else {
                System.out.println("");
            }
        }
    }

    /**
     * A utility function for swapping elements in an int array
     * @param arr Array of integers
     * @param i First index to swap
     * @param j Second index to swap
     */
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Implement Fisher-Yates to get an array of shuffled numbers
     * 
     * @param N How many numbers to shuffle
     * @param r Random number generator to use
     * @return
     */
    public static int[] getShuffledNumbers(int N, Random r) {
        int[] res = new int[N];
        for (int i = 0; i < N; i++) {
            res[i] = i;
        }
        for (int i = 0; i < N-1; i++) {
            // Pick a random index between 0 and N-i-1, inclusive,
            // and swap this index with index N-i-1
            int j = r.nextInt(N-i);
            swap(res, N-i-1, j);
        }
        return res;
    }
}
