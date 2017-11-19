package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import javax.net.ssl.HttpsURLConnection;

/**
 * Main Activity Will handle all the things that the main page ("Message Page") does, like displaying messages,
 * When the User is logged in, the menu displays Create Buzz, change password, and logout
 */
public class MainActivity extends AppCompatActivity{

    /**
     * mData holds the data we get from Volley
     */
    ArrayList<Datum> mData = new ArrayList<>();
    /**
     * mMessageData holds data for all the message objects
     */
    ArrayList<Message> mMessageData = new ArrayList<>();

    Menu optionsMenu;
    //Adapter for the message object
    RecyclerView.Adapter adapter;

    // enter into the browser to understand what the android app is parsing in the GET request.
    String url = "https://quiet-taiga-79213.herokuapp.com/messages";

    String  check = "";

    //This method handles creating the menu icon, it toggles the visibility depending on if they are logged in or not
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        String username = ApplicationWithGlobals.getUsername();
        int key = ApplicationWithGlobals.getKey();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        optionsMenu = menu;
        return true;
    }

    /**
     * This method is called first everytime this activity is  called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Sets up the Recycler adapter to display messages
        RecyclerView rv = (RecyclerView) findViewById(R.id.message_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        adapter = new MessageRecyclerAdapter(mMessageData, 0);
        rv.setAdapter(adapter);

        String username = ApplicationWithGlobals.getUsername();
        int key  = ApplicationWithGlobals.getKey();
        Log.d("Username/key", username + ", " + key + ".");

        refreshList();      // populate RecyclerView with initial set of buzzes.
    }

    /**
     * What happens when the user selects an item in the menu.
     * The only item right now is the option to create a new buzz.
     * @param item from the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.create_buzz_settings) {

            if(ApplicationWithGlobals.getUsername() != "error" && ApplicationWithGlobals.getKey() !=0){
                Intent i = new Intent(getApplicationContext(), CreateBuzzActivity.class);
                i.putExtra("topLabel", "Create a buzz:");
                startActivityForResult(i, 789); // 789 is the number that will come back to us
                return true;
            }
            else{
                Toast.makeText(MainActivity.this, "Error: User not Logged In", Toast.LENGTH_LONG).show();
            }
        }

        if(id == R.id.logout_settings)
        {
            Intent i = new Intent(getApplicationContext(), logout.class);
            i.putExtra("topLabel","Are you sure you want to logout?");
            startActivityForResult(i,4);
        }

        if(id == R.id.profile_settings){
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            String username = ApplicationWithGlobals.getUsername();
            i.putExtra("otherUser", username);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    // refresh the list of buzzes.
    // refreshList() only queues the request to populate.
    // populateMessageFromVolley() does the real work: parsing the server response and updating the adapter.
    public void refreshList()
    {
        String url = "https://quiet-taiga-79213.herokuapp.com/messages";
        URL urlType = null;
        try {
            urlType = new URL("https://quiet-taiga-79213.herokuapp.com/messages");
        }catch(MalformedURLException e){
            //wat
        }
        /////
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) urlType.openConnection();
        }catch(IOException e){
            //wat
        }
        //if(conn !=null) {
        long currentTime = System.currentTimeMillis();
        long expires = conn.getHeaderFieldDate("Expires", currentTime);
        long lastModified = conn.getHeaderFieldDate("Last-Modified", currentTime);
        long lastUpdateTime = 0;
        //}

        // lastUpdateTime represents when the cache was last updated.
        if (lastModified < lastUpdateTime) {
            //skip update
            Toast.makeText(MainActivity.this,"hit skip update", Toast.LENGTH_LONG).show();
        } else {
            // Parse update
            Toast.makeText(MainActivity.this,"hit do update", Toast.LENGTH_LONG).show();
            lastUpdateTime = lastModified;
        }
        /////
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        populateMessageFromVolley(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("jnm219", "StringRequest() failed: " + error.getMessage());
                    }
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * populateMessageFromVolley parses the response string and extracts necessary data to make datums.
     * @param response is a string returned from a GET request.
     */
    private void populateMessageFromVolley(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray json = new JSONArray(jsonObject.getString("mMessageData"));

            mMessageData.clear();      // get rid of existing buzzes
            for (int i = 0; i < json.length(); ++i) {
                int mUserId = json.getJSONObject(i).getInt("mId");
                String mTitle = json.getJSONObject(i).getString("mSubject");
                String mMessage = json.getJSONObject(i).getString("mMessage");
                String mCreateTime = json.getJSONObject(i).getString("mCreateTime");
                int mVotes = json.getJSONObject(i).getInt("mVotes");
                Log.d("Liger", json.getJSONObject(i).toString());
                String mUsername = json.getJSONObject(i).getString("mUsername");
                mMessageData.add(new Message(mUserId, mTitle, mMessage, mCreateTime,mVotes,mUsername));
            }
            adapter.notifyDataSetChanged();
        } catch (final JSONException e) {
            Log.d("Liger", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("Liger", "Successfully parsed JSON file.");
    }

    //Calls login user to refresh the login
    private void refreshLogin()
    {
        //This finds the menuItem for creating a buzz and makes it visible
        MenuItem buzz = (MenuItem) optionsMenu.findItem(R.id.create_buzz_settings);
        buzz.setVisible(true);

        //Finds the logout button and makes it hidden
        MenuItem logout = (MenuItem) optionsMenu.findItem(R.id.logout_settings);
        logout.setVisible(true);
    }
    /**
     * @param requestCode tells you which activity needs to be responded to.
     * @param resultCode holds the result ok if the activity succeed.
     * @param intent gets sent from the activity. The only activity right now is the CreateBuzzActivity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        //Toast.makeText(MainActivity.this,"RequestCode: "+ requestCode+ "ResultCode: "+resultCode, Toast.LENGTH_LONG).show();
        // Json request for Create Buzz
        if (requestCode == 789) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                String url = "https://quiet-taiga-79213.herokuapp.com/messages";
                // POST to backend server. Modified version of:
                // https://www.itsalif.info/content/android-volley-tutorial-http-get-post-put
                Map<String, String> jsonParams = new HashMap<String, String>();
                final String resultSubject = intent.getStringExtra("resultSubject");
                final String resultMessage = intent.getStringExtra("resultMessage");


                // add the data collected from user into map which gets made into a JSONObject
                jsonParams.put("mSubject", resultSubject);
                jsonParams.put("mMessage", resultMessage);
                jsonParams.put("mUsername",ApplicationWithGlobals.getUsername());
                jsonParams.put("mKey",ApplicationWithGlobals.getKey()+"");
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    check = response.getString("mMessageData");
                                    if(check == "false")
                                    {
                                        refreshLogout();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("jnm219", "got response");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("jnm219", "JsonObjectRequest() failed: " + error.getMessage());
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("User-agent", System.getProperty("http.agent"));
                        return headers;
                    }
                };
                //Toast.makeText(MainActivity.this,"Check: "+check, Toast.LENGTH_LONG).show();
                if(check != "false") {
                    VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
                }
                else{
                    ApplicationWithGlobals.setKey(0);
                    ApplicationWithGlobals.setUsername("error");
                    refreshLogout();
                }
                finish();
                startActivity(getIntent());

            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(MainActivity.this, "Buzz Canceled", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(MainActivity.this,"Error Creating Buzz", Toast.LENGTH_LONG).show();
            }
        }


        //Json request for the logout functionality
        if(requestCode == 4) {
            if(resultCode == RESULT_OK) {
                String url = "https://quiet-taiga-79213.herokuapp.com/logout";

                Map<String, String> jsonParams = new HashMap<String, String>();
                jsonParams.put("mUsername", ApplicationWithGlobals.getUsername());

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response){
                                Log.e("Liger","got response");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Liger", "JsonObjectRequest() failed: " + error.getMessage());

                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("User-agent", System.getProperty("http.agent"));
                        return headers;
                    }
                };
                //Volley request to logout a user from the server
                VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
                refreshLogout();
            }
            else{
                Toast.makeText(MainActivity.this,"Logout Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This method logs out a user by getting rid of the username and key in local storage, and changes the view back to logged out
     */
    private void refreshLogout() {
        ApplicationWithGlobals.setKey(0);
        ApplicationWithGlobals.setUsername("Error");

        Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
        Toast.makeText(MainActivity.this,"Logout Successful", Toast.LENGTH_LONG).show();
        //These two lines reload the MainActivity from itself, essentially calling OnCreate again
        //necessary in order to get rid of all the messages from the main activity
        finish();
        startActivity(i);
    }

}
