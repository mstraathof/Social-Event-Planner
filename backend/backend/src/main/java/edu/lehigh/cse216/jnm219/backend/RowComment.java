package edu.lehigh.cse216.jnm219.backend;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * RowComment is fields of comment
 */
public class RowComment {
    /**
     * The ID of comment
     */
    int mCommentId;
    /**
     * The id of the message
     */
    int mMessageId;
    /**
     * The comment 
     */
    String mComment;

    /**
     * The time of row creation
     */
    String mCreateTime;
    /**
     * The username that created the comment
     */
    String mUsername;

    /**
     * Construct a RowComment object by providing values for its fields
     */
    public RowComment(int id, String username, int messageId,String comment, String createTime) {
        mCommentId = id;
        mUsername= username;
        mMessageId = messageId;
        mComment=comment;
        mCreateTime = createTime;
    }

}