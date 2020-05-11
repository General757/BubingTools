package com.bubing.camera.constant;

/**
 * @ClassName: Constants
 * @Description: 常量
 * @Author: bubing
 * @Date: 2020-05-09 14:44
 */
public class Constants {
    public class Type {
        public static final String GIF = "gif";
        public static final String VIDEO = "video";
    }

    //key的常量
    public class Key {
        //证件-拍摄类型标记
        public final static String PUT_CERTIFICATE_TYPE = "keyOfCertificateTypePut";
        //证件-拍摄类型标记
        public final static String RESULT_CERTIFICATE_TYPE = "keyOfCertificateTypeResult";
        //证件-图片路径标记
        public final static String RESULT_CERTIFICATE_PATH = "keyOfCertificatePathResult";

        //easyPhotos的返回数据Key
        public static final String RESULT_PHOTOS = "keyOfEasyPhotosResult";

        public static final String RESULT_SELECTED_ORIGINAL = "keyOfEasyPhotosResultSelectedOriginal";
        //预览图片的当前角标
        public static final String PREVIEW_PHOTO_INDEX = "keyOfPreviewPhotoIndex";
        //当前预览界面的专辑index
        public static final String PREVIEW_ALBUM_ITEM_INDEX = "keyOfPreviewAlbumItemIndex";
        //预览界面是否点击完成
        public static final String PREVIEW_CLICK_DONE = "keyOfPreviewClickDone";
        //拼图界面图片类型,true-Photo,false-String
        public static final String PUZZLE_FILE_IS_PHOTO = "keyOfPuzzleFilesTypeIsPhoto";
        //拼图界面图片结合
        public static final String PUZZLE_FILES = "keyOfPuzzleFiles";
        //拼图界面图片保存文件夹地址
        public static final String PUZZLE_SAVE_DIR = "keyOfPuzzleSaveDir";
        //拼图界面图片保存文件名前缀
        public static final String PUZZLE_SAVE_NAME_PREFIX = "keyOfPuzzleSaveNamePrefix";

        //拼图的返回数据Key-图片集合
        public static final String RESULT_PUZZLE_PHOTO = "keyOfPuzzlePhotoResult";
        //拼图的返回数据Key--状态
        public static final String RESULT_PUZZLE_TYPE = "keyOfPuzzleTypeResult";
    }

    //Code常量
    public class Code {
        //权限请求码
        public final static int REQUEST_CODE_PERMISSIONS = 0x12;

        //权限请求码
        public static final int REQUEST_PERMISSION = 12;

        //请求应用详情
        public static final int REQUEST_SETTING_APP_DETAILS = 14;

        //相机请求码
        public static final int REQUEST_CAMERA = 11;

        //预览activity请求码
        public static final int REQUEST_PREVIEW_ACTIVITY = 13;

        //拼图选择activity请求吗
        public static final int REQUEST_PUZZLE_SELECTOR = 16;

        //拼图activity请求吗
        public static final int REQUEST_PUZZLE = 15;

        //拼图activity请求吗
        public static final int REQUEST_PUZZLE_PHOTO = 91;

        /**
         * request Code 裁剪照片
         **/
        public final static int RC_CROP = 1001;
    }
}
