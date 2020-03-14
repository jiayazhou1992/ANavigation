package com.ya.anavigationlib.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2020/3/11 17:11
 * @DESC *****************************
 */
public class FStackEntry implements Parcelable {

    String tag;
    String path;
//    boolean addToBackStack;

    public FStackEntry() {
    }

    public FStackEntry(String tag, String path) {
        this.tag = tag;
        this.path = path;
//        this.addToBackStack = addToBackStack;
    }

    public FStackEntry(Parcel parcel){
        this.tag = parcel.readString();
        this.path = parcel.readString();
//        this.addToBackStack = parcel.readByte() == 0 ? false : true;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    public boolean isAddToBackStack() {
//        return addToBackStack;
//    }
//
//    public void setAddToBackStack(boolean addToBackStack) {
//        this.addToBackStack = addToBackStack;
//    }

    public static final Parcelable.Creator<FStackEntry> CREATOR =
            new Parcelable.Creator<FStackEntry>() {
                @Override
                public FStackEntry createFromParcel(Parcel in) {
                    return new FStackEntry(in);
                }

                @Override
                public FStackEntry[] newArray(int size) {
                    return new FStackEntry[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag);
        dest.writeString(this.path);
//        dest.writeByte((byte) (this.addToBackStack ? 1 : 0));
    }
}
