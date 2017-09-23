package jnm219.cse216.lehigh.edu.tutorialforjnm219;

class Datum {
    /**
     * An integer id for this piece of data
     */
    int mId;

    /**
     * The string contains the title for the piece of data
     */
    String mTitle;

    /**
     * The string contains the message for the piece of data
     */
    String mMessage;

    /**
     * The integer mVote contains the sum or all the votes for the data
     */
    //int mVotes;


    /**
     * Construct a Datum by setting its index and text
     *
     * @param id The index of this piece of data
     * @param title The string contents for this piece of data
     * @param message The string contains the message for the piece of data
     * @param //mVotes The integer total number of votes for the piece of data
     */
    Datum(int id, String title, String message) {
        //Datum(int id, String title, String message, int votes) {
        mId = id;
        mTitle = title;
        mMessage = message;
        // mVote = votes;
    }
}
