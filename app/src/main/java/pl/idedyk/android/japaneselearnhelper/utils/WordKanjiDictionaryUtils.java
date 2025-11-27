package pl.idedyk.android.japaneselearnhelper.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary2.api.helper.Dictionary2HelperCommon;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.Gloss;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.JMdict;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.LanguageSource;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.Sense;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.SenseAdditionalInfo;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.Misc2Info;

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

    public static String getWordFullTextWithMark(Context context, FindWordResult.ResultItem resultItem, String findWord, FindWordRequest findWordRequest) {

        // wygenerowanie docelowego html-a
        StringBuffer result = new StringBuffer();

        // sprawdzenie, czy mamy dane w nowym, czy starym formacie
        JMdict.Entry dictionaryEntry2 = resultItem.getEntry();
        DictionaryEntry dictionaryEntry = resultItem.getDictionaryEntry();

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
                    result.append("<big>" + getStringWithMark(kanji, findWord, findWordRequest.searchKanji) + "</big>").append(" - ");
                }

                result.append("<big>" + getStringWithMark(kana, findWord, findWordRequest.searchKana) + "</big>").append(" - ");
                result.append("<big>" + getStringWithMark(romaji, findWord, findWordRequest.searchRomaji) + "</big>");
            }

            for (int senseIdx = 0; senseIdx < dictionaryEntry2.getSenseList().size(); ++senseIdx) {
                Sense sense = dictionaryEntry2.getSenseList().get(senseIdx);
                PrintableDictionaryEntry2Sense printableDictionaryEntry2Sense = new PrintableDictionaryEntry2Sense(sense);

                if (senseIdx == 0) {
                    result.append("\n\n");
                }

                // znaczenie
                List<Gloss> polishGlossList = printableDictionaryEntry2Sense.getPolishGlossList();
                SenseAdditionalInfo polishAdditionalInfo = printableDictionaryEntry2Sense.getPolishAdditionalInfo();

                //

                for (int currentGlossIdx = 0; currentGlossIdx < polishGlossList.size(); ++currentGlossIdx) {

                    Gloss gloss = polishGlossList.get(currentGlossIdx);

                    result.append("<big><strong>" + getStringWithMark(
                            gloss.getValue(), findWord, findWordRequest.searchTranslate) +
                            (gloss.getGType() != null ? " (" + Dictionary2HelperCommon.translateToPolishGlossType(gloss.getGType()) + ")" : "") +
                            (currentGlossIdx != polishGlossList.size() - 1 ? "\n" : "") + "</strong></big>");
                }

                // informacje dodatkowe
                if (polishAdditionalInfo != null) {
                    result.append("\n");
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getStringWithMark(polishAdditionalInfo.getValue(), findWord, findWordRequest.searchInfo));
                }

                // dynamiczna przerwa
                Consumer<Void> onetimeSpacerGenerator = new Consumer<Void>() {
                    private boolean generatedSpacer = false;

                    @Override
                    public void accept(Void o) {
                        if (generatedSpacer == true) {
                            return;
                        }

                        generatedSpacer = true;

                        if (polishAdditionalInfo == null) {
                            result.append("\n");
                            return;
                        }

                        result.append("\n<small><br/></small>");
                    }
                };

                // ograniczone do kanji/kana
                String restrictedToKanjiKanaString = printableDictionaryEntry2Sense.getRestrictedToKanjiKanaString(context);

                if (restrictedToKanjiKanaString != null) {
                    onetimeSpacerGenerator.accept(null);
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>" + restrictedToKanjiKanaString + "</i>").append("\n");
                }

                // czesci mowy
                String translatedToPolishPartOfSpeechEnum = printableDictionaryEntry2Sense.getTranslatedToPolishPartOfSpeechEnum(context);

                if (translatedToPolishPartOfSpeechEnum != null) {
                    onetimeSpacerGenerator.accept(null);
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>" + translatedToPolishPartOfSpeechEnum + "</i>").append("\n");
                }

                // kategoria slowa
                String translatedFieldEnum = printableDictionaryEntry2Sense.getTranslatedFieldEnum(context);

                if (translatedFieldEnum != null) {
                    onetimeSpacerGenerator.accept(null);
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>" + translatedFieldEnum + "</i>").append("\n");
                }

                // roznosci
                String translatedMiscEnum = printableDictionaryEntry2Sense.getTranslatedMiscEnum(context);

                if (translatedMiscEnum != null) {
                    onetimeSpacerGenerator.accept(null);
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>" + translatedMiscEnum + "</i>").append("\n");
                }

                // dialekt
                String translatedDialectEnum = printableDictionaryEntry2Sense.getTranslatedDialectEnum(context);

                if (translatedDialectEnum != null) {
                    onetimeSpacerGenerator.accept(null);
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>" + translatedDialectEnum + "</i>").append("\n");
                }

                // zagraniczne pochodzenie slowa
                String joinedLanguageSource = printableDictionaryEntry2Sense.getJoinedLanguageSource(context);

                if (joinedLanguageSource != null) {
                    onetimeSpacerGenerator.accept(null);
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>" + joinedLanguageSource + "</i>").append("\n");
                }

                // odnosnic do innego slowa
                String referenceToAnotherKanjiKana = printableDictionaryEntry2Sense.getReferenceToAnotherKanjiKana(context);

                if (referenceToAnotherKanjiKana != null) {
                    onetimeSpacerGenerator.accept(null);
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>" + referenceToAnotherKanjiKana + "</i>").append("\n");
                }

                // odnosnic do przeciwienstwa
                String antonym = printableDictionaryEntry2Sense.getAntonym(context);

                if (antonym != null) {
                    onetimeSpacerGenerator.accept(null);
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>" + antonym + "</i>").append("\n");
                }

                // przerwa
                if (senseIdx != dictionaryEntry2.getSenseList().size() - 1) {
                    result.append("\n");
                }
            }

        } else if (dictionaryEntry != null) { // stary format

            // pobieramy wszystkie skladniki slowa
            String kanji = dictionaryEntry.getKanji();
            // String prefixKana = oldDictionaryEntry.getPrefixKana();
            String kana = dictionaryEntry.getKana();
            // String prefixRomaji = oldDictionaryEntry.getPrefixRomaji();
            String romaji = dictionaryEntry.getRomaji();
            List<String> translates = dictionaryEntry.getTranslates();
            String info = dictionaryEntry.getInfo();

            // String tempPrefixKana = prefixKana != null && prefixKana.equals("") == false ? prefixKana : null;
            // String tempPrefixRomaji = prefixRomaji != null && prefixRomaji.equals("") == false ? prefixRomaji : null;

            if (kanji != null && kanji.equals("") == false && kanji.equals("-") == false) {
                result.append("<big>" + getStringWithMark(kanji, findWord, findWordRequest.searchKanji) + "</big>").append(" - ");
            }

            result.append("<big>" + getStringWithMark(kana, findWord, findWordRequest.searchKana) + "</big>").append(" - ");
            result.append("<big>" + getStringWithMark(romaji, findWord, findWordRequest.searchRomaji) + "</big>");

            if (translates != null && translates.size() > 0) {
                result.append("\n\n");

                for (int idx = 0; idx < translates.size(); ++idx) {
                    String currentTranslate = translates.get(idx);

                    result.append("<big><strong>" + getStringWithMark(currentTranslate, findWord, findWordRequest.searchTranslate) + "</strong></big>");

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

    public static class PrintableDictionaryEntry2Sense {
        private Sense sense;

        public PrintableDictionaryEntry2Sense(Sense sense) {
            this.sense = sense;
        }

        public String getRestrictedToKanjiKanaString(Context context) {
            String restrictedToKanjiKanaString = null;

            if (sense.getRestrictedToKanjiList().size() > 0 || sense.getRestrictedToKanaList().size() > 0) {
                List<String> restrictedToKanjiKanaList = new ArrayList<>();

                restrictedToKanjiKanaList.addAll(sense.getRestrictedToKanjiList());
                restrictedToKanjiKanaList.addAll(sense.getRestrictedToKanaList());

                // zamiana na przetlumaczona postac
                restrictedToKanjiKanaString = context.getString(R.string.word_dictionary_search_restrictedKanjiKanaForOnly) + " " + join("; ", restrictedToKanjiKanaList);
            }

            return restrictedToKanjiKanaString;
        }

        public String getTranslatedToPolishPartOfSpeechEnum(Context context) {
            String translatedToPolishPartOfSpeechEnum = null;

            if (sense.getPartOfSpeechList().size() > 0) {
                // zamiana na przetlumaczona postac
                translatedToPolishPartOfSpeechEnum = join("; ", Dictionary2HelperCommon.translateToPolishPartOfSpeechEnum(sense.getPartOfSpeechList()));
            }

            return translatedToPolishPartOfSpeechEnum;
        }

        public String getTranslatedFieldEnum(Context context) {
            String translatedFieldEnum = null;

            if (sense.getFieldList().size() > 0) {
                // zamiana na przetlumaczona postac
                translatedFieldEnum = join("; ", Dictionary2HelperCommon.translateToPolishFieldEnumList(sense.getFieldList()));
            }

            return translatedFieldEnum;
        }

        public String getTranslatedMiscEnum(Context context) {
            String translatedMiscEnum = null;

            if (sense.getMiscList().size() > 0) {
                // zamiana na przetlumaczona postac
                translatedMiscEnum = join("; ", Dictionary2HelperCommon.translateToPolishMiscEnumList(sense.getMiscList()));
            }

            return translatedMiscEnum;
        }

        public String getTranslatedDialectEnum(Context context) {
            String translatedDialectEnum = null;

            if (sense.getDialectList().size() > 0) {
                // zamiana na przetlumaczona postac
                translatedDialectEnum = join("; ", Dictionary2HelperCommon.translateToPolishDialectEnumList(sense.getDialectList()));
            }

            return translatedDialectEnum;
        }

        public String getJoinedLanguageSource(Context context) {
            String joinedLanguageSource = null;

            if (sense.getLanguageSourceList().size() > 0) {
                // zamiana na przetlumaczona postac
                List<String> singleLanguageSourceList = new ArrayList<>();

                for (LanguageSource languageSource : sense.getLanguageSourceList()) {

                    StringBuffer singleLanguageSource = new StringBuffer();

                    String languageCodeInPolish = Dictionary2HelperCommon.translateToPolishLanguageCode(languageSource.getLang());
                    String languageValue = languageSource.getValue();
                    String languageLsWasei = Dictionary2HelperCommon.translateToPolishLanguageSourceLsWaseiEnum(languageSource.getLsWasei());

                    if (languageValue != null && languageValue.equals("") == false) {
                        singleLanguageSource.append(languageCodeInPolish + ": " + languageValue);

                    } else {
                        singleLanguageSource.append(Dictionary2HelperCommon.translateToPolishLanguageCodeWithoutValue(languageSource.getLang()));
                    }

                    if (languageLsWasei != null && languageLsWasei.equals("") == false) {
                        singleLanguageSource.append(", ").append(languageLsWasei);
                    }

                    singleLanguageSourceList.add(singleLanguageSource.toString());
                }

                joinedLanguageSource = join("; ", singleLanguageSourceList);
            }

            return joinedLanguageSource;
        }

        public String getReferenceToAnotherKanjiKana(Context context) {
            String referenceToAnotherKanjiKana = null;

            if (sense.getReferenceToAnotherKanjiKanaList().size() > 0) {
                referenceToAnotherKanjiKana = createReferenceAntonymToAnotherKanjiKana(context, sense.getReferenceToAnotherKanjiKanaList(), R.string.word_dictionary_search_referenceToAnotherKanjiKana);
            }

            return referenceToAnotherKanjiKana;
        }

        public String getAntonym(Context context) {
            String antonym = null;

            if (sense.getAntonymList().size() > 0) {
                antonym = createReferenceAntonymToAnotherKanjiKana(context, sense.getAntonymList(), R.string.word_dictionary_search_referewnceToAntonymKanjiKana);
            }

            return antonym;
        }

        private static String createReferenceAntonymToAnotherKanjiKana(Context context, List<String> wordReference, int stringIdTitle) {

            List<String> wordsToCreateLinkList = new ArrayList<>();

            for (String referenceToAnotherKanjiKana : wordReference) {
                // wartosc tutaj znajduja sie moze byc w trzech wariantach: kanji, kanji i kana oraz kanji, kana i numer pozycji w tlumaczeniu
                String[] referenceToAnotherKanjiKanaSplited = referenceToAnotherKanjiKana.split("・");

                if (referenceToAnotherKanjiKanaSplited.length == 1 || referenceToAnotherKanjiKanaSplited.length == 2) {
                    wordsToCreateLinkList.add(referenceToAnotherKanjiKanaSplited[0]);

                } else if (referenceToAnotherKanjiKanaSplited.length == 3) {
                    wordsToCreateLinkList.add(referenceToAnotherKanjiKanaSplited[0]);
                    wordsToCreateLinkList.add(referenceToAnotherKanjiKanaSplited[1]);
                }
            }

            StringBuffer result = new StringBuffer();

            if (wordsToCreateLinkList.size() > 0) {
                result.append(context.getString(stringIdTitle) + " ");

                for (int wordsToCreateLinkListIdx = 0; wordsToCreateLinkListIdx < wordsToCreateLinkList.size(); ++wordsToCreateLinkListIdx) {
                    String currentWordsToCreateLink = wordsToCreateLinkList.get(wordsToCreateLinkListIdx);

                    result.append(currentWordsToCreateLink);

                    if (wordsToCreateLinkListIdx != wordsToCreateLinkList.size() - 1) {
                        result.append(", ");
                    }
                }
            }

            return result.toString();
        }

        public List<Gloss> getPolishGlossList() {
            return Dictionary2HelperCommon.getPolishGlossList(sense.getGlossList());
        }

        public SenseAdditionalInfo getPolishAdditionalInfo() {
            return Dictionary2HelperCommon.findFirstPolishAdditionalInfo(sense.getAdditionalInfoList());
        }
    }

    private static String join(CharSequence delimiter, List<String> elements) {
        StringBuffer result = new StringBuffer();

        for (int idx = 0; idx < elements.size(); ++idx) {
            result.append(elements.get(idx));

            if (idx != elements.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString();
    }
}
