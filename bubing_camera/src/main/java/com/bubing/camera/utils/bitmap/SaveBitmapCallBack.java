package com.bubing.camera.utils.bitmap;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: SaveBitmapCallBack
 * @Description: 保存图片到本地的回调
 * @Author: bubing
 * @Date: 2020-05-09 15:32
 */
public interface SaveBitmapCallBack {
    void onSuccess(File file);

    void onIOFailed(IOException exception);

    void onCreateDirFailed();
}
