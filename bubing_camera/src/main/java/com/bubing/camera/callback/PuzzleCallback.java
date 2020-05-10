package com.bubing.camera.callback;

import com.bubing.camera.models.album.entity.Photo;

/**
 * @ClassName: PuzzleCallback
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 17:35
 */
public abstract class PuzzleCallback {
    /**
     * 选择结果回调
     *
     * @param photo 返回对象：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
     */
    public abstract void onResult(Photo photo);
}
