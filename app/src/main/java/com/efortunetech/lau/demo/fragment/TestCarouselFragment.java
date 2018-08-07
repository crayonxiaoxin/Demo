package com.efortunetech.lau.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.efortunetech.lau.demo.R;
import com.efortunetech.lau.demo.adapter.MyCarouselFigurePagerAdapter;
import com.efortunetech.lau.demo.adapter.TestGridManagerAdapter;
import com.efortunetech.lau.demo.adapter.VideoAdapter;
import com.efortunetech.lau.demo.bean.VideoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yq06 on 2018/7/30.
 */

public class TestCarouselFragment extends Fragment {
    private ViewPager viewPager;
    private View view;
    private Activity mActivity;
    private boolean isRunning = false;
    private Handler handler = new Handler();
    private LinearLayout points;
    private ImageView lastOne;
    private ImageView nextOne;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_testcarousel, null);
        viewPager = view.findViewById(R.id.viewPager);
        points = view.findViewById(R.id.points);
        lastOne = view.findViewById(R.id.lastOne);
        nextOne = view.findViewById(R.id.nextOne);

        recyclerView = view.findViewById(R.id.recyclerView);

        init();

        return view;
    }

    private void init() {
        // add carousel
        final List<Integer> images = new ArrayList<>();
        images.add(R.drawable.pic1);
        images.add(R.drawable.pic2);
        images.add(R.drawable.pic3);
        images.add(R.drawable.pic4);
        images.add(R.drawable.pic5);

        MyCarouselFigurePagerAdapter adapter = new MyCarouselFigurePagerAdapter(mActivity, images);
        adapter.setOnItemClickListener(new MyCarouselFigurePagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Toast.makeText(mActivity, "position => " + (position % images.size()), Toast.LENGTH_SHORT).show();
            }
        });

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(10000);

        // add points
        for (int i = 0; i < images.size(); i++) {
            ImageView point = new ImageView(mActivity);
            point.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
            point.setPadding(5, 5, 5, 5);
            if (i == 0) {
                point.setImageResource(R.drawable.point_black);
            } else {
                point.setImageResource(R.drawable.point_white);
            }
            points.addView(point);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isRunning) {
                    for (int i = 0; i < images.size(); i++) {
                        if (i == (position % images.size())) {
                            ImageView point = (ImageView) points.getChildAt(i);
                            point.setImageResource(R.drawable.point_black);
                        } else {
                            ImageView point = (ImageView) points.getChildAt(i);
                            point.setImageResource(R.drawable.point_white);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        lastOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() > 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });

        nextOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        // add products
//        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
        List<VideoBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            VideoBean videoBean = new VideoBean();
            videoBean.ID = i;
            videoBean.image = "";
            videoBean.num1 = i;
            videoBean.num2 = i;
            videoBean.num3 = i;
            list.add(videoBean);
        }
//        VideoAdapter videoAdapter = new VideoAdapter(mActivity, list);
        TestGridManagerAdapter videoAdapter = new TestGridManagerAdapter(mActivity, list);
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.setOnItemClickListener(new TestGridManagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(mActivity, "position => " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;   // flag
        new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }
}

