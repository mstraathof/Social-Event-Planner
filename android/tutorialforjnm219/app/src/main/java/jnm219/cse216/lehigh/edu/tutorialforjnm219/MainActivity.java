package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Activity;
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
    /**
     * mCommentData holds data for all the comments, this probably has to be put within the declaration of a new message
     */
    ArrayList<Comment> mCommentData = new ArrayList<>();

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        optionsMenu = menu;

        MenuItem buzz = (MenuItem) menu.findItem(R.id.create_buzz_settings);
        buzz.setVisible(false);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the recycler view.
        RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        adapter = new DatumRecyclerAdapter(mData);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // onItemClick() is called when user clicks anywhere in an adapter row.
                        // do nothing here.
                        Log.d("click", "" + position);
                    }
                })
        );
        //Sets the loginInfo object to its default value of user Id and key to 0
        //A user will not be able to comment, post a buzz, or upvote when they are not logged in
        mLoginInfo = new LoginInfo();
        refreshList();      // populate RecyclerView with initial set of buzzes.
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
            int userId = mLoginInfo.mUserId;
            int key = mLoginInfo.mKey;
            if(userId != 0 && key != 0){
                Intent i = new Intent(getApplicationContext(), CreateBuzzActivity.class);
                i.putExtra("topLabel", "Create a buzz:");
                startActivityForResult(i, 789); // 789 is the number that will come back to us
                return true;
            }
            else{
                Toast.makeText(MainActivity.this, userId + " --> " + key, Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.login_settings){
            Intent i = new Intent(getApplicationContext(), login.class);
            i.putExtra("topLabel","Log In:");
            startActivityForResult(i, 666);
            return true;
        }

        if(id == R.id.register_settings){
            Intent i = new Intent(getApplicationContext(), Register.class);
            i.putExtra("topLabel","Register for The Buzz");
            startActivityForResult(i, 667);
            return true;
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
                int mMessageId = json.getJSONObject(i).getInt("mMessageId");
                int mVotes = json.getJSONObject(i).getInt("mVotes");
                String mUsername = json.getJSONObject(i).getString("mUsername");

                mMessageData.add(new Message(mUserId, mTitle, mMessage, mCreateTime,mMessageId,mVotes,mUsername));
                Log.d("Liger", mUserId + ":" + mTitle + ":" + mMessage + ":" + mMessageId);
            }
            adapter.notifyDataSetChanged();
        } catch (final JSONException e) {
            Log.d("Liger", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("Liger", "Successfully parsed JSON file.");
    }

    /**
     *populate Comments from volley parses the response string and extracts the necessary data for the comment objects
     * @param response is a string returned from a GET request
     */
    private void populateCommentFromVolley(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray json = new JSONArray(jsonObject.getString("mCommentData"));

            mCommentData.clear(); //Clears all of the existing messages
            for(int i = 0; i < json.length(); ++i){
                int mCommentId = json.getJSONObject(i).getInt("mCommentId");
                int mUserId = json.getJSONObject(i).getInt("mUserId");
                int mMessageId = json.getJSONObject(i).getInt("mMessageId");
                String mComment = json.getJSONObject(i).getString("mComment");
                String mCreateTime = json.getJSONObject(i).getString("mCreateTime");

                mCommentData.add(new Comment(mCommentId,mUserId,mMessageId,mComment,mCreateTime));
                Log.d("Liger",mCommentId + ":" + mUserId + ":" + mMessageId +":" + mComment+ ":" + mCreateTime);
            }
        adapter.notifyDataSetChanged();
        } catch(final JSONException e){
            Log.d("Liger","Error Parsing JSON file: "+ e.getMessage());
            return;
        }
    }
    /**
     * This Route holds the data for a logged in user
     */
    private void loginUser(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray json = new JSONArray(jsonObject.getString("mLoginInfo"));

            int mUserId = json.getJSONObject(0).getInt("mUserId");
            int mKey = json.getJSONObject(0).getInt ("mKey");

            mLoginInfo = new LoginInfo(mUserId,mKey);
            Log.d("Liger",mUserId+":"+mKey);

        } catch(final JSONException e){
            Log.d("Liger","Error Parsing JSON file: "+e.getMessage());
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
                mLoginInfo.mUserId = 1;
                mLoginInfo.mKey = 2;

                //This finds the menuItem for creating a buzz and makes it visible
                MenuItem buzz = (MenuItem) optionsMenu.findItem(R.id.create_buzz_settings);
                buzz.setVisible(true);

                //This finds the menuItem for login and makes it hidden
                MenuItem login = (MenuItem) optionsMenu.findItem(R.id.login_settings);
                login.setVisible(false);

                //This finds the menuItem for registering and makes it hidden
                MenuItem register = (MenuItem) optionsMenu.findItem(R.id.register_settings);
                register.setVisible(false);

                VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
                refreshList();
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

                jsonParams.put("mUsername",resultUsername);
                jsonParams.put("mPassword",resultPassword);
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

    }

}
