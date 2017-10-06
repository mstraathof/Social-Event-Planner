package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by Jack on 10/5/2017.
 */
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

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageViewHolder>{

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.message_item, parent, false);

        return new MessageRecyclerAdapter.MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        final Message message = messageList.get(position);
        holder.subjectTextView.setText("Subject: " + message.mSubject);
        holder.messageTextView.setText("Message: " + message.mMessage);
        holder.usernameTextView.setText("Username: "+ message.mUsername);
        holder.votesTextView.setText("Votes: " + message.mVotes);        // setText needs a string
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    interface RecyclerItemClickListener{
        void onRecyclerClick(int position);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        //private final RecyclerItemClickListener mListenerInternal;
        private TextView subjectTextView;
        private TextView messageTextView;
        private TextView votesTextView;
        private TextView usernameTextView;
        private Button likeButton;
        private Button disLikeButton;
        private Button commentButton;

        public MessageViewHolder(View itemView) {
            super(itemView);
            subjectTextView = (TextView) itemView.findViewById(R.id.messageItemSubject);
            messageTextView = (TextView) itemView.findViewById(R.id.messageItemMessage);
            votesTextView = (TextView) itemView.findViewById(R.id.messageItemVotes);
            likeButton = (Button) itemView.findViewById(R.id.messageLikeButton);
            disLikeButton = (Button) itemView.findViewById(R.id.messageDislikeButton);
            usernameTextView = (TextView) itemView.findViewById(R.id.messageItemUsername);

            //Click Button handler for the like button.  Sends a json object with mChangeVote with value 1
            //The like button is integrated in the list_item.xml
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // onClick() for button is called after onInterceptTouchEvent() stashed adapter position in a global variable.
                    int position;
                    ApplicationWithGlobals mApp = (ApplicationWithGlobals)v.getContext().getApplicationContext();
                    position = mApp.getPosition();

                    messageList.get(position).mVotes++;
                    notifyItemChanged(position);

                    // todo: move PUT to main activity implementing an interface to link the two
                    //Log.d("jnm219", "attempting to change vote count of " + datumList.get(position).mId);
                    String url = "https://quiet-taiga-79213.herokuapp.com/messages";
                    Map<String, String> jsonParams = new HashMap<String, String>();

                    // make a put request to an up-vote
                    jsonParams.put("mChangeVote", "1");
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url + "/" + messageList.get(position).mUserId,
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

                    messageList.get(position).mVotes--;
                    notifyItemChanged(position);

                    // todo: move PUT to main activity implementing an interface to link the two
                    //Log.d("jnm219", "attempting to change vote count of " + datumList.get(position).mId);
                    String url = "https://quiet-taiga-79213.herokuapp.com/messages";
                    Map<String, String> jsonParams = new HashMap<String, String>();

                    // make a put request to an up-vote
                    jsonParams.put("mChangeVote", "-1");
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url + "/" + messageList.get(position).mUserId,
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
    private final List<Message> messageList;

    public MessageRecyclerAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

}

