package com.store.data.engine.app.about;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.store.data.R;

import java.util.Calendar;

public class AboutFragment extends Fragment {

    private static final String EXTRA_TEXT = "text";

    public static AboutFragment createFor(String text) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final String text = getArguments().getString(EXTRA_TEXT);
		//Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		LinearLayout mLayout = (LinearLayout)view.findViewById(R.id.about);
		Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(getActivity())
			.isRTL(false)
			.setImage(R.drawable.dummy_image)
			.addItem(new Element().setTitle("Version 6.2"))
			.addItem(adsElement)
			.addGroup("Connect with us")
			.addEmail("elmehdi.sakout@gmail.com")
			.addWebsite("http://medyo.github.io/")
			.addFacebook("the.medy")
			.addTwitter("medyo80")
			.addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
			.addPlayStore("com.ideashower.readitlater.pro")
			.addInstagram("medyo80")
			.addGitHub("medyo")
			.addItem(getCopyRightsElement())
			.create();
			mLayout.addView(aboutPage);
	}
	
	Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), copyrights, Toast.LENGTH_SHORT).show();
				}
			});
        return copyRightsElement;
    }
	
}
