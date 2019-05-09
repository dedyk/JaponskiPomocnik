package pl.idedyk.android.japaneselearnhelper.common.queue.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPlaceSearch;

public class WordDictionaryMissingWordEvent extends QueueEventCommon {

    private String word;

    private WordPlaceSearch wordPlaceSearch;

    public WordDictionaryMissingWordEvent(String word, WordPlaceSearch wordPlaceSearch) {

        super();

        this.word = word;
        this.wordPlaceSearch = wordPlaceSearch;
    }

    public WordDictionaryMissingWordEvent(String uuid, Date createDate, String params) {

        super(uuid, createDate);

        Map<String, String> paramsMap = getParamsFromString(params);

        this.word = paramsMap.get("word");
        this.wordPlaceSearch = WordPlaceSearch.valueOf(paramsMap.get("wordPlaceSearch"));
    }

    @Override
    public QueueEventOperation getQueryEventOperation() {
        return QueueEventOperation.WORD_DICTIONARY_MISSING_WORD_EVENT;
    }

    @Override
    public Map<String, String> getParams() {

        Map<String, String> result = new HashMap<>();

        result.put("word", word);
        result.put("wordPlaceSearch", wordPlaceSearch.toString());

        return result;
    }

    public String getWord() {
        return word;
    }

    public WordPlaceSearch getWordPlaceSearch() {
        return wordPlaceSearch;
    }
}
