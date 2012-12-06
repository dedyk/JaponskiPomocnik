package pl.idedyk.android.japaneselearnhelper.example.dto;

public enum ExampleGroupType {
	
	ADJECTIVE_I_II_GRADATION("Stopniowanie (II poziom, bardziej)"),
	
	ADJECTIVE_I_III_GRADATION("Stopniowanie (III poziom, najbardziej)"),
	
	ADJECTIVE_I_NARU("Stawać się"),
	
	ADJECTIVE_I_N_DESU("Forma wyjaśniająca"),
	
	ADJECTIVE_I_SUGIRU("Zbyt wiele"),
	
	ADJECTIVE_I_DESHOU("Prawdopodobnie, ok. 60%"),
	
	ADJECTIVE_I_SOU_DESU("Wygląda na"),
	
	ADJECTIVE_I_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	ADJECTIVE_I_TO_II_TO_OTHERS("Mieć nadzieję, że ... (mówienie o innych)"),
	
	ADJECTIVE_I_TO_II_TO_ME("Mieć nadzieję, że ... (mówienie o sobie)"),
	
	ADJECTIVE_I_TOKI("Kiedy ..., to ..."),
	
	ADJECTIVE_NA_II_GRADATION("Stopniowanie (II poziom, bardziej)"),
	
	ADJECTIVE_NA_III_GRADATION("Stopniowanie (III poziom, najbardziej)"),
	
	ADJECTIVE_NA_NARU("Stawać się"),
	
	ADJECTIVE_NA_NA_DESU("Forma wyjaśniająca"),
	
	ADJECTIVE_NA_SUGIRU("Zbyt wiele"),
	
	ADJECTIVE_NA_DESHOU("Prawdopodobnie, ok. 60%"),
	
	ADJECTIVE_NA_SOU_DESU("Wygląda na"),
	
	ADJECTIVE_NA_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	ADJECTIVE_NA_TO_II_TO_OTHERS("Mieć nadzieję, że ... (mówienie o innych)"),
	
	ADJECTIVE_NA_TO_II_TO_ME("Mieć nadzieję, że ... (mówienie o sobie)"),
	
	ADJECTIVE_NA_TOKI("Kiedy ..., to ..."),
	
	NOUN_NARU("Stawać się"),
	
	NOUN_LIKE("Lubić"),
	NOUN_DISLIKE("Nie lubić"),
	
	NOUN_NA_DESU("Forma wyjaśniająca"),
	
	NOUN_DESHOU("Prawdopodobnie, ok. 60%"),
	
	NOUN_HOSHII("Chcieć (I i II osoba)"),
	
	NOUN_HOSHIGATE_IRU("Chcieć (III osoba)"),
	
	NOUN_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	NOUN_AGERU("Dać (od siebie, ktoś komuś)"),
	
	NOUN_KURERU("Dać (mnie)"),
	
	NOUN_MORAU("Otrzymać"),
	
	NOUN_TOKI("Kiedy ..., to ..."),
	
	VERB_TAI("Chcieć (I i II osoba)"),
	
	VERB_TAGATTE_IRU("Chcieć (III osoba)"),
	
	VERB_SUGIRU("Zbyt wiele"),
	
	VERB_LIKE("Lubić"),
	VERB_DISLIKE("Nie lubić"),
	
	VERB_STEM_NI_IKU("Idę, aby ..."),
	
	VERB_TE_IRU("Trwanie czynności/stanu"),
	
	VERB_TE_KUDASAI("Prośba I"),
	
	VERB_TE_MO_II("Pozwolenie"),
	
	VERB_TE_HA_IKEMASEN("Zakaz 1"),
	
	VERB_NAI_DE_KUDASAI("Zakaz 2"),
	
	VERB_MADA_TE_IMASEN("Jeszcze nie"),
	
	VERB_TSUMORI_DESU("Zamiar"),
	
	VERB_KOTO_GA_ARU("Mieć doświadczenie"),
	
	VERB_N_DESU("Forma wyjaśniająca"),
	
	VERB_ADVICE("Rada"),
	
	VERB_NAKUCHA_IKEMASEN("Musieć"),
	
	VERB_DESHOU("Prawdopodobnie, ok. 60%"),
	
	VERB_TE_MIRU("Próbować"),
	
	VERB_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	VERB_TARA("Jeśli (tryb warunkowy)"),
	
	VERB_TARA_DOU_DESU_KA("Rada lub zalecenie", "Uwaga: Nie używane w zaproszeniach"),
	
	VERB_OU_TO_OMOU("Wola (decyzja podjęta w momencie mówienia)"),
	
	VERB_OU_TO_OMOTTE_IRU("Wola (decyzja podjęta wcześniej)"),
	
	VERB_TE_OKU("Przygotować się"),
	
	VERB_TE_AGERU("Dać czynność (od siebie, ktoś komuś)"),
	
	VERB_TE_KURERU("Dać czynność (mnie)"),
	
	VERB_TE_MORAU("Otrzymać czynność"),
	
	VERB_REQUEST("Prośby II (wybrane, od najbardziej grzecznej)"),
	
	VERB_TO_II_TO_OTHERS("Mieć nadzieję, że ... (mówienie o innych)"),
	
	VERB_TO_II_TO_ME("Mieć nadzieję, że ... (mówienie o sobie)"),
	
	VERB_TOKI("Kiedy ..., to ..."),
	
	VERB_TE_ARIGATOU("Dziękuję, że zrobiłeś"),
	
	VERB_KUTE_ARIGATOU("Dziękuję, że nie zrobiłeś"),
	
	VERB_TE_SUMIMASEN("Przepraszam, że zrobiłem"),
	
	VERB_KUTE_SUMIMASEN("Przepraszam, że nie zrobiłem");
	
	private String name;
	
	private String info;
	
	ExampleGroupType(String name) {
		this.name = name;
	}

	ExampleGroupType(String name, String info) {
		this.name = name;
		this.info = info;
	}
	
	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}
}
