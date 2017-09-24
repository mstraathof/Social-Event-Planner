package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class MainActivity extends AppCompatActivity {

    /**
     * mData holds the data we get from Volley
     */
    ArrayList<Datum> mData = new ArrayList<>();
    //ItemListAdapter adapter;
    RecyclerView.Adapter adapter;

    //String url = "https://quiet-taiga-79213.herokuapp.com/messages";
    String url = "https://forums.wholetomato.com/mira216.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("jnm219", "Debug Message from onCreate");

        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        //String url = "http://www.cse.lehigh.edu/~spear/5k.json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
                            //ArrayAdapter adapter = new ArrayAdapter<>(MainActivity.this,
                            //        android.R.layout.simple_list_item_1,
                            //        myList);
                        //RecyclerView.Adapter adapter = new DatumRecyclerViewAdapter(mData);
                        //rv.setAdapter(adapter);
                        populateListFromVolley(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jnm219", "StringRequest() failed: " + error.getMessage());
            }
        });

        RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        //ItemListAdapter adapter = new ItemListAdapter(this, mData);
        adapter = new DatumRecyclerViewAdapter(mData);
        rv.setAdapter(adapter);

        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), CreateBuzzActivity.class);
            i.putExtra("topLabel", "Create a buzz:");
            startActivityForResult(i, 789); // 789 is the number that will come back to us
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void populateListFromVolley(String response){
        /*try {
            JSONArray json= new JSONArray(response);
            //JSONObject jsonObject = new JSONObject(response);
            //JSONArray json= new JSONArray(jsonObject.getString("mData"));
            //JSONArray json= new JSONArray(jsonObject.getString("mMessage"));

            for (int i = 0; i < json.length(); ++i) {
                //int num = json.getJSONObject(i).getInt("num");
                int mId = json.getJSONObject(i).getInt("mId");
                //String str = json.getJSONObject(i).getString("str");
                String mTitle = json.getJSONObject(i).getString("mTitle");
                String mMessage = json.getJSONObject(i).getString("mMessage");
                //int mVotes = json.getJSONObject(i).getInt("mVotes");
                mData.add(new Datum(mId, mTitle, mMessage));
                //mData.add(new Datum(mId, mTitle, mMessage, mVotes));
            }
        } catch (final JSONException e) {
            Log.d("jnm219", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("jnm219", "Successfully parsed JSON file.");
        RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        //adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);

        adapter.setClickListener(new ItemListAdapter.ClickListener() {
            @Override
            public void onClick(Datum d) {
                Toast.makeText(MainActivity.this, d.mTitle + " --> " + d.mMessage, Toast.LENGTH_LONG).show();
            }
        });
        */
        try {
            //JSONObject jsonObject = new JSONObject(response);
            //JSONArray json= new JSONArray(jsonObject.getString("mData"));

            JSONArray json= new JSONArray(response);
            //String jsonString = "[ { \"mId\":0, \"mTitle\":\"Movie\", \"mMessage\":\"Atomic Blonde\" }, { \"mId\":1, \"mTitle\":\"Game\", \"mMessage\":\"Monopoly\" } ]";
            //JSONArray json= new JSONArray(jsonString);

            for (int i = 0; i < json.length(); ++i) {
                int mId = json.getJSONObject(i).getInt("mId");
                String mTitle = json.getJSONObject(i).getString("mTitle");
                String mMessage = json.getJSONObject(i).getString("mMessage");
                int mVotes = json.getJSONObject(i).getInt("mVotes");
                mData.add(new Datum(mId, mTitle, mMessage, mVotes));
                Log.d("mira", mId + ":" + mTitle + ":" + mMessage + ":" + mVotes);
            }
            adapter.notifyDataSetChanged();
        } catch (final JSONException e) {
            Log.d("jnm219", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("jnm219", "Successfully parsed JSON file.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        // Check which request we're responding to
        if (requestCode == 789) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the "extra" string of intent
                //Toast.makeText(MainActivity.this, intent.getStringExtra("result"), Toast.LENGTH_LONG).show();

                String toastString = intent.getStringExtra("resultTitle") + " " + intent.getStringExtra("resultMessage");
                Toast.makeText(MainActivity.this, toastString, Toast.LENGTH_LONG).show();

                // POST to backend server. Modified version:
                // https://www.itsalif.info/content/android-volley-tutorial-http-get-post-put

                Map<String, String> jsonParams = new HashMap<String, String>();
                //jsonParams.put("mTitle", "Hello");
                //jsonParams.put("mMessage", "World");
                //final String resultTitle = intent.getStringExtra("result");
                final String resultTitle = intent.getStringExtra("resultTitle");
                final String resultMessage = intent.getStringExtra("resultMessage");

                jsonParams.put("mTitle", resultTitle);
                jsonParams.put("mMessage", resultMessage);
                JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("jnm219", "got response");
                                // add also to local list view.
                                mData.add(new Datum(0, resultTitle, resultMessage, 0));   // todo: parse id from response
                                adapter.notifyDataSetChanged();
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

                    //queue.add(postRequest);

            }
        }
    }
}
