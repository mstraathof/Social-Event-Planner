package edu.lehigh.cse216.jnm219.backend;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * RowMessage is fields of users
 */
public class RowUser{

    /**
     * The user id of the user
     */
    int mUserId;
    /**
     * The username of the user
     */
    String mUsername;
    /**
     * The real name of the user
     */
    String mRealName;
    /**
     * The email of the user
     */
    String mEmail;
    /**
     * The salt of the user to encrypt password
     */
    byte [] mSalt;
    /**
     * The encrypted password
     */
    byte [] mPassword;

    /**
     * Construct a RowUser object by providing values for its fields
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