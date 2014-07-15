package com.instasolutions.instadj;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.app.PendingIntent;

public class InstaDJ extends Application {
    // uncaught exception handler variable
    private UncaughtExceptionHandler defaultUEH;

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
        new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {


            	ListeningRoom.mThis.getCurrentRoomFragment().closeRoom();
                // re-throw critical exception further to the os (important)
                defaultUEH.uncaughtException(thread, ex);
            }
        };

    public InstaDJ() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception 
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }
}