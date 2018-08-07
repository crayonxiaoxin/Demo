package com.efortunetech.lau.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.efortunetech.lau.demo.R;
import com.efortunetech.lau.demo.bean.VideoBean;

import java.util.List;

/**
 * Created by yq06 on 2018/8/7.
 */

public class TestGridManagerAdapter extends RecyclerView.Adapter<TestGridManagerAdapter.GridHolder> {

    private Context context;
    private List<VideoBean> list;

    private int TYPE_HEADER = 0;
    private int TYPE_FOOTER = 1;
    private int TYPE_NORMAL = 2;

    private View mHeaderView;
    private View mFooterView;

    private OnItemClickListener onItemClickListener;

    public TestGridManagerAdapter(Context context, List<VideoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TestGridManagerAdapter.GridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new GridHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new GridHolder(mFooterView);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_testgridmanager, parent, false);
        GridHolder holder = new GridHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TestGridManagerAdapter.GridHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            int newPosition;
            if (mHeaderView != null) {
                newPosition = position - 1;
            } else {
                newPosition = position;
            }
            //...
            holder.item1.setTag(newPosition);
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            return;
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        if (mFooterView == null && mHeaderView == null) {
            return list.size();
        } else if (mFooterView == null && mHeaderView != null) {
            return list.size() + 1;
        } else if (mFooterView != null && mHeaderView == null) {
            return list.size() + 1;
        } else {
            return list.size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0 && mHeaderView != null) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && mFooterView != null) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    public class GridHolder extends RecyclerView.ViewHolder {
        private LinearLayout item1;

        public GridHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            //...
            item1 = itemView.findViewById(R.id.item1);
            item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick((Integer) item1.getTag());
                    }
                }
            });
        }
    }

    public void setHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        notifyItemInserted(0);
    }

    public void setFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
        notifyItemInserted(getItemCount() - 1);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
