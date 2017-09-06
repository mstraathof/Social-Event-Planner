package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jack on 9/5/2017.
 */

public class DatumRecyclerViewAdapter extends RecyclerView.Adapter<DatumRecyclerViewAdapter.DatumViewHolder>{

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
        Datum datum = datumList.get(position);
        holder.indexTextView.setText(datum.mIndex);
        holder.textTextView.setText(datum.mText);
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class DatumViewHolder extends RecyclerView.ViewHolder {

        private TextView indexTextView;
        private TextView textTextView;

        public DatumViewHolder(View itemView) {
            super(itemView);

            indexTextView = (TextView) itemView.findViewById(R.id.listItemIndex);
            textTextView = (TextView) itemView.findViewById(R.id.listItemText);
        }

    }

}
