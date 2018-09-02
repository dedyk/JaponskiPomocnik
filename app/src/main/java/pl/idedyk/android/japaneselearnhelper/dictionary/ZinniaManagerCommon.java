package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.List;

import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerResultItem;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

public abstract class ZinniaManagerCommon {

    public abstract IZinnaManagerCharacter createNewCharacter();

    public static interface IZinnaManagerCharacter {

        public void clear();
        public void destroy();

        public void setWidth(int width);
        public void setHeight(int width);

        public void add(int strokeNo, int x, int y);

        public List<KanjiRecognizerResultItem> recognize(int limit) throws DictionaryException;
    }
}
