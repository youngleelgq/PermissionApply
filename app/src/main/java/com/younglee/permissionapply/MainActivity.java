package com.younglee.permissionapply;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gc.materialdesign.widgets.Dialog;
import com.blankj.utilcode.utils.LogUtils;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.younglee.permissionapply.fragment.Fragment1;
import com.younglee.permissionapply.fragment.Fragment2;
import com.younglee.permissionapply.listener.PermissionResultListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity implements OnPermissionCallback {

    @Bind(R.id.buttom)
    LinearLayout buttom;
    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.activity_main)
    RelativeLayout activityMain;
    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.button2)
    Button button2;

    private PermissionHelper permissionHelper;
    private AlertDialog builder;
    Fragment1 fragment1 = new Fragment1();
    Fragment2 fragment2 = new Fragment2();
    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();
    private PermissionResultListener pl;
    protected Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button1, R.id.button2})
    void click(View view) {
        switch (view.getId()) {
            case R.id.button1:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment1).commit();
                break;
            case R.id.button2:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment2).commit();
                break;
            default:
                break;
        }
    }

    public void promissionTest() {
        permissionHelper = PermissionHelper.getInstance(this);
        permissionHelper
                .setForceAccepting(false)// true if you had like force reshowing the permission dialog on Deny (not recommended)
                .request(Manifest.permission.CAMERA);
    }


    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        LogUtils.d("onPermissionGranted:" + Arrays.toString(permissionName));
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        LogUtils.d("onPermissionDeclined:" + Arrays.toString(permissionName));
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        LogUtils.d("onPermissionPreGranted:" + permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        LogUtils.d("onPermissionNeedExplanation:" + permissionName);
        getAlertDialog(permissionName).show();
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        LogUtils.d("onPermissionReallyDeclined:" + permissionName);
    }

    @Override
    public void onNoPermissionNeeded() {
        LogUtils.d("onNoPermissionNeeded");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionHelper.onActivityForResult(requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    public AlertDialog getAlertDialog(final String[] permissions, final String permissionName) {
//        if (builder == null) {
//            builder = new AlertDialog.Builder(getActivity())
//                    .setTitle("Permission Needs Explanation")
//                    .create();
//        }
//        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                permissionHelper.requestAfterExplanation(permissions);
//            }
//        });
//        builder.setMessage("Permissions need explanation (" + permissionName + ")");
//        return builder;
//    }

    public AlertDialog getAlertDialog(final String permission) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this)
                    .setTitle("Permission Needs Explanation")
                    .create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionHelper.requestAfterExplanation(permission);
            }
        });
        builder.setMessage("Permission need explanation (" + permission + ")");
        return builder;
    }


    /**
     * 请求权限
     *
     * @param id                   请求授权的id 唯一标识即可
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    protected void requestPermission(final int id, final String permission, Runnable allowableRunnable, Runnable disallowableRunnable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }

        allowablePermissionRunnables.put(id, allowableRunnable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }

        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                if (!TextUtils.isEmpty(pl.start())) {
                    dialog(pl.start(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, id);
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, id);
                }
            } else {
                allowableRunnable.run();
            }
        } else {
            allowableRunnable.run();
        }
    }

    /**
     * 弹出点击外部不可取消的提示框
     *
     * @param msg 提示框内容
     */
    protected void dialog(String msg, final View.OnClickListener listener) {
        if (dialog == null) {
            dialog = new Dialog(this, "提示", msg);
        } else {
            dialog.setMessage(msg);
        }
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void requestStoragePermission(PermissionResultListener pl) {
        this.pl = pl;
        requestPermission(1, Manifest.permission.CAMERA, new Runnable() {
            @Override
            public void run() {
                MainActivity.this.pl.response(true);
            }
        }, new Runnable() {
            @Override
            public void run() {
                MainActivity.this.pl.response(false);
            }
        });
    }
}
