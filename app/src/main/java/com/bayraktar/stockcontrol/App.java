package com.bayraktar.stockcontrol;

import android.app.Application;
import android.util.Log;

public class App extends Application {
    public static String Test = "Test!";

    public App() {
        Log.d("TAG", "App: ");
    }
}
