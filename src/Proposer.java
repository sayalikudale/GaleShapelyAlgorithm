/**
 * Class to represent a Proposer
 * The Proposer is represented by name and Index and
 * stores an array of preferences of Proposee.
 *Functionality includes storing pointer to next available Proposee
 * @author Sayali Kudale
 *
 */
public class Proposer {

    private int index;
    private String name;
    private int[] preference;
    private int nextProposal;

    /**
     * constructor
     * pre: none
     * post: instance variables are initialized
     * */
    public Proposer(int index, String name) {
        this.index = index;
        this.name = name;
        this.nextProposal = 0;
    }

    /**
     * accessor for name
     * pre: none
     * post: returns  name  */
    public String getName() {
        return name;
    }

    /**
     * accessor for preference
     * pre: none
     * post: returns  preference array  */
    public int[] getPreference() {
        return preference;
    }

    /**
     * set the Preference of Proposer
     * pre: Memory assigned for preferences
     * post: set preference data array  */
    public void setPreference(int[] preference) {
        this.preference = preference;
    }

    /**
     * pre: Memory assigned for preferences and data is assigned, initial value of nextProposal is 0
     * post: @return the index of next proposee from preference list and increment the pointer to next proposee
     */
    public int getNextProposal(){
        return preference[nextProposal++];
    }

}
