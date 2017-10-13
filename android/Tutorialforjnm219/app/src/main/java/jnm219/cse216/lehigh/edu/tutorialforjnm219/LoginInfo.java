package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Jack on 10/4/2017.
 * This information is passed around to confirm that a user is logged in
 */

public class LoginInfo {

    /**
     * User Id for the person logging in
     */
    String mUsername;

    /**
     * Unique key to confirm that someone is logged in
     */
    int mKey;

    LoginInfo(String username, int key){
        mUsername = username;

        mKey = key;
    }

    LoginInfo(){

        mUsername = null;

        mKey = 0;
    }
}
