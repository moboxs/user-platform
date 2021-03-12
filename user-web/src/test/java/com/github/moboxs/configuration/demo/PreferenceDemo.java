package com.github.moboxs.configuration.demo;

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
    public static void main(String[] args) {
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.put("prefer-key", "value111");

    }
}
