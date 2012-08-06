package pl.idedyk.android.japaneselearnhelper.example.dto;

public enum ExampleGroupType {
	
	ADJECTIVE_I_NARU("Stawać się"),
	
	ADJECTIVE_I_N_DESU("Forma wyjaśniająca"),
	
	ADJECTIVE_I_SUGIRU("\"Zbyt wiele\""),
	
	ADJECTIVE_I_DESHOU("Prawdopodobnie, ok. 60%"),
	
	ADJECTIVE_I_SOU_DESU("\"Wygląda na\""),
	
	ADJECTIVE_I_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	ADJECTIVE_NA_NARU("Stawać się"),
	
	ADJECTIVE_NA_NA_DESU("Forma wyjaśniająca"),
	
	ADJECTIVE_NA_SUGIRU("\"Zbyt wiele\""),
	
	ADJECTIVE_NA_DESHOU("Prawdopodobnie, ok. 60%"),
	
	ADJECTIVE_NA_SOU_DESU("\"Wygląda na\""),
	
	ADJECTIVE_NA_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	NOUN_NARU("Stawać się"),
	
	NOUN_LIKE("Lubić"),
	NOUN_DISLIKE("Nie lubić"),
	
	NOUN_NA_DESU("Forma wyjaśniająca"),
	
	NOUN_DESHOU("Prawdopodobnie, ok. 60%"),
	
	NOUN_HOSHII("Chcieć"),
	
	NOUN_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	NOUN_AGERU("Dać (od siebie)"),
	
	NOUN_KURERU("Dać (od kogoś)"),
	
	NOUN_MORAU("Otrzymać"),
	
	VERB_TAI("Chcieć"),
	
	VERB_SUGIRU("\"Zbyt wiele\""),
	
	VERB_LIKE("Lubić"),
	VERB_DISLIKE("Nie lubić"),
	
	VERB_STEM_NI_IKU("\"Idę, aby ...\""),
	
	VERB_TE_IRU("Trwanie czynności/stanu"),
	
	VERB_TE_KUDASAI("Prośba"),
	
	VERB_TE_MO_II("Pozwolenie"),
	
	VERB_TE_HA_IKEMASEN("Zakaz 1"),
	
	VERB_NAI_DE_KUDASAI("Zakaz 2"),
	
	VERB_TSUMORI_DESU("Zamiar"),
	
	VERB_KOTO_GA_ARU("Mieć doświadczenie"),
	
	VERB_N_DESU("Forma wyjaśniająca"),
	
	VERB_ADVICE("Rada"),
	
	VERB_NAKUCHA_IKEMASEN("Musieć"),
	
	VERB_DESHOU("Prawdopodobnie, ok. 60%"),
	
	VERB_TE_MIRU("Próbować"),
	
	VERB_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%");
	
	private String name;
	
	ExampleGroupType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
