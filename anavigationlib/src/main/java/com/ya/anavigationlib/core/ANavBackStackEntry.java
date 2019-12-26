package com.ya.anavigationlib.core;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/24 18:44
 * @DESC *****************************
 */
public class ANavBackStackEntry implements Parcelable {
    private String mPath;
    private Bundle mArgs;

    public ANavBackStackEntry(String mPath, Bundle mArgs) {
        this.mPath = mPath;
        this.mArgs = mArgs;
    }

    public ANavBackStackEntry(Parcel parcel){
        this.mPath = parcel.readString();
        this.mArgs = parcel.readBundle(getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPath);
        dest.writeBundle(mArgs);
    }

    public static final Parcelable.Creator<ANavBackStackEntry> CREATOR =
            new Parcelable.Creator<ANavBackStackEntry>() {
                @Override
                public ANavBackStackEntry createFromParcel(Parcel in) {
                    return new ANavBackStackEntry(in);
                }

                @Override
                public ANavBackStackEntry[] newArray(int size) {
                    return new ANavBackStackEntry[size];
                }
            };
}
