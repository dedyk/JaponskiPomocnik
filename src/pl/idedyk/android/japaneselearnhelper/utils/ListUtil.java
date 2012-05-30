package pl.idedyk.android.japaneselearnhelper.utils;

import java.util.List;

public class ListUtil {

	public static String getListAsString(List<?> list, String delimeter) {
		StringBuffer sb = new StringBuffer();
		
		for (int idx = 0; idx < list.size(); ++idx) {
			sb.append(list.get(idx));
			
			if (idx != list.size() - 1) {
				sb.append(delimeter);
			}
		}
		
		return sb.toString();
	}
}
