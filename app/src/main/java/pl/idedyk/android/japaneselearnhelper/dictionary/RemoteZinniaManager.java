package pl.idedyk.android.japaneselearnhelper.dictionary;

import android.content.pm.PackageInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerRequest;
import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerResultItem;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

public class RemoteZinniaManager extends ZinniaManagerCommon {

    private PackageInfo packageInfo = null;

    private Gson gson = new Gson();

    private ServerClient serverClient = new ServerClient();

    public RemoteZinniaManager(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    @Override
    public IZinnaManagerCharacter createNewCharacter() {
        return new Character();
    }

    public class Character implements IZinnaManagerCharacter {

        private KanjiRecognizerRequest kanjiRecognizerRequest;

        private Character() {
            kanjiRecognizerRequest = new KanjiRecognizerRequest();
        }

        @Override
        public void clear() {
            kanjiRecognizerRequest.getStrokes().clear();
        }

        @Override
        public void destroy() {
            // noop
        }

        @Override
        public void setWidth(int width) {
            kanjiRecognizerRequest.setWidth(width);
        }

        @Override
        public void setHeight(int width) {
            kanjiRecognizerRequest.setHeight(width);
        }

        @Override
        public void add(int strokeNo, int x, int y) {

            int currentStrokesCount = kanjiRecognizerRequest.getStrokes().size();

            if (strokeNo + 1 > currentStrokesCount) {

                for (int idx = currentStrokesCount; idx < strokeNo + 1; ++idx) {
                    kanjiRecognizerRequest.newStroke();
                }
            }

            kanjiRecognizerRequest.getStrokes().get(strokeNo).add(new KanjiRecognizerRequest.Point(x, y));
        }

        @Override
        public List<KanjiRecognizerResultItem> recognize(int limit) throws DictionaryException {

            return ServerClient.callInServerThread(new Callable<Object>() {

                @Override
                public Object call() throws Exception {

                    String requestJson = gson.toJson(kanjiRecognizerRequest);

                    String responseJson = null;

                    List<String> result = null;

                    try {
                        responseJson = serverClient.callRemoteDictionaryConnectorMethod(packageInfo, "kanjiRecognize", requestJson);

                        result = gson.fromJson((String) responseJson, new TypeToken<List<KanjiRecognizerResultItem>>(){}.getType());

                    } catch (Exception e) {
                        return e;
                    }

                    if (result == null) {
                        return new DictionaryException("");
                    }

                    return result;
                }
            }, List.class);
        }
    }
}
