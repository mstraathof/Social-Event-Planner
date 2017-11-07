package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Jack on 10/5/2017.
 */

import android.support.v7.app.AppCompatActivity;

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

/**
 * This class handles the view for the comment activity
 * Everytime a user clicks on the comment button for a message, they are brought to a comment activity filled with comments for that message
 * Menu has an action to create a comment for that message
 */
public class CommentActivity extends AppCompatActivity {

    /**
     * mCommentData holds data for all the comments, this probably has to be put within the declaration of a new message
     */
    ArrayList<Comment> mCommentData = new ArrayList<>();

    Menu optionsMenu;
    //Adapter for the message object
    RecyclerView.Adapter adapter;

    //On create will fill this with the unique messageId
    String messageIDGlobal = "";

    //On create will fill this with the unique view which says where comment
    //is being called from. 0 is for main and 1 is for profile.
    int viewGlobal = 0;

    String check = "";



    // enter into the browser to understand what the android app is parsing in the GET request.
    String urlPost = "";
    String urlGet = "";

    /**
     * This method is called every time the comment button is pressed, will fill the view with comments
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comment_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.comment_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        adapter = new CommentRecyclerAdapter(mCommentData);
        rv.setAdapter(adapter);

        String messageId = getIntent().getStringExtra("messageId");
        final String username = getIntent().getStringExtra("otherUser");
        int view = getIntent().getIntExtra("view",0);
        urlGet = "https://quiet-taiga-79213.herokuapp.com/comments/"+messageId+"/"+ApplicationWithGlobals.getUsername()+"/"+ApplicationWithGlobals.getKey();
        urlPost = "https://quiet-taiga-79213.herokuapp.com/comments";
        messageIDGlobal = messageId;
        viewGlobal = view;
        Log.d("Liger", "This MessageId: "+messageId);

        refreshList();

        // The Cancel button returns to the caller without sending it any data
        Button bCancel = (Button) findViewById(R.id.buttonCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This will refresh the page so the new vote count can be displayed
                if(viewGlobal == 0){
                    Intent i = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(i);
                    //Log.d("button", "click " + position);
                }
                else if(viewGlobal == 1){
                    Intent i = new Intent(v.getContext(), ProfileActivity.class);
                    i.putExtra("otherUser", username);
                    v.getContext().startActivity(i);
                    //Log.d("button", "click " + position);
                }
                else{
                    ApplicationWithGlobals.setKey(0);
                    ApplicationWithGlobals.setUsername("error");
                }
            }
        });

    }


    // refresh the list of buzzes.
    // refreshList() only queues the request to populate.
    // populateMessageFromVolley() does the real work: parsing the server response and updating the adapter.
    private void refreshList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        populateCommentFromVolley(response);
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
                int mMessageId = json.getJSONObject(i).getInt("mMessageId");
                String mComment = json.getJSONObject(i).getString("mComment");
                String mCreateTime = json.getJSONObject(i).getString("mCreateTime");
                String mUsername = json.getJSONObject(i).getString("mUsername");

                mCommentData.add(new Comment(mCommentId,mMessageId,mComment,mCreateTime,mUsername));
                Log.d("Liger",mCommentId + ":" + mMessageId +":" + mComment+ ":" + mCreateTime + ":" + mUsername);
            }
            adapter.notifyDataSetChanged();
        } catch(final JSONException e){
            Log.d("Liger","Error Parsing JSON file: "+ e.getMessage());
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        optionsMenu = menu;

        MenuItem buzz = (MenuItem) menu.findItem(R.id.add_comment_settings);

        return true;
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
        if (id == R.id.add_comment_settings) {
            Intent i = new Intent(getApplicationContext(), CreateCommentActivity.class);
            i.putExtra("topLabel", "Create a buzz:");
            startActivityForResult(i, 789); // 789 is the number that will come back to us
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * @param requestCode tells you which activity needs to be responded to.
     * @param resultCode holds the result ok if the activity succeed.
     * @param intent gets sent from the activity. The only activity right now is the CreateBuzzActivity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        //Toast.makeText(MainActivity.this,"RequestCode: "+ requestCode+ "ResultCode: "+resultCode, Toast.LENGTH_LONG).show();
        // Json request for Create a comment
        if (requestCode == 789) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // POST to backend server. Modified version of:
                // https://www.itsalif.info/content/android-volley-tutorial-http-get-post-put
                Map<String, String> jsonParams = new HashMap<String, String>();
                final String resultComment = intent.getStringExtra("resultComment");
                final String username = ApplicationWithGlobals.getUsername();


                // add the data collected from user into map which gets made into a JSONObject
                jsonParams.put("mUsername",username);
                jsonParams.put("mMessageId",messageIDGlobal);
                jsonParams.put("mComment", resultComment);
                jsonParams.put("mKey",ApplicationWithGlobals.getKey()+"");

                //jsonParams.put("mUsername", mLoginInfo.mUsername);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlPost,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    check = response.getString("mCommentData");
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
                Toast.makeText(CommentActivity.this, "Comment Canceled", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(CommentActivity.this, "Error Creating Comment", Toast.LENGTH_LONG).show();
            }
        }
    }
}
