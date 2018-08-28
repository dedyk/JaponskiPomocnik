package pl.idedyk.android.japaneselearnhelper.dictionary.remote;

import android.content.pm.PackageInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
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

    private PackageInfo packageInfo = null;

    private Gson gson = new Gson();

    private ServerClient serverClient = new ServerClient();

    public RemoteLuceneConnector(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

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
    public FindWordResult findDictionaryEntries(final FindWordRequest findWordRequest) throws DictionaryException {

        return callInServerThread(new Callable<Object>() {

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
        // FIXME !!!!!!!!!!!!!!!!!!!
    }

    @Override
    public DictionaryEntry getDictionaryEntryById(final String id) throws DictionaryException {

        return callInServerThread(new Callable<Object>() {

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
    public DictionaryEntry getDictionaryEntryNameById(final String id) throws DictionaryException {

        return callInServerThread(new Callable<Object>() {

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
    public KanjiEntry getKanjiEntryById(final String id) throws DictionaryException {

        return callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(id);

                String responseJson = null;

                KanjiEntry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getKanjiEntryById", requestJson);

                    result = gson.fromJson((String) responseJson, KanjiEntry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, KanjiEntry.class);
    }

    @Override
    public KanjiEntry getKanjiEntry(final String kanji) throws DictionaryException {

        return callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(kanji);

                String responseJson = null;

                KanjiEntry result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getKanjiEntry", requestJson);

                    result = gson.fromJson((String) responseJson, KanjiEntry.class);

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, KanjiEntry.class);
    }

    @Override
    public List<KanjiEntry> getAllKanjis(final boolean withDetails, final boolean onlyUsed) throws DictionaryException {

        return callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(new GetAllKanjisWrapper(withDetails, onlyUsed));

                String responseJson = null;

                List<String> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "getAllKanjis", requestJson);

                    result = gson.fromJson((String) responseJson, new TypeToken<List<KanjiEntry>>(){}.getType());

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, List.class);
    }

    @Override
    public List<KanjiEntry> findKanjiFromRadicals(final String[] radicals) throws DictionaryException {

        return callInServerThread(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String requestJson = gson.toJson(radicals);

                String responseJson = null;

                List<String> result = null;

                try {
                    responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "findKanjiFromRadicals", requestJson);

                    result = gson.fromJson((String) responseJson, new TypeToken<List<KanjiEntry>>(){}.getType());

                } catch (Exception e) {
                    return e;
                }

                return result;
            }
        }, List.class);
    }

    @Override
    public FindKanjiResult findKanjisFromStrokeCount(final int from, final int to) throws DictionaryException {

        return callInServerThread(new Callable<Object>() {

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

        return callInServerThread(new Callable<Object>() {

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

        return callInServerThread(new Callable<Object>() {

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

    private <T> T callInServerThread(Callable<Object> callable, Class<T> resultClass) throws DictionaryException {

        ExecutorService executorService = null;

        try {
            executorService = Executors.newFixedThreadPool(1);

            Future<Object> resultFuture = executorService.submit(callable);

            Object resultObject = null;

            try {
                resultObject = resultFuture.get();

            } catch (Exception e) {
                throw new DictionaryException(e);
            }

            if (resultObject == null) {
                return null;

            } else if (resultObject instanceof DictionaryException) {
                throw (DictionaryException) resultObject;

            } else if (resultObject instanceof Exception) {
                throw new DictionaryException((Exception) resultObject);

            } else if (resultClass.isInstance(resultObject) == true) {
                return (T) resultObject;

            } else {
                throw new RuntimeException("Unknown object: " + resultObject);
            }

        } finally {

            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }
}
