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

public class Database {
 
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    //private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
   // private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    //private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
   // private PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    //private PreparedStatement mUpdateOne;

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
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
 
    //public static class RowData {
        /**
         * The ID of this row of the database
         */
        //int mId;
        /**
         * The subject stored in this row
         */
       // String mSubject;
        /**
         * The message stored in this row
         */
        //String mMessage;
        /**
         * The votes stored in this row
         */
        //int mVotes;
        /**
         * The date created stored in this row
         */
       // String mCreateTime;
        /**
         * The time modified stored in this row
         */
        //String mModifyTime;
        /**
         * Construct a RowData object by providing values for its fields
         */
         /*
        public RowData(int id, String subject, String message) {
            mId = id;
            mSubject = subject;
            mMessage = message;
        }
    }
*/
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
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
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
            db.mCreateUserTable = db.mConnection.prepareStatement(
                "CREATE TABLE tblUser (user_id SERIAL PRIMARY KEY,"
                +"username VARCHAR(255) not null,"
                +"realname VARCHAR(255) not null,"
                +"email VARCHAR(255) not null,"
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
                +"subject VARCHAR(50) not null,"
                +"user_id INTEGER, message VARCHAR(500) not null,"
                +"createTime VARCHAR(50) not null,"
                +"FOREIGN KEY (user_id) REFERENCES tblUser (user_id))"
            );
            db.mCreateCommentTable = db.mConnection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS tblComment ("
                +"comment_id SERIAL PRIMARY KEY,"
                +"user_id INTEGER,"
                +"message_id INTEGER,"
                +"comment_text VARCHAR(255) not null,"
                //Need to add creation date/time
                +"createTime VARCHAR(50) not null,"
                +"FOREIGN KEY (user_id) REFERENCES tblUser (user_id),"
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
            //db.mDropTable = db.mConnection.prepareStatement("DROP TABLE ?");

            // Standard CRUD operations
            /*
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT id, subject, votes FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ? WHERE id = ?");
            */
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
            } else if (action == 'p') {     // tblProfile
                mCreateProfileTable.execute();
            } else if (action == 'm') {     // tblMessage
                mCreateMessageTable.execute();
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
            String sql = "DROP TABLE " + table;
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
            String[] tables = {"tblUpVote", "tblDownVote", "tblComment", "tblProfile", "tblMessage", "tblUser"};
            for (int i = 0; i < tables.length; i++) {
                result = dropTable(tables[i]);
                if (result == false) {
                    return false;
                }
            }
            /*
            String[] tables = {"tblUser", "tblMessage", "tblProfile", "tblComment", "tblUpVote", "tblDownVote"}; 
            for (int i = 0; i < tables.length; i++) {
                dropTable(tables[i]);
                mDropTable.execute();
            }
            */
        //} catch (SQLException e) {
            //System.err.println("Error occured while dropping tables: "+ e);
           // e.printStackTrace();
            //return false;
        //}
        return true;
    }

    /**
     * Create tblUser.  If it already exists, this will print an error
     */
    boolean createUserTable() {
        try {
            //mCreateTable.execute();
            mCreateUserTable.execute();
        } catch (SQLException e) {
            System.err.println("Table is already created");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Create tblMessage.  If it already exists, this will print an error
     */
    boolean createMessageTable() {
        try {
            //mCreateTable.execute();
            mCreateMessageTable.execute();
        } catch (SQLException e) {
            System.err.println("Table is already created");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Create tblComment.  If it already exists, this will print an error
     */
    boolean createCommentTable() {
        try {
            //mCreateTable.execute();
            mCreateCommentTable.execute();
        } catch (SQLException e) {
            System.err.println("Table is already created");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Create tblDownVote.  If it already exists, this will print an error
     */
    boolean createDownVoteTable() {
        try {
            //mCreateTable.execute();
            mCreateDownVoteTable.execute();
        } catch (SQLException e) {
            System.err.println("Table is already created");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Create tblUpVote.  If it already exists, this will print an error
     */
    boolean createUpVoteTable() {
        try {
            //mCreateTable.execute();
            mCreateUpVoteTable.execute();
        } catch (SQLException e) {
            System.err.println("Table is already created");
            //e.printStackTrace();
            return false;
        }
        return true;
    }
}

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
     /*
    int insertRow(String subject, String message) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    */
    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
     /*
    ArrayList<RowData> selectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), null));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
     /*
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
*/
    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
     /*
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
*/
    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
     /*
    int updateOne(int id, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
*/
  