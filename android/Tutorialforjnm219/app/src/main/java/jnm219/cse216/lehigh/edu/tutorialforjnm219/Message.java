package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Jack on 10/4/2017.
 */

public class Message {

    /**
     * mMessgeId is what is used to distinguish different messages
     */
    int mId;

    /**
     * mSubject  is the text for the subject of a message
     */

    String mSubject;

    /**
     * mMessage is the text for the message of a message
     */

    String mMessage;

    /**
     * mCreateTime is used to show the create time of a message
     */

    String mCreateTime;

    /**
     * mUsername is used to hold data for the posters username
     */

    String mUsername;

    /**
     * mVotes holds the data for how many votes this message has
     */
    int mVotes;

    /**
     *
     * @param messageId
     * @param subject
     * @param message
     * @param createTime
     * @param votes
     * @param username
     */

    Message(int messageId, String subject,String message, String createTime, int votes, String username){

        mId = messageId;

        mSubject = subject;

        mMessage = message;

        mCreateTime = createTime;


        mVotes = votes;

        mUsername = username;
    }
}
