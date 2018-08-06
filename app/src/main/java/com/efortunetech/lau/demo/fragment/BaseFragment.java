package com.efortunetech.lau.demo.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by yq06 on 2018/8/1.
 */

public class BaseFragment extends Fragment {

    /**
     * return true 表示返回事件已處理，return false表示返回事件未處理
     *
     * @return
     */
    public boolean onBackPressed() {
        return false;
    }
}
