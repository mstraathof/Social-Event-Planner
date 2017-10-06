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
     * An indicator for whether the backend should call upVote or downVote functionality.
     */
    public int mChangeVote;
    public String mUsername;
    public String mRealName;
    public String mEmail;
    public String mPassword;
    
}