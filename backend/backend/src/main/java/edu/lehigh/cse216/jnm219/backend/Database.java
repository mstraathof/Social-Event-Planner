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
import java.util.TimeZone;

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
    private PreparedStatement mInsertOneMessage;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;
    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mInsertUser;
    private PreparedStatement mUpdateVote;
    private PreparedStatement mSelectOneUser;
    private PreparedStatement mSelectAllUser;
    private PreparedStatement mUpdateUser;
    private PreparedStatement mInsertComment;
    private PreparedStatement mSelectAllComment;
    private PreparedStatement mInsertLikes;
    private PreparedStatement mDeleteLikes;
    private PreparedStatement mCountLikes;
    private PreparedStatement mInsertDislikes;
    private PreparedStatement mDeleteDislikes;
    private PreparedStatement mCountDislikes;
    private PreparedStatement mGetSalt;
    private PreparedStatement mGetUserId;
    private PreparedStatement mInsertProfile;
    private PreparedStatement mSelectOneProfile;
    private PreparedStatement mSelectUserMessage;
    private PreparedStatement mSelectUserComment;
    private PreparedStatement mSearchLikes;
    private PreparedStatement mSearchDislikes;
    private PreparedStatement mUpdateMessageVote;
    private PreparedStatement mGetVote;
    private PreparedStatement mUpdateProfile;
    private PreparedStatement mGetLikedMessage;
    private PreparedStatement mGetDisLikedMessage;
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
            db.mInsertOneMessage = db.mConnection.prepareStatement("INSERT INTO tblMessage VALUES (default, ?, ?, ?, ?, ?)");
            db.mSelectAllMessage = db.mConnection.prepareStatement("SELECT * FROM tblMessage ORDER BY createTime DESC");
            db.mSelectOneMessage = db.mConnection.prepareStatement("SELECT * from tblMessage WHERE message_id=?");
            //db.mUpdateOneMessage = db.mConnection.prepareStatement("UPDATE tblMessage SET subject = ?, message = ? WHERE id = ?");
            //db.mUpdateVote = db.mConnection.prepareStatement("UPDATE tblMessage SET votes = votes + ? WHERE id = ?");
            db.mInsertUser = db.mConnection.prepareStatement("Insert into tblUnauthUser Values (?, ?, ?)");
            db.mSelectOneUser = db.mConnection.prepareStatement("Select * from tblUser where username=? and password=?");
            db.mSelectAllUser = db.mConnection.prepareStatement("Select * from tblUser");
            db.mUpdateUser = db.mConnection.prepareStatement("Update tblUser Set Password=?, salt=? where username=?");
            db.mInsertComment = db.mConnection.prepareStatement("Insert into tblComment values (default, ?, ?, ?, ?)");
            db.mSelectAllComment = db.mConnection.prepareStatement("select * from tblComment where message_id=? ");
            db.mInsertLikes = db.mConnection.prepareStatement("Insert into tblUpVote values (?,?)");
            db.mDeleteLikes = db.mConnection.prepareStatement("Delete from tblUpVote where username=? and message_id=?");
            db.mCountLikes = db.mConnection.prepareStatement("select count(message_id) from tblUpVote where message_id=?");
            db.mSearchLikes = db.mConnection.prepareStatement("select * from tblUpVote where username=? and message_id=?");
            db.mInsertDislikes = db.mConnection.prepareStatement("Insert into tblDownVote values (?,?)");
            db.mDeleteDislikes = db.mConnection.prepareStatement("Delete from tblDownVote where username=? and message_id=?");
            db.mCountDislikes = db.mConnection.prepareStatement("select count(message_id) from tblDownVote where message_id=?");
            db.mSearchDislikes = db.mConnection.prepareStatement("select * from tblDownVote where username=? and message_id=?");
            db.mGetSalt=db.mConnection.prepareStatement("select salt from tblUser where username=?");
            db.mGetUserId=db.mConnection.prepareStatement("select user_id from tblUser where username=?");
            db.mInsertProfile = db.mConnection.prepareStatement("Insert into tblProfile values (?,?)"); 
            db.mSelectOneProfile = db.mConnection.prepareStatement("select username, realname, email, profile_text from tblUser natural join tblProfile where username=?");
            db.mSelectUserMessage = db.mConnection.prepareStatement("select * from tblMessage where username=?");
            db.mSelectUserComment = db.mConnection.prepareStatement("select * from tblComment where username=?");
            db.mUpdateMessageVote = db.mConnection.prepareStatement("update tblMessage set vote=? where message_id=?");
            db.mGetVote = db.mConnection.prepareStatement("select vote from tblMessage where message_id=?");
            db.mUpdateProfile = db.mConnection.prepareStatement("update tblProfile Set profile_text=? where username=?");
            db.mGetLikedMessage=db.mConnection.prepareStatement("select message_id from tblUpVote where username=?");
            db.mGetDisLikedMessage=db.mConnection.prepareStatement("select message_id from tblDownVote where username=?");
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
     
    int insertOneMessage(String subject, String message, String username) {
        int count = 0;
        int votes = 0;
        try {
            mInsertOneMessage.setString(1, subject);
            mInsertOneMessage.setString(2, message);
            //int userId=getUserId(username);
            mInsertOneMessage.setString(3, username);
            SimpleDateFormat date= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            String strDate=date.format(cal);
            mInsertOneMessage.setString(4,strDate);
            mInsertOneMessage.setInt(5, votes);// create count votes method
            count += mInsertOneMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(count == 0)
                return -1;
        else
            return count;
    }
    int getUserId(String username)
    {
        int id=0;
        try
        {
            mGetUserId.setString(1,username);
            ResultSet rs = mGetUserId.executeQuery();
            if (rs.next())
            {
                id=rs.getInt("user_id");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowMessage> selectAllMessage() {
        ArrayList<RowMessage> res = new ArrayList<RowMessage>();
        try {
            ResultSet rs = mSelectAllMessage.executeQuery();
            while (rs.next()) {

                res.add(new RowMessage(rs.getInt("message_id"), rs.getString("subject"), rs.getString("message"), rs.getString("username"), rs.getString("createTime"),rs.getInt("vote")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    ArrayList<RowComment> selectAllComment(int mId) {
        ArrayList<RowComment> res = new ArrayList<RowComment>();
        try {
            mSelectAllComment.setInt(1, mId);
            ResultSet rs = mSelectAllComment.executeQuery();
            while (rs.next()) {

                res.add(new RowComment(rs.getInt("comment_id"), rs.getString("username"), rs.getInt("message_id"), rs.getString("comment_text"), rs.getString("createTime")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    ArrayList<RowUser> selectAllUser() {
        ArrayList<RowUser> res = new ArrayList<RowUser>();
        try {
            ResultSet rs = mSelectAllUser.executeQuery();
            while (rs.next()) {

                res.add(new RowUser(rs.getInt("user_id"),rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"),rs.getBytes("password")));
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
    RowMessage selectOneMessage(int id) {
        RowMessage res = null;
        try {
            mSelectOneMessage.setInt(1, id);
            ResultSet rs = mSelectOneMessage.executeQuery();
            if (rs.next()) {
                res = new RowMessage(rs.getInt("message_id"), rs.getString("subject"), rs.getString("message"),rs.getString("username"), rs.getString("createTime"),rs.getInt("vote"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    RowProfile selectProfile(String username)
    {
        RowProfile res=null;
        try{
            //int userId=getUserId(username);
            System.out.println(username);
            mSelectOneProfile.setString(1,username);
            ResultSet rs = mSelectOneProfile.executeQuery();
            if (rs.next()) {
                System.out.println("got in to sql try");
                res= new RowProfile(rs.getString("username"),rs.getString("realname"),rs.getString("email"),rs.getString("profile_text"));
            }
        } catch (SQLException e) {
            System.out.println("failed");
            e.printStackTrace();
        }
        return res;
    }
    ArrayList <RowMessage> selectMessageLiked(String username)
    {
        ArrayList<RowMessage> res = new ArrayList<RowMessage>();
        int mId=0;
        try {
            mGetLikedMessage.setString(1,username);
            ResultSet rs = mGetLikedMessage.executeQuery();
            while (rs.next())
            {
                mId=rs.getInt("message_id");
                mSelectOneMessage.setInt(1,mId);
                ResultSet rs2=mSelectOneMessage.executeQuery();
                if (rs2.next())
                {
                    res.add(new RowMessage(rs.getInt("message_id"), rs.getString("subject"), rs.getString("message"), rs.getString("username"), rs.getString("createTime"),rs.getInt("vote")));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }
    ArrayList <RowMessage> selectMessageDisliked(String username)
    {
        ArrayList<RowMessage> res = new ArrayList<RowMessage>();
        int mId=0;
        try{
            mGetDisLikedMessage.setString(1,username);
            ResultSet rs = mGetDisLikedMessage.executeQuery();
            while (rs.next())
            {
                mId=rs.getInt("message_id");
                mSelectOneMessage.setInt(1,mId);
                ResultSet rs2=mSelectOneMessage.executeQuery();
                if (rs2.next())
                {
                    res.add(new RowMessage(rs.getInt("message_id"), rs.getString("subject"), rs.getString("message"), rs.getString("username"), rs.getString("createTime"),rs.getInt("vote")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }
    ArrayList <RowMessage> selectUserMessage(String username)
    {
        ArrayList<RowMessage> res = new ArrayList<RowMessage>();
        try{
            mSelectUserMessage.setString(1,username);
            ResultSet rs = mSelectUserMessage.executeQuery();
            while (rs.next()) 
            {
                res.add(new RowMessage(rs.getInt("message_id"), rs.getString("subject"), rs.getString("message"), rs.getString("username"), rs.getString("createTime"),rs.getInt("vote")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    ArrayList <RowComment> selectUserComment(String username)
    {
        ArrayList<RowComment> res = new ArrayList<RowComment>();
        try{
            mSelectUserComment.setString(1,username);
            ResultSet rs = mSelectUserComment.executeQuery();
             while (rs.next()) {
                res.add(new RowComment(rs.getInt("comment_id"), rs.getString("username"), rs.getInt("message_id"), rs.getString("comment_text"), rs.getString("createTime")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    byte [] getUserSalt (String username)
    {
        System.err.println("HELLO!");
        byte [] salt = null;
        int i=0;
        try {
            System.out.println(username);
            mGetSalt.setString (1,username);
            ResultSet rs=mGetSalt.executeQuery();
            if (rs.next())
            {
                System.out.println("check 1");
                salt= rs.getBytes("salt");
            }
            System.out.println("Check 2");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(salt);
        return salt;
    }
    boolean selectOneUser(String username, byte [] password) 
    {
        System.out.println("Got into select one user");
        boolean check=false;
        try {
            mSelectOneUser.setString(1, username);
            mSelectOneUser.setBytes(2, password);
            ResultSet rs = mSelectOneUser.executeQuery();
            if (!rs.next()) 
            {
                System.out.println("this didn't find a user");
                check=false;
            }
            else
            {
                System.out.println("found a user");
                check= true;
            }
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
            return false;
        }
        return check;
    }
    boolean insertUser(String username,String realname,String email)
    {
        int rs=0;
        try {
            mInsertUser.setString(1,username);
            mInsertUser.setString(2,realname);
            mInsertUser.setString(3,email);
            rs += mInsertUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    boolean insertComment (String username, int mId, String comment)
    {
        int rs=0;
        try {
            mInsertComment.setString(1,username);
            mInsertComment.setInt(2,mId);
            mInsertComment.setString(3,comment);
            SimpleDateFormat date= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            String strDate=date.format(cal);
            mInsertComment.setString(4,strDate);
            rs +=mInsertComment.executeUpdate();
        } catch (SQLException e)
        {
            System.out.println("execute query failed");
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
    boolean updateUpVote(String username, int messageId) 
    {
       int rsVote=0;
       int rsMesage=0;
       ResultSet count=null;
       int voteCount=0;
       ResultSet res=null;
        try {
            mGetVote.setInt(1,messageId);
            count=mGetVote.executeQuery();
            if (count.next())
            {
                voteCount=count.getInt("vote");
            }
            mSearchLikes.setString(1,username);
            mSearchLikes.setInt(2,messageId);
            res =mSearchLikes.executeQuery();
            if (res.next())
            {
                mDeleteLikes.setString(1,username);
                mDeleteLikes.setInt(2,messageId);
                rsVote =mDeleteLikes.executeUpdate();
                voteCount-=1;
            }
            else
            {
                mInsertLikes.setString(1,username);
                mInsertLikes.setInt(2,messageId);
                rsVote =mInsertLikes.executeUpdate();
                voteCount+=1;
            }
            mUpdateMessageVote.setInt(1,voteCount);
            mUpdateMessageVote.setInt(2,messageId);
            rsMesage +=mUpdateMessageVote.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    /**
     * Decreases the number of votes for a given post by one
     * 
     * @param id The id of the row being altered
     * 
     * @return The data of the newly altered row, or null if the ID was invalid
     */
    boolean updateDownVote(String username, int messageId) {
       
       int rsVote=0;
       int rsMesage=0;
       int voteCount=0;
       ResultSet count=null;
       ResultSet res=null;
        try {
            mGetVote.setInt(1,messageId);
            count=mGetVote.executeQuery();
            if (count.next())
            {
                voteCount=count.getInt("vote");
            }
            mSearchDislikes.setString(1,username);
            mSearchDislikes.setInt(2,messageId);
            res =mSearchDislikes.executeQuery();
            if (res.next())
            {
                mDeleteDislikes.setString(1,username);
                mDeleteDislikes.setInt(2,messageId);
                rsVote =mDeleteDislikes.executeUpdate();
                voteCount+=1;

            }
            else
            {
                mInsertDislikes.setString(1,username);
                mInsertDislikes.setInt(2,messageId);
                rsVote =mInsertDislikes.executeUpdate();
                voteCount-=1;
            }
            mUpdateMessageVote.setInt(1,voteCount);
            mUpdateMessageVote.setInt(2,messageId);
            rsMesage +=mUpdateMessageVote.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;

    }
    boolean insertProfile(String username)
    {
        int rs=0;
        String profile="You can edit your profile here";
        try {
            //int userId=getUserId(username);
            mInsertProfile.setString(1,username);
            mInsertProfile.setString(2,profile);
            rs+=mInsertProfile.executeUpdate();
        }catch(SQLException e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }
    boolean updateProfile(String username, String profile)
    {
        int rs=0;
        try {
            //int userId=getUserId(username);
            mUpdateProfile.setString(1,profile);
            mUpdateProfile.setString(2,username);
            rs+=mUpdateProfile.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    boolean updatePassword (String username, byte [] password, byte [] salt)
    {
        int rs=0;
        try {
            mUpdateUser.setBytes(1,password);
            mUpdateUser.setBytes(2,salt);
            mUpdateUser.setString(3,username);
            rs+=mUpdateUser.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

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