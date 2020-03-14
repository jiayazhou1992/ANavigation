package com.ya.anavigationlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ya.anavigationlib.core.ANavigation;
import com.ya.anavigationlib.core.ANavigator;

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
public class ADialogFragmentNavigator implements ANavigator {

    private final String KEY_BACK_STACK_NAMES = "navigation_backStack_names";

    private Context mContext;
    private FragmentManager mFragmentManager;
    private ArrayDeque<String> mBackStack = new ArrayDeque<>();

    private LifecycleEventObserver mObserver = new LifecycleEventObserver() {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_STOP) {
                DialogFragment dialogFragment = (DialogFragment) source;
                if (!dialogFragment.requireDialog().isShowing()) {
                    ANavigation.findNavController(dialogFragment).popBackStack();
                }
            }
        }
    };

    public ADialogFragmentNavigator(Context mContext, FragmentManager mFragmentManager) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
    }

    @Override
    public boolean navigation(String path, Bundle args, Object navigation) {
        if (navigation != null) {
            DialogFragment frag = (DialogFragment)navigation;
            frag.setArguments(args);
            frag.getLifecycle().addObserver(mObserver);
            frag.show(mFragmentManager, generateBackStackName(mBackStack.size() + 1, path));
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
        DialogFragment fragment = (DialogFragment) mFragmentManager.findFragmentByTag(generateBackStackName(mBackStack.size(), mBackStack.peekLast()));
        fragment.getLifecycle().removeObserver(mObserver);
        fragment.dismiss();
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
                    DialogFragment fragment = (DialogFragment) mFragmentManager.findFragmentByTag(generateBackStackName(mBackStack.size(), name));
                    fragment.getLifecycle().addObserver(mObserver);
                }
            }
        }
    }

    private String generateBackStackName(int backStackIndex, String path) {
        String name = backStackIndex + "-" + path;
        Timber.i(name);
        return name;
    }
}
