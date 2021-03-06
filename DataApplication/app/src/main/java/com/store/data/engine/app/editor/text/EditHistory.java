package com.store.data.engine.app.editor.text;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EditHistory {
    public static final EditHistory INSTANCE=new EditHistory();
    private static final String PREF_OPEN_EDITORS="open";
    private AtomicReference<SharedPreferences> prefsRef=
    new AtomicReference<SharedPreferences>();

    public boolean initialize(Context ctxt) {
        if (prefsRef.get()==null) {
            new LoadThread(ctxt).start();

            return(false);
        }

        return(true);
    }

    public List<Uri> getOpenEditors() {
        String editors=prefsRef.get().getString(PREF_OPEN_EDITORS, null);
        ArrayList<Uri> result=new ArrayList<Uri>();

        if (editors!=null) {
            StringReader sr=new StringReader(editors);
            JsonReader json=new JsonReader(sr);

            try {
                json.beginArray();

                while (json.hasNext()) {
                    result.add(Uri.parse(json.nextString()));
                }

                json.endArray();
                json.close();
            }
            catch (IOException e) {
                Log.e(getClass().getSimpleName(),
                      "Exception reading JSON", e);
            }
        }

        return(result);
    }

    public boolean addOpenEditor(Uri document) {
        List<Uri> current=getOpenEditors();

        if (!current.contains(document)) {
            if (current.size()>9) {
                current.remove(0);
            }

            current.add(document);

            return(saveHistory(current));
        }

        return(true);
    }

    public boolean removeOpenEditor(Uri document) {
        List<Uri> current=getOpenEditors();

        current.remove(document);

        return(saveHistory(current));
    }

    private boolean saveHistory(List<Uri> history) {
        StringWriter sw=new StringWriter();
        JsonWriter json=new JsonWriter(sw);

        try {
            json.beginArray();

            for (Uri uri : history) {
                json.value(uri.toString());
            }

            json.endArray();
            json.close();

            prefsRef
                .get()
                .edit()
                .putString(PREF_OPEN_EDITORS, sw.toString())
                .apply();
        }
        catch (IOException e) {
            Log.e(getClass().getSimpleName(),
                  "Exception saving JSON", e);

            return(false);
        }

        return(true);
    }

    public static class InitializedEvent {

    }

    private class LoadThread extends Thread {
        private final Context ctxt;

        LoadThread(Context ctxt) {
            this.ctxt=ctxt.getApplicationContext();
        }

        @Override
        public void run() {
            prefsRef.set(PreferenceManager.getDefaultSharedPreferences(ctxt));
            EventBus.getDefault().post(new InitializedEvent());
        }
    }
}

