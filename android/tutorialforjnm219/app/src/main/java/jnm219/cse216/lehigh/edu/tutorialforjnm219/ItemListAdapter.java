package jnm219.cse216.lehigh.edu.tutorialforjnm219;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ItemListAdapter extends BaseAdapter {

    private ArrayList<Datum> mData;
    private LayoutInflater mLayoutInflater;

    ItemListAdapter(Context context, ArrayList<Datum> data) {
        mData = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mLayoutInflater.inflate(R.layout.list_item, viewGroup, false);
        TextView tv = (TextView) rowView.findViewById(R.id.listItemIndex);
        // NB: must pre-cast to string, or we'll dispatch to the wrong setText()
        String index = "" + mData.get(i).mIndex;
        tv.setText(index);
        tv = (TextView) rowView.findViewById(R.id.listItemText);
        tv.setText(mData.get(i).mText);
        return rowView;
    }
}