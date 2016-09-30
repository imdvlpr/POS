package com.ankit.pointofsolution.storage;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceActivity;

/**
 * SettingsStore - To retrieve and save from Settings
 */
public class SettingsStore {

	private Activity activity = null;
	private Context context = null;
	private Preferences prefs;

	public SettingsStore(Activity thisActivity) {
		activity = thisActivity;
		prefs = new Preferences(activity);
	}


	public SettingsStore(Context context) {
		this.context = context;
		prefs = new Preferences(this.context);
	}

	public SettingsStore(PreferenceActivity thisActivity) {
		activity = thisActivity;
		prefs = new Preferences(activity);
	}

	public void saveLogin(String sUsername, String sPassword) {
		prefs.saveUsername(sUsername);
		prefs.savePassword(sPassword);		 
	}

	/*public String getMODeviceInfoPath() {
		return prefs.getValue(Preferences.KEY_CURRENT_MO_DEV_INFO_PATH, "");
	}*/


}
