package com.younglee.permissionapply.fragment;

import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.utils.LogUtils;
import com.younglee.permissionapply.MainActivity;
import com.younglee.permissionapply.R;
import com.younglee.permissionapply.listener.PermissionResultListener;

import butterknife.Bind;

/**
 * author younglee
 * Description：
 * DataTime 2017/3/10 18:22
 */

public class Fragment2 extends BaseFragment {
    @Bind(R.id.apply)
    Button apply;

    @Override
    protected int getViewRes() {
        return R.layout.content;
    }

    @Override
    protected void initView(View view) {
        apply.setText("权限申请2");
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).requestStoragePermission(new PermissionResultListener() {
                    @Override
                    public void response(boolean result) {
                        LogUtils.d("是否开启成功:" + result);
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }

}
