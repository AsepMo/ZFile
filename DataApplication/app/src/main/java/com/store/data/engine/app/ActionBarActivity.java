package com.store.data.engine.app;

import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.store.data.engine.Api;
import android.content.Intent;

public abstract class ActionBarActivity extends AppCompatActivity{
	public abstract void setUpStatusBar();
    public abstract void setUpDefaultStatusBar();
	private static final String TAG = "BaseDriveActivity";

  
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Api.changeThemeStyle(getDelegate());
        super.onCreate(savedInstanceState);
		
    }

    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    public void recreate() {
        Api.changeThemeStyle(getDelegate());
        super.recreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsManager.setCurrentScreen(this, getTag());
    }

	
    public abstract String getTag();
}

