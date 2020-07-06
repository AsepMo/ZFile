package com.store.data.engine.app.dashboard;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.store.data.R;
import com.store.data.engine.app.about.Element;
import com.store.data.engine.app.about.AboutPage;
import android.graphics.Color;

public class DashboardFragment extends Fragment 
{

    private static final String EXTRA_TEXT = "text";

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		return inflater.inflate(R.layout.fragment_data_dashboard, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onViewCreated(view, savedInstanceState);
		simulateDayNight(0);
        Element adsElementFolder = new Element();
		final String folders = "Folder";
        adsElementFolder.setTitle(folders);
		adsElementFolder.setIconDrawable(R.drawable.ic_folder);
		//adsElementFolder.setIconTint(Color.TRANSPARENT);
		//adsElementFolder.setIconNightTint(Color.TRANSPARENT);
		adsElementFolder.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Toast.makeText(getActivity(), folders, Toast.LENGTH_SHORT).show();
				}
			});
		Element adsElementFTP = new Element();
		final String ftp = "FTP";
        adsElementFTP.setTitle(ftp);
		adsElementFTP.setIconDrawable(R.drawable.ic_home_server);
		//adsElementFTP.setIconTint(android.R.color.transparent);
		//adsElementFTP.setIconNightTint(android.R.color.transparent);
		adsElementFTP.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Toast.makeText(getActivity(), ftp, Toast.LENGTH_SHORT).show();
				}
			});
		
		Element adsElementWebClient = new Element();
		final String webClient = "Web Client";
        adsElementWebClient.setTitle(webClient);
		adsElementWebClient.setIconDrawable(R.drawable.ic_web_client);
		//adsElementWebClient.setIconTint(android.R.color.transparent);
		//adsElementWebClient.setIconNightTint(android.R.color.transparent);
		adsElementWebClient.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Toast.makeText(getActivity(), webClient, Toast.LENGTH_SHORT).show();
				}
			});	
		
		Element adsElementWebServer = new Element();
		final String webServer = "Web Server";
        adsElementWebServer.setTitle(webServer);
		adsElementWebServer.setIconDrawable(R.drawable.ic_web_server);
		//adsElementWebServer.setIconTint(android.R.color.transparent);
		//adsElementWebServer.setIconNightTint(android.R.color.transparent);
		adsElementWebServer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Toast.makeText(getActivity(), webServer, Toast.LENGTH_SHORT).show();
				}
			});		
			
        View dashboardPage = new DashboardPage(getActivity())
			.isRTL(false)
			.setImage(R.drawable.dummy_image)
			.addItem(new Element().setTitle("Dashboard :"))
			.addItem(adsElementFolder)
			.addItem(adsElementFTP)
			.addItem(adsElementWebClient)
			.addItem(adsElementWebServer)
			.addGroup("Next Menu :")
			.addDebug(getActivity(), "Debug")
			.addItem(getCopyRightsElement())
			.create();
			
			LinearLayout mLayout = (LinearLayout)view.findViewById(R.id.gridMenu);
			mLayout.addView(dashboardPage);
		
	}

	Element getCopyRightsElement()
	{
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Toast.makeText(getActivity(), copyrights, Toast.LENGTH_SHORT).show();
				}
			});
        return copyRightsElement;
    }

    void simulateDayNight(int currentSetting)
	{
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
			& Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO)
		{
            AppCompatDelegate.setDefaultNightMode(
				AppCompatDelegate.MODE_NIGHT_NO);
        }
		else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES)
		{
            AppCompatDelegate.setDefaultNightMode(
				AppCompatDelegate.MODE_NIGHT_YES);
        }
		else if (currentSetting == FOLLOW_SYSTEM)
		{
            AppCompatDelegate.setDefaultNightMode(
				AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
	
}
