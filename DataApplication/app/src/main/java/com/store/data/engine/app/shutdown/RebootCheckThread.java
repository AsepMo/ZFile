package com.store.data.engine.app.shutdown;

import android.util.Log;

public class RebootCheckThread extends Thread
{
	private final static String TAG = RebootCheckThread.class.getSimpleName();

	private final Process proc;
	private final RebootThread rebootThread;

	public RebootCheckThread(Process proc, RebootThread rebootThread)
	{
		this.proc = proc;
		this.rebootThread = rebootThread;
	}

	@Override
	public void run()
	{
		try
		{
			sleep(15000); // wait 15s, because Superuser also has 10s timeout
		}
		catch (InterruptedException e)
		{
			Log.i(TAG, "Interrupted.");
			return;
		}
		Log.w(TAG, "Still alive after 15 sec...");
		Utils.dumpProcessOutput(proc);
		proc.destroy();
		rebootThread.interrupt();
		Log.w(TAG, "Interrupted and destroyed.");

		Utils.killMyProcess();
	}
}
