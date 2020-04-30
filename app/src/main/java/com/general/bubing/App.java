package com.general.bubing;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.bubing.framework.BaseApplication;
import com.general.bubing.framework.UserToken;

import org.json.JSONObject;

public class App extends BaseApplication {

    public static final int MESSAGE_TOKEN_CHANGE = 0x01;

    public static final int MESSAGE_IMAGE_CHANGE = 0x02;

    public static final int MESSAGE_NAME_CHANGE = 0x03;

    static final String JPUSH_TOKENID = App.class.getName() + ".JPUSH_TOKENID";

    static final String KEY_USR_TOKEN = App.class.getName() + ".KEY_USR_TOKEN";

    static final String IS_FIRSTSTART = App.class.getName() + ".IS_FIRSTSTART";

    static final String USER_NAME = App.class.getName() + ".USER_NAME";

    UserToken mUserToken;

    @Override
    public void onCreate() {
        super.onCreate();

        String versionName = "";
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        String saveLocal = getSharedString(KEY_USR_TOKEN, null);
        if (saveLocal != null) {
            try {
                JSONObject json = new JSONObject(saveLocal);
                int userid = json.getInt("userid");
                String username = json.getString("username");
                String userToken = json.getString("userToken");
                String nickname = json.getString("nickname");
                String userImage = json.getString("userImage");

                mUserToken = new UserToken(userid, username, userToken, nickname, userImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 保存登录令牌到注册表 */
    void saveToken() {
        String saveLocal = mUserToken == null ? null : mUserToken.toString();
        setSharedString(KEY_USR_TOKEN, saveLocal);
    }

    /* 设置用户token */
    public void setUserToken(UserToken userToken) {
        if (userToken == null && this.mUserToken == null)
            return;

        this.mUserToken = userToken;

        saveToken();

        if (currAgent != null) /* 通知各个页面token发生变化 */
            currAgent.sendMessage(MESSAGE_TOKEN_CHANGE);
    }

    /* 更新用户昵称 */
    public void updateNickName(String nickname) {
        if (mUserToken != null) {
            mUserToken.setNickname(nickname);
            saveToken();

            if (currAgent != null) /* 通知各个页面用户昵称发生变化 */
                currAgent.sendMessage(MESSAGE_NAME_CHANGE);
        }
    }

    /* 更新用户头像 */
    public void updateUsrImage(String imageUrl) {
        if (mUserToken != null) {
            mUserToken.setUserImage(imageUrl);
            saveToken();

            if (currAgent != null)  /* 通知各个页面，用户头像发生变化 */
                currAgent.sendMessage(MESSAGE_IMAGE_CHANGE);
        }
    }

    /* 获取用户token */
    public UserToken getUserToken() {
        return mUserToken;
    }

    public boolean getFirstStart() {
        return getSharedBoolean(IS_FIRSTSTART, true);
    }

    public void setFirstStart(boolean firstStart) {
        setSharedBoolean(IS_FIRSTSTART, firstStart);
    }

    /* 帐号（手机号） */
    public void setUserName(String username) {
        if (username == null)
            return;

        setSharedString(USER_NAME, username);
    }

    public String getUserName() {
        return getSharedString(USER_NAME, null);
    }

    public String getPushToken() {
        return getSharedString(JPUSH_TOKENID, null);
    }
}
