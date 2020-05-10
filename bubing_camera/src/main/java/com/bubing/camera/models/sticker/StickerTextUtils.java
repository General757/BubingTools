package com.bubing.camera.models.sticker;

import com.bubing.camera.models.sticker.entity.TextStickerData;

import java.util.Arrays;

/**
 * @ClassName: StickerTextUtils
 * @Description: 贴纸-工具类
 * @Author: bubing
 * @Date: 2020-05-09 15:51
 */
public class StickerTextUtils {
    /**
     * 添加文字贴纸的文字数据
     *
     * @param textStickerData 文字贴纸的文字数据
     */
    public static void addTextStickerData(TextStickerData... textStickerData) {
        StickerModel.textDataList.addAll(Arrays.asList(textStickerData));
    }

    /**
     * 清空文字贴纸的数据
     */
    public static void clearTextStickerDataList() {
        StickerModel.textDataList.clear();
    }
}
