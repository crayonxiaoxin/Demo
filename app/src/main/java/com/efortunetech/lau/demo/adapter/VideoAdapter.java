package com.efortunetech.lau.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.efortunetech.lau.demo.R;
import com.efortunetech.lau.demo.bean.ProductBean;
import com.efortunetech.lau.demo.bean.VideoBean;

import java.util.List;

/**
 * Created by yq06 on 2018/7/30.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private Context context;
    private List<VideoBean> list;

    private int TYPE_HEADER = 0;
    private int TYPE_FOOTER = 1;
    private int TYPE_NORMAL = 2;

    private View mHeaderView;
    private View mFooterView;

    private OnItemClickListener onItemClickListener;

    public VideoAdapter(Context context, List<VideoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VideoAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new VideoHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new VideoHolder(mFooterView);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        VideoHolder holder = new VideoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            int newPosition;
            if (mHeaderView != null) {
                newPosition = position - 1;
            } else {
                newPosition = position;
            }
            //...
            if (newPosition * 3 + 1 > list.size() - 1) {
                holder.item2.removeAllViews();
                holder.item3.removeAllViews();
            } else if (newPosition * 3 + 2 > list.size() - 1) {
                holder.item3.removeAllViews();
            }
            holder.item1.setTag(newPosition * 3);
            holder.item2.setTag(newPosition * 3 + 1);
            holder.item3.setTag(newPosition * 3 + 2);
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
            return (int) Math.ceil(list.size() / 3.0);
        } else if (mFooterView == null && mHeaderView != null) {
            return (int) Math.ceil(list.size() / 3.0) + 1;
        } else if (mFooterView != null && mHeaderView == null) {
            return (int) Math.ceil(list.size() / 3.0) + 1;
        } else {
            return (int) Math.ceil(list.size() / 3.0) + 2;
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

    public class VideoHolder extends RecyclerView.ViewHolder {
        private LinearLayout item1, item2, item3;

        public VideoHolder(final View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            //...
            item1 = itemView.findViewById(R.id.item1);
            item2 = itemView.findViewById(R.id.item2);
            item3 = itemView.findViewById(R.id.item3);
            item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick((Integer) item1.getTag());
                    }
                }
            });
            item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick((Integer) item2.getTag());
                    }
                }
            });
            item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick((Integer) item3.getTag());
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
