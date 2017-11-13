package com.example.jensen.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jensen on 2017/2/22.
 */

public class MyAdapter extends RecyclerView.Adapter implements MyRecyclerTouchHelper.ItemTouchHelperCallback {
    private Context context;
    private List<String> list;
    private OnMyViewHolder1Listener onMyViewHolder1Listener;
    private OnMyViewHolder2Listener onMyViewHolder2Listener;

    public static final int PULL_UP_LOAD_MORE = 0;
    public static final int LOADING_MORE = 1;
    public static final int NO_MORE_DATA = 2;
    public static final int NO_NETWORK = 3;
    public static final int HIDE_FOOTER = 4;
    private int loadMoreStatus = 0;

    private static final int TYPE_FOOTER = -1;


    public MyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnMyViewHolder1Listener(OnMyViewHolder1Listener onMyViewHolder1Listener) {
        this.onMyViewHolder1Listener = onMyViewHolder1Listener;
    }

    public void setOnMyViewHolder2Listener(OnMyViewHolder2Listener onMyViewHolder2Listener) {
        this.onMyViewHolder2Listener = onMyViewHolder2Listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view1 = LayoutInflater.from(context).inflate(R.layout.item1_layout, parent, false);
            MyViewHolder1 myViewHolder1 = new MyViewHolder1(view1);
            return myViewHolder1;
        } else if (viewType == 1) {
            View view2 = LayoutInflater.from(context).inflate(R.layout.item2_layout, parent, false);
            MyViewHolder2 myViewHolder2 = new MyViewHolder2(view2);
            return myViewHolder2;
        } else {
            View footerView = LayoutInflater.from(context).inflate(R.layout.footer_loadmore_layout, parent, false);
            FooterLoadMoreViewHolder footerLoadMoreViewHolder = new FooterLoadMoreViewHolder(footerView);
            return footerLoadMoreViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder1) {
            ((MyViewHolder1) holder).textView.setText(list.get(position));
            ((MyViewHolder1) holder).textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMyViewHolder1Listener.onTextClick(((MyViewHolder1) holder).textView, position);
                }
            });
        } else if (holder instanceof MyViewHolder2) {
            ((MyViewHolder2) holder).textView.setText(list.get(position));
            ((MyViewHolder2) holder).textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMyViewHolder2Listener.onTextClick(((MyViewHolder2) holder).textView, position);
                }
            });
        } else if (holder instanceof FooterLoadMoreViewHolder) {
            switch (loadMoreStatus) {
                case PULL_UP_LOAD_MORE:
                ((FooterLoadMoreViewHolder) holder).mRelativelayout.setVisibility(View.VISIBLE);
                ((FooterLoadMoreViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((FooterLoadMoreViewHolder) holder).mTextView.setText("上拉加载更多...");
                break;
                case LOADING_MORE:
                    ((FooterLoadMoreViewHolder) holder).mRelativelayout.setVisibility(View.VISIBLE);
                    ((FooterLoadMoreViewHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                    ((FooterLoadMoreViewHolder) holder).mTextView.setText("正在加载更多数据...");
                    break;
                case NO_MORE_DATA:
                    ((FooterLoadMoreViewHolder) holder).mRelativelayout.setVisibility(View.VISIBLE);
                    ((FooterLoadMoreViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                    ((FooterLoadMoreViewHolder) holder).mTextView.setText("加载完成已无更多数据...");
                    break;
                case NO_NETWORK:
                    ((FooterLoadMoreViewHolder) holder).mRelativelayout.setVisibility(View.VISIBLE);
                    ((FooterLoadMoreViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                    ((FooterLoadMoreViewHolder) holder).mTextView.setText("网络不稳定，点击加载...");
                    break;
                case HIDE_FOOTER:
                    ((FooterLoadMoreViewHolder) holder).mRelativelayout.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            if (position % 2 == 1) {
                return 1;
            }
            return 0;
        }
    }

    public void changeLoadMoreStatus(int status) {
        loadMoreStatus = status;
        notifyDataSetChanged();
    }

    @Override
    public void onItemDelete(int positon) {
        list.remove(positon);
        notifyItemRemoved(positon);
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);//交换数据
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class MyViewHolder1 extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyViewHolder1(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item1_textview);
        }
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder2(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item2_textview);
        }
    }

    public static class FooterLoadMoreViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private TextView mTextView;
        private RelativeLayout mRelativelayout;

        public FooterLoadMoreViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.footer_loadmore_progressbar);
            mTextView = (TextView) itemView.findViewById(R.id.footer_loadmore_textview);
            mRelativelayout = (RelativeLayout) itemView.findViewById(R.id.footer_loadmore_relativelayout);
        }
    }

    public interface OnMyViewHolder1Listener {
        void onTextClick(View view, int position);
    }

    public interface OnMyViewHolder2Listener {
        void onTextClick(View view, int position);
    }

}
