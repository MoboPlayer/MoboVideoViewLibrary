package com.clov4r.moboplayer.android.nil.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreverenceLib {
	// public static int

	static Context context;
	private static SharedPreferences sharepDefault = null;
	private static Editor edit = null;

	public static void initSp(Context con) {
		context = con;
		if (sharepDefault == null)
			sharepDefault = PreferenceManager
					.getDefaultSharedPreferences(context);

	}

	public static void saveSetting(Context con, String key, Object value) {
		if (sharepDefault == null) {
			if (context == null)
				return;
			else
				initSp(con);
		}
		if (edit == null)
			edit = sharepDefault.edit();
		if (value == null || value.getClass() == String.class)
			edit.putString(key, (String) value);
		else if (value.getClass() == Integer.class)
			edit.putInt(key, (Integer) value);
		else if (value.getClass() == Long.class)
			edit.putLong(key, (Long) value);
		else if (value.getClass() == Boolean.class)
			edit.putBoolean(key, (Boolean) value);
		else if (value.getClass() == Float.class)
			edit.putFloat(key, (Float) value);
		edit.commit();
	}

	public static Object getByKey(String key, Object defValue) {
		if (sharepDefault == null) {
			if (context == null)
				return "";
			else
				sharepDefault = PreferenceManager
						.getDefaultSharedPreferences(context);
		}
		if ("" == defValue || defValue == null
				|| defValue.getClass() == String.class)
			return sharepDefault.getString(key, (String) defValue);
		else if (defValue.getClass() == Integer.class)
			return sharepDefault.getInt(key, (Integer) defValue);
		else if (defValue.getClass() == Long.class)
			return sharepDefault.getLong(key, (Long) defValue);
		else if (defValue.getClass() == Boolean.class)
			return sharepDefault.getBoolean(key, (Boolean) defValue);
		else if (defValue.getClass() == Float.class)
			return sharepDefault.getFloat(key, (Float) defValue);
		return defValue;
	}

}
