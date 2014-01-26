package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum GroupEnum {
	
	SALUTATIONS("Zwroty grzecznościowe", 1),

	GENKI_1_1("Genki 1-1", 2), 
	GENKI_1_2("Genki 1-2", 3), 
	GENKI_1_3("Genki 1-3", 4), 
	GENKI_1_4("Genki 1-4", 5), 
	GENKI_1_5("Genki 1-5", 6), 
	GENKI_1_6("Genki 1-6", 7), 
	GENKI_1_7("Genki 1-7", 8), 
	GENKI_1_8("Genki 1-8", 9),
	GENKI_1_9("Genki 1-9", 10),
	GENKI_1_10("Genki 1-10", 11),
	GENKI_1_11("Genki 1-11", 12),
	GENKI_1_12("Genki 1-12", 13),

	GENKI_ADDITIONAL_1_8("Genki dodatkowe 1-8", 14),
	GENKI_ADDITIONAL_1_9("Genki dodatkowe 1-9", 15),
	GENKI_ADDITIONAL_1_10("Genki dodatkowe 1-10", 16),
	GENKI_ADDITIONAL_1_11("Genki dodatkowe 1-11", 17),
	GENKI_ADDITIONAL_1_12("Genki dodatkowe 1-12", 18),

	GENKI_KANJI_1_3("Genki kanji 1-3", 19),
	GENKI_KANJI_1_4("Genki kanji 1-4", 20),
	GENKI_KANJI_1_5("Genki kanji 1-5", 21),
	GENKI_KANJI_1_6("Genki kanji 1-6", 22),
	GENKI_KANJI_1_7("Genki kanji 1-7", 23),
	GENKI_KANJI_1_8("Genki kanji 1-8", 24),
	GENKI_KANJI_1_9("Genki kanji 1-9", 25),
	GENKI_KANJI_1_10("Genki kanji 1-10", 26),
	GENKI_KANJI_1_11("Genki kanji 1-11", 27),
	GENKI_KANJI_1_12("Genki kanji 1-12", 28),

	GENKI_2_13("Genki 2-13", 29),
	GENKI_2_14("Genki 2-14", 30),
	GENKI_2_15("Genki 2-15", 31),
	GENKI_2_16("Genki 2-16", 32),
	GENKI_2_17("Genki 2-17", 33),
	GENKI_2_18("Genki 2-18", 34),
	GENKI_2_19("Genki 2-19", 35),
	GENKI_2_20("Genki 2-20", 36),
	GENKI_2_21("Genki 2-21", 37),
	GENKI_2_22("Genki 2-22", 38),
	GENKI_2_23("Genki 2-23", 39),

	GENKI_ADDITIONAL_2_13("Genki dodatkowe 2-13", 40),
	GENKI_ADDITIONAL_2_14("Genki dodatkowe 2-14", 41),
	GENKI_ADDITIONAL_2_15("Genki dodatkowe 2-15", 42),
	GENKI_ADDITIONAL_2_16("Genki dodatkowe 2-16", 43),
	GENKI_ADDITIONAL_2_17("Genki dodatkowe 2-17", 44),
	GENKI_ADDITIONAL_2_18("Genki dodatkowe 2-18", 45),
	GENKI_ADDITIONAL_2_19("Genki dodatkowe 2-19", 46),
	GENKI_ADDITIONAL_2_20("Genki dodatkowe 2-20", 47),
	GENKI_ADDITIONAL_2_21("Genki dodatkowe 2-21", 48),
	GENKI_ADDITIONAL_2_22("Genki dodatkowe 2-22", 49),
	GENKI_ADDITIONAL_2_23("Genki dodatkowe 2-23", 50),

	GENKI_KANJI_2_13("Genki kanji 2-13", 51),
	GENKI_KANJI_2_14("Genki kanji 2-14", 52),
	GENKI_KANJI_2_15("Genki kanji 2-15", 53),
	GENKI_KANJI_2_16("Genki kanji 2-16", 54),
	GENKI_KANJI_2_17("Genki kanji 2-17", 55),
	GENKI_KANJI_2_18("Genki kanji 2-18", 56),
	GENKI_KANJI_2_19("Genki kanji 2-19", 57),
	GENKI_KANJI_2_20("Genki kanji 2-20", 58),
	GENKI_KANJI_2_21("Genki kanji 2-21", 59),
	GENKI_KANJI_2_22("Genki kanji 2-22", 60),
	GENKI_KANJI_2_23("Genki kanji 2-23", 61),

	COUNTERS("Klasyfikatory", 62),
	OTHER("Inne", 63),
	
	JLPT_1("JLPT 1", 64),
	JLPT_2("JLPT 2", 65),
	JLPT_3("JLPT 3", 66),
	JLPT_4("JLPT 4", 67),
	JLPT_5("JLPT 5", 68);

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

		if (groups == null) {
			return null;
		}
		
		List<String> values = new ArrayList<String>();

		for (GroupEnum currentGroup : groups) {
			values.add(currentGroup.getValue());
		}

		return values;		
	}

	public static List<GroupEnum> convertToListGroupEnum(List<String> values) {
		
		if (values == null) {
			return null;
		}

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
		
		if (groups == null) {
			return;
		}
		
		Arrays.sort(groups, new Comparator<GroupEnum>() {

			public int compare(GroupEnum group1, GroupEnum group2) {

				Integer group1Power = group1.getPower();
				Integer group2Power = group2.getPower();

				return group1Power.compareTo(group2Power);
			}
		});
	}
}
