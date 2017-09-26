package jnm219.cse216.lehigh.edu.tutorialforjnm219;

/**
 * Created by mira on 9/24/17.
 * Credit to http://sapandiwakar.in/recycler-view-item-click-handler/
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        // stash adapter position so listener for like-button knows which buzz was liked.
        ApplicationWithGlobals mApp = (ApplicationWithGlobals)view.getContext().getApplicationContext();
        mApp.setPosition(view.getChildAdapterPosition(childView));

        // call onItemClick() only if something must be done if anywhere in row is clicked.
        //if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
        //    mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        //}
        return false;
    }

    /* For future reference, use onTouchEvent if the user presses anywhere in the row.
    * You can use the mGestureDetector above as well
    * */

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {
    }
}