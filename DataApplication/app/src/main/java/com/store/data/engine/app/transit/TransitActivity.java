package com.store.data.engine.app.transit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build;
import android.view.WindowManager;

import com.store.data.R;
import com.store.data.engine.app.about.AboutActivity;
import com.store.data.engine.app.settings.SettingsActivity;
import com.store.data.engine.app.shutdown.Shutdown;
import com.store.data.engine.app.editor.TextEditorActivity;

public class TransitActivity extends Activity
{

	public static String TRANSIT = "transit";
	public static String ABOUT = "About";
	public static String TEXT_EDITOR = "TextEditor";
	public static String SETTINGS = "Settings";
	public static String SHUTDOWN = "Shutdown";
	
	public static String EXIT = "Exit";
	public static void start(Context c, String value){
		Intent mApplication = new Intent(c, TransitActivity.class);
		mApplication.putExtra(TRANSIT, value);
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
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_start);
		final String transition = getIntent().getStringExtra(TRANSIT);
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
					switch (transition)
					{
						case "About":
							AboutActivity.start(TransitActivity.this);
							// close this activity
							finish();
							break;
						case "Settings":
							SettingsActivity.start(TransitActivity.this);
							// close this activity
							finish();
							break;
						case "TextEditor":
							TextEditorActivity.start(TransitActivity.this);
							// close this activity
							finish();
							break;	
						case "Shutdown":
							Shutdown.start(TransitActivity.this);
							// close this activity
							finish();
							break;
						case "Exit":
							finish();
							break;
					}
				}
			}, SPLASH_TIME_OUT);
    }

}

