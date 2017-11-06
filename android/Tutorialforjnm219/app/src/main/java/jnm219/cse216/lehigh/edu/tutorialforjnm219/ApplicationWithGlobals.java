package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by mira on 9/24/17.
 * Adapted from https://stackoverflow.com/questions/21810240/how-to-create-a-global-variable-in-android
 * This is how a global variable is created.
 */

public class ApplicationWithGlobals extends Application {

    private int mPosition;   // current position in adapter, likely (pun) because a like-button was pressed.

    private static String mLoggedInUser = "error";
    private static int mKey = 0;
    private static GoogleApiClient mGoogleApiClient;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public static String getUsername(){
        return mLoggedInUser;
    }

    public static int getKey(){
        return mKey;
    }

    public static void setUsername(String username){ mLoggedInUser = username;}

    public static void setKey(int key){
        mKey = key;
    }
}