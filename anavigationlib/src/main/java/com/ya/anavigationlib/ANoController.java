package com.ya.anavigationlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.ya.anavigationlib.core.ANavController;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/26 10:28
 * @DESC *****************************
 */
public class ANoController extends ANavController {

    public final static String KEY_FRAGMENT_PATH = "fragment_path";
    public final static String KEY_FRAGMENT_ARGS = "fragment_args";

    private Class<? extends Activity> mContainerClass;

    public ANoController(Context mContext) {
        super(mContext);
        mContainerClass = ContainerActivity.class;
    }

    public ANoController(Context mContext, Class<? extends Activity> mContainerClass) {
        super(mContext);
        this.mContainerClass = mContainerClass;
    }

    @Override
    public boolean navigation(String path, Bundle args) {
        Intent intent = new Intent();
        intent.setClass(getContext(), mContainerClass);
        intent.putExtra(KEY_FRAGMENT_PATH, path);
        intent.putExtra(KEY_FRAGMENT_ARGS, args);
        getContext().startActivity(intent);
        return true;
    }

    @Override
    public void popBackStack() {
        ((Activity)getContext()).finish();
    }
}
