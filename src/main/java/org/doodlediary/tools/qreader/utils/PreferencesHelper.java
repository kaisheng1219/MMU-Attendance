package org.doodlediary.tools.qreader.utils;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesHelper {
    private Preferences prefs;

    public PreferencesHelper() {
        prefs = Preferences.userNodeForPackage(this.getClass());
    }

    public void setPref(String key, String value) {
        prefs.put(key, value);
    }

    public String getPref(String key) {
        return prefs.get(key, "");
    }

    public static void main(String[] args) throws BackingStoreException {
        new PreferencesHelper();
    }
}
