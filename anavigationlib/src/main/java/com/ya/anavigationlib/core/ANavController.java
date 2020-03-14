package com.ya.anavigationlib.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayDeque;
import java.util.Map;

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
//    private static final String KEY_NAVIGATOR_STATE_NAMES = "navigator_state_names";
    private final String KEY_BACK_STACK = "back_stack";

    private Context mContext;
    private ArrayDeque<ANavBackStackEntry> mBackStack = new ArrayDeque<>();
    private ANavigatorProvider mNavigatorProvider = new ANavigatorProvider();
    //宿主生命周期监听
    private LifecycleEventObserver mObserver = new LifecycleEventObserver() {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_STOP) {
                clear();
                source.getLifecycle().removeObserver(mObserver);
            }
        }
    };
    //返回事件监听
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

    public void addNavigator(ANavigator aNavigator) {
        this.mNavigatorProvider.addNavigator(aNavigator);
    }

    public void setOnBackPressedDispatcher(@NonNull LifecycleOwner owner, @NonNull OnBackPressedDispatcher dispatcher) {
        // Remove the callback from any previous dispatcher
        mOnBackPressedCallback.remove();
        // Then add it to the new dispatcher
        dispatcher.addCallback(owner, mOnBackPressedCallback);
    }

    public void setLifecycleEventObserver(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().addObserver(mObserver);
    }


    public boolean navigation(String path) {
        return navigation(path, null);
    }

    public boolean navigation(String path, Bundle args){
        String aNavigatorName;
        ANavigator aNavigator;
        //found navigation by aRouter
        Object navigation = ARouter.getInstance().build(path).with(args).navigation();
        if (navigation instanceof DialogFragment) {
            aNavigatorName = ANavigatorProvider.DIALOG_FRAGMENT_ANAVIGATOR;
        } else {
            aNavigatorName = ANavigatorProvider.FRAGMENT_ANAVIGATOR;
        }
        aNavigator = mNavigatorProvider.getANavigator(aNavigatorName);

        boolean nav = aNavigator.navigation(path, args, navigation);
        if (nav) {
            ANavBackStackEntry entry = new ANavBackStackEntry(path, args, aNavigatorName);
            mBackStack.add(entry);
        }
        updateBackPressedCallbackEnable();
        Timber.e("mBackStack size %d", mBackStack.size());
        return nav;
    }

    private ANavigator peekLastNavigator() {
        return this.mNavigatorProvider.getANavigator(mBackStack.peekLast().getNavigatorName());
    }

    public void popBackStack(){
        if (mBackStack.isEmpty()) {
            return;
        }

        boolean nav = peekLastNavigator().popBackStack();
        if (nav) {
            mBackStack.removeLast();
        }
        updateBackPressedCallbackEnable();
        Timber.e("mBackStack size %d", mBackStack.size());
    }

    public void clear(){
        if (mBackStack.isEmpty()) {
            return;
        }
        peekLastNavigator().clear();
        mNavigatorProvider.clear();
        mContext = null;
    }

    public void updateBackPressedCallbackEnable() {
        if (mBackStack.size() <= 1) {
            mOnBackPressedCallback.setEnabled(false);
        } else {
            mOnBackPressedCallback.setEnabled(true);
        }
    }

    public void dispatchOnActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        peekLastNavigator().dispatchOnActivityResult(requestCode, resultCode, data);
    }

    public Bundle saveState() {
        Bundle saveState = new Bundle();

        Bundle navigatorState = new Bundle();
        for (Map.Entry<String, ANavigator> entry : mNavigatorProvider.getNavigators().entrySet()) {
            String name = entry.getKey();
            Bundle savedState = entry.getValue().onSaveState();
            if (savedState != null) {
                navigatorState.putBundle(name, savedState);
            }
        }
        saveState.putBundle(KEY_NAVIGATOR_STATE, navigatorState);

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

            Bundle navigatorState = saveSate.getBundle(KEY_NAVIGATOR_STATE);
            for (Map.Entry<String, ANavigator> entry : mNavigatorProvider.getNavigators().entrySet()) {
                String name = entry.getKey();
                entry.getValue().onRestoreState(navigatorState.getBundle(name));
            }

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
