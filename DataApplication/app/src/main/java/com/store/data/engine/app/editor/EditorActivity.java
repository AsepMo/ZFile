package com.store.data.engine.app.editor;

import android.annotation.TargetApi;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.store.data.R;
import com.store.data.engine.Api;
import com.store.data.engine.app.AnalyticsManager;
import com.store.data.engine.app.ActionBarActivity;
import com.store.data.engine.app.SystemBarTintManager;
import com.store.data.engine.app.settings.SettingsActivity;

public class EditorActivity extends ActionBarActivity
{
    public static void start(Context c)
    {
        Intent mApplication = new Intent(c, EditorActivity.class);
        c.startActivity(mApplication);
    }
	
    private final Handler handler = new Handler();
    private Drawable oldBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {setTheme(R.style.ZTheme_NoActionBar);
        if (Api.hasLollipop())
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        else if (Api.hasKitKat())
        {
            setTheme(R.style.ZTheme_Translucent);
        }
        setUpStatusBar();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_editor);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("The Best Of Editor");
        setSupportActionBar(toolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        showFragment(new EditorFragment());
        changeActionBarColor();
    }

    public void showFragment(Fragment fragment){
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, fragment)
        .commit();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        changeActionBarColor();
    }

    
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
        return null;
    }

}
