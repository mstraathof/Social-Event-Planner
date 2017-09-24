package jnm219.cse216.lehigh.edu.tutorialforjnm219;

class Datum {
    /**
     * An integer id for this piece of data
     */
    int mId;

    /**
     * The string contains the title for the piece of data
     */
    String mSubject;

    /**
     * The string contains the message for the piece of data
     */
    String mMessage;

    /**
     * The integer mVote contains the sum or all the votes for the data
     */
    int mVotes;


    /**
     * Construct a Datum by setting its index and text
     *
     * @param id The index of this piece of data
     * @param subject The string contents for this piece of data
     * @param message The string contains the message for the piece of data
     * @param votes The integer total number of votes for the piece of data
     */

    Datum(int id, String subject, String message, int votes) {
        mId = id;
        mSubject = subject;
        mMessage = message;
        mVotes = votes;
    }
}
