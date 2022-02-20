package com.giladmovieapp.moveonotesapp;

import android.app.Application;

public class Global extends Application {
    private  int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
