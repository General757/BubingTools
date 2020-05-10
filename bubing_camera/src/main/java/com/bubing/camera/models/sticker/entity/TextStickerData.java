package com.bubing.camera.models.sticker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: TextStickerData
 * @Description: 文字贴纸数据类
 * @Author: bubing
 * @Date: 2020-05-09 15:52
 */
public class TextStickerData implements Parcelable {
    public static final Parcelable.Creator<TextStickerData> CREATOR = new Parcelable.Creator<TextStickerData>() {
        @Override
        public TextStickerData createFromParcel(Parcel source) {
            return new TextStickerData(source);
        }

        @Override
        public TextStickerData[] newArray(int size) {
            return new TextStickerData[size];
        }
    };
    public String stickerName;//文字贴纸的名字
    public String stickerValue;//文字贴纸的文字内容

    public TextStickerData(String stickerName, String stickerValue) {
        this.stickerName = stickerName;
        this.stickerValue = stickerValue;
    }

    protected TextStickerData(Parcel in) {
        this.stickerName = in.readString();
        this.stickerValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stickerName);
        dest.writeString(this.stickerValue);
    }

}
