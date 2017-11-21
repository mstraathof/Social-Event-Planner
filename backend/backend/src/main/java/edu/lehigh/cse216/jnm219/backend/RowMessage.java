package edu.lehigh.cse216.jnm219.backend;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * RowMessage is fields of messages
 */
public class RowMessage {
    /**
     * The id of the message
     */
    int mId;
    /**
     * The subject of the message
     */
    String mSubject;
    /**
     * The message itself
     */
    String mMessage;
    /**
     * The number of votes for this row
     */
    int mVotes;
    /**
     * The time of row creation
     */
    String mCreateTime;
    /**
     * The username that created the message
     */
    String mUsername;

    String mWebUrl;

    /**
     * Construct a RowMessage object by providing values for its fields
     */
    public RowMessage(int id, String subject, String message, String username, String createTime, int votes, String webUrl) {
        mId = id;
        mSubject = subject;
        mMessage = message;
        mUsername=username;
        mCreateTime = createTime;
        mVotes=votes;
        mWebUrl = webUrl;
    }

}