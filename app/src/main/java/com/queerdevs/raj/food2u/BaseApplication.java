package com.queerdevs.raj.food2u;

import android.app.Application;

import okhttp3.MediaType;

/**
 * Created by RAJ on 3/2/2017.
 */

public class BaseApplication extends Application {

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    private static int number = 0;

    public static int getNumber() {
        return number;
    }

    public static void setNumber(int number) {
        BaseApplication.number += number;
    }

    public static void delNumber(int number) {
        if (number > 1) {
            BaseApplication.number = BaseApplication.number - number;
        }
    }

    public static void setZero(int number) {
        BaseApplication.number = 0;
    }

}
