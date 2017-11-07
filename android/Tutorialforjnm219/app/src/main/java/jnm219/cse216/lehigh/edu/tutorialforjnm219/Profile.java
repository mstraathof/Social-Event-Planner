package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Trent Gray on 11/6/2017.
 */
public class Profile {

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
    Profile(String username, String realname, String email, String profile){
        mUsername = username;
        mRealName = realname;
        mEmail = email;
        mProfile = profile;
    }
}
