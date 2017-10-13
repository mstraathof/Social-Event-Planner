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

    private static byte [] encryptPw (String password,byte [] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations= 1000;
        char[] chars = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte [] securedPw= skf.generateSecret(spec).getEncoded();
        return securedPw;
    }

    private static byte [] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    static Hashtable<String,Integer> logged_in=new Hashtable<String,Integer    >();
    public static int keyGenerator (){
        Random rand = new Random();
        int  random = rand.nextInt(10000) + 1000;
        return random;
    };
    public static boolean checkLogin (String userName)
    {
        return logged_in.containsKey(userName);
    };
    public static void logOut(String user)
    {
        logged_in.remove(user);
    };
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
        Spark.post("/checkLogin",(request, response) -> { 
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json"); 
            boolean checkExist= checkLogin(req.mUsername);
            int key=0;
            boolean mCheck=false;
            System.out.println(checkExist);
            System.out.println(req.mUsername);
            if (checkExist)
            {
                key=logged_in.get(req.mUsername);
                if (key==req.mKey)
                {
                    mCheck=true;
                    return  gson.toJson(new StructuredLoginCheck("ok",null,mCheck));
                }
            }
            return  gson.toJson(new StructuredLoginCheck("error",null,mCheck));
        });

        // GET route that returns all message titles and Ids.  All we do is get 
        // the data, embed it in a StructuredResponse, turn it into JSON, and 
        // return it.  If there's no data, we return "[]", so there's no need 
        // for error handling.
        
        Spark.get("/messages", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredMessage("ok", null, db.selectAllMessage()));
        });

        // GET route that returns everything for a single row in the DataStore.
        // The ":id" suffix in the first parameter to get() becomes 
        // request.params("id"), so that we can get the requested row ID.  If 
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible 
        // error is that it doesn't correspond to a row with data.
        Spark.get("/messages/:messageId", (request, response) -> {
            int idx = Integer.parseInt(request.params("messageId"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            RowMessage data = db.selectOneMessage(idx);
            if (data == null) {
                return gson.toJson(new StructuredMessage("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredMessage("ok", null, data));
            }
        });
         // POST route for adding a new element to the DataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        
        Spark.post("/messages", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            //String  user = request.params("username");
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            //int userId=db.getUserId(req.mUsername);
            // NB: createEntry checks for null title and message
            int newId = db.insertOneMessage(req.mSubject, req.mMessage,req.mUsername); // mSubject vs mTitle?
            if (newId == -1) {
                return gson.toJson(new StructuredMessage("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredMessage("ok", "" + newId, null));
            }
        });
        Spark.get("/comments/:messageId", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            int mId = Integer.parseInt(request.params("messageId"));
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredComment("ok", null, db.selectAllComment(mId)));
        });

        Spark.post("/comments", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            //String  user = request.params("username");
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            //int userId=db.getUserId(req.mUsername);
            // NB: createEntry checks for null title and message
            System.out.println(req.mUsername +req.mMessageId+req.mComment);
            boolean check = db.insertComment(req.mUsername, req.mMessageId,req.mComment); // mSubject vs mTitle?
            System.out.println(check);
            if (check == false) {
                return gson.toJson(new StructuredComment("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredComment("ok", "" + check, null));
            }
        });


        Spark.get("/register", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllUser()));
        });
        /**This method is to get the information from user to put into table for registeration */
         Spark.post("/register", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            boolean newUser = db.insertUser(req.mUsername, req.mRealName, req.mEmail); // mSubject vs mTitle?
            if (newUser == false) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newUser, null));
            }
        });
         /*
       Spark.get("/login", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllUser()));
        });*/

        Spark.post("/login", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            byte [] salt = db.getUserSalt(req.mUsername);
            if (salt==null)
            {
                System.out.println("Said salt was null");
                return gson.toJson(new Structured_login("error", "No account under that username or password", 3));
            }
            System.out.println("Got passed salt");
            byte [] password= encryptPw (req.mPassword,salt);
            // NB: createEntry checks for null title and message8
            
            boolean curUser = db.selectOneUser(req.mUsername, password); // mSubject vs mTitle?
            if (curUser==false) {
                System.out.println("Got NO select one user");
                return gson.toJson(new Structured_login("error", "No account under that username or password", 2));
            } else {
                boolean createProfile=db.insertProfile(req.mUsername);
                int key=keyGenerator();
                System.out.println("Got YES select one user");
                logged_in.put(req.mUsername,key);
                System.out.println(key);
                System.out.println(logged_in);
                return gson.toJson(new Structured_login("ok", "returned a key", key));
           }
        });
        Spark.post("/upVote", (request, response) -> {

            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
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

        Spark.post("/downVote", (request, response) -> {

            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            boolean downVote=db.updateDownVote(req.mUsername,req.mMessageId);
            if (downVote)
            {
                return gson.toJson(new StructuredMessage("ok", "upvoted", null));
            }
            else 
            {
                return gson.toJson(new StructuredMessage("error", "upvote failed", null));
            }
        });
        
        Spark.post("/profile", (request, response) -> {
             SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
             response.status(200);
             response.type("application/json");
             boolean makeProfile=db.updateProfile(req.mUsername,req.mProfile);
             if (makeProfile)
             {
                return gson.toJson(new StructuredProfile("ok", "successfully made a profile", null,null, null, null, null));
             }
             else
             {
                return gson.toJson(new StructuredProfile("ok", "error making a profile", null, null,null, null, null));
             }
        });
        Spark.get("/profile/:username", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            String user =request.params("username");
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredProfile("ok", "passing in user profile", db.selectProfile(user),db.selectUserMessage(user),db.selectUserComment(user),db.selectMessageLiked(user),db.selectMessageDisliked(user)));
        });
        Spark.put("/changePassword/:username", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            String user =request.params("username");
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            byte [] salt=db.getUserSalt(user);
            byte [] newSalt=getSalt();
            byte [] password= encryptPw (req.mCurrentPassword,salt);
            if (db.selectOneUser(user,password))
            {
                byte [] newPassword= encryptPw (req.mNewPassword,newSalt);
                boolean check=db.updatePassword(user,newPassword,newSalt);
                if (check)
                {
                    return gson.toJson(new Structured_login("ok", "changed password",1));
                }
                else
                {
                    return gson.toJson(new Structured_login("error", "change password failed",-1));
                }
            }
            else
            {
               return gson.toJson(new Structured_login("error", "current password is wrong",-2));
            }
            
        });

        Spark.post ("/logout/", (request, response)->{
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json");
            String username=req.mUsername;
            if (checkLogin(username)==false)
            {
                return gson.toJson(new StructuredResponse("error", "this person is not logged in", null));
            }
            else 
            {
                logOut(username);
                return gson.toJson(new StructuredResponse("ok", username + " logged out ", null));
            }
        });
       
    }
}