package pl.idedyk.android.japaneselearnhelper.gramma.dto;

public enum GrammaFormConjugateGroupType {
	
	ADJECTIVE_I_INFORMAL("Forma nieformalna (prosta)"),
	ADJECTIVE_I_FORMAL("Forma formalna"),
	
	ADJECTIVE_I_KEIGO("Keigo"),

	ADJECTIVE_NA_INFORMAL("Forma nieformalna (prosta)"),
	ADJECTIVE_NA_FORMAL("Forma formalna"),
	
	ADJECTIVE_NA_KEIGO("Keigo"),

	NOUN_INFORMAL("Forma nieformalna (prosta)"),
	NOUN_FORMAL("Forma formalna"),
	
	NOUN_KEIGO("Keigo"),
	
	VERB_FORMAL("Forma formalna (długa)"),
	VERB_INFORMAL("Forma nieformalna (prosta)"),
	
	ADJECTIVE_I_TE("Forma te"),
	ADJECTIVE_NA_TE("Forma te"),
	NOUN_TE("Forma te"),
	VERB_TE("Forma te"),
	
	VERB_STEM("Temat czasownika (ang: stem)"),
	
	VERB_MASHOU("Forma mashou"),
	
	VERB_POTENTIAL_INFORMAL("Forma potencjalna (prosta)"),
	
	VERB_POTENTIAL_FORMAL("Forma potencjalna (formalna)"),
	
	VERB_POTENTIAL_TE("Forma potencjalna, forma te"),

	VERB_PASSIVE_INFORMAL("Forma bierna (prosta)"),
	
	VERB_PASSIVE_FORMAL("Forma bierna (formalna)"),
	
	VERB_PASSIVE_TE("Forma bierna, forma te"),

	VERB_CAUSATIVE_INFORMAL("Forma sprawcza (prosta)"),
	
	VERB_CAUSATIVE_FORMAL("Forma sprawcza (formalna)"),
	
	VERB_CAUSATIVE_TE("Forma sprawcza, forma te"),
	
	VERB_VOLITIONAL("Forma wolicjonalna"),
	
	VERB_BA("Forma ba"),
	
	VERB_KEIGO("Keigo");
	
	private String name;
	
	GrammaFormConjugateGroupType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
