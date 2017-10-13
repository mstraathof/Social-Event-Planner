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
 * This adapter is a bridge between our RecyclerAdapterView and the underlying data for comments
 */
public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder>{

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.comment_item, parent, false);

        return new CommentRecyclerAdapter.CommentViewHolder(itemView);
    }

    /**
     * Binds the comment data with the text view for all the displayed elements for a comment
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final Comment Comment = CommentList.get(position);
        holder.CommentTextView.setText("Comment: " + Comment.mComment);
        holder.usernameTextView.setText("Username: "+ Comment.mUsername);
    }

    @Override
    public int getItemCount() {
        return CommentList.size();
    }

    interface RecyclerItemClickListener{
        void onRecyclerClick(int position);
    }

    /**
     * Creates a holder for all the text view elements for a comment
     */
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        //private final RecyclerItemClickListener mListenerInternal;
        private TextView CommentTextView;
        private TextView usernameTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            CommentTextView = (TextView) itemView.findViewById(R.id.commentItemComment);
            usernameTextView = (TextView) itemView.findViewById(R.id.commentItemUsername);

        }
        void onRecyclerItemClick(){
            int position = getAdapterPosition();
        }
    }
    private final List<Comment> CommentList;

    public CommentRecyclerAdapter(List<Comment> CommentList) {
        this.CommentList = CommentList;
    }

}

