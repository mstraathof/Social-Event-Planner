package edu.lehigh.cse216.jnm219.backend;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

// Import Google's JSON library
import com.google.gson.*;

//Importing the ability to access the database from Postgres
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.spec.InvalidKeySpecException;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
/**
 * Get an integer environment varible if it exists, and otherwise return the
 * default value.
 * 
 * @envar      The name of the environment variable to get.
 * @defaultVal The integer value to use as the default if envar isn't found
 * 
 * @returns The best answer we could come up with for a value for envar
 */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }
    // Enables CORS on requests. This method is an initialization method and should be called once.
    
    /**  
     *This method encrypts the password by taking in the string password from the user
     *  and randomly generated salt by the getSalt() method using PBKDF
     */
    public static byte [] encryptPw (String password,byte [] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations= 1000;
        char[] chars = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte [] securedPw= skf.generateSecret(spec).getEncoded();
        return securedPw;
    }
    /**
    * This randomly creates salt which is in byte []
    */
    public static byte [] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    // This static hashtable keeps the log of which users are logged in with their keys
    public static Hashtable<String,Integer> logged_in=new Hashtable<String,Integer>();
    /** This method randomly creates key to return to the android and web */
    public static int keyGenerator (){
        Random rand = new Random();
        int  random = rand.nextInt(10000) + 1000;
        return random;
    };
    /** This method checks the hashtable if they are logged in */
    public static boolean checkLogin (String userName)
    {
        return logged_in.containsKey(userName);
    };
    /** This method removes the user from the hash table, -- logged out */
    public static void logOut(String user)
    {
        logged_in.remove(user);
    };
    /** This method checks if the key and the username matches in the hashtable */
    public static boolean checkKey(String mUsername, int mKey)
    {
        boolean checkExist= checkLogin(mUsername);
        int key=0;
        if (checkExist)
        {
            key=logged_in.get(mUsername);
            if (key==mKey)
            {
                return  true;
            }
        }
        return  false;
    }
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException 
    {

        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See 
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();
        Spark.port(getIntFromEnv("PORT", 4567));

        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(1);

        // Set up the location for serving static files.  If the STATIC_LOCATION
        // environment variable is set, we will serve from it.  Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        // This is the register/login/logout/changePW section

        /** This route is to get the information from user to put into table for registeration */
         Spark.post("/register", (request, response) -> {
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            boolean newUser = db.insertUser(req.mUsername, req.mRealName, req.mEmail); 
            if (newUser == false) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newUser, null));
            }
        });
        // this route logs in the user and put them in hash table.
        // this also creates the profile page for the user if they just got 
         Spark.post("/login", (request, response) -> {
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            byte [] salt = db.getUserSalt(req.mUsername);
            if (salt==null) // No accounts exist under this username
            {
                return gson.toJson(new Structured_login("error", "No account under that username or password", 3));
                // android uses the value 3 -- discuss with android before changing the data value
            }
            byte [] password= encryptPw (req.mPassword,salt);// encrypt password to verify
            boolean curUser = db.selectOneUser(req.mUsername, password); // see if the password and username matched
            if (curUser==false) { // mismatch between pw and username 
                return gson.toJson(new Structured_login("error", "No account under that username or password", 2));
                // android uses the value 3 -- discuss with android before changing the data value
            } else { // succesfully found a user to login
                boolean createProfile=db.insertProfile(req.mUsername);
                if (checkLogin(req.mUsername)) // if the person is already logged in
                {
                    logOut(req.mUsername);// log the user out
                    return gson.toJson(new Structured_login("error", "Username "+ req.mUsername+"is already loggedin", -1));
                }
                else 
                {
                    int key=keyGenerator();// create key
                    logged_in.put(req.mUsername,key);// logged_in is hashtable, and add values into it

                    return gson.toJson(new Structured_login("ok", null, key));
                }
           }
        });
        // this route logs out people when they press log out
        // sends back status ok and null data when they successfully logged out
         Spark.post ("/logout", (request, response)->{
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            String username=req.mUsername;
            if (checkLogin(username)==false)// if the person is not logged in
            {
                return gson.toJson(new StructuredResponse("error", "this person is not logged in", null));
            }
            else // if person is logged in
            {
                logOut(username);
                return gson.toJson(new StructuredResponse("ok", username + " logged out ", null));
            }
        });
        // this put route changes password and updates tblUser
        // it verifies the old password, then updates with new salt and new encrypted pw
        Spark.put("/changePassword/:username", (request, response) -> {
            String user =request.params("username");
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            if (!checkKey(user,req.mKey))
            {
                return gson.toJson(new Structured_login("logout", null,false));
            }
            byte [] salt=db.getUserSalt(user);// get our old salt
            byte [] newSalt=getSalt();// create a new salt to enter into the table
            byte [] password= encryptPw (req.mCurrentPassword,salt);// get our old pw in bytes
            if (db.selectOneUser(user,password))
            {
                byte [] newPassword= encryptPw (req.mNewPassword,newSalt);// encrypt a new pw
                boolean check=db.updatePassword(user,newPassword,newSalt);// update the tblUser
                if (check)// if successfully updated
                {
                    return gson.toJson(new Structured_login("ok", null ,1));
                }
                else // if update had an error
                {
                    return gson.toJson(new Structured_login("error", "change password failed",-1));
                }
            }
            else // if the user doesn't exist in our tblUser
            {
               return gson.toJson(new Structured_login("error", "current password is wrong",-2));
            }
        });

        //This is the start of the messages

        //This get route returns all the attribute of all the messages
        Spark.get("/messages", (request, response) -> {
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredMessage("ok", null, db.selectAllMessage()));
        });
        // This post route allows user to create the messagge to the table
        Spark.post("/messages", (request, response) -> {
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            if (!checkKey(req.mUsername,req.mKey))
            {
                return gson.toJson(new StructuredMessage("logout", null,false));
            }
            boolean newId = db.insertOneMessage(req.mSubject, req.mMessage,req.mUsername); 
            if (!newId) {
                return gson.toJson(new StructuredMessage("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredMessage("ok", "" + newId, null));
            }
        });
        // This message chooses message according to the message id and returns it in
        // StructuredMessage with RowMessage Data
        Spark.get("/messages/:messageId", (request, response) -> {
            int idx = Integer.parseInt(request.params("messageId"));
            response.status(200);
            response.type("application/json");
            RowMessage data = db.selectOneMessage(idx);
            if (data == null) {
                return gson.toJson(new StructuredMessage("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredMessage("ok", null, data));
            }
        });

        //This is the starts of the comment calls

        // This route takes in the message id and sends back the data
        // that has all the comments of that message id.
        Spark.get("/comments/:messageId/:username/:key", (request, response) -> {
            int mId = Integer.parseInt(request.params("messageId"));
            String user =request.params("username");
            int key = Integer.parseInt(request.params("key"));
            response.status(200);
            response.type("application/json"); 
            if (!checkKey(user,key))
            {
                return gson.toJson(new StructuredComment("logout", null,false));
            }
            return gson.toJson(new StructuredComment("ok", null, db.selectAllComment(mId)));
        });

        // This route allows user to create comment and 
        // adds it into the table.
        Spark.post("/comments", (request, response) -> {
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            if (!checkKey(req.mUsername,req.mKey))
            {
                return gson.toJson(new StructuredComment("logout", null,false));
            }
            boolean check = db.insertComment(req.mUsername, req.mMessageId,req.mComment); // mSubject vs mTitle?
            if (check == false) {
                return gson.toJson(new StructuredComment("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredComment("ok", "" + check, null));
            }
        });
       // This is the start of upvote and downvote section

       //This will either unlike the message you already liked or like a new message
        Spark.post("/upVote", (request, response) -> {

            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            if (!checkKey(req.mUsername,req.mKey))
            {
                return gson.toJson(new StructuredMessage("logout", null,false));
            }
            boolean upVote=db.updateUpVote(req.mUsername,req.mMessageId);
            if (upVote)
            {
                return gson.toJson(new StructuredMessage("ok", "upvoted", null));
            }
            else 
            {
                return gson.toJson(new StructuredMessage("error", "upvote failed", null));
            }
        });

        //This will either undislike the message you already disliked or dislike a new message
        Spark.post("/downVote", (request, response) -> {

            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            if (!checkKey(req.mUsername,req.mKey))
            {
                return gson.toJson(new StructuredMessage("logout", null,false));
            }
            boolean downVote=db.updateDownVote(req.mUsername,req.mMessageId);
            if (downVote)
            {
                return gson.toJson(new StructuredMessage("ok", "downvoted", null));
            }
            else 
            {
                return gson.toJson(new StructuredMessage("error", "downvote failed", null));
            }
        });
        
        //This is the start of the profile page calls

        // This route allows user to create a new profile text
        // and post it to the tables.

        Spark.post("/profile", (request, response) -> {
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            if (!checkKey(req.mUsername,req.mKey))
            {
                return gson.toJson(new StructuredProfile("logout", "logged out",false,null,null,null,null));
            }
            boolean makeProfile=db.updateProfile(req.mUsername,req.mProfile);
            if (makeProfile)
            {
               return gson.toJson(new StructuredProfile("ok", "successfully made a profile", null,null, null, null, null));
            }
            else
            {
               return gson.toJson(new StructuredProfile("error", "error making a profile", null, null,null, null, null));
            }
        });
        /** This route allows web or android to receive informations to display
         * It will use the username to get 
         * User's real name, email, profile_text
         * All the message the person created
         * All the comments that the person commented
         * All the messges that user upvoted or downvoted
        */
        Spark.get("/profile/:username/:key", (request, response) -> {
            String user =request.params("username");
            int key = Integer.parseInt(request.params("key"));
            response.status(200);
            response.type("application/json"); 
            if (!checkKey(user,key))
            {
                return gson.toJson(new StructuredLoginCheck("logout", "logged out",false));
            }
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredProfile("ok", null, db.selectProfile(user),db.selectUserMessage(user),db.selectUserComment(user),db.selectMessageLiked(user),db.selectMessageDisliked(user)));
        });
        Spark.get("/profile/:otherUser/:username/:key", (request, response) -> {
            String user =request.params("username");
            String others =request.params("otherUser");
            int key = Integer.parseInt(request.params("key"));
            response.status(200);
            response.type("application/json"); 
            if (!checkKey(user,key))
            {
                return gson.toJson(new StructuredLoginCheck("logout", "logged out",false));
            }
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredProfile("ok", null, db.selectProfile(others),null,null,null,null));
        });
    }
}