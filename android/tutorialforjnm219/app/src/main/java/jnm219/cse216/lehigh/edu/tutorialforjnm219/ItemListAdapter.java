package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        //TextView mIndex;
        //TextView mText;
        //TextView mId;
        TextView mTitle;
        TextView mMessage;
        //TextView mVote;

        ViewHolder(View itemView) {
            super(itemView);
            //this.mIndex = (TextView) itemView.findViewById(R.id.listItemIndex);
            //this.mText = (TextView) itemView.findViewById(R.id.listItemText);
            this.mTitle = (TextView) itemView.findViewById(R.id.listItemTitle);
            this.mMessage = (TextView) itemView.findViewById(R.id.listItemMessage);
        }
    }

    private ArrayList<Datum> mData;
    private LayoutInflater mLayoutInflater;

    ItemListAdapter(Context context, ArrayList<Datum> data) {
        mData = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Datum d = mData.get(position);
        //holder.mIndex.setText(Integer.toString(d.mIndex));
        // don't want to display the id, for now. Thinking is user does not need to know and screen would get too busy

        holder.mTitle.setText(d.mTitle);
        holder.mMessage.setText(d.mMessage);
        //holder.mVotes.setText(Integer.toString(d.mVotes))

        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);      // d = mData.get(position)
            }
        };
        //holder.mIndex.setOnClickListener(listener);
        //holder.mText.setOnClickListener(listener);
        holder.mTitle.setOnClickListener(listener);
        holder.mMessage.setOnClickListener(listener);
        //holder.mVotes.setOnClickListener(listener);
    }

    interface ClickListener{
        void onClick(Datum d);
    }
    private ClickListener mClickListener;
    ClickListener getClickListener() {return mClickListener;}
    void setClickListener(ClickListener c) { mClickListener = c;}
}