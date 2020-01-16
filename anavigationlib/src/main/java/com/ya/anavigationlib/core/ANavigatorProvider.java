package com.ya.anavigationlib.core;

import com.ya.anavigationlib.ADialogFragmentNavigator;
import com.ya.anavigationlib.AFragmentNavigator;

import java.util.HashMap;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2020/1/16 11:21
 * @DESC *****************************
 */
public class ANavigatorProvider {

    public static final String DIALOG_FRAGMENT_ANAVIGATOR = "dialog_fragment_anavigator";
    public static final String FRAGMENT_ANAVIGATOR = "fragment_anavigator";

    private HashMap<String, ANavigator> aNavigatorHashMap;

    public ANavigatorProvider() {
        aNavigatorHashMap = new HashMap<>();
    }

    public void addNavigator(ANavigator aNavigator) {
        if (aNavigator instanceof ADialogFragmentNavigator) {
            aNavigatorHashMap.put(DIALOG_FRAGMENT_ANAVIGATOR, aNavigator);
        } else if (aNavigator instanceof AFragmentNavigator){
            aNavigatorHashMap.put(FRAGMENT_ANAVIGATOR, aNavigator);
        }
    }

    public ANavigator getANavigator(String name) {
        return aNavigatorHashMap.get(name);
    }
}
