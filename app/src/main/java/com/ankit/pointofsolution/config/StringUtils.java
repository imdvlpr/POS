package com.ankit.pointofsolution.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringUtils {
	public static final String TEST_TYPE_MO = "Mobile Originated Test";
	public static final String TEST_TYPE_MT = "Mobile Terminated Test";
	public static final String TEST_TYPE_FTP = "FTP Test";
	public static final String TEST_TYPE_UDP = "UDP Test";
	public static final String TEST_TYPE_PING = "Ping Test";
	public static final String TEST_TYPE_BROWSER = "Browser Download Test";
	public static final String TEST_TYPE_VOIP = "VoIP Test";
	public static final String TEST_TYPE_EXTERNAL = "External Test";

	public static final String TEST_TYPE_CODE_MO = "MO";
	public static final String TEST_TYPE_CODE_MT = "MT";
	public static final String TEST_TYPE_CODE_FTP = "FTP";
	public static final String TEST_TYPE_CODE_UDP = "UDP";
	public static final String TEST_TYPE_CODE_PING = "PING";
	public static final String TEST_TYPE_CODE_BROWSER = "BROWSER";
	public static final String TEST_TYPE_CODE_VOIP = "VOIP";
	public static final String TEST_TYPE_CODE_EXTERNAL = "EXTERNAL";



	public static String CODE = "CODE";
	public static String DESC = "DESC";
	public static String ERROR_CODE = "ERROR";
	public static String RESP_LOGIN = "RESP_LOGIN";
	public static String SUCCESS = "SUCCESS";

	public static final String TEST_MODE_MANUAL = "MODE_MANUAL";
	public static final String TEST_MODE_CONFIG = "MODE_CONFIG";
	
	public static final String TEST_CYCLE_APPEND_FILE = "-";
	
	private static LinkedHashMap<String, String> HASH_MAP_TEST_TYPES;
	static {
		HASH_MAP_TEST_TYPES = new LinkedHashMap<String, String>();
		HASH_MAP_TEST_TYPES.put(TEST_TYPE_CODE_MO, TEST_TYPE_MO);
		HASH_MAP_TEST_TYPES.put(TEST_TYPE_CODE_MT, TEST_TYPE_MT);
		HASH_MAP_TEST_TYPES.put(TEST_TYPE_CODE_FTP, TEST_TYPE_FTP);
		HASH_MAP_TEST_TYPES.put(TEST_TYPE_CODE_UDP, TEST_TYPE_UDP);
		HASH_MAP_TEST_TYPES.put(TEST_TYPE_CODE_PING, TEST_TYPE_PING);
		HASH_MAP_TEST_TYPES.put(TEST_TYPE_CODE_BROWSER, TEST_TYPE_BROWSER);
		HASH_MAP_TEST_TYPES.put(TEST_TYPE_CODE_VOIP, TEST_TYPE_VOIP);
		HASH_MAP_TEST_TYPES.put(TEST_TYPE_CODE_EXTERNAL, TEST_TYPE_EXTERNAL);
	}
	
	public static LinkedHashMap<String, String> MARKET_PLACES_MAP = null;

	public static String[] getTestTypes() {
		ArrayList<String> lst_test_type = new ArrayList<String>();
		for (Map.Entry<String, String> entry : HASH_MAP_TEST_TYPES.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			lst_test_type.add(value);
		}
		return lst_test_type.toArray(new String[0]);
	}

	public static String[] getTestTypeCodes() {
		ArrayList<String> lst_test_type_codes = new ArrayList<String>();
		for (Map.Entry<String, String> entry : HASH_MAP_TEST_TYPES.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			lst_test_type_codes.add(key);
		}
		return lst_test_type_codes.toArray(new String[0]);
	}

	public static String getTestTypeFromCode(String sTypeCode) {
		String sTypeText = "";
		if (HASH_MAP_TEST_TYPES.containsKey(sTypeCode))
			sTypeText = HASH_MAP_TEST_TYPES.get(sTypeCode);
		return sTypeText;
	}
	
	public static List<String> getStringArray(Set<String> set) {
		List<String> arrayList = new ArrayList<String>();
		Iterator<String> setIterator = set.iterator();
		while (setIterator.hasNext()) {
			String item = setIterator.next();
			arrayList.add(item);
		}
		return arrayList;
	}
}
