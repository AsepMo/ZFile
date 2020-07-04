package com.store.data.engine.app.about;

import com.store.data.R;
import com.store.data.engine.Api;
import com.store.data.engine.app.ActionBarActivity;
import com.store.data.engine.app.SystemBarTintManager;
import com.store.data.engine.app.settings.SettingsActivity;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Build;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class AboutActivity extends ActionBarActivity
{
	public static final String TAG = "AboutActivity";
    private Drawable oldBackground;
	
	public static void start(Context c)
	{
		Intent mApplication = new Intent(c, AboutActivity.class);
		c.startActivity(mApplication);
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		changeActionBarColor(0);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        simulateDayNight(0);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
			.isRTL(false)
			.setImage(R.drawable.dummy_image)
			.addItem(new Element().setTitle("Version 1.0"))
			.addItem(adsElement)
			.addGroup("Connect with us")
			.addEmail("asepmo.story@gmail.com")
			.addWebsite("http://cbox-dev.github.io/CBox-Dev")
			.addFacebook("ZFile")
			.addTwitter("ZFile")
			.addYoutube("UC2H7DyQrnr2RA4RSMF0B4ZA")
			.addPlayStore("com.store.data.pro")
			.addInstagram("AsepMo")
			.addGitHub("AsepMo")
			.addItem(getCopyRightsElement())
			.create();

        setContentView(aboutPage);
		setUpStatusBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		changeActionBarColor(0);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	public void changeActionBarColor(int newColor) {

		int color = newColor != 0 ? newColor : SettingsActivity.getPrimaryColor(this);
		Drawable colorDrawable = new ColorDrawable(color);

		if (oldBackground == null) {
            getSupportActionBar().setBackgroundDrawable(colorDrawable);

        } else {
			TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, colorDrawable });
            getSupportActionBar().setBackgroundDrawable(td);
			td.startTransition(200);
		}

		oldBackground = colorDrawable;
	}
	
    Element getCopyRightsElement()
	{
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Toast.makeText(AboutActivity.this, copyrights, Toast.LENGTH_SHORT).show();
				}
			});
        return copyRightsElement;
    }

    void simulateDayNight(int currentSetting)
	{
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
			& Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO)
		{
            AppCompatDelegate.setDefaultNightMode(
				AppCompatDelegate.MODE_NIGHT_NO);
        }
		else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES)
		{
            AppCompatDelegate.setDefaultNightMode(
				AppCompatDelegate.MODE_NIGHT_YES);
        }
		else if (currentSetting == FOLLOW_SYSTEM)
		{
            AppCompatDelegate.setDefaultNightMode(
				AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

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

    public static int getStatusBarHeight(Context context)
	{
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
		{
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
	@Override
	public String getTag()
	{
		// TODO: Implement this method
		return TAG;
	}
}
