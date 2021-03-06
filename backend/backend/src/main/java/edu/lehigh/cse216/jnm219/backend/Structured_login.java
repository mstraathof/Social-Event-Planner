package edu.lehigh.cse216.jnm219.backend;

/**
 * Structured_login provides a common format for success and failure messages,
 * with an optional payload of type Object that can be converted into JSON.
 * 
 * NB: since this will be converted into JSON, all fields must be public.
 */
public class Structured_login {
    /**
     * The status is a string that the application can use to quickly determine
     * if the response indicates an error.  Values will probably just be "ok" or
     * "error", but that may evolve over time.
     */
    public String mStatus;

    /**
     * The message is only useful when this is an error, or when data is null.
     */
    public String mMessage;

    /**
     * gives login data object
     */
    public Object mLoginData;

    public String mUsername;
    /**
     * Construct a Structured_login by providing a status, message, and data.
     * If the status is not provided, set it to "invalid".
     * 
     * @param status The status of the response, typically "ok" or "error"
     * @param message The message to go along with an error status
     * @param object An object with additional data to send to the client
     */
    public Structured_login(String status, String message, Object data, String username) {
        mStatus = (status != null) ? status : "invalid";
        mMessage = message;
        mLoginData = data;
        mUsername = username;
    }
}