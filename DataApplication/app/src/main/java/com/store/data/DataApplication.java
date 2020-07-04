package com.store.data;

import android.support.v7.app.AppCompatDelegate;
import android.app.Application;
import java.io.File;

import com.store.data.BuildConfig;
import com.store.data.engine.Api;
import com.store.data.engine.app.AnalyticsManager;
import com.store.data.engine.app.settings.AppSettings;
import com.store.data.engine.app.folder.FolderMe;
import com.store.data.engine.Engine;

public class DataApplication extends Application
{

	private static DataApplication sInstance;
	static {
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
	}
	public final AppSettings settings = new AppSettings(this);
	private static boolean isTelevision;

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		if (!BuildConfig.DEBUG)
		{
            AnalyticsManager.intialize(getApplicationContext());
        }

		Engine.initDefault(new Engine.Builder(getApplicationContext())
						   .setFolder(getApplicationContext(), settings)
						   .setDownloadFile(getApplicationContext())
						   .build());
		
        sInstance = this;

		isTelevision = Api.isTelevision(this);
	}

	public static synchronized DataApplication getInstance()
	{
        return sInstance;
    }

	public static boolean isTelevision()
	{
        return isTelevision;
    }
}


