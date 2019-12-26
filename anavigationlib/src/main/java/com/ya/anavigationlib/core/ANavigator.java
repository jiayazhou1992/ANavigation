package com.ya.anavigationlib.core;

import android.os.Bundle;

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

    Bundle onSaveState();

    void onRestoreState(Bundle saveState);
}
