package com.store.data.engine.app.webserver;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.support.customtabs.CustomTabsIntent;
import android.app.PendingIntent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.store.data.R;
import com.store.data.engine.app.webclient.CustomTabActivityHelper;
import com.store.data.engine.app.webclient.WebviewFallback;
import com.store.data.engine.app.asepmo.AsepMoActivity;
import android.graphics.BitmapFactory;

public class WebServerActivity extends AppCompatActivity implements OnClickListener,
android.content.DialogInterface.OnClickListener, OnCancelListener
{

	private static final String TAG = "ServerActivity";
	private ToggleButton toggleButton;
	private TextView textView;
	private TextView textUrl;
	private ConnectivityManager connMgr;
	private String ipAddress;
	private ProgressDialog progress;
	private ImageView img;
	private ImageView imgQrcode;
	private IntentFilter filter;
	private Bitmap mQRbitmap;
	public static void start(Context c)
	{
		Intent mApplication = new Intent(c, WebServerActivity.class);
		c.startActivity(mApplication);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_server);

		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		toggleButton.setOnClickListener(this);

		textView = (TextView) findViewById(R.id.textView1);
		textUrl = (TextView) findViewById(R.id.textView2);
		img = (ImageView) findViewById(R.id.imageView1);
		imgQrcode = (ImageView) findViewById(R.id.imageView_QRcode);
		connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

		Settings.init(this);

		if (!WebFileInstaller.isWebfileInstalled(this))
		{
			showInstallProgress();
			installWebFiles();
		}
		filter = new IntentFilter();
		filter.addAction(Intents.ACTION_SERVER_STATE_CHANGE);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	}

	@Override
	protected void onResume()
	{

		this.registerReceiver(receiver, filter);
		refreshUIState();

		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Web Server")
			.setIcon(R.mipmap.ic_launcher)
			.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					if (HttpService.isRunning())
					{
						ipAddress = Utils.getLocalIpAddress();

						String url = String.format("http://%s:%d", ipAddress, Settings.getPort());

						CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
						String shareLabel = getString(R.string.app_name);
						Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
						PendingIntent pendingIntent = createPendingIntent();
						intentBuilder.setActionButton(icon, shareLabel, pendingIntent);
						intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.abc_ic_ab_back_material));
						intentBuilder.setStartAnimations(WebServerActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
						intentBuilder.setExitAnimations(WebServerActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

						CustomTabActivityHelper.openCustomTab(WebServerActivity.this, intentBuilder.build(), Uri.parse(url), new WebviewFallback());			
						WebServerActivity.this.finish();
					}
					return false;
				}
			}).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		getMenuInflater().inflate(R.menu.server, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId())
		{
			case R.id.action_settings:
				Intent intent=new Intent();
				intent.setClass(this, SettingsActivity.class);
				this.startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void refreshUIState()
	{
		if (!Utils.isSdCardMounted())
		{
			Toast.makeText(this, R.string.storage_off, Toast.LENGTH_LONG)
				.show();
		}

		if (!Utils.isNetworkActive(connMgr))
		{

			textView.setText(R.string.wifi_off);
			textView.setTextColor(Color.RED);
			img.setImageResource(R.drawable.signal_off);
			toggleButton.setEnabled(false);
			textUrl.setVisibility(View.GONE);
			imgQrcode.setVisibility(View.GONE);
			return;

		}
		toggleButton.setEnabled(true);
		if (HttpService.isRunning())
		{
			ipAddress = Utils.getLocalIpAddress();

			textView.setText(R.string.server_on);
			textView.setTextColor(Color.argb(255, 0x22, 0x8b, 0x22));
			img.setImageResource(R.drawable.signal_on);
			toggleButton.setChecked(true);
			textUrl.setVisibility(View.VISIBLE);
			String url = String.format("http://%s:%d", ipAddress, Settings.getPort());
			textUrl.setText(url);

			/*CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
			 String shareLabel = getString(R.string.app_name);
			 Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
			 PendingIntent pendingIntent = createPendingIntent();
			 intentBuilder.setActionButton(icon, shareLabel, pendingIntent);
			 intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.abc_ic_ab_back_material));
			 intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
			 intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,android.R.anim.slide_out_right);

			 CustomTabActivityHelper.openCustomTab(ServerActivity.this, intentBuilder.build(), Uri.parse(url), new WebviewFallback());
			 */
			createQRcode(url);

		}
		else
		{
			textView.setText(R.string.server_off);
			textView.setTextColor(Color.RED);
			img.setImageResource(R.drawable.signal_off);
			toggleButton.setChecked(false);
			textUrl.setVisibility(View.GONE);
			imgQrcode.setVisibility(View.GONE);
		}

	}

	private PendingIntent createPendingIntent()
	{
        Intent actionIntent = new Intent(this.getApplicationContext(), AsepMoActivity.class);
		actionIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return PendingIntent.getActivity(getApplicationContext(), 0, actionIntent, 0);
    }

	@Override
	protected void onPause()
	{
		this.unregisterReceiver(receiver);
		super.onPause();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent)
		{

			if (Intents.ACTION_SERVER_STATE_CHANGE.equals(intent.getAction()))
			{
				refreshUIState();

			}

			else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
																	.getAction()))
			{

				refreshUIState();

			}
		}

	};

	private void showInstallProgress()
	{
		progress = new ProgressDialog(this);
		progress.setTitle(R.string.web_file_extract);
		progress.setCanceledOnTouchOutside(false);
		progress.setCancelable(false);
		progress.setOnCancelListener(this);
		progress.show();
	}

	@Override
	public void onCancel(DialogInterface dialog)
	{

		// Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					if (progress != null)
					{
						progress.dismiss();
					}
					break;
				case 1:
					WebServerActivity.this.finish();
					break;
				case 2:
					if (HttpService.isRunning())
					{
						imgQrcode.setVisibility(View.VISIBLE);
						imgQrcode.setImageBitmap(mQRbitmap);
					}
					break;
				default:
					break;
			}
		}

	};

	private void installWebFiles()
	{
		new Thread() {

			@Override
			public void run()
			{
				WebFileInstaller.installWebfile(WebServerActivity.this, handler);
			}

		}.start();
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{

		this.finish();

	}



	@Override
	public void onClick(View v)
	{

		Intent intent = new Intent();
		intent.setClass(this, HttpService.class);
		if (!HttpService.isRunning())
		{
			startService(intent);
		}
		else
		{
			stopService(intent);

		}
	}


	private void createQRcode(final String url)
	{
		new Thread(){



			@Override
			public void run()
			{

				mQRbitmap = Utils.Create2DCode(url);

				if (mQRbitmap != null)
				{
					Message msg=new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
				super.run();
			}



		}.start();
	}

}
