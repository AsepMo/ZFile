package com.store.data.engine.app.transit;

import android.support.customtabs.CustomTabsIntent;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build;
import android.net.Uri;
import android.view.WindowManager;

import com.store.data.R;
import com.store.data.engine.app.about.AboutActivity;
import com.store.data.engine.app.settings.SettingsActivity;
import com.store.data.engine.app.shutdown.Shutdown;
import com.store.data.engine.app.asepmo.AsepMoActivity;
import com.store.data.engine.app.webclient.WebClientActivity;
import com.store.data.engine.app.webserver.WebServerActivity;
import com.store.data.engine.app.webclient.CustomTabActivityHelper;
import com.store.data.engine.app.webclient.WebviewFallback;
import com.store.data.engine.app.webclient.SmartWebView;

public class TransitActivity extends Activity
{

	public static String TRANSIT = "transit";
	public static String ABOUT = "About";
	public static String TEXT_EDITOR = "TextEditor";
	public static String SETTINGS = "Settings";
	public static String SHUTDOWN = "Shutdown";
	public static String ASEPMO = "AsepMo";
	public static String WEB_CLIENT = "WebClient";
	public static String WEB_SERVER = "WebServer";
	
	public static String EXIT = "Exit";
	private PendingIntent createPendingIntent(Activity c)
	{
        Intent actionIntent = new Intent(c, AsepMoActivity.class);
		return PendingIntent.getActivity(c, 0, actionIntent, 0);
    }
	
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
						case "AsepMo":
							AsepMoActivity.start(TransitActivity.this);
							// close this activity
							finish();
							break;	
						case "WebClient":
							String url = "https://www.google.com";
							CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
							String shareLabel = getString(R.string.app_name);
							Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
							PendingIntent pendingIntent = createPendingIntent(TransitActivity.this);
							intentBuilder.setActionButton(icon, shareLabel, pendingIntent);
							intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.abc_ic_ab_back_material));
							intentBuilder.setStartAnimations(TransitActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
							intentBuilder.setExitAnimations(TransitActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

							CustomTabActivityHelper.openCustomTab(TransitActivity.this, intentBuilder.build(), Uri.parse(url), new WebviewFallback());			
							// close this activity
							finish();
							break;	
						case "WebServer":
							WebServerActivity.start(TransitActivity.this);
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

