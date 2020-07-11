package com.store.data.engine.app.editor.text;


import android.app.FragmentManager;
import android.net.Uri;
import com.commonsware.cwac.pager.ArrayPagerAdapter;
import com.commonsware.cwac.pager.PageDescriptor;
import com.commonsware.cwac.pager.SimplePageDescriptor;
import java.util.ArrayList;

public class EditorAdapter extends ArrayPagerAdapter<TextEditorFragment> {
    public EditorAdapter(FragmentManager fm) {
        super(fm, new ArrayList<PageDescriptor>());
    }

    @Override
    protected TextEditorFragment createFragment(PageDescriptor desc) {
        Uri document=Uri.parse(desc.getFragmentTag());

        return(TextEditorFragment.newInstance(document));
    }

    void addDocument(Uri document) {
        add(new SimplePageDescriptor(document.toString(),
                                     document.getLastPathSegment()));
    }

    void updateTitle(Uri document, String title) {
        int position=getPositionForDocument(document);

        if (position>=0) {
            SimplePageDescriptor desc=
                (SimplePageDescriptor)getPageDescriptor(position);

            desc.setTitle(title);
        }
    }

    void remove(Uri document) {
        int position=getPositionForDocument(document);

        if (position>=0) {
            remove(position);
        }
    }

    int getPositionForDocument(Uri document) {
        return(getPositionForTag(document.toString()));
    }
}

