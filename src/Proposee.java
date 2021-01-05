/**
 * Class to represent a Proposee
 * The Proposee is represented by name and Index and
 * stores an array of preferences of Proposers.
 *Functionality includes deciding proposal accepted or rejected
 * @author Sayali Kudale
 *
 */
public class Proposee {

    private int index;
    private String name;
    private int[] rank;

    /**
     * constructor
     * pre: none
     * post: instance variables are initialized
     * */
    public Proposee(int index, String name) {
        this.index = index;
        this.name = name;
    }

    /**
     * accessor for name
     * pre: none
     * post: returns  name  */
    public String getName() {
        return name;
    }

    /**
     * accessor for the rank of Proposee
     * pre: Memory and data assigned for rank
     * post: returns  rank array  */
    public int[] getRank() {
        return rank;
    }

    /**
     * set the rank of Proposer
     * pre: Memory assigned for rank
     * post: set rank data array  */
    public void setRank(int[] rank) {
        this.rank = rank;
    }

    /**
     * @param currentProposer
     * @param previousProposer
     * pre: Memory and data assigned for rank
     * post: @return the result if proposal accepted or rejected based on the rank
     */
    public boolean isProposalAccepted(int currentProposer,int previousProposer){
        return rank[currentProposer] < rank[previousProposer];

    }

}
