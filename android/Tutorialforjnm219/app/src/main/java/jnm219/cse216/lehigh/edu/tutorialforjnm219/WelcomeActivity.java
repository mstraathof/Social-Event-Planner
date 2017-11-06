package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Jack on 10/23/2017.
 */
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class WelcomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private Button SignOut;
    private SignInButton SignIn;
    private static GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private static final String TAG = "SignInActivity";

    Menu optionsMenu;

    //This method handles creating the menu icon, it toggles the visibility depending on if they are logged in or not
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        String username = ApplicationWithGlobals.getUsername();
        int key = ApplicationWithGlobals.getKey();
        getMenuInflater().inflate(R.menu.welcome_menu, menu);
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
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ApplicationWithGlobals.setKey(0);
        ApplicationWithGlobals.setUsername("Error");

        SignIn = (SignInButton)findViewById(R.id.sign_in_button);
        SignOut = (Button)findViewById(R.id.sign_out_button);
        SignIn.setOnClickListener(this);
        SignOut.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        SignOut.setVisibility(View.GONE);

        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.server_client_id))
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Applies the GoogleSignInOptions to the googleApiClient object
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * This method handles the functionality of button presses on the Welcome screen
     * @param v
     */
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();;
                break;
        }
    }

    /**
     * Handles the program in case the connection with Google fails
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    /**
     * Handles the functionality of what the program should do once the user has signed in
     */
    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    public GoogleSignInOptions.Builder requestIdToken(String serverClientId) {
        return null;
    }

    /**
     * Handles the functionality of what the program should do once the user has signed out
     */
    private void signOut(){
        Log.d("signOut", "Signed out.");
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status){
                updateUI(false);
            }
        });
    }

    /**
     * Handles the data that is returned by Google upon attempting the signin process
     * @param result
     */
    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            final String resultUsername = account.getEmail();

            Log.d("email", resultUsername);
            final String idToken = account.getIdToken();
            Log.d("IdToken", idToken + " ");

            // Sends the ID token to backend
            String url = "https://quiet-taiga-79213.herokuapp.com/tokensignin";
            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("token_id", idToken);
            jsonParams.put("mUserName", idToken);
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", "" + response);
                            int key = 0;
                            String mUsername = "";
                            boolean check;
                            try {
                                key = response.getInt("mLoginData");
                                //mUsername = response.getString("mUsername");
                                Log.d("key", key + " ");
                                //If the key is 3, the persons username or password is wrong, or they are not registered
                                //That user is not logged in
                                if (key == 0) {
                                    Toast.makeText(WelcomeActivity.this, "Invalid Email. Must use one with @lehigh.edu.", Toast.LENGTH_LONG).show();
                                    signOut();
                                } else {
                                    mUsername = response.getString("mUsername");
                                    ApplicationWithGlobals.setUsername(mUsername);
                                    ApplicationWithGlobals.setKey(key);
                                    Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                                    startActivity(i);
                                    signOut();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(MainActivity.this,"Key2: "+key, Toast.LENGTH_LONG).show();
                            Log.e("Liger", "got response");
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
            //Volley request that registers a new user
            VolleySingleton.getInstance(this).addToRequestQueue(postRequest);

            updateUI(true);
        }
    }

    /**
     * Updates the view of the Welcome activity as necesarry in response to logging in
     * @param isLogin
     */
    private void updateUI(boolean isLogin){
        if(isLogin){
            SignIn.setVisibility(View.GONE);
            SignOut.setVisibility(View.VISIBLE);
        }
        else{
            SignIn.setVisibility(View.VISIBLE);
            SignOut.setVisibility(View.GONE);
        }
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
        /**
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
         */

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param requestCode tells you which activity needs to be responded to.
     * @param resultCode holds the result ok if the activity succeed.
     * @param intent gets sent from the activity. The only activity right now is the CreateBuzzActivity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleResult(result);
        }

        //Json Request for the Login Screen
        if (requestCode == 666) {
            if (resultCode == RESULT_OK) {
                String url = "https://quiet-taiga-79213.herokuapp.com/login";

                Map<String, String> jsonParams = new HashMap<String, String>();
                final String resultUsername = intent.getStringExtra("resultUserName");
                final String resultPassword = intent.getStringExtra("resultPassword");

                jsonParams.put("mUsername", resultUsername);
                jsonParams.put("mPassword", resultPassword);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                int key = 0;
                                boolean check;
                                try {
                                    key = response.getInt("mLoginData");
                                    //If the key is 3, the persons username or password is wrong, or they are not registered
                                    //That user is not logged in
                                    if (key == 3) {
                                        Toast.makeText(WelcomeActivity.this, "No Registered User under " + resultUsername, Toast.LENGTH_LONG).show();
                                    } else if (key == -1) {
                                        Toast.makeText(WelcomeActivity.this, "User is already logged in", Toast.LENGTH_LONG).show();
                                    } else if (key == 2) {
                                        Toast.makeText(WelcomeActivity.this, "Username and Password do not match", Toast.LENGTH_LONG).show();
                                    } else {
                                        ApplicationWithGlobals.setUsername(resultUsername);
                                        ApplicationWithGlobals.setKey(key);
                                        Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //Toast.makeText(MainActivity.this,"Key2: "+key, Toast.LENGTH_LONG).show();
                                Log.e("Liger", "got response");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(WelcomeActivity.this, "Login Failed: Please Try Again", Toast.LENGTH_LONG).show();
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
            }
        }
        //Json Request for the Register Screen
        if (requestCode == 667) {
            if (resultCode == RESULT_OK) {
                String url = "https://quiet-taiga-79213.herokuapp.com/register";

                Map<String, String> jsonParams = new HashMap<String, String>();
                Map<String, String> jsonParamsProfile = new HashMap<String, String>();
                final String resultUsername = intent.getStringExtra("resultUserName");
                final String resultPassword = intent.getStringExtra("resultPassword");
                final String resultRealName = intent.getStringExtra("resultRealName");
                final String resultEmail = intent.getStringExtra("resultEmail");

                jsonParams.put("mUsername", resultUsername);
                jsonParams.put("mRealName", resultRealName);
                jsonParams.put("mEmail", resultEmail);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("Liger", "got response");
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
                //Volley request that registers a new user
                VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
            }
        }
    }

}
