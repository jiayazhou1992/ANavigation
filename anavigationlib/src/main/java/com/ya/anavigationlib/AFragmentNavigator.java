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
import com.ya.anavigationlib.core.FStackEntry;

import java.util.ArrayDeque;

import timber.log.Timber;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/25 9:34
 * @DESC *****************************
 */
public class AFragmentNavigator implements ANavigator {

    private final String KEY_BACK_STACK_ENTRYS = "navigation_backStack_entrys";

    private Context mContext;
    private FragmentManager mFragmentManager;
    private final int mContainerId;
    private ArrayDeque<FStackEntry> mBackStack = new ArrayDeque<>();

    public AFragmentNavigator(Context mContext, FragmentManager mFragmentManager, int mContainerId) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
        this.mContainerId = mContainerId;
    }

    @Override
    public boolean navigation(String path, Bundle args, Object navigation) {
        if (navigation != null) {

            FStackEntry fStackEntry = new FStackEntry();
            fStackEntry.setPath(path);
            fStackEntry.setTag(generateBackStackName(mBackStack.size() + 1, path));

            Fragment frag = (Fragment)navigation;
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(mContainerId, frag, fStackEntry.getTag());
            ft.setPrimaryNavigationFragment(frag);
            if (!mBackStack.isEmpty()) {//默认第一个都不加入栈
                ft.addToBackStack(fStackEntry.getTag());
            }
            ft.setReorderingAllowed(true);
            ft.commit();
            mBackStack.addLast(fStackEntry);

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

        FStackEntry fStackEntry = mBackStack.peekLast();
        mFragmentManager.popBackStack(fStackEntry.getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mBackStack.removeLast();
        return true;
    }

    @Override
    public void clear() {
        if (mBackStack.isEmpty()) {
            return;
        }
        mBackStack.clear();
        mContext = null;
        mFragmentManager = null;
    }

    @Override
    public void dispatchOnActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mBackStack.isEmpty()) {
            return;
        }
        mFragmentManager.findFragmentByTag( mBackStack.peekLast().getTag()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Bundle onSaveState() {
        if (!mBackStack.isEmpty()) {
            Bundle b = new Bundle();
            FStackEntry[] fStackEntries = new FStackEntry[mBackStack.size()];
            int index = 0;
            for (FStackEntry stackEntry: mBackStack) {
                fStackEntries[index++] = stackEntry;
            }
            b.putParcelableArray(KEY_BACK_STACK_ENTRYS, fStackEntries);
            return b;
        } else {
            return null;
        }
    }

    @Override
    public void onRestoreState(Bundle saveState) {
        if (saveState != null) {
            FStackEntry[] fStackEntries = (FStackEntry[]) saveState.getParcelableArray(KEY_BACK_STACK_ENTRYS);
            if (fStackEntries != null) {
                mBackStack.clear();
                for (FStackEntry stackEntry : fStackEntries) {
                    mBackStack.add(stackEntry);
                }
            }
        }
    }

    private String generateBackStackName(int backStackIndex, String path) {
        return backStackIndex + "-" + path;
    }
}
