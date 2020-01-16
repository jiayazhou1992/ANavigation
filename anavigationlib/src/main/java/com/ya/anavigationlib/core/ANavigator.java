package com.ya.anavigationlib.core;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/24 18:14
 * @DESC *****************************
 */
public interface ANavigator {

    boolean navigation(String path, Bundle args);

    boolean popBackStack();

    void dispatchOnActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    Bundle onSaveState();

    void onRestoreState(Bundle saveState);
}
