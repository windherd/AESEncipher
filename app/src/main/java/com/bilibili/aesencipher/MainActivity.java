package com.bilibili.aesencipher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "zyk";
    String content = "bilibili";
    String key = "zhangtietou12345";//任意16位字符作为key

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Log.d(TAG, "加密前：" + content);
            String encrypt = AESUtil.encryptAES(key, content);
            Log.d(TAG, "加密后：" + encrypt);
            String decrypt = AESUtil.decryptAES(key, encrypt);
            Log.d(TAG, "解密后：" + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR：" + e.getMessage());
        }
    }
}
