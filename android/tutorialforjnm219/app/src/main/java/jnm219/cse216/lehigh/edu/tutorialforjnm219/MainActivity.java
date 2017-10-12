package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    /**
     * mData holds the data we get from Volley
     */
    ArrayList<Datum> mData = new ArrayList<>();
    /**
     * mMessageData holds data for all the message objects
     */
    ArrayList<Message> mMessageData = new ArrayList<>();

    int keySave = 0;

    /**
     * Global for the loginInfo object, key and id will be 0 when not logged in
     */
    LoginInfo mLoginInfo = new LoginInfo();

    Menu optionsMenu;
    //Adapter for the message object
    RecyclerView.Adapter adapter;

    // enter into the browser to understand what the android app is parsing in the GET request.
    String url = "https://quiet-taiga-79213.herokuapp.com/messages";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        String username = ApplicationWithGlobals.getUsername();
        int key = ApplicationWithGlobals.getKey();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        optionsMenu = menu;
        //case if the user is not logged in yet
        if(username == "error" || key == 0) {
            MenuItem login = (MenuItem) menu.findItem(R.id.login_settings);
            login.setVisible(true);

            MenuItem register = (MenuItem) menu.findItem(R.id.register_settings);
            register.setVisible(true);

            MenuItem buzz = (MenuItem) menu.findItem(R.id.create_buzz_settings);
            buzz.setVisible(false);

            //Finds the logout button and makes it hidden
            MenuItem logout = (MenuItem) optionsMenu.findItem(R.id.logout_settings);
            logout.setVisible(false);
        }
        //Case if the user is logged in
        else{
            MenuItem login = (MenuItem) menu.findItem(R.id.login_settings);
            login.setVisible(false);

            MenuItem register = (MenuItem) menu.findItem(R.id.register_settings);
            register.setVisible(false);

            MenuItem buzz = (MenuItem) menu.findItem(R.id.create_buzz_settings);
            buzz.setVisible(true);

            //Finds the logout button and makes it hidden
            MenuItem logout = (MenuItem) optionsMenu.findItem(R.id.logout_settings);
            logout.setVisible(true);
        }
        Toast.makeText(MainActivity.this, "Username: "+username+" Key: "+key, Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.message_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        adapter = new MessageRecyclerAdapter(mMessageData);
        rv.setAdapter(adapter);

        String username = ApplicationWithGlobals.getUsername();
        int key  = ApplicationWithGlobals.getKey();

        //If a user is logged in, the List of all the messages are displayed on the main page, other wise it is empty
        if(username != "Error" && key != 0) {
            refreshList();      // populate RecyclerView with initial set of buzzes.
        }
            //VolleySingleton.getInstance(this).addToRequestQueue(stringRequest); // add request to queue.
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
        if (id == R.id.login_settings){
            Intent i = new Intent(getApplicationContext(), login.class);
            i.putExtra("topLabel","Log In:");
            i.putExtra("username","Enter Username:");
            i.putExtra("password","Enter Password");
            startActivityForResult(i, 666);
            return true;
        }

        if(id == R.id.register_settings){
            Intent i = new Intent(getApplicationContext(), Register.class);
            i.putExtra("topLabel","Register for The Buzz");
            i.putExtra("username","Enter Username:");
            i.putExtra("password","Enter Password");
            startActivityForResult(i, 667);
            return true;
        }

        if(id == R.id.logout_settings)
        {
            Intent i = new Intent(getApplicationContext(), logout.class);
            i.putExtra("topLabel","Are you sure you want to logout?");
            startActivityForResult(i,4);
        }

        return super.onOptionsItemSelected(item);
    }

    // refresh the list of buzzes.
    // refreshList() only queues the request to populate.
    // populateMessageFromVolley() does the real work: parsing the server response and updating the adapter.
    private void refreshList()
    {
        String url = "https://quiet-taiga-79213.herokuapp.com/messages";
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
                Log.d("Liger", mUserId + ":" + mTitle + ":" + mMessage + ":");
                //String mUsername = json.getJSONObject(i).getString("mUsername");
                String mUsername = "Jack";
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

        //This finds the menuItem for login and makes it hidden
        MenuItem login = (MenuItem) optionsMenu.findItem(R.id.login_settings);
        login.setVisible(false);

        //This finds the menuItem for registering and makes it hidden
        MenuItem register = (MenuItem) optionsMenu.findItem(R.id.register_settings);
        register.setVisible(false);

        //Finds the logout button and makes it hidden
        MenuItem logout = (MenuItem) optionsMenu.findItem(R.id.logout_settings);
        logout.setVisible(true);

    }
    /**
     * This Route holds the data for a logged in user
     */
    private void loginUser(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);;
            int key = jsonObject.getInt("mLoginData");
            keySave = key;
            Toast.makeText(MainActivity.this,"Key2: "+key, Toast.LENGTH_LONG).show();
            mLoginInfo.mKey = keySave;
        } catch(final JSONException e){
            Log.d("Liger","Error Parsing JSONN file: "+e.getMessage());
            return;
        }
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
                jsonParams.put("mUsername",mLoginInfo.mUsername);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
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
                VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
                refreshList();
            }
            else{
                Toast.makeText(MainActivity.this,"Error Creating Buzz", Toast.LENGTH_LONG).show();
            }
        }

        //Json Request for the Login Screen
        if (requestCode == 666)
        {
            if(resultCode == RESULT_OK) {
                String url = "https://quiet-taiga-79213.herokuapp.com/login";

                Map<String, String> jsonParams = new HashMap<String, String>();
                final String resultUsername = intent.getStringExtra("resultUserName");
                final String resultPassword = intent.getStringExtra("resultPassword");

                jsonParams.put("mUsername",resultUsername);
                jsonParams.put("mPassword",resultPassword);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response){
                                int key = 0;
                                try {
                                    key = response.getInt("mLoginData");
                                    ApplicationWithGlobals.setUsername(resultUsername);
                                    ApplicationWithGlobals.setKey(key);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                keySave = key;
                                Toast.makeText(MainActivity.this,"Key2: "+key, Toast.LENGTH_LONG).show();
                                mLoginInfo.mKey = keySave;
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

                refreshLogin();
                refreshList();
                VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
            }
        }

        //Json Request for the Register Screen
        if(requestCode == 667)
        {
            if(resultCode == RESULT_OK) {
                String url = "https://quiet-taiga-79213.herokuapp.com/register";

                Map<String, String> jsonParams = new HashMap<String, String>();
                final String resultUsername = intent.getStringExtra("resultUserName");
                final String resultPassword = intent.getStringExtra("resultPassword");
                final String resultRealName = intent.getStringExtra("resultRealName");
                final String resultEmail = intent.getStringExtra("resultEmail");

                jsonParams.put("mUsername",resultUsername);
                jsonParams.put("mPassword",resultPassword);
                jsonParams.put("mRealName",resultRealName);
                jsonParams.put("mEmail",resultEmail);
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
                VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
                refreshList();
            }
        }

        //Json request for the logout functionality
        if(requestCode == 4) {
            if(resultCode == RESULT_OK) {
                refreshLogout();
            }
            else{
                Toast.makeText(MainActivity.this,"Logout Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void refreshLogout() {

        //This finds the menuItem for creating a buzz and makes it visible
        MenuItem buzz = (MenuItem) optionsMenu.findItem(R.id.create_buzz_settings);
        buzz.setVisible(false);

        //This finds the menuItem for login and makes it visible
        MenuItem login = (MenuItem) optionsMenu.findItem(R.id.login_settings);
        login.setVisible(true);

        //This finds the menuItem for registering and makes it visible
        MenuItem register = (MenuItem) optionsMenu.findItem(R.id.register_settings);
        register.setVisible(true);

        //Finds the logout button and makes it hidden
        MenuItem logout = (MenuItem) optionsMenu.findItem(R.id.logout_settings);

        logout.setVisible(false);

        ApplicationWithGlobals.setKey(0);
        ApplicationWithGlobals.setUsername("Error");


        Toast.makeText(MainActivity.this,"Logout Successful", Toast.LENGTH_LONG).show();
        //These two lines reload the MainActivity from itself, essentially calling OnCreate again
        //necessary in order to get rid of all the messages from the main activity
        finish();
        startActivity(getIntent());
    }

}
