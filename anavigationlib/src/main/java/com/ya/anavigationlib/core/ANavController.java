package com.ya.anavigationlib.core;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import timber.log.Timber;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/24 18:14
 * @DESC *****************************
 */
public class ANavController {

    private final String KEY_NAVIGATOR_STATE = "navigator_state";
    private final String KEY_BACK_STACK = "back_stack";

    private final Context mContext;
    private ArrayDeque<ANavBackStackEntry> mBackStack = new ArrayDeque<>();
    private ANavigator mNavigator;

    private final OnBackPressedCallback mOnBackPressedCallback =
            new OnBackPressedCallback(false) {
                @Override
                public void handleOnBackPressed() {
                    popBackStack();
                }
            };

    public ANavController(Context mContext) {
        this.mContext = mContext;
    }

    public Context getContext() {
        return mContext;
    }

    public void setNavigator(ANavigator aNavigator) {
        this.mNavigator = aNavigator;
    }

    public void setOnBackPressedDispatcher(@NonNull LifecycleOwner owner, @NonNull OnBackPressedDispatcher dispatcher) {
        // Remove the callback from any previous dispatcher
        mOnBackPressedCallback.remove();
        // Then add it to the new dispatcher
        dispatcher.addCallback(owner, mOnBackPressedCallback);
    }


    public void navigation(String path) {
        navigation(path, null);
    }

    public void navigation(String path, Bundle args){
        boolean nav = this.mNavigator.navigation(path, args);
        if (nav) {
            ANavBackStackEntry entry = new ANavBackStackEntry(path, args);
            mBackStack.add(entry);
        }
        updateBackPressedCallbackEnable();
        Timber.e("mBackStack size %d", mBackStack.size());
    }

    public void popBackStack(){
        if (mBackStack.isEmpty()) {
            return;
        }
        boolean nav = this.mNavigator.popBackStack();
        if (nav) {
            mBackStack.removeLast();
        }
        updateBackPressedCallbackEnable();
        Timber.e("mBackStack size %d", mBackStack.size());
    }

    public void updateBackPressedCallbackEnable() {
        if (mBackStack.size() <= 1) {
            mOnBackPressedCallback.setEnabled(false);
        } else {
            mOnBackPressedCallback.setEnabled(true);
        }
    }

    public Bundle saveState() {
        Bundle saveState = new Bundle();
        saveState.putBundle(KEY_NAVIGATOR_STATE, mNavigator.onSaveState());
        if (!mBackStack.isEmpty()) {
            Parcelable[] parcelables = new Parcelable[mBackStack.size()];
            int index = 0;
            for (ANavBackStackEntry entry : mBackStack) {
                parcelables[index++] = entry;
            }
            saveState.putParcelableArray(KEY_BACK_STACK, parcelables);
        }
        return saveState;
    }

    public void restoreState(Bundle saveSate) {
        if (saveSate != null) {
            mNavigator.onRestoreState(saveSate.getBundle(KEY_NAVIGATOR_STATE));
            Parcelable[] parcelables = saveSate.getParcelableArray(KEY_BACK_STACK);
            if (parcelables != null) {
                mBackStack.clear();
                for (Parcelable parcelable : parcelables) {
                    mBackStack.add((ANavBackStackEntry) parcelable);
                }
            }
        }
    }
}
