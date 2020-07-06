package com.store.data.engine.app.dashboard;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.store.data.R;
import com.store.data.engine.app.about.Element;
import com.store.data.engine.app.about.AboutPage;
import android.view.Gravity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatDelegate;
import java.util.Calendar;
import android.widget.LinearLayout;

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
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(getActivity())
			.isRTL(false)
			.setImage(R.drawable.dummy_image)
			.addItem(new Element().setTitle("Version 1.0"))
			.addItem(adsElement)
			.addGroup("Connect with us")
			.addEmail("asepmo.story@gmail.com")
			.addWebsite("http://cbox-dev.github.io/CBox-Dev")
			.addFacebook("ZFile")
			.addTwitter("ZFile")
			.addYoutube("UC2H7DyQrnr2RA4RSMF0B4ZA")
			.addPlayStore("com.store.data.pro")
			.addInstagram("AsepMo")
			.addGitHub("AsepMo")
			.addItem(getCopyRightsElement())
			.create();
			
			LinearLayout mLayout = (LinearLayout)view.findViewById(R.id.gridMenu);
			mLayout.addView(aboutPage);
		
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
