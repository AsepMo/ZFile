package com.store.data.engine.app.updater;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.DialogInterface;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;
import java.io.File;

import com.store.data.R;

public class UpdaterActivity extends AppCompatActivity 
{
	public static final String TAG = UpdaterActivity.class.getSimpleName();
	private String mDirPath;
    public static final String UPGRADE = "upgrade";
    private UpdaterService loadFileService;
    //private TextView messageTextView;

    private boolean isRegisteredService;
    //private Handler handler = new Handler();
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_updater);
		Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mDirPath = createDownLoadUpgradeDir(getApplicationContext());
		new Updater(this, "https://asepmo-story.000webhostapp.com/updater/updater.json", new UpdateListener() {
				@Override
				public void onJsonDataReceived(final UpdateModel updateModel, JSONObject jsonObject) {
					if (Updater.getCurrentVersionCode(UpdaterActivity.this) < updateModel.getVersionCode()) {
						new AlertDialog.Builder(UpdaterActivity.this)
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
    }
	
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
}
