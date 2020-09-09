//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bubing.tools.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;

/**
 * @ClassName: ScreenShootUitl
 * @Author: Bubing
 * @Date: 2020/9/9 4:09 PM
 * @Description: java类作用描述
 */
public class ScreenShootUitl {
    private long startCheckTime = 0L;

    public ScreenShootUitl() {
    }

    public void isCatchScreenShoot(final Context mContext, long startCheckTime, final Handler handler) {
        this.startCheckTime = startCheckTime;
        (new Thread() {
            public void run() {
                String[] columns = new String[]{"date_added", "_data"};
                Cursor cursor = null;

                try {
                    cursor = mContext.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, columns, (String) null, (String[]) null, "date_added desc");
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            String filePath = cursor.getString(cursor.getColumnIndex("_data"));
                            long addTime = cursor.getLong(cursor.getColumnIndex("date_added"));
                            if (ScreenShootUitl.this.matchAddTime(addTime) && ScreenShootUitl.this.matchPath(filePath) && ScreenShootUitl.this.matchSize(filePath, mContext)) {
                                handler.sendEmptyMessage(111);
                                return;
                            }
                        }

                        return;
                    }
                } catch (Exception var15) {
                    var15.printStackTrace();
                    return;
                } finally {
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception var14) {
                            var14.printStackTrace();
                        }
                    }

                }

            }
        }).start();
    }

    private boolean matchAddTime(long addTime) {
        return this.startCheckTime - addTime * 1000L < 3000L;
    }

    private boolean matchSize(String filePath, Context mContext) {
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        int screenHeight = ScreenUtils.getTotalScreenHeight(mContext);
        Point size = new Point(screenWidth, screenHeight);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return size.x >= options.outWidth && size.y >= options.outHeight;
    }

    private boolean matchPath(String filePath) {
        String lower = filePath.toLowerCase();
        return lower.contains("screenshot") || lower.contains("截屏");
    }
}

