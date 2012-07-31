package pl.idedyk.android.japaneselearnhelper.gramma.dto;

public enum GrammaFormConjugateResultType {
	
	ADJECTIVE_I_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy"),	
	ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy"),	
	ADJECTIVE_I_INFORMAL_PAST("Twierdzenie, czas przeszły"),	
	ADJECTIVE_I_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły"),
	
	ADJECTIVE_I_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy"),	
	ADJECTIVE_I_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy"),
	ADJECTIVE_I_FORMAL_PAST("Twierdzenie, czas przeszły"),	
	ADJECTIVE_I_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły"),
	
	ADJECTIVE_NA_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy"),	
	ADJECTIVE_NA_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy"),	
	ADJECTIVE_NA_INFORMAL_PAST("Twierdzenie, czas przeszły"),	
	ADJECTIVE_NA_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły"),
	
	ADJECTIVE_NA_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy"),	
	ADJECTIVE_NA_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy"),
	ADJECTIVE_NA_FORMAL_PAST("Twierdzenie, czas przeszły"),	
	ADJECTIVE_NA_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły"),	

	NOUN_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy"),	
	NOUN_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy"),	
	NOUN_INFORMAL_PAST("Twierdzenie, czas przeszły"),	
	NOUN_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły"),
	
	NOUN_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy"),	
	NOUN_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy"),
	NOUN_FORMAL_PAST("Twierdzenie, czas przeszły"),	
	NOUN_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły"),

	VERB_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy"),	
	VERB_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy"),
	VERB_FORMAL_PAST("Twierdzenie, czas przeszły"),	
	VERB_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły");
	
	private String name;
	
	GrammaFormConjugateResultType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
