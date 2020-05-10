package com.bubing.camera.models.compress;

import com.bubing.camera.models.ResultPhoto;

import java.util.ArrayList;

/**
 * @ClassName: CompressImage
 * @Description: 压缩照片2.0
 * @Author: bubing
 * @Date: 2020-05-09 14:49
 */
public interface CompressImage {
    void compress();

    /**
     * 压缩结果监听器
     */
    interface CompressListener {
        /**
         * 压缩成功
         *
         * @param images 已经压缩图片
         */
        void onCompressSuccess(ArrayList<ResultPhoto> images);

        /**
         * 压缩失败
         *
         * @param images 压缩失败的图片
         * @param msg    失败的原因
         */
        void onCompressFailed(ArrayList<ResultPhoto> images, String msg);
    }
}
