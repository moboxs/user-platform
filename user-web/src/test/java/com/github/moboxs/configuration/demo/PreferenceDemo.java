package com.github.moboxs.configuration.demo;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * java.util.prefs.Preferences
 * 用戶偏好
 * windows 注冊表實現
 * Linux/Unix 使用文件實現
 *
 *
 */
public class PreferenceDemo {
    public static void main(String[] args) throws BackingStoreException {
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.put("prefer-key", "value111");
        userPreferences.flush();
        String str = userPreferences.get("prefer-key", null);
        System.out.println(str);
    }
}
