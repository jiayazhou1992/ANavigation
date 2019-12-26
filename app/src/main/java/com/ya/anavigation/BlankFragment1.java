package com.ya.anavigation;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ya.anavigationlib.core.ANavigation;

@Route(path = "/main/fragment1")
public class BlankFragment1 extends Fragment {

    @Autowired
    String hello;

    private BlankFragment1ViewModel mViewModel;

    TextView textView;

    public static BlankFragment1 newInstance() {
        return new BlankFragment1();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blank_fragment1_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ARouter.getInstance().inject(this);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ANavigation.findNavController(BlankFragment1.this).navigation("/main/fragment2");
            }
        });
        textView = view.findViewById(R.id.textView);
        textView.setText(hello);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BlankFragment1ViewModel.class);
        // TODO: Use the ViewModel
    }

}
