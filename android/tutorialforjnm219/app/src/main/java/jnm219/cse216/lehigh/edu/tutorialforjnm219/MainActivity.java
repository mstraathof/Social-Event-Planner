package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.ArrayAdapter;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {

    /**
     * mData holds the data we get from Volley
     */
    ArrayList<Datum> mData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("jnm219", "Debug Message from onCreate");
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.cse.lehigh.edu/~spear/5k.json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final ArrayList<String> myList = new ArrayList<>();
                        try {
                            JSONArray jStringArray = new JSONArray(response);
                            for (int i = 0; i < jStringArray.length(); ++i) {
                                myList.add(jStringArray.getString(i));
                            }
                        } catch (final JSONException e) {
                            Log.d("jnm219", "Error parsing JSON file..." + e.getMessage());
                        }
                        RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
                        //ArrayAdapter adapter = new ArrayAdapter<>(MainActivity.this,
                        //        android.R.layout.simple_list_item_1,
                        //        myList);
                        RecyclerView.Adapter adapter = new DatumRecyclerViewAdapter(mData);
                        rv.setAdapter(adapter);
                        populateListFromVolley(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jnm219", "That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void populateListFromVolley(String response){
        try {
            JSONArray json= new JSONArray(response);
            for (int i = 0; i < json.length(); ++i) {
                int num = json.getJSONObject(i).getInt("num");
                String str = json.getJSONObject(i).getString("str");
                mData.add(new Datum(num, str));
            }
        } catch (final JSONException e) {
            Log.d("jnm219", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("jnm219", "Successfully parsed JSON file.");
        RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);
    }





}
