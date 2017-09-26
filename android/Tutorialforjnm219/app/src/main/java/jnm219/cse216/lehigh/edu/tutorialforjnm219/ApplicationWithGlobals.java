package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Application;

/**
 * Created by mira on 9/24/17.
 * Adapted from https://stackoverflow.com/questions/21810240/how-to-create-a-global-variable-in-android
 * This is how a global variable is created.
 */

public class ApplicationWithGlobals extends Application {

    private int mPosition;   // current position in adapter, likely (pun) because a like-button was pressed.

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }
}