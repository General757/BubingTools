package com.bubing.camera.models.compress;

import android.content.Context;
import android.text.TextUtils;

import com.bubing.camera.models.ResultPhoto;

import java.io.File;
import java.util.ArrayList;

/**
 * @ClassName: CompressImageImpl
 * @Description: 压缩照片
 * @Author: bubing
 * @Date: 2020-05-09 14:49
 * 技术博文：http://www.devio.org
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class CompressImageImpl implements CompressImage {
    private CompressImageUtil compressImageUtil;
    private ArrayList<ResultPhoto> images;
    private CompressImage.CompressListener listener;

    public static CompressImage of(Context context, CompressConfig config, ArrayList<ResultPhoto> images, CompressImage.CompressListener listener) {
        if (config.getLubanOptions() != null) {
            return new CompressWithLuBan(context, config, images, listener);
        } else {
            return new CompressImageImpl(context, config, images, listener);
        }
    }

    private CompressImageImpl(Context context, CompressConfig config, ArrayList<ResultPhoto> images, CompressImage.CompressListener listener) {
        compressImageUtil = new CompressImageUtil(context, config);
        this.images = images;
        this.listener = listener;
    }

    @Override
    public void compress() {
        if (images == null || images.isEmpty()) {
            listener.onCompressFailed(images, " images is null");
        }
        for (ResultPhoto image : images) {
            if (image == null) {
                listener.onCompressFailed(images, " There are pictures of compress  is null.");
                return;
            }
        }
        compress(images.get(0));
    }

    private void compress(final ResultPhoto image) {
        if (TextUtils.isEmpty(image.getOriginalPath())) {
            continueCompress(image, false);
            return;
        }

        File file = new File(image.getOriginalPath());
        if (file == null || !file.exists() || !file.isFile()) {
            continueCompress(image, false);
            return;
        }

        compressImageUtil.compress(image.getOriginalPath(), new CompressImageUtil.CompressListener() {
            @Override
            public void onCompressSuccess(String imgPath) {
                image.setCompressPath(imgPath);
                continueCompress(image, true);
            }

            @Override
            public void onCompressFailed(String imgPath, String msg) {
                continueCompress(image, false, msg);
            }
        });
    }

    private void continueCompress(ResultPhoto image, boolean preSuccess, String... message) {
        image.setCompressed(preSuccess);
        int index = images.indexOf(image);
        boolean isLast = index == images.size() - 1;
        if (isLast) {
            handleCompressCallBack(message);
        } else {
            compress(images.get(index + 1));
        }
    }

    private void handleCompressCallBack(String... message) {
        if (message.length > 0) {
            listener.onCompressFailed(images, message[0]);
            return;
        }

        for (ResultPhoto image : images) {
            if (!image.isCompressed()) {
                listener.onCompressFailed(images, image.getCompressPath() + " is compress failures");
                return;
            }
        }
        listener.onCompressSuccess(images);
    }
}
