//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bubing.tools.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.bubing.tools.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: AssetDatabaseOpenHelper
 * @Author: Bubing
 * @Date: 2020/9/9 2:56 PM
 * @Description: SQLiteDatabase工具
 */
public class AssetDatabaseOpenHelper {
    private Context context;
    private String databaseName;

    public AssetDatabaseOpenHelper(Context context, String databaseName) {
        this.context = context;
        this.databaseName = databaseName;
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        File dbFile = this.context.getDatabasePath(this.databaseName);
        if (dbFile != null && !dbFile.exists()) {
            try {
                this.copyDatabase(dbFile);
            } catch (IOException var3) {
                throw new RuntimeException("Error creating source database", var3);
            }
        }

        assert dbFile != null;

        return SQLiteDatabase.openDatabase(dbFile.getPath(), (CursorFactory) null, 0);
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        File dbFile = this.context.getDatabasePath(this.databaseName);
        if (dbFile != null && !dbFile.exists()) {
            try {
                this.copyDatabase(dbFile);
            } catch (IOException var3) {
                throw new RuntimeException("Error creating source database", var3);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), (CursorFactory) null, 1);
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    private void copyDatabase(File dbFile) throws IOException {
        InputStream stream = this.context.getAssets().open(this.databaseName);
        FileUtils.writeFile(dbFile, stream);
        stream.close();
    }
}

