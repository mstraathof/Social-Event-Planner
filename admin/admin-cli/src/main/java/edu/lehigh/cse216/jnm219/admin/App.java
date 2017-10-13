package edu.lehigh.cse216.jnm219.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;

/**
 * App is our basic admin app.  For now, it is a demonstration of the six key 
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("---------------------------");
        System.out.println("  [T] Create all tables");
        System.out.println("  [D] Drop all tables");
        System.out.println();
        System.out.println("  [a] Create unauthorized user table");
        System.out.println("  [U] Create user table");
        System.out.println("  [p] Create profile table");        
        System.out.println("  [m] Create message table");
        System.out.println("  [c] Create comment table");
        System.out.println("  [u] Create upvote table");
        System.out.println("  [d] Create downvote table");
        System.out.println();
        System.out.println("  [A] Drop unauthorized user table");
        System.out.println("  [X] Drop user table");
        System.out.println("  [P] Drop profile table");
        System.out.println("  [M] Drop message table");
        System.out.println("  [C] Drop comment table");
        System.out.println("  [Y] Drop upvote table");
        System.out.println("  [Z] Drop downvote table");
        System.out.println();
        System.out.println("  [r] Print unauthorized users");
        System.out.println("  [+] Authorize user");
        System.out.println("  [-] Reject user");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String mainActions = "TDq?";
        String allActions = "TDaUpmcudAXPMCYZq?r+-";
        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + mainActions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (allActions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    public static void main(String[] argv) {
        // get the Postgres configuration from the environment
        // Get the port on which to listen for requests
        Map<String, String> env = System.getenv();

        // Get a fully-configured connection to the database, or exit 
        // immediately
        // This url is for the main url for the quiet-taiga database
        Database db = Database.getDatabase("jdbc:postgresql://ec2-107-21-109-15.compute-1.amazonaws.com:5432/dfjhqhen0vfnm?user=wmptnnamvihvzv&password=021c55db34a371a345a4e8279d144dde484f6e1455b10b217525f6885e363433&sslmode=require");
        if (db == null)
        {
            return;
        }
        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            char action = prompt(in);
            if (action == '?') {
                menu();
            }
            // disconnect from database
            else if (action == 'q') {
                break;
            } else if (action == 'T') {
                db.createAllTables();
            } else if (action == 'D') {
                db.dropAllTables();
            } 
            // Individual creation of tables
            else if (action == 'a') {       // tblUnauthorizedUser
                db.createTable('a');
            }else if (action == 'U') {       // tblUser
                db.createTable('U');
            } else if (action == 'p') {     // tblProfile
                db.createTable('p');
            } else if (action == 'm') {     // tblMessage
                db.createTable('m');
            } else if (action == 'c') {     // tblComment
                db.createTable('c');
            } else if (action == 'u') {     // tblUpVote
                db.createTable('u');
            } else if (action == 'd') {     // tblDownVote
                db.createTable('d');
            } 
            // Individual table drops
            else if (action == 'A') {
                db.dropTable("tblUnauthUser");
            } else if (action == 'X') {   
                db.dropTable("tblUser");
            } else if (action == 'P') {       
                db.dropTable("tblProfile");
            } else if (action == 'M') {       
                db.dropTable("tblMessage");
            } else if (action == 'C') {       
                db.dropTable("tblComment");
            } else if (action == 'Y') {       
                db.dropTable("tblUpVote");
            } else if (action == 'Z') {       
                db.dropTable("tblDownVote");
            } 
            // misc.
            else if (action == 'r') {
                db.selectUnauthUserAll();
            } else if (action == '+') {
                String username = getString(in, "Enter username");
                if (!username.equals("")) {
                    String[] credentials = new String[4];   // username, realname, email, password
                    credentials[0] = username;
                    if (db.authorizeUser(credentials)) {
                        //System.out.println(credentials[0] + " " + credentials[1] + " " + credentials[2] + " " + credentials[3]);
                        String subject = "Welcome to The Buzz";
                        String content = "Thank you, " + credentials[1] + " for joining the Buzz. Your username is: " + credentials[0] + ". Your password is: " + credentials[3];
                        Mailer mailer = new Mailer(credentials[2], subject, content);   // to, subject, content
                        mailer.send();
                    }
                    else {
                        System.out.println("Unable to authorize user");
                    }
                } 
            } else if (action == '-') {
                String username = getString(in, "Enter username");
                db.rejectUser(username);
            }
            
        }
         db.disconnect();
    }

    /**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar      The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static String getDBURLFromEnv() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        return processBuilder.environment().get("JDBC_DATABASE_URL");
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }
}
      