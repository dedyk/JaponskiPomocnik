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
	
	VERB_TE("Forma te");
	
	private String name;
	
	GrammaFormConjugateGroupType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
