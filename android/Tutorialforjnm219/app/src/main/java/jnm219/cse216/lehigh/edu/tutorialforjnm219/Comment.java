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
     * mUserId is used to hold the Id of the user who wrote the comment
     */

    int mUserId;

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

    Comment(int commentId, int userId, int messageId, String comment, String createTime)
    {
        mCommentId = commentId;

        mUserId = userId;

        mMessageId = messageId;

        mComment = comment;

        mCreateTime = createTime;
    }
}
