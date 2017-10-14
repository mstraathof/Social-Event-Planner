package edu.lehigh.cse216.jnm219.backend;

/**
 * StructuredLoginCheck provides a common format for success and failure messages,
 * with an optional payload of type Object that can be converted into JSON.
 * 
 * NB: since this will be converted into JSON, all fields must be public.
 */
public class StructuredLoginCheck{
    /**
     * The status is a string that the application can use to quickly determine
     * if the response indicates an error.  Values will probably just be "ok" or
     * "logout", but that may evolve over time.
     * "logout" means that their key was not found in the hash table
     */
    public String mStatus;

    /**
     * The message is only useful when this is an error, or when data is null.
     */
    public String mMessage;

    /**
     * mCheck will return a boolean
     */
    public Object mCheck;

    /**
     * Construct a StructuredLoginCheck by providing a status, message, and data.
     * If the status is not provided, set it to "invalid".
     * 
     * @param status The status of the response, typically "ok" or "logout"
     * @param message The message to go along with an error status
     * @param object An object with additional data to send to the client
     */
    public StructuredLoginCheck(String status, String message, Object data) {
        mStatus = (status != null) ? status : "invalid";
        mMessage = message;
        mCheck = data;
    }
}