package pl.idedyk.android.japaneselearnhelper.gramma.dto;

public enum GrammaFormConjugateGroupType {
	
	ADJECTIVE_I_INFORMAL("Forma nieformalna (prosta)"),
	
	ADJECTIVE_I_FORMAL("Forma formalna");
	
	private String name;
	
	GrammaFormConjugateGroupType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
