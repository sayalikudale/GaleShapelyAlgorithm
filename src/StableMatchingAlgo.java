/**
 * Class to perform stable matching
 *
 * @author Sayali Kudale
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class StableMatchingAlgo {
    /**
     * This main method
     * 1. prepare data structures from given input file
     * 2. perform Gale-Shapley algorithm to get stable matching
     * 3. print the result on console
     * @param args
     * pre:none
     * post: print error to the console if input is not valid otherwise print stable matching result
     */
    public static void main(String[] args) {

        final String inputFileName = "test.txt";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Queue<Integer> availableProposers = new LinkedList<>();
        ArrayList<Proposer> proposerList = new ArrayList<>();
        ArrayList<Proposee> proposeeList = new ArrayList<>();
        int[] proposerMatching, proposeeMatching;
        int numberOfMatches;

        try {
            fileReader = new FileReader(inputFileName);
            bufferedReader = new BufferedReader(fileReader);
            numberOfMatches = Integer.parseInt(bufferedReader.readLine().trim());

            //special case for 0 input
            if (numberOfMatches == 0)
                return;

            proposerMatching = new int[numberOfMatches + 1];
            proposeeMatching = new int[numberOfMatches + 1];

            readProposers(bufferedReader, numberOfMatches, availableProposers, proposerList);

            setProposerPreference(bufferedReader, numberOfMatches, proposerList);

            readProposee(bufferedReader, numberOfMatches, proposeeList);

            setProposeePreference(bufferedReader, numberOfMatches, proposeeList);

            //validate the inputs
            if (!validateInput(proposerList, proposeeList, numberOfMatches))
                return;

            galeShapelyAlgo(availableProposers, proposerList, proposeeList, proposerMatching, proposeeMatching);

            printResult(proposerList, proposeeList, proposerMatching, proposeeMatching);

        } catch (FileNotFoundException fnfe) {
            System.err.println(inputFileName + " file not found !!");
        } catch (IOException ioe) {
            System.err.println("Something went wrong!! \n" + ioe.getMessage());

        } catch (NumberFormatException nfe) {
            System.err.println("Preferences are not in number format " + nfe.getMessage());
        } catch (NullPointerException npe) {
            System.err.println("Input data is Invalid: " + npe.getMessage());
        } finally {
            try {
                if (bufferedReader != null && fileReader != null) {
                    bufferedReader.close();
                    fileReader.close();
                }
            } catch (IOException ioe) {
                System.err.println("Error in InputStream close(): " + ioe);
            }
        }

    }

    /**
     * Perform below validation on input data.
     * 1. Proposer's and Proposes' preference are not null
     * 2. Proposer's and Proposes' preference values should be between 1 to number of matches and less than or equal to numberOfMatches
     * 3. Proposer's and Proposes' preference values should not be repeated
     * 4. Proposer's and Proposes' number of preference should be equal to numberOfMatches
     * @param proposerList
     * @param proposeeList
     * @param numberOfMatches
     * pre: memory and data assigned to proposerList,proposeeList,numberOfMatches
     * post: return false if validation fails and print error to the console otherwise return true
     */
    private static boolean validateInput(ArrayList<Proposer> proposerList,
                                         ArrayList<Proposee> proposeeList,
                                         int numberOfMatches) {
        if (null == proposerList || null == proposeeList) {
            System.err.println("Prefernces can not be null or empty!!!");
            return false;
        }
        if (proposerList.size() != numberOfMatches || proposeeList.size() != numberOfMatches) {
            System.err.println("Prefernces input values does not match with number of matches!!!");
            return false;
        }
        if (checkDuplicatePreferences(proposerList)) {
            System.err.println("input values are not in valid format. Preferences should be between 1 to number of matches, " +
                    "should not repeat preferences");
            return false;
        }

        if (checkDuplicateRank(proposeeList)) {
            System.err.println("input values are not in valid format. Preferences should be between 1 to number of matches, " +
                    "should not repeat preferences");
            return false;
        }

        return true;

    }

    /**
     * Perform below validation on input data:
     * 1. Proposer's preference values should be between 1 to number of matches and less than or equal to number Of Matches
     * 2. Proposer's preference values should not be repeated
     * @param proposerList
     * pre: memory and data assigned to proposerList
     * post: return true if validation fails otherwise return false
     * */
    private static boolean checkDuplicatePreferences(ArrayList<Proposer> proposerList) {

        for (int i = 0; i < proposerList.size(); i++) {
            int[] preferences = proposerList.get(i).getPreference();
            Set<Integer> validPreference = new HashSet<>();
            for (int j = 0; j < preferences.length; j++) {
                if (preferences[j] < 0 || preferences[j] > preferences.length || validPreference.contains(preferences[j]))
                    return true;

                validPreference.add(preferences[j]);
            }

        }
        return false;
    }

    /**
     * Proposes' rank values should be between 1 to number of matches and less than or equal to number Of Matches
     * Proposes' rank values should not be repeated
     * @param proposeeList
     * pre: memory and data assigned to proposeeList
     * post: return true if validation fails otherwise return false
     */
    private static boolean checkDuplicateRank(ArrayList<Proposee> proposeeList) {

        for (int i = 0; i < proposeeList.size(); i++) {
            int[] rank = proposeeList.get(i).getRank();
            Set<Integer> validPreference = new HashSet<>();
            for (int j = 1; j < rank.length; j++) {
                if (rank[j] < 0 || rank[j] > rank.length || validPreference.contains(rank[j]))
                    return true;

                validPreference.add(rank[j]);
            }

        }
        return false;
    }

    /**
     * This method reads proposers from given file
     * @param bufferedReader
     * @param numberOfMatches
     * @param availableProposers
     * @param proposerList
     * @throws IOException IOException if input is incorrect
     * pre:memory assigned to proposerList,bufferedReader and numberOfMatches,availableProposers
     * post:Proposer object is initialised with index and name and added in proposerList
     */
    private static void readProposers(BufferedReader bufferedReader, int numberOfMatches, Queue<Integer> availableProposers,
                                      ArrayList<Proposer> proposerList) throws IOException, NullPointerException {
        for (int i = 1; i < numberOfMatches + 1; i++) {
            availableProposers.add(i);
            Proposer proposer = null;
            try {
                proposer = new Proposer(i, bufferedReader.readLine().trim());
            } catch (IOException | NullPointerException e) {
                throw e;
            }
            proposerList.add(proposer);
        }


    }

    /**
     * This method assign proposer preference to object in proposerList
     * @param bufferedReader
     * @param numberOfMatches
     * @param proposerList
     * @throws IOException if input is incorrect
     * pre: memory assigned to proposerList and bufferedReader
     * post: assignes values to array preference in each proposer object of proposerList
     */
    private static void setProposerPreference(BufferedReader bufferedReader, int numberOfMatches,
                                              ArrayList<Proposer> proposerList) throws IOException, NumberFormatException {
        for (int i = 1; i < numberOfMatches + 1; i++) {
            String[] arr = bufferedReader.readLine().split(" ");
            int[] intArr = new int[arr.length];
            for (int j = 0; j < arr.length; j++) {
                try {
                    intArr[j] = Integer.parseInt(arr[j]);

                } catch (NumberFormatException nfe) {
                    throw nfe;

                }
            }
            proposerList.get(i - 1).setPreference(intArr);
        }
    }

    /**
     *This method reads proposee from given file
     * @param bufferedReader
     * @param numberOfMatches
     * @param proposeeList
     * @throws IOException IOException if input is incorrect
     * pre:memory assigned to proposeeList,bufferedReader and numberOfMatches
     * post:proposee object is initialised with index and name and added in proposeeList
     */
    private static void readProposee(BufferedReader bufferedReader, int numberOfMatches, ArrayList<Proposee> proposeeList)
            throws IOException, NullPointerException {
        for (int i = 1; i < numberOfMatches + 1; i++) {

            try {
                Proposee proposee = new Proposee(i, bufferedReader.readLine().trim());
                proposeeList.add(proposee);
            } catch (IOException | NullPointerException e) {
                throw e;
            }


        }
    }

    /**
     * This method assign proposer rank from proposee preference to object in proposeeList
     * @param bufferedReader
     * @param numberOfMatches
     * @param proposeeList
     * @throws IOException IOException if input is incorrect
     * pre: memory assigned to proposeeList and bufferedReader
     * post: assignes values to array rank in each proposee object of proposeeList
     */
    private static void setProposeePreference(BufferedReader bufferedReader, int numberOfMatches,
                                              ArrayList<Proposee> proposeeList) throws IOException, NumberFormatException {
        for (int i = 1; i < numberOfMatches + 1; i++) {
            String[] arr = bufferedReader.readLine().split(" ");
            int[] intArr = new int[arr.length + 1];
            for (int j = 1; j < arr.length + 1; j++) {
                try {
                    intArr[j] = Integer.parseInt((arr[j - 1]));

                } catch (NumberFormatException nfe) {
                    throw nfe;
                }
            }
            //store it as rank
            int[] rank = new int[arr.length + 1];
            for (int k = 1; k < arr.length + 1; k++) {
                rank[intArr[k]] = k;
            }
            proposeeList.get(i - 1).setRank(rank);
        }
    }

    /**
     * This method performs Gayle-Shapely algorithm for Stable matching
     * @param availableProposers
     * @param proposerList
     * @param proposeeList
     * @param proposerMatching
     * @param proposeeMatching
     * pre: memory should be allocated to availableProposers,proposerList,proposeeList,proposerMatching,proposeeMatching
     *      and validateInput method should be performed
     * post: Stable match get assigned in array proposerMatching,proposeeMatching
     */
    private static void galeShapelyAlgo(Queue<Integer> availableProposers, ArrayList<Proposer> proposerList,
                                        ArrayList<Proposee> proposeeList, int[] proposerMatching, int[] proposeeMatching) {
        while (!availableProposers.isEmpty()) {
            int currentProposer = availableProposers.peek();
            Proposer objProposer = proposerList.get(currentProposer - 1);
            int proposee = objProposer.getNextProposal();
            Proposee objPraposee = proposeeList.get(proposee - 1);

            if (proposeeMatching[proposee] == 0) {

                performMatching(availableProposers, proposerMatching, proposeeMatching, currentProposer, proposee);

            } else if (objPraposee.isProposalAccepted(currentProposer, proposeeMatching[proposee])) {

                removeOldMatching(availableProposers, proposerMatching, proposeeMatching[proposee]);
                performMatching(availableProposers, proposerMatching, proposeeMatching, currentProposer, proposee);

            }
        }
    }

    /**
     * @param availableProposers
     * @param proposerMatching
     * @param previousProposer
     * pre: memory allocated to proposerMatching and availableProposers
     * post: removes the assigned proposee from proposersList and make proposer available
     * */
    private static void removeOldMatching(Queue<Integer> availableProposers, int[] proposerMatching, int previousProposer) {
        proposerMatching[previousProposer] = 0;
        availableProposers.add(previousProposer);
    }

    /**This method performs the proposer proposee matching
     * @param availableProposers
     * @param proposerMatching
     * @param proposeeMatching
     * @param currentProposer
     * @param proposee
     * pre:memory assigned to proposerMatching & proposeeMatching & availableProposers and values are added and data initialise to 0
     * post: assigns proposer and proposee to each other in respective array and removes proposer from queue
     */
    private static void performMatching(Queue<Integer> availableProposers, int[] proposerMatching, int[] proposeeMatching,
                                        int currentProposer, int proposee) {
        proposeeMatching[proposee] = currentProposer;
        proposerMatching[currentProposer] = proposee;
        availableProposers.remove();
    }

    /** print output to console
     * pre: memory assigned to proposerList and proposeeList and data assigned values after performing stable matching Algo
     * post: output result to console
     */
    private static void printResult(ArrayList<Proposer> proposerList, ArrayList<Proposee> proposeeList,
                                    int[] proposerMatching, int[] proposeeMatching) {
        for (int i = 1; i < proposerMatching.length; i++) {
            Proposer proposer = proposerList.get(i - 1);
           // Proposee proposee = proposeeList.get(proposeeMatching[i] - 1);  //previous submitted code
            Proposee proposee = proposeeList.get(proposerMatching[i] - 1); ////newly modified code
            System.out.println(proposer.getName() + " / " + proposee.getName());
        }
    }


}
