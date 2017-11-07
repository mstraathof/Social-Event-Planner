package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trent Gray on 11/5/2017.
 */

public class ProfileActivity extends AppCompatActivity{
    /**
     * mMessageData holds data for all the messages, this probably has to be put within the declaration of a new message
     */
    ArrayList<Message> mMessageData = new ArrayList<>();

    Menu optionsMenu;
    //Adapter for the message object
    RecyclerView.Adapter adapter;

    //On create will fill this with the unique messageId
    String messageIDGlobal = "";

    String check = "";

    // enter into the browser to understand what the android app is parsing in the GET request.
    String urlGet = "";
    String urlPost = "";

    // Information for the user profile
    String mUsername;
    String mRealName;
    String mEmail;
    String mProfile;

    //Profile info text views
    private TextView usernameTextView;
    private TextView realnameTextView;
    private TextView emailTextView;
    private TextView bioTextView;
    private Button profileButtonBio;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.comment_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        adapter = new MessageRecyclerAdapter(mMessageData, 1);
        rv.setAdapter(adapter);

        String otherUser = getIntent().getStringExtra("otherUser");
        urlGet = "https://quiet-taiga-79213.herokuapp.com/profile/"+otherUser+"/"+ApplicationWithGlobals.getUsername()+"/"+ApplicationWithGlobals.getKey();
        urlPost = "https://quiet-taiga-79213.herokuapp.com/profile";
        messageIDGlobal = otherUser;
        Log.d("Liger", "This MessageId: "+otherUser);

        // Creating Profile Infromation TextViews
        usernameTextView = (TextView)findViewById(R.id.profileUsername);
        realnameTextView = (TextView)findViewById(R.id.profileRealname);
        emailTextView = (TextView)findViewById(R.id.profileEmail);
        bioTextView = (TextView)findViewById(R.id.profileBio);
        profileButtonBio = (Button) findViewById(R.id.profileBioButton);

        // Checks if user is visiting their own profile. If they are, the change bio button will be visable.
        // It will no be visible otherwise.
        if(!otherUser.equals(ApplicationWithGlobals.getUsername())){
            profileButtonBio.setVisibility(profileButtonBio.GONE);
        }

        // Method for checking if the usere clicks on the change bio button.
        profileButtonBio.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), CreateBioActivity.class);
                i.putExtra("topLabel", "Create a buzz:");
                startActivityForResult(i, 935); // 789 is the number that will come back to us
            }
        });

        refreshList();

    }

    // refresh the list of messages.
    // refreshList() only queues the request to populate.
    // populateMessageFromVolley() does the real work: parsing the server response and updating the adapter.
    private void refreshList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        populateMessagesFromVolley(response);
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
     *populate Messages from volley parses the response string and extracts the necessary data for the comment objects
     * @param response is a string returned from a GET request
     */
    private void populateMessagesFromVolley(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray json = new JSONArray(jsonObject.getString("mMessageData"));
            JSONObject profile = new JSONObject(jsonObject.getString("mProfileData"));
            Log.d("Profile", profile + "");

            mMessageData.clear(); //Clears all of the existing messages
            for(int i = 0; i < json.length(); ++i){
                int mId = json.getJSONObject(i).getInt("mId");
                String mSubject = json.getJSONObject(i).getString("mSubject");
                String mMessage = json.getJSONObject(i).getString("mMessage");
                int mVotes = (int) json.getJSONObject(i).getInt("mVotes");
                String mCreateTime = json.getJSONObject(i).getString("mCreateTime");
                String mUsername = json.getJSONObject(i).getString("mUsername");

                mMessageData.add(new Message(mId,mSubject,mMessage,mCreateTime,mVotes,mUsername));
                Log.d("Liger",mId + ":" + mSubject + ":" + mMessage +":" + mVotes+ ":" + mCreateTime + ":" + mUsername);
            }

            // Gets profile data from json object named profile
            mUsername = profile.getString("mUsername");
            mRealName = profile.getString("mRealName");
            mEmail = profile.getString("mEmail");
            mProfile = profile.getString("mProfile");

            Log.d("Variables", "Username: " + mUsername + ", Realname: " + mRealName + ", Email: " + mEmail + ", Profile: " + mProfile);

            // Changes the TextView items to match the information obtained by the json object
            usernameTextView.setText("Username: " + mUsername);
            realnameTextView.setText("Real Name: " + mRealName);
            emailTextView.setText("Email: " + mEmail);
            bioTextView.setText("Bio: " + mProfile);

            adapter.notifyDataSetChanged();
        } catch(final JSONException e){
            Log.d("Liger","Error Parsing JSON file: "+ e.getMessage());
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        optionsMenu = menu;

        MenuItem buzz = (MenuItem) menu.findItem(R.id.message_settings);

        return true;
    }
    /**
     * What happens when the user selects an item in the menu.
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
                Toast.makeText(ProfileActivity.this, "Error: User not Logged In", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.message_settings) {
            Intent i = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(i);
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

    /**
     * This method is responsible for createing a buzz
     *
     * @param requestCode
     * @param resultCode
     * @param intent
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
                Toast.makeText(ProfileActivity.this, "Buzz Canceled", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(ProfileActivity.this,"Error Creating Buzz", Toast.LENGTH_LONG).show();
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
                Toast.makeText(ProfileActivity.this,"Logout Cancelled", Toast.LENGTH_LONG).show();
            }
        }

        //Toast.makeText(MainActivity.this,"RequestCode: "+ requestCode+ "ResultCode: "+resultCode, Toast.LENGTH_LONG).show();
        // Json request to create a bio
        if (requestCode == 935) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // POST to backend server. Modified version of:
                // https://www.itsalif.info/content/android-volley-tutorial-http-get-post-put
                Map<String, String> jsonParams = new HashMap<String, String>();
                final String resultBio = intent.getStringExtra("resultBio");
                final String username = ApplicationWithGlobals.getUsername();
                final int key = ApplicationWithGlobals.getKey();


                // add the data collected from user into map which gets made into a JSONObject
                jsonParams.put("mUsername",username);
                jsonParams.put("mKey",key+"");
                jsonParams.put("mProfile", resultBio);

                //jsonParams.put("mUsername", mLoginInfo.mUsername);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlPost,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    check = response.getString("mProfileData");
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
                if(check != "false") {
                    VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
                }
                else{
                    ApplicationWithGlobals.setKey(0);
                    ApplicationWithGlobals.setUsername("error");
                }
                refreshList();

                //Calls method from itself to more reliably refill the comment adapter
                finish();
                startActivity(getIntent());
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(ProfileActivity.this, "Bio Canceled", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(ProfileActivity.this, "Error Editing Bio", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This method logs out a user by getting rid of the username and key in local storage, and changes the view back to logged out
     */
    private void refreshLogout() {
        ApplicationWithGlobals.setKey(0);
        ApplicationWithGlobals.setUsername("Error");

        Intent i = new Intent(ProfileActivity.this, WelcomeActivity.class);
        Toast.makeText(ProfileActivity.this,"Logout Successful", Toast.LENGTH_LONG).show();
        //These two lines reload the MainActivity from itself, essentially calling OnCreate again
        //necessary in order to get rid of all the messages from the main activity
        finish();
        startActivity(i);
    }
}
