package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Jack on 10/4/2017.
 */

public class Message {

    /**
     * mMessgeId is what is used to distinguish different messages
     */
    int mMessageId;

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
     * mUserId is used to link a message to a specific user, same as the mUserId in user
     */

    int mUserId;

    Message(int messageId, String subject,String message, String createTime, int userId){

        mMessageId = messageId;

        mSubject = subject;

        mMessage = message;

        mCreateTime = createTime;

        mUserId = userId;
    }
}
