package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Jack on 10/4/2017.
 */

public class User {

    /**
     * mUserId contains the unique ID for a user
     */
    int mUserId;

    /**
     * mUsername contains the username for a user
     */
    String mUsername;

    /**
     * The string contains the real name for a user
     */
    String mRealName;

    /**
     * The string containts the email for a user
     */
    String mEmail;

    /**
     * mSalt contains the salt value that is used by the backend to retrieve the password
     */
    int mSalt;

    /**
     * password contains the encrypted password for a user, needs to be hashed and salted to get the password
     */
    String mPassword;


    User(int id, String username, String realName, String email, int salt, String password) {
        mUserId = id;
        mUsername = username;
        mRealName = realName;
        mEmail = email;
        mSalt = salt;
        mPassword = password;
    }
}
