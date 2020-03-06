package com.ya.anavigationlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ya.anavigationlib.core.ANavigator;

import java.util.ArrayDeque;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/25 9:34
 * @DESC *****************************
 */
public class AFragmentNavigator implements ANavigator {

    private final String KEY_BACK_STACK_NAMES = "navigation_backStack_names";

    private final Context mContext;
    private final FragmentManager mFragmentManager;
    private final int mContainerId;
    private ArrayDeque<String> mBackStack = new ArrayDeque<>();

    public AFragmentNavigator(Context mContext, FragmentManager mFragmentManager, int mContainerId) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
        this.mContainerId = mContainerId;
    }

    @Override
    public boolean navigation(String path, Bundle args, Object navigation) {
        if (navigation != null) {
            Fragment frag = (Fragment)navigation;
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(mContainerId, frag, generateBackStackName(mBackStack.size() + 1, path));
            ft.setPrimaryNavigationFragment(frag);
            if (!mBackStack.isEmpty()) {
                ft.addToBackStack(generateBackStackName(mBackStack.size() + 1, path));
            }
            ft.setReorderingAllowed(true);
            ft.commit();
            mBackStack.addLast(path);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean popBackStack() {
        if (mBackStack.isEmpty()) {
            return false;
        }
        if (mFragmentManager.isStateSaved()) {
            return false;
        }
        mFragmentManager.popBackStack(
                generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mBackStack.removeLast();
        return true;
    }

    @Override
    public void dispatchOnActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mBackStack.isEmpty()) {
            return;
        }
        mFragmentManager.findFragmentByTag(generateBackStackName(mBackStack.size(), mBackStack.peekLast())).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Bundle onSaveState() {
        if (!mBackStack.isEmpty()) {
            Bundle b = new Bundle();
            String[] names = new String[mBackStack.size()];
            int index = 0;
            for (String name: mBackStack) {
                names[index++] = name;
            }
            b.putStringArray(KEY_BACK_STACK_NAMES, names);
            return b;
        } else {
            return null;
        }
    }

    @Override
    public void onRestoreState(Bundle saveState) {
        if (saveState != null) {
            String[] names = saveState.getStringArray(KEY_BACK_STACK_NAMES);
            if (names != null) {
                mBackStack.clear();
                for (String name : names) {
                    mBackStack.add(name);
                }
            }
        }
    }

    private String generateBackStackName(int backStackIndex, String path) {
        return backStackIndex + "-" + path;
    }
}
