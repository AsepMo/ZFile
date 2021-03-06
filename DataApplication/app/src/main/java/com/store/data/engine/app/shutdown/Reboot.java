package com.store.data.engine.app.shutdown;

import com.store.data.R;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

public class Reboot extends AppCompatActivity implements OnErrorListener, DialogInterface.OnKeyListener
{

	public static final String TAG = Shutdown.class.getSimpleName();

	public static void start(Context c){
		Intent shutdown = new Intent(c, Reboot.class);
		c.startActivity(shutdown);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.really_reboot).setOnKeyListener(this).setCancelable(true)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					Reboot.this.shutdown();
				}
			}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					Reboot.this.forceExit();
				}
			});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void shutdown()
	{
		new RebootThread(this).start();
	}

	@Override
	public void onNotRoot()
	{
		runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					showNotRootedDialog();
				}
			});
	}

	@Override
	public void onError(final String msg)
	{
		runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					showErrorDialog(msg);
				}
			});
	}

	@Override
	public void onError(final Exception exc)
	{
		final String msg = exc.getClass().getSimpleName() + ": " + exc.getMessage();
		onError(msg);
	}

	private void showErrorDialog(String msg)
	{
		AlertDialog.Builder builder = buildErrorDialog(msg);
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showNotRootedDialog()
	{
		final Uri uri = Uri.parse(getString(R.string.rooting_url));
		AlertDialog.Builder builder = buildErrorDialog(getString(R.string.not_rooted));
		builder.setNegativeButton(R.string.what, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					startActivity(new Intent(Intent.ACTION_VIEW, uri));
					Reboot.this.forceExit();
				}
			});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private AlertDialog.Builder buildErrorDialog(String msg)
	{
		return new AlertDialog.Builder(this).setMessage(msg).setOnKeyListener(this).setCancelable(true)
			.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					Reboot.this.forceExit();
				}
			});
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			dialog.dismiss();
			forceExit();
			return true;
		}
		return false;
	}

	private void forceExit()
	{
		finish();
		Utils.killMyProcess();
	}

}
