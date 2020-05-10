package com.bubing.camera.constant;

/**
 * @ClassName: StartupType
 * @Description: 启动模式
 * @Author: bubing
 * @Date: 2020-05-09 14:13
 */
public enum StartupType {

    //CAMERA-相机
    CAMERA(10),

    //CAMERA-身份证正面
    CAMERA_IDCARD_FRONT(11),

    //CAMERA-身份证反面
    CAMERA_IDCARD_BACK(12),

    // CAMERA-竖版营业执照
    CAMERA_COMPANY_PORTRAIT(13),

    // CAMERA-横版营业执照
    CAMERA_COMPANY_LANDSCAPE(14),

    //ALBUM-相册专辑
    ALBUM(20),

    //ALBUM_CAMERA-带有相机按钮的相册专辑
    ALBUM_CAMERA(30);

    private int value;

    private StartupType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static StartupType valueOf(int value) {
        StartupType[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            StartupType v = var1[var3];
            if (v.value == value) {
                return v;
            }
        }

        return null;
    }
}
