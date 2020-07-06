package com.store.data.engine.app.debug;

import com.store.data.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LogFragment extends Fragment
{
	private static final int REQUEST_WRITE_STORAGE = 112;
    private static TextView output;
    private static ScrollView scroll;
	private View rootView;
    /**
     * Show message in TextView, used from Logger
     *
     * @param log message
     */
    public static void showLog(final String log) {
        // show log in TextView
        output.post(new Runnable() {
				@Override
				public void run() {
					output.setText(log);
					// scroll TextView to bottom
					scroll.post(new Runnable() {
							@Override
							public void run() {
								scroll.fullScroll(View.FOCUS_DOWN);
								scroll.clearFocus();
							}
						});
				}
			});
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		rootView = inflater.inflate(R.layout.content_data_logging, container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onViewCreated(view, savedInstanceState);
		output = (TextView)view.findViewById(R.id.outputView);
        scroll = (ScrollView)view.findViewById(R.id.scrollView);

        // enable context clickable
        output.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		TextView outputView = (TextView)rootView.findViewById(R.id.outputView);
        // restore font size
        outputView.setTextSize(TypedValue.COMPLEX_UNIT_SP, PrefStore.getFontSize(getActivity()));
        // restore logs
        String log = Logger.get();
        if (log.length() == 0) {
            // show info if empty
            new ExecScript(getActivity(), "info").start();
        } else {
            showLog(log);
        }
	}
	
}
