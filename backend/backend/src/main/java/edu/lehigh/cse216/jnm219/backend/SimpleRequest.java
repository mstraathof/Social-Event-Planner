package edu.lehigh.cse216.jnm219.backend;

/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class SimpleRequest {
    /**
     * The title being provided by the client.
     */
    public String mSubject; // mSubject rather then mTitle now

    /**
     * The message being provided by the client.
     */
    public String mMessage;
    /**
     * A username provided by the client.
     */
    public String mUsername;
    /**
     * A real name provided by the client.
     */
    public String mRealName;
    /**
     * A email provided by the client.
     */
    public String mEmail;
    /**
     * A password provided by the client.
     */
    public String mPassword;
    /**
     * A key provided by the client.
     */
    public int mKey;
    /**
     * A message id provided by the client.
     */
    public int mMessageId;
    /**
     * A comment provided by the client.
     */
    public String mComment;
    /**
     * A profile_text provided by the client.
     */
    public String mProfile;
    /**
     * A current password provided by the client.
     */
    public String mCurrentPassword;
    /**
     * A new password provided by the client.
     */
    public String mNewPassword;
}