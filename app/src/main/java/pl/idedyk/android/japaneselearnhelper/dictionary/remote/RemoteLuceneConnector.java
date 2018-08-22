package pl.idedyk.android.japaneselearnhelper.dictionary.remote;

import com.google.gson.Gson;

import java.util.List;
import java.util.Set;

import pl.idedyk.japanese.dictionary.api.dictionary.IDatabaseConnector;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiResult;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.GroupWithTatoebaSentenceList;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

public class RemoteLuceneConnector implements IDatabaseConnector {

    private Gson gson = new Gson();

    @Override
    public int getDictionaryEntriesSize() {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return 0;
    }

    @Override
    public int getDictionaryEntriesNameSize() {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return 0;
    }

    @Override
    public DictionaryEntry getNthDictionaryEntry(int i) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public FindWordResult findDictionaryEntries(FindWordRequest findWordRequest) throws DictionaryException {

        String json = gson.toJson(findWordRequest);


        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public void findDictionaryEntriesInGrammaFormAndExamples(FindWordRequest findWordRequest, FindWordResult findWordResult) throws DictionaryException {
        // FIXME !!!!!!!!!!!!!!!!!!!
    }

    @Override
    public DictionaryEntry getDictionaryEntryById(String s) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public DictionaryEntry getDictionaryEntryNameById(String s) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public KanjiEntry getKanjiEntryById(String s) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public KanjiEntry getKanjiEntry(String s) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public List<KanjiEntry> getAllKanjis(boolean b, boolean b1) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public List<KanjiEntry> findKanjiFromRadicals(String[] strings) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public FindKanjiResult findKanjisFromStrokeCount(int i, int i1) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public Set<String> findAllAvailableRadicals(String[] strings) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public FindKanjiResult findKanji(FindKanjiRequest findKanjiRequest) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public List<GroupEnum> getDictionaryEntryGroupTypes() {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public List<DictionaryEntry> getGroupDictionaryEntries(GroupEnum groupEnum) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public GroupWithTatoebaSentenceList getTatoebaSentenceGroup(String s) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

        return null;
    }

    @Override
    public void findDictionaryEntriesInNames(FindWordRequest findWordRequest, FindWordResult findWordResult) throws DictionaryException {

        // FIXME !!!!!!!!!!!!!!!!!!!

    }
}
