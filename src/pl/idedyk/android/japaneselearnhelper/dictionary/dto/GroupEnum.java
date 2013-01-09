package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum GroupEnum {

	GENKI_1_1("Genki 1-1", 1), 
	GENKI_1_2("Genki 1-2", 2), 
	GENKI_1_3("Genki 1-3", 3), 
	GENKI_1_4("Genki 1-4", 4), 
	GENKI_1_5("Genki 1-5", 5), 
	GENKI_1_6("Genki 1-6", 6), 
	GENKI_1_7("Genki 1-7", 7), 
	GENKI_1_8("Genki 1-8", 8),
	GENKI_1_9("Genki 1-9", 9),
	GENKI_1_10("Genki 1-10", 10),
	GENKI_1_11("Genki 1-11", 11),
	GENKI_1_12("Genki 1-12", 12),

	GENKI_ADDITIONAL_1_8("Genki dodatkowe 1-8", 13),
	GENKI_ADDITIONAL_1_9("Genki dodatkowe 1-9", 14),
	GENKI_ADDITIONAL_1_10("Genki dodatkowe 1-10", 15),
	GENKI_ADDITIONAL_1_11("Genki dodatkowe 1-11", 16),
	GENKI_ADDITIONAL_1_12("Genki dodatkowe 1-12", 17),

	GENKI_KANJI_1_3("Genki kanji 1-3", 18),
	GENKI_KANJI_1_4("Genki kanji 1-4", 19),
	GENKI_KANJI_1_5("Genki kanji 1-5", 20),
	GENKI_KANJI_1_6("Genki kanji 1-6", 21),
	GENKI_KANJI_1_7("Genki kanji 1-7", 22),
	GENKI_KANJI_1_8("Genki kanji 1-8", 23),
	GENKI_KANJI_1_9("Genki kanji 1-9", 24),
	GENKI_KANJI_1_10("Genki kanji 1-10", 25),
	GENKI_KANJI_1_11("Genki kanji 1-11", 26),
	GENKI_KANJI_1_12("Genki kanji 1-12", 27),

	GENKI_2_13("Genki 2-13", 28),
	GENKI_2_14("Genki 2-14", 29),
	GENKI_2_15("Genki 2-15", 30),
	GENKI_2_16("Genki 2-16", 31),
	GENKI_2_17("Genki 2-17", 32),
	GENKI_2_18("Genki 2-18", 33),

	GENKI_ADDITIONAL_2_13("Genki dodatkowe 2-13", 34),
	GENKI_ADDITIONAL_2_14("Genki dodatkowe 2-14", 35),
	GENKI_ADDITIONAL_2_15("Genki dodatkowe 2-15", 36),
	GENKI_ADDITIONAL_2_16("Genki dodatkowe 2-16", 37),
	GENKI_ADDITIONAL_2_17("Genki dodatkowe 2-17", 38),

	GENKI_KANJI_2_13("Genki kanji 2-13", 39),
	GENKI_KANJI_2_14("Genki kanji 2-14", 40),
	GENKI_KANJI_2_15("Genki kanji 2-15", 41),
	GENKI_KANJI_2_16("Genki kanji 2-16", 42),
	GENKI_KANJI_2_17("Genki kanji 2-17", 43),

	COUNTERS("Klasyfikatory", 44),
	OTHER("Inne", 45),
	
	JLPT_1("JLPT 1", 46),
	JLPT_2("JLPT 2", 47),
	JLPT_3("JLPT 3", 48),
	JLPT_4("JLPT 4", 49),
	JLPT_5("JLPT 5", 50);

	private String value;

	private int power;

	GroupEnum(String value, int power) {
		this.value = value;

		this.power = power;
	}

	public String getValue() {
		return value;
	}

	public int getPower() {
		return power;
	}

	public static GroupEnum getGroupEnum(String value) {

		if (value == null || value.equals("") == true) {
			return null;
		}
		
		GroupEnum[] values = GroupEnum.values();

		for (GroupEnum groupEnum : values) {

			if (groupEnum.getValue().equals(value) == true) {
				return groupEnum;
			}
		}

		throw new RuntimeException("Can't find group enum for: " + value);
	}

	public static List<String> convertToValues(List<GroupEnum> groups) {

		List<String> values = new ArrayList<String>();

		for (GroupEnum currentGroup : groups) {
			values.add(currentGroup.getValue());
		}

		return values;		
	}

	public static List<GroupEnum> convertToListGroupEnum(List<String> values) {

		List<GroupEnum> groupEnumList = new ArrayList<GroupEnum>();

		for (String currentValue : values) {
			
			GroupEnum groupEnum = getGroupEnum(currentValue);
			
			if (groupEnum != null) {
				groupEnumList.add(groupEnum);
			}
		}

		return groupEnumList;
	}

	public static List<GroupEnum> sortGroups(List<GroupEnum> groups) {

		if (groups == null) {
			return null;
		}

		Collections.sort(groups, new Comparator<GroupEnum>() {

			public int compare(GroupEnum group1, GroupEnum group2) {

				Integer group1Power = group1.getPower();
				Integer group2Power = group2.getPower();

				return group1Power.compareTo(group2Power);
			}
		});

		return groups;
	}
	
	public static void sortGroups(GroupEnum[] groups) {
		
		Arrays.sort(groups, new Comparator<GroupEnum>() {

			public int compare(GroupEnum group1, GroupEnum group2) {

				Integer group1Power = group1.getPower();
				Integer group2Power = group2.getPower();

				return group1Power.compareTo(group2Power);
			}
		});
	}
}
