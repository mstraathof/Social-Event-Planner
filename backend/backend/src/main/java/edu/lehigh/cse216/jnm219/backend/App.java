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
        Spark.get("/messages/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
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
        Spark.get("/comments", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            response.status(200);
            response.type("application/json"); 
            return gson.toJson(new StructuredMessage("ok", null, db.selectAllComment(req.mMessageId)));
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
            boolean check = db.insertComment(req.mUsername, req.mMessageId,req.mComment); // mSubject vs mTitle?
            if (check == false) {
                return gson.toJson(new StructuredMessage("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredMessage("ok", "" + check, null));
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
            byte [] salt = getSalt();
            byte [] password= encryptPw (req.mPassword,salt);
            // NB: createEntry checks for null title and message
            boolean newUser = db.insertUser(req.mUsername, req.mRealName, req.mEmail,salt, password); // mSubject vs mTitle?
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
        });
*/
        Spark.get("/login", (request, response) -> {
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
                 return gson.toJson(new Structured_login("error", "No account under that username or password", null));
            }
            byte [] password= encryptPw (req.mPassword,salt);
            // NB: createEntry checks for null title and message8
            
            boolean curUser = db.selectOneUser(req.mUsername, password); // mSubject vs mTitle?
            if (false) {
                return gson.toJson(new Structured_login("error", "No account under that username or password", null));
            } else {
                int key=252;//keyGenerator();
                //logged_in.put(req.mUsername,key);
                return gson.toJson(new Structured_login("ok", "its all good" , key));
            }
        });
/*
        Spark.get ("/log_in/:username", (request, response)->{
            String username=request.params("username");
            response.status(200);
            response.type("application/json");
            int key= keyGenerator(); 
            if (checkLogin(username)==false)
            {
                logged_in.put(username,key);
                return gson.toJson(new StructuredResponse("ok", username + " has key: "+key, null));
            }
            else 
            {
                logOut(username);
                return gson.toJson(new StructuredResponse("error", "this person is already logged in", null));
            }
        });
        Spark.get ("/log_out/:username", (request, response)->{
            String username=request.params("username");
            response.status(200);
            response.type("application/json");
            if (checkLogin(username)==false)
            {
                return gson.toJson(new StructuredResponse("error", "this person is not logged in", null));
            }
            else 
            {
                logOut(username);
                return gson.toJson(new StructuredResponse("ok", username + " logged out ", null));
            }
        });*/
       











        /**
         * PUT route for updating a row in the DataStore.  This is almost 
         * exactly the same as POST
         */
        /*
        Spark.put("/messages/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = -1;
            // Upvote if mChangeVote equals 1
            if(req.mChangeVote == 1)
            {
                result = db.upVote(idx, 1);
                return gson.toJson(new StructuredResponse("1", null, result));
            }
            // Downvote if mChangevote equals -1
            else if(req.mChangeVote == -1)
            {
                result = db.downVote(idx, -1);
                return gson.toJson(new StructuredResponse("1", null, result));
            }
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("error", "invalid mChangeVote value. Must be 1 or -1.", null));
            }
        });
*/
        /**
        // DELETE route for removing a row from the DataStore
        Spark.delete("/messages/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            int result = db.deleteRow(idx);
            if (result != -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
        */
        
    }
}