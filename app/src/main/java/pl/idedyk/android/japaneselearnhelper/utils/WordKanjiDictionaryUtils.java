package pl.idedyk.android.japaneselearnhelper.utils;

import android.util.Log;

import java.util.List;
import java.util.Locale;

import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiDic2Entry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.JMdict;

public class WordKanjiDictionaryUtils {

    public static String getWordFullTextWithMark(DictionaryEntry dictionaryEntry) {

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

    public static String getWordFullTextWithMark(FindWordResult.ResultItem resultItem, JMdict.Entry dictionaryEntry2, String findWord, FindWordRequest findWordRequest) {

        String kanji = resultItem.getKanji();
        String prefixKana = resultItem.getPrefixKana();
        List<String> kanaList = resultItem.getKanaList();
        String prefixRomaji = resultItem.getPrefixRomaji();
        List<String> romajiList = resultItem.getRomajiList();
        List<String> translates = resultItem.getTranslates();
        String info = resultItem.getInfo();

        StringBuffer result = new StringBuffer();

        String tempPrefixKana = prefixKana != null && prefixKana.equals("") == false ? prefixKana : null;
        String tempPrefixRomaji = prefixRomaji != null && prefixRomaji.equals("") == false ? prefixRomaji : null;

        if (resultItem.isKanjiExists() == true) {

            if (tempPrefixKana != null) {
                result.append("(").append(getStringWithMark(tempPrefixKana, findWord, false)).append(") ");
            }

            result.append(getStringWithMark(kanji, findWord, findWordRequest.searchKanji)).append(" ");
        }

        if (kanaList != null && kanaList.size() > 0) {
            result.append(getStringWithMark(toString(kanaList, tempPrefixKana), findWord, findWordRequest.searchKana)).append(" - ");
        }

        if (romajiList != null && romajiList.size() > 0) {
            result.append(getStringWithMark(toString(romajiList, tempPrefixRomaji), findWord, findWordRequest.searchRomaji));
        }

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

        int fixme = 1; // !!!!!!!!!!!!!!!!!!!!!

        //

        return result.toString().replaceAll("\n", "<br/>");
    }

    public static String getKanjiFullTextWithMark(KanjiEntry kanjiEntry) {
        return getKanjiFullTextWithMark(kanjiEntry, null);
    }

    public static String getKanjiFullTextWithMark(KanjiEntry kanjiEntry, String findWord) {

        String kanji = kanjiEntry.getKanji();
        List<String> polishTranslates = kanjiEntry.getPolishTranslates();

        String info = kanjiEntry.getInfo();

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

    public static String getKanjiRadicalTextWithMark(KanjiEntry kanjiEntry) {
        return getKanjiRadicalTextWithMark(kanjiEntry, null);
    }

    public static String getKanjiRadicalTextWithMark(KanjiEntry kanjiEntry, String findWord) {

        KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();

        List<String> radicals = null;

        if (kanjiDic2Entry != null) {
            radicals = kanjiDic2Entry.getRadicals();

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
}
