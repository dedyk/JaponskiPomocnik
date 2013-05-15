package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.KeigoHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.KeigoHelper.KeigoEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.AttributeType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;
import pl.idedyk.android.japaneselearnhelper.grammaexample.GrammaExampleHelper;

public class VerbExampler {
	public static List<ExampleGroupTypeElements> makeAll(KeigoHelper keigoHelper, DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// like : suki		
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_LIKE, makeSukiExample(dictionaryEntry));
		
		// dislike : kirai		
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_DISLIKE, makeKiraiExample(dictionaryEntry));
		
		// stem + ni + iku
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_STEM_NI_IKU, makeStemNiIkuExample(dictionaryEntry, grammaFormCache));
		
		// te iru
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_IRU, makeTeIruExample(dictionaryEntry, grammaFormCache));
		
		// te kudasai
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_KUDASAI, makeTeKudasaiExample(dictionaryEntry, grammaFormCache));
		
		// te mo ii desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MO_II, makeTeMoIiExample(dictionaryEntry, grammaFormCache));
		
		// te wa ikemasen
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_HA_IKEMASEN, makeTeHaIkemasenExample(dictionaryEntry, grammaFormCache));
		
		// nai de kudasai
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_NAI_DE_KUDASAI, makeNaiDeKudasai(dictionaryEntry, grammaFormCache));
		
		// mada te imasen
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_MADA_TE_IMASEN, makeMadaTeImasen(dictionaryEntry, grammaFormCache));
		
		// tsumori desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TSUMORI_DESU, makeTsumoriDesu(dictionaryEntry, grammaFormCache));
		
		// koto ga aru
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_KOTO_GA_ARU, makeKotoGaAru(dictionaryEntry, grammaFormCache));
		
		// tai
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TAI, makeTaiExample(dictionaryEntry, grammaFormCache));

		// tagatte iru
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TAGATTE_IRU, makeTagatteIruExample(dictionaryEntry, grammaFormCache));
		
		// n desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_N_DESU, makeNDesuExample(dictionaryEntry));
		
		// sugiru
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_SUGIRU, makeSugiruExample(dictionaryEntry, grammaFormCache));
		
		// advice
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_ADVICE, makeAdviceExample(dictionaryEntry, grammaFormCache));
		
		// nakucha ikemasen
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_NAKUCHA_IKEMASEN, makeNakuchaIkemasenExample(dictionaryEntry, grammaFormCache));
		
		// deshou
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_DESHOU, makeDeshouExample(dictionaryEntry));
		
		// te miru
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MIRU, makeTeMiruExample(dictionaryEntry, grammaFormCache));

		// kamoshi remasen
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		// tara dou desu ka
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TARA_DOU_DESU_KA, makeTaraDouDesuKaExample(dictionaryEntry, grammaFormCache));

		// ou to omou
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_OU_TO_OMOU, makeOuToOmou(dictionaryEntry, grammaFormCache));

		// ou to omotte iru
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_OU_TO_OMOTTE_IRU, makeOuToOmotteIru(dictionaryEntry, grammaFormCache));
		
		// te oku
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_OKU, makeTeOkuExample(dictionaryEntry, grammaFormCache));
		
		// te ageru
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_AGERU, makeTeAgeruExample(dictionaryEntry, grammaFormCache));
		
		// te kureru
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_KURERU, makeTeKureruExample(dictionaryEntry, grammaFormCache));
		
		// te morau
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MORAU, makeTeMorauExample(dictionaryEntry, grammaFormCache));		
		
		// te kudasai
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_REQUEST, makeRequest(dictionaryEntry, grammaFormCache));
		
		// to ii to others
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TO_II_TO_OTHERS, makeToIIToOthers(dictionaryEntry, grammaFormCache));

		// to ii to me
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TO_II_TO_ME, makeToIIToMe(dictionaryEntry, grammaFormCache));
		
		// toki
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TOKI, makeToki(dictionaryEntry, grammaFormCache));
		
		// te arigatou
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_ARIGATOU, makeTeArigatou(dictionaryEntry, grammaFormCache));

		// te kute arigatou
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_KUTE_ARIGATOU, makeKuteArigatou(dictionaryEntry, grammaFormCache));
		
		// te sumimasen
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_SUMIMASEN, makeTeSumimasen(dictionaryEntry, grammaFormCache));

		// kute sumimasen
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_KUTE_SUMIMASEN, makeKuteSumimasen(dictionaryEntry, grammaFormCache));
		
		// sou desu (hear)
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_SOU_DESU_HEAR, makeSouDesuHear(dictionaryEntry, grammaFormCache));

		// sou desu (hear)
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TTE, makeTte(dictionaryEntry, grammaFormCache));
		
		// tara
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TARA, makeTaraExample(dictionaryEntry, grammaFormCache));
		
		// nakute mo ii desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_NAKUTE_MO_II_DESU, makeNakuteMoIiDesu(dictionaryEntry, grammaFormCache));
		
		// mitai desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_MITAI_DESU, makeMitaiDesuExample(dictionaryEntry, grammaFormCache));
		
		// mae ni
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_MAE_NI, makeMaeNi(dictionaryEntry, grammaFormCache));

		// te kara
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_KARA, makeTeKara(dictionaryEntry, grammaFormCache));
		
		// te shimau
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_SHIMAU, makeTeShimau(dictionaryEntry, grammaFormCache));
		
		// to
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TO, makeTo(dictionaryEntry, grammaFormCache));
		
		// nagara
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_NAGARA, makeNagara(dictionaryEntry, grammaFormCache));
		
		// ba yokatta
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_BA_YOKATTA, makeBaYokatta(dictionaryEntry, grammaFormCache));

		// te yokatta
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_YOKATTA, makeTeYokatta(dictionaryEntry, grammaFormCache));

		// nakute yokatta
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_NAKUTE_YOKATTA, makeNakuteYokatta(dictionaryEntry, grammaFormCache));
		
		// ba negative yokatta
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_BA_NEGATIVE_YOKATTA, makeBaNegativeYokatta(dictionaryEntry, grammaFormCache));
		
		// keigo kudasai
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_KEIGO_KUDASAI, makeKeigoKudasai(keigoHelper, dictionaryEntry, grammaFormCache));
		
		// hazu desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_HAZU_DESU, makeHazuDesu(dictionaryEntry, grammaFormCache));
		
		// nai de
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_NAI_DE, makeNaiDe(dictionaryEntry, grammaFormCache));
		
		// questions with larger sentences
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_QUESTIONS_WITH_LARGER_SENTENCES, makeQuestionsWithLargerSentences(dictionaryEntry, grammaFormCache));
		
		// yasui
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_YASUI, makeYasui(dictionaryEntry, grammaFormCache));
		
		// nikui
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_NIKUI, makeNikui(dictionaryEntry, grammaFormCache));
		
		// te aru
		if (dictionaryEntry.getAttributeList().contains(AttributeType.VERB_TRANSITIVITY) == true || dictionaryEntry.getDictionaryEntryType() == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_ARU, makeTeAruExample(dictionaryEntry, grammaFormCache));
		}
		
		// te iru aida ni
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_IRU_AIDA_NI, makeTeIruAidaNi(dictionaryEntry, grammaFormCache));
		
		// te hoshii
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_TE_HOSHII, makeTeHoshii(dictionaryEntry, grammaFormCache));
		
		// negative te hoshii
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_NEGATIVE_TE_HOSHII, makeNegativeTeHoshii(dictionaryEntry, grammaFormCache));
		
		// make let verb
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_MAKE_LET, makeMakeLet(dictionaryEntry, grammaFormCache));
		
		// let verb
		GrammaExampleHelper.addExample(result, ExampleGroupType.VERB_LET, makeLet(dictionaryEntry, grammaFormCache));
		
		return result;
	}

	private static ExampleResult makeStemNiIkuExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String[][] templates = {
				{ "[miejsce] [に/へ] %sに行く", "[miejsce] [に/へ] %sにいく", "[miejsce] [に/へ] %s ni iku" }, 
				{ "[miejsce] [に/へ] %sに来る", "[miejsce] [に/へ] %sにくる", "[miejsce] [に/へ] %s ni kuru" },
				{ "[miejsce] [に/へ] %sに帰る", "[miejsce] [に/へ] %sにかえる", "[miejsce] [に/へ] %s ni kaeru" }
		};
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
		
		ExampleResult result = GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templates[0][0], templates[0][1], templates[0][2], false);
		
		ExampleResult alternative1 = GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templates[1][0], templates[1][1], templates[1][2], false);
		
		ExampleResult alternative2 = GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templates[2][0], templates[2][1], templates[2][2], false);
		
		alternative1.setAlternative(alternative2);		
		result.setAlternative(alternative1);
		
		return result;
	}

	private static ExampleResult makeTeIruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sいる";
		final String templateKana = "%sいる";
		final String templateRomaji = "%s iru";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeKudasaiExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sください";
		final String templateKana = "%sください";
		final String templateRomaji = "%s kudasai";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);		
	}
	
	private static ExampleResult makeTeMoIiExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sもいいです";
		final String templateKana = "%sもいいです";
		final String templateRomaji = "%s mo ii desu";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult moIidesu = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
		
		moIidesu.setAlternative(
				GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji + "か", templateKana + "か", templateRomaji + " ka", true));
		
		return moIidesu;
	}
	
	private static ExampleResult makeNaiDeKudasai(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sでください";
		final String templateKana = "%sでください";
		final String templateRomaji = "%s de kudasai";
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeMadaTeImasen(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "まだ%sいません";
		final String templateKana = "まだ%sいません";
		final String templateRomaji = "mada %s imasen";

		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTsumoriDesu(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPresentForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji = "%sつもりです";
		final String templateKana = "%sつもりです";
		final String templateRomaji = "%s tsumori desu";
		
		ExampleResult tsumoriDesu = GrammaExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		ExampleResult naiTsumoriDesu = GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true);
		
		tsumoriDesu.setAlternative(naiTsumoriDesu);
		
		return tsumoriDesu;
	}
	
	private static ExampleResult makeKotoGaAru(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		final String templateKanji = "%sことがある";
		final String templateKana = "%sことがある";
		final String templateRomaji = "%s koto ga aru";
		
		ExampleResult kotoGaAru = GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
		
		ExampleResult kotoGaAruKa = GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji + "か", templateKana + "か", templateRomaji + " ka", true);
		
		kotoGaAru.setAlternative(kotoGaAruKa);
		
		return kotoGaAru;
	}
	
	private static ExampleResult makeTeHaIkemasenExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sはいけません";
		final String templateKana = "%sはいけません";
		final String templateRomaji = "%s wa ikemasen";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult moIidesu = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
				
		return moIidesu;
	}
	
	private static ExampleResult makeTaiExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
		
		String prefixKana = stemForm.getPrefixKana();
		String prefixRomaji = stemForm.getPrefixRomaji();
		
		if (prefixKana != null && prefixKana.equals("を") == true) {
			prefixKana = "が";
			prefixRomaji = "ga";
		}
		
		String kanji = stemForm.getKanji();
		
		if (kanji != null) { 
			kanji = kanji.replaceAll("を", "が");
		}
		
		List<String> kanaList = stemForm.getKanaList();
		
		if (kanaList != null) {
			
			List<String> newKanaList = new ArrayList<String>();
			
			for (String currentKana : kanaList) {
				newKanaList.add(currentKana.replaceAll("を", "が"));
			}
			
			kanaList = newKanaList;
		}
		
		List<String> romajiList = stemForm.getRomajiList();
		
		if (romajiList != null) {
			
			List<String> newRomajiList = new ArrayList<String>();
			
			for (String currentRomaji : romajiList) {
				newRomajiList.add(currentRomaji.replaceAll(" o ", " ga "));
			}
			
			romajiList = newRomajiList;
		}
		
		final String templateKanji = "%sたいです";
		final String templateKana = "%sたいです";
		final String templateRomaji = "%stai desu";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(
				prefixKana, kanji, kanaList, prefixRomaji, romajiList, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeTagatteIruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
		
		final String templateKanji = "%sたがっている";
		final String templateKana = "%sたがっている";
		final String templateRomaji = "%stagatte iru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeNDesuExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sんです";
		final String templateKana = "%sんです";
		final String templateRomaji = "%sn desu";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSugiruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
		
		final String templateKanji = "%sすぎる";
		final String templateKana = "%sすぎる";
		final String templateRomaji = "%s sugiru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeSukiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sのが好き";
		final String templateKana = "%sのがすき";
		final String templateRomaji = "%s no ga suki";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeKiraiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sのがきらい";
		final String templateKana = "%sのがきらい";
		final String templateRomaji = "%s no ga kirai";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeAdviceExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sほうがいいです";
		final String templateKana = "%sほうがいいです";
		final String templateRomaji = "%s hou ga ii desu";
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		ExampleResult houGaIiDesu = GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
		
		houGaIiDesu.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return houGaIiDesu;
	}
	
	private static ExampleResult makeNakuchaIkemasenExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String postfixKana = "くちゃいけません";
		String postfixRomaji = "kucha ikemasen";
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		String kanji = informalPresentNegativeForm.getKanji();
		List<String> kanaList = informalPresentNegativeForm.getKanaList();
		List<String> romajiList = informalPresentNegativeForm.getRomajiList();

		ExampleResult result = new ExampleResult();
				
		result.setPrefixKana(dictionaryEntry.getPrefixKana());
		result.setPrefixRomaji(dictionaryEntry.getPrefixRomaji());
		
		if (kanji != null) {		
			result.setKanji(removeLastChar(kanji) + postfixKana);
		}

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			kanaListResult.add(removeLastChar(currentKana) + postfixKana);
		}

		result.setKanaList(kanaListResult);

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			romajiListResult.add(removeLastChar(currentRomaji) + postfixRomaji);
		}

		result.setRomajiList(romajiListResult);
		
		return result;		
	}
	
	private static String removeLastChar(String text) {
		return text.substring(0, text.length() - 1);
	}
	
	private static ExampleResult makeDeshouExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sでしょう";
		final String templateKana = "%sでしょう";
		final String templateRomaji = "%s deshou";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeMiruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sみる";
		final String templateKana = "%sみる";
		final String templateRomaji = "%s miru";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult teMiru = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
				
		return teMiru;
	}
	
	private static ExampleResult makeKamoshiRemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sかもしれません";
		final String templateKana = "%sかもしれません";
		final String templateRomaji = "%s kamoshi remasen";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTaraExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);

		final String templateKanji1 = "%sら、...";
		final String templateKana1 = "%sら、...";
		final String templateRomaji1 = "%sra, ...";
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "%sかったら、...";
		final String templateKana2 = "%sかったら、...";
		final String templateRomaji2 = "%skattara, ...";		
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(
				informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeOuToOmou(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult volitionalForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_VOLITIONAL);
		
		if (volitionalForm == null) {
			return null;
		}
		
		final String templateKanji = "%sと思う";
		final String templateKana = "%sとおもう";
		final String templateRomaji = "%s to omou";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(volitionalForm, templateKanji, templateKana, templateRomaji, true);		
	}

	private static ExampleResult makeOuToOmotteIru(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult volitionalForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_VOLITIONAL);
		
		if (volitionalForm == null) {
			return null;
		}
		
		final String templateKanji = "%sと思っている";
		final String templateKana = "%sとおもっている";
		final String templateRomaji = "%s to omotte iru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(volitionalForm, templateKanji, templateKana, templateRomaji, true);		
	}
	
	private static ExampleResult makeTaraDouDesuKaExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);

		final String templateKanji = "%sらどうですか";
		final String templateKana = "%sらどうですか";
		final String templateRomaji = "%sra dou desu ka";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeOkuExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sおく";
		final String templateKana = "%sおく";
		final String templateRomaji = "%s oku";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeAgeruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "私 [は/が] [odbiorca] に%sあげる";
		final String templateKana = "わたし [は/が] [odbiorca] に%sあげる";
		final String templateRomaji = "watashi [wa/ga] [odbiorca] ni %s ageru";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTeKureruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "[dający] [は/が] %sくれる";
		final String templateKana = "[dający] [は/が] %sくれる";
		final String templateRomaji = "[dający] [wa/ga] %s kureru";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTeMorauExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "[odbiorca] [は/が] [dający] [に/から] %sもらう";
		final String templateKana = "[odbiorca] [は/が] [dający] [に/から] %sもらう";
		final String templateRomaji = "[odbiorca] [wa/ga] [dający] [ni/kara] %s morau";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeRequest(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String[][] templates = new String[][] {
				{ "%sいただけませんか", "%sいただけませんか", "%s itadakemasen ka" },
				{ "%sくれませんか", "%sくれませんか", "%s kuremasen ka" },
				{ "%sくれない", "%sくれない", "%s kurenai" }
		};
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult currentExampleResult = null;
		ExampleResult startExampleResult = null;
		
		for (int idx = 0; idx < templates.length; ++idx) {
			
			if (idx == 0) {
				startExampleResult = currentExampleResult = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						GrammaExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
				currentExampleResult.setAlternative(alternativeExampleResult);
				
				currentExampleResult = alternativeExampleResult;				
			}
		}
		
		return startExampleResult;
	}
	
	private static ExampleResult makeToIIToOthers(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sといいですね";
		final String templateKana = "%sといいですね";
		final String templateRomaji = "%s to ii desu ne";
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeToIIToMe(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji1 = "%sといいんですが";
		final String templateKana1 = "%sといいんですが";
		final String templateRomaji1 = "%s to ii n desu ga";

		final String templateKanji2 = "%sといいんですけど";
		final String templateKana2 = "%sといいんですけど";
		final String templateRomaji2 = "%s to ii n desu kedo";
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(exampleResult2);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		exampleResult2.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji1, templateKana1, templateRomaji1, true));
		exampleResult2.getAlternative().setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult1;
	}
	
	private static ExampleResult makeToki(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%s時、...";
		final String templateKana = "%sとき、...";
		final String templateRomaji = "%s toki, ...";
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeTeArigatou(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String[][] templates = new String[][] {
				{ "%sくださって、ありがとうございました", "%sくださって、ありがとうございました", "%s kudasatte, arigatou gozaimashita" },
				{ "%sくれて、ありがとう", "%sくれて、ありがとう", "%s kurete, arigatou" },
				{ "%s、ありがとう", "%s、ありがとう", "%s, arigatou" }
		};
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult currentExampleResult = null;
		ExampleResult startExampleResult = null;
		
		for (int idx = 0; idx < templates.length; ++idx) {
			
			if (idx == 0) {
				startExampleResult = currentExampleResult = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						GrammaExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
				currentExampleResult.setAlternative(alternativeExampleResult);
				
				currentExampleResult = alternativeExampleResult;				
			}
		}
		
		return startExampleResult;
	}
	
	private static ExampleResult makeKuteArigatou(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String[][] templates = new String[][] {
				{ "%sくださって、ありがとうございました", "%sくださって、ありがとうございました", "%s kudasatte, arigatou gozaimashita" },
				{ "%sくてくれて、ありがとう", "%sくてくれて、ありがとう", "%skute kurete, arigatou" },
				{ "%sくて、ありがとう", "%sくて、ありがとう", "%skute, arigatou" }				
		};
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		ExampleResult currentExampleResult = null;
		ExampleResult startExampleResult = null;
		
		for (int idx = 0; idx < templates.length; ++idx) {
			
			if (idx == 0) {
				startExampleResult = currentExampleResult = GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
				currentExampleResult.setAlternative(alternativeExampleResult);
				
				currentExampleResult = alternativeExampleResult;				
			}
		}
		
		return startExampleResult;
	}

	private static ExampleResult makeTeSumimasen(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String[][] templates = new String[][] {
				{ "%s、すみませんでした", "%s、すみませんでした", "%s, sumimasen deshita" },
				{ "%s、すみません", "%s、すみません", "%s, sumimasen" },
				{ "%s、ごめん", "%s、ごめん", "%s, gomen" }
		};
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult currentExampleResult = null;
		ExampleResult startExampleResult = null;
		
		for (int idx = 0; idx < templates.length; ++idx) {
			
			if (idx == 0) {
				startExampleResult = currentExampleResult = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						GrammaExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
				currentExampleResult.setAlternative(alternativeExampleResult);
				
				currentExampleResult = alternativeExampleResult;				
			}
		}
		
		return startExampleResult;
	}
	
	private static ExampleResult makeKuteSumimasen(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String[][] templates = new String[][] {
				{ "%sくて、すみませんでした", "%sくて、すみませんでした", "%skute, sumimasen deshita" },
				{ "%sくて、すみません", "%sくて、すみません", "%skute, sumimasen" },
				{ "%sくて、ごめん", "%sくて、ごめん", "%skute, gomen" }	
		};
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		ExampleResult currentExampleResult = null;
		ExampleResult startExampleResult = null;
		
		for (int idx = 0; idx < templates.length; ++idx) {
			
			if (idx == 0) {
				startExampleResult = currentExampleResult = GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
				currentExampleResult.setAlternative(alternativeExampleResult);
				
				currentExampleResult = alternativeExampleResult;				
			}
		}
		
		return startExampleResult;
	}
	
	private static ExampleResult makeSouDesuHear(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sそうです";
		final String templateKana = "%sそうです";
		final String templateRomaji = "%s sou desu";

		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}	

	private static ExampleResult makeTte(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sって";
		final String templateKana = "%sって";
		final String templateRomaji = "%stte";

		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeNakuteMoIiDesu(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "%sくてもいいです";
		final String templateKana2 = "%sくてもいいです";
		final String templateRomaji2 = "%skute mo ii desu";
		
		return GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true);
	}
	
	private static ExampleResult makeMitaiDesuExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji1 = "%sみたいです";
		final String templateKana1 = "%sみたいです";
		final String templateRomaji1 = "%s mitai desu";
		
		final String templateKanji2 = "%sみたいな";
		final String templateKana2 = "%sみたいな";
		final String templateRomaji2 = "%s mitai na";
		
		final String templateKanji3 = "... みたいに%s";
		final String templateKana3 = "... みたいに%s";
		final String templateRomaji3 = "... mitai ni %s";

		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);

		ExampleResult alternative2 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		alternative2.setInfo("Zachowuje się, jak na-przymiotnik");
		exampleResult.setAlternative(alternative2);
		
		ExampleResult alternative3 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji3, templateKana3, templateRomaji3, false);
		alternative2.setAlternative(alternative3);
		
		GrammaFormConjugateResult informalPast = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		ExampleResult alternative4 = GrammaExampleHelper.makeSimpleTemplateExample(informalPast,  templateKanji1, templateKana1, templateRomaji1, true);
		alternative3.setAlternative(alternative4);
		
		ExampleResult alternative5 = GrammaExampleHelper.makeSimpleTemplateExample(informalPast, templateKanji2, templateKana2, templateRomaji2, true);
		alternative5.setInfo("Zachowuje się, jak na-przymiotnik");
		alternative4.setAlternative(alternative5);

		ExampleResult alternative6 = GrammaExampleHelper.makeSimpleTemplateExample(informalPast, templateKanji3, templateKana3, templateRomaji3, false);
		alternative5.setAlternative(alternative6);	
			
		return exampleResult;
	}
	
	private static ExampleResult makeMaeNi(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%s前に, ...";
		final String templateKana = "%sまえに, ...";
		final String templateRomaji = "%s mae ni, ...";

		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);	
	}
	
	private static ExampleResult makeTeKara(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sから, ...";
		final String templateKana = "%sから, ...";
		final String templateRomaji = "%s kara, ...";
		
		GrammaFormConjugateResult verbTe = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);

		return GrammaExampleHelper.makeSimpleTemplateExample(verbTe, templateKanji, templateKana, templateRomaji, true);	
	}
	
	private static ExampleResult makeTeShimau(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult verbTe = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		final String templateKanji1 = "%sしまう";
		final String templateKana1 = "%sしまう";
		final String templateRomaji1 = "%s shimau";

		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(verbTe, templateKanji1, templateKana1, templateRomaji1, true);

		ExampleResult exampleResult2 = null;
		
		if (verbTe.getKanaList().get(0).endsWith("て") == true) {
			
			final String templateKanji2 = "%sちゃう";
			final String templateKana2 = "%sちゃう";
			final String templateRomaji2 = "%schau";

			exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExampleWithKanaLastCharAndRomajiTwoCharsRemove(verbTe, templateKanji2, templateKana2, templateRomaji2, true);
			
			List<String> exampleResultRomajiList = exampleResult2.getRomajiList();
			List<String> newExampleResultRomajiList = new ArrayList<String>();
			
			String templateRomajiToCheck = String.format(templateRomaji2, "t");
			String templateRomajiToChange = String.format(templateRomaji2, "c");
			
			for (String currentExampleResultRomaji : exampleResultRomajiList) {
				
				if (currentExampleResultRomaji.endsWith(templateRomajiToCheck) == true) {
					String newExampleResultRomaji = currentExampleResultRomaji.substring(0, currentExampleResultRomaji.length() - templateRomajiToCheck.length()) + templateRomajiToChange;
					
					newExampleResultRomajiList.add(newExampleResultRomaji);
				} else {
					newExampleResultRomajiList.add(currentExampleResultRomaji);
				}
			}
			
			exampleResult2.setRomajiList(newExampleResultRomajiList);
			
		} else if (verbTe.getKanaList().get(0).endsWith("で") == true) {

			final String templateKanji2 = "%sじゃう";
			final String templateKana2 = "%sじゃう";
			final String templateRomaji2 = "%sjau";

			exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExampleWithKanaLastCharAndRomajiTwoCharsRemove(verbTe, templateKanji2, templateKana2, templateRomaji2, true);
			
		} else {
			throw new RuntimeException("Bad te form for: " + verbTe.getKanaList().get(0));
		}
		
		exampleResult1.setAlternative(exampleResult2);
		
		final String templateKanji3 = "%sしまいました";
		final String templateKana3 = "%sしまいました";
		final String templateRomaji3 = "%s shimaimashita";

		ExampleResult exampleResult3 = GrammaExampleHelper.makeSimpleTemplateExample(verbTe, templateKanji3, templateKana3, templateRomaji3, true);
				
		exampleResult2.setAlternative(exampleResult3);
		
		ExampleResult exampleResult4 = null;
		
		if (verbTe.getKanaList().get(0).endsWith("て") == true) {
			
			final String templateKanji2 = "%sちゃいました";
			final String templateKana2 = "%sちゃいました";
			final String templateRomaji2 = "%schaimashita";

			exampleResult4 = GrammaExampleHelper.makeSimpleTemplateExampleWithKanaLastCharAndRomajiTwoCharsRemove(verbTe, templateKanji2, templateKana2, templateRomaji2, true);
			
			List<String> exampleResultRomajiList = exampleResult4.getRomajiList();
			List<String> newExampleResultRomajiList = new ArrayList<String>();
			
			String templateRomajiToCheck = String.format(templateRomaji2, "t");
			String templateRomajiToChange = String.format(templateRomaji2, "c");
			
			for (String currentExampleResultRomaji : exampleResultRomajiList) {
				
				if (currentExampleResultRomaji.endsWith(templateRomajiToCheck) == true) {
					String newExampleResultRomaji = currentExampleResultRomaji.substring(0, currentExampleResultRomaji.length() - templateRomajiToCheck.length()) + templateRomajiToChange;
					
					newExampleResultRomajiList.add(newExampleResultRomaji);
				} else {
					newExampleResultRomajiList.add(currentExampleResultRomaji);
				}
			}
			
			exampleResult4.setRomajiList(newExampleResultRomajiList);
			
		} else if (verbTe.getKanaList().get(0).endsWith("で") == true) {

			final String templateKanji2 = "%sじゃいました";
			final String templateKana2 = "%sじゃいました";
			final String templateRomaji2 = "%sjaimashita";

			exampleResult4 = GrammaExampleHelper.makeSimpleTemplateExampleWithKanaLastCharAndRomajiTwoCharsRemove(verbTe, templateKanji2, templateKana2, templateRomaji2, true);
			
		} else {
			throw new RuntimeException("Bad te form for: " + verbTe.getKanaList().get(0));
		}
		
		exampleResult3.setAlternative(exampleResult4);
		
		return exampleResult1;
	}
	
	private static ExampleResult makeTo(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sと, ...";
		final String templateKana = "%sと, ...";
		final String templateRomaji = "%s to, ...";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeNagara(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);

		final String templateKanji = "%sながら, ...";
		final String templateKana = "%sながら, ...";
		final String templateRomaji = "%s nagara, ...";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeBaYokatta(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_BA_AFFIRMATIVE);

		final String templateKanji = "%sよかった";
		final String templateKana = "%sよかった";
		final String templateRomaji = "%s yokatta";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeTeYokatta(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);

		final String templateKanji = "%sよかった";
		final String templateKana = "%sよかった";
		final String templateRomaji = "%s yokatta";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeNakuteYokatta(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "%sくてよかった";
		final String templateKana2 = "%sくてよかった";
		final String templateRomaji2 = "%skute yokatta";
		
		return GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true);
	}
	
	private static ExampleResult makeBaNegativeYokatta(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_BA_NEGATIVE);

		final String templateKanji = "%sよかった";
		final String templateKana = "%sよかった";
		final String templateRomaji = "%s yokatta";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeKeigoKudasai(KeigoHelper keigoHelper, DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		// check		
		List<AttributeType> attributeList = dictionaryEntry.getAttributeList();
		
		String kanji = dictionaryEntry.getKanji();
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		for (String currentKana : kanaList) {
			
			if (currentKana.equals("ある") == true) {
				return null;
			}
		}
		
		// is keigo high
		boolean isKeigoHigh = false;
		
		if (attributeList.contains(AttributeType.VERB_KEIGO_HIGH) == true) {
			isKeigoHigh = true;
		}
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		final String templateOKanji = "お%sください";
		final String templateOKana = "お%sください";
		final String templateORomaji = "o%s kudasai";
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			
			final String woSuruKana = "をする";
			final String woSuruRomaji = "o suru";

			final String suruKana = "する";
			final String suruRomaji = "suru";
			
			final String templateGoSuruKanji = "ご%sください";
			final String templateGoSuruKana = "ご%sください";
			final String templateGoSuruRomaji = "go%s kudasai";
			
			if (kanaList.get(0).endsWith(woSuruKana) == true) {
				
				if (kanji != null) {
					
					if (kanji.endsWith(woSuruKana) == false) {
						throw new RuntimeException("Kanji: " + kanji);
					}
					
					kanji = kanji.substring(0, kanji.length() - woSuruKana.length());					
				}
				
				List<String> newKanaList = new ArrayList<String>();
				
				for (String currentKanaList : kanaList) {					
					newKanaList.add(currentKanaList.substring(0, currentKanaList.length() - woSuruKana.length()));
				}
				
				kanaList = newKanaList;
				
				List<String> newRomajiList = new ArrayList<String>();
				
				for (String currentRomajiList : romajiList) {
					
					if (currentRomajiList.endsWith(woSuruRomaji) == false) {
						throw new RuntimeException("Romaji: " + kanji);
					}
					
					newRomajiList.add(currentRomajiList.substring(0, currentRomajiList.length() - woSuruRomaji.length()));
				}
				
				romajiList = newRomajiList;
				
				return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry.getPrefixKana(), kanji, kanaList, dictionaryEntry.getPrefixRomaji(),
						romajiList, templateGoSuruKanji, templateGoSuruKana, templateGoSuruRomaji, false);
				
			} else if (kanaList.get(0).endsWith(suruKana) == true) {
				
				if (kanji != null) {
					
					if (kanji.endsWith(suruKana) == false) {
						throw new RuntimeException("Kanji: " + kanji);
					}
					
					kanji = kanji.substring(0, kanji.length() - suruKana.length());					
				}
				
				List<String> newKanaList = new ArrayList<String>();
				
				for (String currentKanaList : kanaList) {					
					newKanaList.add(currentKanaList.substring(0, currentKanaList.length() - suruKana.length()));
				}
				
				kanaList = newKanaList;
				
				List<String> newRomajiList = new ArrayList<String>();
				
				for (String currentRomajiList : romajiList) {
					
					if (currentRomajiList.endsWith(suruRomaji) == false) {
						throw new RuntimeException("Romaji: " + kanji);
					}
					
					newRomajiList.add(currentRomajiList.substring(0, currentRomajiList.length() - suruRomaji.length()));
				}
				
				romajiList = newRomajiList;
				
				return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry.getPrefixKana(), kanji, kanaList, dictionaryEntry.getPrefixRomaji(),
						romajiList, templateGoSuruKanji, templateGoSuruKana, templateGoSuruRomaji, false);	
				
			} else {				
				GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
				
				return GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateOKanji, templateOKana, templateORomaji, false);				
			}
			
		} else {
			
			if (isKeigoHigh == true) {
				
				KeigoEntry keigoEntry = keigoHelper.getKeigoHighEntryFromKeigoWord(dictionaryEntry.getKanji(), null, dictionaryEntry.getKanaList().get(0), null);
				
				if (keigoEntry == null) {
					throw new RuntimeException("Empty keigo entry for: " + dictionaryEntry.getKanji() + " - " + dictionaryEntry.getKanaList().get(0));
				}
				
				return makeKeigoKudasaiForKeigoEntry(dictionaryEntry, keigoEntry, true);
				
			} else {
				
				KeigoEntry keigoEntry = keigoHelper.findKeigoHighEntry(dictionaryEntry.getDictionaryEntryType(), dictionaryEntry.getKanji(), dictionaryEntry.getKanaList(), dictionaryEntry.getRomajiList());

				if (keigoEntry == null) {
					GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
					
					return GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateOKanji, templateOKana, templateORomaji, false);
					
				} else {
					
					return makeKeigoKudasaiForKeigoEntry(dictionaryEntry, keigoEntry, false);
				}
			}
		}
	}
	
	private static ExampleResult makeKeigoKudasaiForKeigoEntry(DictionaryEntry dictionaryEntry, KeigoEntry keigoEntry, boolean isKeigoHigh) {

		String kanji = null;
		List<String> kanaList = null;
		List<String> romajiList = null;
		
		if (isKeigoHigh == false) {
			
			kanji = dictionaryEntry.getKanji();
			kanaList = dictionaryEntry.getKanaList();
			romajiList = dictionaryEntry.getRomajiList();
			
		} else {
			
			kanji = keigoEntry.getKeigoKanji(false);
			
			kanaList = new ArrayList<String>();
			kanaList.add(keigoEntry.getKeigoKana(false));
			
			romajiList = new ArrayList<String>();
			romajiList.add(keigoEntry.getKeigoRomaji(false));
		}
		
		DictionaryEntryType keigoDictionaryEntryType = keigoEntry.getKeigoDictionaryEntryType();
		
		if (keigoDictionaryEntryType != DictionaryEntryType.WORD_VERB_U) {
			throw new RuntimeException("Keigo dictionary entry type != WORD_VERB_U");
		}
		
		String keigoKanji = keigoEntry.getKeigoKanji(false);
		String keigoKana = keigoEntry.getKeigoKana(false);
		String keigoRomaji = keigoEntry.getKeigoRomaji(false);
		
		if (kanji != null) {
			
			if (isKeigoHigh == false) {

				if (keigoEntry.getKanji() != null && keigoKanji != null) {				
					kanji =  replaceEndWith(kanji, keigoEntry.getKanji(), "お" + keigoKanji);
				} else {				
					kanji = replaceEndWith(kanji, keigoEntry.getKana(), "お" + keigoKana);
				}
				
			} else {
				kanji = keigoKanji;
			}
			
			if (kanji.endsWith("る") == true) {
				kanji = kanji.substring(0, kanji.length() - 1) + "り";
				
			} else if (kanji.endsWith("う") == true) {
				kanji = kanji.substring(0, kanji.length() - 1) + "い";
			}
			
			if (kanji.startsWith("おお") == true) {
				kanji = kanji.substring(1);
			} else if (kanji.startsWith("おご") == true) {
				kanji = kanji.substring(1);
			}
						
			kanji += "ください";
		}

		List<String> newKanaList = new ArrayList<String>();
		
		for (String currentKana : kanaList) {		
			
			if (isKeigoHigh == false) {
				currentKana = replaceEndWith(currentKana, keigoEntry.getKana(), "お" + keigoKana);
			} else {
				currentKana = keigoKana;
			}			
			
			if (currentKana.endsWith("る") == true) {
								
				currentKana = currentKana.substring(0, currentKana.length() - 1) + "り";
				
			} else if (currentKana.endsWith("う") == true) {
				currentKana = currentKana.substring(0, currentKana.length() - 1) + "い";
			}
			
			if (currentKana.startsWith("おお") == true) {
				currentKana = currentKana.substring(1);
			} else if (currentKana.startsWith("おご") == true) {
				currentKana = currentKana.substring(1);
			}
						
			currentKana += "ください";
			
			newKanaList.add(currentKana);
		}
					
		kanaList = newKanaList;

		List<String> newRomajiList = new ArrayList<String>();
		
		for (String currentRomaji : romajiList) {
			
			if (isKeigoHigh == false) {		
				currentRomaji = replaceEndWith(currentRomaji, keigoEntry.getRomaji(), (isKeigoHigh == false ? "o" : "") + keigoRomaji);
			} else {
				currentRomaji = keigoRomaji;
			}
			
			if (currentRomaji.endsWith("ru") == true) {
				currentRomaji = currentRomaji.substring(0, currentRomaji.length() - 2) + "ri";
				
			} else if (currentRomaji.endsWith("u") == true) {
				currentRomaji = currentRomaji.substring(0, currentRomaji.length() - 1) + "i";
			}
			
			if (currentRomaji.startsWith("oo") == true) {
				currentRomaji = currentRomaji.substring(1);
			} else if (currentRomaji.startsWith("ogo") == true) {
				currentRomaji = currentRomaji.substring(1);
			}
						
			currentRomaji += " kudasai";

			newRomajiList.add(currentRomaji);
		}
				
		romajiList = newRomajiList;
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry.getPrefixKana(), kanji, kanaList, dictionaryEntry.getPrefixRomaji(),
				romajiList, "%s", "%s", "%s", false);		
	}
	
	private static String replaceEndWith(String word, String wordEndWithToReplace, String replacement) {
		return word.substring(0, word.length() - wordEndWithToReplace.length()) + replacement; 
	}
	
	private static ExampleResult makeHazuDesu(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji1 = "%sはずです";
		final String templateKana1 = "%sはずです";
		final String templateRomaji1 = "%s hazu desu";
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		final String templateKanji2 = "%sはずでした";
		final String templateKana2 = "%sはずでした";
		final String templateRomaji2 = "%s hazu deshita";
		
		ExampleResult alternative1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(alternative1);
		
		return exampleResult1;
	}
	
	private static ExampleResult makeNaiDe(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sで、...";
		final String templateKana = "%sで、...";
		final String templateRomaji = "%s de, ...";
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		return GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeQuestionsWithLargerSentences(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji1 = "[słówko pytające] ... %s か 知っています, 覚えていません, わかりません, etc";
		final String templateKana1 = "[słówko pytające] ... %s か しっています, おぼえていません, わかりません, etc";
		final String templateRomaji1 = "[słówko pytające] ... %s ka shitte imasu, oboete imasen, wakarimasen, etc";
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, false);
		
		final String templateKanji2 = "%s か (どうか) 知っています, 覚えていません, わかりません, etc";
		final String templateKana2 = "%s か (どうか) しっています, おぼえていません, わかりません, etc";
		final String templateRomaji2 = "%s ka (douka) shitte imasu, oboete imasen, wakarimasen, etc";
		
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, false);
		
		exampleResult1.setAlternative(exampleResult2);
		
		return exampleResult1;
	}
	
	private static ExampleResult makeYasui(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
		
		String templateKanji1 = "%sやすいです";
		String templateKana1 = "%sやすいです";
		String templateRomaji1 = "%s yasui desu";
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji1, templateKana1, templateRomaji1, false);

		String templateKanji2 = "%sやすかったです";
		String templateKana2 = "%sやすかったです";
		String templateRomaji2 = "%s yasukatta desu";
		
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji2, templateKana2, templateRomaji2, false);
		
		exampleResult1.setAlternative(exampleResult2);
		
		return exampleResult1;
	}
	
	private static ExampleResult makeNikui(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
		
		String templateKanji1 = "%sにくいです";
		String templateKana1 = "%sにくいです";
		String templateRomaji1 = "%s nikui desu";
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji1, templateKana1, templateRomaji1, false);

		String templateKanji2 = "%sにくかったです";
		String templateKana2 = "%sにくかったです";
		String templateRomaji2 = "%s nikukatta desu";
		
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji2, templateKana2, templateRomaji2, false);
		
		exampleResult1.setAlternative(exampleResult2);
		
		return exampleResult1;
	}
	
	private static ExampleResult makeTeAruExample(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sある";
		final String templateKana = "%sある";
		final String templateRomaji = "%s aru";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
		
		if (dictionaryEntry.getPrefixKana() != null && dictionaryEntry.getPrefixKana().equals("を") == true) {
			exampleResult.setPrefixKana("が");
			exampleResult.setPrefixRomaji("ga");
		}
		
		return exampleResult;
	}
	
	private static ExampleResult makeTeIruAidaNi(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		final String templateKanji1 = "%sいる間に, ...";
		final String templateKana1 = "%sいるあいだに, ...";
		final String templateRomaji1 = "%s iru aida ni, ...";

		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji1, templateKana1, templateRomaji1, true);
		
		final String templateKanji2 = "%sいる間, ...";
		final String templateKana2 = "%sいるあいだ, ...";
		final String templateRomaji2 = "%s iru aida, ...";

		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji2, templateKana2, templateRomaji2, true);
		exampleResult2.setInfo("Czynność wykonywana przez dłuższy okres czasu");
		
		exampleResult1.setAlternative(exampleResult2);
		
		return exampleResult1;
	}
	
	private static ExampleResult makeTeHoshii(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		final String templateKanji1 = "[osoba に] ... %sほしいです";
		final String templateKana1 = "[osoba に] ... %sほしいです";
		final String templateRomaji1 = "[osoba ni] ... %s hoshii desu";

		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji1, templateKana1, templateRomaji1, false);
				
		return exampleResult1;
	}

	private static ExampleResult makeNegativeTeHoshii(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		final String templateKanji1 = "[osoba に] ... %sほしくないです";
		final String templateKana1 = "[osoba に] ... %sほしくないです";
		final String templateRomaji1 = "[osoba ni] ... %s hoshikunai desu";

		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(teForm, templateKanji1, templateKana1, templateRomaji1, false);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "[osoba に] ... %sでほしいです";
		final String templateKana2 = "[osoba に] ... %sでほしいです";
		final String templateRomaji2 = "[osoba ni] ... %s de hoshii desu";
		
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, false);
		
		exampleResult1.setAlternative(exampleResult2);
		
		return exampleResult1;
	}
	
	private static ExampleResult makeMakeLet(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult verbCausativeInFormalPresentGrammaFormConjugateResult1 = grammaFormCache.get(GrammaFormConjugateResultType.VERB_CAUSATIVE_INFORMAL_PRESENT);
		GrammaFormConjugateResult verbCausativeFormalPastGrammaFormConjugateResult2 = grammaFormCache.get(GrammaFormConjugateResultType.VERB_CAUSATIVE_FORMAL_PAST);
		
		final String templateKanji = "[osoba zmuszająca] は/が [osoba zmuszana] に ... %s";
		final String templateKana = "[osoba zmuszająca] は/が [osoba zmuszana] に ... %s";
		final String templateRomaji = "[osoba zmuszająca] wa/ga [osoba zmuszana] ni ... %s";

		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(verbCausativeInFormalPresentGrammaFormConjugateResult1, templateKanji, templateKana, templateRomaji, false);
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(verbCausativeFormalPastGrammaFormConjugateResult2, templateKanji, templateKana, templateRomaji, false);
		
		exampleResult1.setAlternative(exampleResult2);
		
		return exampleResult1;
	}
	
	private static ExampleResult makeLet(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult verbCausativeTeGrammaFormConjugateResult = grammaFormCache.get(GrammaFormConjugateResultType.VERB_CAUSATIVE_TE);
		
		final String templateKanji1 = "[osoba pozwalająca] は/が [osoba otrzymujące pozwolenie] に ... %sあげる";
		final String templateKana1 = "[osoba pozwalająca] は/が [osoba otrzymujące pozwolenie] に ... %sあげる";
		final String templateRomaji1 = "[osoba pozwalająca] wa/ga [osoba otrzymujące pozwolenie] ni ... %s ageru";

		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(verbCausativeTeGrammaFormConjugateResult, templateKanji1, templateKana1, templateRomaji1, false);
		
		final String templateKanji2 = "[osoba pozwalająca] は/が  ... %sくれる";
		final String templateKana2 = "[osoba pozwalająca] は/が  ... %sくれる";
		final String templateRomaji2 = "[osoba pozwalająca] wa/ga ... %s kureru";
		
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(verbCausativeTeGrammaFormConjugateResult, templateKanji2, templateKana2, templateRomaji2, false);
		exampleResult2.setInfo("Do mnie");
		
		final String templateKanji3 = "私に ... %sください";
		final String templateKana3 = "私に ... %sください";
		final String templateRomaji3 = "watashi ni ... %s kudasai";

		ExampleResult exampleResult3 = GrammaExampleHelper.makeSimpleTemplateExample(verbCausativeTeGrammaFormConjugateResult, templateKanji3, templateKana3, templateRomaji3, false);
		exampleResult3.setInfo("Pozwól mi");
		
		exampleResult2.setAlternative(exampleResult3);
		
		exampleResult1.setAlternative(exampleResult2);
		
		return exampleResult1;
	}
}
