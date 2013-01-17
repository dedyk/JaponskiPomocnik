package pl.idedyk.android.japaneselearnhelper.example.dto;

public enum ExampleGroupType {
	
	ADJECTIVE_I_II_GRADATION("Stopniowanie (II poziom, bardziej)"),
	
	ADJECTIVE_I_III_GRADATION("Stopniowanie (III poziom, najbardziej)"),
	
	ADJECTIVE_I_NARU("Stawać się"),
	
	ADJECTIVE_I_N_DESU("Forma wyjaśniająca"),
	
	ADJECTIVE_I_SUGIRU("Zbyt wiele"),
	
	ADJECTIVE_I_DESHOU("Prawdopodobnie, ok. 60%"),
	
	ADJECTIVE_I_SOU_DESU_LOOKS_LIKE("Wygląda na", "Bardziej używane do odbieranych przez zmysły informacji, tylko do przymiotników"),
	
	ADJECTIVE_I_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	ADJECTIVE_I_TO_II_TO_OTHERS("Mieć nadzieję, że ... (mówienie o innych)"),
	
	ADJECTIVE_I_TO_II_TO_ME("Mieć nadzieję, że ... (mówienie o sobie)"),
	
	ADJECTIVE_I_TOKI("Kiedy ..., to ..."),
	
	ADJECTIVE_I_SOU_DESU_HEAR("Słyszałem, że ..."),
	
	ADJECTIVE_I_TTE("Forma tte", "Zastępuje そうです oraz と言っていました"),
	
	ADJECTIVE_I_TARA("Jeśli (tryb warunkowy), to ..."),
	
	ADJECTIVE_I_NAKUTE_MO_II_DESU("Nie trzeba"),
	
	ADJECTIVE_I_MITAI_DESU("Wygląda, jak", "Bardziej używane z analizy sytuacji, do przymiotników częściej forma そうです"),
	
	ADJECTIVE_I_TO("Kiedy A staje się, wtedy również B staje się", "Mówi o rzeczach oczywistych, z której druga wynika z pierwszej"),
	
	ADJECTIVE_NA_II_GRADATION("Stopniowanie (II poziom, bardziej)"),
	
	ADJECTIVE_NA_III_GRADATION("Stopniowanie (III poziom, najbardziej)"),
	
	ADJECTIVE_NA_NARU("Stawać się"),
	
	ADJECTIVE_NA_NA_DESU("Forma wyjaśniająca"),
	
	ADJECTIVE_NA_SUGIRU("Zbyt wiele"),
	
	ADJECTIVE_NA_DESHOU("Prawdopodobnie, ok. 60%"),
	
	ADJECTIVE_NA_SOU_DESU_LOOKS_LIKE("Wygląda na", "Bardziej używane do odbieranych przez zmysły informacji, tylko do przymiotników"),
	
	ADJECTIVE_NA_KAMOSHI_REMASEN("Prawdopodobnie, ok. 30%"),
	
	ADJECTIVE_NA_TO_II_TO_OTHERS("Mieć nadzieję, że ... (mówienie o innych)"),
	
	ADJECTIVE_NA_TO_II_TO_ME("Mieć nadzieję, że ... (mówienie o sobie)"),
	
	ADJECTIVE_NA_TOKI("Kiedy ..., to ..."),
	
	ADJECTIVE_NA_SOU_DESU_HEAR("Słyszałem, że ..."),
	
	ADJECTIVE_NA_TTE("Forma tte", "Zastępuje そうです oraz と言っていました"),
	
	ADJECTIVE_NA_TARA("Jeśli (tryb warunkowy), to ..."),
	
	ADJECTIVE_NA_NAKUTE_MO_II_DESU("Nie trzeba"),
	
	ADJECTIVE_NA_MITAI_DESU("Wygląda, jak", "Bardziej używane z analizy sytuacji, do przymiotników częściej forma そうです"),
	
	ADJECTIVE_NA_TO("Kiedy A staje się, wtedy również B staje się", "Mówi o rzeczach oczywistych, z której druga wynika z pierwszej"),
	
	NOUN_NI_NARU("Stawać się"),
	
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
	
	NOUN_TO_II_TO_OTHERS("Mieć nadzieję, że ... (mówienie o innych)"),
	
	NOUN_TO_II_TO_ME("Mieć nadzieję, że ... (mówienie o sobie)"),
	
	NOUN_TOKI("Kiedy ..., to ..."),
	
	NOUN_SOU_DESU_HEAR("Słyszałem, że ..."),
	
	NOUN_TTE("Forma tte", "Zastępuje そうです oraz と言っていました"),
	
	NOUN_TARA("Jeśli (tryb warunkowy), to ..."),
	
	NOUN_MITAI_DESU("Wygląda, jak", "Bardziej używane z analizy sytuacji"),
	
	NOUN_TO("Kiedy A staje się, wtedy również B staje się", "Mówi o rzeczach oczywistych, z której druga wynika z pierwszej"),
		
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
	
	VERB_TARA("Jeśli (tryb warunkowy), to ..."),
	
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
	
	VERB_KUTE_SUMIMASEN("Przepraszam, że nie zrobiłem"),
	
	VERB_SOU_DESU_HEAR("Słyszałem, że ..."),
	
	VERB_TTE("Forma tte", "Zastępuje そうです oraz と言っていました"),
	
	VERB_NAKUTE_MO_II_DESU("Nie trzeba tego robić"),
	
	VERB_MITAI_DESU("Wygląda, jak", "Bardziej używane z analizy sytuacji"),
	
	VERB_MAE_NI("Przed czynnością, robię ..."),
	
	VERB_TE_KARA("Po czynności, robię ..."),
	
	VERB_TE_SHIMAU("Zrobić cos do końca / Niestety coś stało się"),
	
	VERB_TO("Kiedy A staje się, wtedy również B staje się", "Mówi o rzeczach oczywistych, z której druga wynika z pierwszej"),
	
	VERB_NABARA("W trakcie czynności, robię ...", "Musi być ten sam podmiot");
	
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
