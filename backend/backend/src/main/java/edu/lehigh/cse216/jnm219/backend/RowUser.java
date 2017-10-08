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
public class RowUser{

    /**
     * The most recent time of row modification
     */
    int mUserId;
    String mUsername;
    String mRealName;
    String mEmail;
    byte [] mSalt;
    byte [] mPassword;

    /**
     * Construct a RowData object by providing values for its fields
     */

    
    public RowUser(int id, String username, String realName, String email, byte[] salt, byte[] password)
    {
        mUserId=id;
        mUsername=username;
        mRealName=realName;
        mEmail=email;
        mSalt=salt;
        mPassword=password;
    }
}