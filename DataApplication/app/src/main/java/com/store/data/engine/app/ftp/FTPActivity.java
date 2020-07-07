package com.store.data.engine.app.ftp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;

import com.store.data.R;

public class FTPActivity extends AppCompatActivity
{

	public static void start(Context c)
	{
		Intent mApplication = new Intent(c, FTPActivity.class);
		c.startActivity(mApplication);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ftp_server);
	}
}

