package pl.idedyk.android.japaneselearnhelper.dictionary.remote;

import android.content.pm.PackageInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
import pl.idedyk.japanese.dictionary.api.dictionary.IDatabaseConnector;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiResult;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPowerList;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.GroupWithTatoebaSentenceList;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePair;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePairWithDictionaryEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.JMdict;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

public class RemoteLuceneConnector implements IDatabaseConnector {

    private PackageInfo packageInfo = null;

    private Gson gson = new Gson();

    private ServerClient serverClient = new ServerClient();

    public RemoteLuceneConnector(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    @Override
    public int getDictionaryEntriesSize() throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String responseJson = null;

                Integer result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntriesSize", "");

                    result = gson.fromJson(responseJson, Integer.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, Integer.class);
    }

    @Override
    public int getDictionaryEntriesNameSize() throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String responseJson = null;

                Integer result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntriesNameSize", "");

                    result = gson.fromJson(responseJson, Integer.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, Integer.class);
    }

    /*
    @Override
    public DictionaryEntry getNthDictionaryEntry(int i) throws DictionaryException {
        throw new UnsupportedOperationException();
    }
    */

    @Override
    public FindWordResult findDictionaryEntries(final FindWordRequest findWordRequest) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(findWordRequest);

                String responseJson = null;

                FindWordResult result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "findDictionaryEntries", requestJson);

                    result = gson.fromJson(responseJson, FindWordResult.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, FindWordResult.class);
    }

    @Override
    public void findDictionaryEntriesInGrammaFormAndExamples(FindWordRequest findWordRequest, FindWordResult findWordResult) throws DictionaryException {

        if (findWordRequest.searchGrammaFormAndExamples == false) {
            return;
        }

        if (findWordResult.moreElemetsExists == true) {
            return;
        }

        // wyszukiwanie po formach gramatycznych i przykladach odbywa sie z uzyciem metody w ServerClient
        // na razie nie ma potrzeby implementowania tej metody
        throw new UnsupportedOperationException();
    }

    /*
    @Override
    public DictionaryEntry getDictionaryEntryById(final String id) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(id);

                String responseJson = null;

                DictionaryEntry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntryById", requestJson);

                    result = gson.fromJson((String) responseJson, DictionaryEntry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, DictionaryEntry.class);
    }

    @Override
    public DictionaryEntry getDictionaryEntryByUniqueKey(final String uniqueKey) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(uniqueKey);

                String responseJson = null;

                DictionaryEntry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntryByUniqueKey", requestJson);

                    result = gson.fromJson((String) responseJson, DictionaryEntry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, DictionaryEntry.class);
    }
    */

    @Override
    public JMdict.Entry getDictionaryEntry2ById(final Integer id) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(id);

                String responseJson = null;

                JMdict.Entry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntry2ById", requestJson);

                    result = gson.fromJson((String) responseJson, JMdict.Entry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, JMdict.Entry.class);
    }

    @Override
    public JMdict.Entry getDictionaryEntry2ByCounter(int counter) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(counter);

                String responseJson = null;

                JMdict.Entry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntry2ByCounter", requestJson);

                    result = gson.fromJson((String) responseJson, JMdict.Entry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, JMdict.Entry.class);
    }

    @Override
    public JMdict.Entry getDictionaryEntry2ByOldPolishJapaneseDictionaryId(long oldPolishJapaneseDictionaryId) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(oldPolishJapaneseDictionaryId);

                String responseJson = null;

                JMdict.Entry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntry2ByOldPolishJapaneseDictionaryId", requestJson);

                    result = gson.fromJson((String) responseJson, JMdict.Entry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, JMdict.Entry.class);
    }

    @Override
    public JMdict.Entry getDictionaryEntry2ByOldPolishJapaneseDictionaryUniqueKey(String uiqueKey) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(uiqueKey);

                String responseJson = null;

                JMdict.Entry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntry2ByOldPolishJapaneseDictionaryUniqueKey", requestJson);

                    result = gson.fromJson((String) responseJson, JMdict.Entry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, JMdict.Entry.class);
    }

    @Override
    public DictionaryEntry getDictionaryEntryNameById(final String id) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(id);

                String responseJson = null;

                DictionaryEntry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntryNameById", requestJson);

                    result = gson.fromJson((String) responseJson, DictionaryEntry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, DictionaryEntry.class);
    }

    @Override
    public DictionaryEntry getDictionaryEntryNameByUniqueKey(final String uniqueKey) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(uniqueKey);

                String responseJson = null;

                DictionaryEntry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntryNameByUniqueKey", requestJson);

                    result = gson.fromJson((String) responseJson, DictionaryEntry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, DictionaryEntry.class);
    }

    @Override
    public KanjiCharacterInfo getKanjiEntryById(final String id) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(id);

                String responseJson = null;

                KanjiCharacterInfo result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getKanjiEntryById", requestJson);

                    result = gson.fromJson((String) responseJson, KanjiCharacterInfo.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, KanjiCharacterInfo.class);
    }

    @Override
    public KanjiCharacterInfo getKanjiEntry(final String kanji) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(kanji);

                String responseJson = null;

                KanjiCharacterInfo result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getKanjiEntry", requestJson);

                    result = gson.fromJson((String) responseJson, KanjiCharacterInfo.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, KanjiCharacterInfo.class);
    }

    @Override
    public List<KanjiCharacterInfo> getKanjiEntryList(final List<String> kanjiList) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(kanjiList);

                String responseJson = null;

                List<KanjiCharacterInfo> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getKanjiEntryList", requestJson);

                    result = gson.fromJson((String) responseJson, new TypeToken<List<KanjiCharacterInfo>>(){}.getType());

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, List.class);
    }

    @Override
    public List<KanjiCharacterInfo> getAllKanjis(final boolean withDetails, final boolean onlyUsed) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(new GetAllKanjisWrapper(withDetails, onlyUsed));

                String responseJson = null;

                List<String> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getAllKanjis", requestJson);

                    result = gson.fromJson((String) responseJson, new TypeToken<List<KanjiCharacterInfo>>(){}.getType());

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, List.class);
    }

    @Override
    public List<KanjiCharacterInfo> findKanjiFromRadicals(final String[] radicals) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(radicals);

                String responseJson = null;

                List<String> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "findKanjiFromRadicals", requestJson);

                    result = gson.fromJson((String) responseJson, new TypeToken<List<KanjiCharacterInfo>>(){}.getType());

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, List.class);
    }

    @Override
    public FindKanjiResult findKanjisFromStrokeCount(final int from, final int to) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(new FindKanjisFromStrokeCountWrapper(from, to));

                String responseJson = null;

                FindKanjiResult result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "findKanjisFromStrokeCount", requestJson);

                    result = gson.fromJson((String) responseJson, FindKanjiResult.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, FindKanjiResult.class);
    }

    @Override
    public Set<String> findAllAvailableRadicals(final String[] radicals) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(radicals);

                String responseJson = null;

                Set<String> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "findAllAvailableRadicals", requestJson);

                    result = gson.fromJson((String) responseJson, new TypeToken<Set<String>>(){}.getType());

                } catch (Exception e) {
                    return e;
                }

                if (result == null) {
                    return new DictionaryException("");
                }

                return result;
            }
        }, Set.class);
    }

    @Override
    public FindKanjiResult findKanji(final FindKanjiRequest findKanjiRequest) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(findKanjiRequest);

                String responseJson = null;

                FindKanjiResult result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "findKanji", requestJson);

                    result = gson.fromJson(responseJson, FindKanjiResult.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, FindKanjiResult.class);
    }

    @Override
    public List<GroupEnum> getDictionaryEntryGroupTypes() throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String responseJson = null;

                List<GroupEnum> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getDictionaryEntryGroupTypes", "");

                    result = gson.fromJson(responseJson, new TypeToken<List<GroupEnum>>(){}.getType());

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, List.class);
    }

    @Override
    public List<JMdict.Entry> getGroupDictionaryEntry2List(final GroupEnum groupEnum) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(groupEnum);

                String responseJson = null;

                List<DictionaryEntry> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getGroupDictionaryEntry2List", requestJson);

                    result = gson.fromJson(responseJson, new TypeToken<List<JMdict.Entry>>(){}.getType());

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, List.class);
    }

    @Override
    public GroupWithTatoebaSentenceList getTatoebaSentenceGroup(final String groupId) throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(groupId);

                String responseJson = null;

                GroupWithTatoebaSentenceList result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getTatoebaSentenceGroup", requestJson);

                    result = gson.fromJson(responseJson, GroupWithTatoebaSentenceList.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, GroupWithTatoebaSentenceList.class);
    }

    @Override
    public void findDictionaryEntriesInNames(FindWordRequest findWordRequest, FindWordResult findWordResult) throws DictionaryException {

        if (findWordRequest.searchName == false) {
            return;
        }

        if (findWordResult.moreElemetsExists == true) {
            return;
        }

        // wyszukiwanie po formach gramatycznych i przykladach odbywa sie z uzyciem metody w ServerClient
        // na razie nie ma potrzeby implementowania tej metody
        throw new UnsupportedOperationException();
    }

    public List<TransitiveIntransitivePairWithDictionaryEntry> getTransitiveIntransitivePairsList() throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson("");

                String responseJson = null;

                List<TransitiveIntransitivePairWithDictionaryEntry> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getTransitiveIntransitivePairsList", requestJson);

                    result = gson.fromJson(responseJson, new TypeToken<List<TransitiveIntransitivePairWithDictionaryEntry>>() {}.getType());

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, List.class);
    }

    public WordPowerList getWordPowerList() throws DictionaryException {

        return ServerClient.callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson("");

                String responseJson = null;

                WordPowerList result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getWordPowerList", requestJson);

                    result = gson.fromJson(responseJson, WordPowerList.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, WordPowerList.class);
    }
}
