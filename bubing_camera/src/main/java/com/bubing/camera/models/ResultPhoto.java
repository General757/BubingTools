package com.bubing.camera.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.bubing.camera.constant.ImageType;
import com.bubing.camera.utils.BubingLog;

/**
 * @ClassName: ResultPhoto
 * @Description: 操作成功返回的处理结果
 * @Author: bubing
 * @Date: 2020-05-10 16:55
 */
public class ResultPhoto implements Parcelable {
    private static final String TAG = "Photo";
    public static final Creator<ResultPhoto> CREATOR = new Creator<ResultPhoto>() {
        @Override
        public ResultPhoto createFromParcel(Parcel source) {
            return new ResultPhoto(source);
        }

        @Override
        public ResultPhoto[] newArray(int size) {
            return new ResultPhoto[size];
        }
    };

    private Uri uri;//图片Uri
    private String name;//图片名称
    private String path;//图片全路径
    private String type;//图片类型
    private int width;//图片宽度
    private int height;//图片高度
    private long size;//图片文件大小，单位：Bytes
    private long duration;//视频时长，单位：毫秒
    private long time;//图片拍摄的时间戳,单位：毫秒
    private boolean selectedOriginal;//用户选择时是否选择了原图选项

    private ImageType imageType;//CAMERA图片来源相机，OTHER图片来源其他

    private Uri originalUri;//原始图片Uri
    private String originalPath;//原始图片全路径
    private String compressPath;//压缩图片全路径
    private boolean cropped;//是否被裁剪
    private boolean compressed;//是否被压缩

    public ResultPhoto() {
        super();
    }

    protected ResultPhoto(Parcel in) {
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.name = in.readString();
        this.path = in.readString();
        this.type = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.size = in.readLong();
        this.duration = in.readLong();
        this.time = in.readLong();
        this.selectedOriginal = in.readByte() != 0;

        this.originalUri = in.readParcelable(Uri.class.getClassLoader());
        this.originalPath = in.readString();
        this.compressPath = in.readString();
        this.cropped = in.readByte() != 0;
        this.compressed = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.type);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeLong(this.size);
        dest.writeLong(this.duration);
        dest.writeLong(this.time);
        dest.writeByte(this.selectedOriginal ? (byte) 1 : (byte) 0);

        dest.writeParcelable(this.originalUri, flags);
        dest.writeString(this.originalPath);
        dest.writeString(this.compressPath);
        dest.writeByte(this.cropped ? (byte) 1 : (byte) 0);
        dest.writeByte(this.compressed ? (byte) 1 : (byte) 0);
    }

    @Override
    public boolean equals(Object o) {
        try {
            ResultPhoto other = (ResultPhoto) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            BubingLog.e(TAG, "equals: " + BubingLog.getStackTraceString(e));
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "ResultPhoto{uri:" + uri.toString() + ", name:" + name + ", path:" + path + ", type:" + type + ", width:" + width + ", height:" + height +
                ", size:" + size + ", duration:" + duration + ", time:" + time + ", selectedOriginal:" + selectedOriginal + ", originalUri:" + originalUri.toString() +
                ", originalPath:" + originalPath + ", compressPath:" + compressPath + ", cropped:" + cropped + ", compressed:" + compressed + '}';
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSelectedOriginal() {
        return selectedOriginal;
    }

    public void setSelectedOriginal(boolean selectedOriginal) {
        this.selectedOriginal = selectedOriginal;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public Uri getOriginalUri() {
        return originalUri;
    }

    public void setOriginalUri(Uri originalUri) {
        this.originalUri = originalUri;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public boolean isCropped() {
        return cropped;
    }

    public void setCropped(boolean cropped) {
        this.cropped = cropped;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }
}
