package edu.lehigh.cse216.jnm219.backend;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * RowData is like a struct in C: we use it to hold data, and we allow 
 * direct access to its fields.  In the context of this Database, RowData 
 * represents the data we'd see in a row.
 * 
 * We make RowData a static class of Database because we don't really want
 * to encourage users to think of RowData as being anything other than an
 * abstract representation of a row of the database.  RowData and the 
 * Database are tightly coupled: if one changes, the other should too.
 */
public class RowProfile {
    /**
     * The ID of this row of the database
     */
    String mUsername;
    /**
     * The subject stored in this row
     */
    String mRealName;
    /**
     * The message stored in this row
     */
    /**
     * The number of votes for this row
     */
    String mEmail;
    /**
     * The time of row creation
     */
    String mProfile;

    /**
     * Construct a RowData object by providing values for its fields
     */
     
    public RowProfile(String username, String realname, String email, String profile) {
        mUsername=username;
        System.out.println(mUsername);
        mRealName=realname;
        mEmail=email;
        mProfile=profile;
        System.out.println(mRealName);
        System.out.println(mEmail);
        System.out.println(mProfile);
    }

}