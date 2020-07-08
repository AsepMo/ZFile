package com.store.data.engine.app.ftp;

import android.annotation.TargetApi;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.store.data.R;
import com.store.data.engine.Api;
import com.store.data.engine.app.AnalyticsManager;
import com.store.data.engine.app.ActionBarActivity;
import com.store.data.engine.app.SystemBarTintManager;
import com.store.data.engine.app.settings.AppSettings;
import com.store.data.engine.app.settings.SettingsActivity;

public abstract class SingleFragmentActivity extends ActionBarActivity
{

	protected abstract Fragment createFragment();
	
	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		changeActionBarColor();
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(R.style.ZTheme_NoActionBar);
        if (Api.hasLollipop())
		{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        else if (Api.hasKitKat())
		{
            setTheme(R.style.ZTheme_Translucent);
        }
        setUpStatusBar();
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ftp_server);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_frame);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
				.add(R.id.content_frame, fragment)
				.commit();
        }
		changeActionBarColor();
	}

	private final Handler handler = new Handler();
	private Drawable oldBackground;

    private void changeActionBarColor()
	{

		int color = SettingsActivity.getPrimaryColor(this);
		Drawable colorDrawable = new ColorDrawable(color);

		if (oldBackground == null)
		{
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
		}
		else
		{
			TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, colorDrawable });
            getSupportActionBar().setBackgroundDrawable(td);
			td.startTransition(200);
		}

		oldBackground = colorDrawable;

        setUpStatusBar();
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who)
		{
			getSupportActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when)
		{
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what)
		{
			handler.removeCallbacks(what);
		}
	};

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void setUpStatusBar()
	{
		// TODO: Implement this method
		int color = Api.getStatusBarColor(SettingsActivity.getPrimaryColor(this));
        if (Api.hasLollipop())
		{
            getWindow().setStatusBarColor(color);
        }
        else if (Api.hasKitKat())
		{
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setTintColor(color);
            systemBarTintManager.setStatusBarTintEnabled(true);
        }
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void setUpDefaultStatusBar()
	{
		// TODO: Implement this method
		int color = ContextCompat.getColor(this, R.color.alertColor);
        if (Api.hasLollipop())
		{
            getWindow().setStatusBarColor(color);
        }
        else if (Api.hasKitKat())
		{
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setTintColor(Api.getStatusBarColor(color));
            systemBarTintManager.setStatusBarTintEnabled(true);
        }
    }

	@Override
	public String getTag()
	{
		// TODO: Implement this method
		return null;
	}
}

