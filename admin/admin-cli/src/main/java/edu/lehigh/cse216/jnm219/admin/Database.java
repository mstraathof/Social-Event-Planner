package edu.lehigh.cse216.jnm219.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Database {
 
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for inserting into the tblUser
     */
    private PreparedStatement mInsertUser;
    private PreparedStatement mSelectUnauthUserOne; // from the tblUnauthorizedUser table
    public PreparedStatement mSelectUnauthUserAll; // public so App.java can execute() this.

    /**
     * A prepared statement for creating the unauthorized user table in the database
     */
    private PreparedStatement mCreateUnauthUserTable;

    /**
     * A prepared statement for creating the user table in the database
     */
    private PreparedStatement mCreateUserTable;

    /**
     * A prepared statement for creating the user's profile table in the database
     */
    private PreparedStatement mCreateProfileTable;

    /**
     * A prepared statement for creating the message in the database
     */
    private PreparedStatement mCreateMessageTable;

    /**
     * A prepared statement for creating the comment table in the database
     */
    private PreparedStatement mCreateCommentTable;

    /**
     * A prepared statement for creating the down-vote table in the database
     */
    private PreparedStatement mCreateDownVoteTable;

    /**
     * A prepared statement for creating the up-vote table in the database
     */
    private PreparedStatement mCreateUpVoteTable;
 
    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() {
    }
    // url for test
    private static Connection getConnection(String url) throws URISyntaxException, SQLException {
        //String dbUrl = App.getDBURLFromEnv();
        return DriverManager.getConnection(url);
    }
    
    /**
     * Get a fully-configured connection to the database
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String dbUrl)  {
        // Create an un-configured Database object
        Database db = new Database();
  
        // Give the Database object a connection, fail if we cannot get one
        try {
            Connection conn = getConnection(dbUrl);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }
        catch (URISyntaxException e) {
            System.err.println("Error: DriverManager.getConnection() threw a URISyntaxException");
            e.printStackTrace();
            return null;
        }
        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            db.mCreateUnauthUserTable = db.mConnection.prepareStatement(
                "CREATE TABLE tblUnauthUser ("
                +"user_id SERIAL PRIMARY KEY,"
                +"username VARCHAR(255) UNIQUE,"
                +"realname VARCHAR(255),"
                +"email VARCHAR(255))"
            );
            db.mCreateUserTable = db.mConnection.prepareStatement(
                "CREATE TABLE tblUser ("
                +"user_id SERIAL PRIMARY KEY,"
                +"username VARCHAR(255) UNIQUE,"
                +"realname VARCHAR(255),"
                +"email VARCHAR(255),"
                +"salt BYTEA,"
                +"password BYTEA)"
            );
            db.mCreateProfileTable = db.mConnection.prepareStatement(
                "CREATE TABLE tblProfile ("
                +"profile_id SERIAL PRIMARY KEY,"
                +"profile_text VARCHAR(500),"
                +"user_id INTEGER,"
                +"FOREIGN KEY (user_id) REFERENCES tblUser (user_id))"
            );
            db.mCreateMessageTable = db.mConnection.prepareStatement(
                "CREATE TABLE tblMessage (message_id SERIAL PRIMARY KEY,"
                +"subject VARCHAR(50),"
                +"message VARCHAR(500),"
                +"username VARCHAR(255),"
                +"createTime VARCHAR(50),"
                +"vote INTEGER,"
                +"FOREIGN KEY (username) REFERENCES tblUser (username))"
            );
            db.mCreateCommentTable = db.mConnection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS tblComment ("
                +"comment_id SERIAL PRIMARY KEY,"
                +"username VARCHAR(255),"
                +"message_id INTEGER,"
                +"comment_text VARCHAR(255),"
                //Need to add creation date/time
                +"createTime VARCHAR(50),"
                +"FOREIGN KEY (username) REFERENCES tblUser (username),"
                +"FOREIGN KEY (message_id) REFERENCES tblMessage (message_id))"
            );
            db.mCreateDownVoteTable = db.mConnection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS tblDownVote ("
                +"user_id INTEGER,"
                +"message_id INTEGER,"
                +"FOREIGN KEY (user_id) REFERENCES tblUser (user_id),"
                +"FOREIGN KEY (message_id) REFERENCES tblMessage (message_id),"
                +"PRIMARY KEY (user_id, message_id))"
            );
            db.mCreateUpVoteTable = db.mConnection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS tblUpVote ("
                +"user_id INTEGER,"
                +"message_id INTEGER,"
                +"FOREIGN KEY (user_id) REFERENCES tblUser (user_id),"
                +"FOREIGN KEY (message_id) REFERENCES tblMessage (message_id),"
                +"PRIMARY KEY (user_id, message_id))"
            );
            db.mInsertUser = db.mConnection.prepareStatement("INSERT INTO tblUser VALUES (default, ?, ?, ?, ?, ?)");
            db.mSelectUnauthUserOne = db.mConnection.prepareStatement("SELECT * FROM tblUnauthUser WHERE username=?");
            db.mSelectUnauthUserAll = db.mConnection.prepareStatement("SELECT * FROM tblUnauthUser");
            
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }
    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            //e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Create all tables: tblUser, tblMessage, tblComment, tblUpVote, tblDownVote.  
     * If it already exists, this will print an error
     */
    boolean createAllTables() {
        try {
            mCreateUnauthUserTable.execute();
            mCreateUserTable.execute();
            mCreateProfileTable.execute();
            mCreateMessageTable.execute();
            mCreateCommentTable.execute();
            mCreateDownVoteTable.execute();
            mCreateUpVoteTable.execute();
        } catch (SQLException e) {
            System.err.println("Table is already created");
            //e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /** Create table based on parameter given */
    boolean createTable(char action) {
        try {
            if (action == 'U') {            // tblUser
                mCreateUserTable.execute();
                mCreateProfileTable.execute();  // following tables rely on tblUser
                mCreateMessageTable.execute();
                mCreateCommentTable.execute();
                mCreateUpVoteTable.execute();
                mCreateDownVoteTable.execute();
            } else if (action == 'a') {
                mCreateUnauthUserTable.execute();
            } else if (action == 'p') {     // tblProfile
                mCreateProfileTable.execute();
            } else if (action == 'm') {     // tblMessage
                mCreateMessageTable.execute();
                mCreateCommentTable.execute(); // following tables rely on tblMessage
                mCreateUpVoteTable.execute();
                mCreateDownVoteTable.execute();
            } else if (action == 'c') {     // tblComment
                mCreateCommentTable.execute();
            } else if (action == 'u') {     // tblUpVote
                mCreateUpVoteTable.execute();
            } else if (action == 'd') {     // tblDownVote
                mCreateDownVoteTable.execute();
            } else {
                System.err.println("Invalid input for creating table.");
                System.err.println("Options are: [U]ser, [p]rofile, [m]essage, [c]omment, [d]ownvote, [u]pvote");
                return false;
            }    
        } catch (SQLException e) {
            System.err.println("Table is already created. Error: " + e);
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Remove tblData from the database.  If it does not exist, this will print
     * an error.
     */
    boolean dropTable(String table) {
        Statement stmt = null;        
        try {
            stmt = mConnection.createStatement();
            String sql = "DROP TABLE " + table + " CASCADE";
            stmt.executeUpdate(sql);
            //mDropTable.setString(1, table);
            //mDropTable.execute();
        } catch (SQLException e) {
            System.err.println("There is no table to drop");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    boolean dropAllTables() {
       // try {
            boolean result;
            String[] tables = {"tblUpVote", "tblDownVote", "tblComment", "tblProfile", "tblMessage", "tblUser", "tblUnauthUser"};
            for (int i = 0; i < tables.length; i++) {
                result = dropTable(tables[i]);
                if (result == false) {
                    return false;
                }
            }
        return true;
    }

    public boolean selectUnauthUserAll() {
        try {
            ResultSet rs = mSelectUnauthUserAll.executeQuery();
            int columnsNumber = 4;
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print("   ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    boolean authorizeUser(String username) {
        try {
            Password pw = new Password();

            mSelectUnauthUserOne.setString(1, username);
            System.out.println(mSelectUnauthUserOne.toString());
            ResultSet rs = mSelectUnauthUserOne.executeQuery();
            System.out.println("got user");
            
            if (rs.next())
            {
                mInsertUser.setString(1, username);
                System.out.println("1");
                mInsertUser.setString(2, rs.getString("realname"));
                System.out.println("2");
                mInsertUser.setString(3, rs.getString("email"));
                // TODO: need to get the salt from JavaPasswordSecurity.java
                System.out.println("before salt");
                byte [] salt = pw.getSalt();
                System.out.println("salt: "+salt);
                String password = pw.getPassword();
                System.out.println("password: "+password);
                byte [] saltedPassword = pw.encryptPw (password, salt);
                mInsertUser.setBytes(4,salt);
                mInsertUser.setBytes(5, saltedPassword);
                mInsertUser.executeUpdate();
                // TODO: send email with username and unsalted password
            }
            else
            {
                System.out.println("username not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
  