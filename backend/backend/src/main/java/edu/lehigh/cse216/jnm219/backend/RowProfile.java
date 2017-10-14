package edu.lehigh.cse216.jnm219.backend;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * RowProfile is fields of profile
 */
public class RowProfile {
    /**
     * The username of the profile owner
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
     * The profile_text of the user
     */
    String mProfile;

    /**
     * Construct a RowProfile object by providing values for its fields
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