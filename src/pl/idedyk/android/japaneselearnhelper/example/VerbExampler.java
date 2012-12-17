package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class VerbExampler {
	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// like : suki		
		ExampleHelper.addExample(result, ExampleGroupType.VERB_LIKE, makeSukiExample(dictionaryEntry));
		
		// dislike : kirai		
		ExampleHelper.addExample(result, ExampleGroupType.VERB_DISLIKE, makeKiraiExample(dictionaryEntry));
		
		// stem + ni + iku
		ExampleHelper.addExample(result, ExampleGroupType.VERB_STEM_NI_IKU, makeStemNiIkuExample(dictionaryEntry, grammaFormCache));
		
		// te iru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_IRU, makeTeIruExample(dictionaryEntry, grammaFormCache));
		
		// te kudasai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_KUDASAI, makeTeKudasaiExample(dictionaryEntry, grammaFormCache));
		
		// te mo ii desu
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MO_II, makeTeMoIiExample(dictionaryEntry, grammaFormCache));
		
		// te wa ikemasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_HA_IKEMASEN, makeTeHaIkemasenExample(dictionaryEntry, grammaFormCache));
		
		// nai de kudasai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_NAI_DE_KUDASAI, makeNaiDeKudasai(dictionaryEntry, grammaFormCache));
		
		// mada te imasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_MADA_TE_IMASEN, makeMadaTeImasen(dictionaryEntry, grammaFormCache));
		
		// tsumori desu
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TSUMORI_DESU, makeTsumoriDesu(dictionaryEntry, grammaFormCache));
		
		// koto ga aru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_KOTO_GA_ARU, makeKotoGaAru(dictionaryEntry, grammaFormCache));
		
		// tai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TAI, makeTaiExample(dictionaryEntry, grammaFormCache));

		// tagatte iru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TAGATTE_IRU, makeTagatteIruExample(dictionaryEntry, grammaFormCache));
		
		// n desu
		ExampleHelper.addExample(result, ExampleGroupType.VERB_N_DESU, makeNDesuExample(dictionaryEntry));
		
		// sugiru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_SUGIRU, makeSugiruExample(dictionaryEntry, grammaFormCache));
		
		// advice
		ExampleHelper.addExample(result, ExampleGroupType.VERB_ADVICE, makeAdviceExample(dictionaryEntry, grammaFormCache));
		
		// nakucha ikemasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_NAKUCHA_IKEMASEN, makeNakuchaIkemasenExample(dictionaryEntry, grammaFormCache));
		
		// deshou
		ExampleHelper.addExample(result, ExampleGroupType.VERB_DESHOU, makeDeshouExample(dictionaryEntry));
		
		// te miru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MIRU, makeTeMiruExample(dictionaryEntry, grammaFormCache));

		// kamoshi remasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		// tara dou desu ka
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TARA_DOU_DESU_KA, makeTaraDouDesuKaExample(dictionaryEntry, grammaFormCache));

		// ou to omou
		ExampleHelper.addExample(result, ExampleGroupType.VERB_OU_TO_OMOU, makeOuToOmou(dictionaryEntry, grammaFormCache));

		// ou to omotte iru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_OU_TO_OMOTTE_IRU, makeOuToOmotteIru(dictionaryEntry, grammaFormCache));
		
		// te oku
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_OKU, makeTeOkuExample(dictionaryEntry, grammaFormCache));
		
		// te ageru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_AGERU, makeTeAgeruExample(dictionaryEntry, grammaFormCache));
		
		// te kureru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_KURERU, makeTeKureruExample(dictionaryEntry, grammaFormCache));
		
		// te morau
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MORAU, makeTeMorauExample(dictionaryEntry, grammaFormCache));		
		
		// te kudasai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_REQUEST, makeRequest(dictionaryEntry, grammaFormCache));
		
		// to ii to others
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TO_II_TO_OTHERS, makeToIIToOthers(dictionaryEntry, grammaFormCache));

		// to ii to me
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TO_II_TO_ME, makeToIIToMe(dictionaryEntry, grammaFormCache));
		
		// toki
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TOKI, makeToki(dictionaryEntry, grammaFormCache));
		
		// te arigatou
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_ARIGATOU, makeTeArigatou(dictionaryEntry, grammaFormCache));

		// te kute arigatou
		ExampleHelper.addExample(result, ExampleGroupType.VERB_KUTE_ARIGATOU, makeKuteArigatou(dictionaryEntry, grammaFormCache));
		
		// te sumimasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_SUMIMASEN, makeTeSumimasen(dictionaryEntry, grammaFormCache));

		// kute sumimasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_KUTE_SUMIMASEN, makeKuteSumimasen(dictionaryEntry, grammaFormCache));
		
		// sou desu (hear)
		ExampleHelper.addExample(result, ExampleGroupType.VERB_SOU_DESU_HEAR, makeSouDesuHear(dictionaryEntry, grammaFormCache));

		// sou desu (hear)
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TTE, makeTte(dictionaryEntry, grammaFormCache));
		
		// tara
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TARA, makeTaraExample(dictionaryEntry, grammaFormCache));
		
		// nakute mo ii desu
		ExampleHelper.addExample(result, ExampleGroupType.VERB_NAKUTE_MO_II_DESU, makeNakuteMoIiDesu(dictionaryEntry, grammaFormCache));
		
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
		
		ExampleResult result = ExampleHelper.makeSimpleTemplateExample(stemForm, templates[0][0], templates[0][1], templates[0][2], false);
		
		ExampleResult alternative1 = ExampleHelper.makeSimpleTemplateExample(stemForm, templates[1][0], templates[1][1], templates[1][2], false);
		
		ExampleResult alternative2 = ExampleHelper.makeSimpleTemplateExample(stemForm, templates[2][0], templates[2][1], templates[2][2], false);
		
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
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeKudasaiExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sください";
		final String templateKana = "%sください";
		final String templateRomaji = "%s kudasai";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);		
	}
	
	private static ExampleResult makeTeMoIiExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sもいいです";
		final String templateKana = "%sもいいです";
		final String templateRomaji = "%s mo ii desu";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult moIidesu = ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
		
		moIidesu.setAlternative(
				ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji + "か", templateKana + "か", templateRomaji + " ka", true));
		
		return moIidesu;
	}
	
	private static ExampleResult makeNaiDeKudasai(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sでください";
		final String templateKana = "%sでください";
		final String templateRomaji = "%s de kudasai";
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		return ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeMadaTeImasen(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "まだ%sいません";
		final String templateKana = "まだ%sいません";
		final String templateRomaji = "mada %s imasen";

		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTsumoriDesu(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPresentForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji = "%sつもりです";
		final String templateKana = "%sつもりです";
		final String templateRomaji = "%s tsumori desu";
		
		ExampleResult tsumoriDesu = ExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		ExampleResult naiTsumoriDesu = ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true);
		
		tsumoriDesu.setAlternative(naiTsumoriDesu);
		
		return tsumoriDesu;
	}
	
	private static ExampleResult makeKotoGaAru(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		final String templateKanji = "%sことがある";
		final String templateKana = "%sことがある";
		final String templateRomaji = "%s koto ga aru";
		
		ExampleResult kotoGaAru = ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
		
		ExampleResult kotoGaAruKa = ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji + "か", templateKana + "か", templateRomaji + " ka", true);
		
		kotoGaAru.setAlternative(kotoGaAruKa);
		
		return kotoGaAru;
	}
	
	private static ExampleResult makeTeHaIkemasenExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sはいけません";
		final String templateKana = "%sはいけません";
		final String templateRomaji = "%s wa ikemasen";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult moIidesu = ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
				
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
		
		return ExampleHelper.makeSimpleTemplateExample(
				prefixKana, kanji, kanaList, prefixRomaji, romajiList, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeTagatteIruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
		
		final String templateKanji = "%sたがっている";
		final String templateKana = "%sたがっている";
		final String templateRomaji = "%stagatte iru";
		
		return ExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeNDesuExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sんです";
		final String templateKana = "%sんです";
		final String templateRomaji = "%sn desu";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSugiruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult stemForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_STEM);
		
		final String templateKanji = "%sすぎる";
		final String templateKana = "%sすぎる";
		final String templateRomaji = "%s sugiru";
		
		return ExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeSukiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sのが好き";
		final String templateKana = "%sのがすき";
		final String templateRomaji = "%s no ga suki";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeKiraiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sのがきらい";
		final String templateKana = "%sのがきらい";
		final String templateRomaji = "%s no ga kirai";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeAdviceExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sほうがいいです";
		final String templateKana = "%sほうがいいです";
		final String templateRomaji = "%s hou ga ii desu";
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		ExampleResult houGaIiDesu = ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
		
		houGaIiDesu.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
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
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeMiruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sみる";
		final String templateKana = "%sみる";
		final String templateRomaji = "%s miru";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult teMiru = ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
				
		return teMiru;
	}
	
	private static ExampleResult makeKamoshiRemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sかもしれません";
		final String templateKana = "%sかもしれません";
		final String templateRomaji = "%s kamoshi remasen";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTaraExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);

		final String templateKanji1 = "%sら、...";
		final String templateKana1 = "%sら、...";
		final String templateRomaji1 = "%sra, ...";
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "%sかったら、...";
		final String templateKana2 = "%sかったら、...";
		final String templateRomaji2 = "%skattara, ...";		
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(
				informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeOuToOmou(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult volitionalForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_VOLITIONAL);
		
		final String templateKanji = "%sと思う";
		final String templateKana = "%sとおもう";
		final String templateRomaji = "%s to omou";
		
		return ExampleHelper.makeSimpleTemplateExample(volitionalForm, templateKanji, templateKana, templateRomaji, true);		
	}

	private static ExampleResult makeOuToOmotteIru(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult volitionalForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_VOLITIONAL);
		
		final String templateKanji = "%sと思っている";
		final String templateKana = "%sとおもっている";
		final String templateRomaji = "%s to omotte iru";
		
		return ExampleHelper.makeSimpleTemplateExample(volitionalForm, templateKanji, templateKana, templateRomaji, true);		
	}
	
	private static ExampleResult makeTaraDouDesuKaExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);

		final String templateKanji = "%sらどうですか";
		final String templateKana = "%sらどうですか";
		final String templateRomaji = "%sra dou desu ka";
		
		return ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeOkuExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sおく";
		final String templateKana = "%sおく";
		final String templateRomaji = "%s oku";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeAgeruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "私 [は/が] [odbiorca] に%sあげる";
		final String templateKana = "わたし [は/が] [odbiorca] に%sあげる";
		final String templateRomaji = "watashi [wa/ga] [odbiorca] ni %s ageru";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTeKureruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "[dający] [は/が] %sくれる";
		final String templateKana = "[dający] [は/が] %sくれる";
		final String templateRomaji = "[dający] [wa/ga] %s kureru";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTeMorauExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "[odbiorca] [は/が] [dający] [に/から] %sもらう";
		final String templateKana = "[odbiorca] [は/が] [dający] [に/から] %sもらう";
		final String templateRomaji = "[odbiorca] [wa/ga] [dający] [ni/kara] %s morau";
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
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
				startExampleResult = currentExampleResult = ExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						ExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
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
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
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
		
		ExampleResult exampleResult1 = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		ExampleResult exampleResult2 = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(exampleResult2);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		exampleResult2.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji1, templateKana1, templateRomaji1, true));
		exampleResult2.getAlternative().setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult1;
	}
	
	private static ExampleResult makeToki(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%s時、...";
		final String templateKana = "%sとき、...";
		final String templateRomaji = "%s toki, ...";
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeTeArigatou(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String[][] templates = new String[][] {
				{ "%sくれて、ありがとう", "%sくれて、ありがとう", "%s kurete, arigatou" },
				{ "%s、ありがとう", "%s、ありがとう", "%s, arigatou" }
		};
		
		GrammaFormConjugateResult teForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_TE);
		
		ExampleResult currentExampleResult = null;
		ExampleResult startExampleResult = null;
		
		for (int idx = 0; idx < templates.length; ++idx) {
			
			if (idx == 0) {
				startExampleResult = currentExampleResult = ExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						ExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
				currentExampleResult.setAlternative(alternativeExampleResult);
				
				currentExampleResult = alternativeExampleResult;				
			}
		}
		
		return startExampleResult;
	}
	
	private static ExampleResult makeKuteArigatou(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String[][] templates = new String[][] {
				{ "%sくてくれて、ありがとう", "%sくてくれて、ありがとう", "%skute kurete, arigatou" },
				{ "%sくて、ありがとう", "%sくて、ありがとう", "%skute, arigatou" }				
		};
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		ExampleResult currentExampleResult = null;
		ExampleResult startExampleResult = null;
		
		for (int idx = 0; idx < templates.length; ++idx) {
			
			if (idx == 0) {
				startExampleResult = currentExampleResult = ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
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
				startExampleResult = currentExampleResult = ExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						ExampleHelper.makeSimpleTemplateExample(teForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
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
				startExampleResult = currentExampleResult = ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
			} else {
				ExampleResult alternativeExampleResult = 
						ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templates[idx][0], templates[idx][1], templates[idx][2], true);
				
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

		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}	

	private static ExampleResult makeTte(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sって";
		final String templateKana = "%sって";
		final String templateRomaji = "%stte";

		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeNakuteMoIiDesu(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "%sくてもいいです";
		final String templateKana2 = "%sくてもいいです";
		final String templateRomaji2 = "%skute mo ii desu";
		
		return ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true);
	}
}
