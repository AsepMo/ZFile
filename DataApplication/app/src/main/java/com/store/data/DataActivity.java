package com.store.data;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.store.data.R;
import com.store.data.engine.app.about.AboutActivity;
import com.store.data.engine.app.settings.SettingsActivity;
import com.store.data.engine.EngineActivity;

public class DataActivity extends AppCompatActivity
{

	public static void start(Context c, String value){
		Intent mApplication = new Intent(c, DataActivity.class);
		c.startActivity(mApplication);
	}
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_black));
		}
		//setTheme(R.style.ZTheme);
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

