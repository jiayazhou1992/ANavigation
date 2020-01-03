package com.ya.anavigationlib.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ya.anavigationlib.ANavHostFragment;
import com.ya.anavigationlib.ANoController;
import com.ya.anavigationlib.BuildConfig;
import com.ya.anavigationlib.R;

import timber.log.Timber;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/24 18:19
 * @DESC *****************************
 */
public class ANavigation {

    private static Class<? extends Activity> mContainerClass;

    public static void init(Application application, boolean debug, Class mClass) {
        mContainerClass = mClass;
        if (debug) {
            ARouter.openDebug();
            ARouter.openLog();

            Timber.plant(new Timber.DebugTree());
        }
        ARouter.init(application);
    }

    public static void setNavController(View view, ANavController navController) {
        view.setTag(R.id.nav_view_tag, navController);
    }

    public static ANavController findNavController(Fragment fragment) {
        Fragment findFragment = fragment;
        while (findFragment != null) {
            if (findFragment instanceof ANavHostFragment) {
                return ((ANavHostFragment) findFragment).getNavController();
            }
            Fragment primaryNavFragment = findFragment.requireFragmentManager()
                    .getPrimaryNavigationFragment();
            if (primaryNavFragment instanceof ANavHostFragment) {
                return ((ANavHostFragment) primaryNavFragment).getNavController();
            }
            findFragment = findFragment.getParentFragment();
        }

        // Try looking for one associated with the view instead, if applicable
        View view = fragment.getView();
        if (view != null) {
            return ANavigation.findNavController(view);
        }
        throw new IllegalStateException("Fragment " + fragment + " does not have a NavController set");
    }

    public static ANavController findNavController(Activity activity, @IdRes int viewId) {
        View view = ActivityCompat.requireViewById(activity, viewId);
        return findNavController(view);
    }

    public static ANavController findNavController(View view) {
        ANavController controller = null;
        Object o = view.getTag(R.id.nav_view_tag);
        if (o != null) {
            controller = (ANavController) o;
        } else {
            controller = new ANoController(getActivityFromView(view));
        }
        return controller;
    }

    public static ANavController findNavController(Activity activity) {
        return mContainerClass == null ? new ANoController(activity) : new ANoController(activity, mContainerClass);
    }

    private static Activity getActivityFromView(View view) {
        if (null != view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }
}
