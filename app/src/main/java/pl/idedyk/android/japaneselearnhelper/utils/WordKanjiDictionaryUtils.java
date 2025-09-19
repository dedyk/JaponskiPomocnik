package pl.idedyk.android.japaneselearnhelper.utils;

import android.util.Log;

import java.util.List;
import java.util.Locale;

import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary2.api.helper.Dictionary2HelperCommon;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.JMdict;
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

    public static String getWordFullTextWithMark(FindWordResult.ResultItem resultItem, String findWord, FindWordRequest findWordRequest) {

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

            result.append("\n\n");

            // FM_FIXME: dokonczyc, testy !!!!

            // FM_FIXME: z wersji web
            /*
            	private Div createWordColumn(FindWordRequest findWordRequest, String findWord, String kanji, String kana, String romaji) {
                Div singleWordDiv = new Div(null, "display: flex; width: 100%; ");

                // kanji
                Span singleWordKanjiSpan = new Span(null, "width: 33%; padding: 5px 15px 10px 0px; overflow-wrap: break-word;");

                if (kanji != null) {
                    singleWordKanjiSpan.addHtmlElement(new Text(WordDictionary2SenseUtils.getStringWithMark(kanji, findWord, findWordRequest.searchKanji)));
                }

                // kana
                Span singleWordKanaSpan = new Span(null, "width: 33%; padding: 5px 15px 10px 0px; overflow-wrap: break-word;");
                singleWordKanaSpan.addHtmlElement(new Text(WordDictionary2SenseUtils.getStringWithMark(kana, findWord, findWordRequest.searchKana)));

                // romaji
                Span singleWordRomajiSpan = new Span(null, "width: 33%; padding: 5px 0px 10px 0px; overflow-wrap: break-word;");
                singleWordRomajiSpan.addHtmlElement(new Text(WordDictionary2SenseUtils.getStringWithMark(romaji, findWord, findWordRequest.searchRomaji)));

                // dodanie elementow
                singleWordDiv.addHtmlElement(singleWordKanjiSpan);
                singleWordDiv.addHtmlElement(singleWordKanaSpan);
                singleWordDiv.addHtmlElement(singleWordRomajiSpan);

                return singleWordDiv;
            }

             */

            // FM_FIXME: do naprawy
            /*
            String tempPrefixKana = prefixKana != null && prefixKana.equals("") == false ? prefixKana : null;
            String tempPrefixRomaji = prefixRomaji != null && prefixRomaji.equals("") == false ? prefixRomaji : null;

             */




        } else if (oldDictionaryEntry != null) { // stary format
            // FM_FIXME: do naprawy
            return "FM_FIXME: oldDictionaryEntry";

        } else {
            throw new RuntimeException(); // to nigdy nie powinno zdarzyc sie
        }




        ////////


        // FM_FIXME: do naprawy
        /*
        String kanji = resultItem.getKanji();
        String prefixKana = resultItem.getPrefixKana();
        List<String> kanaList = resultItem.getKanaList();
        String prefixRomaji = resultItem.getPrefixRomaji();
        List<String> romajiList = resultItem.getRomajiList();
        List<String> translates = resultItem.getTranslates();
        String info = resultItem.getInfo();
        */



        if (dictionaryEntry2 == null) { // dane w starym formacie

            // FM_FIXME: do naprawy
            /*
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
             */

        } else { // dane w nowym formacie

            // FM_FIXME: do naprawy
            /*
            List<Dictionary2HelperCommon.KanjiKanaPair> kanjiKanaPairList = Dictionary2HelperCommon.getKanjiKanaPairListStatic(dictionaryEntry2);

            // szukamy konkretnego znaczenia dla naszego slowa
            Dictionary2HelperCommon.KanjiKanaPair dictionaryEntry2KanjiKanaPair = Dictionary2HelperCommon.findKanjiKanaPair(kanjiKanaPairList, resultItem.getDictionaryEntry());

            Dictionary2HelperCommon.PrintableSense printableSense = Dictionary2HelperCommon.getPrintableSense(dictionaryEntry2KanjiKanaPair);

            // mamy znaczenia
            for (int senseIdx = 0; senseIdx < printableSense.getSenseEntryList().size(); ++senseIdx) {

                if (senseIdx == 0) {
                    result.append("\n\n");
                }

                Dictionary2HelperCommon.PrintableSenseEntry printableSenseEntry = printableSense.getSenseEntryList().get(senseIdx);

                for (int currentGlossIdx = 0; currentGlossIdx < printableSenseEntry.getGlossList().size(); ++currentGlossIdx) {

                    Dictionary2HelperCommon.PrintableSenseEntryGloss printableSenseEntryGloss = printableSenseEntry.getGlossList().get(currentGlossIdx);

                    result.append(getStringWithMark(
                            printableSenseEntryGloss.getGlossValue(), findWord, findWordRequest.searchTranslate) +
                            (printableSenseEntryGloss.getGlossValueGType() != null ? " (" + printableSenseEntryGloss.getGlossValueGType() + ")" : "") +
                            (currentGlossIdx != printableSenseEntry.getGlossList().size() - 1 ? "\n" : ""));
                }

                // informacje dodatkowe
                if (printableSenseEntry.getAdditionalInfoValue() != null) {
                    result.append("\n");
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getStringWithMark(printableSenseEntry.getAdditionalInfoValue(), findWord, findWordRequest.searchInfo));
                }

                // przerwa
                if (senseIdx != printableSense.getSenseEntryList().size() - 1) {
                    result.append("\n\n");
                }
            }
            */
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
