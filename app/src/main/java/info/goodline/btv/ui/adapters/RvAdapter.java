package info.goodline.btv.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.view.SwipePanel;

/**
 * Created by g on 26.07.15.
 */
public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] mDataset;
    private LayoutInflater mInflater;
    private Context mContext;

    public RvAdapter(Context context, String[] dataSet) {
        mDataset = dataSet;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        //mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;
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
}
