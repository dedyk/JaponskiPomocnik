package pl.idedyk.android.japaneselearnhelper.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary2.api.helper.Dictionary2HelperCommon;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.Gloss;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.JMdict;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.Sense;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.SenseAdditionalInfo;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.Misc2Info;

public class WordKanjiDictionaryUtils {

    public static String getWordFullTextWithMark(DictionaryEntry dictionaryEntry) {

        // FM_FIXME: do analizy co z tym zrobic

        String kanji = dictionaryEntry.getKanji();
        String prefixKana = dictionaryEntry.getPrefixKana();
        List<String> kanaList = dictionaryEntry.getKanaList();
        String prefixRomaji = dictionaryEntry.getPrefixRomaji();
        List<String> romajiList = dictionaryEntry.getRomajiList();
        List<String> translates = dictionaryEntry.getTranslates();
        String info = dictionaryEntry.getInfo();

        StringBuffer result = new StringBuffer();

        String tempPrefixKana = prefixKana != null && prefixKana.equals("") == false ? prefixKana : null;
        String tempPrefixRomaji = prefixRomaji != null && prefixRomaji.equals("") == false ? prefixRomaji : null;

        if (dictionaryEntry.isKanjiExists() == true) {

            if (tempPrefixKana != null) {
                result.append("(").append(getStringWithMark(tempPrefixKana, null, false)).append(") ");
            }

            result.append(getStringWithMark(kanji, null, false)).append(" ");
        }

        if (kanaList != null && kanaList.size() > 0) {
            result.append(getStringWithMark(toString(kanaList, tempPrefixKana), null, false)).append(" - ");
        }

        if (romajiList != null && romajiList.size() > 0) {
            result.append(getStringWithMark(toString(romajiList, tempPrefixRomaji), null, false));
        }

        if (translates != null && translates.size() > 0) {
            result.append("\n\n");
            result.append(getStringWithMark(toString(translates, null), null, false));
        }

        if (info != null && info.equals("") == false) {
            result.append(" - ").append(getStringWithMark(info, null, false));
        }

        return result.toString();
    }

    public static String getWordFullTextWithMark(Context context, FindWordResult.ResultItem resultItem, String findWord, FindWordRequest findWordRequest) {

        // wygenerowanie docelowego html-a
        StringBuffer result = new StringBuffer();

        // sprawdzenie, czy mamy dane w nowym, czy starym formacie
        JMdict.Entry dictionaryEntry2 = resultItem.getEntry();
        DictionaryEntry oldDictionaryEntry = resultItem.getOldDictionaryEntry();

        if (dictionaryEntry2 != null) { // nowy format
            // wygenerowanie wszystkich kombinacji
            List<Dictionary2HelperCommon.KanjiKanaPair> kanjiKanaPairList = Dictionary2HelperCommon.getKanjiKanaPairListStatic(dictionaryEntry2, true);

            for (int kanjiKanaPairIdx = 0; kanjiKanaPairIdx < kanjiKanaPairList.size(); ++kanjiKanaPairIdx) {

                if (kanjiKanaPairIdx != 0) {
                    result.append("\n");
                }

                Dictionary2HelperCommon.KanjiKanaPair kanjiKanaPair = kanjiKanaPairList.get(kanjiKanaPairIdx);

                // pobieramy wszystkie skladniki slowa
                String kanji = kanjiKanaPair.getKanji();
                String kana = kanjiKanaPair.getKana();
                String romaji = kanjiKanaPair.getRomaji();

                if (kanji != null) {
                    result.append(getStringWithMark(kanji, findWord, findWordRequest.searchKanji)).append(" - ");
                }

                result.append(getStringWithMark(kana, findWord, findWordRequest.searchKana)).append(" - ");
                result.append(getStringWithMark(romaji, findWord, findWordRequest.searchRomaji));
            }

            // dodanie znaczen
            // FM_FIXME: do dokonczenia
            for (int senseIdx = 0; senseIdx < dictionaryEntry2.getSenseList().size(); ++senseIdx) {
                Sense sense = dictionaryEntry2.getSenseList().get(senseIdx);

                if (senseIdx == 0) {
                    result.append("\n\n");
                }

                // ograniczone do kanji/kana
                if (sense.getRestrictedToKanjiList().size() > 0 || sense.getRestrictedToKanaList().size() > 0) {
                    List<String> restrictedToKanjiKanaList = new ArrayList<>();

                    restrictedToKanjiKanaList.addAll(sense.getRestrictedToKanjiList());
                    restrictedToKanjiKanaList.addAll(sense.getRestrictedToKanaList());

                    // zamiana na przetlumaczona postac
                    String restrictedToKanjiKanaString = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "・" + context.getString(R.string.word_dictionary_search_restrictedKanjiKanaForOnly) + " " + String.join("; ", restrictedToKanjiKanaList);

                    result.append(restrictedToKanjiKanaString).append("\n");
                }

                // czesci mowy
                if (sense.getPartOfSpeechList().size() > 0) {
                    // zamiana na przetlumaczona postac
                    String translatedToPolishPartOfSpeechEnum = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "・" + String.join("; ", Dictionary2HelperCommon.translateToPolishPartOfSpeechEnum(sense.getPartOfSpeechList()));

                    result.append(translatedToPolishPartOfSpeechEnum).append("\n");
                }

                // kategoria slowa
                if (sense.getFieldList().size() > 0) {
                    // zamiana na przetlumaczona postac
                    String translatedfieldEnum = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "・" + String.join("; ", Dictionary2HelperCommon.translateToPolishFieldEnumList(sense.getFieldList()));

                    result.append(translatedfieldEnum).append("\n");
                }

                // roznosci
                if (sense.getMiscList().size() > 0) {
                    // zamiana na przetlumaczona postac
                    String translatedMiscEnum = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "・" + String.join("; ", Dictionary2HelperCommon.translateToPolishMiscEnumList(sense.getMiscList()));

                    result.append(translatedMiscEnum).append("\n");
                }

                // znaczenie
                List<Gloss> polishGlossList = Dictionary2HelperCommon.getPolishGlossList(sense.getGlossList());
                SenseAdditionalInfo polishAdditionalInfo = Dictionary2HelperCommon.findFirstPolishAdditionalInfo(sense.getAdditionalInfoList());

                //

                for (int currentGlossIdx = 0; currentGlossIdx < polishGlossList.size(); ++currentGlossIdx) {

                    Gloss gloss = polishGlossList.get(currentGlossIdx);

                    result.append(getStringWithMark(
                            gloss.getValue(), findWord, findWordRequest.searchTranslate) +
                            (gloss.getGType() != null ? " (" + Dictionary2HelperCommon.translateToPolishGlossType(gloss.getGType()) + ")" : "") +
                            (currentGlossIdx != polishGlossList.size() - 1 ? "\n" : ""));
                }

                // informacje dodatkowe
                if (polishAdditionalInfo != null) {
                    result.append("\n");
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getStringWithMark(polishAdditionalInfo.getValue(), findWord, findWordRequest.searchInfo));
                }

                // przerwa
                if (senseIdx != dictionaryEntry2.getSenseList().size() - 1) {
                    result.append("\n\n");
                }
            }



            // FM_FIXME: do dokonczenia
            // FM_FIXME: do naprawy

            /*
            FM_FIXME: z wersji web
            for (int senseIdx = 0; senseIdx < entry.getSenseList().size(); ++senseIdx) {

                Sense sense = entry.getSenseList().get(senseIdx);

                if (addSenseNumber == true) {
                    // numer znaczenia
                    Div senseNoDiv = new Div("col-md-1");

                    H senseNoDivH = new H(4, null, "margin-top: 20px; text-align: right");

                    senseNoDivH.addHtmlElement(new Text("" + (senseIdx + 1)));
                    senseNoDiv.addHtmlElement(senseNoDivH);

                    translateTd.addHtmlElement(senseNoDiv);
                }

                Div singleSenseDiv = new Div("col-md-11");
                translateTd.addHtmlElement(singleSenseDiv);

                // dialekt
                if (sense.getDialectList().size() > 0) {
                    Div dialectDiv = new Div(null, "font-size: 75%; margin-top: 3px; text-align: justify");

                    // zamiana na przetlumaczona postac
                    String translatedDialectEnum = "・" + String.join("; ", Dictionary2HelperCommon.translateToPolishDialectEnumList(sense.getDialectList()));

                    dialectDiv.addHtmlElement(new Text(translatedDialectEnum + "<br/>"));

                    singleSenseDiv.addHtmlElement(dialectDiv);
                }

                // zagraniczne pochodzenie slowa
                if (sense.getLanguageSourceList().size() > 0) {
                    Div languageSourceDiv = new Div(null, "font-size: 75%; margin-top: 3px; text-align: justify");

                    // zamiana na przetlumaczona postac
                    List<String> singleLanguageSourceList = new ArrayList<>();

                    for (LanguageSource languageSource : sense.getLanguageSourceList()) {

                        StringBuffer singleLanguageSource = new StringBuffer();

                        String languageCodeInPolish = Dictionary2HelperCommon.translateToPolishLanguageCode(languageSource.getLang());
                        String languageValue = languageSource.getValue();
                        String languageLsWasei = Dictionary2HelperCommon.translateToPolishLanguageSourceLsWaseiEnum(languageSource.getLsWasei());

                        if (languageValue != null) {
                            singleLanguageSource.append(languageCodeInPolish + ": " + languageValue);

                        } else {
                            singleLanguageSource.append(Dictionary2HelperCommon.translateToPolishLanguageCodeWithoutValue(languageSource.getLang()));
                        }

                        if (languageLsWasei != null) {
                            singleLanguageSource.append(", ").append(languageLsWasei);
                        }

                        singleLanguageSourceList.add(singleLanguageSource.toString());
                    }

                    String joinedLanguageSource = "・" + String.join("; ", singleLanguageSourceList);

                    languageSourceDiv.addHtmlElement(new Text(joinedLanguageSource + "<br/>"));

                    singleSenseDiv.addHtmlElement(languageSourceDiv);
                }

                // odnosnic do innego slowa
                if (sense.getReferenceToAnotherKanjiKanaList().size() > 0) {
                    createReferenceAntonymToAnotherKanjiKanaDiv(messageSource, servletContextPath, singleSenseDiv, sense.getReferenceToAnotherKanjiKanaList(), "wordDictionary.page.search.table.column.details.referenceToAnotherKanjiKana");
                }

                // odnosnic do przeciwienstwa
                if (sense.getAntonymList().size() > 0) {
                    createReferenceAntonymToAnotherKanjiKanaDiv(messageSource, servletContextPath, singleSenseDiv, sense.getAntonymList(), "wordDictionary.page.search.table.column.details.referewnceToAntonymKanjiKana");
                }

                // przerwa
                if (senseIdx != entry.getSenseList().size() - 1) {
                    Div marginDiv = new Div(null, "margin-bottom: 17px;");

                    singleSenseDiv.addHtmlElement(marginDiv);
                }
                */

            } else if (oldDictionaryEntry != null) { // stary format

            // pobieramy wszystkie skladniki slowa
            String kanji = oldDictionaryEntry.getKanji();
            // String prefixKana = oldDictionaryEntry.getPrefixKana();
            String kana = oldDictionaryEntry.getKana();
            // String prefixRomaji = oldDictionaryEntry.getPrefixRomaji();
            String romaji = oldDictionaryEntry.getRomaji();
            List<String> translates = oldDictionaryEntry.getTranslates();
            String info = oldDictionaryEntry.getInfo();

            // String tempPrefixKana = prefixKana != null && prefixKana.equals("") == false ? prefixKana : null;
            // String tempPrefixRomaji = prefixRomaji != null && prefixRomaji.equals("") == false ? prefixRomaji : null;

            if (kanji != null && kanji.equals("") == false && kanji.equals("-") == false) {
                result.append(getStringWithMark(kanji, findWord, findWordRequest.searchKanji)).append(" - ");
            }

            result.append(getStringWithMark(kana, findWord, findWordRequest.searchKana)).append(" - ");
            result.append(getStringWithMark(romaji, findWord, findWordRequest.searchRomaji));

            if (translates != null && translates.size() > 0) {
                result.append("\n\n");

                for (int idx = 0; idx < translates.size(); ++idx) {
                    String currentTranslate = translates.get(idx);

                    result.append(getStringWithMark(currentTranslate, findWord, findWordRequest.searchTranslate));

                    if (idx != translates.size() - 1) {
                        result.append("\n");
                    }
                }
            }

            if (info != null && info.equals("") == false) {
                result.append("\n");
                result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getStringWithMark(info, findWord, findWordRequest.searchInfo));
            }

        } else {
            throw new RuntimeException(); // to nigdy nie powinno zdarzyc sie
        }

        return result.toString().replaceAll("\n", "<br/>");
    }

    public static String getKanjiFullTextWithMark(KanjiCharacterInfo kanjiEntry) {
        return getKanjiFullTextWithMark(kanjiEntry, null);
    }

    public static String getKanjiFullTextWithMark(KanjiCharacterInfo kanjiEntry, String findWord) {

        String kanji = kanjiEntry.getKanji();
        List<String> polishTranslates = Utils.getPolishTranslates(kanjiEntry);

        String info = Utils.getPolishAdditionalInfo(kanjiEntry);

        StringBuffer result = new StringBuffer();

        result.append("<big>").append(getStringWithMark(kanji, findWord, true)).append("</big> - ");
        result.append(getStringWithMark(toString(polishTranslates, null), findWord, true));

        if (info != null && info.equals("") == false) {

            result.append("\n");

            result.append(getStringWithMark(info, findWord, true));
        }

        result.append("\n");

        return result.toString();
    }

    public static String getKanjiRadicalTextWithMark(KanjiCharacterInfo kanjiEntry) {
        return getKanjiRadicalTextWithMark(kanjiEntry, null);
    }

    public static String getKanjiRadicalTextWithMark(KanjiCharacterInfo kanjiEntry, String findWord) {

        Misc2Info misc2Info = kanjiEntry.getMisc2();

        List<String> radicals = null;

        if (misc2Info != null) {
            radicals = misc2Info.getRadicals();

            if (radicals != null && radicals.size() == 0) {
                radicals = null;
            }
        }

        StringBuffer result = new StringBuffer();

        if (radicals != null) {
            result.append(getStringWithMark(toString(radicals, null), findWord, true));
        }

        return result.toString();
    }


    private static String getStringWithMark(String text, String findWord, boolean mark) {

        if (mark == false) {
            return text;
        }

        if (findWord == null) {
            return text;
        }

        String findWordLowerCase = findWord.toLowerCase(Locale.getDefault());

        StringBuffer texStringBuffer = new StringBuffer(text);
        StringBuffer textLowerCaseStringBuffer = new StringBuffer(text.toLowerCase(Locale.getDefault()));

        int idxStart = 0;

        final String fontBegin = "<font color='red'>";
        final String fontEnd = "</font>";

        while(true) {

            int idx1 = textLowerCaseStringBuffer.indexOf(findWordLowerCase, idxStart);

            if (idx1 == -1) {
                break;
            }

            texStringBuffer.insert(idx1, fontBegin);
            textLowerCaseStringBuffer.insert(idx1, fontBegin);

            texStringBuffer.insert(idx1 + findWordLowerCase.length() + fontBegin.length(), fontEnd);
            textLowerCaseStringBuffer.insert(idx1 + findWordLowerCase.length() + fontBegin.length(), fontEnd);

            idxStart = idx1 + findWordLowerCase.length() + fontBegin.length() + fontEnd.length();
        }

        return texStringBuffer.toString();
    }

    private static String toString(List<String> listString, String prefix) {

        StringBuffer sb = new StringBuffer();

        sb.append("[");

        for (int idx = 0; idx < listString.size(); ++idx) {
            if (prefix != null) {
                sb.append("(").append(prefix).append(") ");
            }

            sb.append(listString.get(idx));

            if (idx != listString.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");

        return sb.toString();
    }

    public static Integer getStrokeNumber(KanjiCharacterInfo kanjiEntry, Integer defaultValue) {
        List<Integer> strokeCountList = kanjiEntry.getMisc().getStrokeCountList();

        if (strokeCountList.size() > 0) {
            return strokeCountList.get(0);
        } else {
            return defaultValue;
        }
    }

    public static KanjivgEntry createKanjivgEntry(KanjiCharacterInfo kanjiEntry) {
        KanjivgEntry kanjivgEntry = new KanjivgEntry();

        kanjivgEntry.setKanji(kanjiEntry.getKanji());
        kanjivgEntry.setStrokePaths(kanjiEntry.getMisc2().getStrokePaths());

        return kanjivgEntry;
    }
}
