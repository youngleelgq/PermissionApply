package com.younglee.permissionapply.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * author younglee
 * Description：
 * DataTime 2017/3/10 18:17
 */

public abstract class BaseFragment extends Fragment {

    protected LayoutInflater inflater;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(getViewRes(), container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    protected abstract int getViewRes();

    protected abstract void initView(View view);

    protected abstract void initData();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
