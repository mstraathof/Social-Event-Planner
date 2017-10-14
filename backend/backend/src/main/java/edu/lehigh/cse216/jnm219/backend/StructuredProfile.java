package edu.lehigh.cse216.jnm219.backend;

/**
 * StructuredProfile provides a common format for success and failure messages,
 * with an optional payload of type Object that can be converted into JSON.
 * 
 * NB: since this will be converted into JSON, all fields must be public.
 */
public class StructuredProfile{
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
     * mMessageData will contain all the message user made
     */
    public Object mMessageData;
    /**
     * mCommentData will contain all the comment user made
     */
    public Object mCommentData;
    /**
     * mProfileData will contain user's profile
     */
    public Object mProfileData;
    /**
     * mLikedData will contain all the message user liked
     */
    public Object mLikedData;
    /**
     * mDislikedData will contain all the message user disliked
     */
    public Object mDislikedData;
    /**
     * Construct a StructuredProfile by providing a status, message, and data.
     * If the status is not provided, set it to "invalid".
     * 
     * @param status The status of the response, typically "ok" or "error"
     * @param message The message to go along with an error status
     * @param object An object with additional data to send to the client
     */
    public StructuredProfile(String status, String message, Object profile, Object allM, Object allC, Object allLM, Object allDM) {
        mStatus = (status != null) ? status : "invalid";
        mMessage = message;
        mProfileData = profile;
        mMessageData = allM;
        mCommentData = allC;
        mLikedData = allLM;
        mDislikedData = allDM;

    }
}