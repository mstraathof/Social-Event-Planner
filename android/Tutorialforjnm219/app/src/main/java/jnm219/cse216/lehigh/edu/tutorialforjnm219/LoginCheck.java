package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jack on 10/12/2017.
 * This Class is used in order to see if someone is still logged in or not
 * This should be done before any post to the database
 * If it fails, the user should be logged out locally
 */

public class LoginCheck {

    String username = ApplicationWithGlobals.getUsername();
    int key = ApplicationWithGlobals.getKey();

    boolean check = false;

    public boolean checkLogin(){
        String url = "https://quiet-taiga-79213.herokuapp.com/checkLogin";
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("mUsername",username);
        jsonParams.put("mKey",key+"");
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.e("Liger","got response");
                        try {
                            check = response.getBoolean("mCheck");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

        return check;
    }


}
