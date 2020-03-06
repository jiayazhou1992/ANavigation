package com.ya.anavigationlib;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.ya.anavigationlib.core.ANavController;
import com.ya.anavigationlib.core.ANavHost;
import com.ya.anavigationlib.core.ANavigation;

import timber.log.Timber;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/24 18:17
 * @DESC *****************************
 */
public class ANavHostFragment extends Fragment implements ANavHost {

    private final String KEY_NAV_CON_STATE = "nav_controller_state";

    private ANavController mNavController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Timber.e("onAttach");
        requireFragmentManager().beginTransaction()
                .setPrimaryNavigationFragment(this)
                .commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.e("onCreate");
        mNavController = new ANavController(requireActivity());
        mNavController.addNavigator(new AFragmentNavigator(requireActivity(), getChildFragmentManager(), getId()));
        mNavController.addNavigator(new ADialogFragmentNavigator(requireActivity(), getChildFragmentManager()));
        mNavController.setOnBackPressedDispatcher(this, requireActivity().getOnBackPressedDispatcher());

        if (savedInstanceState != null) {
            Bundle navState = savedInstanceState.getBundle(KEY_NAV_CON_STATE);
            mNavController.restoreState(navState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.e("onCreateView");
        FragmentContainerView containerView = new FragmentContainerView(inflater.getContext());
        containerView.setId(getId());
        return containerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.e("onViewCreated");

        ANavigation.setNavController(view, mNavController);
    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Timber.e("onInflate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.e("onActivityCreated");

    }

    @Override
    public ANavController getNavController() {
        if (mNavController == null) {
            throw new IllegalArgumentException("NavController为空");
        }
        return mNavController;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle navState = mNavController.saveState();
        if (navState != null) {
            outState.putBundle(KEY_NAV_CON_STATE, navState);
        }
    }
}
