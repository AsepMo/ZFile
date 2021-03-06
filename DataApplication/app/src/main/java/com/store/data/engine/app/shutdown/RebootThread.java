package com.store.data.engine.app.shutdown;

import java.io.IOException;

import android.util.Log;

public class RebootThread extends Thread {

	private static final String TAG = RebootThread.class.getSimpleName();

	private final OnErrorListener errorListener;

	public RebootThread(OnErrorListener errorListener) {
		if (errorListener == null) {
			throw new IllegalArgumentException("errorListener cannot be null.");
		}
		this.errorListener = errorListener;
	}

	@Override
	public void run() {
		Log.d(TAG, "Executing 'reboot' command...");
		Runtime runtime = Runtime.getRuntime();
		Process proc = null;

		try {
			try {

				proc = runtime.exec(new String[] { "su", "-c", "reboot" });

			} catch (IOException e) {
				logFailure(e);
				if (e.getMessage().contains("Error running exec(). Command: [su")) {
					errorListener.onNotRoot();
				} else {
					errorListener.onError(e);
				}
			} catch (Exception e) {
				logFailure(e);
				errorListener.onError(e);
			}
			try {
				if (proc != null) {
					Thread aliveCheck = new RebootCheckThread(proc, RebootThread.this);
					aliveCheck.start();
					Log.i(TAG, "Waiting for Reboot...");
					int exitCode = proc.waitFor();
					Log.i(TAG, "Process exited with code " + exitCode + ".");
					aliveCheck.interrupt();
				}
			} catch (InterruptedException e) {
				logFailure(e);
				Log.e(TAG, "Interrupted while waiting for process to finish.");
			}
			if (proc != null) {
				String stdErr = Utils.dumpProcessOutput(proc);
				if (proc.exitValue() != 0 && stdErr.length() > 0) {
					logFailure();
					if (stdErr.contains("not allowed to su")) {
						errorListener.onNotRoot();
					} else {
						errorListener.onError(stdErr);
					}
				}
			}
		} finally {
			// Clean up
			if (proc != null) {
				proc.destroy();
			}
		}
	}

	private void logFailure() {
		logFailure(null);
	}

	private void logFailure(Exception e) {
		Log.e(TAG, "Failed to execute 'reboot' command.", e);
	}
}
