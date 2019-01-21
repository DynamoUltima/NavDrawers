package com.example.joel.navdrawers;

public class Constants {

    public static String KEY_EMAIL = "email";
    public static String KEY_PASSWORD = "password";
    public interface ACTION {
        public static String MAIN_ACTION = "com.example.joel.navdrawers.action.main";
        public static String PREV_ACTION = "com.example.joel.navdrawers.action.prev";
        public static String PLAY_ACTION = "com.example.joel.navdrawers.action.play";
        public static String NEXT_ACTION = "com.example.joel.navdrawers.action.next";
        public static String STARTFOREGROUND_ACTION = "com.example.joel.navdrawers.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.example.joel.navdrawers.action.stopforeground";
    }
    public interface NOTIFICATION_ID{
        public static int FOREGROUND_SERVICE = 101;
    }
}