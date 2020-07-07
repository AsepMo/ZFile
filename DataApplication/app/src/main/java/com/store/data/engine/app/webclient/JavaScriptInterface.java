package com.store.data.engine.app.webclient;

import android.support.customtabs.CustomTabsIntent;
import android.app.Activity;
import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.store.data.R;
import com.store.data.engine.app.asepmo.AsepMoActivity;
import com.store.data.engine.app.webserver.WebServerActivity;
import com.store.data.engine.app.ftp.FTPActivity;

public class JavaScriptInterface
{
	private Activity activity;

	public JavaScriptInterface(Activity activiy)
	{
		this.activity = activiy;
	}

	@JavascriptInterface
	public void test()
	{
		Toast.makeText(activity.getApplicationContext(), "Javascript interface test.", 0).show();
	}

	// Show a toast from the web page
    @JavascriptInterface
    public void showToast(String toast)
	{
        Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public int getAndroidVersion()
	{
        return android.os.Build.VERSION.SDK_INT;
    }

    @JavascriptInterface
    public void showAndroidVersion(String versionName)
	{
        Toast.makeText(activity, versionName, Toast.LENGTH_SHORT).show();
    }

	@JavascriptInterface
	public void webClient()
	{
		String url = "https://www.google.com";
		CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
		String shareLabel = activity.getString(R.string.app_name);
		Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
		PendingIntent pendingIntent = createPendingIntent(activity);
		intentBuilder.setActionButton(icon, shareLabel, pendingIntent);
		intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.abc_ic_ab_back_material));
		intentBuilder.setStartAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_left);
		intentBuilder.setExitAnimations(activity, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

		CustomTabActivityHelper.openCustomTab(activity, intentBuilder.build(), Uri.parse(url), new WebviewFallback());			
		//finish();

	}

	private PendingIntent createPendingIntent(Activity c)
	{
        Intent actionIntent = new Intent(c, AsepMoActivity.class);
		return PendingIntent.getActivity(c, 0, actionIntent, 0);
    }

	@JavascriptInterface
	public void reboot()
	{
		Toast.makeText(activity, "Reboot", Toast.LENGTH_SHORT).show();
	}

	@JavascriptInterface
    public void webServer()
	{
		//Link In index.html <a href="#" onClick="JSInterface.webServer()">Web Server</a>
        WebServerActivity.start(activity);
	}

	@JavascriptInterface
	public void FTP()
	{
		FTPActivity.start(activity);
		Toast.makeText(activity, "FTP", Toast.LENGTH_SHORT).show();
	}

	@JavascriptInterface
    public void exit()
	{   
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
	}
}
