package com.store.data;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.store.data.R;
import com.store.data.engine.Api;
import com.store.data.engine.EngineActivity;
import com.store.data.engine.app.SystemBarTintManager;
import com.store.data.engine.app.about.AboutActivity;
import com.store.data.engine.app.settings.SettingsActivity;

public class DataActivity extends AppCompatActivity
{

	public static String TAG =  DataActivity.class.getSimpleName();
	
	public static void start(Context c, String value){
		Intent mApplication = new Intent(c, DataActivity.class);
		c.startActivity(mApplication);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        //int color = Api.getStatusBarColor(SettingsActivity.getPrimaryColor(this));
		if (Api.hasLollipop())
		{
            getWindow().setStatusBarColor(Color.BLACK);
        }
        else if (Api.hasKitKat())
		{
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setTintColor(Api.getStatusBarColor(Color.BLACK));
            systemBarTintManager.setStatusBarTintEnabled(true);
        }
        //setUpStatusBar();
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_start);
		int SPLASH_TIME_OUT = 5000;
		new Handler().postDelayed(new Runnable() {

				/*
				 * Showing splash screen with a timer. This will be useful when you
				 * want to show case your app logo / company
				 */

				@Override
				public void run()
				{
					// This method will be executed once the timer is over
					// Start your app main activity
					EngineActivity.start(DataActivity.this);
					DataActivity.this.finish();
				}
			}, SPLASH_TIME_OUT);
    }

}

