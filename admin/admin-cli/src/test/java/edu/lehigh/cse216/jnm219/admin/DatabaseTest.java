package edu.lehigh.cse216.jnm219.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple Database.
 */
public class DatabaseTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DatabaseTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DatabaseTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testDatabase()
    {
        assertTrue( true );
    }
    /**
     * Making sure I get disconnected to the database
     */
    public void testDisconnect()
    {
        Database db = Database.getDatabase("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        assertTrue(db.disconnect());
       
    }
    /**
     * Test creating the table, if it creates an extra, it should return false
     */
    public void testCreateTable()
    {
        Database db = Database.getDatabase("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        db.dropTable();
        assertTrue(db.createTable());
        assertFalse(db.createTable());
        db.disconnect();
    }
    /**
     * Test dropping the table, if it drops nothing, it should return false
     */
    public void testDeleteTable()
    {
        Database db = Database.getDatabase ("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        assertTrue(db.dropTable());
        assertFalse(db.dropTable());
        db.createTable();
        db.disconnect();
    }
    
}
