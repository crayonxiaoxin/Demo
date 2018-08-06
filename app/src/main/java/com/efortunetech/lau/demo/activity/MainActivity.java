package com.efortunetech.lau.demo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efortunetech.lau.demo.R;
import com.efortunetech.lau.demo.adapter.MyFragmentPagerAdapter;
import com.efortunetech.lau.demo.bean.TabsBean;
import com.efortunetech.lau.demo.fragment.BaseFragment;
import com.efortunetech.lau.demo.fragment.ProductDetailFragment;
import com.efortunetech.lau.demo.fragment.ProductFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductFragment.ProductFragmentListener {

    private DrawerLayout drawerLayout;
    private LinearLayout leftArea;
    public static RelativeLayout topbar;
    public static TabLayout newsTab;
    private ViewPager newsViewPager;
    private FrameLayout frameLayout;
    private TextView login;
    private ImageView tocart;
    private TextView username;
    private TextView userid;
    private TextView tocart2;
    private TextView myOrders;
    private TextView logout;
    private ProgressBar progressBar;
    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        leftArea = findViewById(R.id.leftArea);
        // 中间
        topbar = findViewById(R.id.relativeLayout);
        newsTab = findViewById(R.id.newsTab);
        newsViewPager = findViewById(R.id.newsViewPager);
        frameLayout = findViewById(R.id.frameLayout);
        login = findViewById(R.id.login);
        tocart = findViewById(R.id.toCart);
        // 左侧
        username = findViewById(R.id.username);
        userid = findViewById(R.id.userid);
        tocart2 = findViewById(R.id.toCart2);
        myOrders = findViewById(R.id.myOrders);
        logout = findViewById(R.id.logout);

        progressBar = findViewById(R.id.progressBar);

        init();
    }

    private void init() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });
        tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "開發中...", Toast.LENGTH_SHORT).show();
            }
        });
        final FragmentManager fm = getSupportFragmentManager();
        List<TabsBean> tabsBeanList = new ArrayList<>();
        TabsBean testBean = new TabsBean();
        testBean.Id = 999;
        testBean.name = "測試";
        tabsBeanList.add(testBean);
        for (int i = 0; i < 6; i++) {
            TabsBean bean = new TabsBean();
            bean.Id = i;
            bean.name = "標題" + i;
            tabsBeanList.add(bean);
        }
        newsTab.setupWithViewPager(newsViewPager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fm, tabsBeanList, 0);
        newsViewPager.setAdapter(adapter);
        newsTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
                frameLayout.setVisibility(View.GONE);
                newsViewPager.setVisibility(View.VISIBLE);
                newsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
                frameLayout.setVisibility(View.GONE);
                newsViewPager.setVisibility(View.VISIBLE);
                newsViewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (isExit) {
                    this.finish();
                } else {
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    isExit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isExit = false;
                        }
                    }, 500);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Fragment f = fm.findFragmentById(R.id.frameLayout);
            if (f instanceof BaseFragment) {
                if (((BaseFragment) f).onBackPressed()) {
                    Log.i("ORM", "fragment back button handled");
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void showProductDetail(int productId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ProductDetailFragment pdf = ProductDetailFragment.newInstance(productId);
        ft.replace(R.id.frameLayout, pdf, "productDetail");
        ft.addToBackStack("productDetail");
        frameLayout.setVisibility(View.VISIBLE);
        ft.commit();
    }

    @Override
    public void showProductDetail_static(String title, String desc) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ProductDetailFragment pdf = ProductDetailFragment.newInstance_static(title, desc);
        ft.replace(R.id.frameLayout, pdf, "productDetail");
        ft.addToBackStack("productDetail");
        frameLayout.setVisibility(View.VISIBLE);
        ft.commit();
    }
}
