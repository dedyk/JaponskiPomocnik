package pl.idedyk.android.japaneselearnhelper.example.dto;

public enum ExampleGroupType {
	
	NOUN_LIKE("Lubić"),
	NOUN_DISLIKE("Nie lubić"),
	
	VERB_LIKE("Lubić"),
	VERB_DISLIKE("Nie lubić"),
	
	VERB_TE_IRU("Trwanie czynności/stanu"),
	
	VERB_TE_KUDASAI("Prośba"),
	
	VERB_TE_MO_II("Pozwolenie"),
	
	VERB_TE_HA_IKEMASEN("Zakaz");
	
	private String name;
	
	ExampleGroupType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
