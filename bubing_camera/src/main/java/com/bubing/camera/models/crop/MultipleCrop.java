package com.bubing.camera.models.crop;

import android.app.Activity;

import com.bubing.camera.exception.BException;
import com.bubing.camera.models.ImageType;
import com.bubing.camera.models.ResultPhoto;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.utils.PhotoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MultipleCrop
 * @Description: 多张图片裁剪
 * @Author: bubing
 * @Date: 2020-05-09 20:47
 */
public class MultipleCrop {
    private ArrayList<Photo> photos;
    private ArrayList<ResultPhoto> resultPhotos;
    private ImageType imageType;//CAMERA图片来源相机，OTHER图片来源其他
    private boolean hasFailed;//是否有裁切失败的标识

    public static MultipleCrop of(Activity activity, ArrayList<Photo> photos, ImageType imageType) throws BException {
        return new MultipleCrop(activity, photos, imageType);
    }

    public static MultipleCrop of(ArrayList<Photo> photos, ArrayList<ResultPhoto> cropPhotos, ImageType imageType) {
        return new MultipleCrop(photos, cropPhotos, imageType);
    }

    private MultipleCrop(Activity activity, ArrayList<Photo> photos, ImageType imageType) throws BException {
        this.photos = photos;
        ArrayList<ResultPhoto> resultPhotos = new ArrayList<>();
        for (Photo photo : photos) {
            resultPhotos.add(PhotoUtils.getCropPhoto(activity, photo, imageType));//生成临时裁切输出路径
        }
        this.resultPhotos = resultPhotos;
        this.imageType = imageType;
    }

    private MultipleCrop(ArrayList<Photo> photos, ArrayList<ResultPhoto> resultPhotos, ImageType imageType) {
        this.photos = photos;
        this.resultPhotos = resultPhotos;
        this.imageType = imageType;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public ArrayList<ResultPhoto> getResultPhotos() {
        return resultPhotos;
    }

    public void setResultPhotos(ArrayList<ResultPhoto> resultPhotos) {
        this.resultPhotos = resultPhotos;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public boolean isHasFailed() {
        return hasFailed;
    }

    public void setHasFailed(boolean hasFailed) {
        this.hasFailed = hasFailed;
    }

    /**
     * 为被裁切的图片设置已被裁切的标识
     *
     * @param resultPhoto 被裁切的图片
     * @return 该图片是否是最后一张
     */
    public Map setCropWithUri(ResultPhoto resultPhoto, boolean cropped) {
        if (!cropped)
            hasFailed = true;

        int index = resultPhotos.indexOf(resultPhoto);
        resultPhotos.get(index).setCropped(cropped);
        Map result = new HashMap();
        result.put("index", index);
        result.put("isLast", index == resultPhotos.size() - 1 ? true : false);
        return result;
    }
}

