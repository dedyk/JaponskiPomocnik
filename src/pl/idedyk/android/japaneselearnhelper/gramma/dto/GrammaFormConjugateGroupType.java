package pl.idedyk.android.japaneselearnhelper.gramma.dto;

public enum GrammaFormConjugateGroupType {
	
	ADJECTIVE_I_INFORMAL("Forma nieformalna (prosta)"),
	ADJECTIVE_I_FORMAL("Forma formalna"),

	ADJECTIVE_NA_INFORMAL("Forma nieformalna (prosta)"),
	ADJECTIVE_NA_FORMAL("Forma formalna"),

	NOUN_INFORMAL("Forma nieformalna (prosta)"),
	NOUN_FORMAL("Forma formalna"),
	
	VERB_FORMAL("Forma formalna (długa)"),
	VERB_INFORMAL("Forma nieformalna (prosta)"),
	
	ADJECTIVE_I_TE("Forma te"),
	ADJECTIVE_NA_TE("Forma te"),
	NOUN_TE("Forma te"),
	VERB_TE("Forma te"),
	
	VERB_STEM("Temat czasownika (ang: stem)"),
	
	VERB_MASHOU("Forma mashou");
	
	private String name;
	
	GrammaFormConjugateGroupType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
