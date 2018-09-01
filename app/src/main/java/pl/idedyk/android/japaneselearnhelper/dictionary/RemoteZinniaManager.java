package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerRequest;
import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerResultItem;

public class RemoteZinniaManager extends ZinniaManagerCommon {

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
        public List<KanjiRecognizerResultItem> recognize(int limit) {

            // FIXME !!!!!!!!!!!!!!

            return new ArrayList<>();
        }
    }
}
