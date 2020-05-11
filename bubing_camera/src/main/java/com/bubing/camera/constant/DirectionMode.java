package com.bubing.camera.constant;

/**
 * @ClassName: DirectionMode
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-04-29 15:21
 */
public enum DirectionMode {

    //拍摄类型-身份证正面
    MODE_IDCARD_FRONT(1),

    //拍摄类型-身份证反面
    MODE_IDCARD_BACK(2),

    // 拍摄类型-竖版营业执照
    MODE_COMPANY_PORTRAIT(3),

    // 拍摄类型-横版营业执照
    MODE_COMPANY_LANDSCAPE(4);

    private int value;

    DirectionMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static DirectionMode valueOf(int value) {
        DirectionMode[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            DirectionMode v = var1[var3];
            if (v.value == value) {
                return v;
            }
        }

        return null;
    }
}
