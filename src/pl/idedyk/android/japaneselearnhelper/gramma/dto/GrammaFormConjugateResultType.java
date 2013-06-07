package pl.idedyk.android.japaneselearnhelper.gramma.dto;

public enum GrammaFormConjugateResultType {
	
	ADJECTIVE_I_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),	
	ADJECTIVE_I_INFORMAL_PAST("Twierdzenie, czas przeszły", true),
	ADJECTIVE_I_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	ADJECTIVE_I_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	ADJECTIVE_I_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	ADJECTIVE_I_FORMAL_PAST("Twierdzenie, czas przeszły", true),
	ADJECTIVE_I_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	ADJECTIVE_I_VIRTUAL("Wirtualny typ", false),
	
	ADJECTIVE_I_KEIGO_LOW("Forma modestywna (skromna)", true),
	
	ADJECTIVE_NA_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	ADJECTIVE_NA_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),	
	ADJECTIVE_NA_INFORMAL_PAST("Twierdzenie, czas przeszły", true),
	ADJECTIVE_NA_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	ADJECTIVE_NA_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	ADJECTIVE_NA_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	ADJECTIVE_NA_FORMAL_PAST("Twierdzenie, czas przeszły", true),
	ADJECTIVE_NA_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	ADJECTIVE_NA_KEIGO_LOW("Forma modestywna (skromna)", true),

	NOUN_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	NOUN_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),	
	NOUN_INFORMAL_PAST("Twierdzenie, czas przeszły", true),
	NOUN_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	NOUN_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	NOUN_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	NOUN_FORMAL_PAST("Twierdzenie, czas przeszły", true),
	NOUN_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	NOUN_KEIGO_LOW("Forma modestywna (skromna)", true),

	VERB_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_FORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),

	VERB_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_INFORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	VERB_TE("Forma te", false),
	ADJECTIVE_I_TE("Forma te", false),
	ADJECTIVE_NA_TE("Forma te", false),
	NOUN_TE("Forma te", false),
	
	VERB_STEM("Temat czasownika (ang: stem)", false),
	
	VERB_MASHOU("Forma mashou", false),
	
	VERB_POTENTIAL_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_POTENTIAL_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_POTENTIAL_INFORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_POTENTIAL_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),

	VERB_POTENTIAL_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_POTENTIAL_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_POTENTIAL_FORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_POTENTIAL_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	VERB_POTENTIAL_TE("Forma te", false),

	VERB_PASSIVE_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_PASSIVE_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_PASSIVE_INFORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_PASSIVE_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),

	VERB_PASSIVE_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_PASSIVE_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_PASSIVE_FORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_PASSIVE_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	VERB_PASSIVE_TE("Forma te", false),

	VERB_CAUSATIVE_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_CAUSATIVE_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_CAUSATIVE_INFORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_CAUSATIVE_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),

	VERB_CAUSATIVE_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_CAUSATIVE_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_CAUSATIVE_FORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_CAUSATIVE_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	VERB_CAUSATIVE_TE("Forma te", false),

	VERB_CAUSATIVE_PASSIVE_INFORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_CAUSATIVE_PASSIVE_INFORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_CAUSATIVE_PASSIVE_INFORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_CAUSATIVE_PASSIVE_INFORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),

	VERB_CAUSATIVE_PASSIVE_FORMAL_PRESENT("Twierdzenie, czas teraźniejszy", true),
	VERB_CAUSATIVE_PASSIVE_FORMAL_PRESENT_NEGATIVE("Przeczenie, czas teraźniejszy", true),
	VERB_CAUSATIVE_PASSIVE_FORMAL_PAST("Twierdzenie, czas przeszły", true),
	VERB_CAUSATIVE_PASSIVE_FORMAL_PAST_NEGATIVE("Przeczenie, czas przeszły", true),
	
	VERB_CAUSATIVE_PASSIVE_TE("Forma te", false),
	
	VERB_VOLITIONAL("Forma wolicjonalna", false),
	
	VERB_BA_AFFIRMATIVE("Twierdzenie", true),
	VERB_BA_NEGATIVE("Przeczenie", true),
	
	VERB_KEIGO_HIGH("Forma honoryfikatywna (wywyższająca)", true),
	VERB_KEIGO_LOW("Forma modestywna (skromna)", true);
	
	private String name;
	
	private boolean show;
	
	GrammaFormConjugateResultType(String name, boolean show) {
		this.name = name;
		this.show = show;
	}

	public String getName() {
		return name;
	}

	public boolean isShow() {
		return show;
	}
}
