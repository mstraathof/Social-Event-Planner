package edu.lehigh.cse216.jnm219.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

public class DatabaseTest extends TestCase
{
    /**
     * Test the insertRow method of Database
     */
    public void testInsertRow()
    {
        Spark.port(App.getIntFromEnv("PORT", 5432));
        Database dbt = Database.getDatabase(2);
        int check = dbt.insertRow("subject", "message");
        assertEquals(check, 1);
    }

    /**
     * Test the selectAll method of Database
     */
    public void testSelectAll()
    {
        Spark.port(App.getIntFromEnv("PORT", 5432));
        Database dbt = Database.getDatabase(2);
        dbt.insertRow("s", "m");
        ArrayList<RowData> list = dbt.selectAll();
        int listLength = list.size();
        assertEquals(true, list.get(listLength-1).mSubject.equals("s"));
    }

    /**
     * Test the selectOne method of Database
     */
    public void testSelectOne()
    {
        Spark.port(App.getIntFromEnv("PORT", 5432));
        Database dbt = Database.getDatabase(2);
        dbt.insertRow("s", "m");
        ArrayList<RowData> list = dbt.selectAll();
        int listLength = list.size();
        int id = list.get(listLength-1).mId;
        assertEquals(id, dbt.selectOne(id).mId);
    }
    
    /**
     * Test the upVote method of Database
     */
    public void testupVote()
    {
        Spark.port(App.getIntFromEnv("PORT", 5432));
        Database dbt = Database.getDatabase(2);
        dbt.insertRow("s", "m");
        assertEquals(false, -1 == dbt.upVote(1, 1));
    }
    /**
     * Test the downVote method of Database
     */
    public void testdownVote()
    {
        Spark.port(App.getIntFromEnv("PORT", 5432));
        Database dbt = Database.getDatabase(2);
        dbt.insertRow("s", "m");
        assertEquals(false, -1 == dbt.upVote(1, -1));
    }
}