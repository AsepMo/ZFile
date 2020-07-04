package com.store.data.engine.app.folder;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import com.store.data.engine.app.settings.AppSettings;

public class FolderMe
{

	private static FolderMe mInstance;
	public static void initDefault(FolderMe engine)
	{
		mInstance = engine;
	}

	public static FolderMe get()
	{
		if (mInstance == null)
		{
			mInstance = new FolderMe(new Builder(getApplicationContext()));
		}
		return mInstance;
	}

	private final String mFolder;
	public static String EXTERNAL_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static String FOLDER = EXTERNAL_DIR + "/ZFOLDER";
	public static String FOLDER_APK = FOLDER + "/Apk";
	public static String FOLDER_IMG = FOLDER + "/Picture";
	public static String FOLDER_AUDIO = FOLDER + "/Audio";
	public static String FOLDER_SCRIPTME = FOLDER + "/ScriptMe";
	public static String FOLDER_VIDEO = FOLDER + "/Video";
	public static String FOLDER_EBOOK = FOLDER + "/Ebook";
	public static String FOLDER_ZIP = FOLDER + "/Zip";

	private final boolean isFolder;
	public static Context getApplicationContext()
	{
		return getApplicationContext();
	}


	public static String getExternalCacheDir(Context c)
	{
		return c.getExternalCacheDir().getAbsolutePath();
	}

	public static String getInternalCacheDir(Context c)
	{
		return c.getCacheDir().getAbsolutePath();
	}

	public static String getExternalFileDir(Context c, String folder)
	{
		return c.getExternalFilesDir(folder).getAbsolutePath();
	}

	public static String getInternalFileDir(Context c)
	{
		return c.getFilesDir().getAbsolutePath();
	}

	public static String getHomeDir(Context c)
	{
		return c.getFilesDir().getAbsolutePath() + "/home";
	}

	public static String getUserDir(Context c)
	{
		return c.getFilesDir().getAbsolutePath() + "/user";
	}

	protected FolderMe(Builder builder)
	{
		mFolder = builder.mPath;
		isFolder = builder.isPath;
	}

	public String getFolder()
	{
		return mFolder;
	}

	public boolean getPath()
	{
		return isFolder;
	}

	public static class Builder
	{
		private boolean isPath = false;

		private String mPath = null;
		private Context mContext;

		public Builder(Context context)
		{
			this.mContext = context;
		}

		public Builder setDefaultDir(boolean mIsFolder)
		{
			File files = new File(FolderMe.FOLDER);
			if (!files.exists())
			{
				files.mkdirs();
			}

			setInternalFileDir(mContext);
			setExternalFileDir(mContext);

			String home = FolderMe.getHomeDir(mContext);
			File mHome = new File(home);
			if (!mHome.exists())
			{
				mHome.mkdirs();
			}

			String user = FolderMe.getUserDir(mContext);
			File mUser = new File(user);
			if (!mUser.exists())
			{
				mUser.mkdirs();
			}
			return this;
		}

		public Builder setFolder(String folder)
		{
			this.isPath = !TextUtils.isEmpty(folder);
			this.mPath = folder;
			File mFolderMe = new File(FolderMe.EXTERNAL_DIR + "/" + mPath);
			if (!mFolderMe.exists())
			{
				mFolderMe.mkdirs();
			}
			
			return this;
		}

		public Builder setFolderApk(boolean isFolder)
		{
			File mFolder = new File(FolderMe.FOLDER_APK);
			if (!mFolder.exists())
			{
				mFolder.mkdirs();
			}
			return this;
		}

		public Builder setFolderPicture(boolean isFolder)
		{
			File mFolder = new File(FolderMe.FOLDER_IMG);
			if (!mFolder.exists())
			{
				mFolder.mkdirs();
			}
			return this;
		}

		public Builder setFolderScriptMe(boolean isFolder)
		{
			File mFolder = new File(FolderMe.FOLDER_SCRIPTME);
			if (!mFolder.exists())
			{
				mFolder.mkdirs();
			}
			return this;
		}

		public Builder setFolderAudio(boolean isFolder)
		{
			File mFolder = new File(FolderMe.FOLDER_AUDIO);
			if (!mFolder.exists())
			{
				mFolder.mkdirs();
			}
			return this;
		}

		public Builder setFolderVideo(boolean isFolder)
		{
			File mFolder = new File(FolderMe.FOLDER_VIDEO);
			if (!mFolder.exists())
			{
				mFolder.mkdirs();
			}
			return this;
		}

		public Builder setFolderEbook(boolean isFolder)
		{
			File mFolder = new File(FolderMe.FOLDER_EBOOK);
			if (!mFolder.exists())
			{
				mFolder.mkdirs();
			}
			return this;
		}

		public Builder setFolderZip(boolean isFolder)
		{
			File mFolder = new File(FolderMe.FOLDER_ZIP);
			if (!mFolder.exists())
			{
				mFolder.mkdirs();
			}
			return this;
		}

		private void setInternalFileDir(Context c)
		{
			getInternalFileDir(c);
		}

		public Builder setInternalCacheDir(Context c)
		{
			getInternalCacheDir(c);
			return this;
		}

		public Builder setExternalFileDir(Context c)
		{
			c.getExternalFilesDir(null).getAbsolutePath();
			return this;
		}

		public Builder setExternalFileDirs(Context c, String folder)
		{
			c.getExternalFilesDir(folder).getAbsolutePath();
			return this;
		}

		public FolderMe build()
		{
			this.isPath = !TextUtils.isEmpty(mPath);
			return new FolderMe(this);
		}
	}
}

