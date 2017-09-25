package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DatumRecyclerViewAdapter extends RecyclerView.Adapter<DatumRecyclerViewAdapter.DatumViewHolder>{

    public class DatumViewHolder extends RecyclerView.ViewHolder {

        private TextView subjectTextView;
        private TextView messageTextView;
        private TextView votesTextView;
        private Button likeButton;

        public DatumViewHolder(View itemView) {
            super(itemView);
            subjectTextView = (TextView) itemView.findViewById(R.id.listItemSubject);
            messageTextView = (TextView) itemView.findViewById(R.id.listItemMessage);
            votesTextView = (TextView) itemView.findViewById(R.id.listItemVotes);
            likeButton = (Button) itemView.findViewById(R.id.listLikeButton);

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // onClick() for button is called after onInterceptTouchEvent() stashed adapter position in a global variable.
                    int position;
                    ApplicationWithGlobals mApp = (ApplicationWithGlobals)v.getContext().getApplicationContext();
                    position = mApp.getPosition();

                    datumList.get(position).mVotes++;
                    notifyItemChanged(position);

                    // todo: write code to POST or PUT a vote change to the backend server.
                    Log.d("button", "click " + position);
                }
            });
        }
    }

    private final List<Datum> datumList;

    public DatumRecyclerViewAdapter(List<Datum> datumList) {
        this.datumList = datumList;
    }

    @Override
    public DatumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new DatumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DatumViewHolder holder, int position) {
        final Datum datum = datumList.get(position);

        holder.subjectTextView.setText("Subject: " + datum.mSubject);
        holder.messageTextView.setText("Message: " + datum.mMessage);
        holder.votesTextView.setText("Votes: " + datum.mVotes);        // setText needs a string
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

}