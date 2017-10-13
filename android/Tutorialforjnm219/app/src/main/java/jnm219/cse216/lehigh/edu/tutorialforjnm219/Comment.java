package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Jack on 10/4/2017.
 */

public class Comment {

    /**
     * mCommentId is used to hold a unique value for each comment
     */
    int mCommentId;

    /**
     * mMessageId is used to reference the message that this comment came from
     */

    int mMessageId;

    /**
     * mComment is used to hold the text for the comment
     */

    String mComment;

    /**
     * mCreateTime is used to hold the data for when the comment was made
     */

    String mCreateTime;

    /**
     * mUsername holds the data for the username of the person who wrote the comment
     */
    String mUsername;

    /**
     *
     * @param commentId
     * @param messageId
     * @param comment
     * @param createTime
     * @param username
     */

    Comment(int commentId, int messageId, String comment, String createTime, String username)
    {
        mCommentId = commentId;

        mMessageId = messageId;

        mComment = comment;

        mCreateTime = createTime;

        mUsername = username;
    }
}
