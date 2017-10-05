package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * This adapter is a bridge between our RecyclerAdapterView and the underlying data
 */

public class DatumRecyclerAdapter extends RecyclerView.Adapter<DatumRecyclerAdapter.DatumViewHolder>{


    interface RecyclerItemClickListener{
        void onRecyclerClick(int position);
    }

    public class DatumViewHolder extends RecyclerView.ViewHolder {

        //private final RecyclerItemClickListener mListenerInternal;
        private TextView subjectTextView;
        private TextView messageTextView;
        private TextView votesTextView;
        private Button likeButton;
        private Button disLikeButton;

        public DatumViewHolder(View itemView) {
            super(itemView);
            subjectTextView = (TextView) itemView.findViewById(R.id.listItemSubject);
            messageTextView = (TextView) itemView.findViewById(R.id.listItemMessage);
            votesTextView = (TextView) itemView.findViewById(R.id.listItemVotes);
            likeButton = (Button) itemView.findViewById(R.id.listLikeButton);
            disLikeButton = (Button) itemView.findViewById(R.id.listDislikeButton);

            //Click Button handler for the like button.  Sends a json object with mChangeVote with value 1
            //The like button is integrated in the list_item.xml
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // onClick() for button is called after onInterceptTouchEvent() stashed adapter position in a global variable.
                    int position;
                    ApplicationWithGlobals mApp = (ApplicationWithGlobals)v.getContext().getApplicationContext();
                    position = mApp.getPosition();

                    datumList.get(position).mVotes++;
                    notifyItemChanged(position);

                    // todo: move PUT to main activity implementing an interface to link the two
                    //Log.d("jnm219", "attempting to change vote count of " + datumList.get(position).mId);
                    String url = "https://quiet-taiga-79213.herokuapp.com/messages";
                    Map<String, String> jsonParams = new HashMap<String, String>();

                    // make a put request to an up-vote
                    jsonParams.put("mChangeVote", "1");
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url + "/" + datumList.get(position).mId,
                            new JSONObject(jsonParams),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("jnm219", "got response from PUT to update vote count");
                                    // todo: consider updating local vote count only if PUT succeeded.
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("jnm219", "JsonObjectRequest() to update vote count failed: " + error.getMessage());
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
                    VolleySingleton.getInstance(v.getContext()).addToRequestQueue(postRequest);

                    //Log.d("button", "click " + position);
                }
            });
            //Click Button handler for the dislike button  Sends a json object with mChangeVote with value -1
            //The Dislike button is integrated in the list_item.xml
            disLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // onClick() for button is called after onInterceptTouchEvent() stashed adapter position in a global variable.
                    int position;
                    ApplicationWithGlobals mApp = (ApplicationWithGlobals)v.getContext().getApplicationContext();
                    position = mApp.getPosition();

                    datumList.get(position).mVotes--;
                    notifyItemChanged(position);

                    // todo: move PUT to main activity implementing an interface to link the two
                    //Log.d("jnm219", "attempting to change vote count of " + datumList.get(position).mId);
                    String url = "https://quiet-taiga-79213.herokuapp.com/messages";
                    Map<String, String> jsonParams = new HashMap<String, String>();

                    // make a put request to an up-vote
                    jsonParams.put("mChangeVote", "-1");
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url + "/" + datumList.get(position).mId,
                            new JSONObject(jsonParams),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("jnm219", "got response from PUT to update vote count");
                                    // todo: consider updating local vote count only if PUT succeeded.
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("jnm219", "JsonObjectRequest() to update vote count failed: " + error.getMessage());
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
                    VolleySingleton.getInstance(v.getContext()).addToRequestQueue(postRequest);
                    //Log.d("button", "click " + position);
                }
            });
        }
        void onRecyclerItemClick(){
            int position = getAdapterPosition();
        }
    }

    private final List<Datum> datumList;    // holds all the datums

    public DatumRecyclerAdapter(List<Datum> datumList) {
        this.datumList = datumList;
    }

    @Override
    public DatumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);

        return new DatumViewHolder(itemView);
    }

    /**
     * Sets the subject, message, and vote count for each buzz in list on the main page.
     * Displayed by the RecyclerView
     * @param holder from the RecyclerView
     * @param position is the specific buzz/datum being placed in the holder
     */
    @Override
    public void onBindViewHolder(DatumViewHolder holder, int position) {
        final Datum datum = datumList.get(position);
        holder.subjectTextView.setText("Subject: " + datum.mSubject);
        holder.messageTextView.setText("Message: " + datum.mMessage);
        holder.votesTextView.setText("Votes: " + datum.mVotes);        // setText needs a string
    }
    /*
    @Override
    public void RecyclerItemClickListenr(int position)
    {
        Datum d = mData.get(position);
        mListener.onR
    }
    */


    @Override
    public int getItemCount() {
        return datumList.size();
    }

}


