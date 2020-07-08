package com.store.data.engine.app.ftp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;


public class FTPEditActivity extends SingleFragmentActivity {

    private static final String EXTRA_FTP = "com.store.data.engine.app.ftp";
	private static final String RETURN_STATE = "ftpState";

    public static Intent newIntent(Context packageContext, FTP ftp) {
        Intent intent = new Intent(packageContext, FTPEditActivity.class);
        intent.putExtra(EXTRA_FTP, ftp);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        FTP ftp = (FTP) getIntent().getSerializableExtra(EXTRA_FTP);
        return FTPEditFragment.newInstance(ftp);
    }

}
