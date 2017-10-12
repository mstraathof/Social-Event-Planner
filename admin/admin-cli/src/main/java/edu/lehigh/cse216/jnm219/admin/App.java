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
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
        /* 
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [*] Query for all rows");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        */
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
        String allActions = "TDaUpmcudAXPMCYZq?r+";
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
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    /*
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
/*
    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    /*
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }
*/
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
                /*
                try {
                    db.mSelectUnauthUserAll.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                */
            } else if (action == '+') {
                String username = getString(in, "Enter username");
                if (!username.equals("")) {
                    String[] credentials = new String[4];
                    credentials[0] = username;
                    //String realname = new String();
                    //String email = new String();
                    //String password = new String();
                    //if (db.authorizeUser(username, password, email, realname)) {
                    if (db.authorizeUser(credentials)) {
                        System.out.println(credentials[0] + " " + credentials[1] + " " + credentials[2] + " " + credentials[3]);
                    }
                    else {
                        System.out.println("Unable to authorize user");
                    }
                } 
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
            /*
            else if (action == '1') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.RowData res = db.selectOne(id);
                if (res != null) {
                    System.out.println("  [" + res.mId + "] " + res.mSubject);
                    System.out.println("  --> " + res.mMessage);
                }
                */
                /*
             else if (action == '*') {
                ArrayList<Database.RowData> res = db.selectAll();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (Database.RowData rd : res) {
                    System.out.println("  [" + rd.mId + "] " + rd.mSubject);
                }*/
                /*
            } else if (action == '-') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteRow(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '+') {
                String subject = getString(in, "Enter the subject");
                String message = getString(in, "Enter the message");
                if (subject.equals("") || message.equals(""))
                    continue;
                int res = db.insertRow(subject, message);
                System.out.println(res + " rows added");
            } else if (action == '~') {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newMessage = getString(in, "Enter the new message");
                int res = db.updateOne(id, newMessage);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }*/
        // Always remember to disconnect from the database when the program 
        // exits
      