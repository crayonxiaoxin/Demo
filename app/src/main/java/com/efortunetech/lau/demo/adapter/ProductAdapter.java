package com.efortunetech.lau.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.efortunetech.lau.demo.R;
import com.efortunetech.lau.demo.bean.ProductBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yq06 on 2018/7/13.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private Context context;
    private List<ProductBean> productBeansList;
    private OnItemClickListener onItemClickListener;
    private View mHeaderView;
    private View mFooterView;
    private int TYPE_HEADER = 0;
    private int TYPE_FOOTER = 1;
    private int TYPE_NORMAL = 2;

    public ProductAdapter(Context context, List<ProductBean> productBeansList) {
        this.context = context;
        this.productBeansList = productBeansList;
    }

    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ProductHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ProductHolder(mFooterView);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        ProductHolder holder = new ProductHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ProductHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            int newPosition;
            if (mHeaderView != null) {
                newPosition = position - 1;
            } else {
                newPosition = position;
            }
            if (newPosition != productBeansList.size() - 1) {
                holder.itemView.setBackgroundResource(R.drawable.shape_bottom_line_v2);
            }
            holder.itemView.setTag(newPosition);
            holder.productImg.setImageResource(productBeansList.get(newPosition).productImg);
//            Picasso.get().load(productBeansList.get(newPosition).productImg).resize(200,200).into(holder.productImg);
            holder.productName.setText(productBeansList.get(newPosition).productName);
            holder.productDesc.setText(productBeansList.get(newPosition).productDesc);
        }
    }

    @Override
    public int getItemCount() {
        if (mFooterView == null && mHeaderView == null) {
            return productBeansList.size();
        } else if (mFooterView == null && mHeaderView != null) {
            return productBeansList.size() + 1;
        } else if (mFooterView != null && mHeaderView == null) {
            return productBeansList.size() + 1;
        } else {
            return productBeansList.size() + 2;
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

    public class ProductHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productDesc;
        private ImageView productImg;
        private View itemView;

        public ProductHolder(final View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            productImg = itemView.findViewById(R.id.productImg);
            productName = itemView.findViewById(R.id.productName);
            productDesc = itemView.findViewById(R.id.productDesc);
            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick((Integer) itemView.getTag());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        notifyItemInserted(0);
    }

    public void setFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
        notifyItemInserted(getItemCount() - 1);
    }
}
