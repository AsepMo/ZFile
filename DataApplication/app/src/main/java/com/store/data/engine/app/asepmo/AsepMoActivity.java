package com.store.data.engine.app.asepmo;

import android.support.customtabs.CustomTabsIntent;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.net.Uri;

import com.store.data.R;
import com.store.data.engine.app.webclient.WebClientActivity;
import com.store.data.engine.app.webclient.CustomTabActivityHelper;
import com.store.data.engine.app.webclient.WebviewFallback;
import com.store.data.engine.app.webclient.SmartWebView;

public class AsepMoActivity extends Activity
{
	public static void start(Context c)
	{
		Intent intent = new Intent(c, AsepMoActivity.class);
        c.startActivity(intent);
	}
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asepmo);

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
					//Intent i = new Intent(AsepMoActivity.this, WebClientActivity.class);
					//startActivity(i);
					String url = "https://asepmo-story.000webhostapp.com";
					CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
					String shareLabel = getString(R.string.app_name);
					Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
					PendingIntent pendingIntent = createPendingIntent(AsepMoActivity.this);
					intentBuilder.setActionButton(icon, shareLabel, pendingIntent);
					intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.abc_ic_ab_back_material));
					intentBuilder.setStartAnimations(AsepMoActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
					intentBuilder.setExitAnimations(AsepMoActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

					CustomTabActivityHelper.openCustomTab(AsepMoActivity.this, intentBuilder.build(), Uri.parse(url), new WebviewFallback());			
					
					// close this activity
					finish();
				}
			}, SPLASH_TIME_OUT);
    }
	
	private PendingIntent createPendingIntent(Activity c)
	{
        Intent actionIntent = new Intent(c, AsepMoActivity.class);
		return PendingIntent.getActivity(c, 0, actionIntent, 0);
    }
}
