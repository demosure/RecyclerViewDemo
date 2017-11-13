package com.example.jensen.recyclerviewdemo;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<String> list;
    private MyAdapter mAdapter;


    private int lastVisibleItem = 0;
    //haha

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initData();
        init();
        register();
    }

    public void initData() {
        list = new ArrayList<String>(50);
        for (int i = 0; i < 50; i++) {
            list.add("JensenGui " + i);
        }
    }

    public void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyAdapter(this, list);
    }

    public void register() {
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#abcdef"), Color.parseColor("#fedcba"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int size = list.size();
                list.add("JensenGui " + size);
                mAdapter.notifyDataSetChanged();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);

            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    mAdapter.changeLoadMoreStatus(MyAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int size = list.size();
                            list.add("JensenGui " + size);
                            mAdapter.notifyDataSetChanged();
                            mAdapter.changeLoadMoreStatus(MyAdapter.PULL_UP_LOAD_MORE);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

        mAdapter.setOnMyViewHolder1Listener(new MyAdapter.OnMyViewHolder1Listener() {
            @Override
            public void onTextClick(View view, int position) {
                Toast.makeText(MainActivity.this, list.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnMyViewHolder2Listener(new MyAdapter.OnMyViewHolder2Listener() {
            @Override
            public void onTextClick(View view, int position) {
                Toast.makeText(MainActivity.this, list.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));

        ItemTouchHelper.Callback callback = new MyRecyclerTouchHelper(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
}
