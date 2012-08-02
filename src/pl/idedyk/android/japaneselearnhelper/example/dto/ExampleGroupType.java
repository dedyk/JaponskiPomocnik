package pl.idedyk.android.japaneselearnhelper.example.dto;

public enum ExampleGroupType {
	
	NOUN_LIKE("Lubić"),
	NOUN_DISLIKE("Nie lubić");
	
	private String name;
	
	ExampleGroupType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
