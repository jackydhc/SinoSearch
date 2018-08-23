package com.sinoshem.sinosearch.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.sinoshem.sinosearch.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity堆栈式管理
 */
public class ActivityManager {

    private static volatile ActivityManager instance;

    public boolean hasMainAct = false;

    private static List<AppCompatActivity> activityStack;

    private ActivityManager() {
        activityStack = new ArrayList<AppCompatActivity>();
    }

    public int getActCounts() {
        return activityStack.size();
    }

    public Activity getMainActivity() {
        for (AppCompatActivity appCompatActivity : activityStack) {
            if (appCompatActivity instanceof MainActivity) {
                return appCompatActivity;
            }
        }
        return null;
    }

    public static ActivityManager get() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    public synchronized void add(AppCompatActivity appCompatActivity) {
        activityStack.add(appCompatActivity);
        if (appCompatActivity instanceof MainActivity) {
            hasMainAct = true;
        }
    }

    public synchronized void remove(AppCompatActivity appCompatActivity) {
        if (appCompatActivity == null) return;
        activityStack.remove(appCompatActivity);
        appCompatActivity = null;
    }


    public synchronized void finishAndRemove(AppCompatActivity appCompatActivity) {
        if (appCompatActivity == null) return;
        if (!appCompatActivity.isFinishing()) appCompatActivity.finish();
        activityStack.remove(appCompatActivity);
        appCompatActivity = null;
    }

    public synchronized void finishLoginsAct() {
        if (getActCounts() == 0) return;

        for (int i = activityStack.size() - 1; i >= 0; i--) {
            AppCompatActivity appCompatActivity = activityStack.get(i);
            /*if (appCompatActivity instanceof LoginActivity) {
                finishAndRemove(appCompatActivity);
            } else if (appCompatActivity instanceof Kr36LoginActivity) {
                finishAndRemove(appCompatActivity);
            } else if (appCompatActivity instanceof ForgotActivity) {
                finishAndRemove(appCompatActivity);
            } else if (appCompatActivity instanceof AssociatedActivity) {
                finishAndRemove(appCompatActivity);
            }*/

        }
    }


    public synchronized void finishActs(boolean isAll, boolean w) {
        if (getActCounts() == 0) return;

        for (int i = activityStack.size() - 1; i >= 0; i--) {
            AppCompatActivity appCompatActivity = activityStack.get(i);
            if (!isAll && appCompatActivity instanceof MainActivity) {
                continue;
            }

            if (isAll && appCompatActivity instanceof MainActivity) {
                hasMainAct = false;
            }

            if (appCompatActivity != null && !appCompatActivity.isFinishing()) appCompatActivity.finish();

            remove(appCompatActivity);
        }

        activityStack.clear();
    }

    public synchronized void finishActs(boolean isAll) {
        finishActs(isAll, false);
    }
}