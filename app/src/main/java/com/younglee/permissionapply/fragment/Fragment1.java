package com.younglee.permissionapply.fragment;

import android.view.View;
import android.widget.Button;

import com.younglee.permissionapply.MainActivity;
import com.younglee.permissionapply.R;

import butterknife.Bind;

/**
 * author younglee
 * Description：
 * DataTime 2017/3/10 18:22
 */

public class Fragment1 extends BaseFragment {
    @Bind(R.id.apply)
    Button apply;

    @Override
    protected int getViewRes() {
        return R.layout.content;
    }

    @Override
    protected void initView(View view) {
        apply.setText("权限申请1");
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).promissionTest();
            }
        });
    }

    @Override
    protected void initData() {

    }

}
