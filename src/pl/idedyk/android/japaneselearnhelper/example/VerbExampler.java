package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.VerbGrammaConjugater;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;

public class VerbExampler {
	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {
		
		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// like : suki		
		ExampleHelper.addExample(result, ExampleGroupType.VERB_LIKE, makeSukiExample(dictionaryEntry));
		
		// dislike : kirai		
		ExampleHelper.addExample(result, ExampleGroupType.VERB_DISLIKE, makeKiraiExample(dictionaryEntry));
		
		// stem + ni + iku
		ExampleHelper.addExample(result, ExampleGroupType.VERB_STEM_NI_IKU, makeStemNiIkuExample(dictionaryEntry));
		
		// te iru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_IRU, makeTeIruExample(dictionaryEntry));
		
		// te kudasai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_KUDASAI, makeTeKudasaiExample(dictionaryEntry));
		
		// te mo ii desu
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MO_II, makeTeMoIiExample(dictionaryEntry));
		
		// te wa ikemasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_HA_IKEMASEN, makeTeHaIkemasenExample(dictionaryEntry));
		
		// nai de kudasai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_NAI_DE_KUDASAI, makeNaiDeKudasai(dictionaryEntry));
		
		// mada te imasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_MADA_TE_IMASEN, makeMadaTeImasen(dictionaryEntry));
		
		// tsumori desu
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TSUMORI_DESU, makeTsumoriDesu(dictionaryEntry));
		
		// koto ga aru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_KOTO_GA_ARU, makeKotoGaAru(dictionaryEntry));
		
		// tai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TAI, makeTaiExample(dictionaryEntry));

		// tagatte iru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TAGATTE_IRU, makeTagatteIruExample(dictionaryEntry));
		
		// n desu
		ExampleHelper.addExample(result, ExampleGroupType.VERB_N_DESU, makeNDesuExample(dictionaryEntry));
		
		// sugiru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_SUGIRU, makeSugiruExample(dictionaryEntry));
		
		// advice
		ExampleHelper.addExample(result, ExampleGroupType.VERB_ADVICE, makeAdviceExample(dictionaryEntry));
		
		// nakucha ikemasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_NAKUCHA_IKEMASEN, makeNakuchaIkemasenExample(dictionaryEntry));
		
		// deshou
		ExampleHelper.addExample(result, ExampleGroupType.VERB_DESHOU, makeDeshouExample(dictionaryEntry));
		
		// te miru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MIRU, makeTeMiruExample(dictionaryEntry));

		// kamoshi remasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));

		// tara
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TARA, makeTaraExample(dictionaryEntry));
		
		// tara dou desu ka
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TARA_DOU_DESU_KA, makeTaraDouDesuKaExample(dictionaryEntry));

		// ou to omou
		ExampleHelper.addExample(result, ExampleGroupType.VERB_OU_TO_OMOU, makeOuToOmou(dictionaryEntry));

		// ou to omotte iru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_OU_TO_OMOTTE_IRU, makeOuToOmotteIru(dictionaryEntry));
		
		// te oku
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_OKU, makeTeOkuExample(dictionaryEntry));
		
		// te ageru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_AGERU, makeTeAgeruExample(dictionaryEntry));
		
		// te kureru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_KURERU, makeTeKureruExample(dictionaryEntry));
		
		// te morau
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MORAU, makeTeMorauExample(dictionaryEntry));		
		
		// te kudasai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_REQUEST, makeRequest(dictionaryEntry));
		
		return result;
	}

	private static ExampleResult makeStemNiIkuExample(DictionaryEntry dictionaryEntry) {
		
		final String[][] templates = {
				{ "[miejsce] [に/へ] %sに行く", "[miejsce] [に/へ] %sにいく", "[miejsce] [に/へ] %s ni iku" }, 
				{ "[miejsce] [に/へ] %sに来る", "[miejsce] [に/へ] %sにくる", "[miejsce] [に/へ] %s ni kuru" },
				{ "[miejsce] [に/へ] %sに帰る", "[miejsce] [に/へ] %sにかえる", "[miejsce] [に/へ] %s ni kaeru" }
		};
		
		GrammaFormConjugateResult stemForm = VerbGrammaConjugater.makeStemForm(dictionaryEntry);
		
		ExampleResult result = ExampleHelper.makeSimpleTemplateExample(stemForm, templates[0][0], templates[0][1], templates[0][2], false);
		
		ExampleResult alternative1 = ExampleHelper.makeSimpleTemplateExample(stemForm, templates[1][0], templates[1][1], templates[1][2], false);
		
		ExampleResult alternative2 = ExampleHelper.makeSimpleTemplateExample(stemForm, templates[2][0], templates[2][1], templates[2][2], false);
		
		alternative1.setAlternative(alternative2);		
		result.setAlternative(alternative1);
		
		return result;
	}

	private static ExampleResult makeTeIruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sいる";
		final String templateKana = "%sいる";
		final String templateRomaji = "%s iru";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeKudasaiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sください";
		final String templateKana = "%sください";
		final String templateRomaji = "%s kudasai";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);		
	}
	
	private static ExampleResult makeTeMoIiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sもいいです";
		final String templateKana = "%sもいいです";
		final String templateRomaji = "%s mo ii desu";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		ExampleResult moIidesu = ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
		
		moIidesu.setAlternative(
				ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji + "か", templateKana + "か", templateRomaji + " ka", true));
		
		return moIidesu;
	}
	
	private static ExampleResult makeNaiDeKudasai(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sでください";
		final String templateKana = "%sでください";
		final String templateRomaji = "%s de kudasai";
		
		GrammaFormConjugateResult informalPresentNegativeForm = VerbGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeMadaTeImasen(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "まだ%sいません";
		final String templateKana = "まだ%sいません";
		final String templateRomaji = "mada %s imasen";

		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTsumoriDesu(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult informalPresentForm = VerbGrammaConjugater.makeInformalPresentForm(dictionaryEntry);
		
		GrammaFormConjugateResult informalPresentNegativeForm = VerbGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		final String templateKanji = "%sつもりです";
		final String templateKana = "%sつもりです";
		final String templateRomaji = "%s tsumori desu";
		
		ExampleResult tsumoriDesu = ExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		ExampleResult naiTsumoriDesu = ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true);
		
		tsumoriDesu.setAlternative(naiTsumoriDesu);
		
		return tsumoriDesu;
	}
	
	private static ExampleResult makeKotoGaAru(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult informalPastForm = VerbGrammaConjugater.makeInformalPastForm(dictionaryEntry);
		
		final String templateKanji = "%sことがある";
		final String templateKana = "%sことがある";
		final String templateRomaji = "%s koto ga aru";
		
		ExampleResult kotoGaAru = ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
		
		ExampleResult kotoGaAruKa = ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji + "か", templateKana + "か", templateRomaji + " ka", true);
		
		kotoGaAru.setAlternative(kotoGaAruKa);
		
		return kotoGaAru;
	}
	
	private static ExampleResult makeTeHaIkemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sはいけません";
		final String templateKana = "%sはいけません";
		final String templateRomaji = "%s wa ikemasen";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		ExampleResult moIidesu = ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
				
		return moIidesu;
	}
	
	private static ExampleResult makeTaiExample(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult stemForm = VerbGrammaConjugater.makeStemForm(dictionaryEntry);
		
		String prefixKana = stemForm.getPrefixKana();
		
		if (prefixKana != null && prefixKana.equals("を") == true) {
			stemForm.setPrefixKana("が");
		}

		if (prefixKana != null && prefixKana.equals("を") == true) {
			stemForm.setPrefixRomaji("ga");
		}
		
		String kanji = stemForm.getKanji();
		
		if (kanji != null) { 
			stemForm.setKanji(kanji.replaceAll("を", "が"));
		}
		
		List<String> kanaList = stemForm.getKanaList();
		
		if (kanaList != null) {
			
			List<String> newKanaList = new ArrayList<String>();
			
			for (String currentKana : kanaList) {
				newKanaList.add(currentKana.replaceAll("を", "が"));
			}
			
			stemForm.setKanaList(newKanaList);
		}
		
		List<String> romajiList = stemForm.getRomajiList();
		
		if (romajiList != null) {
			
			List<String> newRomajiList = new ArrayList<String>();
			
			for (String currentRomaji : romajiList) {
				newRomajiList.add(currentRomaji.replaceAll(" o ", " ga "));
			}
			
			stemForm.setRomajiList(newRomajiList);
		}
		
		final String templateKanji = "%sたいです";
		final String templateKana = "%sたいです";
		final String templateRomaji = "%stai desu";
		
		return ExampleHelper.makeSimpleTemplateExample(stemForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeTagatteIruExample(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult stemForm = VerbGrammaConjugater.makeStemForm(dictionaryEntry);
		
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
	
	private static ExampleResult makeSugiruExample(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult stemForm = VerbGrammaConjugater.makeStemForm(dictionaryEntry);
		
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
	
	private static ExampleResult makeAdviceExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sほうがいいです";
		final String templateKana = "%sほうがいいです";
		final String templateRomaji = "%s hou ga ii desu";
		
		GrammaFormConjugateResult informalPastForm = VerbGrammaConjugater.makeInformalPastForm(dictionaryEntry);
		GrammaFormConjugateResult informalPresentNegativeForm = VerbGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		ExampleResult houGaIiDesu = ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
		
		houGaIiDesu.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return houGaIiDesu;
	}
	
	private static ExampleResult makeNakuchaIkemasenExample(DictionaryEntry dictionaryEntry) {
		
		String postfixKana = "くちゃいけません";
		String postfixRomaji = "kucha ikemasen";
		
		GrammaFormConjugateResult informalPastForm = VerbGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		String kanji = informalPastForm.getKanji();
		List<String> kanaList = informalPastForm.getKanaList();
		List<String> romajiList = informalPastForm.getRomajiList();

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
	
	private static ExampleResult makeTeMiruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sみる";
		final String templateKana = "%sみる";
		final String templateRomaji = "%s miru";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		ExampleResult teMiru = ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
				
		return teMiru;
	}
	
	private static ExampleResult makeKamoshiRemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sかもしれません";
		final String templateKana = "%sかもしれません";
		final String templateRomaji = "%s kamoshi remasen";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTaraExample(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult informalPastForm = VerbGrammaConjugater.makeInformalPastForm(dictionaryEntry);

		final String templateKanji = "%sら";
		final String templateKana = "%sら";
		final String templateRomaji = "%sra";
		
		return ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeOuToOmou(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult volitionalForm = VerbGrammaConjugater.makeVolitionalForm(dictionaryEntry);
		
		final String templateKanji = "%sと思う";
		final String templateKana = "%sとおもう";
		final String templateRomaji = "%s to omou";
		
		return ExampleHelper.makeSimpleTemplateExample(volitionalForm, templateKanji, templateKana, templateRomaji, true);		
	}

	private static ExampleResult makeOuToOmotteIru(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult volitionalForm = VerbGrammaConjugater.makeVolitionalForm(dictionaryEntry);
		
		final String templateKanji = "%sと思っている";
		final String templateKana = "%sとおもっている";
		final String templateRomaji = "%s to omotte iru";
		
		return ExampleHelper.makeSimpleTemplateExample(volitionalForm, templateKanji, templateKana, templateRomaji, true);		
	}
	
	private static ExampleResult makeTaraDouDesuKaExample(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult informalPastForm = VerbGrammaConjugater.makeInformalPastForm(dictionaryEntry);

		final String templateKanji = "%sらどうですか";
		final String templateKana = "%sらどうですか";
		final String templateRomaji = "%sra dou desu ka";
		
		return ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeOkuExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sおく";
		final String templateKana = "%sおく";
		final String templateRomaji = "%s oku";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeAgeruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "私 [は/が] [odbiorca] に%sあげる";
		final String templateKana = "わたし [は/が] [odbiorca] に%sあげる";
		final String templateRomaji = "watashi [wa/ga] [odbiorca] ni %s ageru";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTeKureruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "[dający] [は/が] %sくれる";
		final String templateKana = "[dający] [は/が] %sくれる";
		final String templateRomaji = "[dający] [wa/ga] %s kureru";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeTeMorauExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "[odbiorca] [は/が] [dający] [に/から] %sもらう";
		final String templateKana = "[odbiorca] [は/が] [dający] [に/から] %sもらう";
		final String templateRomaji = "[odbiorca] [wa/ga] [dający] [ni/kara] %s morau";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeRequest(DictionaryEntry dictionaryEntry) {
		
		String[][] templates = new String[][] {
				{ "%sいただけませんか", "%sいただけませんか", "%s itadakemasen ka" },
				{ "%sもらえませんか", "%sもらえませんか", "%s moraemasen ka" },
				{ "%sくれない", "%sくれない", "%s kurenai" }
		};
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
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
}
