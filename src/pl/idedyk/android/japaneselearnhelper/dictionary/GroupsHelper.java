package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupsHelper {
	
	private static Map<String, Integer> groupsPower = null;
	
	public static List<String> sortGroups(List<String> groups) {
		
		if (groups == null) {
			return null;
		}
		
		Collections.sort(groups, new Comparator<String>() {

			public int compare(String group1, String group2) {
								
				Map<String, Integer> groupsPower = getGroupsPower();
				
				Integer group1Power = groupsPower.get(group1);
				
				if (group1Power == null) {
					throw new RuntimeException("Can't find group power for: " + group1);
				}
				
				Integer group2Power = groupsPower.get(group2);

				if (group2Power == null) {
					throw new RuntimeException("Can't find group power for: " + group2);
				}
				
				return group1Power.compareTo(group2Power);
			}
		});
		
		return groups;
	}
	
	private static Map<String, Integer> getGroupsPower() {
		
		if (groupsPower != null) {
			return groupsPower;
		}
		
		int power = 0;
		
		groupsPower = new HashMap<String, Integer>();
		
		groupsPower.put("Genki 1-1", ++power);
		
		groupsPower.put("Genki 1-2", ++power);
		groupsPower.put("Genki 1-3", ++power);
		groupsPower.put("Genki 1-4", ++power);
		groupsPower.put("Genki 1-5", ++power);
		groupsPower.put("Genki 1-6", ++power);
		groupsPower.put("Genki 1-7", ++power);
		groupsPower.put("Genki 1-8", ++power);		
		groupsPower.put("Genki 1-9", ++power);
		groupsPower.put("Genki 1-10", ++power);
		groupsPower.put("Genki 1-11", ++power);
		groupsPower.put("Genki 1-12", ++power);
		
		groupsPower.put("Genki dodatkowe 1-8", ++power);
		groupsPower.put("Genki dodatkowe 1-9", ++power);
		groupsPower.put("Genki dodatkowe 1-10", ++power);
		groupsPower.put("Genki dodatkowe 1-11", ++power);
		groupsPower.put("Genki dodatkowe 1-12", ++power);
		
		groupsPower.put("Genki kanji 1-3", ++power);
		groupsPower.put("Genki kanji 1-4", ++power);
		groupsPower.put("Genki kanji 1-5", ++power);
		groupsPower.put("Genki kanji 1-6", ++power);
		groupsPower.put("Genki kanji 1-7", ++power);
		groupsPower.put("Genki kanji 1-8", ++power);
		groupsPower.put("Genki kanji 1-9", ++power);
		groupsPower.put("Genki kanji 1-10", ++power);
		groupsPower.put("Genki kanji 1-11", ++power);
		groupsPower.put("Genki kanji 1-12", ++power);

		groupsPower.put("Genki 2-13", ++power);		
		groupsPower.put("Genki 2-14", ++power);
		groupsPower.put("Genki 2-15", ++power);
		groupsPower.put("Genki 2-16", ++power);
		
		groupsPower.put("Genki dodatkowe 2-13", ++power);
		groupsPower.put("Genki dodatkowe 2-14", ++power);
		groupsPower.put("Genki dodatkowe 2-15", ++power);
		groupsPower.put("Genki dodatkowe 2-16", ++power);
				
		groupsPower.put("Genki kanji 2-13", ++power);
		groupsPower.put("Genki kanji 2-14", ++power);
		groupsPower.put("Genki kanji 2-15", ++power);
		groupsPower.put("Genki kanji 2-16", ++power);
		
		groupsPower.put("Inne", ++power);
		
		return groupsPower;
	}
}
