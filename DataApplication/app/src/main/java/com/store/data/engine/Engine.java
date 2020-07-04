package com.store.data.engine;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import com.store.data.engine.app.folder.FolderMe;
import com.store.data.engine.app.settings.AppSettings;

public class Engine
{

	private static Engine mInstance;
	public static void initDefault(Engine engine)
	{
		mInstance = engine;
	}

	public static Engine get()
	{
		if (mInstance == null)
		{
			mInstance = new Engine(new Builder(getApplicationContext()));
		}
		return mInstance;
	}

	private final String mEngine;
	private final boolean isEngine;
	public static Context getApplicationContext()
	{
		return getApplicationContext();
	}



	protected Engine(Builder builder)
	{
		mEngine = builder.mGear;
		isEngine = builder.isGear;
	}

	public String getEngine()
	{
		return mEngine;
	}

	public boolean getMekanik()
	{
		return isEngine;
	}

	public static class Builder
	{
		private boolean isGear = false;

		private String mGear = null;

		private Context mContext;

		public Builder(Context context)
		{
			this.mContext = context;
		}

		public Builder setFolder(Context c, AppSettings settings)
		{
			FolderMe.initDefault(new FolderMe.Builder(c)
								 .setDefaultDir(true)
								 .setFolder("ZArchive")
								 .setFolderApk(true)
								 .setFolderAudio(true)
								 .setFolderEbook(true)
								 .setFolderPicture(true)
								 .setFolderScriptMe(true)
								 .setFolderVideo(true)
								 .setFolderZip(true)
								 .build());
			if (!settings.isInitialized())
			{
				settings.setInitialized(true);
				File mFolderMe = new File(FolderMe.EXTERNAL_DIR + "/" + "ZArchive");
			    if (mFolderMe != null) settings.setStorePath(mFolderMe.getPath());
				settings.save();
			}				 
			return this;
		}
		public Builder setDownloadFile(Context c)
		{
			if (Api.isInternetOn(c))
			{

			}
			return this;
		}

		public Engine build()
		{
			this.isGear = !TextUtils.isEmpty(mGear);
			return new Engine(this);
		}
	}
}

