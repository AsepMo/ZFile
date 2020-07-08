package com.store.data.engine.app.ftp;

import android.content.Intent;
import android.content.Context;

import android.support.v4.app.Fragment;

public class FTPListActivity extends SingleFragmentActivity implements FTPListFragment.MyListener {

	public static void start(Context c)
	{
		Intent mApplication = new Intent(c, FTPListActivity.class);
		c.startActivity(mApplication);
	}
	
    @Override
    protected Fragment createFragment() {
        return new FTPListFragment();
    }

	@Override
	public void finishActivity()
	{
		// TODO: Implement this method
		finish();
	}
}
