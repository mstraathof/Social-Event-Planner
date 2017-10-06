package edu.lehigh.cse216.jnm219.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URISyntaxException;
// Imports for time functionality
import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

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
    private PreparedStatement mSelectAllMessage;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOneMessage;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;
    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mInsertUser;
    private PreparedStatement mUpdateVote;
    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Give the Database object a connection, fail if we cannot get one
     * Must be logged into heroku on a local computer to be able to use mvn heroku:deploy
     */
    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL"); // Url for heroku database connection
        return DriverManager.getConnection(dbUrl);
    }

    /**
     * Give the Database object a connection, fail if we cannot get one
     * Must be logged into heroku on a local computer to be able to use mvn heroku:deploy
     * Used for testing only
     */
    private static Connection getConnection2() throws URISyntaxException, SQLException {
        // Main heroku server connect
        //return DriverManager.getConnection("jdbc:postgresql://ec2-107-21-109-15.compute-1.amazonaws.com:5432/dfjhqhen0vfnm?user=wmptnnamvihvzv&password=021c55db34a371a345a4e8279d144dde484f6e1455b10b217525f6885e363433&sslmode=require");
        // Test heroku server connect
        return DriverManager.getConnection("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param int connectionType = type of connection. 1 for normal server and 2 for test server connection.
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(int connectionType) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Connection conn;
            if(connectionType == 1)
            {
                conn = getConnection();
            }
            else if(connectionType == 2)
            {
                conn = getConnection2();
            }
            else
            {
                conn = getConnection();
            }
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
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

            // Standard CRUD operations
            //db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblMessage VALUES (default, ?, ?, ?, ?)");
            db.mSelectAllMessage = db.mConnection.prepareStatement("SELECT * FROM tblMessage ORDER BY createTime DESC");
            db.mSelectOneMessage = db.mConnection.prepareStatement("SELECT * from tblMessage WHERE id=?");
            //db.mUpdateOneMessage = db.mConnection.prepareStatement("UPDATE tblMessage SET subject = ?, message = ? WHERE id = ?");
            db.mUpdateVote = db.mConnection.prepareStatement("UPDATE tblMessage SET votes = votes + ? WHERE id = ?");
            db.mInsertUser = db.mConnection.prepareStatement("Insert into tblUser Values (default, ?, ?, ?, ?, ?)");
           /* db.mSelectOneUser = db.mConnection.prepareStatement("Select * from tblUser where username=?");
            db.mSelectAllUser = db.mConnection.prepareStatement("Select * from tblUser");
            db.mUpdateUser = db.mConnection.prepareStatement("Update tblUser Set Password=? where username=? and email=?");
            db.mInsertComment = db.mConnection.prepareStatement("Insert into tblComment values (defalut, ?, ?, ?, ?");
            db.mSelectAllComment = db.mConnection.prepareStatement("select * from tblComment where message_id=? ");
            db.mInsertLikes = db.mConnection.prepareStatement("");
            db.mUpdateLikes = db.mConnection.prepareStatement("");
            db.mCountLikes = db.mConnection.prepareStatement("");
            db.mSelectOneLikes = db.mConnection.prepareStatement("");
            db.mInsertDislikes = db.mConnection.prepareStatement("");
            db.mUpdateDislikes = db.mConnection.prepareStatement("");
            db.mCountDislikes = db.mConnection.prepareStatement("");
            db.mSelectOneDislikes = db.mConnection.prepareStatement(""); */
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
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(String subject, String message) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            mInsertOne.setInt(3, 0);
            mInsertOne.setString(4, new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));
            mInsertOne.setString(5, new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(count == 0)
                return -1;
        else
            return count;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAllMessage.executeQuery();
            while (rs.next()) {

                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("votes"), rs.getString("createTime"), rs.getString("modifyTime")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOneMessage.setInt(1, id);
            ResultSet rs = mSelectOneMessage.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("votes"), rs.getString("createTime"), rs.getString("modifyTime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    boolean insertUser(String username,String realname,String email,int salt, String pw )
    {
        RowData res=null;
        try {
            mInsertUser.setString(1,username);
            mInsertUser.setString(2,realname);
            mInsertUser.setString(3,email);
            mInsertUser.setInt(4,salt);
            mInsertUser.setString(5,pw);
            ResultSet rs = mInsertUser.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Increases the number of votes for a given post by one
     * 
     * @param id The id of the row being altered
     * 
     * @return The data of the newly altered row, or null if the ID was invalid
     */
    int upVote(int id, int voteChange) {
        int res = -1;
        try {
            mUpdateVote.setInt(1, voteChange);
            mUpdateVote.setInt(2, id);
           res = mUpdateVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Decreases the number of votes for a given post by one
     * 
     * @param id The id of the row being altered
     * 
     * @return The data of the newly altered row, or null if the ID was invalid
     */
    int downVote(int id, int voteChange) {
        int res = -1;
        try {
            //mUpdateVote.setInt(1, voteChange);
            //mUpdateVote.setInt(2, id);
            res = mUpdateVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     * 
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

     * Update the message for a row in the database
     * @param subject The new subject contents
     * @param id The id of the row to update
     * @param message The new message contents
     * @return The number of rows that were updated.  -1 indicates an error.
    int updateOne(int id,String subject, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, subject);
            mUpdateOne.setString(2, message);
            mUpdateOne.setInt(3, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    */
}