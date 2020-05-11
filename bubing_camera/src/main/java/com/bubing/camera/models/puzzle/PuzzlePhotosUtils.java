package com.bubing.camera.models.puzzle;


import android.app.Activity;

import com.bubing.camera.callback.PuzzleCallback;
import com.bubing.camera.engine.ImageEngine;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.result.TakeResult;
import com.bubing.camera.ui.PuzzleActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * @ClassName: PuzzlePhotosUtils
 * @Description: 拼图
 * @Author: bubing
 * @Date: 2020-05-09 16:11
 */
public class PuzzlePhotosUtils {
    /**
     * 启动拼图（最多对9张图片进行拼图）
     *
     * @param act                  上下文
     * @param photos               图片集合（最多对9张图片进行拼图）
     * @param puzzleSaveDirPath    拼图完成保存的文件夹全路径
     * @param puzzleSaveNamePrefix 拼图完成保存的文件名前缀，最终格式：前缀+默认生成唯一数字标识+.png
     * @param requestCode          请求code
     * @param replaceCustom        单击替换拼图中的某张图片时，是否以startForResult的方式启动你的自定义界面，该界面与传进来的act为同一界面。false则在EasyPhotos内部完成，正常需求直接写false即可。 true的情况适用于：用于拼图的图片集合中包含网络图片，是在你的act界面中获取并下载的（也可以直接用网络地址，不用下载后的本地地址，也就是可以不下载下来），而非单纯本地相册。举例：你的act中有两个按钮，一个指向本地相册，一个指向网络相册，用户在该界面任意选择，选择好图片后跳转到拼图界面，用户在拼图界面点击替换按钮，将会启动一个新的act界面，这时，act只让用户在网络相册和本地相册选择一张图片，选择好执行
     *                             Intent intent = new Intent();
     *                             intent.putParcelableArrayListExtra(AlbumBuilder.RESULT_PHOTOS , photos);
     *                             act.setResult(RESULT_OK,intent); 并关闭act，回到拼图界面，完成替换。
     * @param imageEngine          图片加载引擎的具体实现
     */

    public static void startPuzzleWithPhotos(Activity act, ArrayList<Photo> photos, String puzzleSaveDirPath, String puzzleSaveNamePrefix, int requestCode, boolean replaceCustom, @NonNull ImageEngine imageEngine) {
        act.setResult(Activity.RESULT_OK);
        PuzzleActivity.startWithPhotos(act, photos, puzzleSaveDirPath, puzzleSaveNamePrefix, requestCode, replaceCustom, imageEngine);
    }

    public static void startPuzzleWithPhotos(FragmentActivity act, ArrayList<Photo> photos, String puzzleSaveDirPath, String puzzleSaveNamePrefix, boolean replaceCustom, @NonNull ImageEngine imageEngine, PuzzleCallback callback) {
        act.setResult(Activity.RESULT_OK);
        TakeResult.get(act).startPuzzleWithPhotos(photos, puzzleSaveDirPath, puzzleSaveNamePrefix, replaceCustom, imageEngine, callback);
    }
}
