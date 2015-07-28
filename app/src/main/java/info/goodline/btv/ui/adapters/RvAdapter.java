package info.goodline.btv.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.view.RatingDraggablePanel;
import info.goodline.btv.ui.view.SwipePanel;

/**
 * Created by g on 26.07.15.
 */
public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = RvAdapter.class.getSimpleName();
    private String[] mDataset;
    private LayoutInflater mInflater;
    private Context mContext;
    private static final int TYPE_RATE = 1;
    private static final int TYPE_SWIPE = 2;

    public RvAdapter(Context context, String[] dataSet) {
        mDataset = dataSet;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        //mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_RATE;
        return TYPE_SWIPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateView i=" + i);
        switch (i) {
            case TYPE_RATE:
                return getRateHolder(viewGroup);
            case TYPE_SWIPE:
                return getSwipeHolder(viewGroup);
        }
        return null;
    }

    private RecyclerView.ViewHolder getRateHolder(ViewGroup viewGroup) {
        SwipePanel swipePanel = new SwipePanel(viewGroup.getContext());
        swipePanel.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );

        View view1 = mInflater.inflate(R.layout.view_custom_main, null);
        view1.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        View view2 = mInflater.inflate(R.layout.view_custom, null);
        view2.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        swipePanel.init(view1, view2);
        ViewHolder viewHolder = new ViewHolder(swipePanel);
        return viewHolder;
    }

    private RecyclerView.ViewHolder getSwipeHolder(ViewGroup viewGroup) {
        View infoView = mInflater.inflate(R.layout.view_info_container, null);
        infoView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
        View draggView = infoView.findViewById(R.id.llDragContainer);
        RatingDraggablePanel ratingDraggablePanel = new RatingDraggablePanel(viewGroup.getContext());
        ratingDraggablePanel.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        ratingDraggablePanel.init(infoView, draggView);
        return new ViewHolderRate(ratingDraggablePanel);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_RATE:
                Log.d(TAG, "rate pos=" + position + " type=" + getItemViewType(position));
                //ViewHolderRate holderRate = (ViewHolderRate) viewHolder;
                break;

            case TYPE_SWIPE:
                Log.d(TAG, "swipe pos=" + position + " type=" + getItemViewType(position));
                //ViewHolder holder = (ViewHolder) viewHolder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SwipePanel swipePanel;

        public ViewHolder(SwipePanel holderView) {
            super(holderView);
            swipePanel = holderView;
        }
    }

    public static class ViewHolderRate extends RecyclerView.ViewHolder {
        RatingDraggablePanel ratingDraggablePanel;

        public ViewHolderRate(RatingDraggablePanel holderView) {
            super(holderView);
            ratingDraggablePanel = holderView;
        }
    }
}
