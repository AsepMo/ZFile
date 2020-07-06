package com.store.data.engine;

import android.support.v7.app.AppCompatDelegate;
import android.support.v4.text.TextUtilsCompat;
import com.store.data.engine.animation.PopupHelper;
import android.app.Activity;
import android.app.UiModeManager;
import android.graphics.Color;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Configuration;
import android.os.Build;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.Locale;
import java.util.Date;

import com.store.data.R;
import com.store.data.DataActivity;
import com.store.data.engine.app.about.AboutFragment;
import com.store.data.engine.app.settings.SettingsActivity;
import com.store.data.engine.app.shutdown.Shutdown;
import com.store.data.engine.app.shutdown.Reboot;
import com.store.data.engine.app.debug.DevTool;
import com.store.data.engine.app.debug.DebugFunction;
import com.store.data.engine.app.debug.DevToolFragment;


public class Api
{
	public Activity mContext;
	public static int FILE_MODE;
	// How to handle multiple return data
    public static boolean useClipData = true;

    static final int CODE_SD = 0;
    static final int CODE_DB = 1;
    static final int CODE_FTP = 2;

	public Api(Activity context)
	{
		this.mContext = context;
	}

	public static boolean hasFroyo()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasIceCreamSandwich()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMR1()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasMarshmallow()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasNougat()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean hasNougatMR1()
	{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

	public static void changeThemeStyle(AppCompatDelegate delegate)
	{
        int nightMode = Integer.valueOf(SettingsActivity.getThemeStyle());
        AppCompatDelegate.setDefaultNightMode(nightMode);
        delegate.setLocalNightMode(nightMode);
    }

	public static boolean isTelevision(Context context)
	{
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

	public static boolean isNetworkConnected(Context context)
	{
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

	public static final boolean isInternetOn(Context context)
	{

        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Check for network connections
		if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
			connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED)
		{

			// if connected with internet

			Toast.makeText(context, " Connected ", Toast.LENGTH_LONG).show();
			return true;

		}
		else if ( 
			connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED)
		{

			Toast.makeText(context, " Not Connected ", Toast.LENGTH_LONG).show();
			return false;
		}
		return false;
	}

	public static float dp2px(Resources resources, float dp)
	{
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp)
	{
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

	public static int dpToPx(int dp)
	{
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_MEDIUM);
        return Math.round(px);
    }

	public static boolean isRTL()
	{
        return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
			== android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
    }

	public static int getStatusBarColor(int color1)
	{
        int color2 = Color.parseColor("#000000");
        return blendColors(color1, color2, 0.9f);
    }

	public static int blendColors(int color1, int color2, float ratio)
	{
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

	public static String PREFS_FILE_NAME = "preferences";
	public static void setDebugging(final Activity c)
	{

		final DevTool.Builder builder = new DevTool.Builder(c);
		builder.addFunction(new DebugFunction("Power") {
				@Override
				public String call() throws Exception
				{
					setPowerManager(c);
					return "This Power Manager";
				}
			})
			.addFunction(new DebugFunction.Clear("Clear"))
			.addFunction(new DebugFunction("Settings") {
				@Override
				public String call() throws Exception
				{
					SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).edit();
					editor.putString("UpdatedAt", new Date(System.currentTimeMillis()).toString());
					editor.putBoolean("Reboot", true);
					editor.putString("Ringtons", "Deep.ogg");
					editor.putString("Theme", "Dark Theme");
					editor.apply();
					return "Preferences file has been created.";
				}
			}).addFunction(new DebugFunction.DumpSharedPreferences("Shared prefs", PREFS_FILE_NAME));

		builder.setTextSize(12)
			.displayAt(50, 200)
			.setTheme(DevToolFragment.DevToolTheme.DARK)
			.build();
    }

	public static void setPowerManager(final Activity c)
	{

		final DevTool.Builder builder = new DevTool.Builder(c);
		builder.addFunction(new DebugFunction("Shutdown") {
				@Override
				public String call() throws Exception
				{
					Shutdown.start(c);
					return "Power Off";
				}
			})
		.addFunction(new DebugFunction("Reboot") {
				@Override
				public String call() throws Exception
				{
					Reboot.start(c);
					return " Rebooting Android";
				}
			})
			.addFunction(new DebugFunction("Reboot Recovery"){
				@Override
				public String call() throws Exception
				{
					return "Rebooting To Recovery Mode";
				}
			})
			.addFunction(new DebugFunction("Reboot Download"){
				@Override
				public String call() throws Exception
				{
					return "Rebooting To Download Mode";
				}
			})
			.addFunction(new DebugFunction("Reboot Fastboot"){
				@Override
				public String call() throws Exception
				{
					return "Rebooting To Fastboot";
				}
			})
			.addFunction(new DebugFunction("Restart") {
				@Override
				public String call() throws Exception
				{
					
					return "Restart Android";
				}
			});
		builder.setTextSize(12)
			.displayAt(50, 200)
			.setTheme(DevToolFragment.DevToolTheme.DARK)
			.build();
    }

    private DebugFunction doSomeStuff()
	{
        return new DebugFunction() {
            @Override
            public String call() throws Exception
			{
                // Do some kind of really debugging stuff...
                return "Some stuff was done.";
            }
        };
    }

	public static void showPopup(Context c, View view)
	{
		PopupWindow showPopup = PopupHelper.newBasicPopupWindow(c);
		LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = inflater.inflate(R.layout.custom_layout, null);
		showPopup.setContentView(popupView);

		showPopup.setWidth(LayoutParams.WRAP_CONTENT);
		showPopup.setHeight(LayoutParams.WRAP_CONTENT);
		showPopup.setAnimationStyle(R.style.Animations_GrowFromTop);
		showPopup.showAsDropDown(view);
	}
}

