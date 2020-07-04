package com.store.data.engine.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.store.data.engine.Api;

public abstract class ActionBarActivity extends AppCompatActivity{
	public abstract void setUpStatusBar();
    public abstract void setUpDefaultStatusBar();
	
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

