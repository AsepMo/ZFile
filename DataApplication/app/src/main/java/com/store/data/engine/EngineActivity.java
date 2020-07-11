package com.store.data.engine;

import android.annotation.TargetApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.preference.PreferenceManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.net.Uri;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;

import com.store.data.R;
import com.store.data.engine.Api;
import com.store.data.engine.app.AnalyticsManager;
import com.store.data.engine.app.ActionBarActivity;
import com.store.data.engine.app.SystemBarTintManager;
import com.store.data.engine.app.dashboard.DashboardFragment;
import com.store.data.engine.app.transit.TransitActivity;
import com.store.data.engine.app.updater.UpdaterService;
import com.store.data.engine.app.updater.Updater;
import com.store.data.engine.app.updater.UpdateModel;
import com.store.data.engine.app.updater.UpdateListener;
import com.store.data.engine.app.updater.UpgradeUtil;
import com.store.data.engine.app.settings.AppSettings;
import com.store.data.engine.app.settings.SettingsActivity;
import com.store.data.engine.view.menu.DrawerAdapter;
import com.store.data.engine.view.menu.DrawerItem;
import com.store.data.engine.view.menu.SimpleItem;
import com.store.data.engine.view.menu.SpaceItem;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.ls.directoryselector.DirectoryDialog;


public class EngineActivity extends ActionBarActivity implements DrawerAdapter.OnItemSelectedListener, DirectoryDialog.Listener
{
	public static void start(Context c)
	{
		Intent mApplication = new Intent(c, EngineActivity.class);
		c.startActivity(mApplication);
	}
	
    private static final int POS_DASHBOARD = 0;
    private static final int POS_PROFILE = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_MEDIA = 3;
    private static final int POS_LOGOUT = 5;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
	private AppSettings settings;
	private String mDirPath;
    public static final String UPGRADE = "upgrade";
    private UpdaterService loadFileService;
    
    private boolean isRegisteredService;
	
	private static final String TAG = "DataActivity";
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
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
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setSubtitle("The Best Of File Manager");
        setSupportActionBar(toolbar);

		slidingRootNav = new SlidingRootNavBuilder(this)
			.withToolbarMenuToggle(toolbar)
			.withMenuOpened(false)
			.withContentClickableWhenMenuOpened(false)
			.withSavedState(savedInstanceState)
			.withMenuLayout(R.layout.menu_left_drawer)
			.inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
													  createItemFor(POS_DASHBOARD).setChecked(true),
													  createItemFor(POS_PROFILE),
													  createItemFor(POS_MESSAGES),
													  createItemFor(POS_MEDIA),
													  new SpaceItem(48),
													  createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = (RecyclerView)findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);

		settings = AppSettings.getSettings(this);
		mDirPath = createDownLoadUpgradeDir(getApplicationContext());
		new Updater(EngineActivity.this, "https://asepmo-story.000webhostapp.com/updater/updater_zfile.json", new UpdateListener() {
				@Override
				public void onJsonDataReceived(final UpdateModel updateModel, JSONObject jsonObject) {
					if (Updater.getCurrentVersionCode(EngineActivity.this) < updateModel.getVersionCode()) {
						new AlertDialog.Builder(EngineActivity.this)
                            .setTitle(getString(R.string.actions_update))
                            .setCancelable(updateModel.isCancellable())
                            .setPositiveButton(getString(R.string.btn_update), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
									String url = updateModel.getUrl();
                                    Intent intent = new Intent(getApplicationContext(), UpdaterService.class);
									intent.putExtra("down_load_path", mDirPath);
									intent.putExtra("down_load_url", url);
									bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                                }
                            })
                            .show();
					}
				}

				@Override
				public void onError(String error) {
					// Do something

				}
			}).execute();
		changeActionBarColor();
    }

	@Override
	public void onItemSelected(int position)
	{
		// TODO: Implement this method
		if (position == POS_DASHBOARD)
		{
			showFragment(new DashboardFragment());
		}
		if (position == POS_PROFILE)
		{
			TransitActivity.start(EngineActivity.this, TransitActivity.TEXT_EDITOR);
		}
		if (position == POS_MESSAGES)
		{
			TransitActivity.start(EngineActivity.this, TransitActivity.TEXT_EDITOR);
		}
		if (position == POS_MEDIA)
		{
			TransitActivity.start(EngineActivity.this, TransitActivity.TEXT_EDITOR);
		}
		if (position == POS_LOGOUT)
		{
			TransitActivity.start(EngineActivity.this, TransitActivity.EXIT);
            finish();
        }
        slidingRootNav.closeMenu();
        //Fragment selectedScreen = EngineFragment.createFor(screenTitles[position]);
        //showFragment(selectedScreen);
	}

	@Override
	public void onDirectorySelected(File dir)
	{
		// TODO: Implement this method
		settings.setStorePath(dir.getPath());
		settings.saveDeferred();
		Toast.makeText(this, dir.getPath(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCancelled()
	{
		// TODO: Implement this method
		Toast.makeText(this, "Settings Folder Cancelled", Toast.LENGTH_SHORT).show();
	}

	public void showFragment(Fragment fragment)
	{
        getSupportFragmentManager().beginTransaction()
			.replace(R.id.content_frame, fragment)
			.commit();
    }

    private DrawerItem createItemFor(int position)
	{
        return new SimpleItem(screenIcons[position], screenTitles[position])
			.withIconTint(color(R.color.textColorSecondary))
			.withTextTint(color(R.color.textColorPrimary))
			.withSelectedIconTint(color(R.color.colorAccent))
			.withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles()
	{
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons()
	{
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++)
		{
            int id = ta.getResourceId(i, 0);
            if (id != 0)
			{
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res)
	{
        return ContextCompat.getColor(this, res);
    }

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		settings.load();
		changeActionBarColor();
		Toast.makeText(this, settings.getStorePath(), Toast.LENGTH_SHORT).show();
		
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(sharedPrefsChangeListener);
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(sharedPrefsChangeListener);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
		{
			//Fragment selectedScreen = DataFragment.createFor("Settings");
			//showFragment(selectedScreen);
			AnalyticsManager.logEvent("Setting");
			TransitActivity.start(EngineActivity.this, TransitActivity.SETTINGS);
            return true;
        }
		else if (id == R.id.action_about)
		{
			AnalyticsManager.logEvent("About");
			TransitActivity.start(EngineActivity.this, TransitActivity.ABOUT);
            return true;
        }
		else if (id == R.id.action_menu)
		{
			//final View menuItemView = findViewById(R.id.action_menu);
			//showPopup(menuItemView);
			AnalyticsManager.logEvent("AsepMo");
			TransitActivity.start(EngineActivity.this, TransitActivity.ASEPMO);
			return true;
		}
		else
		{
			return super.onOptionsItemSelected(item);
		}
	}

	private final SharedPreferences.OnSharedPreferenceChangeListener sharedPrefsChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
		{
			settings.load();
		}
	};
	public void setStopDownload(){
		if (isRegisteredService) {
			unbindService(serviceConnection);
			isRegisteredService = false;
		}
	}
	
	public void setDeleteFile()
	{
		if (isRegisteredService) {
			unbindService(serviceConnection);
			isRegisteredService = false;
		}
		File dir = new File(mDirPath);
		UpgradeUtil.deleteContentsOfDir(dir);
	}

	private static String createDownLoadUpgradeDir(Context context) {
        String dir = null;
        final String dirName = UPGRADE;
        File root = null;
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            root = context.getExternalFilesDir(null);
        } else {
            root = context.getFilesDir();
        }
        File file = new File(root, dirName);
        file.mkdirs();
        dir = file.getAbsolutePath();
        return dir;
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            loadFileService = ((UpdaterService.LocalBinder) service).getService();
            isRegisteredService = true;

            loadFileService.setOnProgressChangeListener(new UpdaterService.onProgressChangeListener() {
					@Override
					public void onProgressChange(final int progress, final String message) {
						/*handler.post(new Runnable() {
						 @Override
						 public void run() {
						 messageTextView.setText(message + ",Progress" + progress + "%");
						 }
						 });*/
					}
				});
            Log.i(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected");
            loadFileService = null;
            isRegisteredService = false;
        }
    };
	
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

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		setDeleteFile();
	}

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		//super.onBackPressed();
		if (slidingRootNav.isMenuOpened())
		{
			slidingRootNav.closeMenu();
		}
		else
		{
			slidingRootNav.openMenu();
		}
	}
}

